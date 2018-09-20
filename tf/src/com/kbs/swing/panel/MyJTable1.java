package com.kbs.swing.panel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


public class MyJTable1 extends JTable{
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		// TODO Auto-generated method stub
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
//        super.editCellAt(rowIndex, columnIndex, null);
//        if(this.getValueAt(rowIndex, columnIndex) instanceof JButton){
//        	JButton btn=(JButton)this.getValueAt(rowIndex, columnIndex);
//        	btn.doClick();
//        }
	}

	public  MyJTable1(){
		super();
		basicConfig();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		if(this.getValueAt(row, column) instanceof JButton){
			return true;
		}
		return false;
	}
	
	public MyJTable1(TableModel model){
		super();
		basicConfig();
		this.setModel(model);
	}
	
	public MyJTable1(TableModel tm,TableColumnModel tcm,ListSelectionModel lsm ){
		super(tm, null, null);
	}
	
	public void fillTable(List<Map> dataList){
		DefaultTableModel model=(DefaultTableModel) this.getModel();
		Vector v;
		if(dataList==null||dataList.size()<1)
			return;
		for(Map<String,Object> map:dataList){
			v=new Vector();
			for(String key:map.keySet()){
				v.add(map.get(key));
			}
			model.addRow(v);
		}
	}
	
	public void basicConfig(){
		this.setRowHeight(50);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
