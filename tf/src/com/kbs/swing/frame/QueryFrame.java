package com.kbs.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.consume.service.ConsumeService;

/**
*统计查询
*/
public class QueryFrame extends javax.swing.JFrame {
	private JPanel panel;
	private JLabel label_date;
	private JTextField txt_date;
	private JLabel label_enddate;
	private JTextField txt_enddate;
	private JLabel label_time;
	private JButton btn_serach;
	private JComboBox cbx_pricerId;
	private JLabel label_pricerId;
	private JScrollPane scroll_table;
	private JTable table_info;
	private JComboBox comb_time;
	private MainFrame mainFrame;
	private MetaDataBO metaDataBO=new MetaDataBO();

	public QueryFrame(MainFrame mainFrame) {
		super();
		this.mainFrame=mainFrame;
		initGUI();
		searchConsumeToTable();
	}
	
	private void initGUI() {
		try {
			this.setTitle("查询");
			this.addWindowListener(new WindowAdapter()
		      {
		        public void windowIconified(WindowEvent arg0)
		        {
		          QueryFrame.this.setExtendedState(6);
		        }
		      });
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			{
				panel = new JPanel();
				getContentPane().add(panel, "Center");
				panel.setLayout(null);
				panel.setPreferredSize(new Dimension(1024, 638));
				panel.setBounds(0, 0, 892, 551);
				{
					label_date = new JLabel();
					panel.add(label_date);
					label_date.setText("开始时间");
					label_date.setBounds(87, 38, 72, 36);
					
				}
				{
					txt_date = new JTextField();
					panel.add(txt_date);
					txt_date.setBounds(150, 44, 113, 24);
					txt_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				}
				{
					 label_enddate = new JLabel();
					 panel.add(label_enddate);
					 label_enddate.setText("结束时间");
					 label_enddate.setBounds(298, 43, 54, 26);
				 }
				{
					 txt_enddate = new JTextField();
					 JLabel endLabel=new JLabel("<html><font color=red >*</font></html>"); 
					 endLabel.setBounds(471, 49, 15, 15);
					 panel.add(txt_enddate);
					 panel.add(endLabel);
					 txt_enddate.setBounds(348, 45, 113, 24);
					 txt_enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				 }
				
			}
			{
				 label_time = new JLabel();
				 panel.add(label_time);
				 label_time.setText("餐次");
				 label_time.setBounds(505, 43, 54, 26);
			 }
//			CalendarPanel p = new CalendarPanel(txt_date, "yyyy-MM-dd");  
//			CalendarPanel p1 = new CalendarPanel(txt_enddate, "yyyy-MM-dd");  
//			getContentPane().add(p);
//			p.initCalendarPanel(); 
//			panel.add(p);  
//			getContentPane().add(p1);
//			p1.initCalendarPanel(); 
//			panel.add(p1); 
			 {
				 
				 ComboBoxModel comb_timeModel = 
					 new DefaultComboBoxModel(getMealtimes().toArray());
				 comb_time = new JComboBox();
				 panel.add(comb_time);
				 comb_time.setModel(comb_timeModel);
				 comb_time.setBounds(541, 44, 100, 24);
			 }
			 {
				 btn_serach = new JButton();
				 
				 panel.add(btn_serach);
				 btn_serach.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						searchConsumeToTable();
					}
				});
				 btn_serach.setText("查询");
				 btn_serach.setBounds(842, 41, 64, 30);
			 }
			 {
				 scroll_table = new JScrollPane();
				 panel.add(scroll_table);
				 scroll_table.setBounds(87, 84, 819, 450);
				 {
					 table_info = new JTable();
					 String[] columnNames={"时间","订单号","消费金额","卡号","原额","操作人","餐次"};
					 DefaultTableModel tableModel = new DefaultTableModel(null,columnNames);
					 table_info.setModel(tableModel);
					 DefaultTableCellRenderer dc = 
							new DefaultTableCellRenderer();
					 //设置包装器对齐方式
					 dc.setHorizontalAlignment(SwingConstants.CENTER);
						
					 //将样式包装器设置到JTable对象
					 table_info.setDefaultRenderer(Object.class, dc);
					 table_info.getTableHeader().setFont(new Font("宋体",Font.BOLD,18));
					 scroll_table.setViewportView(table_info);
					 table_info.setBounds(37, 70, 771, 387);
					 table_info.getColumnModel().getColumn(0).setPreferredWidth((table_info.getWidth()/7)+80);
//					 table_info.setPreferredSize(new java.awt.Dimension(768, 961));
				 }
			 }
				
			 JLabel label = new JLabel("<html><font color=red >*</font></html>");
			 label.setBounds(273, 49, 15, 15);
			 panel.add(label);
			 panel.add(getLabel_pricerId());
			 panel.add(getCbx_pricerId());
			getContentPane().add(panel, BorderLayout.CENTER);
//			pack();
//			this.setPreferredSize(new Dimension(1024, 768));
			this.setUndecorated(true); 
	        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
	        this.setSize(d.width, d.height);  
		} catch (Exception e) {
			e.printStackTrace();
		}

		 

	}
	public ArrayList<String> getMealtimes(){
		ArrayList<String> resultlist=new ArrayList<String>();
		resultlist.add("无");
		List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_mealtimes");
		for(Map mealMap:list){
			resultlist.add(mealMap.get("MT_NAME").toString());
		}
		return resultlist;
	}
	
	public ArrayList<String> getPricerId(){
		ArrayList<String> resultlist=new ArrayList<String>();
		resultlist.add("无");
		List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_chiptype where CY_VALUE='121'");
		for(Map mealMap:list){
			resultlist.add(mealMap.get("CY_PRICE").toString());
		}
		return resultlist;
	}
	
	private JLabel getLabel_pricerId() {
		if(label_pricerId == null) {
			label_pricerId = new JLabel();
			label_pricerId.setText("定价器编号");
			label_pricerId.setBounds(669, 48, 73, 17);
		}
		return label_pricerId;
	}
	
	private JComboBox getCbx_pricerId() {
		if(cbx_pricerId == null) {
			ComboBoxModel cbx_pricerIdModel = 
				new DefaultComboBoxModel(getPricerId().toArray());
			cbx_pricerId = new JComboBox();
			cbx_pricerId.setModel(cbx_pricerIdModel);
			cbx_pricerId.setBounds(742, 44, 62, 24);
		}
		return cbx_pricerId;
	}

	public void searchConsumeToTable(){
		((DefaultTableModel)table_info.getModel()).setRowCount(0);
//		table_info.updateUI();
		DefaultTableModel dtm=(DefaultTableModel)table_info.getModel();
		ConsumeService consumeService=new ConsumeService();
		List<Map<String,Object>> consumList=consumeService.queryConsumeByTime(txt_date.getText(), txt_enddate.getText(),comb_time.getSelectedItem().toString(),cbx_pricerId.getSelectedItem().toString().trim());
		 for (Map<String, Object> map : consumList) {
	            Vector v = new Vector();
	            v.add(map.get("CREATETIME"));
	            v.add(map.get("COR_ID"));
	            v.add(map.get("CCR_MONEY"));
	            v.add(map.get("IC_ID"));
	            v.add(map.get("CCR_ORIGINALAMOUNT"));
	            v.add(map.get("UID"));
	            v.add(map.get("MT_NAME"));
	            dtm.addRow(v);
	        }
		 table_info.setModel(dtm);
	}
	
}
