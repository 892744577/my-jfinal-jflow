package com.kakarote.crm9.erp.yeyx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.utils.R;

import java.util.ArrayList;
import java.util.List;

public class HrGongDanController extends Controller {

    @Inject
    private HrGongDanService hrGongDanService;

    /**
     * @author tmr
     * 分页工单查询数据
     */
    public void queryPageList(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongDanService.queryPageList(basePageRequest)));
    }

    /**
     * 校验数据
     * @param hrGongdanRequest
     */
    public void validate(@Para("") HrGongdanRequest hrGongdanRequest){

        renderJson(R.ok().put("data",hrGongDanService.update(hrGongdanRequest)));
    }

    /**
     * 批量校验数据
     */
    public void validateMore(){
        JSONArray array = JSON.parseArray(this.getRawData());
        List<HrGongdanRequest> list= array.toJavaList(HrGongdanRequest.class);
        for(int i=0;i<list.size();i++){
            hrGongDanService.update(list.get(i));
        }
        renderJson(R.ok());
    }
}
