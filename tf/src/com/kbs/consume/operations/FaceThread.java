package com.kbs.consume.operations;


import java.io.IOException;
import java.net.UnknownHostException;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.ic.IcUtils;
import com.kbs.consume.deduction.FaceDeductionFactory;
import com.kbs.consume.deduction.IDeductionFactory;
import com.kbs.consume.deduction.IcDeductionFactory;
import com.kbs.util.TF;

public class FaceThread extends Thread {
	long time=System.currentTimeMillis();
	int count=0;
	private IDeductionFactory deductionICFactory=new FaceDeductionFactory();
	private IOperationsDeduction ic_deduction=deductionICFactory.createOperationsDeduction();
	IcUtils icUtil=IcUtils.getInstance();
	@Override
	public synchronized void run() {
		FaceUtil fx =null;
		try {
			fx = new FaceUtil();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=fx.message&&!fx.message.equals("") && fx.message.indexOf("Quit")>0){
			TF.ConsumeJson.put("face_code", fx.message.substring(fx.message.indexOf("id=")+4,fx.message.indexOf("name")-2));
			System.out.println();
			fx.message =new StringBuffer();
			if(TF.ConsumeJson.containsKey("face_code") && TF.ConsumeJson.getString("face_code").length()>0){
				ic_deduction.deduction(null);
			}
			}
			
		}
//	  while(true){
//		if(TF.comStatusMap.getIntValue("icComStatus")==1){
//			try {
//				JSONObject icJson = readIc();
//				if(icJson.getBooleanValue("result")){
//					TF.ConsumeJson.put("ic_code", icJson.getJSONObject("data").getString("IC_ID"));
//				}
//			} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//		}
//		if(TF.ConsumeJson.containsKey("ic_code") && TF.ConsumeJson.getString("ic_code").length()>0){
//		ic_deduction.deduction(null);
//		TF.ConsumeJson.put("ic_code", "");
//		}
//		else{
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
 }
	public JSONObject readIc(){
		JSONObject jsonObj= new JSONObject();
		jsonObj = icUtil.operateIc(TF.icJson);
		jsonObj.put("data", new JSONObject());
		if(jsonObj==null){
			jsonObj.put("result", false);
			jsonObj.put("msg", "读卡出现异常");
			return jsonObj;
		}
		if(jsonObj.containsKey("id")&& !jsonObj.getString("id").isEmpty()){
			String serialId=jsonObj.getString("id");
			  if(serialId.length()!=8){
				  jsonObj.put("result", false);
				  jsonObj.put("msg", "读卡出现异常");
				  return jsonObj;
			  }
			String str=serialId.substring(6, 8)+serialId.substring(4, 6)+serialId.substring(2, 4)+serialId.substring(0, 2);
			String id = Long.parseLong(str, 16)+"";
			if(TF.icJson.getIntValue("id_length")==10){
				while(id.length()<10){
					id="0"+id;
				}
			}
			jsonObj.getJSONObject("data").put("IC_ID", id);
			jsonObj.put("result", true);
		}
		return jsonObj;
		
	}
}