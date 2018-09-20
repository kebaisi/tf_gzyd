package com.kbs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.plaf.PanelUI;

import com.alibaba.fastjson.JSONObject;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.FlowLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class CustomerPanelGg extends JDialog {
	Timer displayTimer = null;
	JSONObject newJSON = new JSONObject();
	JSONObject oldCacheJSON=new JSONObject();
	private JPanel panel_customer_details;
	private JLabel label_customer_num;
	private JLabel label_customer_sum;
	private JLabel label_customer_status;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel card_bance;
	private JLabel cash_ss_l;
	private JLabel cash_ss_v;
	private JLabel cash_zl_l;
	private JLabel cash_zl_v;

	/**
	 * Create the panel.
	 */
	public CustomerPanelGg() {
		initGUI();
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
	}
	 private void addCompForBorder(Border border,String lable,Container container) {  
         //</span><span style="font-size:18px;">//(1)</span><span style="font-size:18px;">  
         JPanel comp = new JPanel(false);  
         JLabel label = new JLabel(lable, JLabel.CENTER);  
         comp.setLayout(new GridLayout(1, 1));  
         comp.add(label);  
         comp.setBorder(border) ;

     container.add(Box.createRigidArea(new Dimension(0, 10)));  
     container.add(comp);  
 } 
	public void initGUI(){
		setSize(1024, 768);
		getContentPane().setLayout(null);
		
		JPanel panel_customer_status = new JPanel();
		panel_customer_status.setBackground(new Color(0, 51, 102));
		panel_customer_status.setBounds(706, 0, 302, 87);
		getContentPane().add(panel_customer_status);
		panel_customer_status.setLayout(new BorderLayout(0, 0));
		
		label_customer_status = new JLabel("请放托盘");
		panel_customer_status.add(label_customer_status, BorderLayout.CENTER);
		label_customer_status.setForeground(new Color(255, 255, 255));
		label_customer_status.setFont(new Font("微软雅黑", Font.PLAIN, 32));
		label_customer_status.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_customer_info = new JPanel();
		panel_customer_info.setBackground(Color.GRAY);
		panel_customer_info.setForeground(Color.WHITE);
//		panel_customer_info.setBackground(new Color(0, 153, 102));
		panel_customer_info.setBounds(706, 578, 302, 61);
		getContentPane().add(panel_customer_info);
		panel_customer_info.setLayout(new GridLayout(1, 0, 0, 0));
		
		label_customer_num = new JLabel("3");
		label_customer_num.setForeground(Color.WHITE);
		label_customer_num.setFont(new Font("微软雅黑 Light", Font.BOLD, 30));
		label_customer_num.setHorizontalAlignment(SwingConstants.CENTER);
		panel_customer_info.add(label_customer_num);
		
		label_customer_sum = new JLabel("20000.0");
		label_customer_sum.setForeground(Color.WHITE);
		label_customer_sum.setFont(new Font("微软雅黑 Light", Font.BOLD, 30));
		label_customer_sum.setHorizontalAlignment(SwingConstants.CENTER);
		panel_customer_info.add(label_customer_sum);
		panel_customer_details = new JPanel();
		panel_customer_details.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		
		
		panel_customer_details.setBackground(Color.GRAY);
		panel_customer_details.setBounds(706, 87, 300, 490);
		getContentPane().add(panel_customer_details);
		panel_customer_details.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setPreferredSize(new Dimension(300,40));
		panel_customer_details.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		JLabel label = new JLabel("  冰红茶",JLabel.CENTER);
		
		label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		label.setForeground(Color.BLACK);
		label.setPreferredSize(new Dimension(180,40));
		panel_1.add(label);
		
		lblNewLabel = new JLabel("X 2");
		lblNewLabel.setPreferredSize(new Dimension(40,40));
		lblNewLabel.setFont(new Font("微软雅黑", Font.ITALIC, 14));
		panel_1.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("5361.6");
		lblNewLabel_1.setPreferredSize(new Dimension(80,40));
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
		panel_1.add(lblNewLabel_1);
		
		JPanel panel_customer_card = new JPanel();
		panel_customer_card.setBackground(Color.WHITE);
		panel_customer_card.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		panel_customer_card.setBounds(706, 638, 302, 87);
		getContentPane().add(panel_customer_card);
		panel_customer_card.setLayout(null);
		
		card_bance = new JLabel("99999.0");
		
		card_bance.setForeground(Color.BLACK);
		card_bance.setHorizontalAlignment(SwingConstants.CENTER);
		card_bance.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		card_bance.setBounds(0, 0, 302, 87);
		panel_customer_card.add(card_bance);
		cash_ss_l = new JLabel("实收");
		cash_ss_l.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		cash_ss_l.setBounds(0, 0, 54, 15);
		panel_customer_card.add(cash_ss_l);
		
		cash_ss_v = new JLabel("100000");
		cash_ss_v.setHorizontalAlignment(SwingConstants.CENTER);
		cash_ss_v.setFont(new Font("微软雅黑", Font.PLAIN, 30));
		cash_ss_v.setBounds(0, 20, 152, 36);
		panel_customer_card.add(cash_ss_v);
		
		cash_zl_l = new JLabel("找零");
		cash_zl_l.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		cash_zl_l.setBounds(144, 0, 54, 15);
		panel_customer_card.add(cash_zl_l);
		
		cash_zl_v = new JLabel("100000");
		cash_zl_v.setHorizontalAlignment(SwingConstants.CENTER);
		cash_zl_v.setFont(new Font("微软雅黑", Font.PLAIN, 30));
		cash_zl_v.setBounds(154, 20, 146, 36);
		panel_customer_card.add(cash_zl_v);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 705, 1008, 24);
		getContentPane().add(panel);
		
		JLabel label_1 = new JLabel("深圳市科拜斯物联网科技有限公司");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel.add(label_1);
		
	
		Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);
        view.setBounds(0, 0, 705, 729);
        view.setPreferredSize(new Dimension(705, 768));
