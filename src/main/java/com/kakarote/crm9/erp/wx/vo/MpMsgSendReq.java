package com.kakarote.crm9.erp.wx.vo;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class MpMsgSendReq {

	private String touser; //公众号openid
	private String maAppId; //小程序appid
	private String template_id; //所需下发的订阅模板id
	private String page; //点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
	private String data; //模板内容，格式形如 [{"name":"first","value":"222"},{"name":"keyword1","value":"222"},{"name":"keyword2","value":"222"},{"name":"remark","value":"222"}]

}
