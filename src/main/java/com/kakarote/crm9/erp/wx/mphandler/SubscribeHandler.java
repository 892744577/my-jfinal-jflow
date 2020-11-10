package com.kakarote.crm9.erp.wx.mphandler;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.wx.mpbuilder.TextBuilder;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountFans;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountQrcodeFans;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShopQrcode;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class SubscribeHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        this.logger.info("新关注用户: " + JSON.toJSONString(wxMessage));

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                //add by wangkaida 用户关注时调用的方法
                String eventKey = wxMessage.getEventKey();
                String toUserName = wxMessage.getToUser();
                String fromUserName = wxMessage.getFromUser();

                //亚太天能公众号
                if ("gh_9594312e8ff1".equals(toUserName)) {
                    logger.debug(fromUserName+"关注亚太天能公众号");
                    eventKey = eventKey.substring(eventKey.lastIndexOf("_")+1);
                    if (StringUtils.isNotBlank(eventKey)){
                        //判断是新码还是旧码，新码直接保存到qrcode_fans表
                        WxcmsAccountShopQrcode wxcmsAccountShopQrcodeDb = WxcmsAccountShopQrcode.dao.findFirst(Db.getSql("admin.wxcmsAccountShopQrcode.getShopByQrcodeParam")
                                ,eventKey);
                        if (wxcmsAccountShopQrcodeDb == null) {
                            //说明新码表里面不存在，则是旧码，用旧码去换取新码保存
                            List<Record> recordList = Db.find(Db.getSqlPara("admin.wxcmsAccount.getNewQrcode", Kv.by("search",eventKey)));
                            if (recordList != null && recordList.get(0) != null ) {
                                saveToQrcodeFans(fromUserName,recordList.get(0).getStr("shop_qrcode_param"),toUserName);
                            }else {
                                logger.debug("代理商不存在,参数eventKey:"+eventKey);
                            }
                        }else {
                            //说明是新码，则直接保存到qrcode_fans表
                            saveToQrcodeFans(fromUserName,eventKey,toUserName);
                        }

                        //新关注用户保存到wxcms_account_fans表,先判断openId是否存在表中
                        WxcmsAccountFans fans = WxcmsAccountFans.dao.findFirst(Db.getSql("admin.wxcmsAccountFans.getAccountFansByOpenId")
                                ,fromUserName);
                        if (fans != null) {
                            logger.debug("用户"+fromUserName+"已经关注过亚太天能公众号");
                        }else {
                            //通过openId获取微信用户的信息
                            WxMpUser wxMpUser = Aop.get(WxMpService.class).getUserService().userInfo(fromUserName);
                            if (null != wxMpUser) {
                                logger.debug("获取用户信息接口返回结果：" + wxMpUser.toString());
                                fans = new WxcmsAccountFans();
                                fans.setOpenId(wxMpUser.getOpenId());// 用户的标识
                                fans.setSubscribeStatus(wxMpUser.getSubscribe()?1:0);// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                                fans.setSubscribeTime(DateUtil.timestampToDateStr(String.valueOf(wxMpUser.getSubscribeTime())));// 用户关注时间
                                try {
                                    String nickname = wxMpUser.getNickname();
                                    fans.setNickName(nickname.getBytes("UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                fans.setGender(wxMpUser.getSex()); // 用户的性别（1是男性，2是女性，0是未知）
                                fans.setLanguage(wxMpUser.getLanguage());// 用户的语言，简体中文为zh_CN
                                fans.setCountry(wxMpUser.getCountry());// 用户所在国家
                                fans.setProvince(wxMpUser.getProvince());// 用户所在省份
                                fans.setCity(wxMpUser.getCity());// 用户所在城市
                                fans.setHeadImgUrl(wxMpUser.getHeadImgUrl());// 用户头像
                                fans.setRemark(wxMpUser.getRemark());
                                fans.setStatus(1);
                                fans.setCreateTime(new Date());
                                // 设置公众号
                                fans.setAccount("gh_9594312e8ff1");
                                //保存数据到wxcms_account_fans表
                                fans.save();
                            }

                        }

                    }else{
                        //代理商店铺为空，则默认把粉丝保存到亚太天能店铺二维码
                        WxcmsAccountShopQrcode wxcmsAccountShopQrcode = WxcmsAccountShopQrcode.dao.findFirst(Db.getSql("admin.wxcmsAccountShopQrcode.getQrcodeParamByShopId")
                                ,1);
                        saveToQrcodeFans(fromUserName,wxcmsAccountShopQrcode.getQrcodeParam(),toUserName);
                    }

                }

            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
        throws Exception {
        //TODO
        return null;
    }

    /*
     * @Description //保存到qrcode_fans表
     * @Author wangkaida
     * @Date 11:58 2020/11/9
     * @Param [fromUserName, eventKey, toUserName]
     * @return boolean
     **/
    private boolean saveToQrcodeFans(String fromUserName,String eventKey,String toUserName){
        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFansDb = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByFromUserName")
                ,fromUserName);

        WxcmsAccountQrcodeFans wxcmsAccountQrcodeFans = WxcmsAccountQrcodeFans.dao.findFirst(Db.getSql("admin.wxcmsAccountQrcodeFans.getFansByQrcodeParam")
                ,fromUserName,eventKey);

        if (wxcmsAccountQrcodeFansDb != null) {
            if (wxcmsAccountQrcodeFans != null) {
                logger.debug("用户"+fromUserName+"已经关注过亚太天能公众号,参数:"+eventKey);
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

                //进行代理商的新增关注粉丝数量信息推送
//                                    String tmpResult = sendTemplateMessageToAgent(agentId);
//                                    logger.debug("进行代理商的新增关注粉丝数量信息推送结果:"+tmpResult);
            }
        }
        return true;
    }

}