//        browser.loadURL("http://www.junglevents.com/");
        browser.loadURL(TF.dsJson.getString("gg"));
		getContentPane().add(view);
		
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		// TODO Auto-generated method stub
//		ImageIcon img=new ImageIcon(CustomerPanel.class.getResource("/images/customer.jpg"));
//		g.drawImage(img.getImage(), 0, 0,this.getWidth(),this.getHeight(), null);
////		g.drawLine(0, 0, 500, 500);
//		System.out.println("------------");
//	}
	
	//当前消费详细展示
			public void showDetailsToFrame(JSONObject newJSON){
				System.out.println("999999999999999-++++++++");
				panel_customer_details.removeAll();
				panel_customer_details.repaint();
				panel_customer_details.repaint();
//				invalidate();
				int index=0;
				//遍历rfid
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key);
					panel_1 = new JPanel();
					panel_1.setBackground(Color.WHITE);
					panel_1.setPreferredSize(new Dimension(300,40));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
					JLabel label = new JLabel(" "+dataJson.getString("cgname"),JLabel.LEFT);
					label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
					label.setForeground(Color.BLACK);
					label.setPreferredSize(new Dimension(180,40));
					panel_1.add(label);
					
					lblNewLabel = new JLabel("X 1");
					lblNewLabel.setPreferredSize(new Dimension(40,40));
					lblNewLabel.setFont(new Font("微软雅黑", Font.ITALIC, 14));
					panel_1.add(lblNewLabel);
					
					lblNewLabel_1 = new JLabel(dataJson.getString("price"),JLabel.CENTER);
					lblNewLabel_1.setPreferredSize(new Dimension(80,40));
					lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
					panel_1.add(lblNewLabel_1);
					if(dataJson.getIntValue("pay_status")==1){
						label.setForeground(new Color(0, 153, 102));
						lblNewLabel.setForeground(new Color(0, 153, 102));
						lblNewLabel_1.setForeground(new Color(0, 153, 102));
					}
					panel_customer_details.add(panel_1);
				}
				//遍历手工
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key);
					panel_1 = new JPanel();
					panel_1.setBackground(Color.WHITE);
					panel_1.setPreferredSize(new Dimension(300,40));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
					JLabel label = new JLabel(" 定价商品",JLabel.LEFT);
					label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
					label.setForeground(Color.BLACK);
					label.setPreferredSize(new Dimension(180,40));
					panel_1.add(label);
					
					lblNewLabel = new JLabel("X 1");
					lblNewLabel.setPreferredSize(new Dimension(40,40));
					lblNewLabel.setFont(new Font("微软雅黑", Font.ITALIC, 14));
					panel_1.add(lblNewLabel);
					
					lblNewLabel_1 = new JLabel(dataJson.getString("price"),JLabel.CENTER);
					lblNewLabel_1.setPreferredSize(new Dimension(80,40));
					lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
					panel_1.add(lblNewLabel_1);
					if(dataJson.getIntValue("pay_status")==1){
						label.setForeground(new Color(0, 153, 102));
						lblNewLabel.setForeground(new Color(0, 153, 102));
						lblNewLabel_1.setForeground(new Color(0, 153, 102));
					}
					panel_customer_details.add(panel_1);
				}
				//遍历商品
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key);
					panel_1 = new JPanel();
					panel_1.setBackground(Color.WHITE);
					panel_1.setPreferredSize(new Dimension(300,40));
					panel_1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
					JLabel label = new JLabel(" "+dataJson.getString("cgname"),JLabel.LEFT);
					label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
					label.setForeground(Color.BLACK);
					label.setPreferredSize(new Dimension(180,40));
					panel_1.add(label);
					
					lblNewLabel = new JLabel("X "+dataJson.getString("num"));
					lblNewLabel.setPreferredSize(new Dimension(40,40));
					lblNewLabel.setFont(new Font("微软雅黑", Font.ITALIC, 14));
					panel_1.add(lblNewLabel);
					
					lblNewLabel_1 = new JLabel(DataConvertUtil.moneryFormat(dataJson.getFloatValue("price")*dataJson.getIntValue("num")),JLabel.CENTER);
					lblNewLabel_1.setPreferredSize(new Dimension(80,40));
					lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
					panel_1.add(lblNewLabel_1);
					if(dataJson.getIntValue("pay_status")==1){
						label.setForeground(new Color(0, 153, 102));
						lblNewLabel.setForeground(new Color(0, 153, 102));
						lblNewLabel_1.setForeground(new Color(0, 153, 102));
					}
					panel_customer_details.add(panel_1);
				}
				for(int i=index;i<panel_customer_details.getComponents().length;i++){
//					JPanel panel=(JPanel) panel_details.getComponents()[i];
					invalidate();
					if(panel_customer_details.getComponents()[i].getGraphics()!=null)
						panel_customer_details.getComponents()[i].getGraphics().dispose();
					JPanel panel=(JPanel) panel_customer_details.getComponents()[i];
					panel.updateUI();
//					panel.repaint();
//					panel.removeAll();
//					panel.repaint();
//					panel.setUI(null);
					validate();
				}
				validate();
			}
			
			//当前消费详细展示
			public void showInfoInJLable(int index,JPanel panel,final int type,final float price,final String cgname,final int num){
//				panel.removeAll();
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
//					label.setForeground(new Color(14, 147, 46));
					bottomLabel.setText("定价商品");
				}else if(type==3){
					label.setForeground(Color.black);
					bottomLabel.setText(cgname+"*"+num);
				}
				panel.setLayout(new BorderLayout());
