package com.kbs.util;

import com.alibaba.fastjson.JSONObject;

public class RandomUtil {
	public static JSONObject getRandom(JSONObject jsonObject){
		String num = String.valueOf((int)(Math.random()*jsonObject.getIntValue("length")+jsonObject.getIntValue("length")));
		while(jsonObject.containsKey(num)){
			num = String.valueOf((int)(Math.random()*jsonObject.getIntValue("length")+jsonObject.getIntValue("length")));
		}
		jsonObject.put(num, num);
		jsonObject.put("result", num);
		return jsonObject;
	}
	public static void main(String[] args) {
		JSONObject random = new JSONObject();
		random.put("length", 100);
		for(int i=0;i<200;i++){
			RandomUtil.getRandom(random);
			System.out.println(random.getString("result"));
		}
		System.out.println("--");
		System.out.println(random.keySet().size());
		System.out.println(random.getString("result"));
	}
}
