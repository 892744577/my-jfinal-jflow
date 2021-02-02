package com.kakarote.crm9.erp.admin.service;

import BP.Tools.StringUtils;
import BP.Web.WebUser;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.kakarote.crm9.common.config.cache.CaffeineCache;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.common.WorkOrderEnum;
import com.kakarote.crm9.erp.crm.service.CrmBusinessService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.FieldUtil;
import com.kakarote.crm9.utils.ParamsUtil;
import com.kakarote.crm9.utils.R;

import java.util.*;
import java.util.stream.Collectors;

public class AdminSceneService{
    @Inject
    private AdminFieldService adminFieldService;

    @Inject
    private CrmBusinessService crmBusinessService;

    /**
     * @author wyq
     * 查询场景字段
     */
    public R queryField(Integer label){
        List<Record> recordList = new LinkedList<>();
        FieldUtil fieldUtil = new FieldUtil(recordList);
        String[] settingArr = new String[]{};
        if(CrmEnum.CRM_LEADS.getType() == label){
            fieldUtil.add("leads_name", "线索名称", "text", settingArr)
                    .add("telephone", "电话", "text", settingArr)
                    .add("mobile", "手机", "mobile", settingArr)
                    .add("address", "地址", "text", settingArr)
                    .add("next_time", "下次联系时间", "datetime", settingArr)
                    .add("remark", "备注", "text", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_CUSTOMER.getType() == label){
            List<Record> dealStatusList = new ArrayList<>();
            dealStatusList.add(new Record().set("name","未成交").set("value",0));
            dealStatusList.add(new Record().set("name","已成交").set("value",1));
            fieldUtil.add("customer_name", "客户名称", "text", settingArr)
                    .add("mobile", "手机", "text", settingArr)
                    .add("telephone", "电话", "text", settingArr)
                    .add("website", "网址", "text", settingArr)
                    .add("next_time", "下次联系时间", "datetime", settingArr)
                    .add("remark", "备注", "text", settingArr)
                    .add("deal_status", "成交状态", "dealStatus", dealStatusList)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr)
                    .add("address", "地区定位", "map_address", settingArr);
        }else if(CrmEnum.CRM_CONTACTS.getType() == label){
            fieldUtil.add("name", "姓名", "text", settingArr)
                    .add("customer_name", "客户名称", "customer", settingArr)
                    .add("mobile", "手机", "mobile", settingArr)
                    .add("telephone", "电话", "text", settingArr)
                    .add("email", "电子邮箱", "email", settingArr)
                    .add("post", "职务", "text", settingArr)
                    .add("address", "地址", "text", settingArr)
                    .add("next_time", "下次联系时间", "datetime", settingArr)
                    .add("remark", "备注", "text", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_PRODUCT.getType() == label){
            fieldUtil.add("name", "产品名称", "text", settingArr)
                    .add("category_id", "产品类别", "category", settingArr)
                    .add("num", "产品编码", "number", settingArr)
                    .add("price", "价格", "floatnumber", settingArr)
                    .add("description", "产品描述", "text", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_BUSINESS.getType() == label){
            fieldUtil.add("business_name", "商机名称", "text", settingArr)
                    .add("customer_name", "客户名称", "customer", settingArr)
                    .add("type_id", "商机状态组", "business_type", crmBusinessService.queryBusinessStatusOptions("condition"))
                    .add("money", "商机金额", "floatnumber", settingArr)
                    .add("deal_date", "预计成交日期", "datetime", settingArr)
                    .add("remark", "备注", "text", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_CONTRACT.getType() == label){
            List<Map<String,Object>> checkList = new ArrayList<>();
            checkList.add(new JSONObject().fluentPut("name", "通过").fluentPut("value", 1));
            checkList.add(new JSONObject().fluentPut("name", "拒绝").fluentPut("value", 2));
            checkList.add(new JSONObject().fluentPut("name", "审核中").fluentPut("value", 3));
            checkList.add(new JSONObject().fluentPut("name", "已撤回").fluentPut("value", 4));
            checkList.add(new JSONObject().fluentPut("name", "未提交").fluentPut("value", 5));
            checkList.add(new JSONObject().fluentPut("name", "已作废").fluentPut("value", 6));
            fieldUtil.add("num", "合同编号", "number", settingArr)
                    .add("name", "合同名称", "text", settingArr)
                    .add("check_status", "审核状态", "checkStatus", checkList)
                    .add("customer_name", "客户名称", "customer", settingArr)
                    .add("business_name", "商机名称", "business", settingArr)
                    .add("order_date", "下单时间", "date", settingArr)
                    .add("money", "合同金额", "floatnumber", settingArr)
                    .add("start_time", "合同开始时间", "datetime", settingArr)
                    .add("end_time", "合同结束时间", "datetime", settingArr)
                    .add("contacts_name", "客户签约人", "contacts", settingArr)
                    .add("company_user_id", "公司签约人", "user", settingArr)
                    .add("remark", "备注", "number", settingArr)
                    .add("product", "产品", "product", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else if(CrmEnum.CRM_RECEIVABLES.getType() == label){
            List<Map<String,Object>> checkList = new ArrayList<>();
            checkList.add(new JSONObject().fluentPut("name", "通过").fluentPut("value", 1));
            checkList.add(new JSONObject().fluentPut("name", "拒绝").fluentPut("value", 2));
            checkList.add(new JSONObject().fluentPut("name", "审核中").fluentPut("value", 3));
            checkList.add(new JSONObject().fluentPut("name", "已撤回").fluentPut("value", 4));
            checkList.add(new JSONObject().fluentPut("name", "未提交").fluentPut("value", 5));
            fieldUtil.add("number", "回款编号", "number", settingArr)
                    .add("check_status", "审核状态", "checkStatus", checkList)
                    .add("customer_name", "客户名称", "customer", settingArr)
                    .add("contract_num", "合同编号", "contract", settingArr)
                    .add("return_time", "回款日期", "date", settingArr)
                    .add("money", "回款金额", "floatnumber", settingArr)
                    .add("remark", "备注", "textarea", settingArr)
                    .add("owner_user_id", "负责人", "user", settingArr)
                    .add("create_user_id", "创建人", "user", settingArr)
                    .add("update_time", "更新时间", "datetime", settingArr)
                    .add("create_time", "创建时间", "datetime", settingArr);
        }else{
            return R.error("场景label不符合要求！");
        }
        recordList = fieldUtil.getRecordList();
        List<Record> records = adminFieldService.customFieldList(label);
        if(recordList != null && records != null){
            for(Record r : records){
                r.set("field_name", r.getStr("name"));
            }
            recordList.addAll(records);
        }
        return R.ok().put("data", recordList);
    }

    /**
     * @author wyq
     * 增加场景
     */
    @Before(Tx.class)
    public R addScene(AdminScene adminScene) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        adminScene.setIsHide(0).setSort(99999).setIsSystem(0).setCreateTime(DateUtil.date()).setUserId(userId);
        adminScene.save();
        if(1 == adminScene.getIsDefault()){
            AdminSceneDefault adminSceneDefault = new AdminSceneDefault();
            adminSceneDefault.setSceneId(adminScene.getSceneId()).setType(adminScene.getType()).setUserId(userId).save();
        }
        return R.ok();
    }

    /**
     * @author wyq
     * 更新场景
     */
    @Before(Tx.class)
    public R updateScene(AdminScene adminScene) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        AdminScene oldAdminScene = AdminScene.dao.findById(adminScene.getSceneId());
        if(1 == adminScene.getIsDefault()){
            Db.update("update aptenon_admin_scene_default set scene_id = ? where user_id = ? and type = ?", adminScene.getSceneId(), userId, oldAdminScene.getType());
        }
        adminScene.setUserId(userId).setType(oldAdminScene.getType()).setSort(oldAdminScene.getSort()).setIsSystem(oldAdminScene.getIsSystem()).setUpdateTime(DateUtil.date());
        return R.isSuccess(adminScene.update());
    }

    /**
     * @author wyq
     * 设置默认场景
     */
    @Before(Tx.class)
    public R setDefaultScene(Integer sceneId) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        AdminScene oldAdminScene = AdminScene.dao.findById(sceneId);
        Db.delete("delete from aptenon_admin_scene_default where user_id = ? and type = ?", userId, oldAdminScene.getType());
        AdminSceneDefault adminSceneDefault = new AdminSceneDefault();
        return adminSceneDefault.setSceneId(sceneId).setType(oldAdminScene.getType()).setUserId(userId).save() ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 删除场景
     */
    @Before(Tx.class)
    public R deleteScene(AdminScene adminScene){
        if(1 == AdminScene.dao.findById(adminScene.getSceneId()).getIsSystem()){
            return R.error("系统场景不能删除");
        }
        return AdminScene.dao.deleteById(adminScene.getSceneId()) ? R.ok() : R.error();
    }

    /**
     * @author wyq
     * 查询场景
     */
    @Before(Tx.class)
    public R queryScene(Integer type) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        //查询userId下是否有系统场景，没有则插入
        Integer number = Db.queryInt(Db.getSql("admin.scene.querySystemNumber"), type, userId);
        if(number.equals(0)){
            AdminScene systemScene = new AdminScene();
            systemScene.setUserId(userId).setSort(0).setData("").setIsHide(0).setIsSystem(1).setCreateTime(DateUtil.date()).setType(type);
            JSONObject ownerObject = new JSONObject();
            ownerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "is").fluentPut("value", userId));
            JSONObject subOwnerObject = new JSONObject();
            subOwnerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "in").fluentPut("value", getSubUserId(userId, BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1)));
            if(CrmEnum.CRM_LEADS.getType() == type){
                systemScene.setName("全部线索").setData(new JSONObject().fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0)).toString()).save();
                ownerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "is").fluentPut("value", userId)).fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0));
                systemScene.setSceneId(null).setName("我负责的线索").setData(ownerObject.toString()).save();
                subOwnerObject.fluentPut("owner_user_id", new JSONObject().fluentPut("name", "owner_user_id").fluentPut("condition", "in").fluentPut("value", getSubUserId(userId, BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1))).fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", 0));
                systemScene.setSceneId(null).setName("下属负责的线索").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", "1"));
                systemScene.setSceneId(null).setName("已转化的线索").setData(jsonObject.toString()).setBydata("transform").save();
            }else if(CrmEnum.CRM_CUSTOMER.getType() == type){
                systemScene.setName("全部客户").save();
                systemScene.setSceneId(null).setName("我负责的客户").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的客户").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的客户").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_CONTACTS.getType() == type){
                systemScene.setName("全部联系人").save();
                systemScene.setSceneId(null).setName("我负责的联系人").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的联系人").setData(subOwnerObject.toString()).save();
            }else if(CrmEnum.CRM_PRODUCT.getType() == type){
                systemScene.setName("全部产品").save();
                systemScene.setSceneId(null).setName("上架的产品").setData(new JSONObject().fluentPut("是否上下架", new JSONObject().fluentPut("name", "是否上下架").fluentPut("condition", "is").fluentPut("value", "上架")).toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("是否上下架", new JSONObject().fluentPut("name", "是否上下架").fluentPut("condition", "is").fluentPut("value", "下架"));
                systemScene.setSceneId(null).setName("下架的产品").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_BUSINESS.getType() == type){
                systemScene.setName("全部商机").save();
                systemScene.setSceneId(null).setName("我负责的商机").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的商机").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的商机").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_CONTRACT.getType() == type){
                systemScene.setName("全部合同").save();
                systemScene.setSceneId(null).setName("我负责的合同").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的合同").setData(subOwnerObject.toString()).save();
                JSONObject jsonObject = new JSONObject();
                jsonObject.fluentPut("ro_user_id", new JSONObject().fluentPut("name", "ro_user_id").fluentPut("condition", "takePart").fluentPut("value", userId));
                systemScene.setSceneId(null).setName("我参与的合同").setData(jsonObject.toString()).save();
            }else if(CrmEnum.CRM_RECEIVABLES.getType() == type){
                systemScene.setName("全部回款").save();
                systemScene.setSceneId(null).setName("我负责的回款").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("下属负责的回款").setData(subOwnerObject.toString()).save();
            }
        }
        return R.ok().put("data", Db.find(Db.getSql("admin.scene.queryScene"), type, userId));
    }

    /**
     * 递归查询下属id
     */
    public String getSubUserId(String userId, Integer deepness){
        StringBuilder ids = new StringBuilder();
        if(deepness > 0){
            List<String> list = Db.query("select user_id from aptenon_admin_user where parent_id = ?", userId);
            if(list != null && list.size() > 0){
                for(String l : list){
                    ids.append(",").append(l).append(getSubUserId(l, deepness - 1));
                }
            }
        }
        return StrUtil.isNotEmpty(ids.toString().trim()) ? ids.toString() : ",0";
    }

    /**
     * @author wyq
     * 查询场景设置
     */
    public R querySceneConfig(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        List<Record> valueList = Db.find(Db.getSql("admin.scene.queryScene"), adminScene.getType(), userId);
        for(Record scene : valueList){
            if(StrUtil.isNotEmpty(scene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(scene.getStr("data"));
                scene.set("data", jsonObject);
            }
        }
        List<Record> hideValueList = Db.find(Db.getSql("admin.scene.queryHideScene"), adminScene.getType(), userId);
        for(Record hideScene : hideValueList){
            if(StrUtil.isNotEmpty(hideScene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(hideScene.getStr("data"));
                hideScene.set("data", jsonObject);
            }
        }
        return R.ok().put("data", Kv.by("value", valueList).set("hide_value", hideValueList));
    }

    /**
     * @author wyq
     * 设置场景
     */
    @Before(Tx.class)
    public R sceneConfig(AdminScene adminScene){
        Long userId = BaseUtil.getUser().getUserId();
        String[] sortArr = adminScene.getNoHideIds().split(",");
        for(int i = 0; i < sortArr.length; i++){
            Db.update(Db.getSql("admin.scene.sort"), i + 1, adminScene.getType(), userId, sortArr[i]);
        }
        if(null != adminScene.getHideIds()){
            String[] hideIdsArr = adminScene.getHideIds().split(",");
            Record number = Db.findFirst(Db.getSqlPara("admin.scene.queryIsHideSystem", Kv.by("ids", hideIdsArr)));
            if(number.getInt("number") > 0){
                return R.error("系统场景不能隐藏");
            }
            Db.update(Db.getSqlPara("admin.scene.isHide", Kv.by("ids", hideIdsArr).set("type", adminScene.getType()).set("userId", userId)));
        }
        return R.ok();
    }

    public R filterConditionAndGetPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer sceneId = jsonObject.getInteger("sceneId");
        JSONObject data = new JSONObject();
        if(sceneId != null && sceneId != 0){
            data = JSON.parseObject(AdminScene.dao.findById(sceneId).getData());
        }
        if(sceneId == null && jsonObject.getInteger("type") == 1){
            data = new JSONObject().fluentPut("is_transform", new JSONObject().fluentPut("name", "is_transform").fluentPut("condition", "is").fluentPut("value", "0"));
        }
        if(jsonObject.getJSONObject("data") != null){
            if(data != null){
                data.putAll(jsonObject.getJSONObject("data"));
            }
        }
        jsonObject.put("data",data);
        basePageRequest.setJsonObject(jsonObject);
//        return getCrmPageList(basePageRequest);
        return getHrGongdanPageList(basePageRequest);
    }

    /*
     * @Description //工单场景列表查询
     * @Author wangkaida
     * @Date 13:41 2020/7/29
     * @Param [basePageRequest]
     * @return com.kakarote.crm9.utils.R
     **/
    public R getHrGongdanPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Integer statistics = jsonObject.getInteger("statistics");
        Integer status = jsonObject.getInteger("status");
        basePageRequest.setJsonObject(jsonObject);
        if (StrUtil.isEmpty(String.valueOf(statistics))){
            return R.error("statistics参数不能为空!");
        }

        //查询条件
        String search = basePageRequest.getJsonObject().getString("search");
        Kv kv = Kv.by("search",search);

        //时间
        if(statistics==1){ //本日
            String today = com.kakarote.crm9.erp.wx.util.DateUtil.changeDateTOStr3(new Date());
            kv.set("today",today);
        }else if(statistics==2){ //本周
            String weekend = com.kakarote.crm9.erp.wx.util.DateUtil.changeDateTOStr3(new Date());
            kv.set("weekend",weekend);
        }else if(statistics==3) { //本月
            String yearmonth = com.kakarote.crm9.erp.wx.util.DateUtil.changeDateTOStr6(new Date());
            kv.set("yearmonth", yearmonth);
        }

        //状态
        if(status==1){
            kv.set("confirm", "902");
        }else if(status==2) {
            String overtime = com.kakarote.crm9.erp.wx.util.DateUtil.changeDateTOStr3(new Date());
            kv.set("overtime", overtime);
        }else if(status==3) {
            List list = new ArrayList();
            list.add("906");
            list.add("907");
            kv.set("toBeCompleted", list);
        }else if(status==4) {
            kv.set("serviceSp", 2);
        }

        //平台内服务商
        String acceptor = basePageRequest.getJsonObject().getString("acceptor");
        kv.set("acceptor",acceptor);

        //平台内服务商-团员
        String master = basePageRequest.getJsonObject().getString("master");
        kv.set("master",master);

        //其他第三方系统
        String serviceSystem = basePageRequest.getJsonObject().getString("serviceSystem");
        kv.set("serviceSystem",serviceSystem);

        String realm;
        List<JSONObject> queryList = new ArrayList<>();
        Integer type = jsonObject.getInteger("type");
        //自定义字段列表
        Map<String,AdminField> fieldMap = getWorkOrderAdminFieldMap(type);
        //权限标识
        switch(type){
            case 0:
                realm = "gongdan";
                break;
            default:
                return R.error("type不符合要求");
        }
        JSONObject data = basePageRequest.getJsonObject().getJSONObject("data");
        if (!appendSqlCondition(kv, fieldMap, queryList, data)){
            return R.error("参数包含非法字段");
        }

        kv.set("orderByKey", "RDT").set("orderByType", "desc").set("fieldType", 1);
        kv.set("page", (basePageRequest.getPage() - 1) * basePageRequest.getLimit()).set("limit", basePageRequest.getLimit());
        String selectSql = "select * ";
        JSONObject resultJsonObject = new JSONObject();
        List<String> batchList = queryBatchId(queryList);
        if(batchList.size()==0&&kv.containsKey("field")){
            batchList.add("");
        }
        kv.set("select", selectSql).set("queryList", queryList).set("realm", realm).set("fieldMap", fieldMap).set("label", type);
        kv.set("batchList",batchList);
        SqlPara sqlPara = Db.getSqlPara("admin.hrGongDan.getHrGongDanListPage", kv);
        List<Record> recordPage = Db.find(sqlPara);
        resultJsonObject.put("list", recordPage);
        SqlPara countSql = Db.getSqlPara("admin.hrGongDan.queryHrGongDanListCount", kv);
        Integer count = Db.queryInt(countSql.getSql(), countSql.getPara());
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data", resultJsonObject);
    }

    /*
     * @Author wangkaida
     * @Date 18:34 2020/7/29
     * @Param [type]
     * @return java.util.Map<java.lang.String,com.kakarote.crm9.erp.admin.entity.AdminField>
     **/
    private Map<String, AdminField> getWorkOrderAdminFieldMap(Integer type) {
        Map<String,AdminField> adminFieldMap = CaffeineCache.ME.get("field", type);
        if(adminFieldMap == null){
            List<AdminField> adminFields = AdminField.dao.find("SELECT field_name,field_type,type FROM aptenon_admin_field WHERE label=?", type);
            adminFieldMap = new HashMap<>();
            for(AdminField adminField : adminFields){
                adminFieldMap.put(adminField.getFieldName(), adminField);
            }
            AdminField adminField=new AdminField();
            adminField.setFieldType(1);
//            adminFieldMap.put("owner_user_id",adminField);
//            adminFieldMap.put("create_user_id",adminField);
//            adminFieldMap.put("create_time",adminField);
//            adminFieldMap.put("update_time",adminField);
//            adminFieldMap.put("is_transform",adminField);
//            adminFieldMap.put("deal_status",adminField);
//            adminFieldMap.put("customer_id",adminField);
//            adminFieldMap.put("contract_id",adminField);
//            adminFieldMap.put("contacts_id",adminField);
//            adminFieldMap.put("leads_id",adminField);
//            adminFieldMap.put("receivables_id",adminField);
//            adminFieldMap.put("product_id",adminField);
//            adminFieldMap.put("business_id",adminField);
        }
        return adminFieldMap;
    }

    /**
     * @author zhangzhiwei
     * Crm列表页查询
     */
    public R getCrmPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject();
        Kv kv = new Kv();
        String realm;
        List<JSONObject> queryList = new ArrayList<>();
        Integer type = jsonObject.getInteger("type");
        //自定义字段列表
        Map<String,AdminField> fieldMap = getAdminFieldMap(type == 9 ? 2 : type);
        //权限标识
        switch(type){
            case 1:
                realm = "leads";
                break;
            case 2:
                realm = "customer";
                break;
            case 3:
                realm = "contacts";
                break;
            case 4:
                realm = "product";
                break;
            case 5:
                realm = "business";
                break;
            case 6:
                realm = "contract";
                break;
            case 7:
                realm = "receivables";
                break;
            case 9:
                realm = "customer";
                break;
            case 0:
                realm = "business";
                if(jsonObject.getJSONObject("data").containsKey("create_time")){
                    JSONObject sqlObject = new JSONObject();
                    JSONObject date=jsonObject.getJSONObject("data").getJSONObject("create_time");
                    sqlObject.put("sql", "and (SELECT COUNT(*) FROM aptenon_crm_business_change as b WHERE b.business_id = a.business_id and (b.create_time between '"+date.getString("start")+"' and  '"+date.getString("end")+"'))>0");
                    sqlObject.put("type", 1);
                    queryList.add(sqlObject);
                    jsonObject.getJSONObject("data").remove("create_time");
                }
                break;
            default:
                return R.error("type不符合要求");
        }
        JSONObject data = basePageRequest.getJsonObject().getJSONObject("data");
        if (!appendSqlCondition(kv, fieldMap, queryList, data)){
            return R.error("参数包含非法字段");
        }
        String search = basePageRequest.getJsonObject().getString("search");
        if(StrUtil.isNotEmpty(search)){
            if (!appendSqlSearch(type, queryList, search)){
                return R.error("参数包含非法字段");
            }
            if(!kv.containsKey("fixed")){
                kv.set("fixed",true);
            }
        }

        String camelField = basePageRequest.getJsonObject().getString("sortField");
        String sortField = StrUtil.toUnderlineCase(camelField);
        String orderNum = basePageRequest.getJsonObject().getString("order");
        if(StrUtil.isEmpty(sortField) || StrUtil.isEmpty(orderNum)){
            kv.set("orderByKey", "update_time").set("orderByType", "desc").set("fieldType", 1);
        }else{
            if(! ParamsUtil.isValid(sortField)){
                return R.error("参数包含非法字段");
            }
            orderNum = "2".equals(orderNum) ? "asc" : "desc";
            kv.set("orderByKey", sortField).set("orderByType", orderNum).set("fieldType", fieldMap.get(sortField) != null ? fieldMap.get(sortField).getFieldType() : 0);
        }
        StringBuilder conditions = new StringBuilder();
        if(2 == type){
            conditions.append(" and owner_user_id is not null");
        }else if(9 == type){
            conditions.append(" and owner_user_id is null");
        }else if(4 == type){
            conditions.append(" and status != '3'");
        }
        Long userId = BaseUtil.getUserId();
        List<Integer> roles = BaseUtil.getUser().getRoles();
        if((!type.equals(9) && ! type.equals(4)) && ! BaseConstant.SUPER_ADMIN_USER_ID.equals(userId) && !roles.contains(BaseConstant.SUPER_ADMIN_ROLE_ID)){
            List<Long> longs = Aop.get(AdminUserService.class).queryUserByAuth(userId, realm);
            if(longs != null && longs.size() > 0){
                conditions.append(" and (owner_user_id in (").append(StrUtil.join(",", longs)).append(")");
                if(type.equals(2) || type.equals(6) || type.equals(5)){
                    conditions.append(" or ro_user_id like CONCAT('%,','").append(userId).append("',',%')").append(" or rw_user_id like CONCAT('%,','").append(userId).append("',',%')");
                }
                conditions.append(")");
            }
        }
        JSONObject sqlObject = new JSONObject();
        sqlObject.put("sql", conditions.toString());
        sqlObject.put("type", 1);
        if(!kv.containsKey("fixed")){
            kv.set("fixed",true);
        }
        queryList.add(sqlObject);
        if(StrUtil.isEmpty(basePageRequest.getJsonObject().getString("excel"))){
            kv.set("page", (basePageRequest.getPage() - 1) * basePageRequest.getLimit()).set("limit", basePageRequest.getLimit());
        }
        String selectSql;
        JSONObject resultJsonObject = new JSONObject();
        if(2 == type){
            Integer configType = Db.queryInt("select status from aptenon_admin_config where name = 'customerPoolSetting'");
            selectSql = 1 == configType ? Db.getSql("admin.scene.getCustomerPageList") : "select *,(select count(*) from aptenon_crm_business as a where a.customer_id = views.customer_id) as business_count";
        }else if(6 == type){
            selectSql = "select *,IFNULL((select SUM(a.money) from aptenon_crm_receivables as a where a.check_status = '1' and a.contract_id = views.contract_id),0) as receivedMoney,(IFNUll(money,0) - IFNULL((select SUM(a.money) from aptenon_crm_receivables as a where a.check_status = '1' and a.contract_id = views.contract_id),0)) as unreceivedMoney";
        }else if(9 == type){
            Integer putInPoolTodayNum = Db.queryInt(Db.getSql("admin.scene.queryPutInPoolTodayNum"));
            selectSql = "select *";
            resultJsonObject.put("putInPoolTodayNum", putInPoolTodayNum);
        }else{
            selectSql = "select * ";
        }
        List<String> batchList = queryBatchId(queryList);
        if(batchList.size()==0&&kv.containsKey("field")){
            batchList.add("");
        }
        kv.set("select", selectSql).set("queryList", queryList).set("realm", realm).set("fieldMap", fieldMap).set("label", type == 9 ? 2 : type);
        kv.set("batchList",batchList);
        SqlPara sqlPara;
        if(kv.getInt("fieldType") == 0){
            sqlPara = Db.getSqlPara("admin.scene.queryCrmPageListByFieldType2", kv);
        }else{
            sqlPara = Db.getSqlPara("admin.scene.queryCrmPageListByFieldType1", kv);
        }
        List<Record> recordPage = Db.find(sqlPara);
        if(type == CrmEnum.CRM_BUSINESS.getType()){
            recordPage.forEach(record -> {
                if(record.getInt("is_end") == 1){
                    record.set("status_name", "赢单");
                }else if(record.getInt("is_end") == 2){
                    record.set("status_name", "输单");
                }else if(record.getInt("is_end") == 3){
                    record.set("status_name", "无效");
                }
            });
            setBusinessStatus(recordPage);
        }else if (type == CrmEnum.CRM_CONTRACT.getType()){
            if(recordPage.size() > 0){
                List<Integer> contractIds = recordPage.stream().map(record -> record.getInt("contract_id")).collect(Collectors.toList());
                Record record = Db.findFirst("SELECT IFNULL(SUM(money),0) AS contractMoney,IFNULL(SUM(receivedMoney),0) AS receivedMoney from (SELECT a.money,(SELECT SUM(money) FROM aptenon_crm_receivables AS b where b.contract_id=a.contract_id and b.check_status = 1) as receivedMoney FROM aptenon_crm_contract AS a WHERE a.check_status = '1' AND a.contract_id IN (" + StrUtil.join(",", contractIds) + ")) as x");
                resultJsonObject.put("money", record);
            }
        }
        resultJsonObject.put("list", recordPage);
        SqlPara countSql = Db.getSqlPara("admin.scene.queryCrmPageListCount", kv);
        Integer count = Db.queryInt(countSql.getSql(), countSql.getPara());
        resultJsonObject.put("totalRow", count);
        return R.ok().put("data", resultJsonObject);
    }

    private boolean appendSqlSearch(Integer type, List<JSONObject> queryList, String search) {
        if (!ParamsUtil.isValid(search)) {
            return false;
        }
        StringBuilder conditions = new StringBuilder();
        if (type == 1) {
            conditions.append(" and (leads_name like '%").append(search).append("%' or telephone like '%")
                    .append(search).append("%' or mobile like '%").append(search).append("%')");
        } else if (type == 2 || type == 9) {
            conditions.append(" and (customer_name like '%").append(search).append("%' or telephone like '%")
                    .append(search).append("%' or mobile like '%").append(search).append("%')");
        } else if (type == 3) {
            conditions.append(" and (name like '%").append(search).append("%' or telephone like '%")
                    .append(search).append("%' or mobile like '%").append(search).append("%')");
        } else if (type == 4 || type == 6) {
            conditions.append(" and (name like '%").append(search).append("%')");
        } else if (type == 5) {
            conditions.append(" and (business_name like '%").append(search).append("%')");
        } else {
            conditions.append(" and (number like '%").append(search).append("%')");
        }
        JSONObject sqlObject = new JSONObject();
        sqlObject.put("sql", conditions.toString());
        sqlObject.put("type", 1);
        queryList.add(sqlObject);
        return true;
    }

    private boolean appendSqlCondition(Kv kv, Map<String, AdminField> fieldMap, List<JSONObject> queryList, JSONObject data) {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if(data != null){
            data.forEach((k, v) -> jsonObjectList.add(JSON.parseObject(v.toString())));
        }
        for(JSONObject jsonObject : jsonObjectList){
            String condition = jsonObject.getString("condition");
            String value = jsonObject.getString("value");
            String name = jsonObject.getString("name");
            StringBuilder conditions = new StringBuilder();
            JSONObject sqlObject = new JSONObject();
            if(! ParamsUtil.isValid(name)){
                return false;
            }
            if(StrUtil.isNotEmpty(value) && ! ParamsUtil.isValid(value)){
                return false;
            }
            if(StrUtil.isNotEmpty(jsonObject.getString("start")) && ! ParamsUtil.isValid(jsonObject.getString("start"))){
                return false;
            }
            if(StrUtil.isNotEmpty(jsonObject.getString("end")) && ! ParamsUtil.isValid(jsonObject.getString("end"))){
                return false;
            }
            String formType = jsonObject.getString("formType");
            if("business_type".equals(formType)){
                conditions.append(" and ").append(name).append(" = ").append(jsonObject.getInteger("typeId"));
                if(StrUtil.isNotEmpty(jsonObject.getString("statusId"))){
                    if("win".equals(jsonObject.getString("statusId"))){
                        conditions.append(" and is_end = 1");
                    }else if("lose".equals(jsonObject.getString("statusId"))){
                        conditions.append(" and is_end = 2");
                    }else if("invalid".equals(jsonObject.getString("statusId"))){
                        conditions.append(" and is_end = 3");
                    }else{
                        conditions.append(" and status_id = ").append(jsonObject.getString("statusId"));
                    }
                }
                sqlObject.put("type", 1);
            }else if("map_address".equals(formType)){
                String address = value.substring(0, value.length() - 1);
                conditions.append(" and ").append(name).append(" like '%").append(address).append("%'");
                sqlObject.put("type", 1);
            }else {
                if(StrUtil.isNotEmpty(value) || StrUtil.isNotEmpty(jsonObject.getString("start")) || StrUtil.isNotEmpty(jsonObject.getString("end"))){
                    if("takePart".equals(condition)){
                        conditions.append(" and (ro_user_id like '%,").append(value).append(",%' or rw_user_id like '%,").append(value).append(",%')");
                        sqlObject.put("type", 1);
                    }else{
                        sqlObject.put("name",name);
                        conditions.append(" and ").append(name);
                        if("is".equals(condition)){
                            conditions.append(" = '").append(value).append("'");
                            sqlObject.put("connector","=");
                            sqlObject.put("value","'"+value+"'");
                        }else if("isNot".equals(condition)){
                            conditions.append(" != '").append(value).append("'");
                            sqlObject.put("connector","!=");
                            sqlObject.put("value","'"+value+"'");
                        }else if("contains".equals(condition)){
                            conditions.append(" like '%").append(value).append("%'");
                            sqlObject.put("connector","like");
                            sqlObject.put("value","'%"+value+"%'");
                        }else if("notContains".equals(condition)){
                            conditions.append(" not like '%").append(value).append("%'");
                            sqlObject.put("connector","not like");
                            sqlObject.put("value","'%"+value+"%'");
                        }else if("isNull".equals(condition)){
                            conditions.append(" is null");
                            sqlObject.put("connector","is null");
                            sqlObject.put("value","");
                        }else if("isNotNull".equals(condition)){
                            conditions.append(" is not null");
                            sqlObject.put("connector","is not null");
                            sqlObject.put("value","");
                        }else if("gt".equals(condition)){
                            conditions.append(" > ").append(value);
                            sqlObject.put("connector",">");
                            sqlObject.put("value",value);
                        }else if("egt".equals(condition)){
                            conditions.append(" >= ").append(value);
                            sqlObject.put("connector",">=");
                            sqlObject.put("value",value);
                        }else if("lt".equals(condition)){
                            conditions.append(" < ").append(value);
                            sqlObject.put("connector","<");
                            sqlObject.put("value",value);
                        }else if("elt".equals(condition)){
                            conditions.append(" <= ").append(value);
                            sqlObject.put("connector","<=");
                            sqlObject.put("value",value);
                        }else if("in".equals(condition)){
                            conditions.append(" in (").append(value).append(")");
                            sqlObject.put("connector","in");
                            sqlObject.put("value","("+value+")");
                        }
                        if("datetime".equals(formType) || "date".equals(formType)){
                            conditions.append(" between '").append(jsonObject.getString("start")).append("' and '").append(jsonObject.getString("end")).append("'");
                            sqlObject.put("connector","between");
                            sqlObject.put("value","'"+jsonObject.getString("start")+"'"+" and '"+jsonObject.getString("end")+"'");
                        }
                        sqlObject.put("type", fieldMap.get(name) != null ? fieldMap.get(name).getFieldType() : 2);
                    }
                }else {
                    continue;
                }
            }
            sqlObject.put("sql", conditions.toString());
            if(sqlObject.get("type").equals(1)&&!kv.containsKey("fixed")){
                kv.put("fixed",true);
            }else if(sqlObject.get("type").equals(0)&&!kv.containsKey("field")){
                kv.put("field",true);
            }
            queryList.add(sqlObject);
        }
        return true;
    }

    public Map<String,AdminField> getAdminFieldMap(Integer type){
        Map<String,AdminField> adminFieldMap = CaffeineCache.ME.get("field", type);
        if(adminFieldMap == null){
            List<AdminField> adminFields = AdminField.dao.find("SELECT field_name,field_type,type FROM aptenon_admin_field WHERE label=?", type);
            adminFieldMap = new HashMap<>();
            for(AdminField adminField : adminFields){
                adminFieldMap.put(adminField.getFieldName(), adminField);
                if(adminField.getType().equals(12) && ! adminFieldMap.containsKey("dept")){
                    adminFieldMap.put("dept", null);
                }else if(adminField.getType().equals(10) && ! adminFieldMap.containsKey("user")){
                    adminFieldMap.put("user", null);
                }
            }
            AdminField adminField=new AdminField();
            adminField.setFieldType(1);
            adminFieldMap.put("owner_user_id",adminField);
            adminFieldMap.put("create_user_id",adminField);
            adminFieldMap.put("create_time",adminField);
            adminFieldMap.put("update_time",adminField);
            adminFieldMap.put("is_transform",adminField);
            adminFieldMap.put("deal_status",adminField);
            adminFieldMap.put("customer_id",adminField);
            adminFieldMap.put("contract_id",adminField);
            adminFieldMap.put("contacts_id",adminField);
            adminFieldMap.put("leads_id",adminField);
            adminFieldMap.put("receivables_id",adminField);
            adminFieldMap.put("product_id",adminField);
            adminFieldMap.put("business_id",adminField);
        }
        return adminFieldMap;
    }

    public void setBusinessStatus(List<Record> list){
        list.forEach(record -> {
            if(record.getInt("is_end") == 0){
                Integer sortNum = Db.queryInt("select order_num from aptenon_crm_business_status where status_id = ?", record.getInt("status_id"));
                int totalStatsNum = Db.queryInt("select count(*) from aptenon_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                if(sortNum == null){
                    sortNum = 0;
                }
                record.set("progressBar", sortNum + "/" + totalStatsNum);
            }else if(record.getInt("is_end") == 1){
                int totalStatsNum = Db.queryInt("select count(*) from aptenon_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                record.set("progressBar", totalStatsNum + "/" + totalStatsNum);
            }else if(record.getInt("is_end") == 2){
                int totalStatsNum = Db.queryInt("select count(*) from aptenon_crm_business_status where type_id = ?", record.getInt("type_id")) + 1;
                record.set("progressBar", "0/" + totalStatsNum);
            }else if(record.getInt("is_end") == 3){
                record.set("progressBar", "0/0");
            }
        });
    }

    /**
     * 自定义字段的查询
     * @author zhangzhiwei
     */
    private List<String> queryBatchId(List<JSONObject> queryList){
        List<String> arrayList=new ArrayList<>();
        queryList.forEach(jsonObject -> {
            if(jsonObject.containsKey("type")&&jsonObject.get("type").equals(0)){
                JSONObject object= (JSONObject) jsonObject.clone();
                object.put("batchList",arrayList);
                SqlPara sqlPara=Db.getSqlPara("admin.scene.queryBatchId",object);
                arrayList.clear();
                List<String> batchIdList=Db.query(sqlPara.getSql(),sqlPara.getPara());
                if(batchIdList.size()==0){
                    arrayList.add("");
                }else {
                    arrayList.addAll(batchIdList);
                }
            }
        });
        return arrayList;
    }

    /*
     * @Description //查询工单场景
     * @Author wangkaida
     * @Date 15:13 2020/6/30
     * @Param [type]
     * @return com.kakarote.crm9.utils.R
     **/
    @Before(Tx.class)
    public R queryWorkOrderScene(Integer type) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        String fkDept = WebUser.getFK_Dept();
        if(StringUtils.isEmpty(userId)){
            R.error("用户没登陆，请重新登陆");
        }
        //查询userId下是否有系统场景，没有则插入
        Integer number = Db.queryInt(Db.getSql("admin.scene.querySystemNumber"), type, userId);
        if(number.equals(0)){
            AdminScene systemScene = new AdminScene();
            systemScene.setUserId(userId).setSort(0).setData("").setIsHide(0).setIsSystem(1).setCreateTime(DateUtil.date()).setType(type);
            JSONObject ownerObject = new JSONObject();
            ownerObject.fluentPut("FlowStarter", new JSONObject().fluentPut("name", "FlowStarter").fluentPut("condition", "is").fluentPut("value", userId));
            JSONObject subOwnerObject = new JSONObject();
//            subOwnerObject.fluentPut("FlowStarter", new JSONObject().fluentPut("name", "FlowStarter").fluentPut("condition", "in").fluentPut("value", getSubUserId(userId, BaseConstant.AUTH_DATA_RECURSION_NUM).substring(1)));
            subOwnerObject.fluentPut("FK_Dept", new JSONObject().fluentPut("name", "FK_Dept").fluentPut("condition", "is").fluentPut("value", fkDept));
            if(WorkOrderEnum.WorkOrder_NULL.getType() == type){
                systemScene.setName("全部工单").save();
                systemScene.setSceneId(null).setName("我负责的工单").setData(ownerObject.toString()).save();
                systemScene.setSceneId(null).setName("本部门负责的工单").setData(subOwnerObject.toString()).save();
            }
        }
        return R.ok().put("data", Db.find(Db.getSql("admin.scene.queryScene"), type, userId));
    }

    /*
     * @Description //查询工单场景字段
     * @Author wangkaida
     * @Date 9:44 2020/7/27
     * @Param [label]
     * @return com.kakarote.crm9.utils.R
     **/
    public R queryWorkOrderField(Integer label) {
        List<Record> recordList = new LinkedList<>();
        FieldUtil fieldUtil = new FieldUtil(recordList);
        String[] settingArr = new String[]{};
        if(WorkOrderEnum.WorkOrder_NULL.getType() == label){
            List<Record> serviceTypeList = new ArrayList<>();
            List<Record> serviceSystemList = new ArrayList<>();
            List<Record> serviceSegmentationList = new ArrayList<>();
            List<Record> acceptorList = new ArrayList<>();
            List<Record> dutyTimeList = new ArrayList<>();
            //服务单类型
            SysSftable sysSfTableFWDLX = SysSftable.dao.findFirst(Db.getSql("admin.dictionary.getSysSfTableByNo"),"FWDLX");
            if (sysSfTableFWDLX != null) {
                String selectStatement = sysSfTableFWDLX.getSelectStatement();
                String selectStatementSql = selectStatement.replace("~","\'");
                List<Record> dataList = Db.find(selectStatementSql);

                for(int i=0;i<dataList.size();i++){
                    Record record = dataList.get(i);
                    String No = record.getStr("No") ;
                    String Name = record.getStr("Name") ;
                    serviceTypeList.add(new Record().set("name",Name).set("value",No));
                }

            }

            //服务单平台
            SysSftable sysSfTableFWDPT = SysSftable.dao.findFirst(Db.getSql("admin.dictionary.getSysSfTableByNo"),"FWDPT");
            if (sysSfTableFWDPT != null) {
                String selectStatement = sysSfTableFWDPT.getSelectStatement();
                String selectStatementSql = selectStatement.replace("~","\'");
                List<Record> dataList = Db.find(selectStatementSql);

                for(int i=0;i<dataList.size();i++){
                    Record record = dataList.get(i);
                    String No = record.getStr("No") ;
                    String Name = record.getStr("Name") ;
                    serviceSystemList.add(new Record().set("name",Name).set("value",No));
                }

            }

            //服务单细分
            SysSftable sysSfTableFWDXF = SysSftable.dao.findFirst(Db.getSql("admin.dictionary.getSysSfTableByNo"),"FWDXF");
            if (sysSfTableFWDXF != null) {
                String selectStatement = sysSfTableFWDXF.getSelectStatement();
                String selectStatementSql = selectStatement.replace("~","\'");
                List<Record> dataList = Db.find(selectStatementSql);

                for(int i=0;i<dataList.size();i++){
                    Record record = dataList.get(i);
                    String No = record.getStr("No") ;
                    String Name = record.getStr("Name") ;
                    serviceSegmentationList.add(new Record().set("name",Name).set("value",No));
                }

            }

            //下一节点处理人
            SysSftable sysSfTableRenYuan = SysSftable.dao.findFirst(Db.getSql("admin.dictionary.getSysSfTableByNo"),"RenYuan");
            if (sysSfTableRenYuan != null) {
                String selectStatement = sysSfTableRenYuan.getSelectStatement();
                String selectStatementSql = selectStatement.replace("~","\'");
                List<Record> dataList = Db.find(selectStatementSql);

                for(int i=0;i<dataList.size();i++){
                    Record record = dataList.get(i);
                    String No = record.getStr("No") ;
                    String Name = record.getStr("Name") ;
                    acceptorList.add(new Record().set("name",Name).set("value",No));
                }

            }

            //预约时间段
            SysEnummain sysEnummainYYSJD = SysEnummain.dao.findFirst(Db.getSql("admin.dictionary.getSysEnummainByNo"),"YYSJD");
            if (sysEnummainYYSJD != null) {
                String selectStatement = sysEnummainYYSJD.getCfgVal();
                String [] dutyTimeArr = selectStatement.split("@");

                for (int i = 1; i < dutyTimeArr.length; i++) {
                    String [] dutyTimeSecArr = dutyTimeArr[i].split("=");
                    String No = dutyTimeSecArr[0] ;
                    String Name = dutyTimeSecArr[1] ;
                    dutyTimeList.add(new Record().set("name",Name).set("value",No));
                }

            }

            fieldUtil.add("serviceSystem", "服务单第三方系统", "select", serviceSystemList)
                    .add("serviceType", "服务单类型", "select", serviceTypeList)
                    .add("serviceZt", "服务单状态", "text", settingArr)
                    .add("reworkId", "返修原单号", "text", settingArr)
                    .add("factory", "厂商单标志", "number", settingArr)
                    .add("facInWarranty", "标识产品是否在保", "number", settingArr)
                    .add("smcCityId", "城市id", "number", settingArr)
                    .add("smcProvinceId", "省id", "number", settingArr)
                    .add("smcDistrictId", "区id", "number", settingArr)
                    .add("telephone", "联系电话", "mobile", settingArr)
                    .add("contactName", "联系人姓名", "text", settingArr)
                    .add("gender", "性别", "text", settingArr)
                    .add("address", "地址", "text", settingArr)
                    .add("shippingOrderNo", "发货单号", "text", settingArr)
                    .add("dutyTime", "预约时间", "datetime", settingArr)
                    .add("dutyTime1", "预约日期", "date", settingArr)
                    .add("dutyTime2", "预约时间段", "select", dutyTimeList)
                    .add("CDT", "下单时间", "datetime", settingArr)
                    .add("productId", "言而有信产品id", "number", settingArr)
                    .add("facProductId", "厂商产品id", "text", settingArr)
                    .add("productCount", "产品数量", "number", settingArr)
                    .add("orderDiscountAmount", "优惠金额", "number", settingArr)
                    .add("orderDiscountSourceData", "优惠快照", "text", settingArr)
                    .add("orderDiscountRemark", "优惠备注", "text", settingArr)
                    .add("remark", "服务单备注", "text", settingArr)
                    .add("SMC", "省市区", "text", settingArr)
                    .add("orderDiscount", "订单优惠", "floatnumber", settingArr)
                    .add("serviceSegmentation", "服务细分", "select", serviceSegmentationList)
                    .add("orderId", "第三方平台订单号", "text", settingArr)
                    .add("masterName", "工程师名称", "text", settingArr)
                    .add("masterPhone", "工程师手机号", "mobile", settingArr)
                    .add("productPictureUrls", "完工图片地址列表", "text", settingArr)
                    .add("serviceSp", "工单审批标志", "text", settingArr)
                    .add("otherRemark", "其他备注", "text", settingArr)
                    .add("servicePrice", "安装单单价", "floatnumber", settingArr)
                    .add("serviceExtraCharge", "安装附加费", "floatnumber", settingArr)
                    .add("cancelRemark", "取消备注", "text", settingArr)
                    .add("acceptor", "下一节点处理人", "select", acceptorList)
                    .add("fuselageCode", "机身码", "text", settingArr)
                    ;
        }else{
            return R.error("场景label不符合要求！");
        }
        recordList = fieldUtil.getRecordList();
        List<Record> records = adminFieldService.customFieldList(label);
        if(recordList != null && records != null){
            for(Record r : records){
                r.set("field_name", r.getStr("name"));
            }
            recordList.addAll(records);
        }
        return R.ok().put("data", recordList);
    }

    /*
     * @Description //设置工单场景
     * @Author wangkaida
     * @Date 9:55 2020/7/28
     * @Param [adminScene]
     * @return com.kakarote.crm9.utils.R
     **/
    @Before(Tx.class)
    public R workOrderSceneConfig(AdminScene adminScene) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        String[] sortArr = adminScene.getNoHideIds().split(",");
        for(int i = 0; i < sortArr.length; i++){
            Db.update(Db.getSql("admin.scene.sort"), i + 1, adminScene.getType(), userId, sortArr[i]);
        }
        if(null != adminScene.getHideIds()){
            String[] hideIdsArr = adminScene.getHideIds().split(",");
            Record number = Db.findFirst(Db.getSqlPara("admin.scene.queryIsHideSystem", Kv.by("ids", hideIdsArr)));
            if(number.getInt("number") > 0){
                return R.error("系统场景不能隐藏");
            }
            Db.update(Db.getSqlPara("admin.scene.isHide", Kv.by("ids", hideIdsArr).set("type", adminScene.getType()).set("userId", userId)));
        }
        return R.ok();
    }

    /*
     * @Description //查询工单场景设置
     * @Author wangkaida
     * @Date 14:17 2020/7/28
     * @Param [adminScene]
     * @return com.kakarote.crm9.utils.R
     **/
    public R queryWorkOrderSceneConfig(AdminScene adminScene) throws Exception {
//        Long userId = BaseUtil.getUser().getUserId();
        String userId = WebUser.getNo();
        List<Record> valueList = Db.find(Db.getSql("admin.scene.queryScene"), adminScene.getType(), userId);
        for(Record scene : valueList){
            if(StrUtil.isNotEmpty(scene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(scene.getStr("data"));
                scene.set("data", jsonObject);
            }
        }
        List<Record> hideValueList = Db.find(Db.getSql("admin.scene.queryHideScene"), adminScene.getType(), userId);
        for(Record hideScene : hideValueList){
            if(StrUtil.isNotEmpty(hideScene.getStr("data"))){
                JSONObject jsonObject = JSON.parseObject(hideScene.getStr("data"));
                hideScene.set("data", jsonObject);
            }
        }
        return R.ok().put("data", Kv.by("value", valueList).set("hide_value", hideValueList));
    }
}
