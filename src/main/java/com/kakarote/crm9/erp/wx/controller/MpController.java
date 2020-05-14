package com.kakarote.crm9.erp.wx.controller;

import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.util.MpUtil;
import com.kakarote.crm9.utils.R;
import com.jfinal.core.Controller;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;

public class MpController extends Controller {

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
    public void jscode2session(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }

        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);

        renderJson(R.ok().put("openId",wxMpUser.getOpenId()).put("code","000000"));


        
    }

}
