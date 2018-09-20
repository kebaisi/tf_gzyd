package com.kbs.swing.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.commons.rfid.RfidUtils;
import com.kbs.swing.CommonInterface;
import com.kbs.util.TF;

public class SaleManagerFrame extends JPanel implements CommonInterface{
	private Logger log = Logger.getLogger(SaleManagerFrame.class);
	private MetaDataBO metaDataBO=new MetaDataBO();
	private MainFrame mainFrame;
	private MyTableModel tableModel;
	private JPanel panel_type;
	private RfidUtils rfidUtil=RfidUtils.getInstance();
	private Timer displayTimer;
	private JLabel label_id;
	private JLabel label_typeId;
	private JLabel label_cgName;
	private JLabel label_typeStatus;
	private JTable table;
	private List<String> columnList;
	private List<String> columnKeyList;
	private JTable table_mealInfo;
	private MyTableModel mealInfoModel;
	private JTable table_chipInfo;
	private MyTableModel chipInfoModel;
	private JTable table_priceInfo;
	private MyTableModel priceModel;
	private Font defaultFont= new Font("微软雅黑", Font.PLAIN, 20);
	/**
	 * Create the frame.
	 */
	public SaleManagerFrame(MainFrame mainFrame) {
		super();
		this.mainFrame=mainFrame;
		initGUI();
	}
	
