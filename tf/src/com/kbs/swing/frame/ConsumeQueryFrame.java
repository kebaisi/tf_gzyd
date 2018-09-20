package com.kbs.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.jdesktop.swingx.JXDatePicker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.swing.CommonInterface;
import com.kbs.swing.MyTableModel;
import com.kbs.swing.panel.CalendarPanel;
import com.kbs.util.DataConvertUtil;
import com.kbs.util.ExcelUtil;
import com.kbs.util.HttpClient;
import com.kbs.util.SuccessPrint;
import com.kbs.util.TF;

public class ConsumeQueryFrame extends JPanel implements CommonInterface,FocusListener {
	private JTable table;
	private TableModel table_infModel;
	private TableModel table_lossModel;
	private TableModel table_commodityModel;
	private JTextField textField;
	private JLabel label_date;
	private JXDatePicker txt_date;
	private JLabel label_enddate;
	private JXDatePicker txt_enddate;
	private JPanel panel_right_1;
	private JPanel panel_right_2;
	private JPanel panel_right_3;
	private JPanel loss_chip_panel;
	private JPanel commodity_count_panel;
	private MetaDataBO metaDataBO=new MetaDataBO();
	private JXDatePicker txt_startdate_2;
	private JXDatePicker txt_enddate_2;
	private JTable table_total;
	private TableModel table_infModel_total;
	private TableModel table_infModel_details;
	private JTable loss_table;
	private JTable commodity_table;
	private JComboBox cbx_user;
	private JXDatePicker txt_startdate_3;
	private JXDatePicker txt_enddate_3;
	private JTable table_details;
	private JComboBox cbx_mealName;
	private JComboBox cbx_userName;
	private JComboBox cbx_pay;
	private Map<String,Integer> pageMap;
	private List<Vector> detailslist=new ArrayList();
	private JLabel label_current;
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private MainFrame mainFrame=null;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private JLabel label_5;
	private JLabel lblStarDate;
	private JLabel label_7;
	private JLabel lblEndDate;
	private JLabel label_9;
	private JLabel label_10;
	private JLabel lblNewLabel_2;
	private JLabel label_11;
	private JLabel label_12;
	private JLabel label_13;
	private JLabel label_14;
	private JLabel label_15;
	private JLabel label_16;
	private JLabel label_17;
	private JLabel label_18;
	private JLabel lbl1Money;
	private JLabel lbl1card;
	private JLabel lbl1SumPerson;
	private JLabel lbl1MoenySum;
	private JLabel lbl2Money;
	private JLabel lbl3Money;
	private JLabel lbl2card;
	private JLabel lbl3card;
	private JLabel lbl2SumPerson;
	private JLabel lbl2MoenySum;
	private JLabel lbl3SumPerson;
	private JLabel lbl3MoenySum;
	private JLabel label_31;
	private JLabel label_32;
	private JLabel label_33;
	private JLabel label_34;
	private JLabel label_35;
	private JLabel lblMoneySum;
	private JLabel lblCardSum;
	private JLabel lblPersonSum;
	private JLabel lbltotalMoeny;
	private JLabel label_40;
	private JLabel label_41;
	private JLabel label_42;
	private JLabel lblcuDate;
	private JLabel lblMachineId;
	private JLabel label_date1;
	private JTextField txt_date1;
	private JLabel label_enddate1;
	private JTextField txt_enddate1;
	private JButton btn_serach1;
	private JPanel sumPanel;
	private JTextField txt_startdate_4;
	private JTextField txt_enddate_4;
	private JTable table_4;
	private MyTableModel table_infModel_4;
	private JPanel panel_right_5;
	private JSplitPane splitPane;
	private JPanel panel_left;
	private HttpClient httpClient=new HttpClient();
	private NumberInputPnl numInputPnl;
	private JLabel lbl4Money;
	private JLabel lbl4card;
	private JLabel lbl4SumPerson;
	private JLabel lbl4MoenySum;
	private JLabel lbl5Money;
	private JLabel lbl5card;
	private JLabel lbl5SumPerson;
	private JLabel lbl5MoenySum;
	private JTextField txt_go;
	private JButton btn_go;
	private JXDatePicker txt_loss_starttime;
	private JXDatePicker txt_loss_endtime;
	private JXDatePicker txt_commodity_starttime;
	private JXDatePicker txt_commodity_endtime;
	/**
	 * Create the frame.
	 */
	public ConsumeQueryFrame(MainFrame mainFrame) {
		initGUI();
//		txt_date.addFocusListener(this);
////		txt_date1.addFocusListener(this);
//		txt_enddate.addFocusListener(this);
////		txt_enddate1.addFocusListener(this);
//		txt_enddate_2.addFocusListener(this);
//		txt_enddate_3.addFocusListener(this);
//		txt_enddate_4.addFocusListener(this);
//		txt_startdate_2.addFocusListener(this);
//		txt_startdate_3.addFocusListener(this);
//		txt_startdate_4.addFocusListener(this);
		this.mainFrame=mainFrame;
		pageMap=new HashMap();
		pageMap.put("pageSize", 15);
		pageMap.put("currentPage", 1);
		pageMap.put("pageNum", 0);
		pageMap.put("total", 0);
//		List vList=queryForConsume();
//		fillTable((DefaultTableModel)table_infModel,vList);
		splitPane.setDividerLocation(150);
		splitPane.setLeftComponent(panel_left);
		
		JButton button = new JButton("漏单");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splitPane.setDividerLocation(150);
				splitPane.setLeftComponent(panel_left);
				splitPane.setRightComponent(loss_chip_panel);
//				fillMealtimes(cbx_meal);
//				fillUser(cbx_user);
				List list=queryLossChip();
				fillTable((DefaultTableModel)table_lossModel,list);
//				searchConsumeSum();
				
			}
		});
		button.setBounds(10, 214, 124, 46);
		panel_left.add(button);
		
		JButton btn_commodity_count = new JButton("商品零售统计");
		btn_commodity_count.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splitPane.setDividerLocation(150);
				splitPane.setLeftComponent(panel_left);
				splitPane.setRightComponent(commodity_count_panel);
//				fillMealtimes(cbx_meal);
//				fillUser(cbx_user);
				List list=queryCommodity();
				fillTable((DefaultTableModel)table_commodityModel,list);
//				searchConsumeSum();
			}
		});
		btn_commodity_count.setBounds(10, 280, 124, 46);
		panel_left.add(btn_commodity_count);
		splitPane.setRightComponent(panel_right_1);
		
		txt_go = new JTextField();
		txt_go.setBounds(498, 611, 63, 35);
		panel_right_3.add(txt_go);
		txt_go.setColumns(10);
		
		btn_go = new JButton("GO");
		btn_go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DataConvertUtil.checkIsPageNum(txt_go.getText())){
					if(Integer.parseInt(txt_go.getText())>pageMap.get("pageNum")){
						JOptionPane.showMessageDialog(ConsumeQueryFrame.this,"超过页码范围");
						return;
					}else{
						pageMap.put("currentPage", Integer.parseInt(txt_go.getText()));
						List<Vector> list=detailslist.subList((pageMap.get("currentPage")-1)*pageMap.get("pageSize"), pageMap.get("currentPage")*pageMap.get("pageSize")>detailslist.size()?detailslist.size():pageMap.get("currentPage")*pageMap.get("pageSize"));
						fillTable((DefaultTableModel)table_infModel_details, list);
						label_current.setText(pageMap.get("currentPage")+"/"+pageMap.get("pageNum"));
					}
				}else{
					JOptionPane.showMessageDialog(ConsumeQueryFrame.this,"请输入正确的页码");
					return;
				}
			}
		});
		btn_go.setBounds(571, 611, 78, 35);
		panel_right_3.add(btn_go);
		
		JButton btn_printAll = new JButton();
		btn_printAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject allConsumeJson=new JSONObject();
				JSONObject tempJson=getPrintAllInfo();
				List<String> nameList=new ArrayList<String>();
				if(tempJson==null||tempJson.getJSONArray("data")==null||tempJson.getJSONArray("data").size()<1){
					return;
				}else{
					for(int i=0;i<tempJson.getJSONArray("data").size();i++){
						if(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME").equals("早餐")){
							allConsumeJson.put("早餐", tempJson.getJSONArray("data").getJSONObject(i));
						}else if(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME").equals("中餐")){
							allConsumeJson.put("午餐", tempJson.getJSONArray("data").getJSONObject(i));
						}else if(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME").equals("晚餐")){
							allConsumeJson.put("晚餐", tempJson.getJSONArray("data").getJSONObject(i));
						}else if(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME").equals("夜宵")){
							allConsumeJson.put("夜宵", tempJson.getJSONArray("data").getJSONObject(i));
						}else if(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME").equals("拓展")){
							allConsumeJson.put("拓展", tempJson.getJSONArray("data").getJSONObject(i));
						}
						allConsumeJson.put("cash", allConsumeJson.getFloatValue("cash")+tempJson.getJSONArray("data").getJSONObject(i).getFloatValue("cash_consume"));
						allConsumeJson.put("card", allConsumeJson.getFloatValue("card")+tempJson.getJSONArray("data").getJSONObject(i).getFloatValue("card_consume"));
						allConsumeJson.put("num", allConsumeJson.getIntValue("num")+tempJson.getJSONArray("data").getJSONObject(i).getIntValue("all_num"));
						allConsumeJson.put("sum", allConsumeJson.getFloatValue("sum")+tempJson.getJSONArray("data").getJSONObject(i).getFloatValue("all_consume"));
						nameList.add(tempJson.getJSONArray("data").getJSONObject(i).getString("MT_NAME"));
					}
					if(!nameList.contains("早餐")){
						allConsumeJson.put("早餐", new JSONObject());
					}
					if(!nameList.contains("中餐")){
						allConsumeJson.put("午餐", new JSONObject());
					}
					if(!nameList.contains("晚餐")){
						allConsumeJson.put("晚餐", new JSONObject());
					}
					if(!nameList.contains("夜宵")){
						allConsumeJson.put("夜宵", new JSONObject());
					}
					if(!nameList.contains("拓展")){
						allConsumeJson.put("拓展", new JSONObject());
					}

					//打印操作
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date=sdf.format(new Date());
			        StringBuffer sb = new StringBuffer();
			        try {
						sb.append("\r\n\r\n       "+new String(TF.dsJson.getString("merchantName").getBytes("ISO-8859-1"),"gb2312")+"结算报表");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        sb.append("\r\n\r\n时间: "+date);
			        sb.append("\r\n\r\n--------------------------------");
			        sb.append("\r\n\r\n起始时间 : "+sdf.format(txt_startdate_2.getDate())+" 00:00:00");
			        sb.append("\r\n\r\n截至时间 : "+sdf.format(txt_enddate_2.getDate())+" 23:59:59");
			        sb.append("\r\n\r\n--------------------------------");
			        sb.append("\r\n\r\n明细：");
			        sb.append("\r\n--------------------------------");
			        sb.append("\r\n\r\n餐次 现金   刷卡   人数    小计");
			        sb.append("\r\n\r\n早餐 "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("早餐").getFloatValue("cash_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("早餐").getFloatValue("card_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("早餐").getIntValue("all_num")+"")+" "+allConsumeJson.getJSONObject("早餐").getFloatValue("all_consume")+"");
			        sb.append("\r\n\r\n午餐 "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("午餐").getFloatValue("cash_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("午餐").getFloatValue("card_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("午餐").getIntValue("all_num")+"")+" "+allConsumeJson.getJSONObject("午餐").getFloatValue("all_consume")+"");
			        sb.append("\r\n\r\n晚餐 "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("晚餐").getFloatValue("cash_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("晚餐").getFloatValue("card_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("晚餐").getIntValue("all_num")+"")+" "+allConsumeJson.getJSONObject("晚餐").getFloatValue("all_consume")+"");
			        sb.append("\r\n\r\n夜宵 "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("夜宵").getFloatValue("cash_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("夜宵").getFloatValue("card_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("夜宵").getIntValue("all_num")+"")+" "+allConsumeJson.getJSONObject("夜宵").getFloatValue("all_consume")+"");
			        sb.append("\r\n\r\n拓展 "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("拓展").getFloatValue("cash_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("拓展").getFloatValue("card_consume")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getJSONObject("拓展").getIntValue("all_num")+"")+" "+allConsumeJson.getJSONObject("拓展").getFloatValue("all_consume")+"");
			        sb.append("\r\n\r\n--------------------------------");
			        sb.append("\r\n\r\n统计: 现金  刷卡   人数    总计");
			        sb.append("\r\n\r\n     "+DataConvertUtil.StrComple(allConsumeJson.getFloatValue("cash")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getFloatValue("card")+"")+" "+DataConvertUtil.StrComple(allConsumeJson.getIntValue("num")+"")+" "+allConsumeJson.getFloatValue("sum")+"");
			        sb.append("\r\n\r\n\r\n--------------------------------");
			        sb.append("  收银员：          审核：\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
			        String printStr = sb.toString();
			    	SuccessPrint successPrint = new SuccessPrint(printStr);
			    	successPrint.printMsg();
				
				}
			}
		});
//		btn_printAll.setVisible(false);
		btn_printAll.setText("打印全部");
		btn_printAll.setBounds(736, 31, 93, 30);
		panel_right_2.add(btn_printAll);
		List vList=queryForConsume();
		fillTable((DefaultTableModel)table_infModel,vList);
//		searchConsumeSum();
	}
	
	public void initGUI(){
		TF.dsJson.put("title", "统计");
		BorderLayout thisLayout = new BorderLayout();
		setLayout(thisLayout);
		JPanel panel = new JPanel();
//		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		add(panel);
		this.setSize(1024,768);
		splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, 1016, 688);
		splitPane.setVisible(true);
		panel.add(splitPane);
		
		panel_left = new JPanel();
		splitPane.setDividerLocation(150);
		splitPane.setLeftComponent(panel_left);
		panel_left.setLayout(null);
		
		panel_right_1 = new JPanel();
