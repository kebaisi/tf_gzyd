package com.kbs.consume.operations;

import java.net.InetAddress;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.cashbox.CashBoxUtils;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.RandomUtil;
import com.kbs.util.TF;

public class OperationsFreeDeduction implements IOperationsDeduction{
	private Logger logger=Logger.getLogger(OperationsFreeDeduction.class);
	private MetaDataBO metadataBo=new MetaDataBO();
	private float consumeMoney=(float)0;
	JSONObject tempConsumeJSON=new JSONObject();
	private MetaDataBO metaDataBo=new MetaDataBO();
	public static boolean isConsume=true;
	@Override
	public boolean deduction(JSONObject jsonObject) {
		boolean flag=false;
		try {
			flag=consume(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
//			String msg=e.getMessage().length()>100?e.getMessage().substring(0, 100):e.getMessage();
			logger.error("现金扣费出现异常"+e.getMessage());
			return false;
		}
		return flag;
	}

	public boolean consume(JSONObject jsonObject){
		tempConsumeJSON=new JSONObject();
		tempConsumeJSON=JSONObject.parseObject(TF.ConsumeJson.toJSONString());
		ConsumeService consumeService=ConsumeService.getInstance();
		try{
			//begin
//			TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+ TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price")));
//			TF.ConsumeJson.put("total_num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num"));
			
			
			JSONObject dataJson=consumeData();
			insertIntoDB(dataJson, 1);
			if(TF.dsJson.getBooleanValue("isOpenCashBox")){
				CashBoxUtils cashBoxUtils = CashBoxUtils.getInstance();
				cashBoxUtils.openCashBox();
			}
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("real_price", TF.ConsumeJson.getFloatValue("total_price"));
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("dispenser", "0.0");
			consumeService.payComplement(dataJson);
				boolean pay_result = false;
			
			if(pay_result){
			insertIntoDB(dataJson, 1);
			if(TF.dsJson.getBooleanValue("isOpenCashBox")){
				CashBoxUtils cashBoxUtils = CashBoxUtils.getInstance();
				cashBoxUtils.openCashBox();
			}
			consumeService.payComplement(dataJson);
			}else{
				return false;
			}
			return true;
		
		}catch(Exception e){
			String msg=e.getMessage().length()>100?e.getMessage().substring(0, 100):e.getMessage();
			logger.error("扣费出现异常"+msg);
			return false;
		}
//		return false;
	}
	
	
	
	/**
	 * 组装数据
	 * @param jsonObj
	 * @return
	 */
	public JSONObject consumeData(){
		JSONObject dataJson=new JSONObject();
		JSONObject orderMap=new JSONObject();
		JSONObject randomJSON = new JSONObject();
		randomJSON.put("length", 100);
		orderMap.put("COR_ID", "cor"+TF.storeJson.getString("CLIENT_CODE")+TF.storeJson.getString("STORE_CODE")+TF.dsJson.getString("machineId")+DateTimeUtil.getNowDate("yyyyMMddHHmmss")+RandomUtil.getRandom(randomJSON).getString("result"));
		System.out.println(orderMap.getString("COR_ID"));
		TF.ConsumeJson.put("cor_id", orderMap.get("COR_ID"));
		orderMap.put("CREATETIME", DateTimeUtil.getNowDate());
		orderMap.put("COR_AMOUNT", TF.ConsumeJson.getString("total_num"));
//		orderMap.put("COR_TYPE", "0");
		orderMap.put("MACHINE_NO",TF.meterJson.getString("MACHINE_NO"));
		orderMap.put("COR_MONERY", TF.ConsumeJson.getString("total_price"));
		orderMap.put("U_ID", TF.userJson.getString("U_ID"));
		dataJson.put("data", new JSONObject());
		dataJson.getJSONObject("data").put("tf_consume_order_record", orderMap);
		dataJson.getJSONObject("data").put("tf_consume_details_record", new JSONArray());
		for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
			 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
				  continue;
				  JSONObject detailsJson=new JSONObject();
				  detailsJson.put("CDR_ID", "cdr"+TF.storeJson.getString("CLIENT_CODE")+TF.storeJson.getString("STORE_CODE")+TF.dsJson.getString("machineId")+DateTimeUtil.getNowDate("yyyyMMddHHmmss")+RandomUtil.getRandom(randomJSON).getString("result"));
				  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
				  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("price"));
				  detailsJson.put("CDR_NUMBER", "1");
				  detailsJson.put("CDR_MONEY", detailsJson.getString("CDR_UNIT_PRICE"));
				  detailsJson.put("CDR_NO", id);
				  detailsJson.put("CDR_TYPE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(id).getString("CDR_TYPE"));
				  dataJson.getJSONObject("data").getJSONArray("tf_consume_details_record").add(detailsJson);
		 }
		for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
			 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
				  continue;
				  JSONObject detailsJson=new JSONObject();
				  detailsJson.put("CDR_ID", "cdr"+TF.storeJson.getString("CLIENT_CODE")+TF.storeJson.getString("STORE_CODE")+TF.dsJson.getString("machineId")+DateTimeUtil.getNowDate("yyyyMMddHHmmss")+RandomUtil.getRandom(randomJSON).getString("result"));
				  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
				  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(id).getString("price"));
				  detailsJson.put("CDR_NUMBER", "1");
				  detailsJson.put("CDR_MONEY", detailsJson.getString("CDR_UNIT_PRICE"));
				  detailsJson.put("CDR_NO", id);
				  detailsJson.put("CDR_TYPE", 1);
				  dataJson.getJSONObject("data").getJSONArray("tf_consume_details_record").add(detailsJson);
		 }
		for(String id:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
			 if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("pay_status")==1)
				  continue;
				  JSONObject detailsJson=new JSONObject();
				  detailsJson.put("CDR_ID", "cdr"+TF.storeJson.getString("CLIENT_CODE")+TF.storeJson.getString("STORE_CODE")+TF.dsJson.getString("machineId")+DateTimeUtil.getNowDate("yyyyMMddHHmmss")+RandomUtil.getRandom(randomJSON).getString("result"));
				  detailsJson.put("COR_ID", orderMap.get("COR_ID"));
				  detailsJson.put("CREATETIME", orderMap.get("CREATETIME"));
				  detailsJson.put("CDR_UNIT_PRICE", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getString("price"));
				  detailsJson.put("CDR_NUMBER", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("num"));
				  detailsJson.put("CDR_MONEY", detailsJson.getFloatValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER"));
				  detailsJson.put("CDR_NO", id);
				  detailsJson.put("CDR_TYPE", 2);
				  dataJson.getJSONObject("data").getJSONArray("tf_consume_details_record").add(detailsJson);
		 }
		JSONObject consumeCardMap=new JSONObject();
		consumeCardMap.put("CCR_ID","ccr"+TF.storeJson.getString("CLIENT_CODE")+TF.storeJson.getString("STORE_CODE")+TF.dsJson.getString("machineId")+DateTimeUtil.getNowDate("yyyyMMddHHmmss")+RandomUtil.getRandom(randomJSON).getString("result"));
		consumeCardMap.put("IC_ID","0");
		consumeCardMap.put("IC_SERIAL_NUMBER","0");
		consumeCardMap.put("U_ID", TF.userJson.getString("U_ID"));
		consumeCardMap.put("CCR_ORIGINALAMOUNT",DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")));
		consumeCardMap.put("CCR_MONEY",DataConvertUtil.moneryFormat(tempConsumeJSON.getFloatValue("total_price")));
		consumeCardMap.put("CREATETIME",DateTimeUtil.getNowDate());
		consumeCardMap.put("COR_ID",orderMap.get("COR_ID"));
		consumeCardMap.put("MT_ID", TF.currentMealTimes);
		consumeCardMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
		consumeCardMap.put("CCR_PAY_TYPE", 4);
		consumeCardMap.put("CCR_STATUS","1");
		consumeCardMap.put("CCR_DATABASE_STATUS", "0");
		consumeCardMap.put("CCR_UPLOAD_STATUS", "0");
		dataJson.getJSONObject("data").put("tf_consume_card_record", consumeCardMap);
		dataJson.put("machine_no", consumeCardMap.getString("MACHINE_NO"));
		dataJson.put("IC_ID", consumeCardMap.getString("IC_ID"));
		dataJson.put("MONEY", consumeCardMap.getString("CCR_MONEY"));
		return dataJson;
	}
	
	/**
	 * 入库
	 * @param dataJson
	 */
	public void insertIntoDB(JSONObject dataJson,int status){
//		if(TF.checkStatus.getIntValue("checkStatus")==1){
//			dataJson.getJSONObject("data").getJSONObject("tf_consume_card_record").put("CCR_STATUS",2);
//		}else{
			dataJson.getJSONObject("data").getJSONObject("tf_consume_card_record").put("CCR_STATUS",status);
//		}
		metadataBo.execute(metadataBo.toSql(dataJson.getJSONObject("data").getJSONObject("tf_consume_card_record"), "tf_consume_card_record"));
		metadataBo.execute(metadataBo.toSql(dataJson.getJSONObject("data").getJSONObject("tf_consume_order_record"), "tf_consume_order_record"));
		for(Object obj:dataJson.getJSONObject("data").getJSONArray("tf_consume_details_record")){
			metadataBo.execute(metadataBo.toSql((JSONObject)obj, "tf_consume_details_record"));
		}
	}

}
