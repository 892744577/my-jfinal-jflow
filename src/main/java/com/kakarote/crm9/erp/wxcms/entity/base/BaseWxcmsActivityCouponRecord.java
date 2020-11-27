package com.kakarote.crm9.erp.wxcms.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWxcmsActivityCouponRecord<M extends BaseWxcmsActivityCouponRecord<M>> extends Model<M> implements IBean {

	public void setCouponId(String couponId) {
		set("coupon_id", couponId);
	}
	
	public String getCouponId() {
		return getStr("coupon_id");
	}

	public void setOpenId(String openId) {
		set("open_id", openId);
	}
	
	public String getOpenId() {
		return getStr("open_id");
	}

}