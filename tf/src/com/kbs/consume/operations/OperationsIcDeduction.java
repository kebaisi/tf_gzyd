package com.kbs.consume.operations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

public class OperationsIcDeduction implements IOperationsDeduction {
	public Logger logger=Logger.getLogger(OperationsIcDeduction.class);
	private MetaDataBO metadataBo=new MetaDataBO();
	IcUtils icUtil=IcUtils.getInstance();
	private HttpClient httpClient=new HttpClient(5);
	ConsumeService consumeService=ConsumeService.getInstance();
	long priviousTime=0;
	private boolean flag=false;
	private SpeakUtil speakUtil=new SpeakUtil();
	private String ic_code = "";
	private String ic_hex_code="";
	public String getConsumeCmd(){
		
		;
		String trade_no = Integer.toHexString(TF.trade_no);
		if(trade_no.length()>4 || trade_no.equals("FFFF")){
			TF.trade_no =1;
			trade_no = Integer.toHexString(TF.trade_no);
		}
		if(trade_no.length()%2!=0){
			trade_no="0"+trade_no;
		}
		if(trade_no.length()!=4){
			trade_no = trade_no+"00";
		}
				
		String cont = "0300"+trade_no+"0501"+ic_hex_code;
		int money = (int)(TF.ConsumeJson.getFloatValue("total_price")*100);
//		int money = (int)(TF.ConsumeJson.getFloatValue("total_price"));
//		int money = 1;
		String ts = icUtil.hexTwoData(ClcUtil.decToHex(money));
//		icUtil.hexTwoData(ClcUtil.decToHex(money));
		for(int i=ts.length();i<8;i++){
			ts=ts+"0";
		}
		cont = cont+ts+"3000000000000000000000000000000000000000";
		return ClcUtil.getSt(cont);
		
	}
	public double getBalance(String hex){
//		if(hex.indexOf("00")==0){
//			hex.replace("00", "");
//		}
//		hex = hex.replaceAll("00", "");
		if(hex.length()%2!=0){
			hex=hex+"0";
		}
		String dd = "";
		int k=1;
		for(int i=1;i<=hex.length();i++){
			if(i%2==0){
				dd=dd+hex.substring(hex.length()-k*2, hex.length()-k*2+2);
				k++;
			}
		}
		dd= dd.replaceAll("00", "");
		double cc = Double.parseDouble(String.valueOf(Integer.parseInt(dd, 16)));
		return cc/100;
	}
	public boolean readCard(String cmd){
		boolean f =false;
		try {
		String ic_id = cmd.substring(32, 34)+cmd.substring(30, 32)+cmd.substring(28, 30)+cmd.substring(26, 28);
		TF.ConsumeJson.put("ic_code", Long.parseLong(ic_id, 16));
		TF.ConsumeJson.put("ic_hex_code", cmd.substring(26, 34));
		ic_code = TF.ConsumeJson.getString("ic_code");
		ic_hex_code = TF.ConsumeJson.getString("ic_hex_code");
		TF.ConsumeJson.put("ic_code", "");
		TF.ConsumeJson.getJSONObject("deduction").put("member",new JSONObject());
		String money = cmd.substring(34,42);
		String ic_status="正常卡";
		if(money.equals("00000000")){
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").put("balance", "0.0");
		}else{
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").put("balance",getBalance(money));
		}
		if(cmd.substring(22,24).equals("01")){
			TF.ConsumeJson.put("status","过期卡");
			speakUtil.speak("过期卡");
			ic_status="过期卡"; 
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("02")){
			TF.ConsumeJson.put("status","冻结卡");
			speakUtil.speak("冻结卡");
			ic_status="冻结卡"; 
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("03")){
			TF.ConsumeJson.put("status","挂失卡");
			speakUtil.speak("挂失卡");
			ic_status="挂失卡"; 
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("04")){
			TF.ConsumeJson.put("status","身份受限卡");
			speakUtil.speak("身份受限卡");
			ic_status="身份受限卡"; 
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("05")){
			TF.ConsumeJson.put("status","刷卡次数超限卡");
			speakUtil.speak("刷卡次数超限卡");
			ic_status="刷卡次数超限卡";
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("06")){
			TF.ConsumeJson.put("status","未注册卡");
			ic_status="未注册卡";
			speakUtil.speak("未注册卡");
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("07")){
			TF.ConsumeJson.put("status","销户卡");
			ic_status="销户卡";
			speakUtil.speak("销户卡");
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("08")){
			TF.ConsumeJson.put("status","设备运行状态不支持当前账户");
			ic_status="设备运行状态不支持当前账户";
			speakUtil.speak("设备运行状态不支持当前账户");
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("09")){
			TF.ConsumeJson.put("status","重复二维码");
			ic_status="重复二维码";
			speakUtil.speak("重复二维码");
			Thread.sleep(1000);
		}else if(cmd.substring(22,24).equals("00")){
			f=true;
		}
		consumeService.ic_cmd(cmd, ic_code, "HTPAP-->PTPE[ 有卡信息推送]", DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+" 卡片状态: "+cmd.substring(22,24)+"("+ic_status+") :账户类型:"+cmd.substring(24,26)+"  卡号:"+ic_code+" 卡余额:"+getBalance(money), 0);
		String mcd = ClcUtil.getSt("0500"+cmd.substring(cmd.length()-18,cmd.length()-16)+"0003010000");
		icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).send(icUtil.getHexArray(mcd));
		consumeService.ic_cmd(mcd, ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+" PTPE-->HTPAP[ 有卡信息推送]", "", 0);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	
	@Override
	public synchronized boolean deduction(JSONObject jsonObject) {
		long startTime = System.currentTimeMillis();
		try {
			if(readCard(jsonObject.getJSONObject("data").getString("IC_ID"))){
			
				if(TF.ConsumeJson.getIntValue("stage_type")==1){
					TF.ConsumeJson.put("stage_type", 2);
					TF.ConsumeJson.put("status","会员卡***支付中");
					String consume_cmd = getConsumeCmd();
					consumeService.ic_cmd(consume_cmd, ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+" PTPE-->HTPAP[申请交易]", "卡号:"+ic_code+" 交易金额:"+TF.ConsumeJson.getFloatValue("total_price"), 0);
					 Long start = System.currentTimeMillis();
					icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).send(icUtil.getHexArray(consume_cmd));
					int t =10;  
					for(int i=0;i<500;i++){
						  if(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")>=0 && icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0601")>=0 && (icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().length()-icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218"))>=56){
							  break;
						  }else{
							  Thread.sleep(20);
						  }
						  if(i%50==0){
							  TF.ConsumeJson.put("status","申请交易中--"+t+"S");
							  t=t-1;
						  }
					  }
					if(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0501")>0){
						consumeService.ic_cmd(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0501")-18,icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0501")+10),ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+ " HTPAP-->PTPE[申请交易]", "卡号:"+ic_code+" 交易金额:"+TF.ConsumeJson.getFloatValue("total_price"), (System.currentTimeMillis()-start));
						if(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("05010000")<0){
							 TF.ConsumeJson.put("stage_type", 1);
							TF.ConsumeJson.put("status","交易申请失败");
							 speakUtil.speak("交易申请失败");
								Thread.sleep(1000);
								return false;
						}
					}else{
						  TF.ConsumeJson.put("stage_type", 1);
							consumeService.ic_cmd("交易超时",ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+ " 系统判定(10S)：[交易结果]", "交易超时", (System.currentTimeMillis()-start)); 
							TF.ConsumeJson.put("status","交易超时");
							 speakUtil.speak("交易超时");
							 Thread.sleep(1000);
							return false;
					}
					if(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")>=0 && icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0601")>0 && (icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().length()-icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218"))>=56){

						String consume_status = "";
					  switch (icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0601")+4, icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0601")+8)) {
							case "08a0":
								TF.ConsumeJson.put("status","写卡失败");
								consume_status = "写卡失败";
								 speakUtil.speak("写卡失败");
								 Thread.sleep(1000);
								break;
							case "01a0":
								TF.ConsumeJson.put("status","过期卡");	
								consume_status = "过期卡";
								 speakUtil.speak("过期卡");
								 Thread.sleep(1000);
								break;
							case "02a0":
								TF.ConsumeJson.put("status","冻结卡");	
								consume_status = "冻结卡";
								 speakUtil.speak("冻结卡");
								 Thread.sleep(1000);
								break;
							case "03a0":
								TF.ConsumeJson.put("status","挂失卡");	
								consume_status = "挂失卡";
								 speakUtil.speak("挂失卡");
								 Thread.sleep(1000);
								break;
							case "04a0":
								TF.ConsumeJson.put("status","身份受限卡");	
								consume_status = "身份受限卡";
								 speakUtil.speak("身份受限卡");
								 Thread.sleep(1000);
								break;
							case "05a0":
								TF.ConsumeJson.put("status","刷卡超卡限");	
								consume_status = "刷卡超卡限";
								 speakUtil.speak("刷卡超卡限");
								 Thread.sleep(1000);
								break;
							case "06a0":
								TF.ConsumeJson.put("status","未注册卡");
								consume_status = "未注册卡";
								 speakUtil.speak("未注册卡");
								 Thread.sleep(1000);
								break;
							case "07a0":
								TF.ConsumeJson.put("status","卡余额不足");	
								consume_status = "卡余额不足";
								 speakUtil.speak("卡余额不足");
								 Thread.sleep(1000);
								break;
							case "09a0":
								TF.ConsumeJson.put("status","交易超时");	
								consume_status = "交易超时";
								 speakUtil.speak("交易超时");
								 Thread.sleep(1000);
								break;
							case "0ea0":
								TF.ConsumeJson.put("status","交易密码错");	
								consume_status = "交易密码错";
								 speakUtil.speak("交易密码错");
								 Thread.sleep(1000);
								break;
							case "0da0":
								TF.ConsumeJson.put("status","未知错误");
								consume_status = "未知错误";
								 speakUtil.speak("未知错误");
								 Thread.sleep(1000);
								break;
							case "0000":
								consume_status = "交易成功";
								TF.ConsumeJson.put("status","支付成功");
								TF.ConsumeJson.getJSONObject("deduction").put("member",new JSONObject());
								TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").put("balance", getBalance(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+44,icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+52)));
								JSONObject dataJson  = consumeData();
								consumeService.payComplement(dataJson);
								consumeService.ic_cmd(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218"),icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+56),ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+ " HTPAP-->PTPE[交易结果推送]", "交易结果:"+consume_status+"卡号:"+ic_code+" 交易金额:"+TF.ConsumeJson.getFloatValue("total_price")+"卡内余额:"+getBalance(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+44,icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+52)),(System.currentTimeMillis()-start));
								JSONObject cardJson=dataJson.getJSONObject("tf_consume_card_record");
								cardJson.put("CCR_UPLOAD_STATUS", "0");
								cardJson.put("CCR_STATUS", "1");
								cardJson.put("ELAPSEDTIME", 0);
								cardJson.put("CREATETIME", DateTimeUtil.changeMillsToTime(cardJson.getLongValue("CREATETIME")));
								String re_ic_id=icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(60, 62)+
										icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(58, 60)+
										icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(56, 58)+
										icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(54, 56);
								List<Map<String,Object>> list = metadataBo.queryForListMap("select * from  tf_cardinfo where ic_id='"+Long.parseLong(re_ic_id, 16)+"'");
								if(null!=list && list.size()>0){
									cardJson.put("MI_ID", list.get(0).get("MI_ID"));
								}
								insertIntoDB(dataJson, 1);
								return true;
							}
					  TF.ConsumeJson.put("stage_type", 1);
					  consumeService.ic_cmd(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218"),icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0218")+56),ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+ " HTPAP-->PTPE[交易结果推送]", "交易结果:"+consume_status+"卡号:"+ic_code+" 交易金额:"+TF.ConsumeJson.getFloatValue("total_price")+"卡内余额:"+getBalance(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().substring(72, 80)),(System.currentTimeMillis()-start));
					  
					  
						 // String mcd = icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg();
						//	icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).send(icUtil.getHexArray(ClcUtil.getSt("0500"+mcd.substring(mcd.length()-18,mcd.length()-16)+"0004010000")));
						
						
						
					
					}else{
						  TF.ConsumeJson.put("stage_type", 1);
						consumeService.ic_cmd("交易超时",ic_code, DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+ " 系统判定(10S)：[交易结果]", "交易超时", (System.currentTimeMillis()-start)); 
						TF.ConsumeJson.put("status","交易超时");
						 speakUtil.speak("交易超时");
						 Thread.sleep(1000);
						 
						return false;
					}
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			}
			
			
				
				
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			 if(TF.ConsumeJson.getString("stage_type").equals("2")){
				 TF.ConsumeJson.put("stage_type", "1");
			 }
		}
		System.out.println("耗时:"+(System.currentTimeMillis()- startTime ));
			
		return true;
	}
	public JSONObject consumeData(){
		JSONObject dataJson=new JSONObject();
		dataJson.put("result", true);
		try {
		JSONObject randomJSON = new JSONObject();
		randomJSON.put("length", 1000000);
		JSONObject orderMap=new JSONObject();
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
		double total_price = 0.0;
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
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")));
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
				  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")));
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
					  detailsJson.put("CDR_MONEY", DataConvertUtil.moneryFormat(detailsJson.getDoubleValue("CDR_UNIT_PRICE")*detailsJson.getIntValue("CDR_NUMBER")));
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
		consumeCardMap.put("IC_ID",ic_code);
		consumeCardMap.put("U_ID", TF.userJson.getString("U_ID"));
//		consumeCardMap.put("CCR_ORIGINALAMOUNT",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getFloatValue("balance"));
		consumeCardMap.put("CREATETIME",DateTimeUtil.getNowDate());
		consumeCardMap.put("COR_ID",orderMap.get("COR_ID"));
		consumeCardMap.put("MT_ID", TF.currentMealTimes);
		consumeCardMap.put("MACHINE_NO", TF.meterJson.getString("MACHINE_NO"));
		consumeCardMap.put("CCR_PAY_TYPE", 1);
		consumeCardMap.put("CCR_DATABASE_STATUS", "0");
		consumeCardMap.put("CCR_UPLOAD_STATUS", "0");
		consumeCardMap.put("PAY_REMARK", "消费("+TF.storeJson.getString("STORE_NAME"));
//		consumeCardMap.put("ACCOUNT_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("ACCOUNT_ID"));
//		consumeCardMap.put("MI_ID", TF.ConsumeJson.getJSONObject("deduction").getJSONObject("member").getJSONObject("cash").getString("MI_ID"));
		consumeCardMap.put("ISM_STATUS", "0");
		consumeCardMap.put("CLIENT_CODE",  TF.storeJson.getString("CLIENT_CODE"));
		consumeCardMap.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
		consumeCardMap.put("CCR_MONEY",DataConvertUtil.moneryFormat(total_price));
		orderMap.put("COR_MONERY", DataConvertUtil.moneryFormat(total_price));
		dataJson.put("tf_consume_order_record", orderMap);
		dataJson.put("tf_consume_card_record", consumeCardMap);
		
		} catch (Exception e) {
			dataJson.put("result", false);
			e.printStackTrace();
			TF.ConsumeJson.put("status", "销售商品数据异常");
		}
		return dataJson;
		
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

