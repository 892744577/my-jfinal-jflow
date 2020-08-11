package com.kakarote.crm9.erp.wx.vo;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class MaReq {

	private String touser; //接收者（用户）的 openid
	private String template_id; //所需下发的订阅模板id
	private String page; //点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
	private String data; //模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
	private String miniprogram_state; //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版

}
