package com.kbs.sys;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.swing.frame.LoginPanel;
import com.kbs.swing.frame.MainFrame;
import com.kbs.swing.frame.MenuPanel;
import com.kbs.swing.frame.SalePanel;
import com.kbs.util.ConfigPro;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.HttpClient;
import com.kbs.util.TF;

public class LoginService{
	private LoginPanel loginFrame;
	private Logger log = Logger.getLogger(LoginService.class);
	private HttpClient httpClient=new HttpClient();
	Properties props = ConfigPro.getInstance().getProperties();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date=new Date();
	String cuDateStr=sdf.format(date)+":";
	String serverTime="";
	javax.swing.Timer displayTimer;
	javax.swing.Timer checkStatusTimer;
	javax.swing.Timer loadMainTimer;
	JSONObject statusJson=new JSONObject();
	private MetaDataBO metaDataBo=new MetaDataBO();
	private MainFrame mainFrame;
	JSONArray taskArr=new JSONArray();
	
	public javax.swing.Timer getLoadMainTimer() {
		return loadMainTimer;
	}

	public void setLoadMainTimer(javax.swing.Timer loadMainTimer) {
		this.loadMainTimer = loadMainTimer;
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public LoginService(MainFrame mainFrame){
		this.mainFrame=mainFrame;
		this.loginFrame=(LoginPanel) mainFrame.getMainPanel();
		if(loginFrame.getTxt_userName().getText().equals("root")&&loginFrame.getTxt_pwd().getText().equals("root")){
			MenuPanel menuFrame=new MenuPanel(mainFrame);
			mainFrame.addJPanelToMain(menuFrame);
			cancleTimer();
			TF.userJson.put("LOGINTYPE", "0");
			TF.userJson.put("ULOGINNO", "root");
			TF.userJson.put("UNAME", "root");
			TF.userJson.put("UTYPE", 2);
			return;
		}
		statusJson.put("msg", "初始化中");
		addTask();
		ProGressWork work = new ProGressWork();  
        //work.execute();  
	}
	
	private String getLocalHostIp(){
		String result="";
		 try {
			String name = InetAddress.getLocalHost().getHostName();
			// 获取IP地址
		    result = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.error("获取本机IP失败");
			//加入异常信息表
		}
	     return result;
	}
	
	
	 /** 
     * 通过调用本地命令date和time修改计算机时间 
     * @param date 
     */  
    private boolean setComputeDate(String datetime) throws Exception {  
    	date=sdf.parse(datetime);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));  
        c.setTime(date);  
        int year = c.get(Calendar.YEAR);  
        int month = c.get(Calendar.MONTH) + 1;  
        int day = c.get(Calendar.DAY_OF_MONTH);  
        int hour = c.get(Calendar.HOUR_OF_DAY);  
        int minute = c.get(Calendar.MINUTE);  
        int second = c.get(Calendar.SECOND);  
  
        c.setTime(new Date());  
        int year_c = c.get(Calendar.YEAR);  
        int month_c = c.get(Calendar.MONTH) + 1;  
        int day_c = c.get(Calendar.DAY_OF_MONTH);  
        int hour_c = c.get(Calendar.HOUR_OF_DAY);  
        int minute_c = c.get(Calendar.MINUTE); 
        int second_c=c.get(Calendar.SECOND);
  
        String ymd = year + "-" + month + "-" + day;  
        String time = hour + ":" + minute + ":" + second;  
        try {  
            // 日期不一致就修改一下日期  
            if (year != year_c || month != month_c || day != day_c) {  
                String cmd = "cmd /c date " + ymd;  
                Process process = Runtime.getRuntime().exec(cmd);  
                int i=process.waitFor();  
                if(i!=0){
                	return false;
                }
            }  
  
            // 时间不一致就修改一下时间  
            if (hour != hour_c || minute != minute_c||second!=second_c) {  
                String cmd = "cmd  /c  time " + time;  
                Process process = Runtime.getRuntime().exec(cmd);  
                int i=process.waitFor();  
                if(i!=0){
                	return false;
                }
            }  
        } catch (IOException ex) {  
        	ex.printStackTrace();
        	return false;
        } catch (InterruptedException ex) {  
        	ex.printStackTrace();
        	return false;
        } 
        return true;
    } 
    
