package com.kakarote.crm9.erp.wx.vo;

import lombok.Data;

@Data
public class WxCpMessageReq {
    //应用id
    private Integer agentId; //必填

    //向谁发送
    private String user; //"非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送"
    private String party; //"非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数"
    private String tag; //"非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数"

    //text消息
    private String content; //必填

}
