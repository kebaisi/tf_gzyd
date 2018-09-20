package com.kbs.swing.frame;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.kbs.swing.CommonInterface;
import com.kbs.util.TF;

public class HelpPanel extends JPanel implements CommonInterface{

	private MainFrame mainFrame;
	private JTree tree_left;
	private JSplitPane splitPane;
	private JPanel panel_1;
 
	public HelpPanel(MainFrame mainFrame) {
		super();
		this.mainFrame=mainFrame;
		initGUI();
	}
	
	public void initGUI(){
//		this.setTitle("帮助");
		TF.dsJson.put("title", "帮助");
		BorderLayout thisLayout = new BorderLayout();
		setLayout(thisLayout);
		
		JPanel panel = new JPanel();
		DefaultMutableTreeNode root =new  DefaultMutableTreeNode("帮助");
		DefaultMutableTreeNode parent =new  DefaultMutableTreeNode("在线帮助");
		DefaultMutableTreeNode intro =new  DefaultMutableTreeNode("使用说明书");
		
		DefaultMutableTreeNode tips =new  DefaultMutableTreeNode("文字提示");
		DefaultMutableTreeNode repair =new  DefaultMutableTreeNode("远程维护");
		DefaultMutableTreeNode problem =new  DefaultMutableTreeNode("常见问题列表");
		DefaultMutableTreeNode update =new  DefaultMutableTreeNode("在线升级");
		parent.add(tips);
		parent.add(repair);
		parent.add(problem);
		parent.add(update);
		root.add(parent);
		root.add(intro);
		panel.setLayout(new BorderLayout());
		
		
		splitPane = new JSplitPane();
		
		tree_left = new JTree(root);
		splitPane.setLeftComponent(tree_left);
		tree_left.setPreferredSize(new java.awt.Dimension(170, 578));
		
		panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel.add(splitPane, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		this.setSize(1024, 768);
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
}
