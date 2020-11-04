package com.kakarote.crm9.erp.yeyx.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanHistoryWsf<M extends BaseHrGongdanHistoryWsf<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}
	
	public Long getId() {
		return getLong("id");
	}

	public void setOrderNo(String orderNo) {
		set("orderNo", orderNo);
	}
	
	public String getOrderNo() {
		return getStr("orderNo");
	}

	public void setTotalAmount(String totalAmount) {
		set("totalAmount", totalAmount);
	}
	
	public String getTotalAmount() {
		return getStr("totalAmount");
	}

	public void setAddress(String address) {
		set("address", address);
	}
	
	public String getAddress() {
		return getStr("address");
	}

	public void setDistrict(String district) {
		set("district", district);
	}
	
	public String getDistrict() {
		return getStr("district");
	}

	public void setCity(String city) {
		set("city", city);
	}
	
	public String getCity() {
		return getStr("city");
	}

	public void setProvince(String province) {
		set("province", province);
	}
	
	public String getProvince() {
		return getStr("province");
	}

	public void setPrintDate(String printDate) {
		set("printDate", printDate);
	}
	
	public String getPrintDate() {
		return getStr("printDate");
	}

	public void setDeliveryStatus(String deliveryStatus) {
		set("deliveryStatus", deliveryStatus);
	}
	
	public String getDeliveryStatus() {
		return getStr("deliveryStatus");
	}

	public void setBuyerMessage(String buyerMessage) {
		set("buyerMessage", buyerMessage);
	}
	
	public String getBuyerMessage() {
		return getStr("buyerMessage");
	}

	public void setInternalNote(String internalNote) {
		set("internalNote", internalNote);
	}
	
	public String getInternalNote() {
		return getStr("internalNote");
	}

	public void setPrintNote(String printNote) {
		set("printNote", printNote);
	}
	
	public String getPrintNote() {
		return getStr("printNote");
	}

	public void setCustomerNote(String customerNote) {
		set("customerNote", customerNote);
	}
	
	public String getCustomerNote() {
		return getStr("customerNote");
	}

	public void setTrialDate(String trialDate) {
		set("trialDate", trialDate);
	}
	
	public String getTrialDate() {
		return getStr("trialDate");
	}

	public void setTrialPerson(String trialPerson) {
		set("trialPerson", trialPerson);
	}
	
	public String getTrialPerson() {
		return getStr("trialPerson");
	}

	public void setPlaceOrderPerson(String placeOrderPerson) {
		set("placeOrderPerson", placeOrderPerson);
	}
	
	public String getPlaceOrderPerson() {
		return getStr("placeOrderPerson");
	}

	public void setConsignee(String consignee) {
		set("consignee", consignee);
	}
	
	public String getConsignee() {
		return getStr("consignee");
	}

	public void setReceivePhone(String receivePhone) {
		set("receivePhone", receivePhone);
	}
	
	public String getReceivePhone() {
		return getStr("receivePhone");
	}

	public void setBuyerName(String buyerName) {
		set("buyerName", buyerName);
	}
	
	public String getBuyerName() {
		return getStr("buyerName");
	}

	public void setBuyerId(String buyerId) {
		set("buyerId", buyerId);
	}
	
	public String getBuyerId() {
		return getStr("buyerId");
	}

	public void setExternalNum(String externalNum) {
		set("externalNum", externalNum);
	}
	
	public String getExternalNum() {
		return getStr("externalNum");
	}

	public void setShopName(String shopName) {
		set("shopName", shopName);
	}
	
	public String getShopName() {
		return getStr("shopName");
	}

}