package com.kbs.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.PanelUI;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.deduction.CashDeductionFactory;
import com.kbs.consume.deduction.DlbDeductionFactory;
import com.kbs.consume.deduction.FreeDeductionFactory;
import com.kbs.consume.deduction.IDeductionFactory;
import com.kbs.consume.deduction.WxDeductionFactory;
import com.kbs.consume.deduction.ZfbDeductionFactory;
import com.kbs.consume.operations.FixedValuation;
import com.kbs.consume.operations.IOperationsDeduction;
import com.kbs.consume.operations.OperationsWxDeduction;
import com.kbs.consume.operations.RfidThread;
import com.kbs.consume.service.ConsumeService;
import com.kbs.swing.CommonInterface;
import com.kbs.swing.panel.CustomerPanel;
import com.kbs.swing.panel.CustomerPanelGg;
import com.kbs.swing.panel.PrintDialog;
import com.kbs.sys.Oper;
import com.kbs.util.AudioPlay;
import com.kbs.util.ConfigPro;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.SpeakUtil;
import com.kbs.util.TF;
import com.kbs.util.WindowUtil;
import com.sun.awt.AWTUtilities;

import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

public class SalePanel extends JPanel implements CommonInterface{
	private Logger log=Logger.getLogger(SalePanel.class);
	private IDeductionFactory deductionCashFactory=new CashDeductionFactory();
	private IDeductionFactory free_factory=new FreeDeductionFactory();
	private IDeductionFactory zfb_factory=new ZfbDeductionFactory();
	private IDeductionFactory wx_factory=new WxDeductionFactory();
	private IDeductionFactory dlb_factory=new DlbDeductionFactory();
	private IOperationsDeduction cash_deduction=deductionCashFactory.createOperationsDeduction();
	private IOperationsDeduction free_deduction=free_factory.createOperationsDeduction();
	private IOperationsDeduction zfb_deduction=zfb_factory.createOperationsDeduction();
	private IOperationsDeduction wx_deduction=wx_factory.createOperationsDeduction();
	private IOperationsDeduction dlb_deduction=dlb_factory.createOperationsDeduction();
	private MetaDataBO metaDataBO=new MetaDataBO();
	private MainFrame mainFrame;
	private JPanel panel_details;
	private JPanel panel_bottom;
	private boolean isCalcMode=false;
	private JPanel panel_calc;
	private JPanel panel_calc_outer;
	private JPanel panel_goods;
	private Long orderElapsedTime=0L;
	private JButton btn_mode;
	private JButton btn_0;
	private JButton btn_1;
	private JButton btn_2;
	private JButton btn_3;
	private JButton btn_4;
	private JButton btn_5;
	private JButton btn_6;
	private JButton btn_clear;
	private JButton btn_ok;
	private JButton btn_dot;
	private JButton btn_7;
	private JButton btn_8;
	private JButton btn_9;
	private JButton btn_back;
	private JButton btn_print;
	private JButton btn_refresh;
	private JButton btn_pay;
	private JButton btn_12;
	private JButton btn_13;
	private JButton btn_14;
	private JButton btn_10;
	private static String priceValue="0";
	private JTextField txt_price;
	Timer checkisOnSaleTimer;
	Timer displayTimer = null;
	Timer payTimer =null;
	Timer processTimer =null;
	private NumActionListener numListener=new NumActionListener();
	private OpActionListener opListener=new OpActionListener();
	boolean flag;
	private JLabel label_num;
	private JLabel label_sum;
	private JLabel label_status;
	JSONObject newJSON = new JSONObject();
	JSONObject oldCacheJSON=new JSONObject();
	private JLabel label_actual;
	private JLabel label_change;
	private JLabel label_cardMoney;
	private JLabel label_water_1;
	private JLabel label_water_2;
	private JLabel label_water_3;
	private JLabel label_people_total;
	private JLabel label_people_current;
	private Map<String,Long> pageMap=new HashMap();
//	private JSONArray jsonArr;
	private ActionListener addCommodityListener;
	private ActionListener priviousListener;
	private ActionListener nextListener;
	int row=3;	//一行展示的商品数
	int col=4;	//行数
	private JPanel panel_guadan_details;
	private JLabel label_guadan_num;
	private JLabel label_guadan_total;
	private int danshuSize=0;
	private CustomerPanelGg customerPanel;
	private JLabel label_actual_txt;
	private JLabel label_change_txt;
	private String inputText="";
	private SpeakUtil speakUtil=new SpeakUtil();
	private JProgressBar progressBar;
	private JLabel lblTs;
	private JLabel lblMPhone;
	private JLabel lbl_MName;
	private JLabel model_dispaly;
	
	public SalePanel(MainFrame mainFrame){
		this.mainFrame=mainFrame;
		setBackground(new Color(255, 255, 255));
		initGUI();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		if(gs.length>1){
			customerPanel=new CustomerPanelGg();
//			customerFrame.dispose();
			customerPanel.setUndecorated(true);
			AWTUtilities.setWindowOpaque(customerPanel, false); 
			customerPanel.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			customerPanel.setVisible(true);
			Dimension d1=Toolkit.getDefaultToolkit().getScreenSize();
			customerPanel.setBounds(d1.width, 0, 1024, 768);
		}
		pageMap.put("currentPage", (long) 1);
		panel_calc_outer.add(panel_goods);
		changeMode();
		addListeners();
		for(int i=0;i<panel_details.getComponents().length;i++){
			if(panel_details.getComponents()[i] instanceof JPanel){
				JPanel panel=(JPanel) panel_details.getComponents()[i];
				if((i+1)%3!=0){
					panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray));
				}else{
					panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
				}
			}
		}
		for(Component com:panel_bottom.getComponents()){
        	if(com instanceof JButton){
				JButton btn=(JButton) com;
				btn.setForeground(new Color(39,38,54));
				btn.setBorder(BorderFactory.createLineBorder(Color.white, 0));
			}else if(com instanceof JPanel){
        		for(Component com1:((JPanel) com).getComponents()){
        			if(com1 instanceof JButton){
        				JButton btn1=(JButton) com1;
        				btn1.setForeground(new Color(39,38,54));
        				btn1.setBorder(BorderFactory.createLineBorder(Color.white, 0));
        			}else if(com1 instanceof JPanel){
                		for(Component com2:((JPanel) com1).getComponents()){
                			if(com2 instanceof JButton){
                				JButton btn2=(JButton) com2;
                				btn2.setForeground(new Color(39,38,54));
                				btn2.setBorder(BorderFactory.createLineBorder(Color.white, 0));
                			}
                		}
                	}
        		}
        	}
        }
		
		displayTimer=new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					initPage();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		displayTimer.start();
		displayTimer.setRepeats(true);
		payTimer=new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(StringUtils.isEmpty(txt_price.getText())){
					return;
				}
				
				try {
//					System.out.println(TF.commodity_CP_Json.toJSONString());
					//判读是否是商品
					if(TF.commodity_CP_Json.containsKey(txt_price.getText())){
						if(TF.ConsumeJson.getIntValue("stage_type")==3){
							Oper.clearConsumeCache();
						}
						JSONObject commodityJson = TF.commodity_CP_Json.getJSONObject(txt_price.getText());
						String id=commodityJson.getString("CI_ID");
						Float price=Float.parseFloat(commodityJson.getString("CI_PRICE"));
						if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id)==null)
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").put(id,new JSONObject());
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("price", commodityJson.getString("CI_PRICE"));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("member_price", commodityJson.getString("CI_MEMBERPRICE"));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("cgname",commodityJson.getString("CI_NAME"));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("num")+1);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("use_status",0);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("pay_status",0);
						
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+price));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+1);
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
						TF.ConsumeJson.put("stage_type",1);
						TF.ConsumeJson.put("status", "请支付");
						
						priceValue="0";
						txt_price.setText("");
						txt_price.requestFocus();
						AudioPlay.playAudio(ConsumeService.FILE_PLEASESETCARD);
						return;
					}
					System.out.println(txt_price.getText()+"^^^^^^^^^^^");
					System.out.println(TF.ConsumeJson.getIntValue("stage_type"));
					if(txt_price.getText().length()==18 && (txt_price.getText().indexOf("28")==0 || txt_price.getText().indexOf("13")==0)){
						TF.ConsumeJson.put("pay_code", txt_price.getText());
						if(TF.ConsumeJson.getIntValue("stage_type")==3){
							txt_price.setText("");
							return;
						}
						if(TF.ConsumeJson.getFloatValue("total_price")<=0.0){
							txt_price.setText("");
							TF.ConsumeJson.put("status", "无效订单");
							return;
						}
						if(TF.ConsumeJson.getIntValue("stage_type")==1){
							Runnable runnable = new Runnable() {  
						         @Override  
						         public void run() {  
						        	 try {
						        		 	dlb_deduction.deduction(null);
										} catch (Exception e1) {
											e1.printStackTrace();
										}  
						         } 
						     };  
						     new Thread(runnable).start(); 
						}
						
						priceValue="0";
						txt_price.setText("");
						txt_price.requestFocus();
					}else if(txt_price.getText().length()==17 && txt_price.getText().indexOf("18")==0){
						TF.ConsumeJson.put("ic_code", txt_price.getText());
						priceValue="0";
						txt_price.setText("");
						txt_price.requestFocus();
					}else if(txt_price.getText().length() == 10 && metaDataBO.queryForListMap("select * from tf_cardinfo where ic_id='"+txt_price.getText()+"'").size()>0){
						TF.ConsumeJson.put("ic_code", txt_price.getText());
						priceValue="0";
						txt_price.setText("");
						txt_price.requestFocus();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		payTimer.start();
		payTimer.setRepeats(true);
		processTimer=new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(TF.ConsumeJson.getString("stage_type").equals("0")){
					progressBar.setValue(0);
					orderElapsedTime = 0L;
					lblTs.setText("0  ");
					TF.ConsumeJson.put("elapsedTime", 0);
				}
				else if(TF.ConsumeJson.getString("stage_type").equals("3")){
					progressBar.setValue(100);
					orderElapsedTime = 0L;
				}else{
					if(orderElapsedTime==0){
						progressBar.setValue(0);
						orderElapsedTime=System.currentTimeMillis();
					}
					if(progressBar.getValue()<95){
						progressBar.setValue(progressBar.getValue()+1);
					}
					TF.ConsumeJson.put("elapsedTime", (System.currentTimeMillis()-orderElapsedTime));
					lblTs.setText(String.valueOf((float)(System.currentTimeMillis()-orderElapsedTime)/1000)+"  ");
				}
//				progressBar.setValue(50);
				
			}
		});
