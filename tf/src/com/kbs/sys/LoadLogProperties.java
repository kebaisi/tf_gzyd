package com.kbs.sys;

import org.apache.log4j.PropertyConfigurator;

import com.kbs.util.GetProjectRealPath;

public class LoadLogProperties {
	public LoadLogProperties(){
		PropertyConfigurator.configure( GetProjectRealPath.getPath("log4j_tf.properties") );
	}
}