//    public void initProgress(){
//    	loginFrame.getLoadingProgressBar().setValue(0);
//    	loginFrame.getLabel_status().setText("");
//    }
    
//    public boolean btnChangeTime(){
//		int i=JOptionPane.showConfirmDialog(loginFrame, "服务器时间 :\n\t"+serverTime+"\n本地时间 :\n\t"+DateTimeUtil.getNowDate(),"矫正时间",JOptionPane.YES_NO_OPTION);
//		if(i==0){
//			try {
//				if(serverTime!=null){
//					boolean b=setComputeDate(serverTime);
//					if(b){
////						statusJson.put("value", 66.6);
////						TF.checkStatus.put("checkSysDate",1);
//						return true;
//					}
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return false;
//	}
    
    
    /**
     * 检测本机连接服务器状态
     * @return
     */
    public int checkServer(){
    	statusJson.put("msg", "检测机器环境中。。。");
    	if(TF.checkStatus.getIntValue("checkStatus")==1){
    		return 1;
    	}else if(TF.checkStatus.getIntValue("checkLoginStatus")==3){
//			JOptionPane.showMessageDialog(loginFrame, "机器号为空，请先注册后再使用","消息",JOptionPane.OK_OPTION);
    		if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
    		statusJson.getJSONObject("error").put("title", "机号检测");
			statusJson.getJSONObject("error").put("content", "机号为空，请先注册后再使用");
//			btnSetting();
//			statusJson.put("msg", "");
			return 3;
		}else if(TF.checkStatus.getIntValue("checkLoginStatus")==4){
//			JOptionPane.showMessageDialog(loginFrame, "当前机号被禁止登录","消息",JOptionPane.OK_OPTION);
			if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
			statusJson.getJSONObject("error").put("title", "机号检测");
			statusJson.getJSONObject("error").put("content", "当前机号被禁止登录");
//			statusJson.put("msg", "");
			return 3;
		}else if(TF.checkStatus.getIntValue("checkLoginStatus")==5){
//			JOptionPane.showMessageDialog(loginFrame, "当前机号正在使用中,请释放连接后重新登录","消息",JOptionPane.OK_OPTION);
			if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
			statusJson.getJSONObject("error").put("title", "机号检测");
			statusJson.getJSONObject("error").put("content", "当前机号正在使用中，请释放连接后重新登录");
//			statusJson.put("msg", "");
			return 3;
		}else if(TF.checkStatus.getIntValue("checkLoginStatus")==2){
    		if(TF.dsJson.getString("machineId")==null||TF.dsJson.getString("machineId").equals("")){
//    			JOptionPane.showMessageDialog(loginFrame, "未发现机号，请连接服务器，注册设备号");
    			if(statusJson.getJSONObject("error")==null){
        			statusJson.put("error", new JSONObject());
        		}
    			statusJson.getJSONObject("error").put("title", "机号检测");
    			statusJson.getJSONObject("error").put("content", "未发现机号，请连接服务器，注册设备号");
//    			statusJson.put("msg", "");
    			return 3;
    		}
    		return 1;
    	}
    	return 1;
    }
    
    public int synData(JSONObject json){
    	String id=json.getString("JT_ID");
    	String tableName=json.getString("JT_NAME");
    	statusJson.put("msg", "同步"+tableName+"中...");
    	Map map=metaDataBo.queryForMap("select * from job_timer where JT_ID='"+id+"'");
    	if(map==null||map.size()<1){
    		statusJson.getJSONObject("error").put("title", "服务异常");
			statusJson.getJSONObject("error").put("content", "服务异常,job_timer表中没有"+tableName+"记录");
			return 3;
    	}
    	String status=(String) map.get("JT_STATUS");
    	if(status.equals("2")){
    		return 1;
    	}else if(status.equals("1")){
    		return 2;
    	}else if(status.equals("3")){
    		return 3;
    	}
    	if(statusJson.getJSONObject("error")==null){
			statusJson.put("error", new JSONObject());
		}
		statusJson.getJSONObject("error").put("title", "同步异常");
		statusJson.getJSONObject("error").put("content", "同步"+tableName+"异常");
    	return 3;
    }
    
    public int checkLogin(){
    	statusJson.put("msg", "校验用户名密码。。。");
		UserLogin user=new UserLogin();
		//隐性用户用于配置相关信息
		try {
			JSONObject checkLoginJson=user.checklogin(loginFrame.getTxt_userName().getText(), loginFrame.getTxt_pwd().getText());
			boolean islogin =checkLoginJson.getBooleanValue("result");
			if(islogin){
				TF.userJson=checkLoginJson;
				return 1;
			}else{
//				JOptionPane.showMessageDialog(loginFrame,checkLoginJson.getString("msg"), "提示",JOptionPane.ERROR_MESSAGE);
				if(statusJson.getJSONObject("error")==null){
	    			statusJson.put("error", new JSONObject());
	    		}
				statusJson.getJSONObject("error").put("title", "登录校验");
    			statusJson.getJSONObject("error").put("content", checkLoginJson.getString("msg"));
				return 3;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
			statusJson.getJSONObject("error").put("title", "登录校验");
			statusJson.getJSONObject("error").put("content", "错误:"+e.getMessage());
		}
//		loginFrame.getBtn_login().addActionListener(listener);
//		loginFrame.getBtn_login().registerKeyboardAction(listener,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		return 3;
	}
    
    public int checkLoginAccess(){
    	statusJson.put("msg", "验证用户权限中");
    	if(TF.userJson.getIntValue("UTYPE")==0){
//    		JOptionPane.showMessageDialog(loginFrame, "充值员禁止登陆");
    		if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
    		statusJson.getJSONObject("error").put("title", "验证用户权限");
			statusJson.getJSONObject("error").put("content", "充值员禁止登录");
    		return 3;
    	}
    	return 1;
    }
    
    public int confirmToMainWhenNotOnline(){
		int i=JOptionPane.showConfirmDialog(loginFrame, "**未连接上服务器、继续使用将有一定风险**、是否进入系统","离线登录确认",JOptionPane.YES_NO_OPTION);
		if(i==0){
			return 1;
		}else {
			statusJson.put("msg", "");
			if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    			statusJson.getJSONObject("error").put("title", "离线登录服务器");
    		}
			statusJson.getJSONObject("error").put("content", "用户取消");
			statusJson.getJSONObject("error").put("status", 1);
			return 3;
		}
    }
    
    public void addTask(){
//    	int index=0;
//    	JSONObject checkServerJson=new JSONObject();
//    	checkServerJson.put("method", getMethod("checkServer", null));
//		JSONObject checksysDate=new JSONObject();
//		checksysDate.put("method", getMethod("checksysDate", null));
////		JSONObject json3=new JSONObject();
////		json3.put("method", getMethod("synData", null));
//		JSONObject checkLoginJson=new JSONObject();
//		checkLoginJson.put("method", getMethod("checkLogin", null));
//		JSONObject checkLoginAccessJson=new JSONObject();
//		checkLoginAccessJson.put("method", getMethod("checkLoginAccess", null));
//		JSONObject confirmToMainWhenNotOnlineJson=new JSONObject();
//		confirmToMainWhenNotOnlineJson.put("method", getMethod("confirmToMainWhenNotOnline", null));
//		if(TF.checkStatus.getIntValue("checkStatus")==1){
//			taskArr.add(index++,checkServerJson);
//			taskArr.add(index++,checksysDate);
//			index=synData(index);
//			taskArr.add(index++,checkLoginJson);
//			taskArr.add(index++,checkLoginAccessJson);
//		}else{
//			taskArr.add(index++,checkServerJson);
//			taskArr.add(index++,checkLoginJson);
//			taskArr.add(index++,confirmToMainWhenNotOnlineJson);
//			taskArr.add(index++,checkLoginAccessJson);
//		}
//		loginFrame.getLoadingProgressBar().setMaximum(taskArr.size());
//		loginFrame.getLoadingProgressBar().setValue(0);
    }
    