//		processTimer.start();
//		processTimer.setRepeats(true);
		
		checkisOnSaleTimer=new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					checkIsOnSale();
				} catch (Exception e1) {
					e1.printStackTrace();
					// TODO Auto-generated catch block
					log.error("检测当前是否在营业时间失败");
				}
			}
		});
		checkisOnSaleTimer.start();
		checkisOnSaleTimer.setRepeats(true);
		//RfidThread.isOnSale=true;
	}
	
	public void initGUI(){
		TF.dsJson.put("title","运营");
		try
        {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible",false);
//            UIManager.put("Button.background", new Color(180, 180, 180));
//            UIManager.put("Button.foreground", new Color(255, 255, 255));
//            UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE, 0) );
        }
        catch(Exception e)
        {
        }
		this.setSize(1024, 696);
		setLayout(null);
		JPanel panel_left = new JPanel();
		panel_left.setLayout(null);
		panel_left.setBounds(0, 0, 512, 696);
		panel_left.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		add(panel_left);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(new Color(0, 51, 102));
		panel.setBounds(0, 0, 511, 95);
		panel_left.add(panel);
		panel.setLayout(null);
		
		lblTs = new JLabel("",JLabel.RIGHT);
		lblTs.setFont(new Font("微软雅黑", Font.ITALIC, 12));
		lblTs.setForeground(Color.WHITE);
		lblTs.setBounds(412, 73, 99, 22);
		panel.add(lblTs);
		
		label_status = new JLabel("请放托盘");
		label_status.setBounds(0, 0, 511, 100);
		panel.add(label_status);
		label_status.setBackground(new Color(0, 51, 102));
		label_status.setOpaque(true); 
		label_status.setForeground(new Color(255, 255, 255));
		label_status.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		label_status.setHorizontalAlignment(SwingConstants.CENTER);
		label_status.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		
		JPanel panel_consume = new JPanel();
		panel_consume.setBackground(new Color(0, 153, 102));
		panel_consume.setBounds(0, 99, 511, 115);
//		panel_consume.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		panel_left.add(panel_consume);
		panel_consume.setLayout(null);
		
		model_dispaly = new JLabel("固价模式:9000元");
		model_dispaly.setBackground(new Color(204, 51, 0));
		model_dispaly.setForeground(new Color(255, 255, 51));
		model_dispaly.setFont(new Font("幼圆", Font.BOLD, 16));
		model_dispaly.setBounds(374, 0, 137, 26);
		panel_consume.add(model_dispaly);
		
		label_num = new JLabel("0");
		label_num.setBounds(0, 0, 226, 115);
		label_num.setForeground(Color.WHITE);
		
		label_num.setFont(new Font("微软雅黑 Light", Font.BOLD, 60));
		label_num.setHorizontalAlignment(SwingConstants.CENTER);
		panel_consume.add(label_num);
		
		label_sum = new JLabel("0.0");
		label_sum.setBounds(236, 0, 275, 115);
		label_sum.setForeground(Color.WHITE);
		label_sum.setFont(new Font("微软雅黑 Light", Font.BOLD, 60));
		label_sum.setHorizontalAlignment(SwingConstants.CENTER);
		panel_consume.add(label_sum);
		
		panel_details = new JPanel();
		panel_details.setForeground(Color.DARK_GRAY);
		panel_details.setBackground(Color.WHITE);
		panel_details.setBounds(0, 214, 511, 385);
		panel_details.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(128, 128, 128)));
		panel_left.add(panel_details);
		panel_details.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.GRAY);
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setPreferredSize(new Dimension(511, 50));
		
//		JLabel lblNewLabel_2 = new JLabel("宫保鸡丁",JLabel.CENTER);
//		lblNewLabel_2.setForeground(new Color(0, 0, 51));
//		lblNewLabel_2.setPreferredSize(new Dimension(250,50));
//		lblNewLabel_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//		panel_1.add(lblNewLabel_2);
//		JLabel lblNewLabel_4 = new JLabel("3000.58");
//		lblNewLabel_4.setPreferredSize(new Dimension(80,50));
//		lblNewLabel_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//		panel_1.add(lblNewLabel_4);
//		JLabel lblNewLabel_5 = new JLabel("X");
//		lblNewLabel_5.setPreferredSize(new Dimension(20,50));
//		lblNewLabel_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//		panel_1.add(lblNewLabel_5);
//		JLabel lblNewLabel_3 = new JLabel("3");
//		lblNewLabel_3.setPreferredSize(new Dimension(50,50));
//		lblNewLabel_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//		panel_1.add(lblNewLabel_3);
//		JLabel lblNewLabel_6 = new JLabel("30000.89");
//		lblNewLabel_6.setPreferredSize(new Dimension(511-400,50));
//		lblNewLabel_6.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
//		panel_1.add(lblNewLabel_6);
		JLabel lblNewLabel_2 = new JLabel("《已支付》宫保鸡丁",JLabel.CENTER);
		lblNewLabel_2.setBounds(0, 0, 210, 50);
		lblNewLabel_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showConfirmDialog(SalePanel.this,"设置成功","提示",JOptionPane.DEFAULT_OPTION);
			}
		});
		panel_1.setLayout(null);
		lblNewLabel_2.setForeground(new Color(0, 153, 102));
		lblNewLabel_2.setPreferredSize(new Dimension(210,50));
		lblNewLabel_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		panel_1.add(lblNewLabel_2);
		JLabel lblNewLabel_4 = new JLabel("3000.58");
		lblNewLabel_4.setForeground(new Color(0, 153, 51));
		lblNewLabel_4.setBounds(250, 0, 80, 50);
		lblNewLabel_4.setPreferredSize(new Dimension(80,50));
		lblNewLabel_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		panel_1.add(lblNewLabel_4);
		JLabel lblNewLabel_5 = new JLabel("X");
		lblNewLabel_5.setForeground(new Color(0, 153, 51));
		lblNewLabel_5.setBounds(330, 0, 20, 50);
		lblNewLabel_5.setPreferredSize(new Dimension(20,50));
		lblNewLabel_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		panel_1.add(lblNewLabel_5);
		JLabel lblNewLabel_3 = new JLabel("38");
		lblNewLabel_3.setForeground(new Color(0, 153, 102));
		lblNewLabel_3.setBounds(350, 0, 25, 50);
		lblNewLabel_3.setPreferredSize(new Dimension(25,50));
		lblNewLabel_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		panel_1.add(lblNewLabel_3);
		JLabel lblNewLabel_6 = new JLabel("30000.89",JLabel.CENTER);
		lblNewLabel_6.setForeground(new Color(0, 153, 102));
		lblNewLabel_6.setBounds(375, 0, 135, 50);
		lblNewLabel_6.setPreferredSize(new Dimension(135,50));
		lblNewLabel_6.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
		panel_1.add(lblNewLabel_6);
		panel_details.add(panel_1);
		JPanel panel_wallet = new JPanel();
		panel_wallet.setBackground(new Color(255, 255, 255));
		panel_wallet.setBounds(0, 601, 511, 95);
		panel_left.add(panel_wallet);
		panel_wallet.setLayout(null);
		
		label_cardMoney = new JLabel("999999");
		label_cardMoney.setHorizontalAlignment(SwingConstants.CENTER);
		label_cardMoney.setForeground(new Color(0, 153, 102));
		label_cardMoney.setFont(new Font("微软雅黑 Light", Font.PLAIN, 60));
		label_cardMoney.setBounds(138, 0, 372, 98);
		panel_wallet.add(label_cardMoney);
		
		label_actual_txt = new JLabel("实收:");
		label_actual_txt.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		label_actual_txt.setBounds(0, 0, 56, 50);
		panel_wallet.add(label_actual_txt);
		
		label_actual = new JLabel("0.0");
		label_actual.setHorizontalAlignment(SwingConstants.CENTER);
		label_actual.setForeground(new Color(14, 147, 46));
		label_actual.setFont(new Font("微软雅黑 Light", Font.PLAIN, 60));
		label_actual.setBounds(0, 0, 255, 95);
		panel_wallet.add(label_actual);
		
		label_change_txt = new JLabel("找零:");
		label_change_txt.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		label_change_txt.setBounds(255, 0, 56, 50);
		panel_wallet.add(label_change_txt);
		
		label_change = new JLabel("0.0");
		label_change.setHorizontalAlignment(SwingConstants.CENTER);
		label_change.setForeground(new Color(14, 147, 46));
		label_change.setFont(new Font("微软雅黑 Light", Font.PLAIN, 60));
		label_change.setBounds(255, 0, 255, 98);
		panel_wallet.add(label_change);
		
		lbl_MName = new JLabel("马克思(YS-001)");
		lbl_MName.setFont(new Font("微软雅黑 Light", Font.BOLD, 16));
		lbl_MName.setBounds(0, 0, 510, 33);
		panel_wallet.add(lbl_MName);
		
		lblMPhone = new JLabel("13600148611");
		lblMPhone.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		lblMPhone.setBounds(0, 34, 128, 27);
		panel_wallet.add(lblMPhone);
		
		progressBar = new JProgressBar();
		progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressBar.setFont(new Font("宋体", Font.PLAIN, 5));
		progressBar.setForeground(new Color(255, 0, 51));
		progressBar.setBackground(new Color(0,51,102));
		progressBar.setBounds(0, 95, 511, 4);
		panel_left.add(progressBar);
		
		JPanel panel_right = new JPanel();
		panel_right.setBounds(512, 0, 512, 696);
		add(panel_right);
		panel_right.setLayout(null);
		
		JPanel panel_zonghe = new JPanel();
		panel_zonghe.setBounds(0, 0, 512, 155);
		panel_right.add(panel_zonghe);
		panel_zonghe.setLayout(null);
		
		JPanel panel_zonghe_top = new JPanel();
		panel_zonghe_top.setBounds(0, 0, 512, 100);
