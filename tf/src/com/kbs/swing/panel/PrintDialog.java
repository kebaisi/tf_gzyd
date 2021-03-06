package com.kbs.swing.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DateTimeUtil;
import com.kbs.util.PrintByXinhui;
import com.kbs.util.TF;

public class PrintDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private MyJTable1 table;
	private JSONArray selectArr=new JSONArray();
	private Object[][] infoObj=new Object[][]{};
	private Object[] headerObj=new Object[]{"序号","订单号","数量","金额"};
	DefaultTableModel model;
	private JSONArray printJsonArr=new JSONArray();
	private int size=5;
	private MetaDataBO metaDataBO=new MetaDataBO();
	private PrintByXinhui printByXinhui=new PrintByXinhui();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			GuadanDialog dialog = new GuadanDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public PrintDialog(JFrame frame,String title,boolean flag) {
		super(frame,title,flag);
		initGUI();
		printJsonArr=new JSONArray();
//		for(int i=TF.printList.size();i>=0;i--){
//			printJsonArr.add(TF.printList.getJSONObject(i));
//		}
		printJsonArr=consumeData(size);
		infoObj=new Object[printJsonArr.size()][4];
		for(int i=0;i<printJsonArr.size();i++){
			JSONObject json=printJsonArr.getJSONObject(i);
//			infoObj[i]=new Object[]{i+1,json.getString("COR_ID"),json.getString("COR_AMOUNT"),json.getString("COR_MONEY")};
			String corId=json.getJSONObject("tf_consume_card_record").getString("COR_ID");
			String amount=json.getJSONObject("tf_consume_order_record").getString("COR_AMOUNT");
			String money=json.getJSONObject("tf_consume_card_record").getString("CCR_MONEY");
			infoObj[i]=new Object[]{i+1,corId,amount,money};
		}
		model=new DefaultTableModel(infoObj,headerObj);
		model.setDataVector(infoObj, headerObj);
		table.setModel(model);
	}
	
	public void initGUI(){
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new MyJTable1();
//				table.setRowHeight(50);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btn_ok = new JButton("确定");
				btn_ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int row=table.getSelectedRow();
						if(row!=-1){
							JSONObject json=printJsonArr.getJSONObject(row);
							printByXinhui.print(json);
						}
					}
				});
				btn_ok.setActionCommand("OK");
				buttonPane.add(btn_ok);
				getRootPane().setDefaultButton(btn_ok);
			}
			{
				JButton btn_cancle = new JButton("关闭");
				btn_cancle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PrintDialog.this.dispose();
					}
				});
				buttonPane.add(btn_cancle);
			}
		}
	}

	public JSONArray consumeData(int size){
		JSONArray arr=new JSONArray();
		List<Map<String,Object>> listCard=metaDataBO.queryForListMap("select * from tf_consume_card_record where CCR_STATUS!='0' order by CREATETIME DESC limit 0,"+size+"");
		if(listCard==null){
			return null;
		}
		listCard=convertDateToString(listCard);
		for(Map<String,Object> cardMap:listCard){
			 String corid=cardMap.get("COR_ID")+"";
			 List detailsList=metaDataBO.queryForListMap("select * from tf_consume_details_record where COR_ID='"+corid+"'");
			 if(detailsList!=null){
				 detailsList=convertDateToString(detailsList);
				 Map orderMap=metaDataBO.queryForMap("select * from tf_consume_order_record where COR_ID='"+corid+"'");
				 if(orderMap!=null){
					 orderMap.put("CREATETIME", DateTimeUtil.getDateStrFromDate(orderMap.get("CREATETIME")));
					 JSONObject json=new JSONObject();
					 json.put("data", new JSONObject());
					 json.put("tf_consume_card_record", cardMap);
					 json.put("tf_consume_details_record", detailsList);
					 json.put("tf_consume_order_record", orderMap);
					 arr.add(json);
				 }
			 }
		 }
		 return arr;
	 }
	
	public List convertDateToString(List<Map<String,Object>> dataList){
		 List resultList=new ArrayList();
		 for(Map map:dataList){
			 map.put("CREATETIME", DateTimeUtil.getDateStrFromDate(map.get("CREATETIME")));
			 resultList.add(map);
		 }
		 return resultList;
	}
}