//		panel_right_1.setVisible(false);
//		splitPane.setRightComponent(panel_right_1);
		panel_right_1.setLayout(null);
		
		table = new JTable();
		{
			table_infModel = 
				new MyTableModel(
						new String[][] {  },
						new String[] { "日期", "现金","支付宝","微信","会员卡","免单","小计"});
			DefaultTableCellRenderer dc = 
				new DefaultTableCellRenderer();
			//设置包装器对齐方式
			dc.setHorizontalAlignment(SwingConstants.CENTER);
			table.setDefaultRenderer(Object.class, dc);
		}
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 103, 705, 552);
		panel_right_1.add(scrollPane);
		
		//将样式包装器设置到JTable对象
		table.setModel(table_infModel);
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		table.setFont(new Font("微软雅黑",Font.PLAIN,18));
		scrollPane.setViewportView(table);
		
		{
			label_date = new JLabel();
			panel_right_1.add(label_date);
			label_date.setText("开始时间");
			label_date.setBounds(64, 44, 72, 26);
			
		}
		{
//			txt_date = new JTextField();
//			panel_right_1.add(txt_date);
//			txt_date.setBounds(125, 46, 113, 24);
//			txt_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			
			txt_date = new JXDatePicker();
			txt_date.setBounds(125, 46, 128, 24);
			panel_right_1.add(txt_date);
			txt_date.setFormats(sdf);
		}
		{
			 label_enddate = new JLabel();
			 panel_right_1.add(label_enddate);
			 label_enddate.setText("结束时间");
			 label_enddate.setBounds(304, 44, 72, 26);
		 }
//		txt_enddate = new JTextField();
//		panel_right_1.add(txt_enddate);
//		txt_enddate.setBounds(368, 46, 113, 24);
//		txt_enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		txt_enddate = new JXDatePicker();
		txt_enddate.setBounds(368, 46, 128, 24);
		panel_right_1.add(txt_enddate);
		txt_enddate.setFormats(sdf);
		{
			 JLabel endLabel=new JLabel("<html><font color=red >*</font></html>"); 
			 panel_right_1.add(endLabel);
			 endLabel.setBounds(263, 43, 20, 24);
		 }
		
		JLabel label = new JLabel("<html><font color=red >*</font></html>");
		label.setBounds(505, 43, 20, 24);
		panel_right_1.add(label);
		
		JButton btn_search = new JButton("查询");
		btn_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List vList=queryForConsume();
				fillTable((DefaultTableModel)table_infModel,vList);
			}
		});
		btn_search.setBounds(544, 46, 93, 23);
		panel_right_1.add(btn_search);
		
		
		
		loss_chip_panel = new JPanel();
//		loss_chip_panel.setVisible(false);
//		splitPane.setRightComponent(loss_chip_panel);
		loss_chip_panel.setLayout(null);
		
		loss_table = new JTable();
		{
			table_lossModel = 
				new MyTableModel(
						new String[][] {  },
						new String[] { "日期","未支付 |金额"});
			DefaultTableCellRenderer dc = 
				new DefaultTableCellRenderer();
			//设置包装器对齐方式
			dc.setHorizontalAlignment(SwingConstants.CENTER);
			loss_table.setDefaultRenderer(Object.class, dc);
		}
		JScrollPane loss_scrollPane = new JScrollPane();
		loss_scrollPane.setBounds(64, 103, 705, 552);
		loss_chip_panel.add(loss_scrollPane);
		
		//将样式包装器设置到JTable对象
		loss_table.setModel(table_lossModel);
		loss_table.setRowHeight(30);
		loss_table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		loss_table.setFont(new Font("微软雅黑",Font.PLAIN,18));
		loss_scrollPane.setViewportView(loss_table);
		
		{
			JLabel lbl_loss_starttime = new JLabel();
			loss_chip_panel.add(lbl_loss_starttime);
			lbl_loss_starttime.setText("开始时间");
			lbl_loss_starttime.setBounds(64, 44, 72, 26);
			
		}
		{
//			txt_date = new JTextField();
//			panel_right_1.add(txt_date);
//			txt_date.setBounds(125, 46, 113, 24);
//			txt_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			
			txt_loss_starttime = new JXDatePicker();
			txt_loss_starttime.setBounds(125, 46, 128, 24);
			loss_chip_panel.add(txt_loss_starttime);
			txt_loss_starttime.setFormats(sdf);
		}
		{
			JLabel lbl_loss_endtime = new JLabel();
			loss_chip_panel.add(lbl_loss_endtime);
			lbl_loss_endtime.setText("结束时间");
			lbl_loss_endtime.setBounds(304, 44, 72, 26);
		 }
//		txt_enddate = new JTextField();
//		panel_right_1.add(txt_enddate);
//		txt_enddate.setBounds(368, 46, 113, 24);
//		txt_enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		txt_loss_endtime = new JXDatePicker();
		txt_loss_endtime.setBounds(368, 46, 128, 24);
		txt_loss_endtime.setFormats(sdf);
		loss_chip_panel.add(txt_loss_endtime);
		
		
		JButton btn_loss_search = new JButton("查询");
		btn_loss_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List vList=queryLossChip();
				fillTable((DefaultTableModel)table_lossModel,vList);
			}
		});
		btn_loss_search.setBounds(544, 46, 93, 23);
		loss_chip_panel.add(btn_loss_search);
		
		JButton export_loss_view = new JButton("导出明细");
		export_loss_view.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser dlg = new JFileChooser();
				dlg.setDialogTitle("Open JPEG file");
//				dlg.setName("2016-09-28.xls");
				dlg.setSelectedFile(new File(sdf.format(txt_loss_starttime.getDate())+"到"+sdf.format(txt_loss_endtime.getDate())+"漏单明细记录.xls"));
//				int result = dlg.showOpenDialog(this);  // 打开"打开文件"对话框
				 int result = dlg.showSaveDialog(ConsumeQueryFrame.this);  // 打"开保存文件"对话框
				if (result == JFileChooser.APPROVE_OPTION) {
				File file = dlg.getSelectedFile();
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = ExcelUtil.createSheet(workbook, "漏单明细");
//				HSSFCellStyle headStyle = ExcelUtil.createCellStyle(workbook, backgroundColor, foregroundColor, halign, font)
				HSSFRow headRow = ExcelUtil.createRow(sheet, 0, 500);
				HSSFFont headFont = workbook.createFont();
				headFont.setFontName("微软雅黑");
//				headFont.setColor(HSSFColor.BLACK.index);
				headFont.setFontHeight((short)200);
				headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				CellStyle headstyle = workbook.createCellStyle();
				headstyle.setFont(headFont);
				HSSFCell headCell = headRow.createCell(0);
				CellStyle bodyStyle = workbook.createCellStyle();
				HSSFFont bodyFont = workbook.createFont();
				bodyFont.setFontName("微软雅黑");
				bodyFont.setFontHeight((short)180);
				bodyStyle.setFont(bodyFont);
				headCell.setCellStyle(headstyle);
				headCell.setCellValue("时间");
				
				 headCell = headRow.createCell(1);
				 headCell.setCellStyle(headstyle);
				headCell.setCellValue("餐具ID");
				 headCell = headRow.createCell(2);
				 headCell.setCellStyle(headstyle);
				headCell.setCellValue("价格");
				 headCell = headRow.createCell(3);
				 headCell.setCellStyle(headstyle);
				headCell.setCellValue("是否已支付");
				List<Map<String, Object>> list = metaDataBO.queryForListMap("SELECT * FROM TF_LOSS_CHIP_RECORD WHERE IS_CONSUME='0' and CREATETIME BETWEEN '"+sdf.format(txt_loss_starttime.getDate())+" 00:00:00' and '"+sdf.format(txt_loss_endtime.getDate())+" 23:59:59'");
				for(int i=0;i<list.size();i++){
					HSSFRow bodyRow = ExcelUtil.createRow(sheet, (i+1), 400);
					HSSFCell bodyCell = bodyRow.createCell(0);
					bodyCell.setCellStyle(bodyStyle);
					bodyCell.setCellValue(list.get(i).get("CREATETIME").toString());
					 bodyCell = bodyRow.createCell(1);
					 bodyCell.setCellStyle(bodyStyle);
					bodyCell.setCellValue(list.get(i).get("CHIP_NO").toString());
					 bodyCell = bodyRow.createCell(2);
					 bodyCell.setCellStyle(bodyStyle);
					bodyCell.setCellValue(list.get(i).get("PRICE").toString());
					 bodyCell = bodyRow.createCell(3);
					if(list.get(i).get("IS_CONSUME").toString().equals("0")){
						CellStyle no_cosume_style = workbook.createCellStyle();
						HSSFFont no_consume_font = workbook.createFont();
						no_consume_font.setFontName("微软雅黑");
						no_consume_font.setFontHeight((short)180);
						no_consume_font.setColor(HSSFFont.COLOR_RED);
						no_cosume_style.setFont(no_consume_font);
						bodyCell.setCellStyle(no_cosume_style);
						bodyCell.setCellValue("未正常支付");
					}else{
						 bodyCell.setCellStyle(bodyStyle);
							bodyCell.setCellValue("正常支付");
					}
				}
				ExcelUtil.writeWorkbook(workbook, file.getPath());
				}
