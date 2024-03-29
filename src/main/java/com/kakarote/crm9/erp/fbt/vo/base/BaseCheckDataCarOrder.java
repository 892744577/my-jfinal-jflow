package com.kakarote.crm9.erp.fbt.vo.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCheckDataCarOrder<M extends BaseCheckDataCarOrder<M>> extends Model<M> implements IBean {

	public void setOrderId(String orderId) {
		set("order_id", orderId);
	}
	
	public String getOrderId() {
		return getStr("order_id");
	}

	public void setSupplierOrderId(String supplierOrderId) {
		set("supplier_order_id", supplierOrderId);
	}
	
	public String getSupplierOrderId() {
		return getStr("supplier_order_id");
	}

	public void setStatus(Integer status) {
		set("status", status);
	}
	
	public Integer getStatus() {
		return getInt("status");
	}

	public void setStatusName(String statusName) {
		set("status_name", statusName);
	}
	
	public String getStatusName() {
		return getStr("status_name");
	}

	public void setSupplierName(String supplierName) {
		set("supplier_name", supplierName);
	}
	
	public String getSupplierName() {
		return getStr("supplier_name");
	}

	public void setDepartureTime(String departureTime) {
		set("departure_time", departureTime);
	}
	
	public String getDepartureTime() {
		return getStr("departure_time");
	}

	public void setDepartureName(String departureName) {
		set("departure_name", departureName);
	}
	
	public String getDepartureName() {
		return getStr("departure_name");
	}

	public void setDepartureAddress(String departureAddress) {
		set("departure_address", departureAddress);
	}
	
	public String getDepartureAddress() {
		return getStr("departure_address");
	}

	public void setArrivalTime(String arrivalTime) {
		set("arrival_time", arrivalTime);
	}
	
	public String getArrivalTime() {
		return getStr("arrival_time");
	}

	public void setArrivalName(String arrivalName) {
		set("arrival_name", arrivalName);
	}
	
	public String getArrivalName() {
		return getStr("arrival_name");
	}

	public void setArrivalAddress(String arrivalAddress) {
		set("arrival_address", arrivalAddress);
	}
	
	public String getArrivalAddress() {
		return getStr("arrival_address");
	}

	public void setCreateTime(String createTime) {
		set("create_time", createTime);
	}
	
	public String getCreateTime() {
		return getStr("create_time");
	}

	public void setCustomerId(String customerId) {
		set("customer_id", customerId);
	}
	
	public String getCustomerId() {
		return getStr("customer_id");
	}

	public void setCustomerName(String customerName) {
		set("customer_name", customerName);
	}
	
	public String getCustomerName() {
		return getStr("customer_name");
	}

	public void setCustomerPhone(String customerPhone) {
		set("customer_phone", customerPhone);
	}
	
	public String getCustomerPhone() {
		return getStr("customer_phone");
	}

	public void setTotalPrice(java.math.BigDecimal totalPrice) {
		set("total_price", totalPrice);
	}
	
	public java.math.BigDecimal getTotalPrice() {
		return get("total_price");
	}

	public void setPersonalTotalPay(java.math.BigDecimal personalTotalPay) {
		set("personal_total_pay", personalTotalPay);
	}
	
	public java.math.BigDecimal getPersonalTotalPay() {
		return get("personal_total_pay");
	}

	public void setPassengerName(String passengerName) {
		set("passenger_name", passengerName);
	}
	
	public String getPassengerName() {
		return getStr("passenger_name");
	}

	public void setPassengerPhone(String passengerPhone) {
		set("passenger_phone", passengerPhone);
	}
	
	public String getPassengerPhone() {
		return getStr("passenger_phone");
	}

}
