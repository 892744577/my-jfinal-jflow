package com.kakarote.crm9.erp.yeyx.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

public class HrGongdanZmnLogService {

    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDanZmnLog.queryPageList",kv)
        );
    }
}