//				 TableModel model = loss_table.getModel();
//			        FileWriter out = null;
//					try {
//						out = new FileWriter( new File("D:\\123.xls"));
//						 for(int i=0; i < model.getColumnCount(); i++) {
//					            out.write(model.getColumnName(i) + "/t");
//					        }
//					        out.write("/n");
//					        for(int i=0; i< model.getRowCount(); i++) {
//					            for(int j=0; j < model.getColumnCount(); j++) {
//					                out.write(model.getValueAt(i,j).toString()+"/t");
//					            }
//					            out.write("/n");
//					        }
//					        out.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//			        
//			       
//			       
				
			}
//			}
		});
		export_loss_view.setBounds(654, 46, 93, 23);
		loss_chip_panel.add(export_loss_view);
		
		
		
		commodity_count_panel = new JPanel();
//		loss_chip_panel.setVisible(false);
//		splitPane.setRightComponent(loss_chip_panel);
		commodity_count_panel.setLayout(null);
		
		commodity_table = new JTable();
		{
			table_commodityModel = 
				new MyTableModel(
						new String[][] {  },
						new String[] { "商品名称", "销售总量","单价","销售总额"});
			DefaultTableCellRenderer dc = 
				new DefaultTableCellRenderer();
			//设置包装器对齐方式
			dc.setHorizontalAlignment(SwingConstants.CENTER);
			commodity_table.setDefaultRenderer(Object.class, dc);
		}
		JScrollPane commodity_scrollPane = new JScrollPane();
		commodity_scrollPane.setBounds(64, 103, 705, 552);
		commodity_count_panel.add(commodity_scrollPane);
		
		//将样式包装器设置到JTable对象
		commodity_table.setModel(table_commodityModel);
		commodity_table.setRowHeight(30);
		commodity_table.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		commodity_table.setFont(new Font("微软雅黑",Font.PLAIN,18));
		commodity_scrollPane.setViewportView(commodity_table);
		
		{
			JLabel lbl_commodity_starttime = new JLabel();
			commodity_count_panel.add(lbl_commodity_starttime);
			lbl_commodity_starttime.setText("开始时间");
			lbl_commodity_starttime.setBounds(64, 44, 72, 26);
			
		}
		{
//			txt_date = new JTextField();
//			panel_right_1.add(txt_date);
//			txt_date.setBounds(125, 46, 113, 24);
//			txt_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			
			txt_commodity_starttime = new JXDatePicker();
			txt_commodity_starttime.setBounds(125, 46, 128, 24);
			commodity_count_panel.add(txt_commodity_starttime);
			txt_commodity_starttime.setFormats(sdf);
		}
		{
			JLabel lbl_loss_endtime = new JLabel();
			commodity_count_panel.add(lbl_loss_endtime);
			lbl_loss_endtime.setText("结束时间");
			lbl_loss_endtime.setBounds(304, 44, 72, 26);
		 }
//		txt_enddate = new JTextField();
//		panel_right_1.add(txt_enddate);
//		txt_enddate.setBounds(368, 46, 113, 24);
//		txt_enddate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		txt_commodity_endtime = new JXDatePicker();
		txt_commodity_endtime.setBounds(368, 46, 128, 24);
		txt_commodity_endtime.setFormats(sdf);
		commodity_count_panel.add(txt_commodity_endtime);
		
		
		JButton btn_commodity_search = new JButton("查询");
		btn_commodity_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List vList=queryCommodity();
				fillTable((DefaultTableModel)table_commodityModel,vList);
			}
		});
		btn_commodity_search.setBounds(544, 46, 93, 23);
		commodity_count_panel.add(btn_commodity_search);
		
		
		
		
		
		
		
		
		
		
		
		
		panel_right_2 = new JPanel();
		panel_right_2.setLayout(null);
		panel_right_2.setBounds(0, 0, 860, 602);
//		splitPane.setRightComponent(panel_right_2);
		
		JLabel label_1 = new JLabel("开始日期:");
		label_1.setBounds(38, 37, 73, 18);
		panel_right_2.add(label_1);
		
		txt_startdate_2 = new JXDatePicker();
		txt_startdate_2.setBounds(97, 34, 93, 21);
		panel_right_2.add(txt_startdate_2);
		txt_startdate_2.setFormats(sdf);
//		txt_startdate_2.setColumns(10);
//		txt_startdate_2.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		JLabel label_2 = new JLabel("结束日期:");
		label_2.setBounds(218, 37, 73, 15);
		panel_right_2.add(label_2);
		
		txt_enddate_2 = new JXDatePicker();
		txt_enddate_2.setBounds(275, 34, 93, 21);
		panel_right_2.add(txt_enddate_2);
		txt_enddate_2.setFormats(sdf);
//		txt_enddate_2.setColumns(10);
//		txt_enddate_2.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		JLabel label_4 = new JLabel("营业员:");
		label_4.setBounds(423, 37, 54, 15);
		panel_right_2.add(label_4);
		
		cbx_user = new JComboBox();
		cbx_user.setBounds(473, 34, 93, 21);
		panel_right_2.add(cbx_user);
		
		JButton btn_search_2 = new JButton("查询");
		btn_search_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List list=queryForTotal();
				fillTable((DefaultTableModel)table_infModel_total,list);
				searchConsumeSum();
			}
		});
		btn_search_2.setBounds(576, 32, 73, 29);
		panel_right_2.add(btn_search_2);
		
		JScrollPane scorll_total = new JScrollPane();
		scorll_total.setBounds(38, 95, 765, 555);
		panel_right_2.add(scorll_total);
		
		table_total = new JTable();
			table_infModel_total = 
				new MyTableModel(
						new String[][] {  },
						new String[] {"餐次", "总额|单数", "平均消费|总人数","现金|次数","刷卡|次数"});
			DefaultTableCellRenderer dc = 
				new DefaultTableCellRenderer();
			//设置包装器对齐方式
			dc.setHorizontalAlignment(SwingConstants.CENTER);
			table_total.setDefaultRenderer(Object.class, dc);
		//将样式包装器设置到JTable对象
		table_total.setModel(table_infModel_total);
//		table_total.getColumnModel().getColumn(0).setWidth(50);
		table_total.getTableHeader().setDefaultRenderer(dc);
		table_total.getTableHeader().getColumnModel().getColumn(0).setWidth(50);
		table_total.setRowHeight(60);
		table_total.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		table_total.setFont(new Font("微软雅黑",Font.PLAIN,18));
		scorll_total.setViewportView(table_total);
		
		
		panel_right_3 = new JPanel();
		panel_right_3.setLayout(null);
		panel_right_3.setBounds(0, 0, 860, 602);
//		splitPane.setRightComponent(panel_right_3);
		
		JLabel label_5 = new JLabel("开始时间:");
		label_5.setBounds(28, 13, 70, 15);
		panel_right_3.add(label_5);
		
		txt_startdate_3 = new JXDatePicker();
		txt_startdate_3.setBounds(90, 10, 134, 21);
		txt_startdate_3.setFormats(sdf);
		panel_right_3.add(txt_startdate_3);
//		txt_startdate_3.setColumns(10);
//		txt_startdate_3.setText(sdf.format(new Date())+" 00:00:00");
		
//		JXDatePicker startDate=new JXDatePicker();
//		startDate.setBounds(90, 31, 134, 21);
//		startDate.setFormats(sdf);
//		panel_right_3.add(startDate);
		
		JLabel label_6 = new JLabel("结束时间:");
		label_6.setBounds(262, 13, 70, 15);
		panel_right_3.add(label_6);
		
		txt_enddate_3 = new JXDatePicker();
		txt_enddate_3.setBounds(322, 10, 134, 21);
		txt_enddate_3.setFormats(sdf);
		panel_right_3.add(txt_enddate_3);
//		txt_enddate_3.setColumns(10);
//		txt_enddate_3.setText(sdf.format(new Date())+" 23:59:59");
		
		JLabel label_7 = new JLabel("餐次:");
		label_7.setBounds(480, 13, 54, 15);
		panel_right_3.add(label_7);
		
		cbx_mealName = new JComboBox();
		cbx_mealName.setBounds(521, 10, 63, 21);
		panel_right_3.add(cbx_mealName);
		
		JLabel label_8 = new JLabel("营业员:");
		label_8.setBounds(608, 13, 54, 15);
		panel_right_3.add(label_8);
		
		JLabel label_9 = new JLabel("结帐方式:");
		label_9.setBounds(28, 58, 70, 15);
		panel_right_3.add(label_9);
		
		cbx_pay = new JComboBox();
		cbx_pay.setBounds(90, 55, 70, 21);
		panel_right_3.add(cbx_pay);
		
		cbx_userName = new JComboBox();
		cbx_userName.setBounds(657, 10, 117, 21);
		panel_right_3.add(cbx_userName);
		
		JButton btn_search_3 = new JButton("查询");
		btn_search_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pageMap.put("currentPage", 1);
				detailslist=queryForDetails();
				if(detailslist.size()==0){
					((DefaultTableModel)table_infModel_details).setRowCount(0);
					return;
				}
				List<Vector> list=new ArrayList<>();
				if(detailslist.size()<pageMap.get("pageSize")){
					list=detailslist.subList(0, detailslist.size());
				}else{
					list=detailslist.subList(0, pageMap.get("pageSize"));
				}
				
				fillTable((DefaultTableModel)table_infModel_details, list);
				label_current.setText(pageMap.get("currentPage")+"/"+pageMap.get("pageNum"));
			}
		});
		btn_search_3.setBounds(220, 54, 93, 23);
		panel_right_3.add(btn_search_3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(28, 96, 797, 505);
		panel_right_3.add(scrollPane_1);
		table_details = new JTable();
		{
			table_infModel_details = 
				new MyTableModel(
						new String[][] {  },
						new String[] { "时间", "订单号","数量","订单金额","结帐方式","收银员"});
//			DefaultTableCellRenderer dc = 
//				new DefaultTableCellRenderer();
//			//设置包装器对齐方式
//			dc.setHorizontalAlignment(SwingConstants.CENTER);
			table_details.setDefaultRenderer(Object.class, dc);
		}
		//将样式包装器设置到JTable对象
		table_details.setModel(table_infModel_details);
		table_details.setRowHeight(28);
		table_details.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		table_details.setFont(new Font("微软雅黑",Font.PLAIN,18));
		table_details.getColumnModel().getColumn(0).setPreferredWidth(200);
		scrollPane_1.setViewportView(table_details);
		
		JButton btn_up = new JButton("上一页");
		btn_up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pageMap.get("currentPage")==1){
					JOptionPane.showMessageDialog(ConsumeQueryFrame.this, "当前已是第一页");
					return;
				}
				pageMap.put("currentPage", pageMap.get("currentPage")-1);
				List<Vector> list=detailslist.subList((pageMap.get("currentPage")-1)*pageMap.get("pageSize"), pageMap.get("currentPage")*pageMap.get("pageSize"));
				fillTable((DefaultTableModel)table_infModel_details, list);
				label_current.setText(pageMap.get("currentPage")+"/"+pageMap.get("pageNum"));
			}
		});
		btn_up.setBounds(303, 611, 85, 35);
		panel_right_3.add(btn_up);
		
		JButton btn_down = new JButton("下一页");
		btn_down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(pageMap.get("currentPage")==pageMap.get("pageNum")){
					JOptionPane.showMessageDialog(ConsumeQueryFrame.this, "当前已是最后一页");
					return;
				}
				pageMap.put("currentPage", pageMap.get("currentPage")+1);
				List<Vector> list=detailslist.subList((pageMap.get("currentPage")-1)*pageMap.get("pageSize"), (pageMap.get("currentPage")*pageMap.get("pageSize"))>pageMap.get("total")?pageMap.get("total"):(pageMap.get("currentPage")*pageMap.get("pageSize")));
				fillTable((DefaultTableModel)table_infModel_details, list);
				label_current.setText(pageMap.get("currentPage")+"/"+pageMap.get("pageNum"));
			}
		});
		btn_down.setBounds(398, 611, 85, 35);
		panel_right_3.add(btn_down);
		
		label_current = new JLabel("");
		label_current.setBounds(248, 615, 45, 31);
		label_current.setHorizontalAlignment(JLabel.CENTER);
		panel_right_3.add(label_current);
		
		JButton btn_consumeBasicInfo = new JButton("概况");
		btn_consumeBasicInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splitPane.setDividerLocation(150);
				splitPane.setLeftComponent(panel_left);
				splitPane.setRightComponent(panel_right_1);
				List vList=queryForConsume();
				System.out.println(JSON.toJSONString("------"+vList));
				fillTable((DefaultTableModel)table_infModel,vList);
			}
		});
		btn_consumeBasicInfo.setBounds(10, 10, 124, 46);
		panel_left.add(btn_consumeBasicInfo);
		
		JButton btn_consumeTotal = new JButton("统计");
		btn_consumeTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				splitPane.setDividerLocation(150);
				splitPane.setLeftComponent(panel_left);
				splitPane.setRightComponent(panel_right_2);
