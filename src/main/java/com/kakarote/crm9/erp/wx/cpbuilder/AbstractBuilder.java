package com.kakarote.crm9.erp.wx.cpbuilder;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

/**
 *  @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
public abstract class AbstractBuilder {
  public abstract WxCpXmlOutMessage build(String content, WxCpXmlMessage wxMessage, WxCpService service);
}
