package com.kakarote.crm9.erp.admin.entity.vo;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PortEmpReq {

	private String tel; //手机号码
	private String parentTel; //上级手机号
	private String wxAppOpenId; //微信小程序openid
	private String wxOpenId; //微信公众号openid
	private String name; //名称
	private String no; //员工账号
	private String parentNo; //父级账号
	private String agentId; //代理商微信openId
	private Integer pageSize; //页面大小
	private Integer pageIndex; //页码
	private String valiCode; //手机验证码
	private String fkFlow;
	private Long workID;

}
