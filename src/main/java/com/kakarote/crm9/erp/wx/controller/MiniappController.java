package com.kakarote.crm9.erp.wx.controller;

import BP.Difference.SystemConfig;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.config.WxMaConfiguration;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;

public class MiniappController extends Controller {

    private String appid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();

    public void jscode2session(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(this.appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            renderJson(R.ok().put("openId",session.getOpenid()).put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    /*
     * @Description //根据代理商小程序openId生成小程序码接口
     * @Author wangkaida
     * @Date 15:24 2020/5/18
     * @Param [wxAppOpenId]
     * @return void
     **/
    public void createWxaCodeUnlimitBytes(@Para("wxAppOpenId") String wxAppOpenId){

        if (StringUtils.isBlank(wxAppOpenId)) {
            renderJson(R.error("小程序openId不能为空!").put("code","000010"));
            return;
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(this.appid);

        try {
            String scene = "t=as&id="+wxAppOpenId;
            String page = "pages/login/reg";
            final byte[] wxCode = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, 280, true, null, false);
            renderJson(R.ok().put("wxCode",wxCode).put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

}
