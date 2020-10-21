package com.kakarote.crm9.erp.yeyx.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.util.DateUtil;

import java.util.Date;

public class HrGongdanFjfService {
    /**
     * 获取记录条数
     * @return
     */
    public int getFjfByOrderNum() {
        String today = DateUtil.changeDateTOStr2(new Date());
        Kv kv= Kv.by("today", today);
        return  Db.queryInt(Db.getSqlPara("admin.hrGongDanFjf.getHrGongDanFjfInitNum",kv).getSql(),today);
    }
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
                Db.getSqlPara("admin.hrGongDanFjf.queryPageList",kv)
        );
    }
}

