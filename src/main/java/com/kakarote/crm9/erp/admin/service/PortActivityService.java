package com.kakarote.crm9.erp.admin.service;

import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.admin.entity.PortActivity;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.List;

public class PortActivityService {

    public R queryActivityList() {

        List<PortActivity> portActivityList = PortActivity.dao.find("select * from port_activity");
        return R.ok().put("data", portActivityList).put("code","000000");
    }
}
