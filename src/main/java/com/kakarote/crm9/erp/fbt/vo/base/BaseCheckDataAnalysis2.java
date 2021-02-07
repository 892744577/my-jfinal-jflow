package com.kakarote.crm9.erp.fbt.vo.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCheckDataAnalysis2<M extends BaseCheckDataAnalysis2<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setUserName(String userName) {
		set("userName", userName);
	}
	
	public String getUserName() {
		return getStr("userName");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}
	
	public String getPhone() {
		return getStr("phone");
	}

	public void setStayDay(String stayDay) {
		set("stayDay", stayDay);
	}
	
	public String getStayDay() {
		return getStr("stayDay");
	}

	public void setStayPrice(java.math.BigDecimal stayPrice) {
		set("stayPrice", stayPrice);
	}
	
	public java.math.BigDecimal getStayPrice() {
		return get("stayPrice");
	}

	public void setStayCity(String stayCity) {
		set("stayCity", stayCity);
	}
	
	public String getStayCity() {
		return getStr("stayCity");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("createTime", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("createTime");
	}

}