//		panel_zonghe_top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		panel_zonghe.add(panel_zonghe_top);
		panel_zonghe_top.setLayout(new GridLayout(1, 3, 0, 0));
		
		JPanel panel_guadan = new JPanel();
		panel_guadan.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray));
		panel_zonghe_top.add(panel_guadan);
		panel_guadan.setLayout(null);
		
		label_guadan_num = new JLabel("0");
		label_guadan_num.setForeground(Color.DARK_GRAY);
		label_guadan_num.setVerticalAlignment(SwingConstants.TOP);
		label_guadan_num.setFont(new Font("微软雅黑 Light", Font.ITALIC, 20));
		label_guadan_num.setHorizontalAlignment(SwingConstants.CENTER);
		label_guadan_num.setBounds(29, 0, 141, 26);
		panel_guadan.add(label_guadan_num);
		
		panel_guadan_details = new JPanel();
		
		panel_guadan_details.setBounds(0, 26, 169, 74);
		panel_guadan.add(panel_guadan_details);
		panel_guadan_details.setLayout(new BorderLayout(0, 0));
		
		label_guadan_total = new JLabel("0");
		label_guadan_total.setForeground(Color.DARK_GRAY);
		label_guadan_total.setFont(new Font("微软雅黑 Light", Font.ITALIC, 35));
		label_guadan_total.setHorizontalAlignment(SwingConstants.CENTER);
		panel_guadan_details.add(label_guadan_total);
		
		JLabel lblNewLabel = new JLabel("挂单");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
		lblNewLabel.setBounds(0, 0, 70, 28);
		panel_guadan.add(lblNewLabel);
		
		JPanel panel_people = new JPanel();
		panel_people.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray));
		panel_people.setLayout(null);
		panel_zonghe_top.add(panel_people);
		
		label_people_total = new JLabel("0");
		label_people_total.setForeground(Color.DARK_GRAY);
		label_people_total.setVerticalAlignment(SwingConstants.TOP);
		label_people_total.setHorizontalAlignment(SwingConstants.CENTER);
		label_people_total.setFont(new Font("微软雅黑 Light", Font.ITALIC, 20));
		label_people_total.setBounds(80, 0, 90, 40);
		panel_people.add(label_people_total);
		
		JLabel label_4 = new JLabel("人数");
		label_4.setVerticalAlignment(SwingConstants.TOP);
		label_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
		label_4.setBounds(0, 0, 70, 40);
		panel_people.add(label_4);
		
		label_people_current = new JLabel("0");
		label_people_current.setForeground(Color.DARK_GRAY);
		label_people_current.setHorizontalAlignment(SwingConstants.CENTER);
		label_people_current.setFont(new Font("微软雅黑 Light", Font.ITALIC, 35));
		label_people_current.setBounds(0, 26, 170, 74);
		panel_people.add(label_people_current);
		
		JPanel panel_water = new JPanel();
		panel_water.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.gray));
		panel_water.setLayout(null);
		panel_zonghe_top.add(panel_water);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBounds(0, 25, 170, 74);
		panel_water.add(panel_12);
		panel_12.setLayout(new GridLayout(3, 1, 0, 0));
		
		label_water_1 = new JLabel("0         0");
		label_water_1.setForeground(Color.DARK_GRAY);
		label_water_1.setVerticalAlignment(SwingConstants.TOP);
		label_water_1.setFont(new Font("微软雅黑 Light", Font.ITALIC, 16));
		label_water_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_12.add(label_water_1);
		
		label_water_2 = new JLabel("0         0");
		label_water_2.setForeground(Color.DARK_GRAY);
		label_water_2.setVerticalAlignment(SwingConstants.TOP);
		label_water_2.setFont(new Font("微软雅黑 Light", Font.ITALIC, 16));
		label_water_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_12.add(label_water_2);
		
		label_water_3 = new JLabel("0         0");
		label_water_3.setForeground(Color.DARK_GRAY);
		label_water_3.setVerticalAlignment(SwingConstants.TOP);
		label_water_3.setFont(new Font("微软雅黑 Light", Font.ITALIC, 16));
		label_water_3.setHorizontalAlignment(SwingConstants.CENTER);
		panel_12.add(label_water_3);
		
		JLabel label_9 = new JLabel("流水");
		label_9.setVerticalAlignment(SwingConstants.TOP);
		label_9.setFont(new Font("微软雅黑 Light", Font.PLAIN, 15));
		label_9.setBounds(0, 0, 70, 40);
		panel_water.add(label_9);
		
		JPanel panel_guadan_btns = new JPanel();
		panel_guadan_btns.setBounds(0, 100, 512, 55);
		panel_guadan_btns.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		panel_zonghe.add(panel_guadan_btns);
		panel_guadan_btns.setLayout(new GridLayout(1, 3, 0, 0));
		
		JButton btn_guadan = new JButton("挂单");
		btn_guadan.setEnabled(false);
		btn_guadan.setForeground(Color.DARK_GRAY);
		btn_guadan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TF.gdJson.put("gd_status", 1);
				guadan();
			}
		});
		btn_guadan.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		ImageIcon ico_guadan=new ImageIcon(GetProjectRealPath.getPath("images/guadan.png"));
		btn_guadan.setIcon(ico_guadan);
//		btn_guadan.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		panel_guadan_btns.add(btn_guadan);
		
		JButton btn_bingdan = new JButton("并单");
		btn_bingdan.setEnabled(false);
		btn_bingdan.setForeground(Color.DARK_GRAY);
		btn_bingdan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TF.ConsumeJson.put("gd_status", 1);
				guadan();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				TF.ConsumeJson = JSONObject.parseObject(TF.gdJson.toJSONString());
				TF.gdJson =JSONObject.parseObject(TF.defaultJSON.toJSONString());
				if(TF.ConsumeJson.getFloatValue("total_price")>0){
					TF.ConsumeJson.put("stage_type", "1");
				}
			}
		});
		btn_bingdan.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		btn_bingdan.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/bingdan.png")));
//		btn_bingdan.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		panel_guadan_btns.add(btn_bingdan);
		
		JButton btn_cancleOrder = new JButton("消单");
		btn_cancleOrder.setEnabled(false);
		btn_cancleOrder.setForeground(Color.DARK_GRAY);
		btn_cancleOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TF.gdJson =JSONObject.parseObject(TF.defaultJSON.toJSONString());
			}
		});
		btn_cancleOrder.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
		btn_cancleOrder.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cancle.png")));
		panel_guadan_btns.add(btn_cancleOrder);
		
		panel_bottom = new JPanel();
		panel_bottom.setBounds(0, 155, 512, 541);
		panel_right.add(panel_bottom);
		panel_bottom.setLayout(null);
		
		panel_calc_outer = new JPanel();
		panel_calc_outer.setBounds(0, 0, 512, 448);
		panel_calc_outer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		panel_bottom.add(panel_calc_outer);
		panel_calc_outer.setLayout(null);
		
		JPanel panel_txt = new JPanel();
		panel_txt.setBounds(0, 0, 512, 60);
		panel_txt.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
		panel_calc_outer.add(panel_txt);
		panel_txt.setLayout(null);
		
		txt_price = new JTextField("");
//		txt_price.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if(TF.ConsumeJson.getIntValue("stage_type")==2){
//					txt_price.setText("");
//					return;
//				}
//				if(OperationsWxDeduction.isConsume){
//					txt_price.setText("");
//					TF.ConsumeJson.put("status", "正在支付中");
//					return;
//				}
//				if(TF.ConsumeJson.getFloatValue("total_price")<=0.0){
//					txt_price.setText("");
//					TF.ConsumeJson.put("status", "无效订单");
//					return;
//				}
////				if(TF.wxConfig==null){
////					txt_price.setText("");
////					TF.ConsumeJson.put("status", "请先配置微信支付");
////					return ;
////				}
//				
//				if(TF.ConsumeJson.getIntValue("stage_type")==0){
//					TF.ConsumeJson.put("status", "支付失败");
//					return;
//				}
//				if(priceValue.equals("")||(!priceValue.equals("0"))&&(Float.parseFloat(priceValue)-TF.ConsumeJson.getFloatValue("total_price")<0)){
//					TF.ConsumeJson.put("status", "金额不足");
//					return;
//				}
//				
////				TF.cardMoney=Float.parseFloat(priceValue);
////				float money=Float.parseFloat(priceValue);
////				if(!(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("card").getString("balance").equals(""))&&TF.ConsumeJson.getJSONObject("deduction").getJSONObject("card").getFloatValue("balance")>0)
////					return;
////				if(Float.parseFloat(priceValue)==0.0){
////					TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("real_price", TF.ConsumeJson.getFloatValue("total_price"));
////				}else{
////					TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("real_price", money);
////					TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("dispenser", money-TF.ConsumeJson.getFloatValue("total_price"));
////				}
//				TF.ConsumeJson.put("pay_code", txt_price.getText());
//				if(TF.ConsumeJson.getString("pay_code").length()==18){
//					if(TF.ConsumeJson.getString("pay_code").indexOf("13")==0){
//						flag=false;
//						Runnable runnable = new Runnable() {  
//					         @Override  
//					         public void run() {  
//					        	 try {
//					        		 	flag=wx_deduction.deduction(null);
//									} catch (Exception e1) {
//										e1.printStackTrace();
//									}  
//					         } 
//					     };  
//					     new Thread(runnable).start(); 
//					}else if(TF.ConsumeJson.getString("pay_code").indexOf("28")==0){
//						flag=false;
//						Runnable runnable = new Runnable() {  
//					         @Override  
//					         public void run() {  
//					        	 try {
//					        		 	flag=zfb_deduction.deduction(null);
//									} catch (Exception e1) {
//										e1.printStackTrace();
//									}  
//					         } 
//					     };  
//					     new Thread(runnable).start(); 
//					}
//				}
////				if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")>0){
//				
////					if(flag){
////						LoadTFCache.clearConsumeCache();
////					}
////				}
////				    try {
////						Thread.sleep(1500);
////					} catch (InterruptedException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
//					priceValue="0";
//					txt_price.setText("");
//					txt_price.requestFocus();
//			}
//		});
//		txt_price.getDocument().addDocumentListener(new DocumentListener() {
//			
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				priceValue=txt_price.getText();
//			}
//			
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				priceValue=txt_price.getText();
//			}
//			
//			@Override
//			public void changedUpdate(DocumentEvent e) {
//				// TODO Auto-generated method stub
//				priceValue=txt_price.getText();
//			}
//		});
//		txt_price.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				// TODO Auto-generated method stub
//				int code = e.getKeyCode();
//				if((code>=KeyEvent.VK_0&&code<=KeyEvent.VK_9)||(code>=KeyEvent.VK_NUMPAD0&&code<=KeyEvent.VK_NUMPAD9)||(code==KeyEvent.VK_PERIOD)){
//					System.out.println("输入了:"+e.getKeyChar());
//					if(inputText.equals("")){
//						if(e.getKeyChar()==KeyEvent.VK_PERIOD){
//							inputText="0.";
////							txt_price.setText("0.");
//						}else{
////							txt_price.setText(e.getKeyChar()+"");
//							inputText=e.getKeyChar()+"";
//						}
//					}else{
//						inputText=inputText+e.getKeyChar();
//					}
//					txt_price.setText(inputText);
//					priceValue=txt_price.getText();
//				 }
//			}
//			
//		});
		txt_price.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		txt_price.setHorizontalAlignment(SwingConstants.RIGHT);
		txt_price.setBounds(0, 0, 409, 60);
		panel_txt.add(txt_price);
		
		btn_mode = new JButton("");
		btn_mode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeMode();
			}
		});
		ImageIcon ico_mode=new ImageIcon(GetProjectRealPath.getPath("images/goods.png"));
		ico_mode.setImage(ico_mode.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		btn_mode.setIcon(ico_mode);
		btn_mode.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		btn_mode.setBounds(410, 0, 102, 60);
		panel_txt.add(btn_mode);
		
		panel_calc = new JPanel();
		panel_calc.setBounds(0, 60, 410, 388);
//		panel_calc_outer.add(panel_calc);
		panel_calc.setLayout(null);
		
		btn_7 = new JButton("7");
		btn_7.setBorder(BorderFactory.createLineBorder(Color.white, 0));
		btn_7.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_7.setBounds(1, 0, 102, 97);
		panel_calc.add(btn_7);
		
		btn_8 = new JButton("8");
		btn_8.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_8.setBounds(103, 0, 102, 97);
		panel_calc.add(btn_8);
		
		btn_9 = new JButton("9");
		btn_9.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_9.setBounds(205, 0, 102, 97);
		panel_calc.add(btn_9);
		
		btn_back = new JButton("←");
		btn_back.setActionCommand("back");
		btn_back.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_back.setBounds(307, 0, 102, 97);
		panel_calc.add(btn_back);
		
		btn_4 = new JButton("4");
		btn_4.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_4.setBounds(1, 97, 102, 97);
		panel_calc.add(btn_4);
		
		btn_5 = new JButton("5");
		btn_5.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_5.setBounds(103, 97, 102, 97);
		panel_calc.add(btn_5);
		
		btn_6 = new JButton("6");
		btn_6.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_6.setBounds(205, 97, 102, 97);
		panel_calc.add(btn_6);
		
		btn_clear = new JButton("C");
		btn_clear.setActionCommand("c");
		btn_clear.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_clear.setBounds(307, 97, 102, 97);
		panel_calc.add(btn_clear);
		
		btn_1 = new JButton("1");
		btn_1.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_1.setBounds(1, 193, 102, 97);
		panel_calc.add(btn_1);
		
		btn_2 = new JButton("2");
		btn_2.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_2.setBounds(103, 193, 102, 97);
		panel_calc.add(btn_2);
		
		btn_3 = new JButton("3");
		btn_3.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_3.setBounds(205, 193, 102, 97);
		panel_calc.add(btn_3);
		
		btn_ok = new JButton("");
		btn_ok.setActionCommand("ok");
		btn_ok.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/enter.png")));
		btn_ok.setFont(new Font("微软雅黑", Font.PLAIN, 45));
		btn_ok.setBounds(307, 193, 102, 194);
		panel_calc.add(btn_ok);
		
		btn_0 = new JButton("0");
		btn_0.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_0.setBounds(1, 290, 204, 97);
		panel_calc.add(btn_0);
		
		btn_dot = new JButton(".");
		btn_dot.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		btn_dot.setBounds(205, 290, 102, 97);
		panel_calc.add(btn_dot);
		
		btn_10 = new JButton("固 价");
		btn_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//				priceValue="10";
//				txt_price.setText("10.0");
//				payOrderIsClicked();
				int n = JOptionPane.showConfirmDialog(null, "是否进行固定价格设置？(金额：0为取消固定价格模式)", "标题",JOptionPane.YES_NO_OPTION);
				if(n==1){
					return;
				}
				if(txt_price.getText().equals("")){
					JOptionPane.showConfirmDialog(SalePanel.this,"无效价格","提示",JOptionPane.DEFAULT_OPTION);
					return;
				}
				if(TF.dsJson.getDoubleValue("fixed_price")==0){
					Properties  props = ConfigPro.getInstance().getProperties();
					props.setProperty("fixed_price", txt_price.getText());
					ConfigPro.getInstance().store();
					TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
					JOptionPane.showConfirmDialog(SalePanel.this,"设置成功","提示",JOptionPane.DEFAULT_OPTION);
		    		FixedValuation fixedValuation = new FixedValuation();
		    		Thread fixThread = new Thread(fixedValuation);
		    		fixThread.start();
		    	}else{
				Properties  props = ConfigPro.getInstance().getProperties();
				props.setProperty("fixed_price", txt_price.getText());
				ConfigPro.getInstance().store();
				TF.dsJson=(JSONObject)JSONObject.toJSON(ConfigPro.getInstance().getProperties());
				JOptionPane.showConfirmDialog(SalePanel.this,"设置成功","提示",JOptionPane.DEFAULT_OPTION);
		    	}
			}
		});
