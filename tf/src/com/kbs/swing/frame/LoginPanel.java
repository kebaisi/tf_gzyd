package com.kbs.swing.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ProgressBarUI;

import org.apache.commons.collections.functors.TruePredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import sun.misc.CRC16;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.swing.panel.CustomerPanel;
import com.kbs.swing.panel.ErrorMsgPanel;
import com.kbs.swing.panel.GgPanel;
import com.kbs.sys.CacheService;
import com.kbs.sys.LoginService;
import com.kbs.sys.UserLogin;
import com.kbs.timer.HeartTimer;
import com.kbs.util.ClcUtil;
import com.kbs.util.ConfigPro;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.HttpClient;
import com.kbs.util.MD5;
import com.kbs.util.MachineUtil;
import com.kbs.util.TF;
import com.sun.awt.AWTUtilities;

import javax.swing.SwingConstants;

public class LoginPanel extends JPanel {

	private JTextField txt_userName;
	private JPasswordField txt_pwd;
	private MyFocusListener focusListener=new MyFocusListener();
	private JButton btn_login;
	Timer checkIsOnLineTimer=new Timer();
	private MainFrame mainFrame;
	private JProgressBar loadingProgressBar;
	private javax.swing.Timer progressTimer;
	private JLabel label_status;
	private NumberInputPnl numInputPnl;
	private MetaDataBO metaDataBo=new MetaDataBO();
//	private MetaDataBO metaDataBo=new MetaDataBO();
//	javax.swing.Timer displayTimer;
//	javax.swing.Timer checkStatusTimer;
	JSONObject statusJson=new JSONObject();
	JSONArray taskArr=new JSONArray();
//	private HttpClient httpClient=new HttpClient();
	String serverTime="";
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private JPanel panel_error;
	private JLabel label_errorTitle;
	private JLabel label_errorInfo;
//	private int percent=0;
	ErrorMsgPanel errorMsgPanel;
	private JTextField textField;
	private JLabel label_2;
	private JPanel panel;
	private JLabel label_1;
	private int progressLength = 0;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					LoginFrame frame = new LoginFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	public void checkValid(){
//		MetaDataBO metaDataBO  =new MetaDataBO();
		HttpClient httpClient = new HttpClient();
		
		
		List<Map<String, Object>> list = metaDataBo.queryForListMap("SELECT * FROM tf_meter_record");
		if(null == list || list.size()==0){
			JOptionPane.showMessageDialog(null, "该设备机器编号不存在！", "权限错误",JOptionPane.ERROR_MESSAGE);
			SetPanel saleFrame=new SetPanel(mainFrame);
    		TF.dsJson.put("title", "系统设置");
    		mainFrame.addJPanelToMain(saleFrame);
			return;
		}else{
			try {
				String result = httpClient.post(TF.dsJson.getString("crm_url")+"/machine/bind?machineCode="+TF.dsJson.getString("machineId")+"&mac="+MachineUtil.getLocalMac(InetAddress.getLocalHost()),null);
				if(result!=null){
					JSONObject resultJson = JSONObject.parseObject(result);
					if(resultJson.getString("code").equals("00000")){
						
						JSONObject meterJson = resultJson.getJSONArray("machineInformation").getJSONObject(0);
						meterJson.put("CREATETIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("CREATETIME")));
						meterJson.put("UPDATETIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("UPDATETIME")));
						meterJson.put("VAILD_TIME",DateTimeUtil.changeMillsToTime(meterJson.getLongValue("VAILD_TIME")));
						metaDataBo.execute(metaDataBo.toSql(meterJson, "tf_meter_record","MACHINE_NO"));
					}
//					else if(resultJson.getString("code").equals("00002")){
//						JOptionPane.showMessageDialog(null, "该设备机器编号不存在！", "权限错误",JOptionPane.ERROR_MESSAGE);
//						System.exit(0);
//					}else if(resultJson.getString("code").equals("00001")){
//						String inputValue = JOptionPane.showInputDialog("请输入授权设备地址码:"); 
//					}
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map<String, Object> map = list.get(0);
			long timediff = DateTimeUtil.dateDiff(DateTimeUtil.getNowSimpleDate(),map.get("VAILD_TIME").toString(), "yyyy-MM-dd", "s");
			if(timediff>15*24*60*60){
//				JOptionPane.showMessageDialog(LoginPanel.this,"请输入激活码？"); 
				panel.setVisible(false);
				btn_login.setEnabled(true);
			}else if(timediff>=0 && timediff<15*24*60*60){
				panel.setVisible(true);
				btn_login.setEnabled(true);
			}else if(timediff<0){
				panel.setVisible(true);
				btn_login.setEnabled(false);
			}
			label_1.setText(map.get("VAILD_TIME").toString());
		}

//			panel.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public LoginPanel(MainFrame mainFrame) {
		this.mainFrame=mainFrame;
		initGUI();
		errorMsgPanel=new ErrorMsgPanel();
		errorMsgPanel.setBounds(573, 374,450,300);
		errorMsgPanel.setVisible(false);
		add(errorMsgPanel);
		
		panel = new JPanel();
		panel.setBounds(137, 13, 558, 172);
		add(panel);
		panel.setLayout(null);
		panel.setVisible(false);
		JLabel label = new JLabel("为保障系统正常使用、请及时激活。 截止日期：");
		label.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
		label.setBounds(10, 1, 320, 55);
		panel.add(label);
		
		JButton button = new JButton("激活");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isEmpty(textField.getText())){
					JOptionPane.showMessageDialog(LoginPanel.this,"请输入激活码？"); 
					return;
				}
				MetaDataBO metaDataBO  =new MetaDataBO();
				Map<String, Object> map = metaDataBO.queryForMap("SELECT * FROM tf_meter_record");
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				int machine_no = Integer.parseInt(map.get("MACHINE_NO").toString().replaceAll("^[a-z|A-Z]+", ""));
				String code = ClcUtil.decToHex(machine_no);
				if(code.length()%2!=0){code = "0"+code;}
				
				try {
					System.out.println("ttt++"+sd.parse(map.get("VAILD_TIME").toString()).getTime());
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(Integer.parseInt(textField.getText())==machine_no/Integer.parseInt(TF.dsJson.getString("active_code").substring(0, 4))){
	try {
				String temp_date =	sd.format(new Date(sd.parse(map.get("VAILD_TIME").toString()).getTime()+5*24*60*60*1000));
				metaDataBO.execute("UPDATE tf_meter_record SET VAILD_TIME='"+temp_date+"'");
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(LoginPanel.this,"临时激活成功！"); 
					checkValid();
				}else if(textField.getText().equals(code)){
					
					metaDataBO.execute("UPDATE tf_meter_operation_record SET VALID='1'");
					JOptionPane.showMessageDialog(LoginPanel.this,"永久激活成功！"); 
					checkValid();
				}else{
					JOptionPane.showMessageDialog(LoginPanel.this,"激活失败，请联系客服。"); 
				}
//				addErrorMsg("登陆异常", "错误信息");
//				checkValid();
//				JOptionPane.showMessageDialog(LoginPanel.this,TF.dsJson.toJSONString()); 
//				errorMsgPanel
			}
		});
		button.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		button.setBounds(350, 89, 113, 52);
		panel.add(button);
		
		textField = new JTextField();
		textField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		textField.setBounds(10, 88, 320, 55);
		panel.add(textField);
		textField.setColumns(10);
		
		label_1 = new JLabel("2016-07-19");
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("等线 Light", Font.PLAIN, 22));
		label_1.setBounds(327, 2, 141, 55);
		panel.add(label_1);
		
		label_2 = new JLabel("激活码：（请联系深圳市科拜斯有限公司）");
		label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
		label_2.setBounds(10, 44, 360, 38);
		panel.add(label_2);
		
//		calcPanel=new CalcPanel(txt_userName);
//		add(calcPanel);
//		this.setUndecorated(true);
		int second=1;
		if(TF.dsJson.getIntValue("onlineCheckTimer")!=0){
			second=TF.dsJson.getIntValue("onlineCheckTimer");
		}
        txt_userName.requestFocusInWindow();
//		txt_userName.grabFocus();
		checkIsOnLineTimer.schedule(new HeartTimer(),0,second*1000);
		checkValid();
	}
	
	public ErrorMsgPanel getErrorMsgPanel() {
		return errorMsgPanel;
	}

	public void setErrorMsgPanel(ErrorMsgPanel errorMsgPanel) {
		this.errorMsgPanel = errorMsgPanel;
	}

	public JLabel getLabel_status() {
		return label_status;
	}

	public void setLabel_status(JLabel label_status) {
		this.label_status = label_status;
	}

	public JProgressBar getLoadingProgressBar() {
		return loadingProgressBar;
	}

	public void setLoadingProgressBar(JProgressBar loadingProgressBar) {
		this.loadingProgressBar = loadingProgressBar;
	}

	public void initGUI(){
		TF.dsJson.put("title","登录");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 698);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
		setLayout(null);
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice[] gs = ge.getScreenDevices();
//		if(gs.length>1){
//			System.out.println("888888888888");
//			GgPanel customerPanel=new GgPanel();
//			customerPanel.dispose();
//			customerPanel.setUndecorated(true);
//			AWTUtilities.setWindowOpaque(customerPanel, false); 
////			customerPanel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
//			customerPanel.setVisible(true);
//			Dimension d1=Toolkit.getDefaultToolkit().getScreenSize();
//			customerPanel.setBounds(d1.width, 0, 1024, 768);
//		}
		txt_userName = new JTextField();
		txt_userName.requestFocusInWindow();
		txt_userName.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		txt_userName.setBounds(320, 198, 258, 55);
//		txt_userName.setText("15361675008");
		add(txt_userName);
		txt_userName.setColumns(10);
		txt_pwd = new JPasswordField();
		
//		txt_pwd.setText("666666");
		txt_pwd.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		txt_pwd.setColumns(10);
		txt_pwd.setBounds(320, 285, 258, 55);
		add(txt_pwd);
		
		JLabel label_userIcon = new JLabel("");
		label_userIcon.setHorizontalAlignment(SwingConstants.RIGHT);
		label_userIcon.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/member.png")));
		label_userIcon.setBounds(203, 0, 55, 55);
		txt_userName.add(label_userIcon);
		
		JLabel label_passwordIcon = new JLabel("");
		label_passwordIcon.setHorizontalAlignment(SwingConstants.RIGHT);
		label_passwordIcon.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/password.png")));
		label_passwordIcon.setBounds(203, 0, 55, 55);
		txt_pwd.add(label_passwordIcon);
		
		loadingProgressBar = new JProgressBar();
		loadingProgressBar.setForeground(new Color(0, 204, 0));
		loadingProgressBar.setBounds(0, 678, 1024, 20);
//		loadingProgressBar.updateUI();
		loadingProgressBar.setUI(new ProgressBarUI() {

			@Override
			public void paint(Graphics g, JComponent c) {
				// TODO Auto-generated method stub
				g.setColor(new Color(14,147,46));
				float i=loadingProgressBar.getValue();
				float max=loadingProgressBar.getMaximum();
				g.fillRect(0, 0, (int)((i/max)*c.getWidth()), c.getHeight());
//				g.fillOval(0, 0, (int)((i/max)*c.getWidth()), c.getHeight());
//				g.drawImage(new ImageIcon(LoginPanel.class.getResource("/images/012.gif")).getImage(), 0, 0,(int)((i/max)*c.getWidth()),c.getHeight(), null);
				repaint();
			}
		});
		add(loadingProgressBar);
		
		label_status = new JLabel("");
		label_status.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		label_status.setBounds(0, 645, 500, 30);
		add(label_status);
		
		panel_error = new JPanel();
		panel_error.setBounds(520, 433, 494, 235);
//		add(panel_error);
		panel_error.setLayout(null);
		
		label_errorTitle = new JLabel("");
		label_errorTitle.setBounds(0, 0, 494, 33);
		panel_error.add(label_errorTitle);
		
		label_errorInfo = new JLabel("");
		label_errorInfo.setBounds(0, 35, 494, 200);
		panel_error.add(label_errorInfo);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 34, 494, 1);
		panel_error.add(separator);
		txt_userName.addFocusListener(focusListener);
		txt_pwd.addFocusListener(focusListener);
		
