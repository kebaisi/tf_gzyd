package com.kbs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class TF {

//	public static String currentMealTimes ="0"; //当前餐次
//	public static JSONObject icruleMap = new JSONObject(); 
//	public static Map<String,String> settingsMap = new HashMap<String ,String>();
//	public static List<String> freezingCardsList=new ArrayList<String>(); //挂失卡列表
//	public static JSONObject consumeObj=new JSONObject();
//	public static Map<String,String> uploadcardIdMap = new HashMap<String, String>();
//	public static JSONObject dsJson=new JSONObject();
//	public static JSONObject tagJson = JSONObject.parseObject("{\"tf_chiptype\":\"0\",\"tf_mealtimes\":\"0\",\"tf_settings\":\"0\",\"tf_userinfo\":\"0\",\"tf_freeze_card\":\"0\",\"tf_commodityinfo\":\"0\",\"tf_chip_category\":\"0\",\"tf_chipinfo\":\"0\",\"tf_merchant\":\"0\"}");
//	public static JSONObject synStatus = new JSONObject();
//	public static JSONObject tablePrimaryKeyJSON = JSONObject.parseObject("{\"tf_chiptype\":\"CY_ID\",\"tf_mealtimes\":\"SEQNO\",\"tf_settings\":\"SID\",\"tf_userinfo\":\"UID\"}");
//	public static JSONObject userJson = new JSONObject();
//	public static JSONObject checkStatus = JSONObject.parseObject("{\"checkStatus\":\"0\"}");//0:离线 1：在线
//	
//	public static Map<String,String> mealMap=new HashMap<String,String>();
//	public static JSONObject icJson=new JSONObject(); //存放卡配置信息
//	public static JSONObject rfidJson=new JSONObject(); //存放RFID配置信息
//	public static boolean rfidIsRunning=false;	//RFID线程是否运行
//	public static JSONObject defaultJSON=JSONObject.parseObject("{\"pay_code\": \"0\",\"status\": \"请放托盘\",\"isPrint\":\"0\",\"total_price\": \"0.0\",\"total_num\": \"0\",\"mealtime\":\"0\",\"stage_type\":0,\"machine_no\":\"\",\"cor_id\":\"\",\"valuation\": {\"auto\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"commodity\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"handwork\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}}},\"deduction\":{\"card\":{\"ic_id\":\"\",\"balance\":\"\",\"serial_number\":\"\"},\"cash\":{\"real_price\":\"0.0\",\"dispenser\":\"0.0\"}}}");
//	public static JSONObject ConsumeJson=new JSONObject();
//	public static JSONObject consumeWaterJson = JSONObject.parseObject("{\"1\":{\"num\":0,\"price\":0},\"2\":{\"num\":0,\"price\":0},\"3\":{\"num\":0,\"price\":0}}");
//	public static JSONObject consumePersonJson = new JSONObject(); 
//	public static JSONObject lastThreeTimeConsume=new JSONObject();
//	public static boolean isRepetileRFID = true;
//	public static List<Map<String,Object>> mealList=new ArrayList<Map<String,Object>>();
//	public static JSONObject preConsumeJson=new JSONObject();
//	public static float cardMoney=(float)0.0;
//	public static String serverTime="";
//	public static JSONObject chipJson=new JSONObject();	//存放数据库中读取的与芯片相关的三张表的数据
//	public static JSONObject commodityJson=new JSONObject();
//	public static boolean isPrint=false;
//	public static JSONArray printList=new JSONArray();
//	public static JSONObject onlineJson=JSONObject.parseObject("{\"data\":{\"tf_consume_card_record\":{},\"tf_consume_details_record\":[],\"tf_consume_order_record\":{}}}");
//	public static List<JSONObject> consumeList=new ArrayList<JSONObject>();
//	public static JSONObject gdJson = JSONObject.parseObject("{\"pay_code\": \"0\",\"status\": \"请放托盘\",\"isPrint\":\"0\",\"total_price\": \"0.0\",\"total_num\": \"0\",\"mealtime\":\"0\",\"stage_type\":0,\"machine_no\":\"\",\"cor_id\":\"\",\"valuation\": {\"auto\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"commodity\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"handwork\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}}},\"deduction\":{\"card\":{\"ic_id\":\"\",\"balance\":\"\",\"serial_number\":\"\"},\"cash\":{\"real_price\":\"0.0\",\"dispenser\":\"0.0\"}}}");
//	public static JSONObject storeJson=new JSONObject();
//	public static int cdPlasePlance=0;
//	public static JSONObject lossChipJson = new JSONObject();
//	public static JSONObject wxConfig = new JSONObject();
	public static String currentMealTimes ="0"; //当前餐次
	public static List<Map<String,Object>> mealList=new ArrayList<Map<String,Object>>();
	public static JSONObject icJson=new JSONObject(); //存放卡配置信息
	public static JSONObject rfidJson=new JSONObject(); //存放RFID配置信息
	public static JSONObject userJson = new JSONObject(); //登录用户信息
	public static JSONObject comStatusMap=new JSONObject(); //串口环境JSON
	public static JSONObject dsJson=new JSONObject(); //系统配置文件
	public static JSONObject chipJson=new JSONObject(); //餐具信息
	public static JSONObject chipCategoryJson = new JSONObject(); //餐具类型
	public static JSONObject mealChipJson = new JSONObject();  //带餐次的餐具类型信息
	public static JSONObject commodity_CI_Json=new JSONObject(); //商品信息
	public static JSONObject commodity_CP_Json=new JSONObject();
	public static List<Map<String,Object>> commodityList=new ArrayList<Map<String,Object>>();
	public static JSONObject storeJson=new JSONObject(); //店铺信息
	public static JSONObject defaultJSON=JSONObject.parseObject("{\"pay_code\": \"0\",\"status\": \"请放托盘\",\"isPrint\":\"0\",\"total_price\": \"0.0\",\"total_num\": \"0\",\"mealtime\":\"0\",\"stage_type\":0,\"machine_no\":\"\",\"cor_id\":\"\",\"valuation\": {\"auto\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"commodity\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"handwork\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}}},\"deduction\":{\"member\":{\"ic_id\":\"\",\"balance\":\"\",\"serial_number\":\"\"},\"cash\":{\"real_price\":\"0.0\",\"dispenser\":\"0.0\"}}}");
	public static JSONObject consumeWaterJson = JSONObject.parseObject("{\"1\":{\"num\":0,\"price\":0},\"2\":{\"num\":0,\"price\":0},\"3\":{\"num\":0,\"price\":0}}");
	public static JSONObject ConsumeJson=JSONObject.parseObject("{\"pay_code\": \"0\",\"status\": \"请放托盘\",\"isPrint\":\"0\",\"total_price\": \"0.0\",\"total_num\": \"0\",\"mealtime\":\"0\",\"stage_type\":0,\"machine_no\":\"\",\"cor_id\":\"\",\"valuation\": {\"auto\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"commodity\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"handwork\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}}},\"deduction\":{\"member\":{\"ic_id\":\"\",\"balance\":\"\",\"serial_number\":\"\"},\"cash\":{\"real_price\":\"0.0\",\"dispenser\":\"0.0\"}}}"); // 运营JSON数据
	public static JSONObject gdJson = JSONObject.parseObject("{\"pay_code\": \"0\",\"status\": \"请放托盘\",\"isPrint\":\"0\",\"total_price\": \"0.0\",\"total_num\": \"0\",\"mealtime\":\"0\",\"stage_type\":0,\"machine_no\":\"\",\"cor_id\":\"\",\"valuation\": {\"auto\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"commodity\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}},\"handwork\": {\"price\":\"0.0\",\"num\":\"0\",\"view\":{}}},\"deduction\":{\"member\":{\"ic_id\":\"\",\"balance\":\"\",\"serial_number\":\"\"},\"cash\":{\"real_price\":\"0.0\",\"dispenser\":\"0.0\"}}}"); //数据挂单
	public static JSONObject storeConfig = new JSONObject();  //店铺配置信息
	public static JSONObject meterJson  = new JSONObject(); //机器配置信息
	public static JSONObject startEnvJson = new JSONObject(); //启动时环境监测信息
	public static JSONObject preConsumeJson = new JSONObject(); //已支付餐具
	public static JSONObject consumePersonJson = new JSONObject(); 
	public static JSONObject checkStatus = JSONObject.parseObject("{\"checkStatus\":\"0\"}");
	public static String serverUrl = "";
	public static int trade_no =1;
	
}
