package com.kbs.swing;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.kbs.commons.metadata.MetaDataBO;
import com.kbs.util.DatabaseBackup;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RestoreFrame extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private MyTableModel tableModel;
	private MetaDataBO metaDataBO=new MetaDataBO();
	private JButton button;
	private JLabel label_msg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestoreFrame frame = new RestoreFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RestoreFrame() {
		initGUI();
		fillTable();
	}

	public void initGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		tableModel = 
			new MyTableModel(
					new String[][] {  },
					new String[] { "文件名", "路径","创建时间","操作人"});
		
		
		DefaultTableCellRenderer dc = 
			new DefaultTableCellRenderer();
		//设置包装器对齐方式
		dc.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(126, 129, 746, 439);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		//将样式包装器设置到JTable对象
		table.setDefaultRenderer(Object.class, dc);
		table.setModel(tableModel);
		table.setRowHeight(50);
		
		button = new JButton("恢复");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(table.getSelectedRow()!=-1){
					int i=JOptionPane.showConfirmDialog(RestoreFrame.this, "确定要恢复数据库吗?","标题",JOptionPane.YES_NO_OPTION);
					if(i==0){
						DatabaseBackup bak = new DatabaseBackup("kbs", "kbs0755");
						String filename=tableModel.getValueAt(table.getSelectedRow(), 1)+"/"+tableModel.getValueAt(table.getSelectedRow(), 0);
						int flag=bak.runBat("restore.bat",filename);
						if(flag==0){
							label_msg.setText("恢复成功");
						}
					}
				}
			}
		});
		button.setBounds(779, 607, 93, 23);
		contentPane.add(button);
		
		label_msg = new JLabel("");
		label_msg.setBounds(332, 611, 385, 15);
		contentPane.add(label_msg);
		this.setSize(1024, 768);
	}
	
	
	public void fillTable(){
		tableModel.setRowCount(0);
		List<Map<String,Object>> backupList=metaDataBO.queryForListMap("select * from tf_backup");
		if(backupList==null||backupList.size()<1){
			return;
		}
		List<Vector> vectorList=new ArrayList();
		Vector v;
		for(Map m:backupList){
			v=new Vector();
			v.add(m.get("BNAME"));
			v.add(m.get("BPATH"));
			v.add(m.get("CREATETIME"));
			v.add(m.get("UID"));
			vectorList.add(v);
		}
		for(Vector vector:vectorList){
			tableModel.addRow(vector);
		}
	}
	
	/**
	 * 自定义table类
	 * @author Administrator
	 *
	 */
	private class MyTableModel extends DefaultTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyTableModel(String[][] arr1,String[] arr2){
			super(arr1,arr2);
		}
		
		public boolean isCellEditable(int row, int column)
        {
			return false;
        }
	}
}
