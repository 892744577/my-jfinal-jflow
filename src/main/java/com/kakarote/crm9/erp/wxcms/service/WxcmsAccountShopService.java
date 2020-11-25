package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShop;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsAccountShopQrcode;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

import java.text.DecimalFormat;
import java.util.Date;

@Slf4j
public class WxcmsAccountShopService {

    @Inject
    private MpService mMpService;

    /**
     * @author tmr
     * 分页工单查询记录
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        String id = basePageRequest.getJsonObject().getString("id");
        Kv kv = Kv.by("search",search);
        kv = kv.set("id",id);

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.wxcmsAccountShop.queryPageList",kv)
        );
    }

    /**
     * 新增门店
     * @param wxcmsAccountShop
     * @return
     */
    public boolean add(WxcmsAccountShop wxcmsAccountShop) {
        boolean flag = wxcmsAccountShop.save();
        wxcmsAccountShop.setShopNo("NO.T"+(new DecimalFormat("0000")).format(wxcmsAccountShop.getId()));
        wxcmsAccountShop.update();
        WxMpQrCodeTicket wxMpQrCodeTicket= mMpService.qrCodeCreateLastTicket(wxcmsAccountShop.getShopNo());
        if(flag == true && wxMpQrCodeTicket!=null ){
            WxcmsAccountShopQrcode wxcmsAccountShopQrcode = new WxcmsAccountShopQrcode();
            wxcmsAccountShopQrcode.setShopId(wxcmsAccountShop.getId());
            wxcmsAccountShopQrcode.setQrcodeParam(wxMpQrCodeTicket.getTicket());
            wxcmsAccountShopQrcode.setQrcodeUrl(wxMpQrCodeTicket.getUrl());
            wxcmsAccountShopQrcode.setCreateTime(new Date());
            wxcmsAccountShopQrcode.save();
        }
        return flag;
    }

    public boolean update(WxcmsAccountShop wxcmsAccountShop) {
        return wxcmsAccountShop.update();
    }
}
