package com.kakarote.crm9.erp.admin.service;

import BP.DA.DataType;
import BP.Sys.CCFormAPI;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

public class PortEmpService {

    @Inject
    private TokenService tokenService;

    public R queryAllUserList() {

        String accessToken = tokenService.getAccessToken(null,tokenService.getErpSecret(),tokenService.getEid(),"resGroupSecret");

        return R.ok().put("data", "");
    }
}