	public void initGUI(){
		try {
			TF.dsJson.put("title","运营管理");
			BorderLayout thisLayout = new BorderLayout();
			setLayout(thisLayout);
			JTabbedPane saleTabbedPanel=new JTabbedPane();
			saleTabbedPanel.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					JTabbedPane tabPane=(JTabbedPane)e.getSource();
					int index=tabPane.getSelectedIndex();
					if(index==0){
						displayTimer=new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								try {
									List rfidList=rfidUtil.findRfidID(TF.rfidJson);
									if(rfidList==null||rfidList.size()<1){
										clearInfoInPage();
									}else if(rfidList.size()>1){
//										JOptionPane.showMessageDialog(SaleManagerFrame.this, "超过一个餐具");
									}else if(rfidList.size()==1){
										JSONObject jsonObj=getDataFromCache(rfidList.get(0)+"");
										showInfoInPage(jsonObj);
									}
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						});
						displayTimer.start();
						displayTimer.setRepeats(true);
					}else if(index==1){
						displayTimer.stop();
						JSONArray jsonArr=getDataFromDB("select * from tf_meter_mealtims_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"'");
						if(jsonArr!=null&&jsonArr.size()>0){
							columnKeyList=new ArrayList();
							columnKeyList.add("MACHINE_NO");
							columnKeyList.add("MT_NAME");
							columnKeyList.add("STARTTIME");
							columnKeyList.add("ENDTIME");
							columnKeyList.add("MT_STATUS");
							columnList=new ArrayList();
							columnList.add("机号");
							columnList.add("餐次名称");
							columnList.add("开始时间");
							columnList.add("结束时间");
							columnList.add("状态");
							for(int i=0;i<jsonArr.size();i++){
								int status=jsonArr.getJSONObject(i).getIntValue("MT_STATUS");
								if(status==0){
									jsonArr.getJSONObject(i).put("MT_STATUS", "启用");
								}else{
									jsonArr.getJSONObject(i).put("MT_STATUS", "禁用");
								}
							}
							mealInfoModel.fillTable(jsonArr,columnKeyList,columnList);
						}
					}else if(index==2){
						displayTimer.stop();
						JSONArray jsonArr=getDataFromDB("select * from tf_meter_chiptype_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"'");
						if(jsonArr!=null&&jsonArr.size()>0){
							columnKeyList=new ArrayList();
							columnKeyList.add("MACHINE_NO");
							columnKeyList.add("CG_NAME");
							columnKeyList.add("BUSINESS_ID");
							columnKeyList.add("CG_PICTURE_URL");
							columnKeyList.add("CG_SIZE");
							columnKeyList.add("CG_SHAPE");
							columnKeyList.add("CG_COLOR");
							columnKeyList.add("USE_STATUS");
							columnList=new ArrayList();
							columnList.add("机号");
							columnList.add("餐具名称");
							columnList.add("商户名称");
							columnList.add("图片路径");
							columnList.add("尺寸");
							columnList.add("形状");
							columnList.add("颜色");
							columnList.add("状态");
							chipInfoModel.fillTable(jsonArr,columnKeyList,columnList);
						}
					}else if(index==3){
						displayTimer.stop();
						Vector<Vector<String>> vList=new Vector<Vector<String>>();
						Vector<String> columnVector=new Vector<String>();
						JSONArray jsonArr=getDataFromDB("select * from tf_meter_mealtims_chipcategroy_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"'");
						if(jsonArr!=null&&jsonArr.size()>0){
							List<Map<String,Object>> typeList=metaDataBO.queryForListMap("select * from tf_meter_chiptype_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"'");
							List<Map<String,Object>> mealList=metaDataBO.queryForListMap("select * from tf_meter_mealtims_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"' order by MT_ID");
							List<String> mtIdList=new ArrayList<String>();
							columnVector.add(" ");
							for(Map map:mealList){
								String str=map.get("MT_NAME")+"("+map.get("STARTTIME")+"-"+map.get("ENDTIME")+")";
								columnVector.add(str);
								mtIdList.add(map.get("MT_ID")+"");
							}
							Vector<String> v;
							for(Map<String,Object> typeMap:typeList){
								v=new Vector<String>();
								v.add(typeMap.get("CG_NAME")+"");
								for(int i=0;i<jsonArr.size();i++){
									for(String mtId:mtIdList){
										if(typeMap.get("CG_ID").equals(jsonArr.getJSONObject(i).getString("CG_ID"))&&jsonArr.getJSONObject(i).getString("MT_ID").equals(mtId)){
											v.add(jsonArr.getJSONObject(i).getString("PRICE"));
										}
									}
								}
								vList.add(v);
							}
						}
						priceModel.setDataVector(vList, columnVector);
					}
				}
			});
			add(saleTabbedPanel, BorderLayout.CENTER);
			JPanel dishInfoPanel = new JPanel();
			saleTabbedPanel.addTab("餐具信息", dishInfoPanel);
			dishInfoPanel.setPreferredSize(new Dimension(1020,630));
			dishInfoPanel.setLayout(null);
			
			JPanel mealInfoPanel=new JPanel();
			saleTabbedPanel.addTab("餐次管理", mealInfoPanel);
			mealInfoPanel.setLayout(null);
			
			JScrollPane scroll_mealInfo = new JScrollPane();
			scroll_mealInfo.setBounds(34, 20, 809, 600);
			mealInfoPanel.add(scroll_mealInfo);
			
			mealInfoModel=new MyTableModel();
			table_mealInfo = new JTable();
			table_mealInfo.setFont(defaultFont);
			table_mealInfo.getTableHeader().setFont(defaultFont);
			table_mealInfo.setRowHeight(40);
			table_mealInfo.setModel(mealInfoModel);
			scroll_mealInfo.setViewportView(table_mealInfo);
			
			panel_type = new JPanel();
			panel_type.setBorder(new TitledBorder(null, "\u7C7B\u578B", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_type.setBounds(83, 142, 660, 186);
			dishInfoPanel.add(panel_type);
			panel_type.setLayout(null);
			
			JLabel label_typeId_txt = new JLabel("ID:");
			label_typeId_txt.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_typeId_txt.setBounds(48, 24, 84, 44);
			panel_type.add(label_typeId_txt);
			
			label_typeId = new JLabel("");
			label_typeId.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_typeId.setBounds(142, 24, 235, 44);
			panel_type.add(label_typeId);
			
			JLabel label_cgName_txt = new JLabel("名称:");
			label_cgName_txt.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_cgName_txt.setBounds(48, 78, 84, 34);
			panel_type.add(label_cgName_txt);
			
			label_cgName = new JLabel("");
			label_cgName.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_cgName.setBounds(142, 78, 235, 44);
			panel_type.add(label_cgName);
			
			JLabel label_typeStatus_txt = new JLabel("状态:");
			label_typeStatus_txt.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_typeStatus_txt.setBounds(48, 136, 84, 34);
			panel_type.add(label_typeStatus_txt);
			
			label_typeStatus = new JLabel("");
			label_typeStatus.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_typeStatus.setBounds(142, 136, 235, 34);
			panel_type.add(label_typeStatus);
			
			JPanel panel_id = new JPanel();
			panel_id.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u9910\u5177", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_id.setBounds(83, 25, 660, 81);
			dishInfoPanel.add(panel_id);
			panel_id.setLayout(null);
			
			JLabel lblId_1 = new JLabel("ID:");
			lblId_1.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			lblId_1.setBounds(46, 19, 84, 52);
			panel_id.add(lblId_1);
			
			label_id = new JLabel("");
			label_id.setFont(new Font("微软雅黑", Font.PLAIN, 20));
			label_id.setBounds(144, 19, 261, 52);
			panel_id.add(label_id);
			
			JPanel panel_price = new JPanel();
			panel_price.setBorder(new TitledBorder(null, "\u4EF7\u683C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_price.setBounds(83, 367, 660, 177);
			dishInfoPanel.add(panel_price);
			panel_price.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			scrollPane.setBounds(34, 52, 556, 83);
			panel_price.add(scrollPane);
			
			tableModel=new MyTableModel();
			table = new JTable();
			table.getTableHeader().setFont(defaultFont);
			table.setFont(defaultFont);
			table.setRowHeight(40);
			table.setModel(tableModel);
			scrollPane.setViewportView(table);
			
			JPanel chipInfoPanel = new JPanel();
			chipInfoPanel.setLayout(null);
			saleTabbedPanel.addTab("餐具管理", chipInfoPanel);
			
			JScrollPane scroll_chipInfo = new JScrollPane();
			scroll_chipInfo.setBounds(34, 20, 809, 600);
			chipInfoPanel.add(scroll_chipInfo);
			
			chipInfoModel=new MyTableModel();
			table_chipInfo = new JTable();
			table_chipInfo.getTableHeader().setFont(defaultFont);
			table_chipInfo.setFont(defaultFont);
			table_chipInfo.setRowHeight(40);
			table_chipInfo.setModel(chipInfoModel);
			scroll_chipInfo.setViewportView(table_chipInfo);
			
			JPanel priceInfoPanel = new JPanel();
			priceInfoPanel.setLayout(null);
			saleTabbedPanel.addTab("价格管理", priceInfoPanel);
			
			JScrollPane scroll_priceInfo = new JScrollPane();
			scroll_priceInfo.setBounds(34, 20, 809, 600);
			priceInfoPanel.add(scroll_priceInfo);
			
			priceModel=new MyTableModel();
			table_priceInfo = new JTable();
			table_priceInfo.getTableHeader().setFont(defaultFont);
			table_priceInfo.setFont(defaultFont);
			table_priceInfo.setRowHeight(40);
			table_priceInfo.setModel(priceModel);
			scroll_priceInfo.setViewportView(table_priceInfo);
			
			this.setSize(1024, 768);
			}catch (Exception e) {
				log.error(e.getMessage());
			    //add your error handling code here
				e.printStackTrace();
			}
	}
	
	public JSONObject getDataFromCache(String id){
//		Map map=metaDataBO.queryForMap("select * from tf_chipinfo where CP_ID='"+id+"'");
//		List list1=(List<Map<String,String>>)TF.chipJson.get("chipinfo");
//		List list2=(List<Map<String,String>>)TF.chipJson.get("category");
//		List list3=(List<Map<String,String>>)TF.chipJson.get("chipprice");
//		System.out.println(list1.size()+"--"+list2.size()+"---"+list3.size());
		JSONObject infoMap=new JSONObject();
//		for(String cpNo:TF.chipJson.getJSONObject("chipinfo").keySet()){
//			if(id.equals(cpNo)){
				String cgid=TF.chipJson.getJSONObject("chipinfo").getJSONObject(id).getString("CG_ID");
				infoMap.put("cgId", cgid);
//				for(String category:TF.chipJson.getJSONObject("category").keySet()){
//					if(cgid.equals(TF.chipJson.getJSONObject("category").getJSONObject(category).getString("CG_ID"))&&TF.chipJson.getJSONObject("category").getJSONObject(category).getString("MACHINE_NO").equals(TF.dsJson.getString("machineId"))){
						String cgName=TF.chipJson.getJSONObject("category").getJSONObject(cgid).getString("CG_NAME");
						String useStatus=TF.chipJson.getJSONObject("category").getJSONObject(cgid).getString("USE_STATUS");
						infoMap.put("cgName", cgName);
						infoMap.put("useStatus", useStatus);
//					}
//				}
				infoMap.put("mealPrice", new JSONObject());
//				for(String chipPrice:TF.chipJson.getJSONObject("chipprice").keySet()){
//				for(int i=0;i<TF.chipJson.getJSONArray("chipprice").size();i++){
//					if(cgid.equals(TF.chipJson.getJSONArray("chipprice").getJSONObject(i).getString("CG_ID"))&&TF.chipJson.getJSONArray("chipprice").getJSONObject(i).getString("MACHINE_NO").equals(TF.dsJson.getString("machineId"))){
//						infoMap.getJSONObject("mealPrice").put(TF.chipJson.getJSONArray("chipprice").getJSONObject(i).getString("MT_ID"), TF.chipJson.getJSONArray("chipprice").getJSONObject(i).getString("PRICE"));
//					}
//				}
				for(String mt:TF.chipJson.getJSONObject("chipprice").keySet()){
//					JSONObject priceJson=TF.chipJson.getJSONArray("chipprice").getJSONObject(i);
//					if(priceJson.getString("CG_ID").equals(cgid)&&priceJson.getString("MACHINE_NO").equals(TF.dsJson.getString("machineId"))){
						infoMap.getJSONObject("mealPrice").put(mt, TF.chipJson.getJSONObject("chipprice").getJSONObject(mt).getJSONObject(cgid).getString("PRICE"));
//					}
				}
//			}
//		}
		infoMap.put("cpNo", id);
//		if(infoMap.size()>0){
//			label_type.setText(infoMap.get("cgid"));
//			label_price.setText(infoMap.get("price"));
//			label_createtime.setText(infoMap.get("createtime"));
//			label_merchant.setText(infoMap.get("mcname"));
//			label_id.setText(infoMap.get("cpid"));
//		}
		return infoMap;
	}
	
	public JSONArray getDataFromDB(String sql){
		List<Map<String,Object>> mealList=metaDataBO.queryForListMap(sql);
		JSONArray jsonArr=new JSONArray();
		JSONObject jsonObj;
		if(mealList!=null&&mealList.size()>0){
			for(Map<String,Object> map:mealList){
				jsonObj=new JSONObject();
				for(String key:map.keySet()){
					jsonObj.put(key, map.get(key));
				}
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr;
	}
	
	public void showInfoInPage(JSONObject jsonObj){
		label_id.setText(jsonObj.get("cpNo")+"");
		label_typeId.setText(jsonObj.get("cgId")+"");
		label_cgName.setText(jsonObj.get("cgName")+"");
		label_typeStatus.setText((jsonObj.get("useStatus")+"").equals("0")?"启用":"禁用");
		if(jsonObj.getJSONObject("mealPrice")==null){
			return;
		}
		tableModel.fillTable(jsonObj.getJSONObject("mealPrice"));
		
	}
	
	public void clearInfoInPage(){
		label_id.setText("");
		label_typeId.setText("");
		label_cgName.setText("");
		label_typeStatus.setText("");
		tableModel.setRowCount(0);
	}
	
	/**
	 * 自定义table类
	 * @author Administrator
	 *
	 */
	private class MyTableModel extends DefaultTableModel{
		public MyTableModel(){
			super();
		}
		public void fillTable(JSONObject jsonObj){
			Vector<Vector<String>> valueVector=new Vector<Vector<String>>();
			Vector<String> v;
			v=new Vector<String>();
			Vector<String> columnVector=new Vector<String>();
			for(String str:jsonObj.keySet()){
				v.add(jsonObj.getString(str)+"");
				Map map=metaDataBO.queryForMap("select MT_NAME from tf_meter_mealtims_relation where MACHINE_NO='"+TF.dsJson.getString("machineId")+"' and MT_ID='"+str+"'");
				if(map==null||map.size()<1){
					continue;
				}
				String columnName=map.get("MT_NAME")+"";
				columnVector.add(columnName);
			}
			valueVector.add(v);
//			page=new PageInfo(valueVector);
//			page.setTotalSize(valueVector.size());
			super.setDataVector(valueVector, columnVector);
		}
		
		public void fillTable(JSONArray dataList,List<String> columnKeyList,List<String> columnList){
			Vector<Vector<String>> valueVector=new Vector<Vector<String>>();
			Vector<String> v;
			for(int i=0;i<dataList.size();i++){
				v=new Vector<String>();
				for(String str:columnKeyList){
					v.add(dataList.getJSONObject(i).get(str)+"");
				}
				valueVector.add(v);
			}
			Vector<String> columnVector=new Vector<String>();
			for(String column:columnList){
				columnVector.add(column);
			}
//			page=new PageInfo(valueVector);
//			page.setTotalSize(valueVector.size());
			super.setDataVector(valueVector, columnVector);
		}
		
		public void fillRow(JSONObject rowList){
			Vector<String> v=new Vector<String>();
			for(String str:rowList.keySet()){
				v.add(rowList.getString(str));
			}
			super.addRow(v);
		}
		
		public void updateRow(JSONObject rowList){
		}
}
	
	@Override
	public void addListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTimer() {
		// TODO Auto-generated method stub
		displayTimer.setRepeats(true);
		displayTimer.start();
	}

	@Override
	public void cancleTimer() {
		// TODO Auto-generated method stub
		displayTimer.setRepeats(false);
		displayTimer.stop();
	}

	@Override
	public void removeListeners() {
		// TODO Auto-generated method stub
		
	}
	
	public void closeTF(){
		int flag = JOptionPane.showConfirmDialog(SaleManagerFrame.this,"是否退出系统？","退出",JOptionPane.YES_NO_OPTION);   
        if(flag==0) { 
			JSONObject uploadJson=new JSONObject();
			uploadJson.put("ONLINE_STATUS", 0);
			metaDataBO.execute(metaDataBO.toSql(uploadJson, "tf_meter_operation_record", "SEQNO"));
			System.exit(0);   
        } 
	}
}
