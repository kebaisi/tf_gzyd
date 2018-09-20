package com.kbs.consume.operations;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.AudioPlay;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.HttpClient;
import com.kbs.util.RandomUtil;
import com.kbs.util.TF;

public class OperationsDlbDeduction implements IOperationsDeduction{
	private Logger logger=Logger.getLogger(OperationsDlbDeduction.class);
	private MetaDataBO metadataBo=new MetaDataBO();
	ConsumeService consumeService=ConsumeService.getInstance();
	private String pay_type ="2";
	@Override
	public boolean deduction(JSONObject jsonObject) {
		System.out.println("进入微信支付");
		TF.ConsumeJson.put("stage_type", 2);				
		if(TF.ConsumeJson.getString("pay_code").indexOf("13")==0){
			TF.ConsumeJson.put("status","微信***支付中");	
			pay_type="2";
		} else if(TF.ConsumeJson.getString("pay_code").indexOf("28")==0){
			TF.ConsumeJson.put("status","支付宝***支付中");	
			pay_type="3";
		}else{
			return false;
		}
		JSONObject dataJson = consumeData();
		try {
			
			//组装数据
			
			HttpClient httpClient = new HttpClient();
			JSONObject paramsJSON = new JSONObject();
			paramsJSON.put("requestNum", dataJson.getJSONObject("tf_consume_card_record").getString("COR_ID"));
			paramsJSON.put("transamt", dataJson.getJSONObject("tf_consume_card_record").getString("CCR_MONEY"));
			paramsJSON.put("authCode", TF.ConsumeJson.getString("pay_code"));
			paramsJSON.put("clientCode", TF.storeJson.getString("CLIENT_CODE"));
			paramsJSON.put("storeCode", TF.storeJson.getString("STORE_CODE"));
			paramsJSON.put("ORDER_TYPE", "2");
			paramsJSON.put("PAY_TYPE", pay_type);
			String result = httpClient.post(TF.serverUrl+"/pay/reverse/scan",paramsJSON.toJSONString());
			if(result!=null){
				JSONObject resultJson  = JSONObject.parseObject(result);
				if(resultJson.getString("code").equals("0000")){
					insertIntoDB(dataJson, 1);
					consumeService.payComplement(dataJson);
				}else{
					insertIntoDB(dataJson, 2);
					TF.ConsumeJson.put("stage_type", 1);
					TF.ConsumeJson.put("status", resultJson.getString("msg"));
				}
			}else{
				insertIntoDB(dataJson, 2);
				TF.ConsumeJson.put("status", "网络异常");
				TF.ConsumeJson.put("stage_type", 1);			
			}
			
		} catch (Exception e) {
			insertIntoDB(dataJson, 2);
			TF.ConsumeJson.put("stage_type", 1);	
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		
		return true;
	}
	public JSONObject consumeData(){
		JSONObject dataJson=new JSONObject();
		List<Map<String, Object>> list = metadataBo.queryForListMap("select * from tf_discount_record where discount_status='1' and DISCOUNT_TYPE='0'");
		double discount = 1;
		if(list!=null && list.size()!=0){
			JSONObject discountJson  = (JSONObject)JSONObject.toJSON(list.get(0));
			discount = discountJson.getDoubleValue("DISCOUNT_RATE_C1")/100;
		}
	double total_price = 0.0;
	JSONObject orderMap=new JSONObject();
	JSONObject randomJSON = new JSONObject();
	randomJSON.put("length", 10000000);
	orderMap.put("COR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
	orderMap.put("CREATETIME", DateTimeUtil.getNowDate());
	orderMap.put("COR_AMOUNT", TF.ConsumeJson.getString("total_num"));
	orderMap.put("COR_TYPE", "0");
	orderMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
	orderMap.put("U_ID", TF.userJson.getString("U_ID"));
	orderMap.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
	orderMap.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
	dataJson.put("data", new JSONObject());
	
	dataJson.put("tf_consume_details_record", new JSONArray());
	for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
		 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
			  continue;
			  JSONObject detailsJson=new JSONObject();
			  String cdrType=TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("CDR_TYPE");
			  detailsJson.put("CDR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
			  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
			  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
			  detailsJson.put("CDR_NUMBER", "1");
			  if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getDoubleValue("member_price")>0){
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getDoubleValue("member_price"));
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")*discount));
				  }else{
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getDoubleValue("price"));
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")));
				  }
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  detailsJson.put("CDR_NO", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("dishId"));
			  detailsJson.put("CDR_TYPE", 2);
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
			  total_price+=detailsJson.getDoubleValue("CDR_MONEY");
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
			  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*discount));
			  detailsJson.put("CDR_NO", TF.storeConfig.getString("PRICING"));
			  detailsJson.put("CDR_TYPE", 2);
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
			  total_price+=detailsJson.getDoubleValue("CDR_MONEY");
	 }
	for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
		 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
			  continue;
			  JSONObject detailsJson=new JSONObject();
			  detailsJson.put("CDR_ID", System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
			  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
			  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
			 
			  detailsJson.put("CDR_NUMBER", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("num"));
			  if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getDoubleValue("member_price")>0){
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getDoubleValue("member_price"));
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")*discount));
				  }else{
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getDoubleValue("price"));
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")));
				  }
			  detailsJson.put("CDR_NO", id);
			  detailsJson.put("CDR_TYPE", 2);
			  detailsJson.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
			  detailsJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
			  dataJson.getJSONArray("tf_consume_details_record").add(detailsJson);
			  total_price+=detailsJson.getDoubleValue("CDR_MONEY");
	 }
	JSONObject consumeCardMap=new JSONObject();
	consumeCardMap.put("CCR_ID",System.currentTimeMillis()+RandomUtil.getRandom(randomJSON).getString("result"));
	consumeCardMap.put("IC_ID","");
	consumeCardMap.put("U_ID", TF.userJson.getString("U_ID"));
//	consumeCardMap.put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
	consumeCardMap.put("CREATETIME",DateTimeUtil.getNowDate());
	consumeCardMap.put("COR_ID",orderMap.get("COR_ID"));
	consumeCardMap.put("MT_ID", TF.currentMealTimes);
	consumeCardMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
	consumeCardMap.put("CCR_PAY_TYPE", pay_type);
	consumeCardMap.put("CCR_DATABASE_STATUS", "0");
	consumeCardMap.put("CCR_UPLOAD_STATUS", "0");
	consumeCardMap.put("PAY_REMARK", "消费("+TF.storeJson.getString("STORE_NAME")+") 折扣:"+discount);
//	consumeCardMap.put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
//	consumeCardMap.put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
	consumeCardMap.put("ISM_STATUS", "0");
	consumeCardMap.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
	consumeCardMap.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
	consumeCardMap.put("CCR_MONEY",DataConvertUtil.moneryFormat(total_price));
	orderMap.put("COR_MONERY", DataConvertUtil.moneryFormat(total_price));
	dataJson.put("tf_consume_order_record", orderMap);
	dataJson.put("tf_consume_card_record", consumeCardMap);
	return dataJson;
	}
	public void insertIntoDB(JSONObject dataJson,int status){
		System.out.println(dataJson.toJSONString());
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
