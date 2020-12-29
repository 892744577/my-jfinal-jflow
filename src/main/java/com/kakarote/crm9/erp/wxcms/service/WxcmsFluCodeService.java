package com.kakarote.crm9.erp.wxcms.service;

import com.kakarote.crm9.erp.wxcms.entity.WxcmsFluCode;

import java.util.List;

public class WxcmsFluCodeService {
    /**
     * @author tmr
     * 分页工单查询记录
     */
    public List<WxcmsFluCode> findAll() {
        return WxcmsFluCode.dao.findAll();
    }
}
