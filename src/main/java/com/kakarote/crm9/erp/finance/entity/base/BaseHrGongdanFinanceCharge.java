package com.kakarote.crm9.erp.finance.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseHrGongdanFinanceCharge<M extends BaseHrGongdanFinanceCharge<M>> extends Model<M> implements IBean {

	public void setNmbaucdocno(String nmbaucdocno) {
		set("nmbaucdocno", nmbaucdocno);
	}
	
	public String getNmbaucdocno() {
		return getStr("nmbaucdocno");
	}

	public void setNmbauc014(String nmbauc014) {
		set("nmbauc014", nmbauc014);
	}
	
	public String getNmbauc014() {
		return getStr("nmbauc014");
	}

	public void setNmbauc021(String nmbauc021) {
		set("nmbauc021", nmbauc021);
	}
	
	public String getNmbauc021() {
		return getStr("nmbauc021");
	}

	public void setMoney(java.math.BigDecimal money) {
		set("money", money);
	}
	
	public java.math.BigDecimal getMoney() {
		return get("money");
	}

}