//		btn_10.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cash.png")));
		btn_10.setForeground(new Color(0, 0, 51));
		btn_10.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_10.setBounds(410, 60, 102, 97);
		panel_calc_outer.add(btn_10);
		
		btn_14 = new JButton("20 ");
		btn_14.setEnabled(false);
		btn_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceValue="20";
				txt_price.setText("20.0");
				payOrderIsClicked();
			}
		});
		btn_14.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cash.png")));
		btn_14.setForeground(new Color(0, 0, 51));
		btn_14.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_14.setBounds(410, 157, 102, 97);
		panel_calc_outer.add(btn_14);
		
		btn_13 = new JButton("50 ");
		btn_13.setEnabled(false);
		btn_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceValue="50";
				txt_price.setText("50.0");
				payOrderIsClicked();
			}
		});
		btn_13.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cash.png")));
		btn_13.setForeground(new Color(0, 0, 51));
		btn_13.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_13.setBounds(410, 253, 102, 97);
		panel_calc_outer.add(btn_13);
		
		btn_12 = new JButton("100");
		btn_12.setEnabled(false);
		btn_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				priceValue="100";
				txt_price.setText("100.0");
				payOrderIsClicked();
			}
		});
		btn_12.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cash.png")));
		btn_12.setForeground(new Color(0, 0, 51));
		btn_12.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_12.setBounds(410, 350, 102, 97);
		panel_calc_outer.add(btn_12);
		
		panel_goods = new JPanel();
		panel_goods.setBounds(0, 60, 411, 387);
