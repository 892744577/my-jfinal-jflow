package com.kakarote.crm9.erp.wx.service;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.mpbuilder.ImageBuilder;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountQrcodeFans;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityCoupon;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityCouponRecord;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

@Slf4j
public class HandlerService {

    //region 业务1：划分粉丝到店二维码
    /*
     * @Description //保存到qrcode_fans表
     * @Author wangkaida
     * @Date 11:58 2020/11/9
     * @Param [fromUserName, eventKey, toUserName]
     * @return boolean
     **/
    public boolean saveToQrcodeFans(String fromUserName,String eventKey,String toUserName){
        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFans1 = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByFromUserName")
                ,fromUserName);

        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFans2 = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByQrcodeParam")
                ,fromUserName,eventKey);

        if (wxcmsAccountQrcodeFans1 != null) {
            if (wxcmsAccountQrcodeFans2 != null) {
                log.debug("用户"+fromUserName+"已经关注过亚太天能公众号,参数:"+eventKey);
            }else {
                //更新到表qrcode_fans
                if (StringUtils.isNotBlank(eventKey)) {
                    WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansUpdate = new WxcmsAccountQrcodeFans();
                    wxcmsAccountQrcodeFansUpdate.setId(wxcmsAccountQrcodeFans1.getId());
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
    //endregion

    //*****************************************************************************

    //#region 业务2：卡券信息推送

    /**
     *
     * @param wxMessage
     * @param wxMpService
     * @return
     */
    public WxMpXmlOutMessage getWxMpXmlOutMessage(WxMpXmlMessage wxMessage, WxMpService wxMpService){
        //1、组装卡券信息
        List<WxcmsActivityCoupon> list = WxcmsActivityCoupon.dao.find(Db.getSql("admin.wxcmsActivityCoupon.getActivityCouponFirst"));
        WxMpXmlOutMessage wxMpXmlOutMessage = null;
        WxcmsActivityCoupon wxcmsActivityCoupon = null;
        if(list!=null && list.size()>0){
            wxcmsActivityCoupon = list.get(0);
            wxMpXmlOutMessage = new ImageBuilder().build(wxcmsActivityCoupon.getMediaId(),wxMessage,wxMpService);
            log.info("组装卡券数据："+ JSON.toJSONString(wxMpXmlOutMessage));
        }
        return wxMpXmlOutMessage;
    }

    /**
     * 判断返回什么
     * @return
     */
    public WxMpXmlOutMessage outMessage(WxMpXmlOutMessage wxMpXmlOutMessage) {
        if(wxMpXmlOutMessage!=null){
            WxMpXmlOutImageMessage wxMpXmlOutImageMessage = (WxMpXmlOutImageMessage)wxMpXmlOutMessage;
            int count = Db.queryInt(Db.getSql("admin.wxcmsActivityCouponRecord.getActivityCouponSendRecord"), wxMpXmlOutImageMessage.getMediaId(),wxMpXmlOutMessage.getToUserName());
            log.info("返回优惠券发送记录："+count);
            if(count>0){
                return null;
            }else{
                WxcmsActivityCouponRecord wxcmsActivityCouponRecord = new WxcmsActivityCouponRecord();
                wxcmsActivityCouponRecord.setCouponId(wxMpXmlOutImageMessage.getMediaId());
                wxcmsActivityCouponRecord.setOpenId(wxMpXmlOutMessage.getToUserName());
                wxcmsActivityCouponRecord.save();
                log.info("保存发送记录："+JSON.toJSONString(wxcmsActivityCouponRecord));
                return wxMpXmlOutMessage;
            }
        }else{
            return null;
        }
    }
}
