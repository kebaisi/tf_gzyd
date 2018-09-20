package com.kbs.util;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author 
 */
public class ConfigPro
{
	private static ConfigPro config = null;
	private Properties props = null; 
	
	public final static String CONFIG_FILE = GetProjectRealPath.getPath("ds.properties");
	
	private void init()
	{
		try 
		{
			props = new Properties();
//			PropertyConfigurator.configure( GetProjectRealPath.getPath("ds.properties") );
//			InputStream in = ConfigPro.this.getClass().getResourceAsStream(CONFIG_FILE);
			InputStream in = new BufferedInputStream (new FileInputStream(CONFIG_FILE));
			props.load(in);
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}		
	}
	
	private ConfigPro()
	{
		init();
	}
	
	public synchronized static ConfigPro getInstance()
	{
		if ( config==null ) config = new ConfigPro();
		return config;
	}
	
	public String getProperty(String key)
	{
		return props.getProperty(key);
	}
	
	public String getProperty(String key,String df)
	{
		return props.getProperty(key,df);
	}
	
	/**
	 * 设置配置参数.
	 * @param key
	 * @param value
	 */
	public void setProperty(String key,String value)
	{
		props.setProperty(key,value);
	}
	
	/**
	 * 保存配置文件.
	 *
	 */
	public void store()
	{
		try
        {
			OutputStream outputFile = new FileOutputStream(CONFIG_FILE);
            props.store(outputFile,"ds");
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
	}

    public Properties getProperties()
    {
        return props;
    }


public static void main(String[] args) {
	ConfigPro configPro=new ConfigPro();
	System.out.println(configPro.CONFIG_FILE);
}
}
