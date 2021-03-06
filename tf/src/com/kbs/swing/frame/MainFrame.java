package com.kbs.swing.frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.alibaba.fastjson.JSONObject;
import com.kbs.swing.CommonInterface;
import com.kbs.swing.panel.BottomPanel;
import com.kbs.swing.panel.BusyPanel;
import com.kbs.swing.panel.TopPanel;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;
import com.kbs.util.WindowUtil;

public class MainFrame extends JFrame {

//	private JPanel contentPane;
	private JPanel mainPanel;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;
	private Timer displayTimer;
	private BusyPanel glassPanel = new BusyPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
//					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		TF.dsJson.put("title","主界面");
		setGlassPane(glassPanel);
		glassPanel.start();
		glassPanel.setVisible(false);
		try
        {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible",false);
        }
        catch(Exception e)
        {
            //TODO exception
        }
		initGUI();
		displayTimer= new javax.swing.Timer(1000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				taskArr=new JSONArray();
//				runTask(taskArr);
				display();
			}
		});
		displayTimer.start();
		displayTimer.setRepeats(true);
		
		setUndecorated(true);
		setVisible(true);
		
		getTopPanel().getBtn_logout().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				back(1);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				if(getTopPanel().getBtn_logout()==e.getSource()){
					getTopPanel().getBtn_logout().setCursor(new Cursor(Cursor.HAND_CURSOR));
					getTopPanel().getBtn_logout().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/shutdown2.png")));
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				if(getTopPanel().getBtn_logout()==e.getSource()){
					getTopPanel().getBtn_logout().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					getTopPanel().getBtn_logout().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/shutdown.png")));
				}
			}
			
		});
		WindowUtil.centerWindow(MainFrame.this);
	}
	
	public void initGUI(){
		TF.dsJson.put("title","主界面");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 768);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(null);
		topPanel=new TopPanel();
		topPanel.getLabel_userName().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/member.png")));
//		topPanel.getLabel_rfidStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/rfid.png")));
//		topPanel.getLabel_rfidStatus().setText("");
//		topPanel.getLabel_icStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/ic.png")));
//		topPanel.getLabel_icStatus().setText("");
//		topPanel.getLabel_serverStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/onlinel.png")));
//		topPanel.getLabel_serverStatus().setText("");
		topPanel.getBtn_logout().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/shutdown.png")));
		topPanel.setBounds(0, 0, 1024, 40);
//		topPanel.setBackground(Color.GRAY);
		bottomPanel=new BottomPanel();
		bottomPanel.getLabel_machineNo().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/machine.png")));
		bottomPanel.getLabel_time().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/time.png")));
		bottomPanel.getLabel_time().setHorizontalAlignment(SwingConstants.RIGHT);
		bottomPanel.setBounds(0, 738, 1024, 30);
