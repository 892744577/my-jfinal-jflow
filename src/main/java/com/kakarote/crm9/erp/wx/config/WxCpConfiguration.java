package com.kakarote.crm9.erp.wx.config;

import BP.Difference.SystemConfig;
import com.google.common.collect.Maps;
import com.kakarote.crm9.erp.wx.cphandler.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import me.chanjar.weixin.cp.message.WxCpMessageRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxCpConfiguration {
    private LogHandler logHandler;
    private NullHandler nullHandler;
    private LocationHandler locationHandler;
    private MenuHandler menuHandler;
    private MsgHandler msgHandler;
    private UnsubscribeHandler unsubscribeHandler;
    private SubscribeHandler subscribeHandler;

    private static WxCpProperties properties=new WxCpProperties();

    private static Map<Integer, WxCpMessageRouter> routers = Maps.newHashMap();
    private static Map<Integer, WxCpService> cpServices = Maps.newHashMap();

    public static Map<Integer, WxCpMessageRouter> getRouters() {
        return routers;
    }

    public static WxCpService getCpService(Integer agentId) {
        return cpServices.get(agentId);
    }
    public static WxCpProperties getProperties(){
        return properties;
    }

    public void initProperties(){
        this.properties.setCorpId(SystemConfig.getCS_AppSettings().get("CP.CORPID").toString());

        WxCpProperties.AppConfig config = new WxCpProperties.AppConfig();
        config.setAgentId(WxCpAgentIdEmun.agent0.getCode());
        config.setSecret(WxCpAgentIdEmun.agent0.getScret());
        config.setAesKey("");
        config.setToken("");

        WxCpProperties.AppConfig config1 = new WxCpProperties.AppConfig();
        config1.setAgentId(WxCpAgentIdEmun.agent1.getCode());
        config1.setSecret(WxCpAgentIdEmun.agent1.getScret());
        config1.setAesKey("");
        config1.setToken("");

        WxCpProperties.AppConfig config2 = new WxCpProperties.AppConfig();
        config2.setAgentId(WxCpAgentIdEmun.agent2.getCode());
        config2.setSecret(WxCpAgentIdEmun.agent2.getScret());
        config2.setAesKey("");
        config2.setToken("");

        List<WxCpProperties.AppConfig> configs = new ArrayList<WxCpProperties.AppConfig>();
        configs.add(config);
        configs.add(config1);

        this.properties.setAppConfigs(configs);
        if (configs == null) {
            throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }
    }

    public void init() {
        initProperties();
        cpServices = this.properties.getAppConfigs().stream().map(a -> {
            val configStorage = new WxCpDefaultConfigImpl();
            configStorage.setCorpId(this.properties.getCorpId());
            configStorage.setAgentId(a.getAgentId());
            configStorage.setCorpSecret(a.getSecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAesKey());
            val service = new WxCpServiceImpl();
            service.setWxCpConfigStorage(configStorage);
            routers.put(a.getAgentId(), this.newRouter(service));
            return service;
        }).collect(Collectors.toMap(service -> service.getWxCpConfigStorage().getAgentId(), a -> a));
    }

    private WxCpMessageRouter newRouter(WxCpService wxCpService) {
        final val newRouter = new WxCpMessageRouter(wxCpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        // 点击菜单链接事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
            .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.UNSUBSCRIBE)
            .handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.LOCATION).handler(this.locationHandler)
            .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
            .handler(this.locationHandler).end();

        // 扫码事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.SCAN).handler(this.nullHandler).end();

        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxCpConsts.EventType.CHANGE_CONTACT).handler(new ContactChangeHandler()).end();

        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxCpConsts.EventType.ENTER_AGENT).handler(new EnterAgentHandler()).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
}
