package com.kakarote.crm9.erp.wx.controller;

import BP.Difference.SystemConfig;
import com.jfinal.aop.Inject;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.util.MpUtil;
import com.kakarote.crm9.utils.R;
import com.jfinal.core.Controller;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;

public class MpController extends Controller {

    @Inject
    private WxMpConfiguration wxMpConfiguration;

    private String appid = SystemConfig.getCS_AppSettings().get("MP.APPID").toString();

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
        wxMpService.switchover(appid);
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        WxMpUser wxMpUser = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("openId",wxMpUser.getOpenId()).put("code","000000"));
    }

}
