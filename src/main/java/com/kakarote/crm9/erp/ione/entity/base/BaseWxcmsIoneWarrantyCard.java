package com.kakarote.crm9.erp.ione.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWxcmsIoneWarrantyCard<M extends BaseWxcmsIoneWarrantyCard<M>> extends Model<M> implements IBean {

	public void setUuid(String uuid) {
		set("uuid", uuid);
	}
	
	public String getUuid() {
		return getStr("uuid");
	}

	public void setProductUuid(String productUuid) {
		set("product_uuid", productUuid);
	}
	
	public String getProductUuid() {
		return getStr("product_uuid");
	}

	public void setMcuId(String mcuId) {
		set("mcu_id", mcuId);
	}
	
	public String getMcuId() {
		return getStr("mcu_id");
	}

	public void setStartDate(java.util.Date startDate) {
		set("start_date", startDate);
	}
	
	public java.util.Date getStartDate() {
		return get("start_date");
	}

	public void setEndDate(java.util.Date endDate) {
		set("end_date", endDate);
	}
	
	public java.util.Date getEndDate() {
		return get("end_date");
	}

	public void setStatus(String status) {
		set("status", status);
	}
	
	public String getStatus() {
		return getStr("status");
	}

	public void setCreateUserUuid(String createUserUuid) {
		set("create_user_uuid", createUserUuid);
	}
	
	public String getCreateUserUuid() {
		return getStr("create_user_uuid");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("create_date", createDate);
	}
	
	public java.util.Date getCreateDate() {
		return get("create_date");
	}

	public void setModiUserUuid(String modiUserUuid) {
		set("modi_user_uuid", modiUserUuid);
	}
	
	public String getModiUserUuid() {
		return getStr("modi_user_uuid");
	}

	public void setModiDate(java.util.Date modiDate) {
		set("modi_date", modiDate);
	}
	
	public java.util.Date getModiDate() {
		return get("modi_date");
	}

}
