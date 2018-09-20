package com.kbs.swing.panel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.alibaba.fastjson.JSONObject;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class GgPanel extends JDialog {
	public GgPanel() {
	}
	JSONObject newJSON = new JSONObject();
	JSONObject oldCacheJSON=new JSONObject();
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
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
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 705, 1024, 24);
		getContentPane().add(panel);
		
		JLabel label_1 = new JLabel("深圳市科拜斯物联网科技有限公司");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		panel.add(label_1);
		
//		JPanel panel = new JPanel();
		
//	
		Browser browser = new Browser();
//		
       BrowserView view = new BrowserView(browser);
//       view.setBounds(0, 0, 1024, 768);
//        view.setPreferredSize(new Dimension(705, 768));
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.add(view, BorderLayout.CENTER);
//        frame.setSize(700, 500);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//       browser.
//        browser.loadURL("http://www.junglevents.com/");
       browser.loadURL("http://www.baidu.com/");
        view.setPreferredSize(new Dimension(dimension.width,dimension.height));
        getContentPane().add(view);
//	        panel.add(view);
	        

	       
//		getContentPane().add(view);
		
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		// TODO Auto-generated method stub
//		ImageIcon img=new ImageIcon(CustomerPanel.class.getResource("/images/customer.jpg"));
//		g.drawImage(img.getImage(), 0, 0,this.getWidth(),this.getHeight(), null);
////		g.drawLine(0, 0, 500, 500);
//		System.out.println("------------");
//	}
	
	
}
