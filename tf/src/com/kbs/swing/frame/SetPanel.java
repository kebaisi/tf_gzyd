package com.kbs.swing.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.swing.panel.BusyPanel;
import com.kbs.util.CheckProcessIsRunning;
import com.kbs.util.ConfigPro;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.HttpClient;
import com.kbs.util.MachineUtil;
import com.kbs.util.OperateProperties;
import com.kbs.util.TF;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;

public class SetPanel extends JPanel{
	private JTabbedPane setTabledPane;
	
	private JPanel commSetPanel;//数据库本地设置面板
	private JPanel ipSetPanel;//服务器配置面板
	private JPanel MachSetPanel;//机器配置
	private JPanel sysSetPanel;//系统配置
	private JLabel commIpLabel;//本地url
	private JLabel commDatabaseLabel;//本地数据库显示
	private JLabel ipLabel;//服务器ip
	private JTextField ipText;//服务器ip显示
	private JLabel portLabel;//服务器端口
	private JTextField portText;//服务器端口显示
	private JButton btnSaveIp;//保存服务器
	//机器配置
	private JLabel machLabel;//机器
	private JTextField machText;//机器显示
	private JButton btnSavMach;//保存机器
	private JTextArea textArea;
	private Properties  props;
	private Logger log = Logger.getLogger(SetPanel.class);
	private HttpClient httpClient=new HttpClient();
	private MainFrame mainFrame;
	private JPanel panel;
	private JPanel dataInitPanel;
	private MetaDataBO metaDataBO=new MetaDataBO();
	private JComboBox isRFID;
	private JComboBox cardType;
	private JComboBox isPrint;
	
	public SetPanel(){
		props = ConfigPro.getInstance().getProperties();
		initSetDialog();
	}
	
	public SetPanel(MainFrame mainFrame){
		props = ConfigPro.getInstance().getProperties();
		//调用缓存中初始加载的properties
//		props = (Properties)TF.dsJson;
		this.mainFrame=mainFrame;
		initSetDialog();
		this.setSize(1024, 696);
	}
	private void initSetDialog(){
		ipText =new JTextField(20);
		portText =new JTextField(5);
		machText =new JTextField(8);
//		this.setTitle("系统设置");
		TF.dsJson.put("title", "系统设置");
		if(TF.dsJson.getString("serverIp")!=null && !TF.dsJson.getString("serverIp").equals("")){
			ipText.setText(TF.dsJson.getString("serverIp"));
		}
		if(TF.dsJson.getString("serverPort")!=null && !TF.dsJson.getString("serverPort").equals("")){
			portText.setText(TF.dsJson.getString("serverPort"));
		}
		if(TF.dsJson.getString("machineId")!=null && !TF.dsJson.getString("machineId").equals("")){
			machText.setText(TF.dsJson.getString("machineId"));
		}
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1024, 696);
		add(panel, BorderLayout.WEST);
		panel.setLayout(null);
		setTabledPane = new JTabbedPane();
		setTabledPane.setBounds(91, 27, 810, 523);
		panel.add(setTabledPane);
		
