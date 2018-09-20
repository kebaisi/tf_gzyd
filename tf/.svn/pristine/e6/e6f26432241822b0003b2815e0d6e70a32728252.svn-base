package com.kbs.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowUtil {
	public static void centerWindow(Component component){ 
        Toolkit toolkit=Toolkit.getDefaultToolkit(); 
        Dimension scmSize=toolkit.getScreenSize(); 
        Dimension size=component.getPreferredSize(); 
        int width=component.getWidth(), height=component.getHeight(); 
        component.setLocation(scmSize.width/2-(width/2),scmSize.height/2-(height/2));
    } 
}
