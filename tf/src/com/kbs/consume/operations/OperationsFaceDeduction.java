package com.kbs.consume.operations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.ic.IcUtils;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.MyException;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.AudioPlay;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.HttpClient;
import com.kbs.util.RandomUtil;
import com.kbs.util.SpeakUtil;
import com.kbs.util.TF;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class OperationsFaceDeduction implements IOperationsDeduction {
	public Logger logger=Logger.getLogger(OperationsFaceDeduction.class);
	private MetaDataBO metadataBo=new MetaDataBO();
	
	private HttpClient httpClient=new HttpClient(5);
	ConsumeService consumeService=ConsumeService.getInstance();
	long priviousTime=0;
	private boolean flag=false;
	private SpeakUtil speakUtil=new SpeakUtil();
	private String face_code = "";
	@Override
	public synchronized boolean deduction(JSONObject jsonObject) {
		long startTime = System.currentTimeMillis();
		try {
			
			face_code = TF.ConsumeJson.getString("face_code");
			TF.ConsumeJson.put("face_code", "");
			List<Map<String, Object>> list = metadataBo.queryForListMap("SELECT * from tf_cardinfo inner join tf_memberinfo on tf_cardinfo.MI_ID = tf_memberinfo.MI_ID where tf_memberinfo.MI_PHONE='"+face_code+"' and tf_cardinfo.IC_TYPE='1'");
			if(list==null || list.size()==0){
				TF.ConsumeJson.put("status", "无效身份");
				 speakUtil.speak("无效身份");
				 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}else{
				face_code = list.get(0).get("IC_ID").toString();
			if(TF.ConsumeJson.getIntValue("stage_type")==1){
				TF.ConsumeJson.put("stage_type", 2);
				TF.ConsumeJson.put("status","支付中");	
//				onlineFindMember();
		
				//计算折扣
				double total_price = 0.0;
				double sale = 0.0;
				if(TF.storeConfig.getString("DISCOUNT").equals("0")){
					total_price =TF.ConsumeJson.getDoubleValue("total_price")*TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getDoubleValue("DISCOUNT_RATE");
				}else{
					total_price = TF.ConsumeJson.getDoubleValue("total_price");
				}
				//是否四舍五入
				if(TF.storeConfig.getIntValue("ROUND")==0){
					TF.ConsumeJson.put("total_price", Math.round(total_price));
				}else{
					TF.ConsumeJson.put("total_price",DataConvertUtil.moneryFormat(total_price));
				}
				JSONObject dataJson = consumeData();
				
				if(TF.checkStatus.getIntValue("checkStatus") == 1){
					onlineConsume(dataJson);
				}else{
					inline(dataJson);
				}
			}else{
				if(TF.checkStatus.getIntValue("checkStatus") == 1){
					onlineFindMember();
				}else{
					findMemberInfo();
				}
				
			
			}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("耗时:"+(System.currentTimeMillis()- startTime ));
			
		//check ic is exits
			
		
		return true;
	}
	public boolean onlineFindMember(){
		//查询会员卡信息
//		JSONObject paramsJson= new JSONObject();
//		paramsJson.put("icId", ic_code);
		String url = "";
		if(TF.storeConfig.getString("IS_MEMBER_JOIN").equals("1")){
			List<Map<String, Object>> list = metadataBo.queryForListMap("select * from tf_cardinfo where ic_id='"+face_code+"'");
			if(null!=list && list.size()>0){
				url = TF.serverUrl+"/member/info/"+list.get(0).get("CLIENT_CODE")+"/"+list.get(0).get("STORE_CODE")+"/icId/"+face_code;
			}else{
				TF.ConsumeJson.put("status", "该会员不存在");
				return false;
			}
		}else{
			url = TF.serverUrl+"/member/info/"+TF.storeJson.getString("CLIENT_CODE")+"/"+TF.storeJson.getString("STORE_CODE")+"/icId/"+face_code;
		}
		String result = httpClient.get(url);
		if(null!=result){
			JSONObject resultJson = JSONObject.parseObject(result);
			if(resultJson.getBooleanValue("result")){
				displayMemberInfo(resultJson);
			return true;
			}else{
				TF.ConsumeJson.put("status", resultJson.getString("msg"));
				return false;
			}
	}else{
		TF.ConsumeJson.put("status", "网络异常");
		return false;
	}
	}
	public void displayMemberInfo(JSONObject dataJson){
		JSONObject memberJson = new JSONObject();
		memberJson.put("balance",dataJson.getJSONObject("data").getJSONArray("account").getJSONObject(0).getFloatValue("BALANCE")+dataJson.getJSONObject("data").getJSONArray("account").getJSONObject(1).getFloatValue("BALANCE"));
		memberJson.put("cash", dataJson.getJSONObject("data").getJSONArray("account").getJSONObject(0));
		memberJson.put("sub", dataJson.getJSONObject("data").getJSONArray("account").getJSONObject(1));
		TF.ConsumeJson.getJSONObject("deduction").put("member", memberJson);
	}
	public void onlineConsume(JSONObject dataJson) throws IOException{
		Long start = System.currentTimeMillis();
		//组装支付数据
		try {
		String payResult = httpClient.post(TF.serverUrl+"/consume/online", dataJson.toJSONString());
		if(null!=payResult){
			JSONObject resultJson = JSONObject.parseObject(payResult);
			if(resultJson.getString("code").equals("0000")){
				displayMemberInfo(resultJson);
				dataJson.getJSONObject("tf_consume_card_record").put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
				dataJson.getJSONObject("tf_consume_card_record").put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
				dataJson.getJSONObject("tf_consume_card_record").put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
				dataJson.getJSONObject("tf_consume_card_record").put("CCR_UPLOAD_STATUS", "1");
				dataJson.getJSONObject("tf_consume_card_record").put("ELAPSEDTIME", System.currentTimeMillis()-start);
				insertIntoDB(dataJson, 1);
				consumeService.payComplement(dataJson);
				IcUtils.getInstance().stopReadCard(TF.icJson);
			}else{
				TF.ConsumeJson.put("stage_type", "1");
				TF.ConsumeJson.put("status", resultJson.getString("msg"));
				speakUtil.speak(resultJson.getString("msg"));
				Thread.sleep(3000);
			}
			
		}else{
			TF.ConsumeJson.put("status", "网络异常,正在校验该支付。");
			//系统将使用20S时间去查询服务器该订单是否已经支付成功
			boolean f = false;
			for(int i=0;i<5;i++){
			String queryResult = httpClient.get(TF.serverUrl+"/consume/order/payresult/"+TF.storeJson.getString("CLIENT_CODE")+"/"+TF.storeJson.getString("STORE_CODE")+"/"+dataJson.getJSONObject("tf_consume_card_record").getString("COR_ID"));
			if(null!=queryResult){
//				System.out.println(TF.serverUrl+"/order/payresult/"+TF.storeJson.getString("CLIENT_CODE")+"/"+TF.storeJson.getString("STORE_CODE")+"/"+dataJson.getJSONObject("tf_consume_card_record").getString("COR_ID"));
//				System.out.println(dataJson.getJSONObject("tf_consume_card_record").toJSONString());
//				System.out.println(queryResult);
				JSONObject queryJson = JSONObject.parseObject(queryResult);
				if(queryJson.getBooleanValue("result")){
					f =true;
					onlineFindMember();
					dataJson.getJSONObject("tf_consume_card_record").put("CCR_UPLOAD_STATUS", "1");
					dataJson.getJSONObject("tf_consume_card_record").put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
					dataJson.getJSONObject("tf_consume_card_record").put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
					dataJson.getJSONObject("tf_consume_card_record").put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
					dataJson.getJSONObject("tf_consume_card_record").put("ELAPSEDTIME", System.currentTimeMillis()-start);
					insertIntoDB(dataJson, 1);
					consumeService.payComplement(dataJson);
					IcUtils.getInstance().stopReadCard(TF.icJson);
					break;
				}else{
					TF.ConsumeJson.put("status", "支付失败，请重新支付");
					TF.ConsumeJson.put("stage_type", "1");
					break;
				}
			}else{
				TF.ConsumeJson.put("status", "第 "+i+"次校验失败。");
			}
			Thread.sleep(1000);
			}
			if(!f){
				TF.ConsumeJson.put("status", "开始本地扣费..");
				findMemberInfo();
				inline(dataJson);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		} catch (Exception e) {
			// TODO: handle exception 
			TF.ConsumeJson.put("stage_type", "1");
			e.printStackTrace();
		}
	}
	public boolean inline(JSONObject dataJson) throws IOException{
		Long start = System.currentTimeMillis();
		if(!findMemberInfo()){ return false;}
		if(!checkMember()){ return false;}
		if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance")<dataJson.getJSONObject("tf_consume_card_record").getDoubleValue("CCR_MONEY")){
			TF.ConsumeJson.put("status", "余额不足");
			speakUtil.speak("余额不足");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TF.ConsumeJson.put("stage_type", 1);
			return false;
		}
		BigDecimal order_price = new BigDecimal(dataJson.getJSONObject("tf_consume_card_record").getDoubleValue("CCR_MONEY"));
		
		//start consume
		if(TF.storeConfig.getIntValue("STORE_TYPE") == 1){
			if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getDoubleValue("BALANCE")>=dataJson.getJSONObject("tf_consume_card_record").getDoubleValue("CCR_MONEY")){
				BigDecimal sub_balance = new BigDecimal(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getDoubleValue("BALANCE"));
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(sub_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getString("ACCOUNT_ID")+"'");
			}else{
				BigDecimal cash_balance = new BigDecimal(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getDoubleValue("BALANCE"));
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='0.0' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("ACCOUNT_ID")+"'");
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(cash_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID")+"'");
			}
			
		}else{
			if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getDoubleValue("BALANCE")>=dataJson.getJSONObject("tf_consume_card_record").getDoubleValue("CCR_MONEY")){
				BigDecimal cash_balance = new BigDecimal(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getDoubleValue("BALANCE"));
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(cash_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID")+"'");
				System.out.println("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(cash_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID")+"'");
			}else{
				BigDecimal sub_balance = new BigDecimal(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getDoubleValue("BALANCE"));
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='0.0' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("ACCOUNT_ID")+"'");
				metadataBo.execute("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(sub_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getString("ACCOUNT_ID")+"'");
				System.out.println("UPDATE tf_member_account_record SET BALANCE='"+DataConvertUtil.moneryFormat(sub_balance.subtract(order_price).doubleValue())+"' where account_id='"+TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("sub").getString("ACCOUNT_ID")+"'");
			}
		}
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").put("balance", DataConvertUtil.moneryFormat(new BigDecimal(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getDoubleValue("balance")).subtract(order_price).doubleValue()));
			
			dataJson.getJSONObject("tf_consume_card_record").put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
			dataJson.getJSONObject("tf_consume_card_record").put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
			dataJson.getJSONObject("tf_consume_card_record").put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
			dataJson.getJSONObject("tf_consume_card_record").put("CCR_UPLOAD_STATUS", "0");
			dataJson.getJSONObject("tf_consume_card_record").put("ELAPSEDTIME", System.currentTimeMillis()-start);
			insertIntoDB(dataJson, 1);
			consumeService.payComplement(dataJson);
			IcUtils.getInstance().stopReadCard(TF.icJson);
		return true;
	}
	public JSONObject consumeData(){
	JSONObject dataJson=new JSONObject();
	JSONObject orderMap=new JSONObject();
	JSONObject randomJSON = new JSONObject();
	randomJSON.put("length", 1000000);
	orderMap.put("COR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
	orderMap.put("CREATETIME", DateTimeUtil.getNowDate());
	orderMap.put("COR_AMOUNT", TF.ConsumeJson.getString("total_num"));
	orderMap.put("COR_TYPE", "0");
	orderMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
	orderMap.put("COR_MONERY", TF.ConsumeJson.getString("total_price"));
	orderMap.put("U_ID", TF.userJson.getString("U_ID"));
	orderMap.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
	orderMap.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
	dataJson.put("data", new JSONObject());
	dataJson.put("tf_consume_order_record", orderMap);
	dataJson.put("tf_consume_details_record", new JSONArray());
	for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
		 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
			  continue;
			  JSONObject detailsJson=new JSONObject();
			  String cdrType=TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("CDR_TYPE");
			  detailsJson.put("CDR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
			  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
			  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
			  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("price"));
			  detailsJson.put("CDR_NUMBER", "1");
			  detailsJson.put("CDR_MONEY", detailsJson.getString("CDR_UNIT_PRICE"));
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  if(cdrType.equals("4")){
				  detailsJson.put("CDR_NO", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("dishId"));
			  }else{
				  detailsJson.put("CDR_NO", id);
			  }
			  detailsJson.put("CDR_TYPE", cdrType);
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
	 }
	for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
		 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
			  continue;
			  JSONObject detailsJson=new JSONObject();
			  detailsJson.put("CDR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
			  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
			  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
			  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(id).getString("price"));
			  detailsJson.put("CDR_NUMBER", "1");
			  detailsJson.put("CDR_MONEY", detailsJson.getString("CDR_UNIT_PRICE"));
			  detailsJson.put("CDR_NO", id);
			  detailsJson.put("CDR_TYPE", 1);
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
	 }
	for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
		 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
			  continue;
			  JSONObject detailsJson=new JSONObject();
			  detailsJson.put("CDR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
			  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
			  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
			  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getString("price"));
			  detailsJson.put("CDR_NUMBER", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("num"));
			  detailsJson.put("CDR_MONEY", detailsJson.getFloatValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER"));
			  detailsJson.put("CDR_NO", id);
			  detailsJson.put("CDR_TYPE", 2);
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
	 }
	JSONObject consumeCardMap=new JSONObject();
	consumeCardMap.put("CCR_ID",System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
	
	consumeCardMap.put("IC_ID",face_code);
	consumeCardMap.put("U_ID", TF.userJson.getString("U_ID"));
//	consumeCardMap.put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
	consumeCardMap.put("CCR_MONEY",TF.ConsumeJson.getFloatValue("total_price"));
	consumeCardMap.put("CREATETIME",DateTimeUtil.getNowDate());
	consumeCardMap.put("COR_ID",orderMap.get("COR_ID"));
	consumeCardMap.put("MT_ID", TF.currentMealTimes);
	consumeCardMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
	consumeCardMap.put("CCR_PAY_TYPE", 1);
	consumeCardMap.put("CCR_DATABASE_STATUS", "0");
	consumeCardMap.put("CCR_UPLOAD_STATUS", "0");
	consumeCardMap.put("PAY_REMARK", "消费("+TF.storeJson.getString("STORE_NAME")+")");
//	consumeCardMap.put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
//	consumeCardMap.put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
	consumeCardMap.put("ISM_STATUS", "0");
	consumeCardMap.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
	consumeCardMap.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
	dataJson.put("tf_consume_card_record", consumeCardMap);
	return dataJson;
	}
	public boolean checkMember(){
		if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getIntValue("MI_STATUS")==1){
			TF.ConsumeJson.put("status", "该会员已注销");
			speakUtil.speak("该会员已注销");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}else if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getIntValue("ACCOUNT_STATUS")==1){
			TF.ConsumeJson.put("status", "该账户被冻结");
			 speakUtil.speak("该账户被冻结");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}else if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getIntValue("ACCOUNT_STATUS")==2){
			TF.ConsumeJson.put("status", "该账户已注销");
			 speakUtil.speak("该账户已注销");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}else if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getIntValue("IC_STATUS")==1){
			TF.ConsumeJson.put("status", "挂失卡");
			 speakUtil.speak("挂失卡");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}else if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getIntValue("IC_STATUS")==2){
			TF.ConsumeJson.put("status", "注销卡");
			 speakUtil.speak("注销卡");
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			return false;
		}else{
			return true;
		}
	}
	public boolean findMemberInfo(){
//		System.out.println("SELECT * FROM tf_cardinfo INNER JOIN tf_memberinfo ON tf_cardinfo.MI_ID = tf_memberinfo.MI_ID INNER JOIN tf_member_account_record ON tf_memberinfo.MI_ID = tf_member_account_record.MI_ID WHERE tf_cardinfo.IC_ID='"+TF.ConsumeJson.getString("ic_code")+"'");
		System.out.println("SELECT * FROM  tf_memberinfo  INNER JOIN tf_member_account_record ON tf_memberinfo.MI_ID = tf_member_account_record.MI_ID WHERE tf_memberinfo.MI_PHONE='"+face_code+"'");
		List<Map<String, Object>> accountList = metadataBo.queryForListMap("SELECT * FROM tf_cardinfo INNER JOIN tf_memberinfo ON tf_cardinfo.MI_ID = tf_memberinfo.MI_ID INNER JOIN tf_member_account_record ON tf_memberinfo.MI_ID = tf_member_account_record.MI_ID WHERE tf_cardinfo.IC_ID='"+face_code+"'");
			if(null!=accountList && accountList.size()>0){
					JSONArray dataArray = (JSONArray)JSON.toJSON(accountList);
					JSONObject memberJson = new JSONObject();
					memberJson.put("balance",dataArray.getJSONObject(0).getFloatValue("BALANCE")+dataArray.getJSONObject(1).getFloatValue("BALANCE"));
//					for(Map<String, Object> map:accountList){
//						if(map.get("ACCOUNT_TYPE").equals("0")){
//							cashAccountJson.put("cash_balance", map.get("BALANCE"));
//						}else{
//							cashAccountJson.put("sub_balance", map.get("BALANCE"));
//						}
//					}
					memberJson.put("cash", dataArray.getJSONObject(0));
					memberJson.put("sub", dataArray.getJSONObject(1));
					TF.ConsumeJson.getJSONObject("deduction").put("member", memberJson);
					return true;
			}else{
				TF.ConsumeJson.put("status", "非法卡");
				 AudioPlay.playAudio(ConsumeService.ILLEGALCARD);
				 try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				  speakUtil.speak("非法卡");
				return false;
			}
		}
	public void insertIntoDB(JSONObject dataJson,int status){
//		if(TF.checkStatus.getIntValue("checkStatus")==1){
//			dataJson.getJSONObject("data").getJSONObject("tf_consume_card_record").put("CCR_STATUS",2);
//		}else{
			dataJson.getJSONObject("tf_consume_card_record").put("CCR_STATUS",status);
//		}
		metadataBo.execute(metadataBo.toSql(dataJson.getJSONObject("tf_consume_card_record"), "tf_consume_card_record"));
		metadataBo.execute(metadataBo.toSql(dataJson.getJSONObject("tf_consume_order_record"), "tf_consume_order_record"));
		for(Object obj:dataJson.getJSONArray("tf_consume_details_record")){
			metadataBo.execute(metadataBo.toSql((JSONObject)obj, "tf_consume_details_record"));
		}
	}

}

