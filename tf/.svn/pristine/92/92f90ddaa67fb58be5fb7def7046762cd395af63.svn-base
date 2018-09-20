package com.kbs.test;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class TestSwingWorker extends JFrame {
	    private JProgressBar bar = new JProgressBar(0, 100);  
	    private JTextArea area = new JTextArea();  
	      
	    public TestSwingWorker() {  
	        setTitle("swingworker和进度条的小例子");  
	        add(bar, BorderLayout.CENTER);  
	        add(area, BorderLayout.SOUTH);  
	        pack();  
	        setVisible(true);  
	        start();  
	    }  
	    private void start() {  
	        ProGressWork work = new ProGressWork();  
	        work.addPropertyChangeListener(new PropertyChangeListener(){  
	            @Override  
	            public void propertyChange(PropertyChangeEvent evt) {  
	                System.out.print(evt.getNewValue());  
	            }  
	        });  
	        work.execute();  
	    }  
	    public static void main(String[] args) {  
	        SwingUtilities.invokeLater(new Runnable() {  
	            @Override  
	            public void run() {  
	                new TestSwingWorker();  
	            }  
	        });  
	    }  
	      
	    class ProGressWork extends SwingWorker<List<Work>, Work> {  
	        @Override  
	        protected List<Work> doInBackground() throws Exception {  
	            int i = 0;  
	            List<Work> list = new ArrayList<Work>();  
	            //模拟耗时任务  
	            //假设有100项待完成任务  
	            while (i < 100) {  
	                i++;  
	                /*******模拟开始一项新工作*******/  
	                Work w = new Work(i);  
	                list.add(w);  
	                publish(w);  
	                setProgress(100 * list.size() / 100);  
	                Thread.sleep(100);  
	                /*******模拟完成此项工作*******/  
	            }  
	            return list;  
	        }  
	        //调用publist的时候会调用  
	        //注意这里是"批处理"  
	        @Override  
	        protected void process(List<Work> works) {  
	            for (Work work : works) {  
	                bar.setValue(work.getId());  
	            }  
	        }  
	        @Override  
	        protected void done() {  
	            area.append("工作已全部完成");  
	        }  
	    }  
	    class Work {  
	        //工作编号  
	        private int id;  
	        public Work(int id) {  
	            this.id = id;  
	        }  
	        public int getId() {  
	            return id;  
	        }  
	    }  
	}  
