package com.kbs.consume.operations;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.kbs.consume.MyException;
import com.kbs.sys.Oper;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.TF;

public class FixedValuation implements  Runnable {
	public void run() {
		while(true){
			try {
				if(TF.dsJson.getDoubleValue("fixed_price")==0){
					break;
				}
		if(TF.ConsumeJson.getIntValue("stage_type")==3){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Oper.clearConsumeCache();
		}
		if(TF.ConsumeJson.getDoubleValue("total_price")==0){
			JSONObject jsonPrice=new JSONObject();
			String time = String.valueOf(System.currentTimeMillis());
			jsonPrice.put("price", TF.dsJson.getDoubleValue("fixed_price"));
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(time,jsonPrice);
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("use_status",0);
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("pay_status",0);
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("price", DataConvertUtil.moneryFormat(TF.dsJson.getDoubleValue("fixed_price")));
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("num",1);
//			TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
			TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(TF.dsJson.getDoubleValue("fixed_price")));	
			TF.ConsumeJson.put("total_num", 1);
			TF.ConsumeJson.put("stage_type",1);
			TF.ConsumeJson.put("status", "请支付");
	}
		
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
} 


