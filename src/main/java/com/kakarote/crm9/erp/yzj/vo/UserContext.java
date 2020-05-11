package com.kakarote.crm9.erp.yzj.vo;


import lombok.Data;

/**
 *         * 类描述：用户上下文信息缓存类
 *         */
@Data
public class UserContext {

    /**
     * 应用id
     */
    private String appid;
    /**
     * 企业id
     */
    private String eid;
    /**
     * 当前用户openid
     */
    private String openid;
    /**
     * 当前用户姓名
     */
    private String username;
    /**
     * 当前用户uid
     */
    private String uid;
    /**
     * 当前用户tid
     */
    private String tid;
    /**
     * 当前用户userid
     */
    private String userid;
}
