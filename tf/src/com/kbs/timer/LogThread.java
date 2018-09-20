package com.kbs.timer;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.HttpClient;
import com.kbs.util.TF;

public class LogThread implements Runnable {
	private MetaDataBO metadataBO = new MetaDataBO();
	@Override
	public void run() {
		
		while(true){
			HttpClient httpClient = new HttpClient();
			try {
				List<Map<String,Object>> list = metadataBO.queryForListMap("select * from ic_cmd_record where ism_status='0'");
				if(list!=null && list.size()>0){
					JSONObject params = new JSONObject();
					String result = httpClient.post(TF.serverUrl+"/cmd/add", JSONArray.toJSONString(list));
					System.out.println(result);
					if(result!=null){
						JSONObject resultJson = JSONObject.parseObject(result);
						if(resultJson.containsKey("data") && resultJson.getJSONArray("data").size()>0){
							String key="";
							for(int i=0;i<resultJson.getJSONArray("data").size();i++){
								key+="'"+resultJson.getJSONArray("data").getString(i)+"',";
							}
							key=key.substring(0,key.length()-1);
							metadataBO.execute("update ic_cmd_record set ism_status='1' where seqno in ("+key+")");
						}
						
					}
				}
				Thread.sleep(60000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	}

}
