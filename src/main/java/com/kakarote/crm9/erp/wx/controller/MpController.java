package com.kakarote.crm9.erp.wx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wx.util.MpUtil;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import com.kakarote.crm9.erp.wx.vo.MpUserInfoReq;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;

public class MpController extends Controller {

    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;

    public void getAccessTokenByInterface(){
        renderJson(R.ok().put("data",MpUtil.getAccessTokenByInterface()));
    }

    /*
     * @Description //通过code获取微信公众号openId
     * @Author wangkaida
     * @Date 16:30 2020/5/14
     * @Param [code]
     * @return void
     **/
    public void oauth2getAccessToken(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }

        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        wxMpService.switchover(mpService.getAppid());
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        WxMpUser wxMpUser = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("data",wxMpUser).put("code","000000"));
    }


    /*
     * @Description //通过code获取微信公众号openId
     * @Author wangkaida
     * @Date 16:30 2020/5/14
     * @Param [code]
     * @return void
     **/
    public void getJsapiConfig(@Para("url") String url){
        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        try {
            renderJson(R.ok().put("data",
                    wxMpService.createJsapiSignature(url))
                    .put("jsapi_ticket",wxMpService.getJsapiTicket()).
                            put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    /**
     * 公众号发送模板消息
     */
    public void send(@Para("") MpMsgSendReq mpMsgSendReq){
        String result = mpService.send(mpMsgSendReq);
        if("ok".equals(result)){
            renderJson(R.ok().put("data",result));
        }else{
            renderJson(R.error().put("data",result));
        }
    }
    /**
     * 获取公众号用户基本信息---判断用户是否关注公众号
     */
    public void getUserData(@Para("") MpUserInfoReq mpUserInfoReq){
        renderJson(R.ok().put("subscribe",mpService.userInfo(mpUserInfoReq).getSubscribe()));
    }
}
