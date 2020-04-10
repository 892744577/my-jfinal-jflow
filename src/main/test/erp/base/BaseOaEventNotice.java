package com.erpsnow.erp;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOaEventNotice<M extends BaseOaEventNotice<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public void setEventId(java.lang.Integer eventId) {
		set("event_id", eventId);
	}
	
	public java.lang.Integer getEventId() {
		return getInt("event_id");
	}

	public void setNoticetype(java.lang.String noticetype) {
		set("noticetype", noticetype);
	}
	
	public java.lang.String getNoticetype() {
		return getStr("noticetype");
	}

	public void setRepeat(java.lang.String repeat) {
		set("repeat", repeat);
	}
	
	public java.lang.String getRepeat() {
		return getStr("repeat");
	}

	public void setStartTime(java.lang.Integer startTime) {
		set("start_time", startTime);
	}
	
	public java.lang.Integer getStartTime() {
		return getInt("start_time");
	}

	public void setStopTime(java.lang.Integer stopTime) {
		set("stop_time", stopTime);
	}
	
	public java.lang.Integer getStopTime() {
		return getInt("stop_time");
	}

}