//    public void runTask(JSONArray arr){
//    	for(int i=0;i<arr.size();i++){
//    		if(!arr.getJSONObject(i).getBooleanValue("result")){
//	    		int status=runTask(arr.getJSONObject(i));
//	    		if(status==1){
////	    			loginFrame.getLoadingProgressBar().setValue(i+1);
//	    			statusJson.put("percent", i+1);
//	    			arr.getJSONObject(i).put("result", true);
//	    		}else if(status==2){
//	    			i--;
//	    			arr.getJSONObject(i).put("result", false);
//	    			continue;
//	    		}else if(status==3){
////	    			loginFrame.getLoadingProgressBar().setValue(arr.size());
//	    			statusJson.put("percent", arr.size());
//	    			try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	    			cancleTimer();
//	    			return;
//	    		}
//    		}
////    		loginFrame.getLoadingProgressBar().setValue(i+1);
//    	}
//    	cancleTimer();
//    }
    
    public int runTask(JSONObject json){
    	int flag=0;
    	String methodName="";
    	try {
//    		json=arr.getJSONObject(i);
    		Method method=(Method) json.get("method");
    		methodName=method.getName();
    		Object paras=json.get("paras");
    		Object resultObj;
    		if(paras==null){
    			resultObj= method.invoke(LoginService.this,null);
    		}else{
    			resultObj= method.invoke(LoginService.this,paras);//调用o对象的方法
    			long time=System.currentTimeMillis();
    			if(resultObj instanceof Integer){
    	         	   flag=(int) resultObj;
    	        }
//    			if(flag==2){
//    				return checkIsSynData(json, time);
//    			}
    		}
            if(resultObj instanceof Integer){
         	   flag=(int) resultObj;
            }
        } catch (Exception ex) {
        	if(statusJson.getJSONObject("error")==null){
    			statusJson.put("error", new JSONObject());
    		}
        	statusJson.getJSONObject("error").put("title", "运行异常");
			statusJson.getJSONObject("error").put("content", "运行"+methodName+"方法时发生异常,错误信息:"+ex.getMessage());
            return 3;
        }
    	return flag;
    }
    
