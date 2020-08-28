package com.kakarote.crm9.erp.yeyx.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanRepair<M extends BaseHrGongdanRepair<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setContact(String contact) {
		set("contact", contact);
	}
	
	public String getContact() {
		return getStr("contact");
	}

	public void setPhone(String phone) {
		set("phone", phone);
	}
	
	public String getPhone() {
		return getStr("phone");
	}

	public void setBookDate(String bookDate) {
		set("bookDate", bookDate);
	}
	
	public String getBookDate() {
		return getStr("bookDate");
	}

	public void setAddress(String address) {
		set("address", address);
	}
	
	public String getAddress() {
		return getStr("address");
	}

	public void setBuyType(String buyType) {
		set("buyType", buyType);
	}
	
	public String getBuyType() {
		return getStr("buyType");
	}

	public void setEquipSNCode(String equipSNCode) {
		set("equipSNCode", equipSNCode);
	}
	
	public String getEquipSNCode() {
		return getStr("equipSNCode");
	}

	public void setPhoto(String photo) {
		set("photo", photo);
	}
	
	public String getPhoto() {
		return getStr("photo");
	}

	public void setRemark(String remark) {
		set("remark", remark);
	}
	
	public String getRemark() {
		return getStr("remark");
	}

	public void setOpenId(String openId) {
		set("open_id", openId);
	}
	
	public String getOpenId() {
		return getStr("open_id");
	}

	public void setOrderNumber(String orderNumber) {
		set("orderNumber", orderNumber);
	}
	
	public String getOrderNumber() {
		return getStr("orderNumber");
	}

	public void setIsGenerate(Integer isGenerate) {
		set("is_generate", isGenerate);
	}
	
	public Integer getIsGenerate() {
		return getInt("is_generate");
	}

	public void setCreator(String creator) {
		set("creator", creator);
	}
	
	public String getCreator() {
		return getStr("creator");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("create_time", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("create_time");
	}

	public void setStandardFee(java.math.BigDecimal standardFee) {
		set("standardFee", standardFee);
	}
	
	public java.math.BigDecimal getStandardFee() {
		return get("standardFee");
	}

	public void setAdjustmentFee(java.math.BigDecimal adjustmentFee) {
		set("adjustmentFee", adjustmentFee);
	}
	
	public java.math.BigDecimal getAdjustmentFee() {
		return get("adjustmentFee");
	}

	public void setPayType(Integer payType) {
		set("payType", payType);
	}
	
	public Integer getPayType() {
		return getInt("payType");
	}

	public void setSourceType(Integer sourceType) {
		set("sourceType", sourceType);
	}
	
	public Integer getSourceType() {
		return getInt("sourceType");
	}

	public void setDeal(String deal) {
		set("deal", deal);
	}
	
	public String getDeal() {
		return getStr("deal");
	}

	public void setProductType(String productType) {
		set("productType", productType);
	}
	
	public String getProductType() {
		return getStr("productType");
	}

	public void setProductColor(String productColor) {
		set("productColor", productColor);
	}
	
	public String getProductColor() {
		return getStr("productColor");
	}

	public void setProductActivationDate(java.util.Date productActivationDate) {
		set("productActivationDate", productActivationDate);
	}
	
	public java.util.Date getProductActivationDate() {
		return get("productActivationDate");
	}

}
