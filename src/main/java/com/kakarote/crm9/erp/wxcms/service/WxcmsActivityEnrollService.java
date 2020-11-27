package com.kakarote.crm9.erp.wxcms.service;

import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityEnroll;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class WxcmsActivityEnrollService {
    public boolean add(WxcmsActivityEnroll wxcmsActivityEnroll) {
        wxcmsActivityEnroll.setCreateTime(new Date());
        return wxcmsActivityEnroll.save();
    }
}
