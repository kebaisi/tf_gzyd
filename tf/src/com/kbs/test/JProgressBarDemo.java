package com.kbs.test;

/** 
 * java swing 之进度条的使用 
 * @author gao 
 */  
  
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
  
public class JProgressBarDemo extends JFrame {  
    public JProgressBarDemo(){  
        this.setTitle("进度条的使用");  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.setBounds(100, 100, 250, 100);  
        JPanel contentPane=new JPanel();  
        contentPane.setBorder(new EmptyBorder(5,5,5,5));  
        this.setContentPane(contentPane);  
        contentPane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));  
        final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100) {
            public Dimension getPreferredSize() {
                return new Dimension(50, 5);
            }
        };
        progressBar.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        progressBar.setForeground(Color.GREEN);
        progressBar.setStringPainted(false);  
        new Thread(){  
            public void run(){  
                for(int i=0;i<=100;i++){  
                    try{  
                        Thread.sleep(100);  
                    }catch(InterruptedException e){  
                        e.printStackTrace();  
                    }  
                      progressBar.setValue(i);  
                }  
                progressBar.setString("升级完成！");  
            }  
        }.start();  
        contentPane.add(progressBar);  
        this.setVisible(true);  
    }  
    public static void main(String[]args){  
    	
//    	try
//      {
//          org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
//          UIManager.put("RootPane.setupButtonVisible",false);
//      }
//      catch(Exception e)
//      {
//          //TODO exception
//      }
        JProgressBarDemo example=new JProgressBarDemo();  
    }  
}  