//		panel_calc_outer.add(panel_goods);
		panel_goods.setLayout(new GridLayout(4, 3, 0, 0));
		
		JButton panel_goods1 = new JButton();
		panel_goods.add(panel_goods1);
		panel_goods1.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods2 = new JButton();
		panel_goods.add(panel_goods2);
		panel_goods2.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods3 = new JButton();
		panel_goods.add(panel_goods3);
		panel_goods3.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods4 = new JButton();
		panel_goods.add(panel_goods4);
		panel_goods4.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods5 = new JButton();
		panel_goods.add(panel_goods5);
		panel_goods5.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods6 = new JButton();
		panel_goods.add(panel_goods6);
		panel_goods6.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods7 = new JButton();
		panel_goods.add(panel_goods7);
		panel_goods7.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods8 = new JButton();
		panel_goods.add(panel_goods8);
		panel_goods8.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods9 = new JButton();
		panel_goods.add(panel_goods9);
		panel_goods9.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods10 = new JButton();
		panel_goods.add(panel_goods10);
		panel_goods10.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods11 = new JButton();
		panel_goods.add(panel_goods11);
		panel_goods11.setLayout(new BorderLayout(0, 0));
		
		JButton panel_goods12 = new JButton();
		panel_goods.add(panel_goods12);
		panel_goods12.setLayout(new BorderLayout(0, 0));

		btn_refresh = new JButton("");
		btn_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		btn_refresh.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/refresh.png")));
		btn_refresh.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_refresh.setBounds(134, 446, 277, 93);
		panel_bottom.add(btn_refresh);
		
		btn_pay = new JButton("");
		btn_pay.setEnabled(false);
		btn_pay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				payOrderIsClicked();
			}
		});
		btn_pay.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/money_big.png")));
		btn_pay.setForeground(new Color(0, 0, 51));	
		btn_pay.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		btn_pay.setBounds(410, 446, 101, 93);
		panel_bottom.add(btn_pay);
		
		btn_print = new JButton("");
		btn_print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintDialog dialog=new PrintDialog(new JFrame(),"小票打印",true);
				dialog.setVisible(false);
				dialog.setUndecorated(true);
				
				AWTUtilities.setWindowOpaque(dialog, false); 
				dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
				
				WindowUtil.centerWindow(dialog);
				dialog.setVisible(true);
			}
		});
		btn_print.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/print.png")));
		btn_print.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		btn_print.setBounds(0, 446, 135, 93);
		panel_bottom.add(btn_print);
	}
	
	public void changeMode(){
		isCalcMode=!isCalcMode;
//		panel_calc_outer.setVisible(false);
		ImageIcon ico_mode;
		if(isCalcMode){
			ico_mode=new ImageIcon(GetProjectRealPath.getPath("images/keyboard.png"));
			panel_calc_outer.add(panel_calc);
			panel_calc_outer.remove(panel_goods);
		}else{
//			jsonArr=TF.commodityJson.getJSONArray("data");
			ico_mode=new ImageIcon(GetProjectRealPath.getPath("images/goods.png"));
			panel_calc_outer.add(panel_goods);
			panel_calc_outer.remove(panel_calc);
			pageMap.put("currentPage", (long) 1);
			showCommodity();
		}
		invalidate();
		btn_mode.setIcon(ico_mode);
		panel_calc_outer.repaint();
		validate();
	}
	
	
	/**
	 * 为计算器添加监听器
	 */
	public void addNumListeners(ActionListener listener){
		btn_0.addActionListener(listener);
		btn_1.addActionListener(listener);
		btn_2.addActionListener(listener);
		btn_3.addActionListener(listener);
		btn_4.addActionListener(listener);
		btn_5.addActionListener(listener);
		btn_6.addActionListener(listener);
		btn_7.addActionListener(listener);
		btn_8.addActionListener(listener);
		btn_9.addActionListener(listener);
		
	}
	
	/**
	 * 为计算器移除监听器
	 */
	public void removeNumListeners(ActionListener listener){
		btn_0.removeActionListener(listener);
		btn_1.removeActionListener(listener);
		btn_2.removeActionListener(listener);
		btn_3.removeActionListener(listener);
		btn_4.removeActionListener(listener);
		btn_5.removeActionListener(listener);
		btn_6.removeActionListener(listener);
		btn_7.removeActionListener(listener);
		btn_8.removeActionListener(listener);
		btn_9.removeActionListener(listener);
	}
	
	/**
	 * 自定义计算器数字键监听器
	 * @author Administrator
	 *
	 */
	private class NumActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String temppriceValue =priceValue;
			if(priceValue.equals("0")){
				priceValue=e.getActionCommand();
			}else{
				temppriceValue+=e.getActionCommand();
				
				if(temppriceValue.split(".").length>2 || (temppriceValue.indexOf(".")>0&&temppriceValue.substring(temppriceValue.indexOf(".")+1,temppriceValue.length()).length()>1)){
					TF.ConsumeJson.put("status","输入格式不正确");
					priceValue="0";
					return;
				}
				
//				if(!temppriceValue.equals("")&&Float.parseFloat(temppriceValue)>999.0){
//					TF.ConsumeJson.put("status","金额额度过大");
//					return;
//				}
				
				priceValue+=e.getActionCommand();
			}
			txt_price.setText(priceValue);
		}
		
	}
	
	/**
	 * 自定义计算器中除去数字以外的监听器
	 * @author Administrator
	 *
	 */
	private class OpActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand().equals(".")){
				if(priceValue.equals("0")){
					priceValue="0.";
				}else{
					if(priceValue.indexOf(".")!=-1){
						return;
					}
					priceValue=priceValue+".";
				}
			}else if(e.getActionCommand().equals("+")){
//				addIsClicked();
			}else if(e.getActionCommand().equals("ok")){
				okIsClicked();
				
			}else if(e.getActionCommand().equals("c")){
				priceValue="0";
				txt_price.setText("");
				txt_price.requestFocus();
			}else if(e.getActionCommand().equals("back")){
				System.out.println(priceValue);
				if(priceValue.equals("0")){
					return;
				}else{
					if(priceValue.indexOf(".")>0){
//						if(priceValue.length()!=priceValue.indexOf(".")+1 || Integer.parseInt(priceValue.substring(priceValue.indexOf(".")+1,priceValue.length()))==0){
//							priceValue = "0";
//						}else{
						priceValue = priceValue.substring(0, priceValue.indexOf("."));
//						}
					}else{
						priceValue = priceValue.substring(0, priceValue.length()-1);
					}
				}
				if(priceValue.equals("")){
					priceValue ="0";
				}
				txt_price.setText(DataConvertUtil.moneryFormat(Double.parseDouble(priceValue)));
			}
		}
		
	}
	
	
	public void okIsClicked(){
		if(priceValue.length()>1&&priceValue.charAt(priceValue.length()-1)=='.'){
			priceValue=priceValue.substring(0, priceValue.length()-1);
		}
		if(priceValue.equals("")||priceValue.equals("0")||Float.parseFloat(priceValue)==0.0||(priceValue.indexOf(".")==priceValue.length()-1)){
			return;
		}
		if(TF.ConsumeJson.getIntValue("stage_type")==3){
			Oper.clearConsumeCache();
		}
		if(TF.ConsumeJson.getIntValue("stage_type")==2){
			return;
		}
		String time=String.valueOf(System.currentTimeMillis());
		JSONObject jsonPrice=new JSONObject();
		jsonPrice.put("price", Float.parseFloat(priceValue));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(time,jsonPrice);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("use_status",0);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(time).put("pay_status",0);
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("price", new java.text.DecimalFormat("0.0").format(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price")+Float.parseFloat(priceValue)));
		TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+1);
//		TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
		if(TF.dsJson.getIntValue("discount")!=100){
			TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
			}else{
				TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
			}
		TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
		TF.ConsumeJson.put("stage_type",1);
		TF.ConsumeJson.put("status", "请支付");
//		if(TF.ConsumeJson.getFloatValue("total_price")<20){
//			AudioPlay.playAudio(ConsumeService.FILE_PLEASESETCARD);
//		}else{
			String price=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
			AudioPlay.speak("请付"+price+"元");
//			speakUtil.speak("请付"+price+"元");
//		}
		priceValue="0";
		txt_price.setText("");
	}
	
//	public void guazhangIsClicked(){
//		if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").size()>0){
//			for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
//				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key).put("gd_status", 1);
//			}
//		}
//		if(TF.ConsumeJson.getFloatValue("total_price")==0){
//			return;
//		}
//		if(TF.ConsumeJson.getIntValue("stage_type")==2){
//			return;
//		}
//		TF.consumeList.add(TF.consumeList.size(),JSONObject.parseObject(TF.ConsumeJson.toJSONString()));
//		TF.gdJson =JSONObject.parseObject(TF.defaultJSON.toJSONString());
//		for(JSONObject consumeJson:TF.consumeList){
//			TF.gdJson.put("total_price", TF.gdJson.getFloatValue("total_price")+consumeJson.getFloatValue("total_price"));
//			TF.gdJson.put("total_num", TF.gdJson.getIntValue("total_num")+consumeJson.getIntValue("total_num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("auto").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getFloatValue("price")+consumeJson.getJSONObject("valuation").getJSONObject("auto").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("auto").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price")+consumeJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+consumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num"));
//			for(String key:consumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
//				TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").put(key,consumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key));
//			}
//			for(String key:consumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
//				TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(key,consumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key));
//			}
//			for(String key:consumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
//				if(TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").containsKey(key)){
//					TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).put("num",TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num"));
//				}else{
//					TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").put(key,consumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key));
//				}
////				TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).put("num",TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num"));
//			}
//		}
//		LoadTFCache.clearConsumeCache();
//	}
//	
//	public void bingdanIsClicked(){
//		if(TF.consumeList.size()==0){
//			return;
//		}
//		if(TF.ConsumeJson.getIntValue("total_num")>0 && TF.ConsumeJson.getIntValue("stage_type")!=2){
//			guazhangIsClicked();
//			TF.gdJson.put("total_price", DataConvertUtil.moneryFormat(TF.gdJson.getFloatValue("total_price")+TF.ConsumeJson.getFloatValue("total_price")));
//			TF.gdJson.put("total_num", TF.gdJson.getIntValue("total_num")+TF.ConsumeJson.getIntValue("total_num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("auto").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("auto").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getIntValue("num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getIntValue("num"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").put("price", TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price"));
//			TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").put("num", TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num"));
//			for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
//				TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key));
//			}
//			for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
//				TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key));
//			}
//			for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
//				if(TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").containsKey(key)){
//					TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).put("num",TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num"));
//				}else{
//					TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key));
//				}
////				TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).put("num",TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num")+consumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num"));
//			}
//		}
//		TF.ConsumeJson = JSONObject.parseObject(TF.gdJson.toJSONString());
//		TF.gdJson=JSONObject.parseObject(TF.defaultJSON.toJSONString());
//		if(TF.ConsumeJson.getIntValue("total_num")>0){
//			if(TF.ConsumeJson.getIntValue("total_num")<=5||TF.ConsumeJson.getFloatValue("total_price")<=20){
//				AudioPlay.playAudio(ConsumeService.FILE_PLEASESETCARD);
//			}else{
//				String price=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
//				AudioPlay.speak("请付"+price+"元");
////				speakUtil.speak("请付"+price+"元");
//			}
//			TF.ConsumeJson.put("gd_status", 1);
//			TF.ConsumeJson.put("stage_type", 1);
//			TF.ConsumeJson.put("status", "请支付");
//		}
//		TF.consumeList=new ArrayList();
//	}
//	
//	public void cancleGuazhang(){
//		TF.consumeList=new ArrayList();
//		TF.gdJson = JSONObject.parseObject(TF.defaultJSON.toJSONString());
//	}
	public synchronized void payOrderIsClicked(){
		if(TF.ConsumeJson.getIntValue("stage_type")==3){
			return;
		}
		if(TF.ConsumeJson.getIntValue("stage_type")==0){
			TF.ConsumeJson.put("status", "支付失败");
			return;
		}
		
		
		float money=Float.parseFloat(priceValue);
		if(Float.parseFloat(priceValue)==0.0){
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("real_price", TF.ConsumeJson.getFloatValue("total_price"));
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("dispenser", "0.0");
		}else{
			if(money < TF.ConsumeJson.getFloatValue("total_price")){
				TF.ConsumeJson.put("status", "金额不足");
				return;
			}
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("real_price", money);
			TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").put("dispenser", money-TF.ConsumeJson.getFloatValue("total_price"));
		}
		if(TF.ConsumeJson.getIntValue("stage_type")==1){
			flag=false;
			Runnable runnable = new Runnable() {  
		         @Override  
		         public void run() {  
		        	 try {
		        		 	flag=cash_deduction.deduction(null);
						} catch (Exception e1) {
							e1.printStackTrace();
						}  
		         } 
		     };  
		     new Thread(runnable).start(); 
//			if(flag){
//				LoadTFCache.clearConsumeCache();
//			}
		}
			priceValue="0";
			txt_price.setText("");
	}
	
	public void refresh(){
		if(TF.ConsumeJson.getIntValue("stage_type")==2){
			TF.ConsumeJson.put("status", "订单正在支付中.....");
		}else{
		Oper.clearConsumeCache();
		priceValue="0";
		OperationsWxDeduction.isConsume=false;
		txt_price.setText("");
		txt_price.requestFocus();
		}
	}
	
	public void checkIsOnSale() {
		// TODO Auto-generated method stub
		List<Map<String,Object>> mealList=TF.mealList;
		boolean b=checkTimeisOnSale(mealList);
		if(!b){
			TF.preConsumeJson=new JSONObject();
			TF.currentMealTimes="0";
			checkisOnSaleTimer.stop();
			displayTimer.stop();
//			if(customerFrame!=null){
//				customerFrame.displayTimer.stop();
//				customerFrame.dispose();
//			}
			//RfidThread.isOnSale=false;
			mainFrame.setVisible(true);
			//SaleFrame.this.dispose();
			mainFrame.back(0);
//			}
		}
	}
	public boolean checkTimeisOnSale(List<Map<String,Object>> mealList){
		for(String key:TF.preConsumeJson.keySet()){
			if(TF.dsJson.getFloatValue("clearPayedBowl")!=0&&(System.currentTimeMillis()-TF.preConsumeJson.getLongValue(key))>(TF.dsJson.getFloatValue("clearPayedBowl")*60*1000)){
				TF.preConsumeJson.remove(key);
			}
		}
//		System.out.println("----mszi"+mealList.size());
		for(Map<String,Object> m:mealList){
			String time=new SimpleDateFormat("HHmm").format(new Date());
			String timeBegin=m.get("STARTTIME")+"";
			String timeEnd=m.get("ENDTIME")+"";
			if(timeBegin.split(":").length==3){
				timeBegin=timeBegin.substring(0, timeBegin.lastIndexOf(":"));
			}
			timeBegin=timeBegin.replace("-", "").replace(":", "");
			if(timeEnd.split(":").length==3){
				timeEnd=timeEnd.substring(0, timeEnd.lastIndexOf(":"));
			}
			timeEnd=timeEnd.replace("-", "").replace(":", "");
			if(Integer.parseInt(timeBegin)>Integer.parseInt(timeEnd)){
				timeEnd+=2400;
			}
//			String timeEnd=(m.get("ENDTIME")+"").replace("-", "").replace(":", "");
			if(Integer.parseInt(time)>=Integer.parseInt(timeBegin)&&Integer.parseInt(time)<Integer.parseInt(timeEnd)){
				if(!(m.get("MT_ID")+"").equals(TF.currentMealTimes)){
					TF.currentMealTimes=m.get("MT_ID")+"";
					TF.preConsumeJson=new JSONObject();
					Oper.loadConsumeNum();
				}
				return true;
			}
		}
		return false;
	}
	
	//添加监听器
	@Override
	public void addListeners() {
		if(btn_0.getActionListeners().length>0){
			removeListeners();
		}
		btn_0.addActionListener(numListener);
		btn_1.addActionListener(numListener);
		btn_2.addActionListener(numListener);
		btn_3.addActionListener(numListener);
		btn_4.addActionListener(numListener);
		btn_5.addActionListener(numListener);
		btn_6.addActionListener(numListener);
		btn_7.addActionListener(numListener);
		btn_8.addActionListener(numListener);
		btn_9.addActionListener(numListener);
		btn_dot.addActionListener(opListener);
		btn_ok.addActionListener(opListener);
		btn_back.addActionListener(opListener);
		btn_clear.addActionListener(opListener);
	}

	//添加定时器
	@Override
	public void addTimer() {
		// TODO Auto-generated method stub
		
	}

	//去除定时器
	@Override
	public void cancleTimer() {
		// TODO Auto-generated method stub
		//RfidThread.isOnSale=false;
		if(customerPanel!=null){
			customerPanel.dispose();
		}
		
	}

	//移除监听器
	@Override
	public void removeListeners() {
		btn_0.removeActionListener(numListener);
		btn_1.removeActionListener(numListener);
		btn_2.removeActionListener(numListener);
		btn_3.removeActionListener(numListener);
		btn_4.removeActionListener(numListener);
		btn_5.removeActionListener(numListener);
		btn_6.removeActionListener(numListener);
		btn_7.removeActionListener(numListener);
		btn_8.removeActionListener(numListener);
		btn_9.removeActionListener(numListener);
		btn_dot.removeActionListener(opListener);
		btn_ok.removeActionListener(opListener);
		btn_back.removeActionListener(opListener);
		btn_clear.removeActionListener(opListener);
	}
	
//	//row:页面一行展示的商品数	col:一列展示的商品数
//	public void showCommodity(JSONArray jsonArr){
//		invalidate();
//		for(Component com:panel_goods.getComponents()){
////			com.removeMouseListener(addCommodityListener);
//			JButton panel=(JButton)com;
//			panel.setBorder(BorderFactory.createLineBorder(Color.gray));
//			panel.removeAll();
//			panel.repaint();
//		}
//		validate();
//		int size=jsonArr.size();
//		pageMap.put("totalSize", size);
//		pageMap.put("pageSize", row*col-2);
//		pageMap.put("pageNum", pageMap.get("totalSize")/pageMap.get("pageSize"));
//		if(pageMap.get("totalSize")%pageMap.get("pageSize")!=0){
//			pageMap.put("pageNum", pageMap.get("pageNum")+1);
//		}
//		showInfoInPage(jsonArr);
//	}
	
	//row:页面一行展示的商品数	col:一列展示的商品数
		public void showCommodity(){
			invalidate();
			for(Component com:panel_goods.getComponents()){
//				com.removeMouseListener(addCommodityListener);
				JButton panel=(JButton)com;
				panel.setBorder(BorderFactory.createLineBorder(Color.gray));
				panel.removeAll();
				panel.repaint();
			}
			validate();
//			int size=metaDataBO.execute("select count(*) from tf_commodity_record");
			long size=(long) metaDataBO.queryForArray("select count(CI_ID) from commodity_record where USE_STATUS='0'")[0];
			pageMap.put("totalSize", size);
			pageMap.put("pageSize", (long) (row*col-2));
			pageMap.put("pageNum", pageMap.get("totalSize")/pageMap.get("pageSize"));
			if(pageMap.get("totalSize")% pageMap.get("pageSize")!=0){
				pageMap.put("pageNum", pageMap.get("pageNum")+1);
			}
//			showInfoInPage(jsonArr);
			showInfoInPage();
		}
	
//	//row:页面一行展示的商品数	col:一列展示的商品数
//	public void showInfoInPage(final JSONArray jsonArr){
//		int first=(pageMap.get("currentPage")-1)*pageMap.get("pageSize");
//		for(int i=first;i<(first+pageMap.get("pageSize")<=pageMap.get("totalSize")?(first+pageMap.get("pageSize")):pageMap.get("totalSize"));i++){
//			showEachCommodityToPage(jsonArr.getJSONObject(i),(i)%pageMap.get("pageSize"));
//		}
//		invalidate();
//		final JButton priviousPanel=(JButton) panel_goods.getComponents()[row*(col-1)];
////		panel_goods.getComponents()[row*(col-1)].removeMouseListener(priviousListener);
//		JLabel priviousPage=new JLabel("上一页");
//		priviousPage.setHorizontalAlignment(JLabel.CENTER);
//		priviousPage.setFont(new Font("微软雅黑",Font.PLAIN,30));
//		priviousPanel.setLayout(new BorderLayout());
//		priviousPanel.add(priviousPage);
//		if(priviousPanel.getActionListeners().length>0){
//			priviousPanel.removeActionListener(priviousPanel.getActionListeners()[0]);
//		}
//		priviousListener=new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(pageMap.get("currentPage")>1){
//					pageMap.put("currentPage", pageMap.get("currentPage")-1);
//				}
//				showCommodity(jsonArr);
//			}
//		};
//		priviousPanel.addActionListener(priviousListener); 
//		final JButton nextPanel=(JButton) panel_goods.getComponents()[row*col-1];
////		panel_goods.getComponents()[row*col-1].removeMouseListener(nextListener);
//		JLabel nextPage=new JLabel("下一页");
//		nextPage.setHorizontalAlignment(JLabel.CENTER);
//		nextPage.setFont(new Font("微软雅黑",Font.PLAIN,30));
//		nextPanel.setLayout(new BorderLayout());
//		nextPanel.add(nextPage);
//		if(nextPanel.getActionListeners().length>0){
//			nextPanel.removeActionListener(nextPanel.getActionListeners()[0]);
//		}
//		nextListener=new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(pageMap.get("currentPage")<pageMap.get("pageNum")){
//					pageMap.put("currentPage", pageMap.get("currentPage")+1);
//				}
//				showCommodity(jsonArr);
//			}
//		};
//		nextPanel.addActionListener(nextListener);
//		validate();
//	}
	
		//row:页面一行展示的商品数	col:一列展示的商品数
		public void showInfoInPage(){
			int first=(int) ((pageMap.get("currentPage")-1)*pageMap.get("pageSize"));
			List<Map<String,Object>> list=metaDataBO.queryForListMap("select * from commodity_record  where USE_STATUS='0' limit "+first+","+pageMap.get("pageSize")+"");
//			for(int i=first;i<(first+pageMap.get("pageSize")<=pageMap.get("totalSize")?(first+pageMap.get("pageSize")):pageMap.get("totalSize"));i++){
//				showEachCommodityToPage(jsonArr.getJSONObject(i),(i)%pageMap.get("pageSize"));
//			}
			if(list==null||list.size()==0){
				return;
			}
			JSONArray arr=new JSONArray();
			for(Map map:list){
				arr.add(map);
			}
			for(int i=0;i<row*col-2;i++){
				if((i==row*(col-1))){
					i+=1;
				}
				if(addCommodityListener!=null){
					JButton btn=(JButton) panel_goods.getComponents()[i];
					for(int j=0;j<btn.getActionListeners().length;j++){
						btn.removeActionListener(btn.getActionListeners()[j]);
					}
				}
			}
			for(int i=0;i<arr.size();i++){
				showEachCommodityToPage(arr.getJSONObject(i),i);
			}
			invalidate();
			final JButton priviousPanel=(JButton) panel_goods.getComponents()[row*(col-1)];
			if(priviousPanel.getActionListeners().length>0){
				priviousPanel.removeActionListener(priviousPanel.getActionListeners()[0]);
			}
			final JButton nextPanel=(JButton) panel_goods.getComponents()[row*col-1];
			if(nextPanel.getActionListeners().length>0){
				nextPanel.removeActionListener(nextPanel.getActionListeners()[0]);
			}
	//			panel_goods.getComponents()[row*(col-1)].removeMouseListener(priviousListener);
				JLabel priviousPage=new JLabel("上一页");
				priviousPage.setHorizontalAlignment(JLabel.CENTER);
				priviousPage.setFont(new Font("微软雅黑",Font.PLAIN,30));
				priviousPanel.setLayout(new BorderLayout());
				priviousPanel.add(priviousPage);
				priviousListener=new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(pageMap.get("currentPage")>1){
							pageMap.put("currentPage", pageMap.get("currentPage")-1);
						}
						showCommodity();
					}
				};
				if(pageMap.get("currentPage")>1){
					priviousPanel.addActionListener(priviousListener); 
				}
					
