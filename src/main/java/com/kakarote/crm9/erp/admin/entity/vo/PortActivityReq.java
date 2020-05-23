package com.kakarote.crm9.erp.admin.entity.vo;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PortActivityReq {

	private String qrcode; //海报二维码
	private String sourceOpenId; //海报发起人小程序openid
	private String leadingOpenId; //负责人小程序openid
	private Integer acId; //活动id
	private Integer pbId; //海报id
	private Integer shareId; //分享id
	private String shareOpenId; //分享人小程序openid
	private String toShareOpenId; //被分享人小程序openid
	private String helperAppOpenId; //助力人小程序openid，针对本次活动，只能助力一次
	private String helperOpenId; //助力人公众号openid，针对本次活动，只能助力一次

}
