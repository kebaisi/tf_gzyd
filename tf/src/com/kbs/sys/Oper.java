package com.kbs.sys;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.TF;

public class Oper {
	private static ConsumeService consumeService=ConsumeService.getInstance();
	private static MetaDataBO metaDataBO=new MetaDataBO();
	/**
	 * 消费人数
	 */
	public static void loadConsumeNum(){
		System.out.println("zx chaxun");
		TF.consumePersonJson.put("cur_meal",consumeService.queryCurrentMealConsumeNum());
		TF.consumePersonJson.put("cur_day",consumeService.queryCurrentDayConsumeNum());
	}
	
	/**
	 * 消费人数统计
	 */
	public static void loadConsumeInfo(){
		TF.consumePersonJson.put("cur_meal",TF.consumePersonJson.getIntValue("cur_meal")+1);
		TF.consumePersonJson.put("cur_day",TF.consumePersonJson.getIntValue("cur_day")+1);
	}
	
	/**
	 * 消费流水JSON
	 * @param consumeJson
	 */
	public static void loadConsumeWater(JSONObject consumeJson){
		TF.consumeWaterJson.put("3", TF.consumeWaterJson.getJSONObject("2"));
		TF.consumeWaterJson.put("2", TF.consumeWaterJson.getJSONObject("1"));
		TF.consumeWaterJson.put("1", consumeJson);
	}
	/**
	 * TF.ConsumeJson初始化
	 */
	public  static void clearConsumeCache(){
		int flag=TF.ConsumeJson.getIntValue("isPrint");
		TF.ConsumeJson = new JSONObject();
		TF.ConsumeJson = JSONObject.parseObject(TF.defaultJSON.toJSONString());
		TF.ConsumeJson.put("isPrint", flag);
	}
	/**
	 * 加载当前餐次
	 * @return
	 */
	public static String getCurrMites(){
		try {
			Date date=new Date();
			Date currentDate=new Date();
			long currentTime=currentTime = new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(date)).getTime();
			List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_mealtimes");
			for(Map mealMap:list){
				long beginTime=new SimpleDateFormat("HH:mm").parse((String)mealMap.get("STARTTIME")).getTime();
				long endTime=new SimpleDateFormat("HH:mm").parse((String)mealMap.get("ENDTIME")).getTime();
				if(endTime<beginTime){
					endTime+=24*60*60*1000;
				}
				if(currentTime>beginTime&&currentTime<endTime){
					return (String)mealMap.get("MT_ID");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
}
