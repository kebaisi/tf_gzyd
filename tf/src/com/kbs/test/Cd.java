package com.kbs.test;

import com.alibaba.fastjson.JSONObject;
import com.kbs.util.HttpClient;

public class Cd {
	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();
		JSONObject paramsJson= new JSONObject();
		paramsJson.put("miId", "171021192-0000-13600148615");
		String result = httpClient.post("http://192.168.1.199:9999/k-occ/member/info/out", paramsJson.toJSONString());
		System.out.println(result);
	}
}
