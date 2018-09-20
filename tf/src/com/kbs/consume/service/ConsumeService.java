package com.kbs.consume.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.operations.RfidThread;
import com.kbs.sys.Oper;
import com.kbs.util.AudioPlay;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.PrintByXinhui;
import com.kbs.util.SpeakUtil;
import com.kbs.util.TF;

public class ConsumeService {
	public static boolean currentReadRFID = false;
	public static  ConsumeService instance = null;
	private MetaDataBO metaDataBO=new MetaDataBO();
	private Logger logger=Logger.getLogger(ConsumeService.class);
	public String CLRARPOINTER="5A000000";
	public String CLEARPRICE="00000000";
	public static String FILE_PLEASESETPANEL="sounds/pleaseSetPanel.wav";
	public static String FILE_PLEASESETCARD="sounds/pleaseSetCard.wav";
//	public static String FILE_EMPTYPANEL="emptyPanel.wav";
	public static String FILE_LOSTCARD="sounds/lostCard.wav";
	public static String FILE_MONEYISNOTENOUGH="sounds/moneyNotEnough.wav";
	public static String FILE_NOEFFECTCARD="sounds/noEffectCard.wav";
	public static String FILE_CONSUMESUCCESS="sounds/consumeSuccess.wav";
	public static String FILE_CONSUMEFILED="sounds/consumeFiled.wav";
	public static String FILE_NOEFFECTPANEL="sounds/noEffectPanel.wav";
	public static String FILE_SENDCARD="sounds/pleaseSendCard.wav";
	public static String FILE_CANCLECARD="sounds/cancleCard.wav";
	public static String FILE_NOTPAY="sounds/notPay.wav";
	public static String NOTACCEPTPANEL="sounds/notAcceptPanel.wav";
	public static String FILE_DISABLEPANEL="sounds/disablePanel.wav";
	public static String ILLEGALCARD="sounds/illegalCard.wav";
	private SpeakUtil speakUtil=new SpeakUtil();
	private PrintByXinhui printByXinhui=new PrintByXinhui();
	
	public static ConsumeService getInstance(){
		if(instance == null){
			instance = new ConsumeService();
		}
		return  instance;
	}
	
