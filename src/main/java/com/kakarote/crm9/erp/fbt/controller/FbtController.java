package com.kakarote.crm9.erp.fbt.controller;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FbtController extends Controller {

    @Inject
    private FbtService fbtService;

    /*
     * @Description //添加部门接口
     * @Author wangkaida
     * @Date 10:18 2020/11/27
     **/
    public void createDept(@Para("") DeptReq deptReq) throws Exception {

        if(StrUtil.isEmpty(deptReq.getEmployee_id())){
            renderJson(R.error("操作人id不能为空!").put("code","000001"));
            return;
        }

        if(StrUtil.isEmpty(deptReq.getEmployee_type())){
            renderJson(R.error("操作人类型不能为空!").put("code","000002"));
            return;
        }

        if(StrUtil.isEmpty(deptReq.getData())){
            renderJson(R.error("请求数据data不能为空!").put("code","000003"));
            return;
        }

        boolean result = fbtService.createDept(deptReq);

        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用添加部门接口出错!").put("code","000004"));
        }

    }

}
