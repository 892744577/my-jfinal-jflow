package com.kakarote.crm9.common.handler;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Aop;
import com.jfinal.handler.Handler;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.api.WxMpService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ProxyHandler extends Handler {

    private String basePrefix = SystemConfig.getCS_AppSettings().get("proxy.basePrefix").toString();
    /**
     * 若是前端页面的路径，动态代理页面的内容回来
     *
     */
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {

        if( (basePrefix + "/dist").equals(request.getRequestURI())
                || (basePrefix + "/dist/index.html").equals(request.getRequestURI())){
            /**
             *  若是这些请求则不再走jfinal的默认接管（路径匹配模式），需要自己处理这些url，否则会报一个 IllegalStateException 异常
             */
            isHandled[0]=true;
            /**
             * to do 进行前端代理
             */

            /**
             * cookies添加参数
             */
            WxMpConfiguration wxMpConfiguration = Aop.get(WxMpConfiguration.class);
            WxMpService wxMpService = wxMpConfiguration.wxMpService();
            try {
                WxJsapiSignature wxJsapiSignature = null;
                if(request.getQueryString()!= null ){
                    wxJsapiSignature = wxMpService.createJsapiSignature("http://app.aptenon.com"+target+ "?"+ request.getQueryString());
                }else{
                    wxJsapiSignature = wxMpService.createJsapiSignature("http://app.aptenon.com"+target);
                }
                Cookie c = new Cookie("jsapiTicket", JSON.toJSONString(wxJsapiSignature));
                c.setMaxAge(24*60*60);
                c.setPath(basePrefix);//同一服务器内所有应用都可访问到该Cookie
                response.addCookie(c);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        next.handle(target, request, response, isHandled);
    }
}
