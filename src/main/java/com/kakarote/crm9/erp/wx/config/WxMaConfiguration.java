package com.kakarote.crm9.erp.wx.config;

import BP.Difference.SystemConfig;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class WxMaConfiguration {

    private static Map<String, WxMaMessageRouter> routers = Maps.newHashMap();
    private static Map<String, WxMaService> maServices = Maps.newHashMap();
    private static WxMaProperties properties=new WxMaProperties();

    public static WxMaService getMaService(String appid) {
        WxMaService wxService = maServices.get(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }

    public static WxMaMessageRouter getRouter(String appid) {
        return routers.get(appid);
    }

    public static WxMaProperties getProperties(){
        return properties;
    }

    /**
     * 初始化配置
     */
    public void initProperties(){
        WxMaProperties.Config config1 = new WxMaProperties.Config();
        config1.setAppid(SystemConfig.getCS_AppSettings().get("MA.APPID").toString());
        config1.setSecret(SystemConfig.getCS_AppSettings().get("MA.APPSECRET").toString());
        List<WxMaProperties.Config> configs = new ArrayList<WxMaProperties.Config>();
        configs.add(config1);
        this.properties.setConfigs(configs);

        if (configs == null) {
            throw new RuntimeException("大哥，拜托先看下项目首页的说明（readme文件），添加下相关配置，注意别配错了！");
        }
    }

    /**
     * 初始化小程序,只能调用一次
     */
    public void init() {
        this.initProperties();
        maServices = this.properties.getConfigs().stream()
            .map(a -> {
                WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                config.setAppid(a.getAppid());
                config.setSecret(a.getSecret());
                config.setToken(a.getToken());
                config.setAesKey(a.getAesKey());
                config.setMsgDataFormat(a.getMsgDataFormat());

                WxMaService service = new WxMaServiceImpl();
                service.setWxMaConfig(config);
                routers.put(a.getAppid(), this.newRouter(service));
                return service;
            }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a));
    }

    private WxMaMessageRouter newRouter(WxMaService service) {
        final WxMaMessageRouter router = new WxMaMessageRouter(service);
        router
            .rule().handler(logHandler).next()
            .rule().async(false).content("模板").handler(templateMsgHandler).end()
            .rule().async(false).content("文本").handler(textHandler).end()
            .rule().async(false).content("图片").handler(picHandler).end()
            .rule().async(false).content("二维码").handler(qrcodeHandler).end();
        return router;
    }

    private final WxMaMessageHandler templateMsgHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendTemplateMsg(WxMaTemplateMessage.builder()
            .templateId("此处更换为自己的模板id")
            .formId("自己替换可用的formid")
            .data(Lists.newArrayList(
                new WxMaTemplateData("keyword1", "339208499", "#173177")))
            .toUser(wxMessage.getFromUser())
            .build());
        return null;
    };

    private final WxMaMessageHandler logHandler = (wxMessage, context, service, sessionManager) -> {
        System.out.println("收到消息：" + wxMessage.toString());
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
            .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler textHandler = (wxMessage, context, service, sessionManager) -> {
        service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("回复文本消息")
            .toUser(wxMessage.getFromUser()).build());
        return null;
    };

    private final WxMaMessageHandler picHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            WxMediaUploadResult uploadResult = service.getMediaService()
                .uploadMedia("image", "png",
                    ClassLoader.getSystemResourceAsStream("tmp.png"));
            service.getMsgService().sendKefuMsg(
                WxMaKefuMessage
                    .newImageBuilder()
                    .mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser())
                    .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

    private final WxMaMessageHandler qrcodeHandler = (wxMessage, context, service, sessionManager) -> {
        try {
            final File file = service.getQrcodeService().createQrcode("123", 430);
            WxMediaUploadResult uploadResult = service.getMediaService().uploadMedia("image", file);
            service.getMsgService().sendKefuMsg(
                WxMaKefuMessage
                    .newImageBuilder()
                    .mediaId(uploadResult.getMediaId())
                    .toUser(wxMessage.getFromUser())
                    .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return null;
    };

}
