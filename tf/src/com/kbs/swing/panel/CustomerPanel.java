package com.kbs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.plaf.PanelUI;

import com.alibaba.fastjson.JSONObject;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.GetProjectRealPath;
import com.kbs.util.TF;

public class CustomerPanel extends JDialog {
	private JLabel label_customer_card_txt;
	private JLabel label_customer_card;
	private JLabel label_customer_change;
	private JLabel label_customer_change_txt;
	Timer displayTimer = null;
	JSONObject newJSON = new JSONObject();
	JSONObject oldCacheJSON=new JSONObject();
	private JPanel panel_customer_details;
	private JLabel label_customer_num;
	private JLabel label_customer_sum;
	private JLabel label_customer_status;
	private JLabel label_customer_acutal;
	private JLabel label_customer_actual_txt;
	private JLabel lbl_MName;
	private JLabel lblMiNo;

	/**
	 * Create the panel.
	 */
	public CustomerPanel() {
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

	public void initGUI(){
		setSize(800, 600);
		getContentPane().setLayout(null);
		
		JPanel panel_customer_status = new JPanel();
		panel_customer_status.setBackground(new Color(51, 204, 255));
		panel_customer_status.setBounds(0, 0, 784, 155);
		getContentPane().add(panel_customer_status);
		panel_customer_status.setLayout(new BorderLayout(0, 0));
		
		label_customer_status = new JLabel("请放托盘");
		panel_customer_status.add(label_customer_status, BorderLayout.CENTER);
		label_customer_status.setForeground(new Color(255, 255, 255));
		label_customer_status.setFont(new Font("微软雅黑", Font.PLAIN, 60));
		label_customer_status.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_customer_info = new JPanel();
		panel_customer_info.setBackground(new Color(128, 0, 0));
		panel_customer_info.setBounds(0, 155, 784, 122);
		getContentPane().add(panel_customer_info);
		panel_customer_info.setLayout(new GridLayout(1, 0, 0, 0));
		
		label_customer_num = new JLabel("3");
		label_customer_num.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
		label_customer_num.setForeground(Color.WHITE);
		label_customer_num.setFont(new Font("微软雅黑 Light", Font.PLAIN, 99));
		label_customer_num.setHorizontalAlignment(SwingConstants.CENTER);
		panel_customer_info.add(label_customer_num);
		
		label_customer_sum = new JLabel("20.0");
		label_customer_sum.setForeground(Color.WHITE);
		label_customer_sum.setFont(new Font("微软雅黑 Light", Font.PLAIN, 99));
		label_customer_sum.setHorizontalAlignment(SwingConstants.CENTER);
		panel_customer_info.add(label_customer_sum);
		
		panel_customer_details = new JPanel();
		panel_customer_details.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		panel_customer_details.setBounds(0, 277, 784, 155);
		getContentPane().add(panel_customer_details);
		panel_customer_details.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_customer_details1 = new JPanel();
		panel_customer_details.add(panel_customer_details1);
		
		JPanel panel_customer_details2 = new JPanel();
		panel_customer_details.add(panel_customer_details2);
		
		JPanel panel_customer_details3 = new JPanel();
		panel_customer_details.add(panel_customer_details3);
		
		JPanel panel_customer_details4 = new JPanel();
		panel_customer_details.add(panel_customer_details4);
		
		JPanel panel_customer_card = new JPanel();
		panel_customer_card.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		panel_customer_card.setBounds(0, 432, 784, 119);
		getContentPane().add(panel_customer_card);
		panel_customer_card.setLayout(null);
		
		label_customer_card_txt = new JLabel("余额:");
		label_customer_card_txt.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		label_customer_card_txt.setBounds(195, 0, 68, 37);
		panel_customer_card.add(label_customer_card_txt);
		
		label_customer_actual_txt = new JLabel("实收:");
		label_customer_actual_txt.setHorizontalAlignment(SwingConstants.RIGHT);
		label_customer_actual_txt.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		label_customer_actual_txt.setBounds(0, 0, 68, 37);
		panel_customer_card.add(label_customer_actual_txt);
		
		label_customer_acutal = new JLabel("100.0");
		label_customer_acutal.setForeground(new Color(0, 102, 51));
		label_customer_acutal.setHorizontalAlignment(SwingConstants.CENTER);
		label_customer_acutal.setFont(new Font("微软雅黑 Light", Font.PLAIN, 56));
		label_customer_acutal.setBounds(0, 0, 387, 126);
		panel_customer_card.add(label_customer_acutal);
		
		label_customer_change_txt = new JLabel("找零:");
		label_customer_change_txt.setHorizontalAlignment(SwingConstants.RIGHT);
		label_customer_change_txt.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		label_customer_change_txt.setBounds(397, 0, 68, 37);
		panel_customer_card.add(label_customer_change_txt);
		
		label_customer_change = new JLabel("80.0");
		label_customer_change.setForeground(new Color(0, 102, 51));
		label_customer_change.setHorizontalAlignment(SwingConstants.CENTER);
		label_customer_change.setFont(new Font("微软雅黑 Light", Font.PLAIN, 56));
		label_customer_change.setBounds(388, 0, 396, 126);
		panel_customer_card.add(label_customer_change);
		
		JLabel label = new JLabel("深圳市科拜斯物联网科技有限公司 400-1680881");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setEnabled(false);
		label.setBounds(0, 108, 1008, 37);
		panel_customer_card.add(label);
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		
		label_customer_card = new JLabel("9999.0");
		label_customer_card.setBounds(195, 0, 589, 126);
		panel_customer_card.add(label_customer_card);
		label_customer_card.setHorizontalAlignment(SwingConstants.CENTER);
		label_customer_card.setForeground(new Color(0, 102, 51));
		label_customer_card.setFont(new Font("微软雅黑 Light", Font.PLAIN, 56));
		
		lbl_MName = new JLabel("");
		lbl_MName.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_MName.setFont(new Font("微软雅黑 Light", Font.BOLD, 28));
		lbl_MName.setBounds(0, 0, 196, 70);
		panel_customer_card.add(lbl_MName);
		
		lblMiNo = new JLabel("");
		lblMiNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblMiNo.setFont(new Font("微软雅黑 Light", Font.PLAIN, 18));
		lblMiNo.setBounds(0, 58, 196, 37);
		panel_customer_card.add(lblMiNo);
		
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
				invalidate();
				int index=0;
				//遍历rfid
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").keySet()){
					if(index>=panel_customer_details.getComponents().length){
						return;
					}
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("auto").getJSONObject("view").getJSONObject(key);
					JPanel panel=(JPanel) panel_customer_details.getComponents()[index];
					showInfoInJLable(index,panel, dataJson.getIntValue("pay_status"), dataJson.getFloatValue("price"), dataJson.getString("cgname"),1);
					index++;
				}
				//遍历手工
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").keySet()){
					if(index>=panel_customer_details.getComponents().length){
						return;
					}
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("handwork").getJSONObject("view").getJSONObject(key);
					JPanel panel=(JPanel) panel_customer_details.getComponents()[index];
					float handPrice=dataJson.getFloatValue("price");
					showInfoInJLable(index,panel, 2, handPrice, "",1);
					index++;
				}
				//遍历商品
				for(String key:newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").keySet()){
					if(index>=panel_customer_details.getComponents().length){
						return;
					}
					JSONObject dataJson=newJSON.getJSONObject("valuation").getJSONObject("commodity").getJSONObject("view").getJSONObject(key);
					JPanel panel=(JPanel) panel_customer_details.getComponents()[index];
					int num=dataJson.getIntValue("num");
					showInfoInJLable(index,panel, 3, dataJson.getFloatValue("price"), dataJson.getString("cgname"),num);
					index++;
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
		label_customer_acutal.setVisible(false);
		label_customer_actual_txt.setVisible(false);
		label_customer_change.setVisible(false);
		label_customer_change_txt.setVisible(false);
		label_customer_card.setVisible(true);
		label_customer_card_txt.setVisible(true);
		lbl_MName.setVisible(true);
		JSONObject deductionJson=newJSON.getJSONObject("deduction");
		if(deductionJson.containsKey("member")){
		JSONObject cardJson=(JSONObject) deductionJson.get("member");
		if(cardJson.containsKey("cash")){
			if(cardJson.getJSONObject("cash").containsKey("MI_NAME")){
		lbl_MName.setText(cardJson.getJSONObject("cash").getString("MI_NAME"));
			}
			if(cardJson.getJSONObject("cash").containsKey("MI_NO")){
			lblMiNo.setText(cardJson.getJSONObject("cash").getString("MI_NO"));
			}
		}
		if(cardJson.containsKey("info")){
			if(cardJson.getJSONObject("info").containsKey("MI_NAME")){
				lbl_MName.setText(cardJson.getJSONObject("info").getString("MI_NAME"));
					}
					if(cardJson.getJSONObject("info").containsKey("MI_NO")){
					lblMiNo.setText(cardJson.getJSONObject("info").getString("MI_NO"));
					}
		}
		label_customer_card.setText(cardJson.getString("balance").equals("")?"":cardJson.getFloatValue("balance")+"");
		}
	}
	
	//显示实收找零
	public void showActualAndChange(JSONObject newJSON){
		label_customer_acutal.setVisible(true);
		label_customer_actual_txt.setVisible(true);
		label_customer_change.setVisible(true);
		label_customer_change_txt.setVisible(true);
		label_customer_card.setVisible(false);
		label_customer_card_txt.setVisible(false);
		JSONObject deductionJson=newJSON.getJSONObject("deduction");
		JSONObject cashJson=(JSONObject) deductionJson.get("cash");
//		JSONObject cardJson=(JSONObject) deductionJson.get("card");
		lbl_MName.setVisible(false);
		label_customer_acutal.setText(cashJson.getFloatValue("real_price")+"");//实收
		label_customer_change.setText(cashJson.getFloatValue("dispenser")+"");//找零
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
		CustomerPanel customerPanel=new CustomerPanel();
		frame.getContentPane().add(customerPanel,BorderLayout.CENTER);
		frame.setUndecorated(true);
		frame.setVisible(true);
	}
}
