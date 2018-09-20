package com.kbs.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.rfid.RfidUtils;
import com.kbs.commons.util.PropertiesUtil;
import com.kbs.util.ClcUtil;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.MD5;
import com.kbs.util.MachineUtil;
import com.kbs.util.TF;

public class test {
public static void main(String[] args) {
//System.out.println("020a0016fc040a02000501000003021800b5f2030a1c000601000014e0bf7d0201000000ec0000006403".substring(54, 62));


//System.out.println("020C00B3C6020A3A000401117DDDCC03".substring("020C00B3C6020A3A000401117DDDCC03".length()-18, "020C00B3C6020A3A000401117DDDCC03".length()-16));

System.out.println("020a00f632040a01000501000003".length());
String cmd ="0218000ede030a5a00060108a0248123dc0100000000600b00006403";

System.out.println(cmd.substring(cmd.indexOf("0601")+26, cmd.indexOf("0601")+34));
//System.out.println(cmd.substring(cmd.indexOf("0218")+44,cmd.indexOf("0218")+52));
//System.out.println("00B6".indexOf("00"));

}
}
