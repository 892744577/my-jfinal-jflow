package com.kakarote.crm9.erp.yeyx.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;

public class HrGongDanAreaRelationService {
    /**
     * @author tmr
     * 分页工单查询记录
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String province = basePageRequest.getJsonObject().getString("province");
        String city = basePageRequest.getJsonObject().getString("city");
        String district = basePageRequest.getJsonObject().getString("district");
        Kv kv = Kv.by("province",province).set("city",city).set("district",district);

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDanAreaRelation.queryPageList",kv)
        );
    }
}
