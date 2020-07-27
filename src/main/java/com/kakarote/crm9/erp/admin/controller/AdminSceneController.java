package com.kakarote.crm9.erp.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminScene;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;

/**
 * @author wyq
 */
public class AdminSceneController extends Controller {
    @Inject
    private AdminSceneService adminSceneService;

    /**
     * @author wyq
     * @param label 模块类型
     * 查询场景字段
     */
    @NotNullValidate(value = "label",message = "label不能为空")
    public void queryField(@Para("label")Integer label){
        renderJson(adminSceneService.queryField(label));
    }

    /**
     * @author wyq
     * 增加场景
     */
    @NotNullValidate(value = "type",message = "type不能为空")
    @NotNullValidate(value = "name",message = "场景名称不能为空")
    @NotNullValidate(value = "data",message = "data不能为空")
    public void addScene(@Para("") AdminScene adminScene) throws Exception {
        renderJson(adminSceneService.addScene(adminScene));
    }

    /**
     * @author wyq
     * 更新场景
     */
    @NotNullValidate(value = "sceneId",message = "场景id不能为空")
    public void updateScene(@Para("")AdminScene adminScene) throws Exception {
        renderJson(adminSceneService.updateScene(adminScene));
    }

    /**
     * @author wyq
     * @param sceneId 场景id
     * 设置默认场景
     */
    @NotNullValidate(value = "sceneId",message = "场景id不能为空")
    public void setDefaultScene(@Para("sceneId")Integer sceneId) throws Exception {
        renderJson(adminSceneService.setDefaultScene(sceneId));
    }

    /**
     * @author wyq
     * 删除场景
     */
    @NotNullValidate(value = "sceneId",message = "场景id不能为空")
    public void deleteScene(@Para("")AdminScene adminScene){
        renderJson(adminSceneService.deleteScene(adminScene));
    }

    /**
     * @author wyq
     * 查询场景
     */
    @NotNullValidate(value = "type",message = "type不能为空")
    public void queryScene(@Para("type") Integer type) throws Exception {
        renderJson(adminSceneService.queryScene(type));
    }

    /**
     * @author wyq
     * 查询场景设置
     */
    @NotNullValidate(value = "type",message = "type不能为空")
    public void querySceneConfig(@Para("") AdminScene adminScene){
        renderJson(adminSceneService.querySceneConfig(adminScene));
    }

    /**
     * @author wyq
     * 设置场景
     */
    @NotNullValidate(value = "type",message = "type不能为空")
    @NotNullValidate(value = "noHideIds",message = "显示场景不能为空")
    public void sceneConfig(@Para("") AdminScene adminScene){
        renderJson(adminSceneService.sceneConfig(adminScene));
    }

    /**
     * @author wyq
     * Crm列表页查询
     */
    public void queryPageList(BasePageRequest basePageRequest){
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /*
     * @Description //查询工单场景
     * @Author wangkaida
     * @Date 15:22 2020/6/30
     * @Param [type]
     * @return void
     **/
    @NotNullValidate(value = "type",message = "type不能为空")
    public void queryWorkOrderScene(@Para("type") Integer type) throws Exception {
        renderJson(adminSceneService.queryWorkOrderScene(type));
    }

    /*
     * @Description //查询工单场景字段
     * @Author wangkaida
     * @Date 9:38 2020/7/27
     * @Param [label]
     * @return void
     **/
    @NotNullValidate(value = "label",message = "label不能为空")
    public void queryWorkOrderField(@Para("label")Integer label){
        renderJson(adminSceneService.queryWorkOrderField(label));
    }
}
