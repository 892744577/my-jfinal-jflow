package com.kakarote.crm9.erp.wx.mphandler;

import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.wx.service.HandlerService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class ScanHandler extends AbstractHandler {
    @Inject
    private HandlerService handlerService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        //组装卡券数据
        WxMpXmlOutMessage wxMpXmlOutMessage = handlerService.getWxMpXmlOutMessage(wxMessage,wxMpService);
        // 扫码事件处理
        return handlerService.outMessage(wxMpXmlOutMessage);
    }
}