//			panel_goods.getComponents()[row*col-1].removeMouseListener(nextListener);
			JLabel nextPage=new JLabel("下一页");
			nextPage.setHorizontalAlignment(JLabel.CENTER);
			nextPage.setFont(new Font("微软雅黑",Font.PLAIN,30));
			nextPanel.setLayout(new BorderLayout());
			nextPanel.add(nextPage);
			nextListener=new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(pageMap.get("currentPage")<pageMap.get("pageNum")){
						pageMap.put("currentPage", pageMap.get("currentPage")+1);
					}
					showCommodity();
				}
			};
			if(pageMap.get("pageNum")>pageMap.get("currentPage")){
				nextPanel.addActionListener(nextListener);
			}
			validate();
		}
		
	public void showEachCommodityToPage(final JSONObject commodityJson,int index){
//		index+=1;
		if((index==row*(col-1))){
			index+=1;
		}
//		else if(index%pageMap.get("pageSize")==(row*col-1)){
//			index+=2;
//		}
		final JButton btn=(JButton) panel_goods.getComponents()[index];
//		panel.removeAll();
		JLabel label=new JLabel(commodityJson.getString("CI_PRICE"));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		JLabel bottomLabel=new JLabel();
		bottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bottomLabel.setVerticalAlignment(SwingConstants.TOP);
		
		bottomLabel.setText(commodityJson.getString("CI_NAME"));

		btn.setLayout(new BorderLayout());
		int fontSize=(int) Math.round(((float)btn.getWidth())/4);
		label.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
		bottomLabel.setFont(new Font("微软雅黑",Font.PLAIN,(int)Math.round(((float)fontSize)/2)));
		label.setOpaque(false);
		bottomLabel.setOpaque(false);
		invalidate();
		btn.add(label, BorderLayout.CENTER);
		btn.add(bottomLabel, BorderLayout.SOUTH);
		validate();
		if(btn.getActionListeners().length>0){
			btn.removeActionListener((ActionListener) btn.getActionListeners()[0]);
		}
		addCommodityListener=new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(TF.ConsumeJson.getIntValue("stage_type")==3){
					Oper.clearConsumeCache();
				}
				if( TF.ConsumeJson.getIntValue("stage_type")==2){
					return;
				}
				// TODO Auto-generated method stub
//				if(TF.ConsumeJson.getIntValue("stage_type")==2){
//					LoadTFCache.clearConsumeCache();
//				}
				String id=commodityJson.getString("CI_ID");
				Float price=Float.parseFloat(commodityJson.getString("CI_PRICE"));
				if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id)==null)
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").put(id,new JSONObject());
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("price", commodityJson.getString("CI_PRICE"));
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("member_price", commodityJson.getString("CI_MEMBERPRICE"));
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("cgname",commodityJson.getString("CI_NAME"));
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).getIntValue("num")+1);
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("use_status",0);
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(id).put("pay_status",0);
				
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+price));
				TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+1);
				//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
				if(TF.dsJson.getIntValue("discount")!=100){
					TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
					}else{
						TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
					}
				TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
				TF.ConsumeJson.put("stage_type",1);
				TF.ConsumeJson.put("status", "请支付");
				String price1=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
				AudioPlay.speak("请付"+price1+"元");
