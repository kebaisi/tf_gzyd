package com.kbs.timer;

import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import com.kbs.util.HttpClient;
import com.kbs.util.TF;


public class HeartTimer extends TimerTask {

	@Override
	public void run() {
		HttpClient httpClient = new HttpClient();
		String result = httpClient.get(TF.serverUrl+"/server/heart");
		if(StringUtils.isNotEmpty(result)){
			TF.checkStatus.put("checkStatus", "1");
		}else{
			TF.checkStatus.put("checkStatus", "0");
		}
	}
	
}
