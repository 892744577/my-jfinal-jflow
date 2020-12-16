package com.kakarote.crm9.erp.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.config.WxMaAppIdEmun;
import com.kakarote.crm9.erp.wx.config.WxMaConfiguration;
import com.kakarote.crm9.erp.wx.util.MaUtil;
import com.kakarote.crm9.erp.wx.vo.MaReq;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;

public class MiniappController extends Controller {


    public void jscode2session(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(WxMaAppIdEmun.ma0.getCode());

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
        final WxMaService wxService = WxMaConfiguration.getMaService(WxMaAppIdEmun.ma0.getCode());

        try {
            String scene = "t=as&id="+wxAppOpenId;
            String page = "pages/login/reg";
            final byte[] wxCode = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, 280, true, null, false);
            renderJson(R.ok().put("wxCode",wxCode).put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    /*
     * @Description //推送微信小程序信息--订阅消息
     * @Author wangkaida
     * @Date 9:23 2020/8/11
     * @Param [toUserOpenId]
     * @return void
     **/
    public void sendMaMessage(@Para("") MaReq maReq){

        if (StringUtils.isBlank(maReq.getTouser())) {
            renderJson(R.error("接收者的openId不能为空!").put("code","000011"));
            return;
        }

        if (StringUtils.isBlank(maReq.getTemplate_id())) {
            renderJson(R.error("所需下发的订阅模板Id不能为空!").put("code","000012"));
            return;
        }

        if (StringUtils.isBlank(maReq.getData())) {
            renderJson(R.error("模板内容不能为空!").put("code","000013"));
            return;
        }

        String sb= MaUtil.ResponseMsg(maReq.getTouser(),maReq.getTemplate_id(),maReq.getPage(),maReq.getData(),maReq.getMiniprogram_state());

        try {
            boolean IsSend = MaUtil.PostMaMsg(sb);

            if (IsSend == true){
                renderJson(R.ok().put("data",null).put("code","000000"));
            }else {
                renderJson(R.error("小程序模板内容推送失败!").put("code","000015"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
