package com.kbs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import com.kbs.util.GetProjectRealPath;

public class BottomPanel extends JPanel {
	private JLabel label_version;
	private JLabel label_companyName;
	private JLabel label_machineNo;
	private JLabel label_time;
	private JLabel lblNewLabel;

//	private JPanel contentPane;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BottomPanel frame = new BottomPanel();
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
	public BottomPanel() {
		initGUI();
//		setUndecorated(true);
	}

	public void initGUI(){
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 30);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
		setLayout(null);
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		label_version = new JLabel("4.0.0.1");
		label_version.setFont(new Font("微软雅黑", Font.ITALIC, 14));
		label_version.setBounds(45, 0, 104, 30);
		add(label_version);
		
		label_companyName = new JLabel("深圳市科拜斯物联网科技有限公司 400-1680881");
		label_companyName.setHorizontalAlignment(SwingConstants.CENTER);
		label_companyName.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_companyName.setBounds(190, 0, 400, 30);
		add(label_companyName);
		
		label_machineNo = new JLabel("001");
		label_machineNo.setHorizontalAlignment(SwingConstants.RIGHT);
		label_machineNo.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_machineNo.setBounds(650, 0, 140, 30);
		add(label_machineNo);
		
		label_time = new JLabel("2016-03-08 14:00:00   ");
		label_time.setHorizontalAlignment(SwingConstants.CENTER);
		label_time.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label_time.setBounds(800, 0, 224, 30);
		add(label_time);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/version.png")));
		lblNewLabel.setBounds(10, 0, 44, 30);
		add(lblNewLabel);
	}
	
	public JLabel getLabel_version() {
		return label_version;
	}

	public void setLabel_version(JLabel label_version) {
		this.label_version = label_version;
	}

	public JLabel getLabel_companyName() {
		return label_companyName;
	}

	public void setLabel_companyName(JLabel label_companyName) {
		this.label_companyName = label_companyName;
	}

	public JLabel getLabel_machineNo() {
		return label_machineNo;
	}

	public void setLabel_machineNo(JLabel label_machineNo) {
		this.label_machineNo = label_machineNo;
	}

	public JLabel getLabel_time() {
		return label_time;
	}

	public void setLabel_time(JLabel label_time) {
		this.label_time = label_time;
	}
	
}
