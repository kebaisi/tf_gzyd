package com.kbs.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CheckProcessIsRunning {
	public static boolean checkIsRunning(String processName){
		boolean flag=false;
		try{
			Process p = Runtime.getRuntime().exec( "cmd /c tasklist ");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream os = p.getInputStream();
			byte b[] = new byte[256];
			while(os.read(b)> 0){
				baos.write(b);
			}
			String s = baos.toString();
			// System.out.println(s);
			if(s.indexOf(processName.trim())>=0){
				flag=true;
			}
			else{
				flag=false;
			}
		}catch(java.io.IOException ioe){
		}
		return flag;
	}
	public static  int  checkThreadNum(String processName){
		 int result = 0;
		try{
			Process p = Runtime.getRuntime().exec( "cmd /c tasklist ");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream os = p.getInputStream();
			byte b[] = new byte[256];
			while(os.read(b)> 0){
				baos.write(b);
			}
			String s = baos.toString();
//			 System.out.println(new String(s.getBytes("ISO-8859-1"),"gb2312"));
			if(s.indexOf(processName.trim())>=0){
				result = s.split(processName).length;
			}
			
		}catch(java.io.IOException ioe){
		}
		return result;
	}
	public static void main(String[] args) {
		System.out.println(CheckProcessIsRunning.checkIsRunning("mysqld.exe"));
	}
}