	public synchronized void payComplement(JSONObject dataJson){
		try {
			TF.ConsumeJson.put("stage_type", 3);
			TF.ConsumeJson.put("gd_status",0);
			TF.ConsumeJson.put("status", "支付成功");
			
		 	Oper.loadConsumeInfo();
		 	JSONObject waterJson = new JSONObject();
//		 	if(TF.ConsumeJson!=null){
		 		waterJson.put("num", dataJson.getJSONObject("tf_consume_order_record").getDoubleValue("COR_AMOUNT"));
			 	waterJson.put("price", dataJson.getJSONObject("tf_consume_order_record").getDoubleValue("COR_MONERY"));
//		 	}
			Oper.loadConsumeWater(waterJson);
			AudioPlay.playAudio(ConsumeService.FILE_CONSUMESUCCESS);
//		 	AudioPlay.speak("谢谢");
//		 	speakUtil.speak("谢谢");
			if(TF.dsJson.getString("isprint").equals("1")){
//				if(TF.ConsumeJson!=null){
//					if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")==0.0){
//						PrintByXinhui.printJob(TF.currentMealTimes, TF.ConsumeJson.getString("total_price"), TF.ConsumeJson.getString("total_price"),"0");
//					}else{
//						PrintByXinhui.printJob(TF.currentMealTimes, TF.ConsumeJson.getString("total_price"), TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")+"",TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("dispenser")+"");
//						
//					}
//				}
				printByXinhui.print(dataJson);
			}
			for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key).put("pay_status", 1);
				TF.preConsumeJson.put(key, System.currentTimeMillis());
//				metaDataBO.execute("UPDATE TF_LOSS_CHIP_RECORD SET IS_CONSUME='1' WHERE CHIP_NO='"+key+"' AND CREATETIME >'"+DateTimeUtil.changeMillsToTime((System.currentTimeMillis()-TF.dsJson.getIntValue("clearPayedBowl")*60*1000))+"'");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询所有营业时间
	 */
	public Map<String,String> queryAllMealTime(){
		List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_mealtimes WHERE MT_STATUS='0' order by MT_ID");
		Map<String,String> mealMap=new HashMap();
		for(Map<String,Object> m:list){
			String time=m.get("STARTTIME")+"-"+m.get("ENDTIME");
			mealMap.put(m.get("MT_NAME")+"",time);
		}
		return mealMap;
	}
	
	/**
	 * 查询当天消费总额
	 * @return
	 */
	public float queryCurrentDayConsume(){
		String datestr=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String,Object> map = metaDataBO.queryForMap("select sum(CCR_MONEY) as ccr_monery from(SELECT CCR_MONEY FROM tf_consume_card_record c ,tf_consume_order_record o where c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and o.COR_ID=c.COR_ID and c.CREATETIME>'"+datestr+"'  and c.CCR_STATUS!='0' )as son");
		if(map==null ||map.keySet().size()==0) return 0;
		else
		return  Float.parseFloat(map.get("ccr_monery")==null?"0":map.get("ccr_monery")+"");
	}
	
	/**
	 * 查询当餐消费总额
	 * @return
	 */
	public float queryCurrentMealConsume(){
		String datestr=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String,Object> map = metaDataBO.queryForMap("select count(*) total_num from tf_consume_card_record where ccr_status='1' and MT_ID='"+TF.currentMealTimes+"' and createtime between '"+datestr+" 00:00:00' and '"+datestr+" 23:59:59'");
		if(map==null ||map.keySet().size()==0) return 0;
		else
		return  Float.parseFloat(map.get("total_num")==null?"0":map.get("total_num")+"");
	}
	
	/**
	 * 查询当天消费人数
	 * @return
	 */
	public int queryCurrentDayConsumeNum(){
		String datestr=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String,Object> map = metaDataBO.queryForMap("select count(*) total_num from tf_consume_card_record where ccr_status='1' and createtime between '"+datestr+" 00:00:00' and '"+datestr+" 23:59:59'");
		if(map==null ||map.keySet().size()==0) return 0;
		else
		return  Integer.parseInt(map.get("total_num")+""==null?"0":map.get("total_num")+"");
	}
	
	/**
	 * 查询当餐消费人数
	 * @return
	 */
	public int queryCurrentMealConsumeNum(){
		String datestr=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String currentMeal=TF.currentMealTimes;
		Map<String,Object> map = metaDataBO.queryForMap("select count(CCR_MONEY) as count from tf_consume_card_record where mt_id='"+TF.currentMealTimes+"' and ccr_status='1' and createtime>'"+datestr+" 00:00:00'");
		if(map==null ||map.keySet().size()==0) return 0;
		else
		return  Integer.parseInt(map.get("count")+""==null?"0":map.get("count")+"");
	}
	/**
	 * 按时间段查询消费记录
	 * @param args
	 */
	public List<Map<String,Object>> queryConsumeByTime(String startTime,String endTime,String mealName,String pricerId){
		List<Map<String,Object>> consumeList=new ArrayList<Map<String,Object>>();
//		String querySql="SELECT c.*,m.MT_NAME FROM tf_consume_card_record c INNER JOIN tf_consume_order_record o on c.COR_ID=o.COR_ID INNER JOIN tf_mealtimes m on o.ME_ID=m.SEQNO ,tf_chiptype p where c.CCR_STATUS!=0";
		String querySql="SELECT c.CREATETIME,c.COR_ID,d.CDR_MONEY,c.IC_SERIAL_NUMBER,c.CCR_ORIGINALAMOUNT,c.UID, m.MT_NAME FROM tf_consume_card_record c,tf_consume_order_record o,tf_mealtimes m,tf_consume_details_record d,tf_chiptype p WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS != '0' AND c.COR_ID = o.COR_ID AND d.COR_ID = o.COR_ID and m.SEQNO=c.MT_ID";
		if(pricerId==null ||pricerId.equals("")||pricerId.indexOf("无")!=-1){
			querySql="SELECT c.*, m.MT_NAME FROM tf_consume_card_record c,tf_consume_order_record o,tf_mealtimes m WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS != '0' AND c.COR_ID = o.COR_ID and m.SEQNO=c.MT_ID";
//			querySql="SELECT c.*,m.MT_NAME FROM tf_consume_card_record c INNER JOIN tf_consume_order_record o ON c.COR_ID = o.COR_ID AND c.CCR_STATUS != '0' INNER JOIN tf_mealtimes m on m.SEQNO=o.ME_ID";
		}
		if(startTime!=null && !startTime.equals("") && endTime!=null && !endTime.equals("")){
			Calendar cd=Calendar.getInstance();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date d=new Date();
			try {
				d = new Date(sdf.parse(endTime).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cd.setTime(d);
			cd.add(Calendar.DATE, 1);
			endTime=sdf.format(cd.getTime());
			querySql+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"'";
		}
		
		if(mealName!=null && !mealName.equals("")){
			if(mealName.indexOf("无")==-1)
			querySql+=" and m.MT_NAME='"+mealName+"'";
		}
		if(pricerId!=null && !pricerId.equals("")){
			if(pricerId.indexOf("无")==-1)
			querySql+=" and p.CY_PRICE='"+pricerId+"' and p.CY_PRICE=d.CPR_ID";
		}
		querySql+=" order by c.CREATETIME desc";
		consumeList=metaDataBO.queryForListMap(querySql);
		return consumeList;
	}
	public void ic_cmd(String cmd,String ic_id,String type,String cmd_explain,long elasedTime){
		JSONObject dataJson = new JSONObject();
		dataJson.put("SEQNO",UUID.randomUUID());
		dataJson.put("CREATETIME", DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		dataJson.put("UPDATETIME", DateTimeUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		dataJson.put("CLIENT_CODE", TF.storeJson.getString("CLIENT_CODE"));
		dataJson.put("STORE_CODE", TF.storeJson.getString("STORE_CODE"));
		dataJson.put("CMD",cmd);
		dataJson.put("MACHINE_NO",TF.meterJson.getString("MACHINE_NO"));
		dataJson.put("MACHINE_NAME",TF.meterJson.getString("MACHINE_NAME"));
		dataJson.put("IC_ID",ic_id);
		dataJson.put("CMD_TYPE",type);
		dataJson.put("CMD_EXLAIN", cmd_explain);
		dataJson.put("CMD_ELASEDTIME", elasedTime);
		metaDataBO.execute(metaDataBO.toSql(dataJson, "ic_cmd_record"));
		
	}
	public static void main(String[] args) {
		ConsumeService consumeService=new ConsumeService();
		System.out.println(consumeService.queryCurrentDayConsume());
	}
}
