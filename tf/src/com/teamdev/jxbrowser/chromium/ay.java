package com.teamdev.jxbrowser.chromium;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ay
{
  public static final SimpleDateFormat a = new SimpleDateFormat(ax.d("-1f7zk41inyuazbev"), Locale.ENGLISH);
  protected String b;
  protected String c;
  private az d;
  private long e = 0L;
  private ax f = null;
  
  protected ay(String paramString1, String paramString2, az paramaz)
  {
    this.c = paramString1;
    this.b = paramString2;
    this.d = paramaz;
  }
  
  public final synchronized void b()
  {
    try
    {
      Date localDate1;
      long l = (localDate1 = new Date()).getTime();
      if ((this.e != 0L) && (l - this.e < 86400000L)) {
        return;
      }
      String str = null;
      ba localba = null;
      Object localObject1 = this.d.a().iterator();
      Object localObject2 = null;
      
      a(null, localDate1, true); 
      
      /*while (((Iterator)localObject1).hasNext()) {
        try
        {
          localba = (ba)((Iterator)localObject1).next();
          localObject2 = MessageFormat.format(ax.d("p93g9dnjhp3v7wc8kkv5gf5wts2fxu4lrkegxm"), new Object[] { this.c, localba.b() });
          a((String)localObject2);
          // 注释掉验证代码
          if (!(localObject2 = localba.a()).b()) {
            //throw a(this.c, ax.d("-1koxh9ny992ac6ta5sop8ylctlxlppcydnte"));
          }
          Date localDate2 = localDate1; 
          Object localObject3 = localObject2;
          localObject2 = this;
         
          this.f = localba.a();
          a(this.f, localDate2, true); 
        }
        catch (RuntimeException localRuntimeException2)
        {
          str = localRuntimeException2.getMessage();
          a((Throwable)localObject2);
        }
      }
      if (this.f == null)
      {
        int i;
        if ((str != null) && ((i = str.indexOf(": ")) != -1)) {
          str = str.substring(i + 2);
        }
        localObject1 = (new StringBuffer()).append(ax.d("-1h5z6hxxos0zbox3xolmba48qlwwd7x6uwuq"));
        ((StringBuffer)localObject1).append(' ');
        if (localba != null)
        {
          ((StringBuffer)localObject1).append(localba.b());
          ((StringBuffer)localObject1).append(" - ");
        }
        if (str != null) {
          ((StringBuffer)localObject1).append(str);
        }
        throw a(this.c, ((StringBuffer)localObject1).toString());
      }*/
      a(MessageFormat.format(ax.d("-2q85h5qltpmyrcjwru3sej0uxscy"), new Object[] { this.c }));
      this.e = l;
      return;
    }
    catch (RuntimeException localRuntimeException1)
    {
        localRuntimeException1.printStackTrace();
      a(localRuntimeException1);
      throw localRuntimeException1;
    }
    catch (Error localError)
    {
      a(localError);
      throw localError;
    }
  }
  
  protected void a(ax paramax, Date paramDate, boolean paramBoolean)
  {
    String str1 = "jack";
    String str2 = "1.0";
    String str3 = "jack";
    String str4 = "NB";
    //到期时间
    Date localDate = b(paramax);
    c(paramax);
    String str5 = "jack Crack";
    String str6 = "jack Crack";
    String str7 = "java.lang.String";
    long l = localDate != null ? localDate.getTime() : 0L;
    //paramax = a(paramax);
    a(paramax);
    boolean bool1;
    Object localObject;
    if ((bool1 = str4.equals(ax.d("-24jl5nttfop484gi"))))
    {
      localObject = new bd(str1, this.b, localDate); localObject = new bc((bd)localObject, new bb[] { new be("ecjgpw1257bg77iav"), new be("-5fz9u9b1d9n77sjezuuai80ktm4k8yjirbsplio9m6yt0"), new bf(), new bg("-6bspffqi914xs2ut3d0ieleutxmzj0t4zx"), new bg("ws9f9lj0luj1n2woe9gkorn") });
      String str8;
      boolean bool3 = (str8 = str5.toLowerCase()).contains(ax.d("-wl8msznmkuqk"));
      boolean bool2 = str8.contains(ax.d("-2ky68kd0oyfkualsija98isgr"));
      if ((bool3) || (bool2)) {
        ((bc)localObject).b();
      }
      if ((bool2 = ((bc)localObject).a(paramDate)))
      {
        /*paramax = b(str1, ((bc)localObject).a());
        throw a(this.c, paramax);*/
      }
    }
    a(ax.d("-fx8zfkfilg7q65z9rg5h0o0ijk") + str1);
    a(ax.d("-167tchxq4gzzfmthz9d865k9y0ulaxxi8") + str2);
    a(ax.d("-65a0ibucyq8yra7nbat84f74w") + str3);
    a(ax.d("-fx8zfqjxqhmbqr30hhe34xcbfk") + str4);
    if (str6 != null) {
      a(ax.d("-959y3b7csqivsggnhg7uok7zdrrznrixf5xqftd6cmx9c") + str6);
    }
    a(ax.d("e1d5mpyagfb433m5qissnmbqgvialnk") + a(paramax));
    a(ax.d("e1d5mp53qy18l2b0dpetgwio6nsn6sg") + (localDate != null ? a(localDate) : ax.d("1js3qp8y")));
    a(ax.d("-fx8zfqjxqhmbqr30hlp1429bs0") + str5);
    a(ax.d("-fx8zg44ytfet74z7tml7k25n1c") + a(paramDate));
   /* if (!this.c.equals(str1)) {
      throw a(this.c, ax.d("1v35k8qx4wmssyw4qti519lbpqfktq"));
    }*/
    if ((paramBoolean) && (!str2.startsWith(this.b))) {
      //throw a(this.c, ax.d("iot24yas8cuw44n62hrb3zbbn2hxxfnkivget9kl2amnhmkrqcg") + this.b + ax.d("-20x74pe1ewn8x0ps") + str2 + '.');
    }
    str6 = "java.lang.String";
    if (str6 != null) {
      try
      {
        Class.forName(str7);
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        throw a(this.c, ax.d("lifgy671svju3mpdy4x1uivfkfwfz332tp0qxyulqcleoi59pc8h0g") + str6);
      }
    }
    if ((localDate != null) && (paramDate.getTime() >= l))
    {
      localObject = b(str1, a(localDate));
      //throw a(this.c, (String)localObject);
    }
  }
  
  private static String b(String paramString1, String paramString2)
  {
    String str1 = ax.d("106h6jqri8vfn4xt2wa9sbhgl5rnhh3du04mc0w680mjnte4i53d33ecsbskh6ao");
    String str2 = ax.d("azgyw1uv8z3saevtpfuscjdn1ovhfuu8n3jcmkf01xm2gtfd40infk");
    String str3 = ax.d("-3ncn8aicc2tbpmjxfwwzof0i3ezah5oyp6m1zjekiv2w5wgbzacrmz6jsuhpa7ei8x3cp07edzsjmyg7hv8wb3x7f5");
    String str4 = ax.d("akw704qofn7v1xjbtj0y8hcj5ykls9dqjo3srlctsslkmwsepp5ab81713y1f3m1kyh5drsi5sfytatyrted9e74");
    String str5 = ax.d("-42bz4x20xc7k8uu2jb5yxlj02nrezdb5hyfic308dkykwip5639q9kkb9z3363ag14vbispaagrneyq7cbsx4bbc2ya0z4a7uqgv0odekbap0sd9ce5u71bxshaj6m0wwlg488g0u92wafuelor2qazv10uikxik1x0ymfdn30j46oefr6g4tjl");
    return str1 + paramString2 + str2 + paramString1 + str3 + paramString1.toLowerCase() + str4 + paramString1 + str5;
  }
  
  protected void a(String paramString)
  {
    System.out.println(paramString);
  }
  
  private void a(Throwable paramThrowable)
  {
    try
    {
      a(paramThrowable.getMessage());
    }
    catch (Exception localException)
    {
      System.out.println(paramThrowable.getMessage());
    }
    for (paramThrowable = paramThrowable; paramThrowable != null; paramThrowable = paramThrowable.getCause()) {
      paramThrowable.setStackTrace(new StackTraceElement[0]);
    }
  }
  
  private static String a(Date paramDate)
  {
    return SimpleDateFormat.getDateInstance(2).format(paramDate);
  }
  
  public static RuntimeException a(String paramString1, String paramString2)
  {
    paramString1 = MessageFormat.format(ax.d("-4njllqpr2n2m62h303cst4lers4j13jyuqjklo6u2i743"), new Object[] { paramString1, paramString2 });
    return new RuntimeException(paramString1);
  }
  
  private Date a(ax paramax)
  {
    /*if ((paramax = paramax.a(ax.d("1m81b2vpljtfnxmoxaelbol"))) == null) {
      throw a(this.c, ax.d("86te4jjcjrfpf5hiittrs2noi8xih2kw8qbdkkxc5toub15ciaq0t12hkni4pfb6dvvacmoc03ucl"));
    }*/
    try
    {
      return a.parse("30-12-2099");
    }
    catch (ParseException localParseException)
    {
        return null;
      //throw a(this.c, ax.d("-benuth93s2hx673qi3yqnuqt9z5k2zctv3l2og3efiyr6mx3lf2gdco4np4b1c") + paramax);
    }
  }
  //到期时间 2099年
  private Date b(ax paramax)
  {
    /*if ((paramax = paramax.a(ax.d("28lqbdq6yls9p1vraqtfplx"))) == null) {
      throw a(this.c, ax.d("86te4furtvk2113ylh2tuqu6j8d2bxwiaq6f6i873lq9d4bjn95lxc12ka8prybplz15qe10dx9ph"));
    }
    if (paramax.toUpperCase().equals(ax.d("1js3qp8y"))) {
      return null;
    }*/
    try
    {
      return a.parse("30-12-2099");
    }
    catch (ParseException localParseException)
    {
       return null;
      //throw a(this.c, ax.d("-benuth93s2hx673qi3yqnuqt9z5k2zctv3l2p9a3wsu97y2get1n35qeverpwg") + paramax);
    }
  }
  
  private static Date c(ax paramax)
  {
    try
    {
      //if ((paramax = paramax.a(ax.d("-1kmye09ftyxuehndrac3s0nt019y7bospggr"))) != null) {
        return a.parse("30-12-2099");
      //}
    }
    catch (ParseException localParseException)
    {
      return null;
    }
  }
}
