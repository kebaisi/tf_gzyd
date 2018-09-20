package com.kbs.util;

import java.security.MessageDigest;

public class MD5
{

  public static String KL(String inStr)
  {
    char[] a = inStr.toCharArray();
    for (int i = 0; i < a.length; i++) {
      a[i] = (char)(a[i] ^ 0x74);
    }
    String s = new String(a);
    return s;
  }

  public static String JM(String inStr)
  {
    char[] a = inStr.toCharArray();
    for (int i = 0; i < a.length; i++) {
      a[i] = (char)(a[i] ^ 0x74);
    }
    String k = new String(a);
    return k;
  }
}
