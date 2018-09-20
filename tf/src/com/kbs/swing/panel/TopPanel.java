package com.kbs.swing.panel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.ic.IcUtils;
import com.kbs.commons.rfid.RfidUtils;
import com.kbs.consume.operations.RfidThread;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;

public class TopPanel extends JPanel {
	private Logger log = Logger.getLogger(TopPanel.class);
	private JLabel label_title;
	private JLabel label_serverStatus;
	private JLabel label_rfidStatus;
	private JLabel label_icStatus;
	private JLabel label_userName;
	private JButton btn_logout;
	private JLabel lbl_fps;
	private JLabel lblNewLabel_1;

//	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TopPanel frame = new TopPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TopPanel() {
		initGUI();
//		setUndecorated(true);
		Runnable runnable = new Runnable() {  
	         @Override  
	         public void run() {  
	        	 try {
	        		 fps();
					} catch (Exception e1) {
						e1.printStackTrace();
					}  
	         } 
	     };  
	     new Thread(runnable).start(); 
	}
	public void fps(){
		int fps=0;
		String line = null;
	    try
	    {
	        Process pro = Runtime.getRuntime().exec("ping "+TF.dsJson.getString("serverIp")+" -t");
	        BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream(),"gbk"));
	        while ((line = buf.readLine()) != null){
	        	if(line.indexOf("时间<")>0){
	        		fps = Integer.parseInt(line.substring(line.indexOf("时间<")+3, line.indexOf("ms")));
	        		lbl_fps.setText(fps+"ms");
	        		if(fps<100){
	        			lbl_fps.setForeground(new Color(0, 204, 51));
	        		}else if(fps>=100 && fps<300){
	        			lbl_fps.setForeground(new Color(255, 204, 0));
	        		}else{
	        			lbl_fps.setForeground(new Color(204, 0, 0));
	        		}
	        	}
	        	else if(line.indexOf("时间=")>0){
	        		fps = Integer.parseInt(line.substring(line.indexOf("时间=")+3, line.indexOf("ms")));
	        		lbl_fps.setText(fps+"ms");
	        		if(fps<100){
	        			lbl_fps.setForeground(new Color(0, 204, 51));
	        		}else if(fps>=100 && fps<300){
	        			lbl_fps.setForeground(new Color(255, 204, 0));
	        		}else{
	        			lbl_fps.setForeground(new Color(204, 0, 0));
	        		}
	            	}
	        	else{
	        		lbl_fps.setText(line);
	        		lbl_fps.setForeground(new Color(204, 0, 0));
	        	}
	        }
	    }
	    catch (Exception ex)
	    {
	        System.out.println(ex.getMessage());
	    }

	}
	public void initGUI(){
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 40);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
		setLayout(null);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		JLabel label_logo = new JLabel();
		label_logo.setBounds(0, 0, 84, 40);
		ImageIcon logoIco=new ImageIcon(GetProjectRealPath.getPath("images/logo.png"));
		logoIco.setImage(logoIco.getImage().getScaledInstance(label_logo.getWidth(),label_logo.getHeight(),Image.SCALE_DEFAULT));;
		label_logo.setIcon(logoIco);
		add(label_logo);
		
		label_title = new JLabel("登录");
		label_title.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		label_title.setBounds(87, 0, 100, 40);
		add(label_title);
		
		lbl_fps = new JLabel("请求超时。");
		lbl_fps.setForeground(new Color(204, 0, 0));
		lbl_fps.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		lbl_fps.setBounds(555, 0, 111, 40);
		add(lbl_fps);
		
		label_serverStatus = new JLabel("");
		label_serverStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_serverStatus.setFont(new Font("微软雅黑", Font.PLAIN, 20));
//		label_serverStatus.setBorder(BorderFactory.createLineBorder(Color.gray));
		label_serverStatus.setBounds(700, 2, 40, 36);
		add(label_serverStatus);
		
		label_rfidStatus = new JLabel("");
		label_rfidStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(TF.comStatusMap.getIntValue("rfidComStatus")==1){
					return;
				}
				boolean rfidIsOk=false;
				JSONObject rfidJson=new JSONObject();
				RfidUtils rfidUtils=RfidUtils.getInstance();
				try {
					rfidJson= rfidUtils.findSerialPort(TF.rfidJson);
					rfidIsOk =rfidJson.getBooleanValue("result");
				} catch (Throwable e1) {
					log.error("检测RFID串口出现异常"+e1.getMessage());
					rfidIsOk=false;
				}
				if(rfidIsOk){
					TF.comStatusMap.put("rfidComStatus", 1);
				}
				else{
					TF.comStatusMap.put("rfidComStatus", 0);
					log.error("RFID未连接");
				}
			}
		});
		label_rfidStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_rfidStatus.setFont(new Font("微软雅黑", Font.PLAIN, 20));
//		label_rfidStatus.setBorder(BorderFactory.createLineBorder(Color.gray));
		label_rfidStatus.setBounds(750, 2, 40, 36);
		add(label_rfidStatus);
		
		label_icStatus = new JLabel("");
		label_icStatus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(TF.comStatusMap.getIntValue("icComStatus")==1){
					return;
				}
				boolean icIsOk=false;
				JSONObject icJson=new JSONObject();
				IcUtils icUtils=IcUtils.getInstance();
				try {
					icJson=icUtils.findSerialPort(TF.icJson);
					icIsOk =icJson.getBooleanValue("result");
				} catch (Throwable e1) {
					log.error("检测IC串口出现异常"+e1.getMessage());
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
		});
		label_icStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_icStatus.setFont(new Font("微软雅黑", Font.PLAIN, 20));
//		label_icStatus.setBorder(BorderFactory.createLineBorder(Color.gray));
		label_icStatus.setBounds(800, 2, 40, 36);
		add(label_icStatus);
		
		label_userName = new JLabel("1501");
		label_userName.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		label_userName.setBounds(870, 0, 100, 40);
		add(label_userName);
		
		btn_logout = new JButton();
		btn_logout.setFocusable(false);
		btn_logout.setBounds(984, 0, 40, 39);
		add(btn_logout);
		
		lblNewLabel_1 = new JLabel("FPS:");
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(514, 0, 51, 40);
		add(lblNewLabel_1);
	}
	
	@Override
	public void print(Graphics g) {
		// TODO Auto-generated method stub
		super.print(g);
	}

	public JLabel getLabel_title() {
		return label_title;
	}

	public void setLabel_title(JLabel label_title) {
		this.label_title = label_title;
	}

	public JLabel getLabel_serverStatus() {
		return label_serverStatus;
	}

	public void setLabel_serverStatus(JLabel label_serverStatus) {
		this.label_serverStatus = label_serverStatus;
	}

	public JLabel getLabel_rfidStatus() {
		return label_rfidStatus;
	}

	public void setLabel_rfidStatus(JLabel label_rfidStatus) {
		this.label_rfidStatus = label_rfidStatus;
	}

	public JLabel getLabel_icStatus() {
		return label_icStatus;
	}

	public void setLabel_icStatus(JLabel label_icStatus) {
		this.label_icStatus = label_icStatus;
	}

	public JLabel getLabel_userName() {
		return label_userName;
	}

	public void setLabel_userName(JLabel label_userName) {
		this.label_userName = label_userName;
	}

	public JButton getBtn_logout() {
		return btn_logout;
	}

	public void setBtn_logout(JButton btn_logout) {
		this.btn_logout = btn_logout;
	}

}
