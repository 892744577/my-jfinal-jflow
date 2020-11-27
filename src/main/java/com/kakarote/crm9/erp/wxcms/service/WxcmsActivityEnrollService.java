package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityEnroll;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class WxcmsActivityEnrollService {
    public boolean add(WxcmsActivityEnroll wxcmsActivityEnroll) {
        wxcmsActivityEnroll.setCreateTime(new Date());
        return wxcmsActivityEnroll.save();
    }

    public List<WxcmsActivityEnroll> getEnroll(WxcmsActivityEnroll wxcmsActivityEnroll) {
        return WxcmsActivityEnroll.dao.find(
                Db.getSqlPara("admin.wxcmsActivityEnroll.getEnroll").getSql(),
                wxcmsActivityEnroll.getAcId(),
                wxcmsActivityEnroll.getOpenId()
        );
    }
}
