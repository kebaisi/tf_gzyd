package com.kbs.swing.frame;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.alibaba.fastjson.JSONArray;
import com.kbs.consume.service.ConsumeService;
import com.kbs.sys.Oper;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;

public class MenuPanel extends JPanel {

	private JPanel contentPane;
	private JPanel mainPanel;
	private JSONArray actionArr;
	private MainFrame mainFrame;
	private ConsumeService consumeService=new ConsumeService();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MenuPanel frame = new MenuPanel();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	
	public MenuPanel(MainFrame mainFrame) {
		this.mainFrame=mainFrame;
//		try
//        {
//            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//            UIManager.put("RootPane.setupButtonVisible",false);
//        }
//        catch(Exception e)
//        {
//            //TODO exception
//        }
		initGUI();
		addBtns();
	}
	
	public void initGUI(){
		setSize(1024, 696);
		setLayout(null);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(170, 95, 618, 416);
		add(mainPanel);
		mainPanel.setLayout(new GridLayout(2,2,3,3));
	}
	
	public void addBtns(){
		createBtn("images/sale.png", "营业",new SaleActionListener());
		//createBtn("images/manager.png", "运营管理",new SaleManagerActionListener());
		createBtn("images/query.png", "统计",new QueryActionListener());
		createBtn("images/set.png", "系统设置",new SetActionListener());
		createBtn("images/help.png", "帮助",new HelpActionListener());
	}
	
	public void createBtn(String src,String txt,ActionListener listener){
		JButton btn1 = new JButton(txt, new ImageIcon(GetProjectRealPath.getPath(src))) ;
		btn1.addActionListener(listener);
		btn1.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn1.setHorizontalTextPosition(SwingConstants.CENTER);
		btn1.setVerticalTextPosition(SwingConstants.BOTTOM);
		mainPanel.add(btn1);
	}
	
	private class SaleActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(TF.userJson.get("LOGINTYPE").equals("0")){
				JOptionPane.showConfirmDialog(mainFrame,"系统用户不能进入运营页面","提示",JOptionPane.DEFAULT_OPTION);
				return;
			}
	        String mealTime=Oper.getCurrMites();
	        if(mealTime!=null&&!mealTime.equals("")){
	        	TF.currentMealTimes=Oper.getCurrMites();
	        	Oper.loadConsumeNum();
	        }
			if(TF.currentMealTimes=="0"){
				Map<String,String> mealMap=consumeService.queryAllMealTime();
				String str="";
				for(String name:mealMap.keySet()){
					str+=name+":"+mealMap.get(name)+"\n";
				}
				JOptionPane.showConfirmDialog(mainFrame,"非营业时间段:\n"+str,"提示",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
				return;
			}else{
				mainFrame.addJPanelToMain(new SalePanel(mainFrame));
			}
		}
		
	} 
	
	private class SaleManagerActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainFrame.addJPanelToMain(new SaleManagerFrame(mainFrame));
		}
		
	}
	
	private class QueryActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainFrame.addJPanelToMain(new ConsumeQueryFrame(mainFrame));
		}
		
	}
	
	private class SetActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainFrame.addJPanelToMain(new SetPanel(mainFrame));
		}
		
	}
	
	private class HelpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			mainFrame.addJPanelToMain(new HelpPanel(mainFrame));
		}
		
	}
}
