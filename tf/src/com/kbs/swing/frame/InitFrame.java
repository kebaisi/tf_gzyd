package com.kbs.swing.frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.dsa224;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.ic.IcUtils;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.commons.rfid.RfidUtils;
import com.kbs.consume.operations.FaceThread;
import com.kbs.consume.operations.FixedValuation;
import com.kbs.consume.operations.IcThread;
import com.kbs.consume.operations.RfidThread;
import com.kbs.data.CheckOrderThread;
import com.kbs.sys.CacheService;
import com.kbs.sys.LoadBasicData;
import com.kbs.sys.LoadLogProperties;
import com.kbs.timer.LogThread;
import com.kbs.util.CheckProcessIsRunning;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;
import com.kbs.util.WindowUtil;

public class InitFrame extends JFrame {
	private Logger log = Logger.getLogger(InitFrame.class);
	private JPanel contentPane;
	private JProgressBar progressBar;
	private JLabel label_msg;
	private JLabel label_img;
	private JLabel label_logo;
	private JLabel label_companyName;
	JSONArray taskArr=new JSONArray();
	private MetaDataBO metaDataBO=new MetaDataBO();
	private JSONObject statusJson=new JSONObject();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(CheckProcessIsRunning.checkThreadNum("tf.exe")>2){
						JOptionPane.showMessageDialog(null, "程序已启动", "提示", JOptionPane.PLAIN_MESSAGE);
						System.exit(0);
					}
					InitFrame frame = new InitFrame();
					WindowUtil.centerWindow(frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InitFrame() {
		TF.dsJson.put("title", "登录");
		LoadLogProperties logProperties = new LoadLogProperties();
		initGUI();
		setUndecorated(true);
		setBackground(new Color(0,0,0,0));
		setVisible(true);
		LoadBasicData.loadData();
		checkEnv();
		
	}
	private void initGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 300, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(51, 204, 0));
		progressBar.setBounds(0, 295, 400, 10);
		contentPane.add(progressBar);
//		progressBar.setStringPainted(true);
		
		label_msg = new JLabel();
		label_msg.setHorizontalAlignment(SwingConstants.CENTER);
		label_msg.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		label_msg.setBounds(0, 265, 200, 30);
		contentPane.add(label_msg);
		
		label_img = new JLabel("");
		label_img.setVisible(false);
//		label_img.setBounds(0, 0, 400, 295);
//		ImageIcon ico=new ImageIcon(test.class.getResource("/img/jc1510231_1.jpg"));
//		ico.setImage(ico.getImage().getScaledInstance(label_img.getWidth(),label_img.getHeight(),Image.SCALE_DEFAULT)); 
//		label_img.setIcon(ico);
//		contentPane.add(label_img);
		
		label_logo = new JLabel("");
		label_logo.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/logo.png")));
		label_logo.setBounds(20, 200, 105, 40);
		ImageIcon ico1=new ImageIcon(GetProjectRealPath.getPath("images/logo.png"));
		ico1.setImage(ico1.getImage().getScaledInstance(label_logo.getWidth(),label_logo.getHeight(),Image.SCALE_DEFAULT)); 
		