		ipLabel =new JLabel("服务器Ip");
		portLabel =new JLabel("服务器端口");
		btnSaveIp =new JButton("保存");
		btnSaveIp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t=new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(ipText.getText()!=null && portText.getText()!=null){
							try {
								mainFrame.setBusy(true);
								//调用中央处理器状态接口
								props.setProperty("serverIp", ipText.getText());
								props.setProperty("serverPort", portText.getText());
								ConfigPro.getInstance().store();
								JOptionPane.showConfirmDialog(SetPanel.this,"保存成功","提示",JOptionPane.DEFAULT_OPTION);
								//加载ds缓存
								TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
								Process p1 = Runtime.getRuntime().exec("taskkill /F /IM tf_db.exe");
								int i=p1.waitFor();
								if(i==0){
									String path=GetProjectRealPath.getPath("tf_db.exe");
									File file = new File(path);
									try {
										Runtime.getRuntime().exec(file.getPath());
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										log.error("数据服务启动失败");
									}
								}
							} catch (Exception e1) {
								e1.printStackTrace();
								JOptionPane.showConfirmDialog(SetPanel.this,"保存失败","提示",JOptionPane.DEFAULT_OPTION);
								log.error("设置服务器地址保存时失败");
								mainFrame.setBusy(false);
							}
							mainFrame.setBusy(false);
						}
					}
				});
				t.start();
			}
		});
		
		ipSetPanel =new JPanel(new GridLayout(8,1));
		ipSetPanel.add(ipLabel);
		ipSetPanel.add(ipText);
		ipSetPanel.add(portLabel);
		ipSetPanel.add(portText);
		ipSetPanel.add(btnSaveIp);
		
		machLabel =new JLabel("机器号");
		
		btnSavMach =new JButton("绑定机器");
		btnSavMach.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mainFrame.setBusy(true);
						if(machText.getText()!=null){
							if(metaDataBO.queryForListMap("select * from tf_meter_record").size()>0){
								JOptionPane.showConfirmDialog(SetPanel.this,"绑定失败,系统已有机器号,如需更换请联系服务商！","提示",JOptionPane.DEFAULT_OPTION);
							}else{
							HttpClient httpClient = new HttpClient();
							try {
								String result = httpClient.post(TF.dsJson.getString("crm_url")+"/machine/bind?machineCode="+machText.getText()+"&mac="+MachineUtil.getLocalMac(InetAddress.getLocalHost()),null);
								if(result!=null){
									JSONObject resultJson = JSONObject.parseObject(result);
									if(resultJson.getString("code").equals("00000")){
										JSONObject meterJson = resultJson.getJSONArray("machineInformation").getJSONObject(0);
										meterJson.put("CREATETIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("CREATETIME")));
										meterJson.put("UPDATETIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("UPDATETIME")));
										meterJson.put("VAILD_TIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("VAILD_TIME")));
										JSONObject storeJson = resultJson.getJSONArray("storeInformation").getJSONObject(0);
										storeJson.put("CREATETIME",DateTimeUtil.changeMillsToTime(storeJson.getLongValue("CREATETIME")));
										storeJson.put("UPDATETIME",DateTimeUtil.changeMillsToTime(storeJson.getLongValue("UPDATETIME")));
										storeJson.put("VAILD_TIME",DateTimeUtil.changeMillsToTime(storeJson.getLongValue("VAILD_TIME")));
										metaDataBO.execute(metaDataBO.toSql(meterJson, "tf_meter_record"));
										metaDataBO.execute(metaDataBO.toSql(storeJson, "tf_store_record"));
										Properties  props = ConfigPro.getInstance().getProperties();
										props.setProperty("machineId", machText.getText());
										ConfigPro.getInstance().store();
										TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
										JOptionPane.showConfirmDialog(SetPanel.this,"授权成功。","提示",JOptionPane.DEFAULT_OPTION);
										Process p1 = Runtime.getRuntime().exec("taskkill /F /IM tf_db.exe");
										int i=p1.waitFor();
										if(i==0){
											String path=GetProjectRealPath.getPath("tf_db.exe");
											File file = new File(path);
											try {
												Runtime.getRuntime().exec(file.getPath());
											} catch (Exception e1) {
												// TODO Auto-generated catch block
												log.error("数据服务启动失败");
											}
										}
										boolean isDBRunning=CheckProcessIsRunning.checkIsRunning("tf_db.exe");
										if(!isDBRunning){
											String path=GetProjectRealPath.getPath("tf_db.exe");
											File file = new File(path);
											try {
												Runtime.getRuntime().exec(file.getPath());
											} catch (Exception e) {
												// TODO Auto-generated catch block
												log.error("数据服务启动失败");
											}
										}
										System.exit(0);
									}else{
										JOptionPane.showConfirmDialog(SetPanel.this,"绑定失败,"+resultJson.getString("msg"),"提示",JOptionPane.DEFAULT_OPTION);
									}
								}else{
									JOptionPane.showConfirmDialog(SetPanel.this,"绑定失败,无法连接到授权中心","提示",JOptionPane.DEFAULT_OPTION);
								}
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
							
							
							
							
//							
//							try {
//								
//								JSONObject jasonObject=new JSONObject();
//								jasonObject.put("machine_no", machText.getText());
//								jasonObject.put("register_ip", getLocalHostIp());
//								jasonObject.put("u_id", TF.userJson.getString("U_ID"));
//								jasonObject.put("TYPE", "0");
//								props.setProperty("machineId", machText.getText());
//								ConfigPro.getInstance().store();
//								JOptionPane.showConfirmDialog(SetPanel.this,"保存成功","提示",JOptionPane.DEFAULT_OPTION);
//								Process p1 = Runtime.getRuntime().exec("taskkill /F /IM tf_db.exe");
//								int i=p1.waitFor();
//								if(i==0){
//									String path=GetProjectRealPath.getPath("tf_db.exe");
//									File file = new File(path);
//									try {
//										Runtime.getRuntime().exec(file.getPath());
//									} catch (Exception e1) {
//										// TODO Auto-generated catch block
//										log.error("数据服务启动失败");
//									}
//								}
//							} catch (Exception e1) {
//								e1.printStackTrace();
//								//加入异常信息表
//								mainFrame.setBusy(false);
//							}
						}
						mainFrame.setBusy(false);
					}
				});
				t.start();
			}
		});
		MachSetPanel =new JPanel(new GridLayout(6,1));
		MachSetPanel.add(machLabel);
		MachSetPanel.add(machText);
		MachSetPanel.add(btnSavMach);
		
		sysSetPanel =new JPanel();
		sysSetPanel.setLayout(null);
		setTabledPane.addTab("服务器配置", ipSetPanel);
		setTabledPane.addTab("机器配置", MachSetPanel);
		setTabledPane.addTab("系统配置", sysSetPanel);
		
		textArea = new JTextArea();
		textArea.setText(OperateProperties.readTxtFile());
		textArea.setLineWrap(true);        //激活自动换行功能 
		textArea.setWrapStyleWord(true); 
		textArea.setBounds(10, 10, 509, 346);
		JScrollPane textScroll=new JScrollPane();
		textScroll.setBounds(10, 28, 509, 350);
		textScroll.setViewportView(textArea);
		
		JButton button = new JButton("保存");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dsStr=textArea.getText();
				try {
					OperateProperties.writeTxtFile(dsStr, ConfigPro.CONFIG_FILE);
					JOptionPane.showConfirmDialog(SetPanel.this,"保存成功","提示",JOptionPane.DEFAULT_OPTION);
					//加载ds缓存
					TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
					Process p1 = Runtime.getRuntime().exec("taskkill /F /IM tf_db.exe");
					int i=p1.waitFor();
					if(i==0){
						String path=GetProjectRealPath.getPath("tf_db.exe");
						File file = new File(path);
						try {
							Runtime.getRuntime().exec(file.getPath());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							log.error("数据服务启动失败");
						}
					}
				} catch (Exception e1) {
					log.error("保存配置信息失败");
					JOptionPane.showConfirmDialog(SetPanel.this,"保存失败","提示",JOptionPane.DEFAULT_OPTION);
				}
			}
		});
		button.setBounds(202, 388, 108, 46);
		sysSetPanel.add(textScroll);
		sysSetPanel.add(button);
		commIpLabel =new JLabel();
		commIpLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		commIpLabel.setBounds(0, 64, 805, 61);
		commIpLabel.setText("卡片类型：");
		commDatabaseLabel =new JLabel();
		commDatabaseLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		commDatabaseLabel.setBounds(0, 120, 86, 61);
		commDatabaseLabel.setText("RFID天线：");
		
		commSetPanel =new JPanel();
		commSetPanel.setLayout(null);
		
		cardType = new JComboBox();
		cardType.setFont(new Font("微软雅黑 Light", Font.PLAIN, 18));
		cardType.setModel(new DefaultComboBoxModel(new String[] {"IC", "ID"}));
		cardType.setBounds(96, 69, 194, 46);
		commSetPanel.add(cardType);
		commSetPanel.add(commIpLabel);
		commSetPanel.add(commDatabaseLabel);
		
		setTabledPane.addTab("收银设置", commSetPanel);
		
		isRFID = new JComboBox();
		isRFID.setModel(new DefaultComboBoxModel(new String[] {"启用", "禁用"}));
		isRFID.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		isRFID.setBounds(96, 135, 194, 40);
		commSetPanel.add(isRFID);
		JLabel label = new JLabel();
		label.setText("打印小票：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label.setBounds(0, 191, 98, 61);
		commSetPanel.add(label);
		
		isPrint = new JComboBox();
		isPrint.setModel(new DefaultComboBoxModel(new String[] {"禁用", "启用"}));
		isPrint.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		isPrint.setBounds(96, 198, 194, 40);
		commSetPanel.add(isPrint);
		isRFID.setSelectedIndex(TF.dsJson.getIntValue("isRFID"));
		cardType.setSelectedIndex(TF.dsJson.getIntValue("cardType"));
		isPrint.setSelectedIndex(TF.dsJson.getIntValue("isprint"));
		JButton btnNewButton = new JButton("保 存");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Properties  props = ConfigPro.getInstance().getProperties();
				props.setProperty("isRFID", isRFID.getSelectedIndex()+"");
				props.setProperty("cardType", cardType.getSelectedIndex()+"");
				props.setProperty("isprint", isPrint.getSelectedIndex()+"");
				ConfigPro.getInstance().store();
				TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
				JOptionPane.showConfirmDialog(SetPanel.this,"保存成功，重启生效。","提示",JOptionPane.DEFAULT_OPTION);
			}
		});
		btnNewButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		btnNewButton.setBounds(0, 320, 805, 70);
		commSetPanel.add(btnNewButton);
		
		
		
		dataInitPanel=new JPanel();
		dataInitPanel.setLayout(null);
