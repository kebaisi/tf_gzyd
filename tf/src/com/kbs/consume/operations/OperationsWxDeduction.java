package com.kbs.consume.operations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.cashbox.CashBoxUtils;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.service.ConsumeService;
import com.kbs.util.AudioPlay;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.HttpClient;
import com.kbs.util.RandomUtil;
import com.kbs.util.TF;

public class OperationsWxDeduction implements IOperationsDeduction{
	private Logger logger=Logger.getLogger(OperationsWxDeduction.class);
	private MetaDataBO metadataBo=new MetaDataBO();
	private float consumeMoney=(float)0;
	JSONObject tempConsumeJSON=new JSONObject();
	private MetaDataBO metaDataBo=new MetaDataBO();
	public static boolean isConsume=false;
	@Override
	public synchronized boolean deduction(JSONObject jsonObject) {
		
		
		
		
		return false;
	}

	
	
}
