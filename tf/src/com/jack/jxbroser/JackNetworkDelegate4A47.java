package com.jack.jxbroser;

import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;

import com.teamdev.jxbrowser.chromium.BeforeSendHeadersParams;
import com.teamdev.jxbrowser.chromium.DataReceivedParams;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate;

public class JackNetworkDelegate4A47 extends DefaultNetworkDelegate
{
    private  JFrame frame;
    private BrowserView browserView;
    
    public JackNetworkDelegate4A47( JFrame frame,BrowserView browserView)
    {
        this.frame = frame;
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.browserView = browserView;
    }
    
    
    public void onBeforeSendHeaders(BeforeSendHeadersParams params)
    {
        params.getHeadersEx().setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 6.0.1; zh-cn; ZUK Z2131 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.5 Mobile Safari/537.36");
        params.getHeadersEx().setHeader("upgrade-insecure-requests","1");
        params.getHeadersEx().setHeader("cache-control","max-age=0");
        params.getHeadersEx().setHeader("accept-encoding","gzip, deflate, sdch, br");
        params.getHeadersEx().setHeader("accept-language","zh-CN,zh;q=0.8");
        params.getHeadersEx().setHeader("upgrade-insecure-requests","1");
        params.getHeadersEx().removeHeader("Connection");
    }
    public void onDataReceived(DataReceivedParams params)
    {
       // System.out.println(params.getURL());
        if(params.getURL().contains("webmain.php"))
        {
            System.out.println(new String(params.getData()));
            params.getBrowser().dispose();
            frame.dispose();
            frame = null;
        }
        // params.getData()
    }
    
}
