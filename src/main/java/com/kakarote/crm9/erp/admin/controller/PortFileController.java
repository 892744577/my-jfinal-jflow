package com.kakarote.crm9.erp.admin.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;

public class PortFileController extends Controller {
    @Inject
    private AdminFileService adminFileService;

    public void index(){
        renderJson(R.ok());
    }

    /**
     * @author zhangzhiwei
     * 上传附件
     *
     */
    public void upload(){
        renderJson(FileUploadUtil.upload(getFile(), BaseConstant.UPLOAD_PATH_GDCX,"aptenon","",true));
    }
}