//		bottomPanel.setBackground(Color.GRAY);
		getContentPane().add(topPanel);
		getContentPane().add(bottomPanel);
	}
	
	public JPanel getMainPanel() {
		return mainPanel;
	}

	public TopPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(TopPanel topPanel) {
		this.topPanel = topPanel;
	}

	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setBottomPanel(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public void addJPanelToMain(JPanel panel){
		if(mainPanel!=null)
		this.remove(mainPanel);
		mainPanel=panel;
		mainPanel.setBounds(0, 40, 1024, 698);
		getContentPane().add(mainPanel);
		mainPanel.setVisible(false);
		mainPanel.setVisible(true);
	}
	
	public void back(int status){
		//正常点击返回
		if(status==1){
			if(TF.userJson==null||TF.userJson.keySet().size()<1){
				int i=JOptionPane.showConfirmDialog(MainFrame.this, "确认退出系统吗?","退出",JOptionPane.YES_NO_OPTION);
				if(i==0){
					System.exit(0);
				}
			}else{ 
				if(TF.userJson.getIntValue("UTYPE")==1){
				//收营员
				int i=JOptionPane.showConfirmDialog(MainFrame.this, "确认退出登录吗?","注销用户",JOptionPane.YES_NO_OPTION);
				if(i==0){
					TF.userJson=new JSONObject();
					TF.dsJson.put("title","登录");
					addJPanelToMain(new LoginPanel(MainFrame.this));
				}
			}else {
				if(getMainPanel().getClass().getSimpleName().equals("MenuPanel")){
					int i=JOptionPane.showConfirmDialog(MainFrame.this, "确认退出登录吗?","注销用户",JOptionPane.YES_NO_OPTION);
					if(i==0){
						TF.userJson=new JSONObject();
						TF.dsJson.put("title","登录");
						addJPanelToMain(new LoginPanel(MainFrame.this));
					}
				}else{
					if(mainPanel instanceof CommonInterface){
						CommonInterface comm=(CommonInterface) mainPanel;
						comm.cancleTimer();
					}
					TF.dsJson.put("title","主界面");
					addJPanelToMain(new MenuPanel(MainFrame.this));
				}
			}
		}
		//当前不在营业时间
		}else if(status==0){
			if(TF.userJson.getIntValue("UTYPE")==1){
				TF.userJson=null;
				addJPanelToMain(new LoginPanel(MainFrame.this));
			}else if(TF.userJson.getIntValue("UTYPE")==2){
				if(mainPanel instanceof CommonInterface){
					CommonInterface comm=(CommonInterface) mainPanel;
					comm.cancleTimer();
				}
				TF.dsJson.put("title","主界面");
				addJPanelToMain(new MenuPanel(MainFrame.this));
			}else{
				TF.dsJson.put("title","登录");
				addJPanelToMain(new LoginPanel(MainFrame.this));
			}
		}
	}
	
	public Timer getDisplayTimer() {
		return displayTimer;
	}

	public void setDisplayTimer(Timer displayTimer) {
		this.displayTimer = displayTimer;
	}

	public void setBusy(boolean busy) {
		if (busy) {
			glassPanel.stop();
			glassPanel.start();
			glassPanel.setVisible(true);
		} else {
			glassPanel.stop();
			glassPanel.setVisible(false);
		}
	}
	
	public void display(){
		MainFrame.this.getTopPanel().getLabel_title().setText(TF.dsJson.getString("title"));
		MainFrame.this.getTopPanel().getLabel_userName().setText(TF.userJson.getString("ULOGINNO")==null?"无":TF.userJson.getString("ULOGINNO"));
		if(TF.comStatusMap.getIntValue("rfidComStatus")==1){
			MainFrame.this.getTopPanel().getLabel_rfidStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/rfid.png")));
//			MainFrame.this.getTopPanel().getLabel_rfidStatus().setBorder(BorderFactory.createLineBorder(Color.GREEN));
		}else{
			MainFrame.this.getTopPanel().getLabel_rfidStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/rfid2.png")));
//			MainFrame.this.getTopPanel().getLabel_rfidStatus().setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		if(TF.comStatusMap.getIntValue("icComStatus")==1){
			MainFrame.this.getTopPanel().getLabel_icStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/ic.png")));
//			MainFrame.this.getTopPanel().getLabel_icStatus().setBorder(BorderFactory.createLineBorder(Color.GREEN));
		}else{
			MainFrame.this.getTopPanel().getLabel_icStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/ic2.png")));
//			MainFrame.this.getTopPanel().getLabel_icStatus().setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		if(TF.checkStatus.getIntValue("checkStatus")==1){
			MainFrame.this.getTopPanel().getLabel_serverStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/onlinel.png")));
		}else{
			MainFrame.this.getTopPanel().getLabel_serverStatus().setIcon(new ImageIcon(GetProjectRealPath.getPath("images/onlinel2.png")));
//			MainFrame.this.getTopPanel().getLabel_serverStatus().setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		MainFrame.this.getBottomPanel().getLabel_machineNo().setText(TF.meterJson.getString("MACHINE_NAME")==null?"无":TF.meterJson.getString("MACHINE_NAME"));
		MainFrame.this.getBottomPanel().getLabel_time().setText(DateTimeUtil.getNowDate()+"   ");
	}
	
}
