package com.kakarote.crm9.erp.admin.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePortActivityAssist<M extends BasePortActivityAssist<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}
	
	public Long getId() {
		return getLong("id");
	}

	public void setAsAcId(Integer asAcId) {
		set("as_ac_id", asAcId);
	}
	
	public Integer getAsAcId() {
		return getInt("as_ac_id");
	}

	public void setAsOpenid(String asOpenid) {
		set("as_openid", asOpenid);
	}
	
	public String getAsOpenid() {
		return getStr("as_openid");
	}

	public void setAsMobile(String asMobile) {
		set("as_mobile", asMobile);
	}
	
	public String getAsMobile() {
		return getStr("as_mobile");
	}

	public void setAsName(String asName) {
		set("as_name", asName);
	}
	
	public String getAsName() {
		return getStr("as_name");
	}

	public void setAsAddress(String asAddress) {
		set("as_address", asAddress);
	}
	
	public String getAsAddress() {
		return getStr("as_address");
	}

	public void setAsProductid(String asProductid) {
		set("as_productid", asProductid);
	}
	
	public String getAsProductid() {
		return getStr("as_productid");
	}

	public void setAsInfo(String asInfo) {
		set("as_info", asInfo);
	}
	
	public String getAsInfo() {
		return getStr("as_info");
	}

}
