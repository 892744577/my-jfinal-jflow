package com.kakarote.crm9.erp.wx.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信服务器配置路径参数接收类
 */
@Data
@Slf4j
public class ServerHandlerRequest {
    private String timestamp;
    private String nonce;
    private String signature;
    private String echostr;
    private String encrypt_type;
}
