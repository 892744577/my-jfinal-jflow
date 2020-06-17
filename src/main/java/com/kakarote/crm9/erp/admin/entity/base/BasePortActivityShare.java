package com.kakarote.crm9.erp.admin.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePortActivityShare<M extends BasePortActivityShare<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setSrPbId(java.lang.Integer srPbId) {
		set("sr_pb_id", srPbId);
	}

	public java.lang.Integer getSrPbId() {
		return getInt("sr_pb_id");
	}

	public void setSrAsId(java.lang.Integer srAsId) {
		set("sr_as_id", srAsId);
	}

	public java.lang.Integer getSrAsId() {
		return getInt("sr_as_id");
	}

	public void setSrShareOpenid(java.lang.String srShareOpenid) {
		set("sr_share_openid", srShareOpenid);
	}

	public java.lang.String getSrShareOpenid() {
		return getStr("sr_share_openid");
	}

	public void setSrShareInfo(java.lang.String srShareInfo) {
		set("sr_share_info", srShareInfo);
	}

	public java.lang.String getSrShareInfo() {
		return getStr("sr_share_info");
	}

	public void setSrToShareOpenid(java.lang.String srToShareOpenid) {
		set("sr_to_share_openid", srToShareOpenid);
	}

	public java.lang.String getSrToShareOpenid() {
		return getStr("sr_to_share_openid");
	}

	public void setSrToShareInfo(java.lang.String srToShareInfo) {
		set("sr_to_share_info", srToShareInfo);
	}

	public java.lang.String getSrToShareInfo() {
		return getStr("sr_to_share_info");
	}

	public void setValidFlag(java.lang.String validFlag) {
		set("valid_flag", validFlag);
	}

	public java.lang.String getValidFlag() {
		return getStr("valid_flag");
	}

}