//				fillMealtimes(cbx_meal);
				fillUser(cbx_user);
				List list=queryForTotal();
				fillTable((DefaultTableModel)table_infModel_total,list);
				searchConsumeSum();
			}
		});
		btn_consumeTotal.setBounds(10, 77, 124, 46);
		panel_left.add(btn_consumeTotal);
		
		JButton btn_consumeDetails = new JButton("明细");
		btn_consumeDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				splitPane.setDividerLocation(150);
				splitPane.setLeftComponent(panel_left);
				splitPane.setRightComponent(panel_right_3);
				fillMealtimes(cbx_mealName);
				fillUser(cbx_userName);
				fillPay();
				detailslist=queryForDetails();
				if(detailslist.size()==0){
					((DefaultTableModel)table_infModel_details).setRowCount(0);
					return;
				}
				List<Vector> list=new ArrayList<>();
				if(detailslist.size()<pageMap.get("pageSize")){
					list=detailslist.subList(0, detailslist.size());
				}else{
					list=detailslist.subList(0, pageMap.get("pageSize"));
				}
				
				fillTable((DefaultTableModel)table_infModel_details, list);
				label_current.setText(pageMap.get("currentPage")+"/"+pageMap.get("pageNum"));
			}
		});
		btn_consumeDetails.setBounds(10, 148, 124, 46);
		panel_left.add(btn_consumeDetails);
		final JPanel panel_right_4 = new JPanel();
		panel_right_4.setBounds(0, 0, 10, 10);
//		splitPane.setRightComponent(panel_right_4);
		panel_right_4.setLayout(null);
		
		{
			panel_right_5 = new JPanel();
			splitPane.setRightComponent(panel_right_5);
		}
		
		JLabel label_3 = new JLabel("开始时间:");
		label_3.setBounds(67, 46, 65, 24);
		panel_right_4.add(label_3);
		
		txt_startdate_4 = new JTextField();
		txt_startdate_4.setBounds(129, 46, 138, 24);
		txt_startdate_4.setText(sdf.format(new Date()));
		panel_right_4.add(txt_startdate_4);
		txt_startdate_4.setColumns(10);
		
		JLabel label_19 = new JLabel("结束日期:");
		label_19.setBounds(318, 46, 65, 24);
		panel_right_4.add(label_19);
		
		txt_enddate_4 = new JTextField();
		txt_enddate_4.setBounds(381, 46, 138, 24);
		txt_enddate_4.setText(sdf.format(new Date()));
		panel_right_4.add(txt_enddate_4);
		txt_enddate_4.setColumns(10);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(67, 109, 656, 401);
		panel_right_4.add(scrollPane_4);
		
		table_4 = new JTable();
		{
			table_infModel_4 = 
				new MyTableModel(
						new String[][] {  },
						new String[] { "金额","次数","餐次"});
//			DefaultTableCellRenderer dc = 
//				new DefaultTableCellRenderer();
//			//设置包装器对齐方式
//			dc.setHorizontalAlignment(SwingConstants.CENTER);
			table_4.setDefaultRenderer(Object.class, dc);
		}
		JButton button_1 = new JButton("查询");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List list=queryForLost();
				fillTable(table_infModel_4, list);
			}
		});
		button_1.setBounds(574, 39, 93, 38);
		panel_right_4.add(button_1);
		//将样式包装器设置到JTable对象
		table_4.setModel(table_infModel_4);
		table_4.setRowHeight(28);
		table_4.getTableHeader().setFont(new Font("微软雅黑",Font.PLAIN,20));
		table_4.setFont(new Font("微软雅黑",Font.PLAIN,18));