//				String price1=((TF.ConsumeJson.getString("total_price").indexOf(".0")!=-1)?TF.ConsumeJson.getString("total_price").substring(0, TF.ConsumeJson.getString("total_price").indexOf(".0")):TF.ConsumeJson.getString("total_price"));
//				AudioPlay.speak("请付"+price1+"元");
//				speakUtil.speak("请付"+price+"元");
			}
		};
		btn.addActionListener(addCommodityListener);
	}
	
	//当前消费详细展示
		public void showDetailsToFrame(JSONObject newJSON){
		
			invalidate();
			int index=0;
			panel_details.removeAll();
			//遍历rfid
			for(String key:newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
//				if(index>=panel_details.getComponents().length){
//					return;
//				}
				JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key);
//				JPanel panel=(JPanel) panel_details.getComponents()[index];
//				showInfoInJLable(index,panel, dataJson.getIntValue("pay_status"), dataJson.getFloatValue("price"), dataJson.getString("cgname"),1);
//				index++;
				JPanel panel_1 = new JPanel();
				panel_1.setForeground(Color.GRAY);
				panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
				panel_1.setBackground(new Color(255, 255, 255));
				panel_1.setPreferredSize(new Dimension(511, 50));
				panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				
				JLabel lblNewLabel_2 = new JLabel(dataJson.getString("cgname"),JLabel.CENTER);
				lblNewLabel_2.setForeground(new Color(0, 0, 51));
				
				lblNewLabel_2.setPreferredSize(new Dimension(200,50));
				lblNewLabel_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_2);
				JLabel lblNewLabel_1 = new JLabel();
				lblNewLabel_1.setForeground(new Color(0, 0, 51));
				
				lblNewLabel_1.setPreferredSize(new Dimension(50,50));
				lblNewLabel_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_1);
				JLabel lblNewLabel_4 = new JLabel();
				lblNewLabel_4.setText(dataJson.getString("price"));
				lblNewLabel_4.setPreferredSize(new Dimension(80,50));
				lblNewLabel_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_4);
				JLabel lblNewLabel_5 = new JLabel("X");
				lblNewLabel_5.setPreferredSize(new Dimension(20,50));
				lblNewLabel_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_5);
				JLabel lblNewLabel_3 = new JLabel("1");
				lblNewLabel_3.setPreferredSize(new Dimension(25,50));
				lblNewLabel_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_3);
				JLabel lblNewLabel_6 = new JLabel(dataJson.getString("price"),JLabel.CENTER);
				lblNewLabel_6.setPreferredSize(new Dimension(135,50));
				lblNewLabel_6.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
				panel_1.add(lblNewLabel_6);
				if(dataJson.getIntValue("pay_status")==1){
					lblNewLabel_1.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/yzf.png")));
					lblNewLabel_2.setForeground(new Color(0, 153, 102));
					lblNewLabel_3.setForeground(new Color(0, 153, 102));
					lblNewLabel_4.setForeground(new Color(0, 153, 102));
					lblNewLabel_5.setForeground(new Color(0, 153, 102));
					lblNewLabel_6.setForeground(new Color(0, 153, 102));
				}
				panel_details.add(panel_1);
			}
			//遍历手工
			for(String key:newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
//				if(index>=panel_details.getComponents().length){
//					return;
//				}
				JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key);
				JPanel panel_1 = new JPanel();
				panel_1.setForeground(Color.GRAY);
				panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
				panel_1.setBackground(new Color(255, 255, 255));
				panel_1.setPreferredSize(new Dimension(511, 50));
				panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				JLabel lblNewLabel_2 = new JLabel("定价商品",JLabel.CENTER);
				lblNewLabel_2.setForeground(new Color(0, 0, 51));
				lblNewLabel_2.setPreferredSize(new Dimension(250,50));
				lblNewLabel_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				lblNewLabel_2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						int n = JOptionPane.showConfirmDialog(null, "请确认：将该商品从当前订单中移除。", "标题",JOptionPane.YES_NO_OPTION);
						if(n==1){
							return;
						}
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").remove(((JLabel)arg0.getSource()).getName());
					}
				});
				panel_1.add(lblNewLabel_2);
				JLabel lblNewLabel_4 = new JLabel();
				lblNewLabel_4.setText(dataJson.getString("price"));
				lblNewLabel_4.setPreferredSize(new Dimension(80,50));
				lblNewLabel_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_4);
				JLabel lblNewLabel_5 = new JLabel("X");
				lblNewLabel_5.setPreferredSize(new Dimension(20,50));
				lblNewLabel_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_5);
				JLabel lblNewLabel_3 = new JLabel("1");
				lblNewLabel_3.setPreferredSize(new Dimension(25,50));
				lblNewLabel_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_3);
				JLabel lblNewLabel_6 = new JLabel(dataJson.getString("price"),JLabel.CENTER);
				lblNewLabel_6.setPreferredSize(new Dimension(135,50));
				lblNewLabel_6.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
				panel_1.add(lblNewLabel_6);
				panel_details.add(panel_1);
//				JPanel panel=(JPanel) panel_details.getComponents()[index];
//				float handPrice=dataJson.getFloatValue("price");
//				showInfoInJLable(index,panel, 2, handPrice, "",1);
//				index++;
			}
			//遍历商品
			for(String key:newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
				JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key);
				JPanel panel_1 = new JPanel();
				panel_1.setForeground(Color.GRAY);
				panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
				panel_1.setBackground(new Color(255, 255, 255));
				panel_1.setPreferredSize(new Dimension(511, 50));
				panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
				JLabel lblNewLabel_1 = new JLabel();
				lblNewLabel_1.setPreferredSize(new Dimension(50,50));
				lblNewLabel_1.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/shoudong.png")));
				lblNewLabel_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				panel_1.add(lblNewLabel_1);
				JLabel lblNewLabel_2 = new JLabel(dataJson.getString("cgname"),JLabel.LEFT);
				lblNewLabel_2.setForeground(new Color(0, 0, 51));
				lblNewLabel_2.setPreferredSize(new Dimension(200,50));
				lblNewLabel_1.setBorder(getBorder());
				lblNewLabel_2.setName(key);
				lblNewLabel_2.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						int n = JOptionPane.showConfirmDialog(null, "请确认：将该商品从当前订单中移除。", "标题",JOptionPane.YES_NO_OPTION);
						if(n==1){
							return;
						}
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")-TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")*TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")-TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num"));
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")-TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num"));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").remove(((JLabel)arg0.getSource()).getName());
					}
				});
				lblNewLabel_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				
				panel_1.add(lblNewLabel_2);
				JLabel lblNewLabel_4 = new JLabel();
				lblNewLabel_4.setText(dataJson.getString("price"));
				lblNewLabel_4.setPreferredSize(new Dimension(80,50));
				lblNewLabel_4.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				
				lblNewLabel_4.setName(key);
				lblNewLabel_4.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(TF.ConsumeJson.getString("stage_type").equals("1")){
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).put("num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")+1);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+1);
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
						}
					}
				});
				panel_1.add(lblNewLabel_4);
				JLabel lblNewLabel_5 = new JLabel("X");
				lblNewLabel_5.setPreferredSize(new Dimension(20,50));
				lblNewLabel_5.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				lblNewLabel_5.setName(key);
				lblNewLabel_5.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(TF.ConsumeJson.getString("stage_type").equals("1")){
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).put("num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")+1);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+1);
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
						}
					}
				});
				panel_1.add(lblNewLabel_5);
				JLabel lblNewLabel_3 = new JLabel(dataJson.getString("num"));
				lblNewLabel_3.setPreferredSize(new Dimension(25,50));
				lblNewLabel_3.setFont(new Font("微软雅黑 Light", Font.PLAIN, 20));
				lblNewLabel_5.setName(key);
				lblNewLabel_3.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(TF.ConsumeJson.getString("stage_type").equals("1")){
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).put("num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")+1);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")+1);
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")+1);
						}
					}
				});
				panel_1.add(lblNewLabel_3);
				JLabel lblNewLabel_6 = new JLabel(DataConvertUtil.moneryFormat(dataJson.getFloatValue("price")*dataJson.getIntValue("num")),JLabel.CENTER);
				lblNewLabel_6.setPreferredSize(new Dimension(135,50));
				lblNewLabel_6.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
				lblNewLabel_6.setName(key);
				
				lblNewLabel_6.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(TF.ConsumeJson.getString("stage_type").equals("1")){
						if(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")==1){
							int n = JOptionPane.showConfirmDialog(null, "请确认：将该商品从当前订单中移除。", "标题",JOptionPane.YES_NO_OPTION);
							if(n==1){
								return;
							}
							
							TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")-TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")));
							TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")-1);
							//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
							if(TF.dsJson.getIntValue("discount")!=100){
								TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
								}else{
									TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
								}
							TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")-1);
							TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").remove(((JLabel)arg0.getSource()).getName());
							}
						else{
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).put("num", TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getIntValue("num")-1);
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("price", DataConvertUtil.moneryFormat(TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getFloatValue("price")-TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(((JLabel)arg0.getSource()).getName()).getFloatValue("price")));
						TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").put("num",TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getIntValue("num")-1);
						//TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat(Math.rint((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100)));
						if(TF.dsJson.getIntValue("discount")!=100){
							TF.ConsumeJson.put("total_price", new BigDecimal((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100).setScale(0, BigDecimal.ROUND_HALF_UP));
							}else{
								TF.ConsumeJson.put("total_price", DataConvertUtil.moneryFormat((TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getDoubleValue("price")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getDoubleValue("price"))*TF.dsJson.getDoubleValue("discount")/100));	
							}
						TF.ConsumeJson.put("total_num", TF.ConsumeJson.getIntValue("total_num")-1);
						}
						}
					}
				});
				panel_1.add(lblNewLabel_6);
				panel_details.add(panel_1);
//				JPanel panel=(JPanel) panel_details.getComponents()[index];
//				int num=dataJson.getIntValue("num");
//				showInfoInJLable(index,panel, 3, dataJson.getFloatValue("price"), dataJson.getString("cgname"),num);
//				index++;
			}
			for(int i=index;i<panel_details.getComponents().length;i++){
//				JPanel panel=(JPanel) panel_details.getComponents()[i];
				invalidate();
				if(panel_details.getComponents()[i].getGraphics()!=null)
				panel_details.getComponents()[i].getGraphics().dispose();
				JPanel panel=(JPanel) panel_details.getComponents()[i];
				panel.updateUI();
//				panel.repaint();
//				panel.removeAll();
//				panel.repaint();
//				panel.setUI(null);
				validate();
			}
			validate();
		}
		
		//当前消费详细展示