//				int fontSize=(int) Math.round(((float)panel.getWidth())/3);
				int fontSize=40;
				label.setFont(new Font("微软雅黑 Light", Font.ITALIC, fontSize));
				bottomLabel.setFont(new Font("微软雅黑 Light",Font.PLAIN,(int)Math.round(((float)fontSize)/2)));
//				label.setOpaque(false);
//				bottomLabel.setOpaque(false);
//				panel.add(label, BorderLayout.CENTER);
//				panel.add(bottomLabel, BorderLayout.SOUTH);
				panel.setUI(new PanelUI() {

					@Override
					public void paint(Graphics g, JComponent c) {
						// TODO Auto-generated method stub
						Image image=new ImageIcon(GetProjectRealPath.getPath("images/cai2.png")).getImage();
						float percent=(float)image.getHeight(null)/(float)c.getHeight();
//								g.drawImage(image, c.getWidth()-(int)(c.getWidth()/percent), 0,(int)(c.getWidth()/percent),c.getHeight(),null);
						if(type==0){
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
							g.setColor(Color.BLACK);
//									g.drawString(cgname, 5, 50);
					        int strWidth = g.getFontMetrics().stringWidth(cgname);
					        g.drawString(cgname, (c.getWidth()-strWidth) / 2, 50);
						
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
							String p=DataConvertUtil.moneryFormat(price);
							g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
						}else if(type==1){
//									label.setForeground(new Color(14, 147, 46));
//									bottomLabel.setText("已支付");
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
							g.setColor(Color.BLACK);
							int strWidth = g.getFontMetrics().stringWidth("已支付");
					        g.drawString("已支付", (c.getWidth()-strWidth) / 2, 50);
//									g.drawString("已支付", 5, 5);
							g.setColor(new Color(14, 147, 46));
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
							String p=DataConvertUtil.moneryFormat(price);
							g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
						}else if(type==2){
//									label.setForeground(new Color(14, 147, 46));
//									bottomLabel.setText("定价商品");
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
							g.setColor(Color.black);
							int strWidth = g.getFontMetrics().stringWidth("定价商品");
					        g.drawString("定价商品", (c.getWidth()-strWidth) / 2, 50);
//									g.drawString("定价商品", 5, 5);
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
							String p=DataConvertUtil.moneryFormat(price);
							g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
						}else if(type==3){
//									label.setForeground(Color.black);
//									bottomLabel.setText(cgname+"*"+num);
							g.setFont(new Font("微软雅黑 Light",Font.PLAIN,22));
							g.setColor(Color.BLACK);
							int strWidth = g.getFontMetrics().stringWidth(cgname+"*"+num);
					        g.drawString(cgname+"*"+num, (c.getWidth()-strWidth) / 2, 50);
//									g.drawString(cgname+"*"+num, 5, 5);
					        g.setFont(new Font("微软雅黑 Light",Font.PLAIN,40));
					        String p=DataConvertUtil.moneryFormat(price);
					        g.drawString(p, ((c.getWidth()-g.getFontMetrics().stringWidth(p))/2), c.getHeight()-5);
						}
//								g.drawString(DataConvertUtil.moneryFormat(price), 5, c.getHeight()-50);
//								super.paint(g, c);
					}
				});
			}
	
	//显示总价和总数量
	public void showConsumeToFrame(JSONObject newJSON){
		label_customer_sum.setText(newJSON.getString("total_price"));
		label_customer_num.setText(newJSON.getString("total_num"));
	}
	
	//显示状态（请放托盘，请刷卡。。）
	public void showStatusToFrame(JSONObject newJSON){
		label_customer_status.setText(newJSON.getString("status"));
	}
	
	//显示卡内余额
	public void showCardMoney(JSONObject newJSON){
		cash_ss_l.setVisible(false);
		cash_ss_v.setVisible(false);
		cash_zl_l.setVisible(false);
		cash_zl_v.setVisible(false);
		card_bance.setVisible(true);
//		label_customer_card_txt.setVisible(true);
		JSONObject deductionJson=newJSON.getJSONObject("deduction");
		JSONObject cardJson=(JSONObject) deductionJson.get("member");
		card_bance.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");
	}
	
