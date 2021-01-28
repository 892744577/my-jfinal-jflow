package com.kakarote.crm9.erp.fbt.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import com.kakarote.crm9.erp.fbt.vo.DeptReq;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FbtController extends Controller {

    @Inject
    private FbtService fbtService;

    public boolean check(DeptReq deptReq){
        if(StrUtil.isEmpty(deptReq.getEmployee_id())){
            renderJson(R.error("操作人id不能为空!").put("code","000001"));
            return true;
        }

        if(StrUtil.isEmpty(deptReq.getEmployee_type())){
            renderJson(R.error("操作人类型不能为空!").put("code","000002"));
            return true;
        }

        if(StrUtil.isEmpty(deptReq.getData())){
            renderJson(R.error("请求数据data不能为空!").put("code","000003"));
            return true;
        }

        if(StrUtil.isEmpty(deptReq.getAccessToken())){
            renderJson(R.error("AccessToken不能为空!").put("code","000006"));
            return true;
        }
        return false;
    }

    /*
     * @Description //添加部门接口
     * @Author wangkaida
     * @Date 10:18 2020/11/27
     **/
    public void createDept(@Para("") DeptReq deptReq) throws Exception {
        if(check(deptReq)) {
            return;
        }
        boolean result = fbtService.createDeptOrEmp(deptReq,
                fbtService.getPath()+"/openapi/func/department/create");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用添加部门接口出错!").put("code","000004"));
        }
    }

    /*
     * @Description //更新部门接口
     * @Author wangkaida
     * @Date 9:45 2021/1/28
     * @Param [deptReq]
     * @return void
     **/
    public void updateDept(@Para("") DeptReq deptReq) throws Exception {
        if(check(deptReq)) {
            return;
        }
        boolean result = fbtService.createDeptOrEmp(deptReq,
                fbtService.getPath()+"/openapi/func/department/update");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用更新部门接口出错!").put("code","000004"));
        }
    }

    /*
     * @Description //添加员工接口
     * @Author wangkaida
     * @Date 16:19 2020/11/30
     * @Param [deptReq]
     * @return void
     **/
    public void createEmployee(@Para("") DeptReq deptReq) throws Exception {
        if(check(deptReq)) {
            return;
        }
        boolean result = fbtService.createDeptOrEmp(deptReq,
                fbtService.getPath()+"/openapi/func/employee/create");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用添加员工接口出错!").put("code","000004"));
        }
    }

    /*
     * @Description //更新员工接口
     * @Author wangkaida
     * @Date 9:53 2020/12/2
     * @Param [deptReq]
     * @return void
     **/
    public void updateEmployee(@Para("") DeptReq deptReq) throws Exception {
        if(check(deptReq)) {
            return;
        }
        boolean result = fbtService.createDeptOrEmp(deptReq,
                fbtService.getPath()+"/openapi/func/employee/update");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用更新员工接口出错!").put("code","000004"));
        }
    }

    /**
     * 创建行程单
     */
    public void createTravelOrder(@Para("") DeptReq deptReq) throws Exception {
        log.info("===:"+ JSON.toJSONString(deptReq));
        boolean result = fbtService.travelOrder(deptReq,
                fbtService.getPath()+"/openapi/func/apply/trip/create");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用创建行程单出错").put("code","000004"));
        }
    }

    /**
     * 改变行程单
     */
    public void changeTravelOrder(@Para("") DeptReq deptReq) throws Exception {
        boolean result = fbtService.travelOrder(deptReq,
                fbtService.getPath()+"/openapi/func/apply/trip/change");
        if (result) {
            renderJson(R.ok().put("code","000000"));
        }else {
            renderJson(R.error("调用更新员工接口出错!").put("code","000004"));
        }
    }

    /*
     * @Description //获取AccessToken接口
     * @Author wangkaida
     * @Date 15:45 2020/11/30
     **/
    public void getAccessToken() throws Exception {
        String result = fbtService.getAccessToken();
        if (StringUtils.isNotBlank(result)) {
            renderJson(R.ok().put("data",result).put("code","000000"));
        }else {
            renderJson(R.error("调用获取AccessToken接口出错!").put("code","000005"));
        }
    }


}
