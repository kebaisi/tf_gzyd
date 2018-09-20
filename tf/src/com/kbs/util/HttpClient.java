package com.kbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class HttpClient{
	RequestConfig defaultRequestConfig;
	CloseableHttpClient httpClient;
	
 public static void main(String[] args) {
	 HttpClient httpClient = new HttpClient(3);
//	 httpClient.uploadFile();
//	 JSONObject json=new JSONObject();
//	 json.put("IC_ID", "2");
//	 json.put("U_ID", "1");
//	 String str= httpClient.operateRequest("postform", "http://192.168.1.103:8080/tf-dsc/card/logout", json.toJSONString());
//	 System.out.println(str);
//	 System.out.println(httpClient.get("http://192.168.1.109:8080/tf-dsc/company/list"));
//	 System.out.println(httpClient.operateRequest("get","http://192.168.1.109:8080/tf-dsc/company/list",null));
//	 System.out.println(httpClient.operateRequest("get","http://192.168.1.109:8080/tf-dsc/companyBranch/company/00011",null));
//	 System.out.println(httpClient.operateRequest("get","http://www.baidu.com",null));
 }

 	public HttpClient(){
 		defaultRequestConfig= RequestConfig.custom().setSocketTimeout(TF.dsJson.getIntValue("httpTimeOut")*1000).setConnectTimeout(TF.dsJson.getIntValue("httpTimeOut")*1000).setConnectionRequestTimeout(TF.dsJson.getIntValue("httpTimeOut")*1000).setStaleConnectionCheckEnabled(true).build();
 		httpClient= HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
 	}
 	
 	public HttpClient(int second){
 		defaultRequestConfig= RequestConfig.custom().setSocketTimeout(second*1000).setConnectTimeout(second*1000).setConnectionRequestTimeout(second*1000).setStaleConnectionCheckEnabled(true).build();
 		httpClient= HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
 	}
	
	/**
	 * post方式提交表单（模拟用户登录请求）
	 */
	public String post(String url,String content) {
		StringBuffer stringBuffer =  new StringBuffer();
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// 创建httppost  
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("data", content));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					stringBuffer.append(EntityUtils.toString(entity, "UTF-8"));
				}
				return stringBuffer.toString();
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(null, "无法连接服务器", "message", JOptionPane.OK_OPTION);
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public JSONObject postForm(String url,String params_name,String content) {
		JSONObject resultJson=new JSONObject();
		resultJson.put("result", true);
		StringBuffer stringBuffer =  new StringBuffer();
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		// 创建httppost  
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(params_name, content));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					stringBuffer.append(EntityUtils.toString(entity, "UTF-8"));
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(null, "无法连接服务器", "message", JOptionPane.OK_OPTION);
			resultJson.put("result", false);
			resultJson.put("msg", "无法连接服务器");
			resultJson.put("type", 10);
//			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			resultJson.put("data", JSONObject.parse(stringBuffer.toString()));
		} catch (Exception e) {
			resultJson.put("result", false);
		}
	
		return resultJson;
	}
	

	/**
	 * 发送 get请求
	 */
	public String get(String url) {
		StringBuffer stringBuffer = new StringBuffer();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
		try {
			// 创建httpget.  
			HttpGet httpget = new HttpGet(url);
			// 执行get请求.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
				// 打印响应状态  
				if (entity != null) {
					// 打印响应内容长度  
					// 打印响应内容  
					stringBuffer.append(EntityUtils.toString(entity));
					return stringBuffer.toString();
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

	public JSONObject upload(String url,String filePath){
		JSONObject resultJson=new JSONObject();
		resultJson.put("result", true);
		HttpEntity httpEntity;
		try {
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
			HttpPost httpPost = new HttpPost(url);
//		Log.debug("post url:"+url);
			httpPost.setHeader("User-Agent","SOHUWapRebot");
			httpPost.setHeader("Accept-Language","zh-cn,zh;q=0.5");
			httpPost.setHeader("Accept-Charset","GBK,utf-8;q=0.7,*;q=0.7");
			httpPost.setHeader("Connection","keep-alive");
			 
			MultipartEntity mutiEntity = new MultipartEntity();
			File file = new File(filePath);
			mutiEntity.addPart("desc",new StringBody("123", Charset.forName("utf-8")));
			mutiEntity.addPart("file", new FileBody(file));
			 
			 
			httpPost.setEntity(mutiEntity);
			HttpResponse  httpResponse = httpclient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			resultJson.put("data", EntityUtils.toString(httpEntity));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			resultJson.put("result", false);
			resultJson.put("msg", "连接服务器失败");
		}
		return resultJson;
	}

	

	
//	public JSONObject operateRequest(String type,String url,String content){
//		JSONObject resultJson=new JSONObject();
//		try {
////			if(TF.checkStatus.getIntValue("checkStatus")==0){
////				if(url.indexOf("/tf-dsc/server/login")!=-1||url.indexOf("/tf-dsc/card/config/syn/")!=-1||url.indexOf("/tf-dsc/server/dictionary")!=-1){
////					
////				}else{
//////				JOptionPane.showMessageDialog(null, "当前离线", "message", JOptionPane.OK_OPTION);
////					resultJson.put("result", false);
////					resultJson.put("msg", "当前离线");
////					return resultJson;
////				}
////			}
//			if(type.equals("get")){
//				resultJson= get(url);
//			}else if(type.toLowerCase().equals("postform")){
//				resultJson= postForm(url, content);
//			}else if(type.equals("upload")){
//				resultJson= upload(url, content);
//			}else{
//				resultJson.put("result", false);
//			}
//			if(resultJson.getBooleanValue("result")){
////					resultJson.put("data", JSONObject.parseObject(resultJson.getString("data")).getJSONArray("data"));
//				resultJson=JSONObject.parseObject(resultJson.getString("data"));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			resultJson.put("result", false);
//		}
//		return resultJson;
//	}
}