package com.kbs.test;

import javax.swing.UIManager;

public class TestUIManager {
public static void main(String[] args) {
	for(Object key:UIManager.getDefaults().keySet()){
		System.out.println(UIManager.getDefaults().get(key));
	}
}
}
