package com.kbs.data;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.HttpClient;
import com.kbs.util.TF;

public class CheckOrderThread  extends Thread{
	private MetaDataBO metadataBo=new MetaDataBO();
	
	private HttpClient httpClient=new HttpClient(5);
	@Override
	public void run() {
		
		
			while (true) {
				try{
				if(TF.checkStatus.getIntValue("checkStatus") == 1){
					List<Map<String, Object>> list = metadataBo.queryForListMap("select * from tf_consume_card_record where ccr_status='2' and createtime>'"+DateTimeUtil.getNowSimpleDate()+" 00:00:00'");
					if(null!=list &&list.size()>0){
						for(Map<String, Object> map:list){
							JSONObject dataJson = (JSONObject)JSON.toJSON(map);
							JSONObject paramsJSON = new JSONObject();
							paramsJSON.put("corId", map.get("COR_ID"));
							paramsJSON.put("clientCode", map.get("CLIENT_CODE"));
							paramsJSON.put("storeCode", map.get("STORE_CODE"));
							String result = httpClient.post(TF.serverUrl+"/pay/result/select",paramsJSON.toJSONString());
							System.out.println("result"+result);
							if(result!=null){
								JSONObject resultJson  = JSONObject.parseObject(result);
								if(resultJson.getString("code").equals("0000")){
									metadataBo.execute("update tf_consume_card_record set ccr_status='1' where ccr_id='"+map.get("CCR_ID")+"'");
								}
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					Thread.sleep(1000*30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		
		
		
	}
	
}
