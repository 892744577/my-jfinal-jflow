package com.kakarote.crm9.erp.yeyx.entity.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanFinanceFee<M extends BaseHrGongdanFinanceFee<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setServiceNo(String serviceNo) {
		set("serviceNo", serviceNo);
	}
	
	public String getServiceNo() {
		return getStr("serviceNo");
	}

	public void setPreServiceNo(String preServiceNo) {
		set("preServiceNo", preServiceNo);
	}
	
	public String getPreServiceNo() {
		return getStr("preServiceNo");
	}

	public void setProductCount(Integer productCount) {
		set("productCount", productCount);
	}
	
	public Integer getProductCount() {
		return getInt("productCount");
	}

	public void setServicePrice(java.math.BigDecimal servicePrice) {
		set("servicePrice", servicePrice);
	}
	
	public java.math.BigDecimal getServicePrice() {
		return get("servicePrice");
	}

	public void setServiceExtraCharge(java.math.BigDecimal serviceExtraCharge) {
		set("serviceExtraCharge", serviceExtraCharge);
	}
	
	public java.math.BigDecimal getServiceExtraCharge() {
		return get("serviceExtraCharge");
	}

	public void setChargeFee(java.math.BigDecimal chargeFee) {
		set("chargeFee", chargeFee);
	}
	
	public java.math.BigDecimal getChargeFee() {
		return get("chargeFee");
	}

	public void setCDT(String CDT) {
		set("CDT", CDT);
	}
	
	public String getCDT() {
		return getStr("CDT");
	}

}