//    public int checkIsSynData(JSONObject json,long time){
//    	boolean b=false;
//    	int flag=2;
//    	while(!b){
//	    	try {
//				Method method=(Method) json.get("method");
//				Object paras=json.get("paras");
//				Object resultObj= method.invoke(LoginService.this,paras);//调用o对象的方法
//				if(resultObj instanceof Integer){
//		         	   flag=(int) resultObj;
//		            }
//				if(flag==2){
//					if(System.currentTimeMillis()-time>=15*60*1000){
//						if(statusJson.getJSONObject("error")==null){
//			    			statusJson.put("error", new JSONObject());
//			    		}
//						statusJson.getJSONObject("error").put("title", "同步数据");
//		    			statusJson.getJSONObject("error").put("content", "同步超时");
//						return 3;
//					}
//					Thread.sleep(100);
//				}else {
//					return flag;
//				}
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				if(statusJson.getJSONObject("error")==null){
//	    			statusJson.put("error", new JSONObject());
//	    		}
//				statusJson.getJSONObject("error").put("title", "同步数据");
//    			statusJson.getJSONObject("error").put("content", "出现异常，"+e.getMessage());
//				return 3;
//			}
//    	}return flag;
//    }
    
    public void cancleTimer(){
//    	checkStatusTimer.setRepeats(false);
//		checkStatusTimer.stop();
//		displayTimer.setRepeats(false);
//		displayTimer.stop();
    }
    
    public Method getMethod(String MethodName,Object []paras){
    	Method method=null;
          Class c[]=null;
          if(paras!=null){//存在
              int len=paras.length;
              c=new Class[len];
              for(int i=0;i<len;++i){
                  c[i]=paras[i].getClass();
              }
          }
         try {
             method=LoginService.this.getClass().getDeclaredMethod(MethodName,c);
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return method;
     }
    
    class ProGressWork extends SwingWorker<Integer, Integer> {  
        @Override  
        protected Integer doInBackground() throws Exception {  
        	while(!statusJson.getBooleanValue("isRunning")){
        		statusJson.put("isRunning", true);
	        	for(int i=0;i<taskArr.size();i++){
	        		if(!taskArr.getJSONObject(i).getBooleanValue("result")){
	    	    		int status=runTask(taskArr.getJSONObject(i));
	    	    		if(status==1){
	//    	    			loginFrame.getLoadingProgressBar().setValue(i+1);
	    	    			statusJson.put("percent", i+1);
	    	    			taskArr.getJSONObject(i).put("result", true);
	    	    		}else if(status==2){
//	    	    			i--;
//	    	    			taskArr.getJSONObject(i).put("result", false);
//	    	    			continue;
	    	    			if(statusJson.getLongValue("startTime")==0){
	    	    				statusJson.put("startTime", System.currentTimeMillis());
	    	    			}else{
		    	    			if(System.currentTimeMillis()-statusJson.getLongValue("startTime")>=15*60*1000){
		    						if(statusJson.getJSONObject("error")==null){
		    			    			statusJson.put("error", new JSONObject());
		    			    		}
		    						statusJson.getJSONObject("error").put("title", "同步数据");
		    		    			statusJson.getJSONObject("error").put("content", "同步超时");
		    						return 3;
		    					}
	    	    			}
	    	    			String tableName=taskArr.getJSONObject(i).getJSONObject("paras").getString("JT_NAME");
	    	    			statusJson.put("msg", "同步"+tableName+"中...");
	    	    			publish(statusJson.getIntValue("percent"));
	    	    			statusJson.put("isRunning", false);
	    	    			Thread.sleep(500);
	    	    			break;
	    	    		}else if(status==3){
	//    	    			loginFrame.getLoadingProgressBar().setValue(arr.size());
	    	    			statusJson.put("percent", taskArr.size());
	    	    			publish(statusJson.getIntValue("percent"));
	    	    			loginFrame.getErrorMsgPanel().getLabel_errorTitle().setText(statusJson.getJSONObject("error").getString("title"));
	    	    			loginFrame.getErrorMsgPanel().getEditorPane().setText(statusJson.getJSONObject("error").getString("content"));
	    	    			loginFrame.getErrorMsgPanel().setVisible(true);
	    	    			return 2;
	    	    		}
	        		}
	        		publish(statusJson.getIntValue("percent"));
	        	}
        	}
        	Thread.sleep(500);
        	if(TF.userJson.getIntValue("UTYPE")==1){
//        		loginFrame.getLoadingProgressBar().setValue(taskArr.size());
        		statusJson.put("percent", taskArr.size());
        		SalePanel saleFrame=new SalePanel(mainFrame);
        		TF.dsJson.put("title", "运营");
        		mainFrame.addJPanelToMain(saleFrame);
        		return 1;
        	}else if(TF.userJson.getIntValue("UTYPE")==2){
//        		loginFrame.getLoadingProgressBar().setValue(taskArr.size());
        		statusJson.put("percent", taskArr.size());
        		MenuPanel menuFrame=new MenuPanel(mainFrame);
        		TF.dsJson.put("title", "主界面");
        		mainFrame.addJPanelToMain(menuFrame);
        		return 1;
        	}
        	return 1;
        }  
        //调用publist的时候会调用  
        //注意这里是"批处理"  
        @Override  
        protected void process(List<Integer> list) {  
//            for (Work work : works) {  
////                bar.setValue(work.getId());  
//            }  
        	for(Integer i:list){
        		System.out.println("i---"+i);
        		loginFrame.getLoadingProgressBar().setValue(i);
        		loginFrame.getLabel_status().setText(statusJson.getString("msg"));
        	}
        }  
        @Override  
        protected void done() {  
            loginFrame.getLabel_status().setText("工作已全部完成");  
        }  
    }  
    
    public static void main(String[] args) throws Exception{
		LoginService welcome=new LoginService(null);
		Date date = null; 
		String dateStr = "2010-9-10"; 
		String[ ]  dateDivide = dateStr.split("-"); 
		if(dateDivide.length==3){  
			int year = Integer.parseInt(dateDivide [0].trim());//去掉空格  
	        int month = Integer.parseInt(dateDivide [1].trim());  
	        int day = Integer.parseInt(dateDivide [2].trim());  
	        Calendar c = Calendar.getInstance();//获取一个日历实例  
	        c.set(year, month-1, day);//设定日历的日期  
	        date = c.getTime();  
		}  
//		welcome.setComputeDate(date);
	}
}
