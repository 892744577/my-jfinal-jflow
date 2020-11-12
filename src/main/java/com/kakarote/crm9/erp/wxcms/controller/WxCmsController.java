package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShop;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShopQrcode;
import com.kakarote.crm9.erp.wxcms.service.WxcmsAccountAgentService;
import com.kakarote.crm9.erp.wxcms.service.WxcmsAccountFansService;
import com.kakarote.crm9.erp.wxcms.service.WxcmsAccountShopService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import java.util.List;

/**
 * 粉丝管理
 */
@Slf4j
public class WxCmsController extends Controller {
    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;
    @Inject
    private WxcmsAccountAgentService wxcmsAccountAgentService;
    @Inject
    private WxcmsAccountShopService wxcmsAccountShopService;
    @Inject
    private WxcmsAccountFansService wxcmsAccountFansService;
    /**
     * 批量生成店铺带参数二维码
     * @param scence
     */
    public void batchQrCodeCreateLastTicket(String scence) {
        //1、查询店铺表的店铺编号作为event_key
        List<WxcmsAccountShop> list = WxcmsAccountShop.dao.findAll();
        for(int i=0;i<list.size();i++){
            WxcmsAccountShop wxcmsAccountShop = list.get(i);
            String shopNo = wxcmsAccountShop.getShopNo();
            WxMpQrCodeTicket wxMpQrCodeTicket = mpService.qrCodeCreateLastTicket(shopNo);
            //2、批量生成参数二维码并插入表shop_qrcode
            WxcmsAccountShopQrcode wxcmsAccountShopQrcode = new WxcmsAccountShopQrcode();
            wxcmsAccountShopQrcode.setShopId(wxcmsAccountShop.getId());
            wxcmsAccountShopQrcode.setQrcodeParam(shopNo);
            wxcmsAccountShopQrcode.setQrcodeUrl(wxMpQrCodeTicket.getUrl());
            wxcmsAccountShopQrcode.save();
        }
        renderJson(R.ok());
    }

    /**
     * 查询代理商
     */
    public void getAgents(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",wxcmsAccountAgentService.queryPageList(basePageRequest)));
    }
    /**
     * 增删查改门店
     */
    public void getShops(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",wxcmsAccountShopService.queryPageList(basePageRequest)));
    }
    public void addShops() {
        log.info("=======保存门店记录");
        WxcmsAccountShop wxcmsAccountShop = getModel(WxcmsAccountShop.class,"",true);
        renderJson(R.ok().put("data",wxcmsAccountShopService.add(wxcmsAccountShop)));
    }
    public void updateShops() {
        log.info("=======修改门店记录");
        WxcmsAccountShop wxcmsAccountShop = getModel(WxcmsAccountShop.class,"",true);
        renderJson(R.ok().put("data",wxcmsAccountShopService.update(wxcmsAccountShop)));
    }
    /**
     * 查询粉丝
     */
    public void getFans(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",wxcmsAccountFansService.queryPageList(basePageRequest)));
    }
}
