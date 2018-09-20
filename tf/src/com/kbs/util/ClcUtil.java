package com.kbs.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;




import com.kbs.commons.ic.IcUtils;
import com.kbs.commons.rfid.RfidUtils;

import sun.applet.Main;

public class ClcUtil {
	static final char TABLE1021[] = { /* CRC1021余式表 */  
	        0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b, 0xc18c,  
	        0xd1ad, 0xe1ce, 0xf1ef, 0x1231, 0x0210, 0x3273, 0x2252, 0x52b5, 0x4294, 0x72f7, 0x62d6, 0x9339, 0x8318,  
	        0xb37b, 0xa35a, 0xd3bd, 0xc39c, 0xf3ff, 0xe3de, 0x2462, 0x3443, 0x0420, 0x1401, 0x64e6, 0x74c7, 0x44a4,  
	        0x5485, 0xa56a, 0xb54b, 0x8528, 0x9509, 0xe5ee, 0xf5cf, 0xc5ac, 0xd58d, 0x3653, 0x2672, 0x1611, 0x0630,  
	        0x76d7, 0x66f6, 0x5695, 0x46b4, 0xb75b, 0xa77a, 0x9719, 0x8738, 0xf7df, 0xe7fe, 0xd79d, 0xc7bc, 0x48c4,  
	        0x58e5, 0x6886, 0x78a7, 0x0840, 0x1861, 0x2802, 0x3823, 0xc9cc, 0xd9ed, 0xe98e, 0xf9af, 0x8948, 0x9969,  
	        0xa90a, 0xb92b, 0x5af5, 0x4ad4, 0x7ab7, 0x6a96, 0x1a71, 0x0a50, 0x3a33, 0x2a12, 0xdbfd, 0xcbdc, 0xfbbf,  
	        0xeb9e, 0x9b79, 0x8b58, 0xbb3b, 0xab1a, 0x6ca6, 0x7c87, 0x4ce4, 0x5cc5, 0x2c22, 0x3c03, 0x0c60, 0x1c41,  
	        0xedae, 0xfd8f, 0xcdec, 0xddcd, 0xad2a, 0xbd0b, 0x8d68, 0x9d49, 0x7e97, 0x6eb6, 0x5ed5, 0x4ef4, 0x3e13,  
	        0x2e32, 0x1e51, 0x0e70, 0xff9f, 0xefbe, 0xdfdd, 0xcffc, 0xbf1b, 0xaf3a, 0x9f59, 0x8f78, 0x9188, 0x81a9,  
	        0xb1ca, 0xa1eb, 0xd10c, 0xc12d, 0xf14e, 0xe16f, 0x1080, 0x00a1, 0x30c2, 0x20e3, 0x5004, 0x4025, 0x7046,  
	        0x6067, 0x83b9, 0x9398, 0xa3fb, 0xb3da, 0xc33d, 0xd31c, 0xe37f, 0xf35e, 0x02b1, 0x1290, 0x22f3, 0x32d2,  
	        0x4235, 0x5214, 0x6277, 0x7256, 0xb5ea, 0xa5cb, 0x95a8, 0x8589, 0xf56e, 0xe54f, 0xd52c, 0xc50d, 0x34e2,  
	        0x24c3, 0x14a0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405, 0xa7db, 0xb7fa, 0x8799, 0x97b8, 0xe75f, 0xf77e,  
	        0xc71d, 0xd73c, 0x26d3, 0x36f2, 0x0691, 0x16b0, 0x6657, 0x7676, 0x4615, 0x5634, 0xd94c, 0xc96d, 0xf90e,  
	        0xe92f, 0x99c8, 0x89e9, 0xb98a, 0xa9ab, 0x5844, 0x4865, 0x7806, 0x6827, 0x18c0, 0x08e1, 0x3882, 0x28a3,  
	        0xcb7d, 0xdb5c, 0xeb3f, 0xfb1e, 0x8bf9, 0x9bd8, 0xabbb, 0xbb9a, 0x4a75, 0x5a54, 0x6a37, 0x7a16, 0x0af1,  
	        0x1ad0, 0x2ab3, 0x3a92, 0xfd2e, 0xed0f, 0xdd6c, 0xcd4d, 0xbdaa, 0xad8b, 0x9de8, 0x8dc9, 0x7c26, 0x6c07,  
	        0x5c64, 0x4c45, 0x3ca2, 0x2c83, 0x1ce0, 0x0cc1, 0xef1f, 0xff3e, 0xcf5d, 0xdf7c, 0xaf9b, 0xbfba, 0x8fd9,  
	        0x9ff8, 0x6e17, 0x7e36, 0x4e55, 0x5e74, 0x2e93, 0x3eb2, 0x0ed1, 0x1ef0 };  
	public static char getCRC1021(byte b[], int len) {  
	    //char crc = 0;  
	    char crc = 0xffff;  
	    byte hb = 0;  
	    int j = 0;  
	    int index;  
	    while (len-- != 0) {  
	        hb = (byte) (crc / 256); // 以8位二进制数的形式暂存CRC的高8位  
	        index = ((hb ^ b[j]) & 0xff); // 求得所查索引下标  
	        crc <<= 8; // 左移8位，相当于CRC的低8位乘以  
	        crc ^= (TABLE1021[index]); // 高8位和当前字节相加后再查表求CRC
	        j++;  
	    }  
	    return (crc); 
	}

public static byte getXor(byte[] datas){

 

	byte temp=datas[0];

		

	for (int i = 1; i <datas.length; i++) {

		temp ^=datas[i];

	}

 

	return temp;

}