//		table_4.getColumnModel().getColumn(0).setPreferredWidth(200);
		scrollPane_4.setViewportView(table_4);
		sumPanel=new JPanel();
		panel.add(sumPanel);
		sumPanel.setVisible(false);
		sumPanel.setLayout(null);
		sumPanel.setPreferredSize(new Dimension(0, 0));
		sumPanel.setBounds(0, 0, 0, 0);
		JButton btn_printCurrent = new JButton();
		btn_printCurrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//打印操作
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date=sdf.format(new Date());
		        StringBuffer sb = new StringBuffer();
		        try {
					sb.append("\r\n\r\n       "+new String(TF.dsJson.getString("merchantName").getBytes("ISO-8859-1"),"gb2312")+"结算报表");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        sb.append("\r\n\r\n时间: "+date);
		        sb.append("\r\n\r\n机号: "+TF.dsJson.getString("machineId"));
		        sb.append("\r\n\r\n--------------------------------");
		        sb.append("\r\n\r\n起始时间 : "+sdf.format(txt_startdate_2.getDate())+" 00:00:00");
		        sb.append("\r\n\r\n截至时间 : "+sdf.format(txt_enddate_2.getDate())+" 23:59:59");
		        sb.append("\r\n\r\n--------------------------------");
		        sb.append("\r\n\r\n明细：");
		        sb.append("\r\n--------------------------------");
		        sb.append("\r\n\r\n餐次 现金   刷卡   人数    小计");
		        sb.append("\r\n\r\n早餐 "+DataConvertUtil.StrComple(lbl1Money.getText())+" "+DataConvertUtil.StrComple(lbl1card.getText())+" "+DataConvertUtil.StrComple(lbl1SumPerson.getText())+" "+lbl1MoenySum.getText()+"");
		        sb.append("\r\n\r\n午餐 "+DataConvertUtil.StrComple(lbl2Money.getText())+" "+DataConvertUtil.StrComple(lbl2card.getText())+" "+DataConvertUtil.StrComple(lbl2SumPerson.getText())+" "+lbl2MoenySum.getText()+"");
		        sb.append("\r\n\r\n晚餐 "+DataConvertUtil.StrComple(lbl3Money.getText())+" "+DataConvertUtil.StrComple(lbl3card.getText())+" "+DataConvertUtil.StrComple(lbl3SumPerson.getText())+" "+lbl3MoenySum.getText()+"");
		        sb.append("\r\n\r\n夜宵 "+DataConvertUtil.StrComple(lbl4Money.getText())+" "+DataConvertUtil.StrComple(lbl4card.getText())+" "+DataConvertUtil.StrComple(lbl4SumPerson.getText())+" "+lbl4MoenySum.getText()+"");
		        sb.append("\r\n\r\n拓展 "+DataConvertUtil.StrComple(lbl5Money.getText())+" "+DataConvertUtil.StrComple(lbl5card.getText())+" "+DataConvertUtil.StrComple(lbl5SumPerson.getText())+" "+lbl5MoenySum.getText()+"");
		        sb.append("\r\n\r\n--------------------------------");
		        sb.append("\r\n\r\n统计: 现金  刷卡   人数    总计");
		        sb.append("\r\n\r\n     "+DataConvertUtil.StrComple(lblMoneySum.getText())+" "+DataConvertUtil.StrComple(lblCardSum.getText())+" "+DataConvertUtil.StrComple(lblPersonSum.getText())+" "+lbltotalMoeny.getText()+"");
		        sb.append("\r\n\r\n\r\n--------------------------------");
		        sb.append("  收银员：          审核：\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
		        String printStr = sb.toString();
		    	SuccessPrint successPrint = new SuccessPrint(printStr);
		    	successPrint.printMsg();
			}
		});
		btn_printCurrent.setText("打印");
		btn_printCurrent.setBounds(652, 31, 81, 30);
		panel_right_2.add(btn_printCurrent);
		{
			 JLabel lblNewLabel = new JLabel("鑫辉餐饮结算报表");
				lblNewLabel.setFont(new Font("宋体", Font.BOLD, 18));
				lblNewLabel.setBounds(380, 57, 205, 42);
				sumPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("时间");
			lblNewLabel_1.setBounds(336, 119, 54, 15);
			sumPanel.add(lblNewLabel_1);
		}
		{
			lblcuDate = new JLabel("2015-04-22  16:03:55");
			lblcuDate.setBounds(380, 111, 129, 30);
			sumPanel.add(lblcuDate);
		}
		{
			JLabel label_20 = new JLabel("机号");
			label_20.setBounds(519, 119, 32, 15);
			sumPanel.add(label_20);
		}
		{
			lblMachineId = new JLabel();
			lblMachineId.setBounds(547, 119, 54, 15);
			sumPanel.add(lblMachineId);
		}
		{
			JLabel label_21 = new JLabel("------------------------------------------------------------");
			label_21.setBounds(336, 144, 377, 15);
			sumPanel.add(label_21);
		}
		{
			label_5 = new JLabel("起始时间：");
			label_5.setBounds(336, 156, 76, 21);
			sumPanel.add(label_5);
		}
		{
			lblStarDate = new JLabel();
			lblStarDate.setBounds(422, 151, 163, 30);
			sumPanel.add(lblStarDate);
		}
		{
			label_7 = new JLabel("截止时间：");
			label_7.setBounds(336, 192, 76, 21);
			sumPanel.add(label_7);
		}
		{
			lblEndDate = new JLabel();
			lblEndDate.setBounds(422, 187, 163, 30);
			sumPanel.add(lblEndDate);
		}
		{
			label_9 = new JLabel("------------------------------------------------------------");
			label_9.setBounds(336, 223, 377, 15);
			sumPanel.add(label_9);
		}
		{
			label_10 = new JLabel("明细：");
			label_10.setBounds(336, 248, 54, 15);
			sumPanel.add(label_10);
		}
		{
			lblNewLabel_2 = new JLabel("餐次");
			lblNewLabel_2.setBounds(336, 282, 32, 15);
			sumPanel.add(lblNewLabel_2);
		}
		{
			label_11 = new JLabel("------------------------------------------------------------");
			label_11.setBounds(336, 262, 377, 15);
			sumPanel.add(label_11);
		}
		{
			label_12 = new JLabel("现金");
			label_12.setBounds(385, 282, 32, 15);
			sumPanel.add(label_12);
		}
		{
			label_13 = new JLabel("刷卡");
			label_13.setBounds(435, 282, 32, 15);
			sumPanel.add(label_13);
		}
		{
			label_14 = new JLabel("人数");
			label_14.setBounds(493, 282, 32, 15);
			sumPanel.add(label_14);
		}
		{
			label_15 = new JLabel("小计");
			label_15.setBounds(550, 282, 32, 15);
			sumPanel.add(label_15);
		}
		{
			label_16 = new JLabel("早餐");
			label_16.setBounds(336, 317, 32, 15);
			sumPanel.add(label_16);
		}
		{
			label_17 = new JLabel("午餐");
			label_17.setBounds(336, 351, 32, 15);
			sumPanel.add(label_17);
		}
		{
			label_18 = new JLabel("晚餐");
			label_18.setBounds(336, 386, 32, 15);
			sumPanel.add(label_18);
		}
		{
			lbl1Money = new JLabel("100000");
			lbl1Money.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl1Money.setBounds(380, 317, 37, 15);
			sumPanel.add(lbl1Money);
		}
		{
			lbl1card = new JLabel("100000");
			lbl1card.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl1card.setBounds(435, 317, 37, 15);
			sumPanel.add(lbl1card);
		}
		{
			lbl1SumPerson = new JLabel("100000");
			lbl1SumPerson.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl1SumPerson.setBounds(493, 317, 37, 15);
			sumPanel.add(lbl1SumPerson);
		}
		{
			lbl1MoenySum = new JLabel("100000");
			lbl1MoenySum.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl1MoenySum.setBounds(550, 317, 37, 15);
			sumPanel.add(lbl1MoenySum);
		}
		{
			lbl2Money = new JLabel("100000");
			lbl2Money.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl2Money.setBounds(380, 351, 37, 15);
			sumPanel.add(lbl2Money);
		}
		{
			lbl3Money = new JLabel("100000");
			lbl3Money.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl3Money.setBounds(380, 386, 37, 15);
			sumPanel.add(lbl3Money);
		}
		{
			lbl2card = new JLabel("100000");
			lbl2card.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl2card.setBounds(435, 351, 37, 15);
			sumPanel.add(lbl2card);
		}
		{
			lbl3card = new JLabel("100000");
			lbl3card.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl3card.setBounds(435, 386, 37, 15);
			sumPanel.add(lbl3card);
		}
		{
			lbl2SumPerson = new JLabel("100000");
			lbl2SumPerson.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl2SumPerson.setBounds(493, 351, 37, 15);
			sumPanel.add(lbl2SumPerson);
		}
		{
			lbl2MoenySum = new JLabel("100000");
			lbl2MoenySum.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl2MoenySum.setBounds(548, 351, 37, 15);
			sumPanel.add(lbl2MoenySum);
		}
		{
			lbl3SumPerson = new JLabel("100000");
			lbl3SumPerson.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl3SumPerson.setBounds(493, 386, 37, 15);
			sumPanel.add(lbl3SumPerson);
		}
		{
			lbl3MoenySum = new JLabel("100000");
			lbl3MoenySum.setFont(new Font("宋体", Font.PLAIN, 10));
			lbl3MoenySum.setBounds(548, 386, 37, 15);
			sumPanel.add(lbl3MoenySum);
		}
		
		JLabel label_20 = new JLabel("夜宵");
		label_20.setBounds(336, 421, 32, 15);
		sumPanel.add(label_20);
		
		lbl4Money = new JLabel("100000");
		lbl4Money.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl4Money.setBounds(380, 421, 37, 15);
		sumPanel.add(lbl4Money);
		
		lbl4card = new JLabel("100000");
		lbl4card.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl4card.setBounds(435, 421, 37, 15);
		sumPanel.add(lbl4card);
		
		lbl4SumPerson = new JLabel("100000");
		lbl4SumPerson.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl4SumPerson.setBounds(493, 421, 37, 15);
		sumPanel.add(lbl4SumPerson);
		
		lbl4MoenySum = new JLabel("100000");
		lbl4MoenySum.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl4MoenySum.setBounds(548, 421, 37, 15);
		sumPanel.add(lbl4MoenySum);
		{
			label_31 = new JLabel("统计:");
			label_31.setBounds(336, 489, 37, 15);
			sumPanel.add(label_31);
		}
		{
			label_32 = new JLabel("现金");
			label_32.setBounds(385, 489, 32, 15);
			sumPanel.add(label_32);
		}
		{
			label_33 = new JLabel("刷卡");
			label_33.setBounds(435, 489, 32, 15);
			sumPanel.add(label_33);
		}
		{
			label_34 = new JLabel("人数");
			label_34.setBounds(493, 489, 32, 15);
			sumPanel.add(label_34);
		}
		{
			label_35 = new JLabel("总计");
			label_35.setBounds(550, 489, 32, 15);
			sumPanel.add(label_35);
		}
		{
			lblMoneySum = new JLabel("100000");
			lblMoneySum.setFont(new Font("宋体", Font.PLAIN, 10));
			lblMoneySum.setBounds(385, 526, 37, 15);
			sumPanel.add(lblMoneySum);
		}
		{
			lblCardSum = new JLabel("100000");
			lblCardSum.setFont(new Font("宋体", Font.PLAIN, 10));
			lblCardSum.setBounds(440, 526, 37, 15);
			sumPanel.add(lblCardSum);
		}
		{
			lblPersonSum = new JLabel("100000");
			lblPersonSum.setFont(new Font("宋体", Font.PLAIN, 10));
			lblPersonSum.setBounds(498, 526, 37, 15);
			sumPanel.add(lblPersonSum);
		}
		{
			lbltotalMoeny = new JLabel("100000");
			lbltotalMoeny.setFont(new Font("宋体", Font.PLAIN, 10));
			lbltotalMoeny.setBounds(553, 526, 37, 15);
			sumPanel.add(lbltotalMoeny);
		}
		{
			label_40 = new JLabel("------------------------------------------------------------");
			label_40.setBounds(336, 550, 377, 15);
			sumPanel.add(label_40);
		}
		{
			label_41 = new JLabel("收银员：");
			label_41.setBounds(347, 575, 54, 15);
			sumPanel.add(label_41);
		}
		{
			label_42 = new JLabel("审核：");
			label_42.setBounds(489, 575, 54, 15);
			sumPanel.add(label_42);
		}
		
		JLabel label_21 = new JLabel("拓展");
		label_21.setBounds(336, 457, 32, 15);
		sumPanel.add(label_21);
		
		lbl5Money = new JLabel("100000");
		lbl5Money.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl5Money.setBounds(380, 457, 37, 15);
		sumPanel.add(lbl5Money);
		
		lbl5card = new JLabel("100000");
		lbl5card.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl5card.setBounds(435, 457, 37, 15);
		sumPanel.add(lbl5card);
		
		lbl5SumPerson = new JLabel("100000");
		lbl5SumPerson.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl5SumPerson.setBounds(493, 457, 37, 15);
		sumPanel.add(lbl5SumPerson);
		
		lbl5MoenySum = new JLabel("100000");
		lbl5MoenySum.setFont(new Font("宋体", Font.PLAIN, 10));
		lbl5MoenySum.setBounds(548, 457, 37, 15);
		sumPanel.add(lbl5MoenySum);
		
		
	}
	
	public void fillTable(DefaultTableModel model,List<Vector> list){
		model.setRowCount(0);
		if(list==null||list.size()==0){
			return;
		}
		for(Vector v:list){
			model.addRow(v);
		}
	}
	public List queryLossChip(){
		List<Vector<String>> resultList=new ArrayList<Vector<String>>();
		List<Map<String, Object>> lossChipList  =metaDataBO.queryForListMap("select date_format(CREATETIME,'%Y-%m-%d') dt,count(*) total_num,sum(PRICE) total_price,sum(case WHEN(IS_CONSUME='1') THEN 1 ELSE 0 END ) consume_num,sum(case when (IS_CONSUME='1') THEN PRICE ELSE 0 END) consume_price,sum(case WHEN(IS_CONSUME='0') THEN 1 ELSE 0 END ) no_consume_num,sum(case when (IS_CONSUME='0') THEN PRICE ELSE 0 END) no_consume_price from tf_loss_chip_record where CREATETIME BETWEEN '"+sdf.format(txt_loss_starttime.getDate())+" 00:00:00' and '"+sdf.format(txt_loss_endtime.getDate())+" 23:59:59' GROUP BY date_format(CREATETIME,'%Y-%m-%d')");
		for(Map<String,Object> map:lossChipList){
			Vector<String> vector = new Vector<>();
			vector.add(0,map.get("dt").toString());
			//vector.add(1,map.get("total_num").toString()+" | "+map.get("total_price").toString());
			//vector.add(2,map.get("consume_num").toString() +" | "+map.get("consume_price").toString());
			vector.add(1,map.get("no_consume_num").toString() +" | "+map.get("no_consume_price").toString());
			resultList.add(vector);
		}
//		List<Vector<String>> resultList=new ArrayList<Vector<String>>();
		return resultList;
		
	}
	public List queryCommodity(){
		List<Map<String, Object>> consume_commodity_list = metaDataBO.queryForListMap("select CI_NAME,SUM(CDR_NUMBER) total_count,CI_PRICE,sum(CDR_MONEY) total_price from tf_consume_details_record INNER JOIN commodity_record on tf_consume_details_record.CDR_NO = commodity_record.CI_ID INNER JOIN tf_consume_card_record on tf_consume_details_record.COR_ID = tf_consume_card_record.COR_ID  where tf_consume_card_record.CCR_STATUS='1'" +
                                                                                   " and tf_consume_details_record.CREATETIME BETWEEN '"+sdf.format(txt_commodity_starttime.getDate())+" 00:00:00' and '"+sdf.format(txt_commodity_endtime.getDate())+" 23:59:59'" +
                                                                                   " GROUP BY CDR_NO ");
		List<Vector<String>> resultList = new ArrayList<Vector<String>>();
		for(Map<String, Object> map:consume_commodity_list){
			Vector<String> vector = new Vector<>();
			vector.add(0,map.get("CI_NAME").toString());
			vector.add(1, map.get("total_count").toString());
			vector.add(2,map.get("CI_PRICE").toString());
			vector.add(3,map.get("total_price").toString());
			resultList.add(vector);
		}
		return resultList;
	}
	/**
	 * 营业概况查询
	 * @return
	 */
	public List queryForConsume(){
//		String startDateStr=txt_date.getText();
//		String endDateStr=txt_enddate.getText();
		Date startDate=null;
		Date endDate=null;
		String startDateStr=null;
		String endDateStr=null;
		try {
//			startDate=sdf.parse(startDateStr);
//			endDate=sdf.parse(endDateStr);
			startDate=txt_date.getDate();
			startDateStr=sdf.format(startDate);
			endDate=txt_enddate.getDate();
			Calendar cd=Calendar.getInstance();
			cd.setTime(endDate);
			cd.add(Calendar.DATE, 1);
			endDateStr=sdf.format(cd.getTime());
			endDate=cd.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(endDate.getTime()<startDate.getTime()){
			return null;
		}
		String sql="select date_format(c.CREATETIME,'%Y-%m-%d') date, sum(case when  c.CCR_PAY_TYPE = '0' then c.CCR_MONEY else 0 end) cash ,sum( CASE WHEN  c.CCR_PAY_TYPE = '2' THEN c.CCR_MONEY ELSE 0 END ) wx,sum( CASE WHEN  c.CCR_PAY_TYPE = '3' THEN c.CCR_MONEY ELSE 0 END ) zfb,sum(case when c.CCR_PAY_TYPE = '1' then c.CCR_MONEY else 0 end) card,sum(case when c.CCR_PAY_TYPE ='4' then c.CCR_MONEY else 0 end) free,sum(c.CCR_MONEY) sum from tf_consume_card_record c,tf_consume_order_record o where c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CREATETIME  BETWEEN '"+startDateStr+"' and '"+endDateStr+"' and c.CCR_STATUS='1' and c.COR_ID=o.COR_ID GROUP BY  date_format(c.CREATETIME,'%Y-%m-%d')";
		List<Map<String,Object>> qList=metaDataBO.queryForListMap(sql);
		JSONObject jsonObject=new JSONObject();
		Vector v;
		List<Vector<String>> resultList=new ArrayList<Vector<String>>();
		Map<String,Vector<String>> resultMap=new HashMap<String,Vector<String>>();
		JSONObject jsonObj=new JSONObject();
		for(int i=0;i<(endDate.getTime()-startDate.getTime())/(24*3600*1000);i++){
			Vector<String> vector=new Vector();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH,i);
			vector.add(sdf.format(calendar.getTime()));
			vector.add("0");
			vector.add("0");
			vector.add("0");
			vector.add("0");
			vector.add("0");
			vector.add("0");
			resultMap.put(sdf.format(calendar.getTime()), vector);
			for(Map totalMap:qList){
				if(totalMap.get("date").equals(sdf.format(calendar.getTime()))){
					v=new Vector<>();
					v.add(0,totalMap.get("date"));
					v.add(1,totalMap.get("cash"));
					v.add(2,totalMap.get("zfb"));
					v.add(3,totalMap.get("wx"));
					v.add(4,totalMap.get("card"));
					v.add(5,totalMap.get("free"));
					v.add(6,totalMap.get("sum"));
					jsonObj.put("cash", jsonObj.getFloatValue("cash")+Float.parseFloat(totalMap.get("cash")+""));
					jsonObj.put("zfb", jsonObj.getFloatValue("zfb")+Float.parseFloat(totalMap.get("zfb")+""));
					jsonObj.put("wx", jsonObj.getFloatValue("wx")+Float.parseFloat(totalMap.get("wx")+""));
					jsonObj.put("card", jsonObj.getFloatValue("card")+Float.parseFloat(totalMap.get("card")+""));
					jsonObj.put("free", jsonObj.getFloatValue("free")+Float.parseFloat(totalMap.get("free")+""));
					jsonObj.put("sum", jsonObj.getFloatValue("sum")+Float.parseFloat(totalMap.get("sum")+""));
					resultMap.put(sdf.format(calendar.getTime()), v);
				}
			}
		}
		for(String key:resultMap.keySet()){
			resultList.add(resultMap.get(key));
		}
		java.util.Collections.sort(resultList, new Comparator<Vector<String>>() {

			@Override
			public int compare(Vector<String> o1, Vector<String> o2) {
				// TODO Auto-generated method stub
				try {
					long flag=(sdf.parse(o2.get(0)).getTime()-sdf.parse(o1.get(0)).getTime());
					if(flag>0){
						return 1;
					}else if(flag==0){
						return 0;
					}else{
						return -1;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		});
		Vector totalVector=new Vector();
		totalVector.add("总计");
		totalVector.add(jsonObj.getFloatValue("cash")+"");
		totalVector.add(jsonObj.getFloatValue("zfb")+"");
		totalVector.add(jsonObj.getFloatValue("wx")+"");
		totalVector.add(jsonObj.getFloatValue("card")+"");
		totalVector.add(jsonObj.getFloatValue("free")+"");
		totalVector.add(jsonObj.getFloatValue("sum")+"");
		resultList.add(totalVector);
		return resultList;
	}
	
	//统计查询
	public List queryForTotal(){
		String startDateStr=sdf.format(txt_startdate_2.getDate());
		String endDateStr=sdf.format(txt_enddate_2.getDate());
		try {
			Calendar cd=Calendar.getInstance();
			cd.setTime(new Date(sdf.parse(endDateStr).getTime()));
			cd.add(Calendar.DATE, 1);
			endDateStr=sdf.format(cd.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List list=new ArrayList();
		List<Map<String,Object>> mealList=TF.mealList;
		for(Map mealMap:mealList){
			String sql="SELECT sum(c.CCR_MONEY) sum, COUNT(c.CCR_MONEY) count, AVG(c.CCR_MONEY) avg, SUM( CASE WHEN c.CCR_PAY_TYPE = '0' THEN c.CCR_MONEY ELSE 0 END ) cash, SUM( CASE WHEN c.CCR_PAY_TYPE = '0' THEN 1 ELSE 0 END ) cashCount, SUM( CASE WHEN c.CCR_PAY_TYPE = '1' THEN CCR_MONEY ELSE 0 END ) card, sum( CASE WHEN c.CCR_PAY_TYPE = '1' THEN 1 ELSE 0 END ) cardCount FROM tf_consume_card_record c, tf_consume_order_record o,tf_userinfo u WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CREATETIME BETWEEN '"+startDateStr+"' AND '"+endDateStr+"' AND c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.U_ID=u.U_ID and c.MT_ID='"+mealMap.get("MT_ID")+"'";
			if(cbx_user.getSelectedItem()!=null){
				if(!((MyObject)cbx_user.getSelectedItem()).getKey().equals("无"))
				sql+=" and c.U_ID='"+((MyObject)cbx_user.getSelectedItem()).getValue()+"'";
			}
			Map<String,Object> map=metaDataBO.queryForMap(sql);
			String str1=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("sum")==null?"0":map.get("sum")+""))+" | "+Integer.parseInt(map.get("count")==null?"0":map.get("count")+"");
			String str2=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("avg")==null?"0":map.get("avg")+""))+" | "+Integer.parseInt(map.get("count")==null?"0":map.get("count")+"");
			String str3=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("cash")==null?"0":map.get("cash")+""))+" | "+Integer.parseInt(map.get("cashCount")==null?"0":map.get("cashCount")+"");
			String str4=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("card")==null?"0":map.get("card")+""))+" | "+Integer.parseInt(map.get("cardCount")==null?"0":map.get("cardCount")+"");
			Vector vector=new Vector();
			vector.add(mealMap.get("MT_NAME"));
			vector.add(str1);
			vector.add(str2);
			vector.add(str3);
			vector.add(str4);
			list.add(vector);
		}
		String sql="SELECT sum(c.CCR_MONEY) sum, COUNT(c.CCR_MONEY) count, AVG(c.CCR_MONEY) avg, SUM( CASE WHEN c.IC_ID = '0' THEN c.CCR_MONEY ELSE 0 END ) cash, SUM( CASE WHEN c.IC_ID = '0' THEN 1 ELSE 0 END ) cashCount, SUM( CASE WHEN c.IC_ID != '0' THEN CCR_MONEY ELSE 0 END ) card, sum( CASE WHEN c.IC_ID != '0' THEN 1 ELSE 0 END ) cardCount FROM tf_consume_card_record c, tf_consume_order_record o,tf_userinfo u WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CREATETIME BETWEEN '"+startDateStr+"' AND '"+endDateStr+"' AND c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.U_ID=u.U_ID";
		if(cbx_user.getSelectedItem()!=null){
			if(!((MyObject)cbx_user.getSelectedItem()).getKey().equals("无"))
			sql+=" and c.U_ID='"+((MyObject)cbx_user.getSelectedItem()).getValue()+"'";
		}
		Map<String,Object> map=metaDataBO.queryForMap(sql);
		String str1=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("sum")==null?"0":map.get("sum")+""))+" | "+Integer.parseInt(map.get("count")==null?"0":map.get("count")+"");
		String str2=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("avg")==null?"0":map.get("avg")+""))+" | "+Integer.parseInt(map.get("count")==null?"0":map.get("count")+"");
		String str3=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("cash")==null?"0":map.get("cash")+""))+" | "+Integer.parseInt(map.get("cashCount")==null?"0":map.get("cashCount")+"");
		String str4=DataConvertUtil.moneryFormat(Float.parseFloat(map.get("card")==null?"0":map.get("card")+""))+" | "+Integer.parseInt(map.get("cardCount")==null?"0":map.get("cardCount")+"");
		Vector vector=new Vector();
		vector.add("小计");
		vector.add(str1);
		vector.add(str2);
		vector.add(str3);
		vector.add(str4);
		list.add(vector);
		return list;
	}
	
	//详细查询
	public List queryForDetails(){
//		String startDateStr=txt_startdate_3.getText();
//		String endDateStr=txt_enddate_3.getText();
		Date startDate=txt_startdate_3.getDate();
		Date endDate=txt_enddate_3.getDate();
		Calendar c=Calendar.getInstance();
		c.setTime(endDate);
		c.add(Calendar.DAY_OF_YEAR, 1); //下一天
		endDate=c.getTime();
		String startDateStr=simpleDateFormat.format(startDate);
		String endDateStr=simpleDateFormat.format(endDate);
		String sql="SELECT c.CREATETIME, c.COR_ID, o.COR_AMOUNT, c.CCR_MONEY, c.IC_ID, u.UNAME,c.CCR_PAY_TYPE FROM tf_consume_card_record c, tf_consume_order_record o,tf_userinfo u WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CREATETIME >= '"+startDateStr+"' AND c.CREATETIME < '"+endDateStr+"' and c.CCR_STATUS ='1'  AND c.COR_ID = o.COR_ID and c.U_ID=u.U_ID";
		if(cbx_mealName.getSelectedItem()!=null){
			if(!((MyObject)cbx_mealName.getSelectedItem()).getKey().equals("无"))
			sql+=" and c.MT_ID='"+((MyObject)cbx_mealName.getSelectedItem()).getValue()+"'";
		}
		if(cbx_pay.getSelectedItem()!=null && !cbx_pay.getSelectedItem().equals("")&&cbx_pay.getSelectedItem()+""!="null"){
			if(!((MyObject)cbx_pay.getSelectedItem()).getKey().equals("无")){
				if((((MyObject)cbx_pay.getSelectedItem()).getValue()).equals("0")){
					sql+=" and c.CCR_PAY_TYPE='0'";
				}else if((((MyObject)cbx_pay.getSelectedItem()).getValue()).equals("1")){
					sql+=" and c.CCR_PAY_TYPE='1'";
				}
				else if((((MyObject)cbx_pay.getSelectedItem()).getValue()).equals("2")){
					sql+=" and c.CCR_PAY_TYPE='2'";
				}
				else if((((MyObject)cbx_pay.getSelectedItem()).getValue()).equals("3")){
					sql+=" and c.CCR_PAY_TYPE='3'";
				}
				else if((((MyObject)cbx_pay.getSelectedItem()).getValue()).equals("4")){
					sql+=" and c.CCR_PAY_TYPE='4'";
				}
			}
		}
		if(cbx_userName.getSelectedItem()!=null){
			if(!((MyObject)cbx_userName.getSelectedItem()).getKey().equals("无"))
			sql+=" and c.U_ID='"+((MyObject)cbx_userName.getSelectedItem()).getValue()+"'";
		}
		List<Vector> resultList=new ArrayList();
		sql+=" order by c.CREATETIME desc";
		List<Map<String,Object>> dList=metaDataBO.queryForListMap(sql);
		pageMap.put("total", dList.size());
		if(pageMap.get("total")%pageMap.get("pageSize")==0){
			pageMap.put("pageNum", pageMap.get("total")/pageMap.get("pageSize"));
		}else{
			pageMap.put("pageNum", pageMap.get("total")/pageMap.get("pageSize")+1);
		}
		Vector v;
		for(Map<String,Object> map:dList){
			v=new Vector();
			try {
				v.add(simpleDateFormat.format(simpleDateFormat.parse(map.get("CREATETIME")+"")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			v.add(map.get("COR_ID"));
			v.add(map.get("COR_AMOUNT"));
//			v.add(map.get("COR_MONERY"));
			v.add(map.get("CCR_MONEY"));
			if(map.get("CCR_PAY_TYPE").equals("0")){
				v.add("现金");
			}else if(map.get("CCR_PAY_TYPE").equals("1")){
				v.add("刷卡");
			}else if(map.get("CCR_PAY_TYPE").equals("3")){
				v.add("支付宝");
			}else if(map.get("CCR_PAY_TYPE").equals("2")){
				v.add("微信");
			}else if(map.get("CCR_PAY_TYPE").equals("4")){
				v.add("免单");
			}
			
			v.add(map.get("UNAME"));
			resultList.add(v);
		}
		return resultList;
	}
	
	public List<Vector> queryForLost(){
		String startDate=txt_startdate_4.getText()+" 00:00:00";
		String endDate=txt_enddate_4.getText()+" 23:59:59";
		String sql="SELECT tf_consume_details_record.CDR_MONEY money, count(*) num, tf_meter_mealtims_relation.MT_NAME meal_name FROM tf_consume_card_record INNER JOIN tf_consume_order_record ON tf_consume_card_record.COR_ID = tf_consume_order_record.COR_ID INNER JOIN tf_consume_details_record ON tf_consume_order_record.COR_ID = tf_consume_details_record.COR_ID INNER JOIN tf_meter_mealtims_relation ON tf_consume_card_record.MT_ID = tf_meter_mealtims_relation.MT_ID WHERE tf_consume_card_record.CCR_STATUS = '1' AND tf_consume_card_record.CREATETIME BETWEEN '"+startDate+"' AND '"+endDate+"' AND tf_consume_details_record.CDR_RFID ='1' GROUP BY tf_consume_details_record.CDR_MONEY, tf_meter_mealtims_relation.MT_ID ORDER BY tf_meter_mealtims_relation.MT_ID";
		Vector v;
		List<Vector> resultList=new ArrayList();
		List<Map<String,Object>> lList=metaDataBO.queryForListMap(sql);
		for(Map<String,Object> map:lList){
			v=new Vector();
			v.add(map.get("money"));
			v.add(map.get("num"));
			v.add(map.get("meal_name"));
			resultList.add(v);
		}
		return resultList;
	}
	
	private class MyObject{
		private String key;
		private String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		private MyObject(){}
		private MyObject(String key,String value){
			this.key=key;
			this.value=value;
		}
		public String toString(){
			return key;
		}
	}
	
	public void fillMealtimes(JComboBox cbx){
		List<MyObject> resultlist=new ArrayList<MyObject>();
		resultlist.add(new MyObject("无",""));
		List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_mealtimes");
		for(Map mealMap:list){
			if(mealMap.get("MT_NAME")==null||mealMap.get("MT_ID")==null){
				return;
			}
			resultlist.add(new MyObject(mealMap.get("MT_NAME").toString(), mealMap.get("MT_ID").toString()));
		}
		DefaultComboBoxModel model=(DefaultComboBoxModel) cbx.getModel();
		model.removeAllElements();
		for(MyObject myobject:resultlist){
			model.addElement(myobject);
		}
	}
	
	public void fillPay(){
		DefaultComboBoxModel model=(DefaultComboBoxModel) cbx_pay.getModel();
		model.removeAllElements();
		MyObject obj1=new MyObject("无", "");
		MyObject obj2=new MyObject("现金", "0");
		MyObject obj3=new MyObject("刷卡","1");
		MyObject obj5=new MyObject("微信","2");
		MyObject obj4=new MyObject("支付宝","3");
		MyObject obj6=new MyObject("免单","4");
		model.addElement(obj1);
		model.addElement(obj2);
		model.addElement(obj3);
		model.addElement(obj4);
		model.addElement(obj5);
	}
	
	public void fillUser(JComboBox cbx){
		List<MyObject> resultlist=new ArrayList<MyObject>();
		resultlist.add(new MyObject("无",""));
		List<Map<String, Object>> list=metaDataBO.queryForListMap("select * from tf_userinfo");
		for(Map userMap:list){
			if(userMap.get("UNAME")==null||userMap.get("U_ID")==null){
				return;
			}
			resultlist.add(new MyObject(userMap.get("UNAME").toString(), userMap.get("U_ID").toString()));
		}
		DefaultComboBoxModel model=(DefaultComboBoxModel) cbx.getModel();
		model.removeAllElements();
		for(MyObject myobject:resultlist){
			model.addElement(myobject);
		}
	}
	
	public void searchConsumeSum(){
		initlblmoney();
		JSONObject resultJson=queryConsumeSumByTime(sdf.format(txt_startdate_2.getDate())+" 00:00:00", sdf.format(txt_enddate_2.getDate())+" 23:59:59");
		//卡消费金额
		lbl1card.setText((resultJson.getString("cardSum1")!=null)?resultJson.getString("cardSum1"):"0");
		lbl2card.setText((resultJson.getString("cardSum2")!=null)?resultJson.getString("cardSum2"):"0");
		lbl3card.setText((resultJson.getString("cardSum3")!=null)?resultJson.getString("cardSum3"):"0");
		lbl4card.setText((resultJson.getString("cardSum4")!=null)?resultJson.getString("cardSum4"):"0");
		lbl5card.setText((resultJson.getString("cardSum5")!=null)?resultJson.getString("cardSum5"):"0");
		//餐次金额
		float moneySum1=(float) 0.0;
		float cardSum1=(float) 0.0;
		if(resultJson.getString("moneySum1")!=null){
			moneySum1=Float.parseFloat(resultJson.getString("moneySum1"));
		}
		if(resultJson.getString("cardSum1")!=null){
			cardSum1=Float.parseFloat(resultJson.getString("cardSum1"));
		}
		lbl1MoenySum.setText(moneySum1+cardSum1+"");
		float moneySum2=(float) 0.0;
		float cardSum2=(float) 0.0;
		if(resultJson.getString("moneySum2")!=null){
			moneySum2=Float.parseFloat(resultJson.getString("moneySum2"));
		}
		if(resultJson.getString("cardSum2")!=null){
			cardSum2=Float.parseFloat(resultJson.getString("cardSum2"));
		}
		lbl2MoenySum.setText(moneySum2+cardSum2+"");
		float moneySum3=(float) 0.0;
		float cardSum3=(float) 0.0;
		if(resultJson.getString("moneySum3")!=null){
			moneySum3=Float.parseFloat(resultJson.getString("moneySum3"));
		}
		if(resultJson.getString("cardSum3")!=null){
			cardSum3=Float.parseFloat(resultJson.getString("cardSum3"));
		}
		lbl3MoenySum.setText(moneySum3+cardSum3+"");
		float moneySum4=(float) 0.0;
		float cardSum4=(float) 0.0;
		if(resultJson.getString("moneySum4")!=null){
			moneySum4=Float.parseFloat(resultJson.getString("moneySum4"));
		}
		if(resultJson.getString("cardSum4")!=null){
			cardSum4=Float.parseFloat(resultJson.getString("cardSum4"));
		}
		lbl4MoenySum.setText(moneySum4+cardSum4+"");
		float moneySum5=(float) 0.0;
		float cardSum5=(float) 0.0;
		if(resultJson.getString("moneySum5")!=null){
			moneySum5=Float.parseFloat(resultJson.getString("moneySum5"));
		}
		if(resultJson.getString("cardSum5")!=null){
			cardSum5=Float.parseFloat(resultJson.getString("cardSum5"));
		}
		lbl5MoenySum.setText(moneySum5+cardSum5+"");
		//现金金额
		lbl1Money.setText((resultJson.getString("moneySum1")!=null)?resultJson.getString("moneySum1"):"0");
		lbl2Money.setText((resultJson.getString("moneySum2")!=null)?resultJson.getString("moneySum2"):"0");
		lbl3Money.setText((resultJson.getString("moneySum3")!=null)?resultJson.getString("moneySum3"):"0");
		lbl4Money.setText((resultJson.getString("moneySum4")!=null)?resultJson.getString("moneySum4"):"0");
		lbl5Money.setText((resultJson.getString("moneySum5")!=null)?resultJson.getString("moneySum5"):"0");
		//人数
		lbl1SumPerson.setText((resultJson.getString("personSum1")!=null)?resultJson.getString("personSum1"):"0");
		lbl2SumPerson.setText((resultJson.getString("personSum2")!=null)?resultJson.getString("personSum2"):"0");
		lbl3SumPerson.setText((resultJson.getString("personSum3")!=null)?resultJson.getString("personSum3"):"0");
		lbl4SumPerson.setText((resultJson.getString("personSum4")!=null)?resultJson.getString("personSum4"):"0");
		lbl5SumPerson.setText((resultJson.getString("personSum5")!=null)?resultJson.getString("personSum5"):"0");
		//总计
		lblMoneySum.setText(Float.parseFloat(lbl1Money.getText())+Float.parseFloat(lbl2Money.getText())+Float.parseFloat(lbl3Money.getText())+Float.parseFloat(lbl4Money.getText())+Float.parseFloat(lbl5Money.getText())+"");
		lblCardSum.setText(Float.parseFloat(lbl1card.getText())+Float.parseFloat(lbl2card.getText())+Float.parseFloat(lbl3card.getText())+Float.parseFloat(lbl4card.getText())+Float.parseFloat(lbl5card.getText())+"");
		lblPersonSum.setText(Integer.parseInt(lbl1SumPerson.getText())+Integer.parseInt(lbl2SumPerson.getText())+Integer.parseInt(lbl3SumPerson.getText())+Integer.parseInt(lbl4SumPerson.getText())+Integer.parseInt(lbl5SumPerson.getText())+"");
		lbltotalMoeny.setText(Float.parseFloat(lblMoneySum.getText())+Float.parseFloat(lblCardSum.getText())+"");
	}
	public void initlblmoney(){
		lblMachineId.setText(TF.dsJson.getString("machineId"));
		lblcuDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		lblStarDate.setText(sdf.format(txt_startdate_2.getDate())+" 00:00:00");
		lblEndDate.setText(sdf.format(txt_enddate_2.getDate())+" 23:59:59");
		//卡消费金额
		lbl1card.setText("");
		lbl2card.setText("");
		lbl3card.setText("");
		lbl4card.setText("");
		lbl5card.setText("");
		//餐次金额
		lbl1MoenySum.setText("");
		lbl2MoenySum.setText("");
		lbl3MoenySum.setText("");
		lbl4MoenySum.setText("");
		lbl5MoenySum.setText("");
		//现金金额
		lbl1Money.setText("");
		lbl2Money.setText("");
		lbl3Money.setText("");
		lbl4Money.setText("");
		lbl5Money.setText("");
		//人数
		lbl1SumPerson.setText("");
		lbl2SumPerson.setText("");
		lbl3SumPerson.setText("");
		lbl4SumPerson.setText("");
		lbl5SumPerson.setText("");
		//总计
		lblMoneySum.setText("");
		lblCardSum.setText("");
		lblPersonSum.setText("");
		lbltotalMoeny.setText("");
	}
	
	/**
	 * 按时间段汇总消费记录
	 * @param args
	 */
	public JSONObject queryConsumeSumByTime(String startTime,String endTime){
		JSONObject consumeSum=new JSONObject();
		//现金早餐消费金额,人数
		String querySql1ByCash="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE='0' and c.MT_ID='1'";
		//刷卡早餐消费金额,人数
		String querySql1ByCard="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE ='1' and c.MT_ID='1'";
		//现金午餐消费金额，人数
		String querySql2ByCash="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE='0' and c.MT_ID='2'";
		//刷卡午餐餐消费金额,人数
		String querySql2ByCard="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE ='1' and c.MT_ID='2'";
		//现金晚餐餐刷卡人数
		String querySql3ByCash="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE='0' and c.MT_ID='3'";
		//刷卡晚餐消费金额,人数
		String querySql3ByCard="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE ='1' and c.MT_ID='3'";
		//现金夜宵消费金额，人数
		String querySql4ByCash="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE='0' and c.MT_ID='4'";
		//刷卡夜宵消费金额,人数
		String querySql4ByCard="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE ='1' and c.MT_ID='4'";
		//现金拓展消费金额，人数
		String querySql5ByCash="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE='0' and c.MT_ID='5'";
		//刷卡拓展消费金额,人数
		String querySql5ByCard="SELECT SUM(c.CCR_MONEY),COUNT(c.CCR_MONEY) FROM tf_consume_card_record c,tf_consume_order_record o WHERE c.MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and  c.CCR_STATUS = '1' AND c.COR_ID = o.COR_ID and c.CCR_PAY_TYPE ='1' and c.MT_ID='5'";

		//		String querySql="SELECT c.CREATETIME,c.COR_ID,d.CDR_MONEY,c.IC_SERIAL_NUMBER,c.CCR_ORIGINALAMOUNT,c.UID, m.MT_NAME FROM tf_consume_card_record c,tf_consume_order_record o,tf_mealtimes m,tf_consume_details_record d,tf_chiptype p WHERE c.CCR_STATUS != '0' AND c.COR_ID = o.COR_ID AND d.COR_ID = o.COR_ID and m.SEQNO=o.ME_ID";
		if(startTime!=null && !startTime.equals("") && endTime!=null && !endTime.equals("")){
			Calendar cd=Calendar.getInstance();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date d=new Date();
			try {
				d = new Date(sdf.parse(endTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			cd.setTime(d);
			cd.add(Calendar.DATE, 1);
			endTime=sdf.format(cd.getTime());
			//早餐统计
			querySql1ByCash+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql1Map=metaDataBO.queryForMap(querySql1ByCash);
			consumeSum.put("moneySum1", querySql1Map.get("SUM(c.CCR_MONEY)"));
			String moneyPerson1="";
			if(querySql1Map.get("COUNT(c.CCR_MONEY)")!=null){
				moneyPerson1=querySql1Map.get("COUNT(c.CCR_MONEY)")+"";
			}else{
				moneyPerson1="0";
			}
			
			querySql1ByCard+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql1CardMap=metaDataBO.queryForMap(querySql1ByCard);
			consumeSum.put("cardSum1", querySql1CardMap.get("SUM(c.CCR_MONEY)"));
			if(querySql1CardMap.get("COUNT(c.CCR_MONEY)")!=null){
				consumeSum.put("personSum1", Integer.parseInt(moneyPerson1)+Integer.parseInt(querySql1CardMap.get("COUNT(c.CCR_MONEY)")+""));
			}else{
				consumeSum.put("personSum1", Integer.parseInt(moneyPerson1));
			}
			//午餐统计
			querySql2ByCash+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql2Map=metaDataBO.queryForMap(querySql2ByCash);
			consumeSum.put("moneySum2", querySql2Map.get("SUM(c.CCR_MONEY)"));
			String moneyPerson2="";
			if(querySql2Map.get("COUNT(c.CCR_MONEY)")!=null){
				moneyPerson2=querySql2Map.get("COUNT(c.CCR_MONEY)")+"";
			}else{
				moneyPerson2="0";
			}
//			String moneyPerson2=querySql2Map.get(1);
			
			querySql2ByCard+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql2CardMap=metaDataBO.queryForMap(querySql2ByCard);
			consumeSum.put("cardSum2", querySql2CardMap.get("SUM(c.CCR_MONEY)"));
			if(querySql2CardMap.get("COUNT(c.CCR_MONEY)")!=null){
				consumeSum.put("personSum2", Integer.parseInt(moneyPerson2)+Integer.parseInt(querySql2CardMap.get("COUNT(c.CCR_MONEY)")+""));
			}else{
				consumeSum.put("personSum2", Integer.parseInt(moneyPerson2));
			}
			//晚餐统计
			querySql3ByCash+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql3Map=metaDataBO.queryForMap(querySql3ByCash);
			consumeSum.put("moneySum3", querySql3Map.get("SUM(c.CCR_MONEY)"));
//			String moneyPerson3=querySql3Map.get(1);
			String moneyPerson3="";
			if(querySql3Map.get("COUNT(c.CCR_MONEY)")!=null){
				moneyPerson3=querySql3Map.get("COUNT(c.CCR_MONEY)")+"";
			}else{
				moneyPerson3="0";
			}
			
			querySql3ByCard+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql3CardMap=metaDataBO.queryForMap(querySql3ByCard);
			consumeSum.put("cardSum3", querySql3CardMap.get("SUM(c.CCR_MONEY)"));
			if(querySql3CardMap.get("COUNT(c.CCR_MONEY)")!=null){
				consumeSum.put("personSum3", Integer.parseInt(moneyPerson3)+Integer.parseInt(querySql3CardMap.get("COUNT(c.CCR_MONEY)")+""));
			}else{
				consumeSum.put("personSum3", Integer.parseInt(moneyPerson3));
			}
			
			//夜宵统计
			querySql4ByCash+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql4Map=metaDataBO.queryForMap(querySql4ByCash);
			consumeSum.put("moneySum4", querySql4Map.get("SUM(c.CCR_MONEY)"));
//			String moneyPerson3=querySql3Map.get(1);
			String moneyPerson4="";
			if(querySql4Map.get("COUNT(c.CCR_MONEY)")!=null){
				moneyPerson4=querySql4Map.get("COUNT(c.CCR_MONEY)")+"";
			}else{
				moneyPerson4="0";
			}
			
			querySql4ByCard+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql4CardMap=metaDataBO.queryForMap(querySql4ByCard);
			consumeSum.put("cardSum4", querySql4CardMap.get("SUM(c.CCR_MONEY)"));
			if(querySql4CardMap.get("COUNT(c.CCR_MONEY)")!=null){
				consumeSum.put("personSum4", Integer.parseInt(moneyPerson4)+Integer.parseInt(querySql4CardMap.get("COUNT(c.CCR_MONEY)")+""));
			}else{
				consumeSum.put("personSum4", Integer.parseInt(moneyPerson4));
			}
			
			//拓展统计
			querySql5ByCash+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql5Map=metaDataBO.queryForMap(querySql5ByCash);
			consumeSum.put("moneySum5", querySql5Map.get("SUM(c.CCR_MONEY)"));
//			String moneyPerson3=querySql3Map.get(1);
			String moneyPerson5="";
			if(querySql5Map.get("COUNT(c.CCR_MONEY)")!=null){
				moneyPerson5=querySql5Map.get("COUNT(c.CCR_MONEY)")+"";
			}else{
				moneyPerson5="0";
			}
			
			querySql5ByCard+=" and c.CREATETIME BETWEEN '"+startTime+"' AND '"+endTime+"' order by c.CREATETIME desc";
			Map<String,Object> querySql5CardMap=metaDataBO.queryForMap(querySql5ByCard);
			consumeSum.put("cardSum5", querySql5CardMap.get("SUM(c.CCR_MONEY)"));
			if(querySql5CardMap.get("COUNT(c.CCR_MONEY)")!=null){
				consumeSum.put("personSum5", Integer.parseInt(moneyPerson5)+Integer.parseInt(querySql5CardMap.get("COUNT(c.CCR_MONEY)")+""));
			}else{
				consumeSum.put("personSum5", Integer.parseInt(moneyPerson5));
			}
			
		}
		return consumeSum;
	}
	
	public JSONObject getPrintAllInfo(){
		try {
			JSONObject requestJson=new JSONObject();
			requestJson.put("startTime", sdf.format(txt_startdate_2.getDate()));
			requestJson.put("endTime", sdf.format(txt_enddate_2.getDate()));
			String bsmPort=TF.dsJson.getString("serverPortForBsm");
			if(bsmPort==null||bsmPort.equals("")){
				bsmPort="8080";
			}
			String server="http://"+TF.dsJson.getString("serverIp")+":"+bsmPort+"/tf-bsm/findSalesStatistics.do?startTime="+requestJson.getString("startTime")+"&endTime="+requestJson.getString("endTime")+"";
//			String result=httpClient.postForm(server, requestJson.toJSONString());
			String result = httpClient.get(server);
			
			JSONObject resultJson= JSONObject.parseObject(result);
			
			if(resultJson.getBooleanValue("result")){
				return resultJson;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof JTextField){
			invalidate();
			JTextField txt=(JTextField)e.getSource();
			JPanel panel=(JPanel) splitPane.getRightComponent();
//			numInputPnl=new NumberInputPnl();
			numInputPnl=new NumberInputPnl(panel,txt, txt.getX()+txt.getWidth(),txt.getY()+txt.getHeight());
			validate();
			repaint();
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		invalidate();
		JPanel panel=(JPanel) splitPane.getRightComponent();
		panel.remove(numInputPnl);
		validate();
		repaint();
	}
	
	@Override
	public void addListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancleTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListeners() {
		// TODO Auto-generated method stub
		
	}
	
	public void closeTF(){
		int flag = JOptionPane.showConfirmDialog(ConsumeQueryFrame.this,"是否退出系统？","退出",JOptionPane.YES_NO_OPTION);   
        if(flag==0) { 
			JSONObject uploadJson=new JSONObject();
			uploadJson.put("ONLINE_STATUS", 0);
			metaDataBO.execute(metaDataBO.toSql(uploadJson, "tf_meter_operation_record", "SEQNO"));
			System.exit(0);   
        } 
	}
}
