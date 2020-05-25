package com.kakarote.crm9.common.service;


import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.ClassFactory;
import BP.En.Entity;
import BP.Port.Emp;
import BP.Web.WebUser;

/**
 * 程序启动的时候，去更新所有流程定义的流程事件实体参数
 */
public class JflowRefreshService {

    public void initFlow(){
        try {
            WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");

            //获取全部流程定义记录
            String sql = "select * from wf_flow";
            DataTable dt = DBAccess.RunSQLReturnTable(sql);

            for(int i = 0 ; i < dt.Rows.size(); i++){
                Entity en = ClassFactory.GetEn("BP.WF.Template.FlowExt");
                en.setPKVal(dt.Rows.get(i).getValue("No"));
                en.RetrieveFromDBSources();
                en.Update();
            }

            WebUser.Exit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
