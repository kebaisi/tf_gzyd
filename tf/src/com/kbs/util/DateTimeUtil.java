package com.kbs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getNowDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(System.currentTimeMillis()));
		
	}
	public static String getNowSimpleDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(System.currentTimeMillis()));
		
	}
	public static String getNowDate(String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date(System.currentTimeMillis()));
	}
	/**
	   * 计算两个日期之间相差的秒
	   * @param date1 
	   * @param date2 
	   * @return 
	   */  
	  public static int misBetween(Date date1,Date date2)  
	  {  
	      Calendar cal = Calendar.getInstance();  
	      cal.setTime(date1);  
	      Long time1 = cal.getTimeInMillis();  
	      cal.setTime(date2);  
	      Long time2 = cal.getTimeInMillis();   
	      Long between_days=(time2-time1)/(1000);    
	     return Integer.parseInt(String.valueOf(between_days));         
	  }
	  public static Long dateDiff(String startTime, String endTime,     
	            String format, String str) {     
	        // 按照传入的格式生成一个simpledateformate对象     
	        SimpleDateFormat sd = new SimpleDateFormat(format);     
	        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数     
	        long nh = 1000 * 60 * 60;// 一小时的毫秒数     
	        long nm = 1000 * 60;// 一分钟的毫秒数     
	        long ns = 1000;// 一秒钟的毫秒数     
	        long diff;     
	        long day = 0;     
	        long hour = 0;     
	        long min = 0;     
	        long sec = 0;     
	        // 获得两个时间的毫秒时间差异     
	        try {     
	            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();     
	            day = diff / nd;// 计算差多少天     
	            hour = diff % nd / nh + day * 24;// 计算差多少小时     
	            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟     
	            sec = diff % nd % nh % nm / ns;// 计算差多少秒     
	            // 输出结果     
	            System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"    
	                    + (min - day * 24 * 60) + "分钟" + sec + "秒。");     
	            System.out.println("hour=" + hour + ",min=" + min);     
	            if (str.equalsIgnoreCase("h")) {     
	                return hour;     
	            } else if(str.equals("s")) {     
	                return diff/1000;     
	            }     
	    
	        } catch (Exception e) {     
	            // TODO Auto-generated catch block     
	            e.printStackTrace();     
	        }     
	        if (str.equalsIgnoreCase("h")) {     
	            return hour;     
	        } else {     
	            return min;     
	        }     
	    }  
	  
	  public static String getDateStrFromDate(Object date){
		  return simpleDateFormat.format(date);
	  }
	  
	  public static String changeMillsToTime(long mills){
		  return simpleDateFormat.format(new Date(mills));
	  }
	  public static String changeMillsToTime(long mills,String format){
		  SimpleDateFormat sim = new SimpleDateFormat(format);
		  return sim.format(new Date(mills));
	  }
}
