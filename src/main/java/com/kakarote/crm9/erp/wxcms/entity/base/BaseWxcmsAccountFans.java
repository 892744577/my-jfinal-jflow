package com.kakarote.crm9.erp.wxcms.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWxcmsAccountFans<M extends BaseWxcmsAccountFans<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setOpenId(String openId) {
		set("open_id", openId);
	}
	
	public String getOpenId() {
		return getStr("open_id");
	}

	public void setSubscribeStatus(Integer subscribeStatus) {
		set("subscribe_status", subscribeStatus);
	}
	
	public Integer getSubscribeStatus() {
		return getInt("subscribe_status");
	}

	public void setSubscribeTime(String subscribeTime) {
		set("subscribe_time", subscribeTime);
	}
	
	public String getSubscribeTime() {
		return getStr("subscribe_time");
	}

	public void setNickName(byte[] nickName) {
		set("nick_name", nickName);
	}
	
	public byte[] getNickName() {
		return get("nick_name");
	}

	public void setGender(Integer gender) {
		set("gender", gender);
	}
	
	public Integer getGender() {
		return getInt("gender");
	}

	public void setLanguage(String language) {
		set("language", language);
	}
	
	public String getLanguage() {
		return getStr("language");
	}

	public void setCountry(String country) {
		set("country", country);
	}
	
	public String getCountry() {
		return getStr("country");
	}

	public void setProvince(String province) {
		set("province", province);
	}
	
	public String getProvince() {
		return getStr("province");
	}

	public void setCity(String city) {
		set("city", city);
	}
	
	public String getCity() {
		return getStr("city");
	}

	public void setHeadImgUrl(String headImgUrl) {
		set("head_img_url", headImgUrl);
	}
	
	public String getHeadImgUrl() {
		return getStr("head_img_url");
	}

	public void setStatus(Integer status) {
		set("status", status);
	}
	
	public Integer getStatus() {
		return getInt("status");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}
	
	public String getRemark() {
		return getStr("remark");
	}

	public void setWxId(String wxId) {
		set("wx_id", wxId);
	}
	
	public String getWxId() {
		return getStr("wx_id");
	}

	public void setAccount(String account) {
		set("account", account);
	}
	
	public String getAccount() {
		return getStr("account");
	}

	public void setIsNewAttention(Integer isNewAttention) {
		set("isNewAttention", isNewAttention);
	}
	
	public Integer getIsNewAttention() {
		return getInt("isNewAttention");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

}
