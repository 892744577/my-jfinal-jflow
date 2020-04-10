package com.erpsnow.erp;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCrmReceivables<M extends BaseCrmReceivables<M>> extends Model<M> implements IBean {

	public void setReceivablesId(java.lang.Integer receivablesId) {
		set("receivables_id", receivablesId);
	}
	
	public java.lang.Integer getReceivablesId() {
		return getInt("receivables_id");
	}

	public void setNumber(java.lang.String number) {
		set("number", number);
	}
	
	public java.lang.String getNumber() {
		return getStr("number");
	}

	public void setPlanId(java.lang.Integer planId) {
		set("plan_id", planId);
	}
	
	public java.lang.Integer getPlanId() {
		return getInt("plan_id");
	}

	public void setCustomerId(java.lang.Integer customerId) {
		set("customer_id", customerId);
	}
	
	public java.lang.Integer getCustomerId() {
		return getInt("customer_id");
	}

	public void setContractId(java.lang.Integer contractId) {
		set("contract_id", contractId);
	}
	
	public java.lang.Integer getContractId() {
		return getInt("contract_id");
	}

	public void setCheckStatus(java.lang.Integer checkStatus) {
		set("check_status", checkStatus);
	}
	
	public java.lang.Integer getCheckStatus() {
		return getInt("check_status");
	}

	public void setExamineRecordId(java.lang.Integer examineRecordId) {
		set("examine_record_id", examineRecordId);
	}
	
	public java.lang.Integer getExamineRecordId() {
		return getInt("examine_record_id");
	}

	public void setReturnTime(java.util.Date returnTime) {
		set("return_time", returnTime);
	}
	
	public java.util.Date getReturnTime() {
		return get("return_time");
	}

	public void setReturnType(java.lang.String returnType) {
		set("return_type", returnType);
	}
	
	public java.lang.String getReturnType() {
		return getStr("return_type");
	}

	public void setMoney(java.math.BigDecimal money) {
		set("money", money);
	}
	
	public java.math.BigDecimal getMoney() {
		return get("money");
	}

	public void setRemark(java.lang.String remark) {
		set("remark", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("remark");
	}

	public void setCreateUserId(java.lang.Long createUserId) {
		set("create_user_id", createUserId);
	}
	
	public java.lang.Long getCreateUserId() {
		return getLong("create_user_id");
	}

	public void setOwnerUserId(java.lang.Long ownerUserId) {
		set("owner_user_id", ownerUserId);
	}
	
	public java.lang.Long getOwnerUserId() {
		return getLong("owner_user_id");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("update_time", updateTime);
	}
	
	public java.util.Date getUpdateTime() {
		return get("update_time");
	}

	public void setRemarks(java.lang.String remarks) {
		set("remarks", remarks);
	}
	
	public java.lang.String getRemarks() {
		return getStr("remarks");
	}

	public void setBatchId(java.lang.String batchId) {
		set("batch_id", batchId);
	}
	
	public java.lang.String getBatchId() {
		return getStr("batch_id");
	}

}
