package com.kakarote.crm9.erp.admin.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePortActivityHelper<M extends BasePortActivityHelper<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setAssistId(java.lang.Integer assistId) {
		set("assistId", assistId);
	}

	public java.lang.Integer getAssistId() {
		return getInt("assistId");
	}

	public void setHelperAppOpenId(java.lang.String helperAppOpenId) {
		set("helperAppOpenId", helperAppOpenId);
	}

	public java.lang.String getHelperAppOpenId() {
		return getStr("helperAppOpenId");
	}

	public void setHelperOpenId(java.lang.String helperOpenId) {
		set("helperOpenId", helperOpenId);
	}

	public java.lang.String getHelperOpenId() {
		return getStr("helperOpenId");
	}

	public void setHelperInfo(java.lang.String helperInfo) {
		set("helperInfo", helperInfo);
	}

	public java.lang.String getHelperInfo() {
		return getStr("helperInfo");
	}

}
