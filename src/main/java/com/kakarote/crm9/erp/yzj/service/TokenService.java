package com.kakarote.crm9.erp.yzj.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.common.util.HttpHelper;
import com.kakarote.crm9.erp.yzj.dao.TokenDao;
import com.kakarote.crm9.erp.yzj.vo.TokenBean;
import com.kakarote.crm9.erp.yzj.vo.UserContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用accessToken
 * @author
 */
@Slf4j
public class TokenService {

    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final int timeoutMillis = 300000;
    @Getter
    private String eid = SystemConfig.getCS_AppSettings().get("APP.EID").toString();
    @Getter
    private String appid = SystemConfig.getCS_AppSettings().get("APP.APPID").toString();
    @Getter
    private String appSecret = SystemConfig.getCS_AppSettings().get("APP.SECRET").toString();
    @Getter
    private String erpSecret = SystemConfig.getCS_AppSettings().get("APP.ERPSECRET").toString();
    @Getter
    private String erpSecretQd = SystemConfig.getCS_AppSettings().get("APP.ERPSECRET.QD").toString();
    @Getter
    private String gatewayHost = SystemConfig.getCS_AppSettings().get("YUNZHIJIA.GATEWAY.HOST").toString();


    @Getter
    private String fid = SystemConfig.getCS_AppSettings().get("FLOW.FID").toString();
    @Getter
    private String flowsecret = SystemConfig.getCS_AppSettings().get("FLOW.SECRET").toString();

    @Inject
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
        parm.put("scope", scope);
        parm.put("timestamp", timestamp);
        if(scope.equals(SCOPES[0])) {
            parm.put("appId", appId);
        } else if(scope.equals(SCOPES[1])) {
            parm.put("appId", appId);
            parm.put("eid", eid);
        }if(scope.equals(SCOPES[2])) {
            // 获取resGroupSecret秘钥
            parm.put("eid", eid);
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
    public String gatewayRequestJson(String url, String parm) throws Exception {
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