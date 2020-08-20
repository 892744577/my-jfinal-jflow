package com.kakarote.crm9.erp.yzj.controller;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Tools.StringUtils;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.oa.entity.OaEvent;
import com.kakarote.crm9.erp.sms.util.StringUtil;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YzjController extends Controller {

    @Inject
    private TokenService tokenService;

    /**
     * 清除组织架构
     */
    public void deleteOldDept(){
        String sql = "delete from port_dept where LENGTH(No)=36";
        int num = DBAccess.RunSQL(sql);
    }

    /**
     * @author wyq
     * oa同步组织机构
     */
    public void queryAllDept() throws Exception {
        deleteOldDept();
        String deptInfoUrl = tokenService.getGatewayHost().concat("/openimport/open/dept/getall?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecret(), tokenService.getEid(), "resGroupSecret"));
        Map currentDeptInfoPrama = new HashMap();
        Map currentDeptInfoJson= new HashMap();
        currentDeptInfoJson.put("eid", tokenService.getEid());
        currentDeptInfoPrama.put("data",JSONObject.toJSONString(currentDeptInfoJson));
        currentDeptInfoPrama.put("eid", tokenService.getEid());
        currentDeptInfoPrama.put("nonce", UUID.randomUUID().toString().replace("-", "").substring(0, 15));
        String deptReturn = tokenService.gatewayRequest(deptInfoUrl, currentDeptInfoPrama);
        JSONObject deptListResult = JSONObject.parseObject(deptReturn);
        if("true".equals(deptListResult.get("success").toString())
                && deptListResult.getJSONArray("data") !=null
                && deptListResult.getJSONArray("data").size()>0){
            JSONArray deptList= deptListResult.getJSONArray("data");
            for(int i=0;deptList.size()>i;i++){
                JSONObject dept= deptList.getJSONObject(i);
                if("5af924c3e4b0738621a1cafb".equals(dept.getString("id"))){

                }else{
                    PortDept portDept = new PortDept();
                    portDept.setNo(dept.getString("id"));
                    portDept.setName(dept.getString("name"));
                    portDept.setNameOfPath(dept.getString("department"));
                    if("5af924c3e4b0738621a1cafb".equals(dept.getString("parentId"))){
                        portDept.setParentNo("100");
                    }else{
                        portDept.setParentNo(dept.getString("parentId"));
                    }
                    portDept.save();
                }

            }
        }
        renderJson(R.ok());
    }

    /**
     * @author wyq
     * oa同步人员
     */
    public void synchronizeAllEmp() throws Exception {
        queryAllDept();
        String userInfoUrl = tokenService.getGatewayHost().concat("/openimport/open/person/getall?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecret(), tokenService.getEid(), "resGroupSecret"));
        Map currentUserInfoPrama = new HashMap();
        Map currentUserInfoJson= new HashMap();
        currentUserInfoJson.put("eid", tokenService.getEid());
        currentUserInfoJson.put("begin", 0);
        currentUserInfoJson.put("count", 1000);
        currentUserInfoPrama.put("data",JSONObject.toJSONString(currentUserInfoJson));
        currentUserInfoPrama.put("eid", tokenService.getEid());
        currentUserInfoPrama.put("nonce", UUID.randomUUID().toString().replace("-", "").substring(0, 15));
        String deptReturn = tokenService.gatewayRequest(userInfoUrl, currentUserInfoPrama);
        JSONObject empListResult = JSONObject.parseObject(deptReturn);
        if("true".equals(empListResult.get("success").toString())
                && empListResult.getJSONArray("data") !=null
                && empListResult.getJSONArray("data").size()>0){
            JSONArray empList= empListResult.getJSONArray("data");
            for(int i=0;empList.size()>i;i++){
                JSONObject emp= empList.getJSONObject(i);

                //名字去空格
                String [] nameStr = emp.getString("name").split("-");
                String name = "";
                for (String nameTemp : nameStr) {
                    name += nameTemp.trim();
                }

                //名字拼音
                String pinyin1 = DataType.ParseStringToPinyin(name);
                String pinyin2 = DataType.ParseStringToPinyinJianXie(name);
                String pinyin = "," + pinyin1 + "," + pinyin2 + ",";

                //从云之家工号中分离出客户编号
                String [] yzjJobNos = emp.getString("jobNo").split("-");
                String khbh = "";
                for (String jobNo : yzjJobNos) {
                    if(StringUtils.isNotEmpty(jobNo) && jobNo.contains("ZN") ){
                        khbh = jobNo.trim().substring(jobNo.trim().indexOf("ZN"),jobNo.trim().indexOf("ZN")+6);
                    }
                }

                //计算部门id，若是客户编号的用户，则部门为客户编号
                String deptId = "";
                if(emp.getString("jobNo").contains("ZN")){
                    deptId = khbh;
                }else if("5af924c3e4b0738621a1cafb".equals(emp.getString("orgId"))){
                    deptId="100";
                }else{
                    deptId = emp.getString("orgId");
                }

                //对于不同名同手机、同名同手机，当作相同的人,更新
                PortEmp portEmp1 = new PortEmp();
                if(StringUtils.isNotEmpty(emp.getString("phone"))){ //手机号为空不做处理
                    portEmp1 = portEmp1.findFirst("select * from port_emp where tel=" + emp.getString("phone"));
                    if( portEmp1 != null ){
                        portEmp1.setFkDept(deptId);
                        portEmp1.setYzjJobNo(emp.getString("jobNo"));
                        portEmp1.setYzjOpenId(emp.getString("openId"));
                        portEmp1.setZt(emp.getString("status"));
                        portEmp1.setYzjJobTitle(emp.getString("jobTitle").trim());
                        portEmp1.update();
                    }
                }

                //对于同名不同手机号，当作不相同的人,暂不处理，因为同名的No不知道怎么设置
                PortEmp portEmp2 = new PortEmp();
                portEmp2 = portEmp2.findByIdLoadColumns(pinyin1,"No");
                if( portEmp2 != null ){
                    //数据不做处理
                }

                //对于即不同名，也不同手机号，当作不同的人，插入
                if(portEmp1 == null && portEmp2 == null){
                    PortEmp portEmp3 = new PortEmp();
                    portEmp3.setNo(pinyin1);
                    portEmp3.setPinYin(pinyin);
                    portEmp3.setName(name);
                    portEmp3.setTel(emp.getString("phone"));
                    portEmp3.setFkDept(deptId);
                    portEmp3.setYzjJobNo(emp.getString("jobNo"));
                    portEmp3.setYzjOpenId(emp.getString("openId"));
                    portEmp3.setZt(emp.getString("status"));
                    portEmp3.setYzjJobTitle(emp.getString("jobTitle").trim());
                    portEmp3.save();
                }
            }
        }
        renderJson(R.ok());
    }
}
