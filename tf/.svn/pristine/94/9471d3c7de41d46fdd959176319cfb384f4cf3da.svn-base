package com.kbs.swing.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.kbs.util.GetProjectRealPath;

public class ErrorMsgPanel extends JPanel {
	private JLabel label_errorTitle;
	private JEditorPane editorPane;

	public JLabel getLabel_errorTitle() {
		return label_errorTitle;
	}

	public void setLabel_errorTitle(JLabel label_errorTitle) {
		this.label_errorTitle = label_errorTitle;
	}

	public JEditorPane getEditorPane() {
		return editorPane;
	}

	public void setEditorPane(JEditorPane editorPane) {
		this.editorPane = editorPane;
	}

	/**
	 * Create the panel.
	 */
	public ErrorMsgPanel() {
		setLayout(null);
		
		JPanel panel_errorMsg = new JPanel();
		panel_errorMsg.setBounds(65, 50, 385, 249);
		add(panel_errorMsg);
		panel_errorMsg.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scroll_error = new JScrollPane();
		panel_errorMsg.add(scroll_error, BorderLayout.CENTER);
		
		editorPane = new JEditorPane();
		editorPane.setEnabled(false);
		scroll_error.setViewportView(editorPane);
		
		JPanel panel_errorTitle = new JPanel();
		panel_errorTitle.setBounds(1, 1, 448, 49);
		add(panel_errorTitle);
		panel_errorTitle.setLayout(null);
		
		JLabel label_errorTitle_txt = new JLabel("标题:");
		label_errorTitle_txt.setBounds(0, 0, 60, 50);
		label_errorTitle_txt.setHorizontalAlignment(SwingConstants.CENTER);
		panel_errorTitle.add(label_errorTitle_txt);
		
		label_errorTitle = new JLabel("");
		label_errorTitle.setBounds(65, 0, 320, 50);
		panel_errorTitle.add(label_errorTitle);
		
		JButton btn_err_close = new JButton("");
		btn_err_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ErrorMsgPanel.this.setVisible(false);
			}
		});
		btn_err_close.setIcon(new ImageIcon(GetProjectRealPath.getPath("images/cancle2.png")));
		btn_err_close.setBounds(418, 0, 32, 32);
		panel_errorTitle.add(btn_err_close);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 48, 450, 1);
		panel_errorTitle.add(separator);
		
		JLabel lblNewLabel = new JLabel("内容:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1, 50, 59, 50);
		add(lblNewLabel);
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
	}
}
