package com.kakarote.crm9.erp.jxc.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.util.DateUtil;

import java.util.Date;

public class JxcReportService {
    /**
     * 获取记录数
     * @return
     */
    public int getCountByOrderNum() {
        String today = DateUtil.changeDateTOStr2(new Date());
        Kv kv= Kv.by("today", today);
        return  Db.queryInt(Db.getSqlPara("admin.jxcReport.getReportInitNum",kv).getSql(),today);
    }
}