//		public void showInfoInJLable(int index,JPanel panel,int type,float price,String cgname,int num){
//			panel.removeAll();
//			JLabel label=new JLabel(price+"");
//			label.setHorizontalAlignment(SwingConstants.CENTER);
//			label.setVerticalAlignment(SwingConstants.BOTTOM);
//			JLabel bottomLabel=new JLabel();
//			bottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
//			bottomLabel.setVerticalAlignment(SwingConstants.BOTTOM);
//			if(type==0){
//				label.setForeground(Color.gray);
//				bottomLabel.setText(cgname);
//			}else if(type==1){
//				label.setForeground(new Color(14, 147, 46));
//				bottomLabel.setText("已支付");
//			}else if(type==2){
////				label.setForeground(new Color(14, 147, 46));
//				bottomLabel.setText("定价商品");
//			}else if(type==3){
//				label.setForeground(Color.black);
//				bottomLabel.setText(cgname+"*"+num);
//			}
//			panel.setLayout(new BorderLayout());
////			int fontSize=(int) Math.round(((float)panel.getWidth())/3);
//			int fontSize=40;
//			label.setFont(new Font("微软雅黑 Light", Font.ITALIC, fontSize));
//			bottomLabel.setFont(new Font("微软雅黑 Light",Font.PLAIN,(int)Math.round(((float)fontSize)/2)));
////			if(num==0){
////				label.setFont(new Font("DigifaceWide", Font.PLAIN, 20));
////				bottomLabel.setFont(new Font(bottomLabel.getFont().getFontName(),Font.ITALIC,fontSize));
////			}
//			label.setOpaque(false);
//			bottomLabel.setOpaque(false);
//			panel.add(label, BorderLayout.CENTER);
//			panel.add(bottomLabel, BorderLayout.SOUTH);
//		}
	
		//当前消费详细展示
				public void showInfoInJLable(int index,JPanel panel,final int type,final float price,final String cgname,final int num){
//					panel.removeAll();
					JLabel label=new JLabel(price+"");
					label.setHorizontalAlignment(SwingConstants.CENTER);
					label.setVerticalAlignment(SwingConstants.BOTTOM);
					JLabel bottomLabel=new JLabel();
					bottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
					bottomLabel.setVerticalAlignment(SwingConstants.BOTTOM);
					if(type==0){
						label.setForeground(Color.gray);
						bottomLabel.setText(cgname);
					}else if(type==1){
						label.setForeground(new Color(14, 147, 46));
						bottomLabel.setText("已支付");
					}else if(type==2){
//						label.setForeground(new Color(14, 147, 46));
						bottomLabel.setText("定价商品");
					}else if(type==3){
						label.setForeground(Color.black);
						bottomLabel.setText(cgname+"*"+num);
					}
					panel.setLayout(new BorderLayout());
//					int fontSize=(int) Math.round(((float)panel.getWidth())/3);
					int fontSize=40;
					label.setFont(new Font("微软雅黑 Light", Font.ITALIC, fontSize));
					bottomLabel.setFont(new Font("微软雅黑 Light",Font.PLAIN,(int)Math.round(((float)fontSize)/2)));
//					label.setOpaque(false);
//					bottomLabel.setOpaque(false);
//					panel.add(label, BorderLayout.CENTER);
//					panel.add(bottomLabel, BorderLayout.SOUTH);
					panel.setUI(new PanelUI() {
						@Override
						public void paint(Graphics g, JComponent c) {
							// TODO Auto-generated method stub
							Image image=new ImageIcon(GetProjectRealPath.getPath("images/cai2.png")).getImage();
							float percent=(float)image.getHeight(null)/(float)c.getHeight();
//									g.drawImage(image, c.getWidth()-(int)(c.getWidth()/percent), 0,(int)(c.getWidth()/percent),c.getHeight(),null);
							if(type==0){
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
								g.setColor(Color.BLACK);
//										g.drawString(cgname, 5, 50);
						        int strWidth = g.getFontMetrics().stringWidth(cgname);
						        g.drawString(cgname, (c.getWidth()-strWidth) / 2, 50);
							
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
								String p=DataConvertUtil.moneryFormat(price);
								g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
							}else if(type==1){
//										label.setForeground(new Color(14, 147, 46));
//										bottomLabel.setText("已支付");
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
								g.setColor(Color.BLACK);
								int strWidth = g.getFontMetrics().stringWidth("已支付");
						        g.drawString("已支付", (c.getWidth()-strWidth) / 2, 50);
//										g.drawString("已支付", 5, 5);
								g.setColor(new Color(14, 147, 46));
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
								String p=DataConvertUtil.moneryFormat(price);
								g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
							}else if(type==2){
//										label.setForeground(new Color(14, 147, 46));
//										bottomLabel.setText("定价商品");
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
								g.setColor(Color.black);
								int strWidth = g.getFontMetrics().stringWidth("定价商品");
						        g.drawString("定价商品", (c.getWidth()-strWidth) / 2, 50);
//										g.drawString("定价商品", 5, 5);
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
								String p=DataConvertUtil.moneryFormat(price);
								g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
							}else if(type==3){
//										label.setForeground(Color.black);
//										bottomLabel.setText(cgname+"*"+num);
								g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
								g.setColor(Color.BLACK);
								int strWidth = g.getFontMetrics().stringWidth(cgname+"*"+num);
						        g.drawString(cgname+"*"+num, (c.getWidth()-strWidth) / 2, 50);
//										g.drawString(cgname+"*"+num, 5, 5);
						        g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
						        String p=DataConvertUtil.moneryFormat(price);
						        g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
							}
//									g.drawString(DataConvertUtil.moneryFormat(price), 5, c.getHeight()-50);
//									super.paint(g, c);
						}
					});
				}
		
		//显示总价和总数量
		public void showConsumeToFrame(JSONObject newJSON){
			label_sum.setText(newJSON.getString("total_price"));
			label_num.setText(newJSON.getString("total_num"));
		}
		
		//显示状态（请放托盘，请刷卡。。）
		public void showStatusToFrame(JSONObject newJSON){
			label_status.setText(newJSON.getString("status"));
		}
		
		//显示实收找零和卡余额
//		public void showActualAndChange(JSONObject newJSON){
//			JSONObject deductionJson=newJSON.getJSONObject("deduction");
//			JSONObject cashJson=(JSONObject) deductionJson.get("cash");
//			JSONObject cardJson=(JSONObject) deductionJson.get("card");
//			label_actual.setText(cashJson.getFloatValue("real_price")+"");//实收
//			label_change.setText(cashJson.getFloatValue("dispenser")+"");//找零
//			label_cardMoney.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");//余额
//		}
		
		//显示卡内余额
		public void showCardMoney(JSONObject newJSON){
			label_actual.setVisible(false);
			label_actual_txt.setVisible(false);
			label_change.setVisible(false);
			label_change_txt.setVisible(false);
			label_cardMoney.setVisible(true);
			lbl_MName.setVisible(true);
			lblMPhone.setVisible(true);
			lbl_MName.setText("");
			lblMPhone.setText("");
			JSONObject deductionJson=newJSON.getJSONObject("deduction");
			if(deductionJson.containsKey("member")){
				JSONObject cardJson=(JSONObject) deductionJson.get("member");
				if(cardJson.containsKey("cash")){
					if(cardJson.getJSONObject("cash").containsKey("MI_NAME")){
				      lbl_MName.setText(cardJson.getJSONObject("cash").getString("MI_NAME"));
					}
					if(cardJson.getJSONObject("cash").containsKey("MI_NO")){
						lbl_MName.setText(lbl_MName.getText()+"("+cardJson.getJSONObject("cash").getString("MI_NO")+")");
					}
					lblMPhone.setText(cardJson.getJSONObject("cash").getString("MI_PHONE"));
				}
				if(cardJson.containsKey("info")){
					if(cardJson.getJSONObject("info").containsKey("MI_NAME")){
						lbl_MName.setText(cardJson.getJSONObject("info").getString("MI_NAME"));
							}
							if(cardJson.getJSONObject("info").containsKey("MI_NO")){
								lbl_MName.setText(lbl_MName.getText()+"("+cardJson.getJSONObject("cash").getString("MI_NO")+")");
							}
							lblMPhone.setText(cardJson.getJSONObject("info").getString("MI_PHONE"));
				}
			label_cardMoney.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");
		}
		}
		
		//显示实收找零
		public void showActualAndChange(JSONObject newJSON){
			label_actual.setVisible(true);
			label_actual_txt.setVisible(true);
			label_change.setVisible(true);
			label_change_txt.setVisible(true);
			label_cardMoney.setVisible(false);
			lbl_MName.setVisible(false);
			lblMPhone.setVisible(false);
			JSONObject deductionJson=newJSON.getJSONObject("deduction");
			JSONObject cashJson=(JSONObject) deductionJson.get("cash");
//			JSONObject cardJson=(JSONObject) deductionJson.get("card");
			label_actual.setText(cashJson.getFloatValue("real_price")+"");//实收
			label_change.setText(cashJson.getFloatValue("dispenser")+"");//找零
//			label_cardMoney.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");//余额
		}
		
		//显示流水
		public void showConsumeWater(){
			for(String key:TF.consumeWaterJson.keySet()){
				switch (key) {
				case "1":
					label_water_1.setText(TF.consumeWaterJson.getJSONObject(key).getString("num")+"          "+DataConvertUtil.moneryFormat(TF.consumeWaterJson.getJSONObject(key).getDoubleValue("price")));
					break;
				case "2":
					label_water_2.setText(TF.consumeWaterJson.getJSONObject(key).getString("num")+"          "+DataConvertUtil.moneryFormat(TF.consumeWaterJson.getJSONObject(key).getDoubleValue("price")));
					break;
				case "3":
					label_water_3.setText(TF.consumeWaterJson.getJSONObject(key).getString("num")+"          "+DataConvertUtil.moneryFormat(TF.consumeWaterJson.getJSONObject(key).getDoubleValue("price")));
					break;
				default:
					break;
				}
			}
		}
		
		//显示当天和当餐消费人数
		public void showConsumeNum(){
			label_people_total.setText(TF.consumePersonJson.getString("cur_day"));
			label_people_current.setText(TF.consumePersonJson.getString("cur_meal"));
		}
		
		//显示挂单单数和总价
		public void showGuadan(){
			label_guadan_num.setText(TF.gdJson.getIntValue("total_num")+"");
			label_guadan_total.setText(DataConvertUtil.moneryFormat(TF.gdJson.getDoubleValue("total_price")));
			repaint();
		}
		public void guadan(){
			if(TF.ConsumeJson.getIntValue("stage_type")==1){
				TF.gdJson.put("total_price", TF.gdJson.getFloatValue("total_price")+TF.ConsumeJson.getFloatValue("total_price"));
				TF.gdJson.put("total_num", TF.gdJson.getIntValue("total_num")+TF.ConsumeJson.getIntValue("total_num"));
				for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
					TF.gdJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key));
				}
				for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
					TF.gdJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key));
				}
				for(String key:TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
					if(TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").containsKey(key)){
						TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).put("num",TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num")+TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key).getIntValue("num"));
					}else{
						TF.gdJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").put(key,TF.ConsumeJson.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key));
					}
				}
				Oper.clearConsumeCache();
			}
			
		}
		//页面展示
		public void initPage(){
			newJSON = JSONObject.parseObject(TF.ConsumeJson.toJSONString());
			if(!newJSON.toJSONString().equals(oldCacheJSON.toJSONString())){
				showConsumeToFrame(newJSON);
				showStatusToFrame(newJSON);
				showDetailsToFrame(newJSON);
//				showActualAndChange(newJSON);
				if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")>0){
					showActualAndChange(newJSON);
				}else{
					showCardMoney(newJSON);
				}
				showConsumeWater();
				showConsumeNum();
				oldCacheJSON = new JSONObject();
				oldCacheJSON = JSONObject.parseObject(newJSON.toJSONString());
				
			}
			if(TF.dsJson.getDoubleValue("fixed_price")==0){
				model_dispaly.setText("");
			}else{
				model_dispaly.setText("固价模式:"+TF.dsJson.getDoubleValue("fixed_price")+"元");
			}
			//lbl_discount.setText(TF.dsJson.getString("discount")+"%");
//			if(danshuSize!=TF.consumeList.size()){
				showGuadan();
//			}
				txt_price.requestFocus();	
		}
		
		public static void main(String[] args) {
			JFrame frame=new JFrame();
			frame.setSize(1024, 696);
			frame.getContentPane().add(new SalePanel(new MainFrame()));
			frame.setUndecorated(true);
			WindowUtil.centerWindow(frame);
			frame.setVisible(true);
		}
}