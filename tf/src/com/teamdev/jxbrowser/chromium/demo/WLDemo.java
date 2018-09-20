package com.teamdev.jxbrowser.chromium.demo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jack.jxbroser.JackNetworkDelegate4A47;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class WLDemo
{
    public static void main(String[] args) {
        for(int i =0;i<1;i++)
        {
            heihei();
        }
       
    }
    
    public static void heihei(){
        final Browser browser = new Browser();
        BrowserView browserView = new BrowserView(browser);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(browserView, BorderLayout.CENTER);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // Handle proxy authorization.
       // browser.getContext().getNetworkService().setNetworkDelegate(new JackNetworkDelegate4A47(frame,browserView));  
       // browser.loadURL("https://api.47ks.com/webcloud/?v=http://film.sohu.com/album/9353216.html?channeled=1200110001&type=lemid");
        browser.loadURL("http://baidu.com");
        
    }
}
