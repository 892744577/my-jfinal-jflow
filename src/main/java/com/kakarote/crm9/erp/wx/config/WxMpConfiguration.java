package com.kakarote.crm9.erp.wx.config;


import BP.Difference.SystemConfig;
import com.kakarote.crm9.erp.wx.mphandler.*;
import com.kakarote.crm9.erp.wx.util.MpUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.EventType;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxMpConfiguration {
    private  LogHandler logHandler;
    private  NullHandler nullHandler;
    private  KfSessionHandler kfSessionHandler;
    private  StoreCheckNotifyHandler storeCheckNotifyHandler;
    private  LocationHandler locationHandler;
    private  MenuHandler menuHandler;
    private  MsgHandler msgHandler;
    private  UnsubscribeHandler unsubscribeHandler;
    private  SubscribeHandler subscribeHandler;
    private  ScanHandler scanHandler;

    public WxMpService wxMpService() {

        WxMpProperties.MpConfig config1 = new WxMpProperties.MpConfig();
        config1.setAppId(SystemConfig.getCS_AppSettings().get("MP.APPID").toString());
        config1.setSecret(SystemConfig.getCS_AppSettings().get("MP.APPSECRET").toString());
        List<WxMpProperties.MpConfig> configs = new ArrayList<WxMpProperties.MpConfig>();
        configs.add(config1);

        if (configs == null) {
            throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
            .stream().map(a -> {
                WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
                configStorage.setAppId(a.getAppId());
                configStorage.setSecret(a.getSecret());
                configStorage.setToken(MpUtil.getAccessTokenByInterface().getAccessToken());
                configStorage.setAesKey(a.getAesKey());
                return configStorage;
            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
            .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
            .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
            .handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

}
