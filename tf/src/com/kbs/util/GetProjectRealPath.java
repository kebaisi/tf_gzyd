package com.kbs.util;

public class GetProjectRealPath {
//	public static String getPath(String fileName) {
//		  ClassLoader loader = GetProjectRealPath.class.getClassLoader();
//		  String clsName = GetProjectRealPath.class.getName() + ".class";
//		  Package pack = GetProjectRealPath.class.getPackage();
//		  String path = "";
//		  if (pack != null)
//		  {
//		   String packName = pack.getName();
//		   clsName = clsName.substring(packName.length() + 1);
//		   if (packName.indexOf(".") < 0) 
//		    path = packName + "/";
//		   else {
//		    int start = 0, end = 0;
//		    end = packName.indexOf(".");
//		    while (end != -1) {
//		     path = path + packName.substring(start, end) + "/";
//		     start = end + 1;
//		     end = packName.indexOf(".", start);
//		    }
//		    path = path + packName.substring(start) + "/";
//		   }
//		  }
//		  java.net.URL url = loader.getResource(path + clsName);
//		  String realPath = url.getPath();
//		  int pos = realPath.indexOf("file:");
//		  if (pos > -1)
//		  realPath = realPath.substring(pos + 5);
//		  pos = realPath.indexOf(path + clsName);
//		  realPath = realPath.substring(0, pos - 5);
//		  if (realPath.endsWith("!"))
//		   realPath = realPath.substring(0, realPath.lastIndexOf("/"));
//		  realPath=realPath.substring(0, realPath.lastIndexOf("/")+1);
//		  return realPath+fileName.trim();
//		 }// getAppPath定义 结束
	
	public static String getPath(String fileName) {
		  return fileName.trim();
		 }// getAppPath定义 结束
	
	public static void main(String[] args) {
		System.out.println(GetProjectRealPath.getPath("log4j.priperties"));
	}
}
