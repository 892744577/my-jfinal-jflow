package com.kakarote.crm9.erp.yzj.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.crm9.erp.yzj.dao.TokenDao;
import com.kakarote.crm9.erp.yzj.util.HttpHelper;
import com.kakarote.crm9.erp.yzj.vo.TokenBean;
import com.kakarote.crm9.erp.yzj.vo.UserContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用accessToken
 * @author
 */
@Slf4j
@Service
public class TokenService {

    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 3000;
    @Getter
    @Value("${APP.EID}")
    private String eid;
    @Getter
    @Value("${APP.APPID}")
    private String appid;
    @Getter
    @Value("${APP.SECRET}")
    private String appSecret;
    @Getter
    @Value("${APP.ERPSECRET}")
    private String erpSecret;
    @Getter
    @Value("${YUNZHIJIA.GATEWAY.HOST}")
    private String gatewayHost;
    @Getter
    @Value("${FRONT.HOST}")
    private String frontHost;
    @Getter
    @Value("${BACKEND.HOST}")
    private String backendHost;

    @Autowired
    private TokenDao tokenDao;


    /**
     * 获取accessToken
     * @author
     * @return
     */
    public String getAccessToken(String appId, String secret, String eid, String scope) {
        TokenBean tokenBean = tokenDao.getToken();
        // 判断当前token是否在有效期内
        if (tokenBean != null && tokenBean.getAccessToken() != null && scope.equals(tokenBean.getScope()) && StringUtils.isNotBlank(eid) && eid.equals(tokenBean.getEid())) {
            if((System.currentTimeMillis()-tokenBean.getUpdateTime().getTime())/1000 < (tokenBean.getExpireIn()-300)){
                log.debug("返回有效期内的access_token: {}", tokenBean.getAccessToken());
                return tokenBean.getAccessToken();
            }
        }
        // 如果没有token信息或者已经过期, 重新从api获取
        final String[] SCOPES = {"app", "team", "resGroupSecret"};
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map parm = new HashMap(5);
        parm.put("scope", scope); parm.put("timestamp", timestamp);
        if(scope.equals(SCOPES[0])) {
            parm.put("appId", appId);
        } else if(scope.equals(SCOPES[1])) {
            parm.put("eid", eid);
        }if(scope.equals(SCOPES[2])) {
            // 获取resGroupSecret秘钥
            parm.put("eid", eid);
            secret = erpSecret;
        }
        parm.put("secret", secret);
        String url = gatewayHost.concat("/oauth2/token/getAccessToken");
        JSONObject result = null;
        try {
            result = JSONObject.parseObject(gatewayRequestJson(url, JSONObject.toJSONString(parm))).getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取access_token信息失败!, 返回null");
        }

        log.debug("获取access_token返回数据: {}", result);
        tokenBean = JSON.toJavaObject(result, TokenBean.class);
        if(tokenBean!=null && tokenBean.getAccessToken()!=null) {
            tokenBean.setUpdateTime(new Date());
            tokenBean.setScope(scope);
            tokenBean.setEid(eid);
            tokenDao.setToken(tokenBean); // 缓存获取的token信息
            log.debug("返回新获取的access_token: {}", tokenBean.getAccessToken());
            return tokenBean.getAccessToken();
        }
        log.error("获取access_token信息失败!, 返回null");
        return null;
    }

    /*****************gateway默认常用url*************/
    /**
     * 校验用户上下文是否失效
     * @param userContext
     * @throws Exception
     */
    public void checkValid(UserContext userContext) throws Exception {
        if(com.alibaba.druid.util.StringUtils.isEmpty(userContext.getOpenid()) && com.alibaba.druid.util.StringUtils.isEmpty(userContext.getEid()))
            throw new RuntimeException("ticket已失效！");
    }
    /**
     * gateway发送json参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public static String gatewayRequestJson(String url, String parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_JSON);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }

    /**
     * gateway发送application/x-www-form-urlencoded参数POST请求
     * @param url
     * @param parm
     * @return
     * @throws Exception
     */
    public String gatewayRequest(String url, Map parm) throws Exception {
        Map headers = new HashMap(1); headers.put("Content-Type", APPLICATION_X_WWW_FORM_URLENCODED);
        return HttpHelper.post(headers, parm, url, timeoutMillis);
    }
}