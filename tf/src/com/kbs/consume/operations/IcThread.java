package com.kbs.consume.operations;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.ic.IcUtils;
import com.kbs.consume.deduction.IDeductionFactory;
import com.kbs.consume.deduction.IcDeductionFactory;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.TF;

public class IcThread extends Thread {
	long time=System.currentTimeMillis();
	int count=0;
	private IDeductionFactory deductionICFactory=new IcDeductionFactory();
	private IOperationsDeduction ic_deduction=deductionICFactory.createOperationsDeduction();
	IcUtils icUtil=IcUtils.getInstance();
	ConsumeService consumeService  = new ConsumeService();
	@Override
	public synchronized void run() {
	  while(true){
		if(TF.comStatusMap.getIntValue("icComStatus")==1){
			try {
				
				JSONObject icJson = readIc();
				if(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0401")>0){
					String mcd = icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg();
					if(mcd.length()>32){
//						mcd = mcd.substring(mcd.length()-32, mcd.length());
						mcd=mcd.substring(mcd.indexOf("020c"),mcd.indexOf("020c")+32);
					}
					String re_ic_id=mcd.substring( mcd.length()-4, mcd.length()-2)+
							mcd.substring( mcd.length()-6, mcd.length()-4)+
							mcd.substring( mcd.length()-8, mcd.length()-6)+
							mcd.substring( mcd.length()-10, mcd.length()-8);
					
					consumeService.ic_cmd(mcd,Long.parseLong(re_ic_id, 16)+"", DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+" HTPAP-->PTPE[卡离开消息推送]", "卡号:"+Long.parseLong(re_ic_id, 16)+"离开", 0);
					consumeService.ic_cmd(ClcUtil.getSt("0500"+mcd.substring(mcd.length()-18,mcd.length()-16)+"0004010000"),Long.parseLong(re_ic_id, 16)+"", DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss:SSS")+" HTPAP-->PTPE[卡离开消息推送]", "", 0);
					icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).send(icUtil.getHexArray(ClcUtil.getSt("0500"+mcd.substring(mcd.length()-18,mcd.length()-16)+"0004010000")));
				}
				if(icJson.getBooleanValue("result")){
					
					ic_deduction.deduction(icJson);
				}
				Thread.sleep(500);
			} catch (Exception e1) {
					e1.printStackTrace();
				}
			
		}
		
		else{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 }

	public JSONObject readIc(){
		JSONObject resultJson  = new JSONObject();
		resultJson.put("result", false);
		if(!icUtil.tfSerialPort.containsKey(TF.icJson.getString("serialport_type"))){
			icUtil.open(TF.icJson);
		}
		resultJson.put("data",new JSONObject());
		if(StringUtils.isNotEmpty(icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg()) && icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("02")==0 && 
				icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().length()==44 &&
						icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().lastIndexOf("03")==42 &&
								icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg().indexOf("0301")==18
				){
			String temp_ms = icUtil.tfSerialPort.get(TF.icJson.getString("serialport_type")).getResultMsg();
			resultJson.getJSONObject("data").put("IC_ID", temp_ms);
			resultJson.put("result", true);
		}
		return resultJson;
	}
}