package com.kakarote.crm9.erp.admin.controller;

import BP.DA.DataType;
import BP.Difference.ContextHolderUtils;
import BP.WF.SendReturnObjs;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.AdminMenu;
import com.kakarote.crm9.erp.admin.service.AdminMenuService;
import com.kakarote.crm9.utils.R;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class AdminMenuController extends Controller {
    @Inject
    private AdminMenuService adminMenuService;

    public void regist(@Para("phone") String phone,@Para("password") String password){


        //开始调用注册审批流程
        Hashtable myht = new Hashtable();
        Hashtable myhtSend = new Hashtable();
        try {
            myht.put("ShouJiHaoMa", phone);
            myht.put("RDT", DataType.getCurrentDateTime());
            myht.put("Title", "测试阿萨大aaa");
            myht.put("FID", 0);
            myht.put("CDT", DataType.getCurrentDateTime() );
            myht.put("Rec", "admin");
            myht.put("Emps", "admin");
            myht.put("FK_Dept", 100);
            myht.put("FK_NY", DataType.getCurrentYearMonth());
            myht.put("MyNum", 1);
            //新建流程
            BP.WF.Dev2Interface.Port_Login("admin");
            long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",null,0,0,null,0,null,0,null,null,null);
            //发送流程
            myhtSend.put("ShouJiHaoMa", phone);
            myhtSend.put("OID", workID);
            myhtSend.put("RDT", "sss");
            myhtSend.put("Title", "测试阿萨大aaa");
            myhtSend.put("FID", 0);
            myhtSend.put("CDT", DataType.getCurrentDateTime());
            myhtSend.put("Rec", "admin");
            myhtSend.put("Emps", "admin");
            myhtSend.put("FK_Dept", 100);
            myhtSend.put("FK_NY", DataType.getCurrentYearMonth());
            myhtSend.put("MyNum", 1);
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,null);

            BP.WF.Dev2Interface.Port_SigOut();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("msg","请求成功!"));

        //开始调用注册审批流程
        /*Hashtable myht = new Hashtable();
        Hashtable myhtSend = new Hashtable();
        try {
            myht.put("TB_ShouJiHaoMa", phone);
            myht.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myht.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
            myht.put("TB_FID", 0);
            myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myht.put("TB_Rec", "admin");
            myht.put("TB_Emps", "admin");
            myht.put("TB_FK_Dept", 100);
            myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
            myht.put("TB_MyNum", 1);
            //新建流程
//            WebUser.SignInOfGener(new Emp("admin"));
//            BP.WF.Dev2Interface.Port_Login("admin");
            WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");
            long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",null,0,0,null,0,null,0,null,null,null);
            //发送流程
            myhtSend.put("TB_ShouJiHaoMa", phone);
            myhtSend.put("TB_OID", workID);
            myhtSend.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myhtSend.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
            myhtSend.put("TB_FID", 0);
            myhtSend.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myhtSend.put("TB_Rec", "admin");
            myhtSend.put("TB_Emps", "admin");
            myhtSend.put("TB_FK_Dept", 100);
            myhtSend.put("TB_FK_NY", DataType.getCurrentYearMonth());
            myhtSend.put("TB_MyNum", 1);
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,null);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        renderJson(R.ok().put("msg","请求成功!"));*/

    }
    /**
     * @author wyq
     * 展示全部菜单
     */
    public void getAllMenuList(){
        renderJson(R.ok().put("data",adminMenuService.getAllMenuList(0,BaseConstant.AUTH_DATA_RECURSION_NUM)));
    }

    /**
     * 通过角色菜单查询菜单
     * @param roleType 角色类型
     */
    public void getMenuListByType(@Para("roleType") Integer roleType){
        renderJson(adminMenuService.getMenuListByType(roleType));
    }

    /**
     * @author hmb
     * 展示全部菜单
     */
    public void getWorkMenuList(){
        Integer workMenuId = Db.queryInt("select menu_id from `aptenon_admin_menu` where parent_id = 0 and realm = 'work'");
        AdminMenu root = new AdminMenu().findById(workMenuId);
        root.put("childMenu",adminMenuService.getAllMenuList(root.getMenuId(),BaseConstant.AUTH_DATA_RECURSION_NUM));
        renderJson(R.ok().put("data",root));
    }
}
