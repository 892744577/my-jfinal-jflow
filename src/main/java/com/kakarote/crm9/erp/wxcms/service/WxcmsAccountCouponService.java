package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityCoupon;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;

import java.io.File;

public class WxcmsAccountCouponService {
    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;

    public WxMpMaterialFileBatchGetResult getMedia(int i, int j) {
        return mpService.getMedia(i,j);
    }

    public WxMpMaterialUploadResult uploadMedia(String name, File file) {
        WxMpMaterialUploadResult wxMpMaterialUploadResult = mpService.uploadMedia(name,file);
        if(wxMpMaterialUploadResult.getMediaId()!=null){
            //保存上传记录
            WxcmsActivityCoupon wxcmsActivityCoupon = new WxcmsActivityCoupon();
            wxcmsActivityCoupon.setMediaId(wxMpMaterialUploadResult.getMediaId());
            wxcmsActivityCoupon.setUrl(wxMpMaterialUploadResult.getUrl());
            wxcmsActivityCoupon.setName(name);
            wxcmsActivityCoupon.save();
        }
        return wxMpMaterialUploadResult;
    }
}
