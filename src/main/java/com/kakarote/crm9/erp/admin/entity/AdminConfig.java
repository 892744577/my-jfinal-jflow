package com.kakarote.crm9.erp.admin.entity;


import com.kakarote.crm9.erp.admin.entity.base.BaseAdminConfig;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AdminConfig extends BaseAdminConfig<AdminConfig> {
	public static final AdminConfig dao = new AdminConfig().dao();
	public static AdminConfig getConfig(String name){
		return dao.findFirst("select * from aptenon_admin_config where name =? limit 0,1",name);
	}
	public static String getValue(String name){
		return getConfig(name).getValue();
	}
	public static Integer getStatus(String name){
		return getConfig(name).getStatus();
	}
}
