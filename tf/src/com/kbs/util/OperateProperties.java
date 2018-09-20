package com.kbs.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * TODO Comment of JProperties
 * 
 * @author dengsilinming
 * @version $Id: JProperties.java 2013-1-30 下午1:34:32 $
 */
public class OperateProperties {
    //行读取config.properties
    public static String readTxtFile(){
    	StringBuilder lineTxt = new StringBuilder();
        try {
            String encoding="UTF8";
//          InputStream in =OperateProperties.class.getClassLoader().getResourceAsStream(filePath);
            InputStream in = new BufferedInputStream (new FileInputStream(ConfigPro.CONFIG_FILE));;
        	InputStreamReader read = new InputStreamReader(in,encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String s="";
            while((s = bufferedReader.readLine()) != null){
            	lineTxt.append(s+"\n");
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineTxt.toString();
    }
    
    public static boolean writeTxtFile(String content,String  fileName)throws Exception{  
  	  RandomAccessFile mm=null;  
  	  boolean flag=false;  
  	  FileOutputStream o=null;  
  	  try {  
  		File file = new File(fileName);  
  	    o = new FileOutputStream(file);  
  	    o.write(content.getBytes("UTF8"));  
  	    o.close();  
  	    flag=true;  
  	  } catch (Exception e) {  
  	    e.printStackTrace();  
  	  }finally{  
  	   if(mm!=null){  
  	    mm.close();  
  	   }  
  	  }  
  	  return flag;  
   } 
    public static void main(String[] args) {
		System.out.println(OperateProperties.readTxtFile());
	}
}