package com.kakarote.crm9.erp.yeyx.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.vo.FactoryRemarkRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;

import java.util.Date;

public class HrGongDanService {
    /**
     * @author tmr
     * 分页工单查询数据
     */
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer statistics = jsonObject.getInteger("statistics");
        Integer status = jsonObject.getInteger("status");
        basePageRequest.setJsonObject(jsonObject);
        if (StrUtil.isEmpty(String.valueOf(statistics))){
            return new Page<>();
        }

        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);

        //时间
        if(statistics==1){ //本日
            String today = DateUtil.changeDateTOStr3(new Date());
            kv.set("today",today);
        }else if(statistics==2){ //本周
            String weekend = DateUtil.changeDateTOStr3(new Date());
            kv.set("weekend",weekend);
        }else if(statistics==3) { //本月
            String yearmonth = DateUtil.changeDateTOStr6(new Date());
            kv.set("yearmonth", yearmonth);
        }

        //状态
        if(status==1){
            kv.set("confirm", "902");
        }else if(status==2) {
            String overtime = DateUtil.changeDateTOStr3(new Date());
            kv.set("overtime", overtime);
        }else if(status==3) {
            kv.set("toBeCompleted", "906");
        }

        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.hrGongDan.getHrGongDanPageList",kv)
        );
    }

    /**
     * 根据id更新审批状态
     * @param hrGongdanRequest
     * @return
     */
    public boolean update(HrGongdanRequest hrGongdanRequest) {
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(hrGongdanRequest.getOid());
        hrGongdan.setServiceSp(hrGongdanRequest.getServiceSp());
        return hrGongdan.update();
    }

    /**
     * 根据orderId获取记录
     * @param factoryRemarkRequest
     * @return
     */
    public HrGongdan getHrGongdanByOrderId(FactoryRemarkRequest factoryRemarkRequest) {
        return HrGongdan.dao.findFirst(Db.getSqlPara("admin.hrGongDan.getHrGongDanPageList",Kv.by("orderId",factoryRemarkRequest.getOrderId())));
    }
}

