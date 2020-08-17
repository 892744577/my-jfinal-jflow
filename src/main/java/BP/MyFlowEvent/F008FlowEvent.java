package BP.MyFlowEvent;

import BP.WF.FlowEventBase;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.wx.util.MaUtil;
import com.kakarote.crm9.erp.wx.vo.MaReq;
import com.kakarote.crm9.utils.R;

import java.util.Date;

public class F008FlowEvent extends FlowEventBase {
    @Override
    public String getFlowMark() {
        return "aptenon_zcsh";
    }

    public String FlowOverAfter() {

       String type = (String) this.getSysPara().get("LeiBie");  //1.代理商 2.非代理商

        if ("1".equals(type)) {

            Regist regist = new Regist();
            regist.setPhone((String) this.getSysPara().get("ShouJiHaoMa"));
            regist.setAppOpenId((String) this.getSysPara().get("appOpenId"));
            regist.setName((String) this.getSysPara().get("XingMing"));
            Aop.get(PortEmpService.class).savePortEmp(regist);

            //进行小程序信息推送
            MaReq maReq = new MaReq();
            maReq.setTouser((String) this.getSysPara().get("appOpenId"));
            maReq.setTemplate_id("yYVbL8yALguI-hXURqaNkCBosW2T5WEmyTVO0Bu04Kk");
            maReq.setPage("pages/index/index");
            maReq.setMiniprogram_state("formal");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("character_string1",new JSONObject().fluentPut("value",(String) this.getSysPara().get("ShouJiHaoMa")));
            jsonObject.put("name2",new JSONObject().fluentPut("value",(String) this.getSysPara().get("XingMing")));
            jsonObject.put("date3",new JSONObject().fluentPut("value", DateUtil.formatDateTime(new Date())));

            maReq.setData(jsonObject.toJSONString());

            String sb= MaUtil.ResponseMsg(maReq.getTouser(),maReq.getTemplate_id(),maReq.getPage(),maReq.getData(),maReq.getMiniprogram_state());

            try {
                boolean IsSend = MaUtil.PostMaMsg(sb);

                if (IsSend == true){
                    System.out.println("推送小程序信息成功!"+maReq.getTemplate_id());
                }else {
                    System.out.println("推送小程序信息失败!"+maReq.getTemplate_id());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if ("2".equals(type)) {

            Regist regist = new Regist();
            regist.setPhone((String) this.getSysPara().get("ShouJiHaoMa"));
            regist.setAppOpenId((String) this.getSysPara().get("appOpenId"));
            regist.setName((String) this.getSysPara().get("XingMing"));
            regist.setParentNo((String) this.getSysPara().get("ParentNo"));
            Aop.get(PortEmpService.class).savePortEmpStaff(regist);

            //进行小程序信息推送
            MaReq maReq = new MaReq();
            maReq.setTouser((String) this.getSysPara().get("appOpenId"));
            maReq.setTemplate_id("yYVbL8yALguI-hXURqaNkCBosW2T5WEmyTVO0Bu04Kk");
            maReq.setPage("pages/index/index");
            maReq.setMiniprogram_state("formal");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("character_string1",new JSONObject().fluentPut("value",(String) this.getSysPara().get("ShouJiHaoMa")));
            jsonObject.put("name2",new JSONObject().fluentPut("value",(String) this.getSysPara().get("XingMing")));
            jsonObject.put("date3",new JSONObject().fluentPut("value", DateUtil.formatDateTime(new Date())));

            maReq.setData(jsonObject.toJSONString());

            String sb= MaUtil.ResponseMsg(maReq.getTouser(),maReq.getTemplate_id(),maReq.getPage(),maReq.getData(),maReq.getMiniprogram_state());

            try {
                boolean IsSend = MaUtil.PostMaMsg(sb);

                if (IsSend == true){
                    System.out.println("推送小程序信息成功!"+maReq.getTemplate_id());
                }else {
                    System.out.println("推送小程序信息失败!"+maReq.getTemplate_id());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return "F008FlowEvent注册审核流程结束";
    }

}
