package com.kakarote.crm9.erp.yzj.controller;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.Tools.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanYzjOvertime;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanYzjOvertimeDetail;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.erp.yzj.vo.ClockInRequest;
import com.kakarote.crm9.utils.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
                    if(deptId.contains("ZN")){
                        portEmp3.setTeamNo(deptId);
                    }
                    portEmp3.save();
                }
            }
        }
        renderJson(R.ok());
    }

    /**
     * 签到
     */
    public void clockIn(@Para("") ClockInRequest clockInRequest) throws Exception {
        String listUrl = tokenService.getGatewayHost().concat("/attendance-data/v1/clockIn/clockintime/list?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecretQd(), tokenService.getEid(), "resGroupSecret"));
        Map listPrama = new HashMap();
        listPrama.put("workDateFrom","1596243458000");
        listPrama.put("workDateTo","1598317058000");
        listPrama.put("openIds",clockInRequest.getOpenId());
        String listReturn = tokenService.gatewayRequest(listUrl, listPrama);

        String qdUrl = tokenService.getGatewayHost().concat("/attendance-data/v1/attSet/getUserPositionList?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecretQd(), tokenService.getEid(), "resGroupSecret"));
        Map currentQdPrama = new HashMap();
        currentQdPrama.put("openId",clockInRequest.getOpenId());
        String deptReturn = tokenService.gatewayRequestJson(qdUrl, JSON.toJSONString(currentQdPrama));

        String singleUploadUrl = tokenService.getGatewayHost().concat("/attendance-data/v1/clockIn/singleUpload?accessToken=").concat(tokenService.getAccessToken(null, tokenService.getErpSecretQd(), tokenService.getEid(), "resGroupSecret"));
        Map singleUploadPrama = new HashMap();
        singleUploadPrama.put("openId",clockInRequest.getOpenId());
        singleUploadPrama.put("clockType",1);
        singleUploadPrama.put("clockInTime",clockInRequest.getClockInTime()*1000);
        singleUploadPrama.put("positionId",clockInRequest.getPositionId());

        String singleUploadReturn = tokenService.gatewayRequestJson(singleUploadUrl, JSON.toJSONString(singleUploadPrama));
        renderJson(R.ok().put("listReturn",JSONObject.parseObject(listReturn))
                .put("deptReturn",JSONObject.parseObject(deptReturn))
                .put("singleUploadReturn",JSONObject.parseObject(singleUploadReturn)));

    }

    /**
     * 加班流程保存数据库
     */
    public void doOvertimeList() throws Exception {
        JSONObject jsonObject1 = JSONObject.parseObject(this.getOvertime(1));
        JSONObject data1 = jsonObject1.getJSONObject("data");
        int pages = data1.getIntValue("pages");
        JSONArray list = data1.getJSONArray("list");

        //从第一页开始循环拉取，并保存到数据库
        for (int i = 1; i <= pages; i++) {
            if (i == 1) {

            } else if (i > 1) {
                JSONObject jsonObject2 = JSONObject.parseObject(getOvertime(i));
                JSONObject data2 = jsonObject2.getJSONObject("data");
                list = data2.getJSONArray("list");
            }
            if(list!=null){
                for (int j = 0; j < list.size(); j++) {
                    JSONObject Overtime =  list.getJSONObject(j);
                    HrGongdanYzjOvertime hrGongdanYzjOvertimeDb = HrGongdanYzjOvertime.dao.findFirst(Db.getSql("admin.hrGongdanYzjOvertime.getHrGongdanYzjOvertime"),Overtime.getString("serialNo"));
                    HrGongdanYzjOvertime hrGongdanYzjOvertime = new HrGongdanYzjOvertime();
                    hrGongdanYzjOvertime.setDeptName(Overtime.getString("deptName"));
                    hrGongdanYzjOvertime.setCreator(Overtime.getString("creator"));
                    hrGongdanYzjOvertime.setCreateName(Overtime.getString("createName"));
                    hrGongdanYzjOvertime.setSerialNo(Overtime.getString("serialNo"));
                    hrGongdanYzjOvertime.setFlowInstId(Overtime.getString("flowInstId"));
                    hrGongdanYzjOvertime.setTitle(Overtime.getString("title"));
                    hrGongdanYzjOvertime.setStatus(Overtime.getString("status"));
                    hrGongdanYzjOvertime.setFormCodeId(Overtime.getString("formCodeId"));
                    hrGongdanYzjOvertime.setFormInstId(Overtime.getString("formInstId"));
                    if (hrGongdanYzjOvertimeDb == null) {
                        hrGongdanYzjOvertime.save();
                    }else {
                        hrGongdanYzjOvertime.setId(hrGongdanYzjOvertimeDb.getId());
                        hrGongdanYzjOvertime.update();
                    }
                    getWorkOverTimeDetail(Overtime.getString("formInstId"),Overtime.getString("formCodeId"));
                }
            }
        }
        renderJson(R.ok());
    }
    /**
     * 加班流程分页获取并保存数据库
     */
    public void doOvertimeOne() throws Exception {
        renderJson(R.ok().put("data", this.getOvertime(1)));
    }
    /**
     * 加班流程分页获取数据
     */
    @NotAction
    public String getOvertime( Integer pageNumber) throws Exception {
        String listUrl = tokenService.getGatewayHost().concat("/workflow/form/thirdpart/findFlows?accessToken=")
                .concat(tokenService.getAccessToken(tokenService.getFid(),tokenService.getFlowsecret(), tokenService.getEid(), "team"));
        Map map = new HashMap();
        map.put("identifyKey","8ACyQrkXMqDeJTYL");
        List list1 = new ArrayList();
        list1.add("45aedb0643814d38b9e365acab352bab");
        map.put("formCodeIds",list1);
        map.put("pageNumber",pageNumber);
        map.put("pageSize",500);
        List list2 = new ArrayList();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate1 = dateFormat1.parse("2021-01-01");
        list2.add(myDate1.getTime());
        map.put("creatTime",list2);
        return tokenService.gatewayRequestJson(listUrl, JSON.toJSONString(map));
    }

    /*
     * @Description //获取加班明细并保存到数据库
     * @Author wangkaida
     * @Date 9:28 2021/3/4
     * @Param [formInstId, formCodeId]
     * @return boolean
     **/
    @NotAction
    public boolean getWorkOverTimeDetail(String formInstId,String formCodeId) throws Exception {
        String listUrl = tokenService.getGatewayHost().concat("/workflow/form/thirdpart/viewFormInst?accessToken=")
                .concat(tokenService.getAccessToken(tokenService.getFid(),tokenService.getFlowsecret(), tokenService.getEid(), "team"));
        Map map = new HashMap();
        map.put("formInstId",formInstId);
        map.put("formCodeId",formCodeId);
        String overTimeDetailReturn = tokenService.gatewayRequestJson(listUrl, JSON.toJSONString(map));
        JSONObject overTimeDetailResult = JSONObject.parseObject(overTimeDetailReturn);
        if (overTimeDetailResult != null) {
            if(overTimeDetailResult.getInteger("errorCode") == 0
                    && overTimeDetailResult.getJSONObject("data") !=null){
                JSONObject widgetMap = overTimeDetailResult.getJSONObject("data").getJSONObject("formInfo").getJSONObject("widgetMap");
                JSONObject detailMap = overTimeDetailResult.getJSONObject("data").getJSONObject("formInfo").getJSONObject("detailMap");
                //加班信息
                JSONObject widgetValue = (JSONObject)detailMap.getJSONObject("_S_INT_OVERTIME_DETAILED").getJSONArray("widgetValue").get(0);
                //加班类型
                String overtimeTypeStr = widgetValue.getString("_S_INT_OVERTIME_TYPE");

                String overtimeType = "0";
                //加班类型 1.工作日加班 2.周末加班 3.节日加班
                switch(overtimeTypeStr){
                    case "_S_INT_OVERTIME_PS":
                        overtimeType = "1";
                        break;
                    case "_S_INT_OVERTIME_ZM":
                        overtimeType = "2";
                        break;
                    case "_S_INT_OVERTIME_JQ":
                        overtimeType = "3";
                        break;
                }

                //加班时长
                String overtimeHours = widgetValue.getString("_S_INT_OVERTIME_HOURS");
                //加班开始时间
                String overtimeBegin = widgetValue.getJSONArray("_S_INT_OVERTIME_TIME").get(0).toString();
                //加班结束时间
                String overtimeEnd = widgetValue.getJSONArray("_S_INT_OVERTIME_TIME").get(1).toString();

                Date overtimeBeginDate = DateUtil.timestampToDate(overtimeBegin);

                Date overtimeEndDate = DateUtil.timestampToDate(overtimeEnd);

                //加班事由
                String overtimeReason = widgetMap.getJSONObject("_S_INT_OVERTIME_REASON").getString("value");
                //流水号
                String serialNumWidget = widgetMap.getJSONObject("_S_SERIAL").getString("value");

                //保存到加班详细表
                HrGongdanYzjOvertimeDetail hrGongdanYzjOvertimeDetailDb = HrGongdanYzjOvertimeDetail.dao.findFirst(Db.getSql("admin.hrGongdanYzjOvertime.getHrGongdanYzjOvertimeDetail"),serialNumWidget);
                HrGongdanYzjOvertimeDetail hrGongdanYzjOvertimeDetail = new HrGongdanYzjOvertimeDetail();
                hrGongdanYzjOvertimeDetail.setOvertimeType(overtimeType);
                hrGongdanYzjOvertimeDetail.setOvertimeHours(overtimeHours);
                hrGongdanYzjOvertimeDetail.setOvertimeBegin(overtimeBeginDate);
                hrGongdanYzjOvertimeDetail.setOvertimeEnd(overtimeEndDate);
                //hrGongdanYzjOvertimeDetail.setOvertimeReason(overtimeReason);
                hrGongdanYzjOvertimeDetail.setSerialNumWidget(serialNumWidget);
                if (hrGongdanYzjOvertimeDetailDb == null) {
                    hrGongdanYzjOvertimeDetail.save();
                }else {
                    hrGongdanYzjOvertimeDetail.setId(hrGongdanYzjOvertimeDetailDb.getId());
                    hrGongdanYzjOvertimeDetail.update();
                }
                return true;
            }
        }
        return false;
    }
}