	public static int CRC_XModem(byte[] bytes){  
	       int crc = 0x00;          // initial value  
	       int polynomial = 0x1021;    
	       for (int index = 0 ; index< bytes.length; index++) {  
	           byte b = bytes[index];  
	           for (int i = 0; i < 8; i++) {  
	               boolean bit = ((b   >> (7-i) & 1) == 1);  
	               boolean c15 = ((crc >> 15    & 1) == 1);  
	               crc <<= 1;  
	               if (c15 ^ bit) crc ^= polynomial;  
	            }  
	       }  
	       crc &= 0xffff;  
	       return crc;     
	} 
	public static String decToHex(int dec) {
	    String hex = "";
	    while(dec != 0) {
	        String h = Integer.toString(dec & 0xff, 16);
	        if((h.length() & 0x01) == 1)
	            h = '0' + h;
	        hex = hex + h;
	        dec = dec >> 8;
	    }
	    return hex;
	}
	public static String getSt(String val){
		IcUtils icUtils =IcUtils.getInstance();
		String result = "02";
		System.out.println(val);
		String [] message = icUtils.getHexArray(val);
		byte [] dd =  new byte[message.length];
		 String st = "";
		 for(int i=0; i<message.length;i++){
			dd[i] = (byte)(Integer.valueOf(message[i], 16)&0xFF);
			
			st+=message[i];
		 }
		 String s = val+ClcUtil.decToHex(ClcUtil.CRC_XModem(dd));
		 System.out.println(s);
		 return result+icUtils.hexTwoData(Integer.toHexString(s.length()/2))+s;
	}
	/**
	 * 16进制字符串转换为Byte型数组16进制源字符串
	 * @param 16进制字符串
	 * @return Byte类型数组
	 */
	public static byte[] HexStringToByte(String hexString) {

	   int len = hexString.length();
	   if (len % 2 != 0)
	      return null;
	   byte[] bufD = new byte[len / 2];
	   byte[] tmpBuf = hexString.getBytes();
	   int i = 0, j = 0;
	   for (i = 0; i < len; i++) {
	      if (tmpBuf[i] >= 0x30 && tmpBuf[i] <= 0x39)
	         tmpBuf[i] -= 0x30;
	      else if (tmpBuf[i] >= 0x41 && tmpBuf[i] <= 0x46)
	         tmpBuf[i] -= 0x37;
	      else if (tmpBuf[i] >= 0x61 && tmpBuf[i] <= 0x66)
	         tmpBuf[i] -= 0x57;
	      else
	         tmpBuf[i] = 0xF;
	   }
	   for (i = 0, j = 0; i < len; i += 2, j++) {
	      bufD[j] = (byte) ((tmpBuf[i] << 4) | tmpBuf[i + 1]);
	   }
	   return bufD;

	}
	public static int xor16( byte buf[], int index, long bytetotal)
	{
	    int xor16 = Integer.parseInt(byte2Hex(buf[index]),16);
	    for (int i = index+1; i <bytetotal; i++) {
	        xor16 = xor16^Integer.parseInt(byte2Hex(buf[i]),16);
	    }
	    System.out.println();
	    return xor16;
	}
	public static String byte2Hex(byte b) {
		   String result = Integer.toHexString(b & 0xFF);
		   if (result.length() == 1) {
		      result = '0' + result;
		   }
		   return result;
	}
	public static void main(String[] args) {
//		System.out.println(getSt("000401A10000"));
//		System.out.println(getXor(datas));00
//		String []  message=IcUtils.getInstance().xorbin(RfidUtils.getInstance().getHexArray("000401A100"));
//		StringBuffer readBuffer = new StringBuffer("");
//		 byte [] dd =  new byte[message.length];
//		 String st = "";
//		 for(int i=0; i<message.length;i++){
//			dd[i] = (byte)(Integer.valueOf(message[i], 16)&0xFF);
//			
//			st+=message[i];
//		 }
//	 System.out.println("st000---:"+st);
//		System.out.println(IcUtils.getInstance().xolCalc("000401A10000"));C7
//		System.out.println(xor16(HexStringToByte("5A000401A2000400000064"),1,HexStringToByte("5A000401A2000400000064").length));
//		System.out.println(xor16(HexStringToByte("5A000301A10000"),1,HexStringToByte("5A000301A10000").length));
		System.out.println("5a00b60152001a880001015600000000000000000064000039582018081617070993".length());
	}
}