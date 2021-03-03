package com.kakarote.crm9.erp.wx.util;

import BP.DA.DataType;
import BP.Tools.HttpClientUtil;
import BP.Tools.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.crm9.erp.wx.config.WxMaAppIdEmun;
import com.kakarote.crm9.erp.wx.vo.AccessToken;
import lombok.extern.slf4j.Slf4j;

/*
 * @Description //微信小程序工具类--已废弃
 * @Author wangkaida
 * @Date 17:19 2020/8/10
 **/
@Slf4j
@Deprecated
public class MaUtil {

    /*
     * @Description //获取微信小程序AccessToken
     * @Author wangkaida
     * @Date 17:24 2020/8/10
     * @Param []
     * @return com.kakarote.crm9.erp.wx.vo.AccessToken
     **/
    public static AccessToken getAccessTokenByInterface() {
        log.info("开始获取微信小程序的accesstoken");
        String appId = WxMaAppIdEmun.ma0.getCode();
        String appSecret = WxMaAppIdEmun.ma0.getScret();

        AccessToken token = null;
        String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
        String result = HttpClientUtils.sendHttpGet(tokenUrl);
//    	log.info("获取到的AccessToken:"+result);
        log.debug("获取到的AccessToken:"+result);
        JSONObject jsonObject = JSONObject.parseObject(result);

        if (null != jsonObject && jsonObject.containsKey("access_token")) {
            token = new AccessToken();
            token.setAccessToken(jsonObject.getString("access_token"));
        } else if (null != jsonObject) {
            log.error("通过接口获取AccessToken失败!");
        }
        return token;
    }

    /*
     * @Description //推送微信小程序信息
     * @Author wangkaida
     * @Date 9:41 2020/8/11
     * @Param [sb]
     * @return boolean
     **/
    public static boolean PostMaMsg(String sb) throws Exception
    {
        String wxStr = "";
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?";
        wxStr = PostForMa(sb, url,null);
        if(DataType.IsNullOrEmpty(wxStr)==false){
            net.sf.json.JSONObject jd = net.sf.json.JSONObject.fromObject(wxStr);
            if(jd.get("errcode").toString().equals("0"))
                return true;

        }
        return false ;
    }

    /*
     * @Description //POST方式请求 小程序返回信息
     * @Author wangkaida
     * @Date 9:48 2020/8/11
     * @Param [parameters, URL]
     * @return java.lang.String
     **/
    public static String PostForMa(String parameters, String URL,String access_token) throws Exception
    {
        if(StringUtils.isEmpty(access_token)){
            access_token = getAccessTokenByInterface().getAccessToken();
        }
        URL = URL + "access_token=" + access_token;
        String str = HttpClientUtil.doPostJson(URL, parameters);
        return str;
    }

    /*
     * @Description //推送参数json串
     * @Author wangkaida
     * @Date 10:12 2020/8/11
     * @Param [toUserOpenId, templateId, data]
     * @return java.lang.String
     **/
    public static String  ResponseMsg(String toUserOpenId, String templateId, String page, String data, String miniProgramState)
    {
        StringBuilder sbStr = new StringBuilder();
        sbStr.append("{");
        sbStr.append("\"touser\":\"" + toUserOpenId + "\",");
        sbStr.append("\"template_id\":\"" + templateId + "\",");
        sbStr.append("\"page\":\"" + page + "\",");
        sbStr.append("\"miniprogram_state\":\"" + miniProgramState + "\",");
        sbStr.append("\"lang\":\"zh_CN\",");
        sbStr.append("\"data\":" + data);
        sbStr.append("}");
        return sbStr.toString();
    }

}
