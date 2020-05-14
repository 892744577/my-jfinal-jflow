package com.kakarote.crm9.erp.wx.util;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.crm9.erp.wx.vo.AccessToken;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MpUtil {

    /**
     *
     * MethodName: getAccessTokenByInterface
     * @author wangkaida
     * @date 2019年1月24日
     */
    public static AccessToken getAccessTokenByInterface() {
        log.info("开始获取公众号的accesstoken");
        AccessToken token = null;
        String tokenUrl = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/weixin/manage/access/token/get/now/W001";
        String result = HttpClientUtils.sendHttpGet(tokenUrl);
//    	log.info("获取到的AccessToken:"+result);
        log.debug("获取到的AccessToken:"+result);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (null != jsonObject && jsonObject.containsKey("accessToken")) {
            token = new AccessToken();
            token.setAccessToken(jsonObject.getString("accessToken"));
        } else if (null != jsonObject) {
            log.error("通过接口获取AccessToken失败!");
        }
        return token;
    }
}