		label_companyName = new JLabel("KBS欢迎你");
		label_companyName.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		label_companyName.setBounds(140, 200, 229, 40);
		contentPane.add(label_companyName);
		label_logo.setIcon(ico1);
		contentPane.add(label_logo);
	}
	
	private void checkEnv(){
		ProGressWork gressWork=new ProGressWork();
		gressWork.execute();
	}
	
	private void addTaskToArr(String methodName){
		try {
			Method method=InitFrame.this.getClass().getMethod(methodName);
			taskArr.add(method);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showMsg(String msg){
		label_msg.setText(msg);
	}
	
	public void checkMysql(){
		showMsg("检测数据库是否启动。。。");
		boolean flag=CheckProcessIsRunning.checkIsRunning("mysqld.exe");
		if(!flag){
			showMsg("启动数据库中。。。");
			String path=GetProjectRealPath.getPath("mysql-5.1.57-win32/bin/mysqld.exe");
			File file = new File(path);
			try {
				Runtime.getRuntime().exec(file.getPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
	}
	
	public void checkDb(){
		showMsg("检测数据服务是否启动...");
		boolean isDBRunning=CheckProcessIsRunning.checkIsRunning("tf_db.exe");
		if(!isDBRunning){
			showMsg("启动数据服务中...");
			String path=GetProjectRealPath.getPath("tf_db.exe");
			File file = new File(path);
			try {
				Runtime.getRuntime().exec(file.getPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("数据服务启动失败");
			}
		}
	}
	
	public void checkRfid(){
		showMsg("检测rfid读写器中...");
		boolean rfidIsOk=false;
		JSONObject rfidJson=new JSONObject();
		RfidUtils rfidUtils=RfidUtils.getInstance();
		try {
			
			rfidJson= rfidUtils.findSerialPort(TF.rfidJson);
			rfidIsOk =rfidJson.getBooleanValue("result");
		} catch (Throwable e) {
			log.error("检测RFID串口出现异常"+e.getMessage());
			rfidIsOk=false;
		}
		if(rfidIsOk){
			TF.comStatusMap.put("rfidComStatus", 1);
		}
		else{
			TF.comStatusMap.put("rfidComStatus", 0);
			log.error("RFID未连接");
		}
//		}
	}
	
	public void checkIc(){
		showMsg("检测ic读写器中...");
		boolean icIsOk=false;
		JSONObject icJson=new JSONObject();
		IcUtils icUtils=IcUtils.getInstance();
		try {
			icUtils.close(TF.icJson);
			Thread.sleep(10);
			icJson=icUtils.findSerialPort(TF.icJson);
			icIsOk =icJson.getBooleanValue("result");
		} catch (Throwable e) {
			log.error("检测IC串口出现异常"+e.getMessage());
			icIsOk=false;
		}
		if(icIsOk){
			TF.comStatusMap.put("icComStatus", 1);
		}
		else{
			TF.comStatusMap.put("icComStatus", 0);
			log.error("IC刷卡器未连接");
		}
	}
	
	class ProGressWork extends SwingWorker<Integer, Integer> {  
        @Override  
        protected Integer doInBackground() throws Exception {
    		addTaskToArr("checkMysql");
    		addTaskToArr("checkDb");
    		if(TF.dsJson.getIntValue("isRFID")==0){
    		addTaskToArr("checkRfid");
    		}
    		if(TF.dsJson.getIntValue("cardType")==0){
    		addTaskToArr("checkIc");
    		}
    		progressBar.setMaximum(taskArr.size());
    		try {
    			for(int i=0;i<taskArr.size();i++){
    				int index=i;
					Method m=(Method) taskArr.get(index);
					m.invoke(InitFrame.this, null);
					publish(i+1);
				
    			}
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}showMsg("检测完成...");
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    		MainFrame mainFrame=new MainFrame();
    		List<Map<String, Object>> list = metaDataBO.queryForListMap("SELECT * FROM tf_meter_record");
    		if(null == list || list.size()==0){
    			JOptionPane.showMessageDialog(null, "该设备机器编号不存在！", "权限错误",JOptionPane.ERROR_MESSAGE);
    			SetPanel saleFrame=new SetPanel(mainFrame);
        		TF.dsJson.put("title", "系统设置");
        		mainFrame.addJPanelToMain(saleFrame);
        		InitFrame.this.dispose();  
        		return 1;
    		}else{
			LoginPanel loginPanel=new LoginPanel(mainFrame);
			mainFrame.addJPanelToMain(loginPanel);
			InitFrame.this.dispose();  
    		return 1;
    		}
    		
    	}  
        //调用publist的时候会调用  
        //注意这里是"批处理"  
        @Override  
        protected void process(List<Integer> list) {  
        	for(Integer i:list){
        		progressBar.setValue(i);
        	}
        }  
        @Override  
        protected void done() {
        	if(TF.dsJson.getIntValue("isRFID")==0){
        		RfidThread rfidThread=new RfidThread();
        		rfidThread.start();
        	}
        	IcThread icThread=new IcThread();
//        	FaceThread faceThread = new FaceThread();
        	CheckOrderThread checkOrderThread = new CheckOrderThread();
//        	faceThread.start();
        
    		icThread.start();
    		checkOrderThread.start();
    		LogThread logThread = new LogThread();
        	Thread cmdLog = new Thread(logThread);
        	cmdLog.start();
    		if(TF.dsJson.getDoubleValue("fixed_price")>0){
    		FixedValuation fixedValuation = new FixedValuation();
    		Thread fixThread = new Thread(fixedValuation);
    		fixThread.start();
    		
    		}
    		
    		
    		
    		
    		
        }  
    }  
}
