package com.kbs.sys;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.MD5;
import com.kbs.util.TF;

public class UserLogin{
//	CommonDataInfo commoDaoInfo = new CommonDataInfo();
//	CommonDao commonDao =new CommonDao();
	MetaDataBO metaDataBO = new MetaDataBO();
	//登录方法
	public JSONObject checklogin(String uName,String uPwd){
//		boolean islogin=false;
		JSONObject resultJson=new JSONObject();
//		commoDaoInfo.setQuerySql("select * from tf_userinfo u where u.ULOGINNAME ='"+uName+"' and  u.ULOGPASSWORD ='"+MD5.KL(uPwd)+"'");
//		commoDaoInfo.setTableName("tf_userinfo");
		Map userMap=metaDataBO.queryForMap("select * from tf_userinfo u where u.ULOGINNO ='"+uName+"' and  u.UPASSWORD ='"+MD5.KL(uPwd)+"'");
//		Map<String,String> userMap =commonDao.queryForMap(commoDaoInfo);
		if(userMap!=null&&userMap.size() >0 && userMap.get("U_ID")!=null){
			resultJson.put("ULOGINNO", userMap.get("ULOGINNO"));
			resultJson.put("U_ID", userMap.get("U_ID"));
			resultJson.put("UNAME", userMap.get("UNAME"));
			resultJson.put("UTYPE", userMap.get("UTYPE"));
			resultJson.put("LOGINTYPE", "1");
			resultJson.put("LOGINTIME", DateTimeUtil.getNowDate());
			resultJson.put("result", true);
		}else{
			resultJson.put("result", false);
			resultJson.put("msg", "用户名或密码错误");
		}
		return resultJson;
	}

}
