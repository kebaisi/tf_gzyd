package com.kbs.consume.operations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.commons.rfid.RfidUtils;
import com.kbs.consume.MyException;
import com.kbs.consume.service.ConsumeService;
import com.kbs.sys.Oper;
import com.kbs.util.AudioPlay;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.RandomUtil;
import com.kbs.util.SpeakUtil;
import com.kbs.util.TF;

public class OperationsAutoValuation22 implements IOperationsValuation {
	private Logger logger=Logger.getLogger(OperationsAutoValuation.class);
	private List<String> rfidList = new ArrayList<String>();
	private SpeakUtil speakUtil=new SpeakUtil();
	private static MetaDataBO metaDataBO=new MetaDataBO();
	@Override
	public synchronized void valuation() throws MyException {
//		rfidList = new ArrayList<String>();
		
		
		
			OperateByRfid(TF.rfidJson);
		
//		test();
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void test(){
		if(TF.ConsumeJson.getIntValue("stage_type")==3){
			Oper.clearConsumeCache();
		}
		JSONObject random = new JSONObject();
		random.put("length", 10);
		String time=String.valueOf(System.currentTimeMillis());
		JSONObject jsonPrice=new JSONObject();
		jsonPrice.put("price", Float.parseFloat(RandomUtil.getRandom(random).getString("result")));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(time,jsonPrice);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("use_status",0);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("pay_status",0);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("price", new java.text.DecimalFormat("0.0").format(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price")+jsonPrice.getFloatValue("price")));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+1);
//		TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
	
		TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))));	
		TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
		TF.ConsumeJson.put("stage_type",1);
		TF.ConsumeJson.put("status", "请支付");
//		if(TF.ConsumeJson.getFloatValue("total_price")<20){
//			AudioPlay.playAudio(ConsumeService.FILE_PLEASESETCARD);
//		}else{
			String price=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
//			AudioPlay.speak("请付"+price+"元");
		
	}
	public List<String> getNotConsumed(){
		List<String> cacheNoConsumeList=new ArrayList<String>();
		JSONObject jsonObject=null;
		if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view")!=null){
			jsonObject=TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view");
		}else{
			return cacheNoConsumeList;
		}
		for(String key:jsonObject.keySet()){
			if(jsonObject.getJSONObject(key).getIntValue("pay_status")==0&&jsonObject.getJSONObject(key).getIntValue("gd_status")==0){
				cacheNoConsumeList.add(key);
			}
		}
		return cacheNoConsumeList;
	}
	
	/**
	 * 检查当前芯片是否存在缓存中
	 * @param rfidJSON
	 * @return
	 */
	public boolean checkRfidCacheData(JSONObject rfidRuleJson) throws MyException{
		List<String> cacheNoConsumeList=getNotConsumed();
		JSONObject jsonObject=null;
		if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view")!=null){
			jsonObject=TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view");
		}else{
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("view", new JSONObject());
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("price", "0.0");
			TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("num", "0");
		}
		if((rfidList==null || rfidList.size()==0) && jsonObject.keySet().size()==0){
			if(TF.ConsumeJson.getIntValue("total_num")==0){
				TF.ConsumeJson.put("stage_type", 0);
				TF.ConsumeJson.put("status", "请放托盘");
			}
			return false;
		}
		
		
		
		if(rfidList.size()==jsonObject.keySet().size()){
			if(rfidList.containsAll(jsonObject.keySet())){
				return false;
			}
		}
		if(cacheNoConsumeList.size()!=0 && (rfidList.size()==0||!rfidList.containsAll(cacheNoConsumeList))){
			while (true) {
				if(TF.ConsumeJson.getIntValue("stage_type")==3){
					return true;
				}
				if(TF.ConsumeJson.getIntValue("gd_status")==1){
					return true;
				}
				TF.ConsumeJson.put("stage_type", 0);
				TF.ConsumeJson.put("status", "未付款");
				AudioPlay.playAudio(ConsumeService.FILE_NOTPAY);
//				speakUtil.speak("未付款");
				rfidList=readPanel();
				if(rfidList!=null&&(rfidList.containsAll(cacheNoConsumeList) || TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num")==0)){
					return true;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		return true;
		
	}
	
	/**
	 * 检测芯片是否为非法餐盘或禁用餐盘
	 * @return
	 */
	public boolean checkChip(){
		System.out.println("---size:"+TF.chipJson.keySet().size());
		for(String chipNo:rfidList){
			System.out.println(chipNo);
			if(!TF.chipJson.containsKey(chipNo)){
				
				TF.ConsumeJson.put("status", "发现未授权餐盘");
				AudioPlay.playAudio(ConsumeService.NOTACCEPTPANEL);
//				speakUtil.speak("未授权餐盘");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
//			else {
//				if(TF.chipJson.getJSONObject("category").getJSONObject(TF.chipJson.getJSONObject("chipinfo").getJSONObject(chipNo).getString("CG_ID")).getIntValue("USE_STATUS")==1){
//					TF.ConsumeJson.put("status", "发现禁用餐盘");
////					AudioPlay.playAudio(ConsumeService.FILE_DISABLEPANEL);
//					speakUtil.speak("禁用餐盘");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					return false;
//				}
//			}
		}
		return true;
	}
	
	/* kai
	 * rfid操作流程
	 * @param commUtil
	 * @throws MyException
	 */
	public void OperateByRfid(JSONObject rfidRuleJson) throws MyException{
		if(TF.ConsumeJson.getIntValue("gd_status")==1){
			return;
		}
		try {
			rfidList=readPanel();
			if(rfidList==null ){
				return;
			}
			
			TF.ConsumeJson.put("machine_no", TF.dsJson.getString("machineId"));
			TF.ConsumeJson.put("mealtime", TF.currentMealTimes);
			if(!checkRfidCacheData(rfidRuleJson)){
				return;
			}
			if(TF.ConsumeJson.getIntValue("stage_type")==3){
				Oper.clearConsumeCache();
			}
			if(!checkChip()){
				Oper.clearConsumeCache();
				return;
			}
			if(!loadConsumeData()){
				return;
			}
			checkLossChip(rfidList);
			if(getNotConsumed().size()<=0||TF.ConsumeJson.getFloatValue("total_price")==0.0)
				return;
			TF.ConsumeJson.put("status", "请支付");
//			if(TF.ConsumeJson.getIntValue("total_num")<=5||TF.ConsumeJson.getFloatValue("total_price")<=20){
				AudioPlay.playAudio(ConsumeService.FILE_PLEASESETCARD);
//			}else{
//				String price=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
//				AudioPlay.speak("请付"+price+"元");
////				speakUtil.speak("请付"+price+"元");
//			}
			TF.ConsumeJson.put("stage_type", 1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//			String msg=e1.getMessage().length()>100?e1.getMessage().substring(0, 100):e1.getMessage();
//			logger.error("计价出现异常"+msg);
//			ExceptionInfoService.insertExceptionData(0, "OperationAutoValuation", "计价出现异常", e1.getMessage());
//			throw new MyException();
			e1.printStackTrace();
		}
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			logger.error("当前计价完成sleep过程中发生错误");
//		}
	}
	
	public void checkLossChip(List<String> rfidList){
//		for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
//			if(!TF.lossChipJson.containsKey(key) || (System.currentTimeMillis()-TF.lossChipJson.getLongValue(key))>TF.dsJson.getIntValue("clearPayedBowl")*60*1000){
//				metaDataBO.execute("INSERT INTO TF_LOSS_CHIP_RECORD(CHIP_NO,CREATETIME,IS_CONSUME,MT_ID,PRICE) VALUES('"+key+"','"+DateTimeUtil.getNowDate()+"','0','"+TF.currentMealTimes+"','"+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key).getString("price")+"')");
//				TF.lossChipJson.put(key, System.currentTimeMillis());
//			}
//			
//		}
		
	}
	/**
	 * type:
	 * 	1:未注册芯片
	 * 	2:没有价格信息
	 *  3:禁用芯片
	 * @param rfidList
	 * @return
	 * @throws MyException
	 */
	public boolean loadConsumeData() throws MyException{
		int payStatus=1;
		JSONObject resultJSON=new JSONObject();
		if(TF.ConsumeJson.getIntValue("gd_status")==1){
			return false;
		}
		int num=0;
		float sum=(float)0.0;
		resultJSON.put("view", new JSONObject());
		resultJSON.put("result", true);
		long start=System.currentTimeMillis();
		for(String key:rfidList){
			resultJSON.getJSONObject("view").put(key, new JSONObject());
			String cgid=TF.chipJson.getJSONObject(key).getString("CG_ID");
			boolean flag=false;
			System.out.println("SELECT * FROM tf_mealtimes_chipcategory_relation INNER JOIN commodity_record on tf_mealtimes_chipcategory_relation.CI_ID = commodity_record.CI_ID where tf_mealtimes_chipcategory_relation.CG_ID='"+cgid+"' and tf_mealtimes_chipcategory_relation.MT_ID='"+TF.currentMealTimes+"'");
			List<Map<String, Object>> mealChipList = metaDataBO.queryForListMap("SELECT * FROM tf_mealtimes_chipcategory_relation INNER JOIN commodity_record on tf_mealtimes_chipcategory_relation.CI_ID = commodity_record.CI_ID where tf_mealtimes_chipcategory_relation.CG_ID='"+cgid+"' and tf_mealtimes_chipcategory_relation.MT_ID='"+TF.currentMealTimes+"'");
			if(null!=mealChipList && mealChipList.size()>0){
				resultJSON.getJSONObject("view").getJSONObject(key).put("CDR_TYPE", 2);
				resultJSON.getJSONObject("view").getJSONObject(key).put("use_status", "0");
				resultJSON.getJSONObject("view").getJSONObject(key).put("cgname", mealChipList.get(0).get("CI_NAME"));
				resultJSON.getJSONObject("view").getJSONObject(key).put("price", mealChipList.get(0).get("CI_PRICE"));
				resultJSON.getJSONObject("view").getJSONObject(key).put("member_price", mealChipList.get(0).get("CI_MEMBERPRICE"));
				resultJSON.getJSONObject("view").getJSONObject(key).put("dishId", mealChipList.get(0).get("CI_ID"));
			}else{
				resultJSON.getJSONObject("view").getJSONObject(key).put("CDR_TYPE", 0);
				resultJSON.getJSONObject("view").getJSONObject(key).put("use_status", "0");
				resultJSON.getJSONObject("view").getJSONObject(key).put("cgname", "未知餐具");
				resultJSON.getJSONObject("view").getJSONObject(key).put("price", "0.00");
				resultJSON.getJSONObject("view").getJSONObject(key).put("member_price", "0.00");
				TF.ConsumeJson.put("status", "未知餐具");
			}
			if(TF.preConsumeJson.containsKey(key)){
				resultJSON.getJSONObject("view").getJSONObject(key).put("pay_status", 1);
				payStatus*=1;
			}else{
				resultJSON.getJSONObject("view").getJSONObject(key).put("pay_status", 0);
				sum+=resultJSON.getJSONObject("view").getJSONObject(key).getFloatValue("price");
				num++;
				payStatus*=0;
			}
			if(TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").containsKey(key)){
				resultJSON.getJSONObject("view").getJSONObject(key).put("gd_status", 1);
				TF.ConsumeJson.put("stage_type", 0);
				TF.ConsumeJson.put("status", "挂单餐具");
//				TF.ConsumeJson=JSONObject.parseObject(TF.defaultJSON.toJSONString());
				return false;
			}
		}
		resultJSON.put("total_price", sum);
		resultJSON.put("total_num", num);
		
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("price", resultJSON.getString("total_price"));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("num", resultJSON.getIntValue("total_num"));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").put("view",resultJSON.getJSONObject("view"));
		if(TF.dsJson.getIntValue("discount")!=100){
		TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+resultJSON.getDoubleValue("total_price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
		}else{
			TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+resultJSON.getDoubleValue("total_price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
		}
		TF.ConsumeJson.put("total_num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+resultJSON.getIntValue("total_num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num"));
		if(TF.ConsumeJson.getIntValue("total_num")==0 && TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet().size()>0){
			TF.ConsumeJson.put("stage_type", 0);
			if(payStatus==1)
			TF.ConsumeJson.put("status", "已支付");
			return false;
	}
		return true;
	}
	
	public List readPanel(){
		try{
			RfidUtils rfidUtils = RfidUtils.getInstance();
			List<String> rfidList= new ArrayList<String>();
			rfidList = rfidUtils.findRfidID(TF.rfidJson);
			if(TF.rfidJson.getIntValue("is_check_read")==1){
			for(int i=0;i<TF.rfidJson.getIntValue("check_read_num");i++){
				List<String> repe = rfidUtils.findRfidID(TF.rfidJson);
				if(repe.size()>rfidList.size()){
					rfidList = repe;
				}
			}
			}
			return rfidList;
		}catch(Exception e){
//			String msg=e.getMessage().length()>100?e.getMessage().substring(0, 100):e.getMessage();
//			logger.error("读餐盘出现错误"+msg);
			
			e.printStackTrace();
			return null;
		}
	}
	
}
