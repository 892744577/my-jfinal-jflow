package com.landray.kmss.sys.organization.client;

import java.util.Properties;

/**
 * 解析配置文件
 * 
 */
public class WebServiceConfig {
	private static WebServiceConfig cfg = new WebServiceConfig();

	// Web服务的URL
	private String address;
	
	// Web服务接口
	private Class serviceClass;
	
	// Web服务标识
	private String serviceBean;
	
	// 用户
	private String user;
	
	// 密码
	private String password;

	private WebServiceConfig() {
		loadCfg();
	}

	public static WebServiceConfig getInstance() {
		return cfg;
	}

	/**
	 * 解析配置文件
	 */
	private void loadCfg() {
		Properties prop = new Properties();
		prop.setProperty("address","http://tenonoa.aptenon.com:9300/sys/webservice/sysSynchroGetOrgWebService");
		prop.setProperty("service_class","com.landray.kmss.sys.organization.webservice.out.ISysSynchroGetOrgWebService");
		prop.setProperty("service_bean","sysSynchroGetOrgWebService");
		prop.setProperty("user","");
		prop.setProperty("password","");


		this.address = prop.getProperty("address");
		String serviceClassName = prop.getProperty("service_class");
		try {
			this.serviceClass = Class.forName(serviceClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.serviceBean = prop.getProperty("service_bean");
		this.user = prop.getProperty("user");
		this.password = prop.getProperty("password");

	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Class getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(Class serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceBean() {
		return serviceBean;
	}

	public void setServiceBean(String serviceBean) {
		this.serviceBean = serviceBean;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
