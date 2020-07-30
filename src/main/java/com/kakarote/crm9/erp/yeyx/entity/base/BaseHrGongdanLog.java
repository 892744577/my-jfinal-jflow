package com.kakarote.crm9.erp.yeyx.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanLog<M extends BaseHrGongdanLog<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}
	
	public Long getId() {
		return getLong("id");
	}

	public void setPreServiceNo(String preServiceNo) {
		set("preServiceNo", preServiceNo);
	}
	
	public String getPreServiceNo() {
		return getStr("preServiceNo");
	}

	public void setEmpNo(String empNo) {
		set("empNo", empNo);
	}
	
	public String getEmpNo() {
		return getStr("empNo");
	}

	public void setEmpName(String empName) {
		set("empName", empName);
	}
	
	public String getEmpName() {
		return getStr("empName");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}
	
	public String getRemark() {
		return getStr("remark");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
