package com.kbs.sys;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.TF;

public class CacheService {
	private static MetaDataBO metaDataBO=new MetaDataBO();
	private static Logger log=Logger.getLogger(CacheService.class);
	public void load(){
		try {
		//加载店铺信息
		List<Map<String,Object>> storelist = metaDataBO.queryForListMap("select * from tf_store_record");
		if(null==storelist || storelist.size()==0){
			TF.startEnvJson.put("code", "2");
			TF.startEnvJson.put("msg", "未加载到店铺信息<<程序无法启动>>");
			return;
		}else{
			TF.storeJson = (JSONObject)JSON.toJSON(storelist.get(0));
		}
		//加载餐次信息
		List<Map<String, Object>> mealList = metaDataBO.queryForListMap("select * from tf_mealtimes");
		if(null==storelist || storelist.size()==0){
			TF.startEnvJson.put("code", "2");
			TF.startEnvJson.put("msg", "未加载到餐次信息<<程序无法启动>>");
			return;
		}else{
			TF.mealList = mealList;
		}
		//加载餐具芯片信息
		List<Map<String, Object>> chipList = metaDataBO.queryForListMap("select * from tf_chipinfo");
		if(null!=chipList && chipList.size()>0){
			for(Map<String,Object> map:chipList){
				TF.chipJson.put(map.get("CP_NO").toString(), map);
			}
		}
		//加载餐具类型
		List<Map<String, Object>> chipCategoryList = metaDataBO.queryForListMap("select * from tf_chip_category");
		if(null!=chipCategoryList && chipCategoryList.size()>0){
			for(Map<String,Object> map:chipCategoryList){
				TF.chipCategoryJson.put(map.get("CG_ID").toString(), map);
			}
		}
		//加载餐具商品信息
		List<Map<String, Object>> mealChipList= metaDataBO.queryForListMap("select * from tf_mealtimes_chipcategory_relation");
		if(null!=mealChipList && mealChipList.size()>0){
			for(Map<String,Object> map:mealChipList){
				TF.mealChipJson.put(map.get("CG_ID").toString(), map);
			}
		}
		//加载商品信息
		List<Map<String, Object>> commodityList= metaDataBO.queryForListMap("select * from commodity_record");
		if(null!=commodityList && commodityList.size()>0){
			TF.commodityList = commodityList;
			for(Map<String,Object> map:commodityList){
				TF.commodity_CI_Json.put(map.get("CI_ID").toString(), map);
				TF.commodity_CP_Json.put(map.get("CI_NO").toString(), map);
			}
		}
		//加载机器配置信息
		List<Map<String, Object>> meterList= metaDataBO.queryForListMap("select * from tf_meter_record");
		if(null!=meterList && meterList.size()>0){
			TF.meterJson = (JSONObject)JSON.toJSON(meterList.get(0));
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//加载店铺配置信息
				List<Map<String, Object>> storeConfigList= metaDataBO.queryForListMap("select * from store_config");
				if(null!=storeConfigList && storeConfigList.size()>0){
					TF.storeConfig = (JSONObject)JSON.toJSON(storeConfigList.get(0));
				}
	}
	
}
