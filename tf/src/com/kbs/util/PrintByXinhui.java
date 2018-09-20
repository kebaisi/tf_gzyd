package com.kbs.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;

public class PrintByXinhui {
	private MetaDataBO metaDataBO=new MetaDataBO();
	/**
	 * 
	 * @param cumeals 当前餐次
	 * @param consumeTotal 消费总额
	 * @param actualMoney  实收金额
	 * @param changeMoney  找零金额
	 */
	public static void printJob(String cumeals,String consumeTotal,String actualMoney,String changeMoney){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdfNo=new SimpleDateFormat("yyyyMMdd");
    	String date=sdf.format(new Date());
    	String cumealName="";
    	if(!"".equals(cumeals)){
    		switch (cumeals) {
			case "1":
				cumealName="早餐";
				break;
			case "2":
				cumealName="午餐";		
				break;
			case "3":
				cumealName="晚餐";
				break;
			case "4":
				cumealName="夜宵";
				break;
			case "5":
				cumealName="拓展";
				break;
			default:
				break;
			}
    	}
        StringBuffer sb = new StringBuffer();
        String cuMealConsumeNum="";
        int i=TF.consumePersonJson.getIntValue("cur_meal");
        switch ((i+"").length()) {
		case 1:
			cuMealConsumeNum="000"+(i);
			break;
		case 2:
			cuMealConsumeNum="00"+(i);
			break;
		case 3:
			cuMealConsumeNum="0"+(i);
			break;
		default:
			cuMealConsumeNum=(i)+"";
        }
//        switch ((TF.currentMealConsumeNum+1+"").length()) {
//		case 1:
//			cuMealConsumeNum="000"+(TF.currentMealConsumeNum+1);
//			break;
//		case 2:
//			cuMealConsumeNum="00"+(TF.currentMealConsumeNum+1);
//			break;
//		case 3:
//			cuMealConsumeNum="0"+(TF.currentMealConsumeNum+1);
//			break;
//		default:
//			cuMealConsumeNum=(TF.currentMealConsumeNum+1)+"";
//		}
//        String newStr = new String(TF.dsJson.getString("merchantName").getBytes("GB2312"),"ISO-8859-1");

        sb.append("       小票编号:"+TF.dsJson.getString("machineId")+sdfNo.format(new Date())+cumeals+cuMealConsumeNum);
        try {
			sb.append("\r\n\r\n\r\n         "+new String(TF.dsJson.getString("merchantName").getBytes("ISO-8859-1"),"gb2312"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sb.append("\r\n\r\n日期:" + date);
        sb.append("\r\n\r\n餐 次: "+cumealName);
        sb.append("\r\n\r\n\r\n--------------------------------\r\n");
        sb.append("\r\n  消 费 总 额：     "+consumeTotal+" 元");
        sb.append("\r\n\r\n\r\n  实 收："+actualMoney+" 元     找零 "+changeMoney+"元");
        sb.append("\r\n\r\n--------------------------------\r\n\r\n");
        sb.append("     谢谢惠顾，欢迎下次光临！");
        sb.append("\r\n     此小票仅作就餐消费依据。\r\n\r\n\r\n\n");
        try {
			sb.append("               "+new String(TF.dsJson.getString("merchantAddress").getBytes("ISO-8859-1"),"gb2312")+"\r\n\r\n\r\n\r\n\r\n");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String printStr = sb.toString();
    	SuccessPrint successPrint = new SuccessPrint(printStr);
    	successPrint.printMsg();
	}
	
	public void print(JSONObject json){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdfNo=new SimpleDateFormat("yyyyMMdd");
    	String date=sdf.format(new Date());
    	String cumealName="";
    	String mtId=json.getJSONObject("tf_consume_card_record").getString("MT_ID");
    	if(!"".equals(mtId)){
    		switch (mtId) {
			case "1":
				cumealName="早餐";
				break;
			case "2":
				cumealName="午餐";		
				break;
			case "3":
				cumealName="晚餐";
				break;
			case "4":
				cumealName="夜宵";
				break;
			case "5":
				cumealName="拓展";
				break;
			default:
				break;
			}
    	}
        StringBuffer sb = new StringBuffer();
        String cuMealConsumeNum="";
        int m=TF.consumePersonJson.getIntValue("cur_meal");
        switch ((m+"").length()) {
		case 1:
			cuMealConsumeNum="000"+(m);
			break;
		case 2:
			cuMealConsumeNum="00"+(m);
			break;
		case 3:
			cuMealConsumeNum="0"+(m);
			break;
		default:
			cuMealConsumeNum=(m)+"";
        }
//        switch ((TF.currentMealConsumeNum+1+"").length()) {
//		case 1:
//			cuMealConsumeNum="000"+(TF.currentMealConsumeNum+1);
//			break;
//		case 2:
//			cuMealConsumeNum="00"+(TF.currentMealConsumeNum+1);
//			break;
//		case 3:
//			cuMealConsumeNum="0"+(TF.currentMealConsumeNum+1);
//			break;
//		default:
//			cuMealConsumeNum=(TF.currentMealConsumeNum+1)+"";
//		}
//        String newStr = new String(TF.dsJson.getString("merchantName").getBytes("GB2312"),"ISO-8859-1");

       
        String consumeTotal=json.getJSONObject("tf_consume_card_record").getString("CCR_MONEY");
        String consumeNum=json.getJSONObject("tf_consume_order_record").getString("COR_AMOUNT");
        String payType=json.getJSONObject("tf_consume_card_record").getString("CCR_PAY_TYPE");
        sb.append("小票编号:");
        sb.append("\r\n" + json.getJSONObject("tf_consume_card_record").getString("COR_ID"));
			sb.append("\r\n\r\n          "+TF.storeJson.getString("STORE_NAME"));
        String actualMoney="";
        String changeMoney="";
        sb.append("\r\n\r\n设备号:" + TF.meterJson.getString("MACHINE_NO"));
        sb.append("\r\n操作员：" + TF.userJson.getString("UNAME"));
        sb.append("\r\n\r\n日期:" + date);
        sb.append("\r\n\r\n品名\t   数量   单价   金额");
        sb.append("\r\n--------------------------------");
        for(int i=0;i<json.getJSONArray("tf_consume_details_record").size();i++){
        	String name="";
        	String price="";
        	JSONObject detailsJson=json.getJSONArray("tf_consume_details_record").getJSONObject(i);
//        	sb.append(detailsJson.getString(""))
        	String cdrType=detailsJson.getString("CDR_TYPE");
        	if(cdrType.equals("0")){
        		Map map=metaDataBO.queryForMap("select * from tf_chipinfo where CP_NO='"+detailsJson.getString("CDR_NO")+"'");
        		if(map!=null&&map.size()>0){
        			Map ctMap=metaDataBO.queryForMap("select * from tf_meter_chiptype_relation where CG_ID='"+map.get("CG_ID")+"'");
        			if(ctMap!=null&&ctMap.size()>0){
        				name=ctMap.get("CG_NAME")+"";
        				price=detailsJson.getString("CDR_MONEY");
        			}
        		}
        	}else if(cdrType.equals("1")){
        		name="定价商品";
        		price=detailsJson.getString("CDR_MONEY");
        		
        	}else if(cdrType.equals("2")){
        		Map map=metaDataBO.queryForMap("select * from commodity_record where CI_ID='"+detailsJson.getString("CDR_NO")+"'");
        		if(map!=null&&map.size()>0){
        			name=map.get("CI_NAME")+"";
    				price=detailsJson.getString("CDR_MONEY");
        		}
        	}else if(cdrType.equals("4")){
        		Map map=metaDataBO.queryForMap("select * from tf_dish_record where DISH_ID='"+detailsJson.getString("CDR_NO")+"'");
        		if(map!=null&&map.size()>0){
        			name=map.get("DISH_NAME")+"";
    				price=detailsJson.getString("CDR_MONEY");
        		}
        	}
        	sb.append("\r\n"+name+"\t    "+detailsJson.getString("CDR_NUMBER")+"     "+detailsJson.getString("CDR_UNIT_PRICE")+"     "+price);
        }
        sb.append("\r\n--------------------------------");
        sb.append("\r\n总数量:"+consumeNum);
        sb.append("\r\n总金额:"+consumeTotal);
//        sb.append("\r\n\r\n  实 收："+actualMoney+" 元     找零 "+changeMoney+"元");
        if(payType.equals("0")){
        	sb.append("\r\n支付方式：现金");
        	actualMoney=json.getJSONObject("tf_consume_card_record").getString("CCR_ORIGINALAMOUNT");
        	changeMoney=(json.getJSONObject("tf_consume_card_record").getFloatValue("CCR_ORIGINALAMOUNT")-json.getJSONObject("tf_consume_card_record").getFloatValue("CCR_MONEY"))+"";
        	sb.append("\r\n实  收:"+actualMoney+"\t找  零 :"+changeMoney);
        }else if(payType.equals("1")){
        	sb.append("\r\n支付方式：会员卡");
        	actualMoney=consumeTotal;
        	changeMoney="0";
        	sb.append("\r\n余额:"+(json.getJSONObject("tf_consume_card_record").getFloatValue("CCR_ORIGINALAMOUNT")));
        	
        }
        else if(payType.equals("2")){
        	sb.append("\r\n支付方式：微信支付");
        }
        else if(payType.equals("3")){
        	sb.append("\r\n支付方式：支付宝支付");
        }
        sb.append("\r\n备注："+json.getJSONObject("tf_consume_card_record").getString("PAY_REMARK"));
        sb.append("\r\n地址："+TF.storeJson.getString("STORE_ADDR"));
        sb.append("\r\n--------------------------------");
        sb.append("     谢谢惠顾，欢迎下次光临！");
        sb.append("\r\n     此小票仅作消费依据。\r\n\r\n\r\n");
        String printStr = sb.toString();
    	SuccessPrint successPrint = new SuccessPrint(printStr);
    	successPrint.printMsg();
	}

}
