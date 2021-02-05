package com.kakarote.crm9.erp.jxc.service;

import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDelivery;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderDeliveryCode;

import java.util.List;

public class JxcService {
    @Inject
    private JxcCodeService jxcCodeService;
    /**
     * 获取单个出货单详情
     * @param entity
     * @return
     */
    public JxcOrderDelivery getByDocno(JxcOrderDelivery entity){
        return JxcOrderDelivery.dao.findFirst(Db.getSqlPara("admin.jxc.getByDocno",entity).getSql(),entity.getDocno());
    }
    /**
     * 分页查询出货单
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);
        Page<Record> jxcOrderDeliveryResult = Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.jxc.queryPageList",kv));
        List<Record> jxcOrderDeliveryList = jxcOrderDeliveryResult.getList();
        jxcOrderDeliveryList.stream().forEach(record -> {
            JxcOrderDeliveryCode jxcOrderDeliveryCode = new JxcOrderDeliveryCode();
            jxcOrderDeliveryCode.setDocno(record.getStr("docno"));
            List<JxcOrderDeliveryCode> jxcOrderDeliveryCodeList = jxcCodeService.getByDocno(jxcOrderDeliveryCode);
            record.set("detail",jxcOrderDeliveryCodeList);
        });
        jxcOrderDeliveryResult.setList(jxcOrderDeliveryList);
        return jxcOrderDeliveryResult;
    }
}
