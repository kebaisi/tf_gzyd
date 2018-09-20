package com.kbs.consume.operations;
/* ----------------------------------------------------------
 * 文件名称：FXMLDocumentController.java
 * 
 * 作者：秦建辉
 * 
 * 微信：splashcn
 * 
 * 博客：http://www.firstsolver.com/wordpress/
 * 
 * 开发环境：
 *      NetBeans 8.1
 *      JDK 8u92
 *      
 * 版本历史：
 *      V1.1    2016年07月17日
 *              因SDK改进更新代码
 *
 *      V1.0    2014年09月04日
 *              接收卡点数据
------------------------------------------------------------ */


import Com.FirstSolver.Security.Utils;
import Com.FirstSolver.Splash.FaceId_Item;
import Com.FirstSolver.Splash.ISocketServerThreadTask;
import Com.FirstSolver.Splash.NetworkStreamPlus;
import Com.FirstSolver.Splash.TcpListenerPlus;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import com.kbs.util.TF;


/**
 *
 * 
 */
public class FaceUtil implements  ISocketServerThreadTask {
    
    private final String DeviceCharset = "GBK";
    
    private boolean IsServerRunning = false;
    public StringBuffer message = new StringBuffer();
    public TcpListenerPlus TcpServer = null;
 
    public FaceUtil() throws UnknownHostException, IOException{
        TcpServer = new TcpListenerPlus(8898, 0, InetAddress.getByName(TF.dsJson.getString("local")),false);
        
        // 设置通信线程任务委托
        TcpServer.ThreadTaskDelegate = this;
        
        // 开启侦听服务
        TcpServer.StartListenThread(null, 0, 0);
        
        IsServerRunning = true;
    }
    
    private void handleButtonStartListenerAction() throws IOException, Exception {
//        if(IsServerRunning)
//        {
//            if(TcpServer != null)
//            {
//                TcpServer.close();
//                TcpServer = null;
//            }
//            IsServerRunning = false;
//            buttonStartListener.setText("开始侦听");
//        }
//        else
//        {   
            // 创建侦听服务器
            TcpServer = new TcpListenerPlus(8898, 0, InetAddress.getByName("192.168.1.199"),false);
            
            // 设置通信线程任务委托
            TcpServer.ThreadTaskDelegate = this;
            
            // 开启侦听服务
            TcpServer.StartListenThread(null, 0, 0);
            
            IsServerRunning = true;
//            buttonStartListener.setText("停止侦听");            
//        }
    }
    
    @Override
    public void OnServerTaskRequest(NetworkStreamPlus stream) throws Exception {
        // 设备特殊通信密码
        // stream.setSecretKey(textFieldSecretKey.getText());  // 注意：密码要和设备通信密码一致
        
        String SerialNumber;    // 设备序列号
        while(true)
        {   
            try {
                // 读取数据
                String Answer = stream.Read(DeviceCharset);
             	message.append(Answer);
                // 显示读取信息
//                Platform.runLater(() -> {
//                    textAreaAnswer.appendText(Answer + "\r\n");
//                });
                	
//                System.out.println(Answer + "\r\n");
                if(Answer.startsWith("PostRecord"))
                {   // 提取序列号并保存
                	 message = new StringBuffer();
                    SerialNumber = FaceId_Item.GetKeyValue(Answer, "sn");
                    
                    // 答复已准备好接收考勤记录
//                    if (checkBoxPostPhoto.isSelected())
//                    {
                        stream.Write("Return(result=\"success\" postphoto=\"false\")", DeviceCharset);
//                    }
//                    else
//                    {
//                        stream.Write("Return(result=\"success\" postphoto=\"false\")", DeviceCharset);
//                    }
                }
                else if(Answer.startsWith("Record"))
                {   // 读取考勤记录   
                    
                    // 服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }
                else if(Answer.startsWith("PostEmployee"))
                {   // 准备上传人员信息
                    
                    // 服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }                
                else if(Answer.startsWith("Employee"))
                {   // 读取人员信息
                    
                    // 服务器回应
                    stream.Write("Return(result=\"success\")", DeviceCharset);
                }
                else if (Answer.startsWith("GetRequest"))
                {   // 下发命令
//                    Platform.runLater(() -> {
//                        String Command = textAreaCommand.getText();
//                        if (!Utils.IsNullOrEmpty(Command))
//                        {
//                            try 
//                            {
//                                stream.Write("GetDeviceInfo()", DeviceCharset);
//                            }
//                            catch (Exception ex)
//                            {
//                                
//                            }
//                            
//                            textAreaCommand.clear();
//                        }
//                    });
                	stream.Write("GetDeviceInfo()", DeviceCharset);
                }
                else if(Answer.startsWith("Quit"))
                {   // 结束会话
               
//                	System.out.println(message.toString());
                    break;
                }
            }
            catch (Exception ex)
            {
                break;  // 连接断开
            }
        }
    }
    
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // 设置服务器地址
//        try
//        {
//            List<String> IPList = new LinkedList<>();
//            Enumeration<NetworkInterface> InterfaceList = NetworkInterface.getNetworkInterfaces();
//            while (InterfaceList.hasMoreElements())
//            { 
//                NetworkInterface iFace = InterfaceList.nextElement();
//                if(iFace.isLoopback() || iFace.isVirtual() || iFace.isPointToPoint() || !iFace.isUp()) continue;
//                                
//                Enumeration<InetAddress> AddrList = iFace.getInetAddresses(); 
//                while (AddrList.hasMoreElements())
//                { 
//                    InetAddress address = AddrList.nextElement(); 
//                    if ((address instanceof Inet4Address) || (address instanceof Inet6Address))
//                    {
//                        IPList.add(address.getHostAddress());                
//                    }
//                } 
//            }
//            
//            if(!IPList.isEmpty()) 
//            {
//                comboBoxServerIP.setItems(FXCollections.observableList(IPList));
//                comboBoxServerIP.setValue(IPList.get(0));
//            }            
//        }
//        catch (SocketException ex) 
//        {
//            // 异常处理
//        }
//    }
}
