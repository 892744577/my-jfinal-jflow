package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsCompetitive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxcmsCompetitiveService {

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
                Db.getSqlPara("admin.competitive.queryPageList",kv)
        );
    }

    public Object save(WxcmsCompetitive wxcmsCompetitive) {
        return wxcmsCompetitive.save();
    }
}
