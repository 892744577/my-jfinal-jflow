package com.kakarote.crm9.erp.wxcms.entity.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseWxcmsCompetitiveNews<M extends BaseWxcmsCompetitiveNews<M>> extends Model<M> implements IBean {

	public void setId(Long id) {
		set("id", id);
	}
	
	public Long getId() {
		return getLong("id");
	}

	public void setOpinion1(String opinion1) {
		set("opinion1", opinion1);
	}
	
	public String getOpinion1() {
		return getStr("opinion1");
	}

	public void setOpinion2(String opinion2) {
		set("opinion2", opinion2);
	}
	
	public String getOpinion2() {
		return getStr("opinion2");
	}

	public void setOpinion3(String opinion3) {
		set("opinion3", opinion3);
	}
	
	public String getOpinion3() {
		return getStr("opinion3");
	}

	public void setOpinion4(String opinion4) {
		set("opinion4", opinion4);
	}
	
	public String getOpinion4() {
		return getStr("opinion4");
	}

}
