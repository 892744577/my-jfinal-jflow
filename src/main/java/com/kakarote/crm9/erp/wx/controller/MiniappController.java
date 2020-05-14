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

    public void jscode2session(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }

        String appid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            renderJson(R.ok().put("openId",session.getOpenid()).put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

}
