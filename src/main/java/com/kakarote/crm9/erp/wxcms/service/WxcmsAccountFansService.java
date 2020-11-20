package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WxcmsAccountFansService {
    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;

    /**
     * @author tmr
     * 分页工单查询记录
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.wxcmsAccountFans.queryPageList",kv)
        );
    }

    /**
     * 生成永久二维码
     * @param shopNo
     * @return
     */
    public WxMpQrCodeTicket createLastTicket(String shopNo){
        return mpService.qrCodeCreateLastTicket(shopNo);
    }
}
