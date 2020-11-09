package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShop;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShopQrcode;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import java.util.List;

public class WxCmsController extends Controller {
    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;
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
            wxcmsAccountShopQrcode.setShopid(wxcmsAccountShop.getId());
            wxcmsAccountShopQrcode.setQrcodeParam(shopNo);
            wxcmsAccountShopQrcode.setQrcodeUrl(wxMpQrCodeTicket.getUrl());
            wxcmsAccountShopQrcode.save();
        }
        renderJson(R.ok());
    }
}
