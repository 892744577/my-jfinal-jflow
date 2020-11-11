package com.kakarote.crm9.erp.wx.service;

import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountQrcodeFans;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Slf4j
public class HandlerService {
    /*
     * @Description //保存到qrcode_fans表
     * @Author wangkaida
     * @Date 11:58 2020/11/9
     * @Param [fromUserName, eventKey, toUserName]
     * @return boolean
     **/
    public boolean saveToQrcodeFans(String fromUserName,String eventKey,String toUserName){
        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansDb = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByFromUserName")
                ,fromUserName);

        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFans = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByQrcodeParam")
                ,fromUserName,eventKey);

        if (wxcmsAccountQrcodeFansDb != null) {
            if (wxcmsAccountQrcodeFans != null) {
                log.debug("用户"+fromUserName+"已经关注过亚太天能公众号,参数:"+eventKey);
            }else {
                //更新到表qrcode_fans
                if (StringUtils.isNotBlank(eventKey)) {
                    WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansUpdate = new WxcmsAccountQrcodeFans();
                    wxcmsAccountQrcodeFansUpdate.setId(wxcmsAccountQrcodeFansDb.getId());
                    wxcmsAccountQrcodeFansUpdate.setEventKey(eventKey);
                    wxcmsAccountQrcodeFansUpdate.update();

                    //进行代理商的新增关注粉丝数量信息推送
//                                        String tmpResult = sendTemplateMessageToAgent(agentId);
//                                        logger.debug("进行代理商的新增关注粉丝数量信息推送结果:"+tmpResult);
                }
            }
        }else {
            //保存到表qrcode_fans
            if (StringUtils.isNotBlank(eventKey)) {
                WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansSave = new WxcmsAccountQrcodeFans();
                wxcmsAccountQrcodeFansSave.setFromuserName(fromUserName);
                wxcmsAccountQrcodeFansSave.setEventKey(eventKey);
                wxcmsAccountQrcodeFansSave.setTouserName(toUserName);
                wxcmsAccountQrcodeFansSave.setCreateTime(new Date());
                Boolean flag = wxcmsAccountQrcodeFansSave.save();

//              进行代理商的新增关注粉丝数量信息推送
                String tmpResult = sendTemplateMessageToAgent();
                log.debug("进行代理商的新增关注粉丝数量信息推送结果:"+tmpResult);
            }
        }
        return true;
    }

    public String sendTemplateMessageToAgent(){

        return null;
    }
}