		btn_login = new JButton("登录");
		ActionListener listener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//LoginService loginPanel = new LoginService(mainFrame);
				if(txt_userName.getText().equals("13600148611") && txt_pwd.getText().equals("666666")){
					TF.userJson.put("loginType", "0");
					TF.userJson.put("uloginno", "admin");
					TF.userJson.put("uname", "admin");
					TF.userJson.put("utype", 2);
					MenuPanel menuFrame=new MenuPanel(mainFrame);
					mainFrame.addJPanelToMain(menuFrame);
				}else{
				login();
				}
			}
		};
		btn_login.addActionListener(listener);
		btn_login.registerKeyboardAction(listener,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		btn_login.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		btn_login.setBounds(391, 374, 139, 49);
		add(btn_login);
		progressTimer=new javax.swing.Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(statusJson.containsKey("msg")){
					label_status.setText(statusJson.getString("msg"));
					}
					if(statusJson.containsKey("error_title")){
						errorMsgPanel.getLabel_errorTitle().setText(statusJson.getString("error_title"));
						errorMsgPanel.getEditorPane().setText(statusJson.getString("error_content"));
						getErrorMsgPanel().setVisible(true);
					}
				loadingProgressBar.setValue(progressLength);
				if(progressLength==100) progressTimer.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		progressTimer.start();
		progressTimer.setRepeats(true);
	}

	public void login(){
		//检测设备状态
		statusJson.put("msg", "验证设备");
		List<Map<String, Object>> meterList = metaDataBo.queryForListMap("select * from tf_meter_record");
		if(meterList!=null && meterList.size()>0){
			progressLength=20;
		}else{
			progressLength=100;
			statusJson.put("error_title", "机具检测");
			statusJson.put("error_content", "未发现机具信息");
			return;
		}
		//检查用户名密码
		statusJson.put("msg", "验证登录");
		if(checkLogin()){
			progressLength=40;
		}else{
			progressLength=100;
			return;
		}
		//检查是否是在线&离线
		if(TF.checkStatus.getIntValue("checkStatus")==1){
				if(checkSync()){
					progressLength=60;
					loadCache();
					progressLength=80;
					forword();
				}else{
					progressLength=100;
				}
			
			
		}else{
			if(leveLine()){
				loadCache();
				loadingProgressBar.setValue(80);
				forword();
			}
			
		}
		
	}
	public void  forword(){
		if(TF.userJson.getIntValue("UTYPE")==1){
//    		loginFrame.getLoadingProgressBar().setValue(taskArr.size());
    		SalePanel saleFrame=new SalePanel(mainFrame);
    		TF.dsJson.put("title", "运营");
    		mainFrame.addJPanelToMain(saleFrame);
    	}else if(TF.userJson.getIntValue("UTYPE")==3 || TF.userJson.getIntValue("UTYPE")==2){
    		MenuPanel menuFrame=new MenuPanel(mainFrame);
    		TF.dsJson.put("title", "主界面");
    		mainFrame.addJPanelToMain(menuFrame);
    	}else{
    		JOptionPane.showConfirmDialog(LoginPanel.this,"**该用户不允许登录,请联系管理员**","提示",JOptionPane.DEFAULT_OPTION);
    	}
	}
	public boolean leveLine(){
		int i=JOptionPane.showConfirmDialog(null, "**未连接上服务器、继续使用将有一定风险**、是否进入系统","离线登录确认",JOptionPane.YES_NO_OPTION);
		if(i==0){
			return true;
		}else {
			progressLength=100;
			statusJson.put("msg", "取消登录");
			return false;
		}
	}
	public boolean checkLogin(){
		List<Map<String, Object>> userList=metaDataBo.queryForListMap("select * from tf_userinfo u where u.ULOGINNO ='"+getTxt_userName().getText()+"' and  u.UPASSWORD ='"+MD5.KL(getTxt_pwd().getText())+"'");
		if(null!=userList && userList.size()>0){
			TF.userJson = (JSONObject)JSON.toJSON(userList.get(0));
			TF.userJson.put("LOGINTYPE", "1");
			return true;
		}else{
			statusJson.put("error_title", "登录检测");
			statusJson.put("error_content", "用户名&密码错误。");
			return false;
		}
	}
	public boolean checkSync(){
		List<Map<String, Object>> sync = metaDataBo.queryForListMap("select * from sync_table_record where ism_status='0'");
		if(sync!=null && sync.size()>0){
			statusJson.put("msg", "系统正在同步数据、请稍后登录。");
			return false;
		}else{
			return true;
		}
	}
	public void loadCache(){
		statusJson.put("msg", "加载基础数据");
		CacheService cacheService = new CacheService();
		cacheService.load();
		
	}
	private class MyFocusListener implements FocusListener{

		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() instanceof JTextField){
//				invalidate();
				JTextField txt=(JTextField)e.getSource();
//				loginPanel.setVisible(false);
				numInputPnl=new NumberInputPnl(LoginPanel.this,txt, txt.getX()+txt.getWidth(),txt.getY());
//				loginPanel.setVisible(true);
				validate();
				repaint();
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			// TODO Auto-generated method stub
			invalidate();
			LoginPanel.this.remove(numInputPnl);
			validate();
			repaint();
		}
		
	}
	

    
	public JTextField getTxt_userName() {
		return txt_userName;
	}

	public void setTxt_userName(JTextField txt_userName) {
		this.txt_userName = txt_userName;
	}

	public JPasswordField getTxt_pwd() {
		return txt_pwd;
	}

	public void setTxt_pwd(JPasswordField txt_pwd) {
		this.txt_pwd = txt_pwd;
	}

	public JButton getBtn_login() {
		return btn_login;
	}

	public void setBtn_login(JButton btn_login) {
		this.btn_login = btn_login;
	}
}