//	//显示实收找零
	public void showActualAndChange(JSONObject newJSON){
		cash_ss_l.setVisible(true);
		cash_ss_v.setVisible(true);
		cash_zl_l.setVisible(true);
		cash_zl_v.setVisible(true);
		card_bance.setVisible(false);
		JSONObject deductionJson=newJSON.getJSONObject("deduction");
		JSONObject cashJson=(JSONObject) deductionJson.get("cash");
//		JSONObject cardJson=(JSONObject) deductionJson.get("card");
		cash_ss_v.setText(cashJson.getFloatValue("real_price")+"");//实收
		cash_zl_v.setText(cashJson.getFloatValue("dispenser")+"");//找零
//		label_cardMoney.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");//余额
	}
	
	//页面展示
	public void initPage(){
		newJSON = JSONObject.parseObject(TF.ConsumeJson.toJSONString());
		if(!newJSON.toJSONString().equals(oldCacheJSON.toJSONString())){
			showConsumeToFrame(newJSON);
			showStatusToFrame(newJSON);
			showDetailsToFrame(newJSON);
			if(TF.ConsumeJson.getJSONObject("deduction").getJSONObject("cash").getFloatValue("real_price")>0){
			showActualAndChange(newJSON);
			}else{
				showCardMoney(newJSON);
			}
			oldCacheJSON = new JSONObject();
			oldCacheJSON = JSONObject.parseObject(newJSON.toJSONString());
		}
	}

	public static void main(String[] args) {
		JFrame frame=new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setSize(1024, 768);
		CustomerPanelGg customerPanel=new CustomerPanelGg();
		frame.getContentPane().add(customerPanel,BorderLayout.CENTER);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}
