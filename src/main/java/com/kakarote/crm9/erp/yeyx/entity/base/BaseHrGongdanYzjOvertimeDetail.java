package com.kakarote.crm9.erp.yeyx.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanYzjOvertimeDetail<M extends BaseHrGongdanYzjOvertimeDetail<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setOvertimeType(String overtimeType) {
		set("overtimeType", overtimeType);
	}
	
	public String getOvertimeType() {
		return getStr("overtimeType");
	}

	public void setOvertimeHours(String overtimeHours) {
		set("overtimeHours", overtimeHours);
	}
	
	public String getOvertimeHours() {
		return getStr("overtimeHours");
	}

	public void setOvertimeBegin(java.util.Date overtimeBegin) {
		set("overtimeBegin", overtimeBegin);
	}
	
	public java.util.Date getOvertimeBegin() {
		return get("overtimeBegin");
	}

	public void setOvertimeEnd(java.util.Date overtimeEnd) {
		set("overtimeEnd", overtimeEnd);
	}
	
	public java.util.Date getOvertimeEnd() {
		return get("overtimeEnd");
	}

	public void setOvertimeReason(String overtimeReason) {
		set("overtimeReason", overtimeReason);
	}
	
	public String getOvertimeReason() {
		return getStr("overtimeReason");
	}

	public void setSerialNumWidget(String serialNumWidget) {
		set("serialNumWidget", serialNumWidget);
	}
	
	public String getSerialNumWidget() {
		return getStr("serialNumWidget");
	}

}