package com.kbs.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataConvertUtil {
	public static Map<String,String> objectMapConvetStringMap(Map<String,Object> map){
		Map<String,String> dataMap = new HashMap<String,String>();
		for(String key:map.keySet()){
			dataMap.put(key, String.valueOf(map.get(key)));
		}
		return dataMap;
	}
	public static String moneryFormat(double value){
		DecimalFormat decimalFormat = new DecimalFormat(".#"); 
		return String.valueOf(Double.parseDouble(decimalFormat.format(value)));
	}
	
	public static boolean checkIsPageNum(String str){
		Pattern pattern = Pattern.compile("[1-9]{1}[0-9]*");  
		Matcher matcher = pattern.matcher(str);    
		while(matcher.matches()) {  
			return true;
		}  
		return false;
	}
	
	/**
	 * 字符串转16进制
	 * @param bin
	 * @return
	 */
	public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }
 
    /**
     * 十六进制转换字符串
     * @param hex String 十六进制
     * @return String 转换后的字符串
     */
    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }
   public static String hexTwoData(String hex){
	   if(hex.length()%2!=0){
		   hex="0"+hex;
	   }
	   return hex;
   }
   /**
    * 16进制取反
    * @return
    */
	   public static String hexAntiCode(String hex){
	   int temp = Integer.parseInt(hex, 16);
	   String str1 = hexTwoData(Integer.toBinaryString(temp));
	   String str2 = Integer.toBinaryString(~temp);
	   String str = hexTwoData(str2.substring(str2.length()-str1.length()));
	   return Integer.toHexString(Integer.parseInt(str,2));
    }
	   /**
	    * 10进制取反
	    * @return
	    */
	   public static int binAntiCode(int temp){
		   String str1 = hexTwoData(Integer.toBinaryString(temp));
		   String str2 = Integer.toBinaryString(~temp);
		   String str = hexTwoData(str2.substring(str2.length()-str1.length()));
		   return Integer.parseInt(str,2);
	   }
	   
	   /**
		 * 字符串固定大小
		 * @param bin
		 * @return
		 */
		public static String StrComple(String str) {
			String[] blanks = {""," ", "  ", "   ", "    ", "     ", "      "};
	        StringBuffer sb = new StringBuffer("");
	        sb.append(str).append(blanks[5 - (str.length()-1)]);
//	        sb.delete(0, 6); // 注意清空sb，可复用此对象
	        return sb.toString();
	    }
}