//		setTabledPane.addTab("初始化", dataInitPanel);
		
		JButton btn_saleDataInit = new JButton("运营数据初始化");
		btn_saleDataInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i=JOptionPane.showConfirmDialog(SetPanel.this, "确定要清除所有运营数据吗?","警告",JOptionPane.YES_NO_OPTION);
				if(i==0){
					metaDataBO.execute("delete from tf_chipinfo");
					metaDataBO.execute("delete from tf_cardinfo");
					metaDataBO.execute("delete from tf_commodity_record");
					metaDataBO.execute("delete from tf_commodity_type_record");
					metaDataBO.execute("delete from tf_exception_record");
					metaDataBO.execute("delete from tf_meter_chiptype_relation");
					metaDataBO.execute("delete from tf_meter_mealtims_chipcategroy_relation");
					metaDataBO.execute("delete from tf_meter_mealtims_relation");
					metaDataBO.execute("delete from tf_meter_running");
					metaDataBO.execute("delete from tf_meter_sys_config");
					metaDataBO.execute("delete from tf_settings");
					metaDataBO.execute("delete from tf_syn_record");
					metaDataBO.execute("delete from tf_upload_record");
					metaDataBO.execute("delete from tf_userinfo");
//					metaDataBO.execute("update tb_sequence set current_value='1'");
					JOptionPane.showMessageDialog(SetPanel.this, "初始化运营数据成功","消息",JOptionPane.OK_OPTION);
				}else{
					return;
				}
			}
		});
		btn_saleDataInit.setBounds(248, 100, 127, 32);
		dataInitPanel.add(btn_saleDataInit);
		
		JButton btn_dataInit = new JButton("数据初始化");
		btn_dataInit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int i=JOptionPane.showConfirmDialog(SetPanel.this, "确定要清除所有数据吗?","警告",JOptionPane.YES_NO_OPTION);
				if(i==0){
					metaDataBO.execute("delete from tf_chipinfo");
					metaDataBO.execute("delete from tf_cardinfo");
					metaDataBO.execute("delete from tf_commodity_record");
					metaDataBO.execute("delete from tf_commodity_type_record");
					metaDataBO.execute("delete from tf_exception_record");
					metaDataBO.execute("delete from tf_meter_chiptype_relation");
					metaDataBO.execute("delete from tf_meter_mealtims_chipcategroy_relation");
					metaDataBO.execute("delete from tf_meter_mealtims_relation");
					metaDataBO.execute("delete from tf_meter_running");
					metaDataBO.execute("delete from tf_meter_sys_config");
					metaDataBO.execute("delete from tf_settings");
					metaDataBO.execute("delete from tf_syn_record");
					metaDataBO.execute("delete from tf_upload_record");
					metaDataBO.execute("delete from tf_userinfo");
					metaDataBO.execute("delete from tf_consume_card_record");
					metaDataBO.execute("delete from tf_consume_details_record");
					metaDataBO.execute("delete from tf_consume_order_record");
					metaDataBO.execute("update tb_sequence set current_value='1'");
					JOptionPane.showMessageDialog(SetPanel.this, "初始化数据成功","消息",JOptionPane.OK_OPTION);
				}else{
					return;
				}
			
			}
		});
		btn_dataInit.setBounds(248, 191, 127, 32);
		dataInitPanel.add(btn_dataInit);
		add(panel, BorderLayout.CENTER);
//		pack();
//		this.setPreferredSize(new Dimension(1024, 768));
//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
//        this.setSize(d.width, d.height); 
	}
	private String getLocalHostIp(){
		String result="";
		 try {
			String name = InetAddress.getLocalHost().getHostName();
			// 获取IP地址
		    result = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			log.error("获取本机IP失败");
			//加入异常信息表
		}
	     return result;
	}
	
	public void closeTF(){
		int flag = JOptionPane.showConfirmDialog(SetPanel.this,"是否退出系统？","退出",JOptionPane.YES_NO_OPTION);   
        if(flag==0) { 
			JSONObject uploadJson=new JSONObject();
			uploadJson.put("ONLINE_STATUS", 0);
			metaDataBO.execute(metaDataBO.toSql(uploadJson, "tf_meter_operation_record", "SEQNO"));
			System.exit(0);   
        } 
	}
}
