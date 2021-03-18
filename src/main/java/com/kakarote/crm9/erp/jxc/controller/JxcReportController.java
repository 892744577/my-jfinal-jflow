package com.kakarote.crm9.erp.jxc.controller;

import BP.DA.DataType;
import BP.Port.Emp;
import BP.Tools.DateUtils;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.jxc.entity.JxcOrderReportDetail;
import com.kakarote.crm9.erp.jxc.entity.vo.JxcReportRequest;
import com.kakarote.crm9.erp.jxc.service.JxcReportService;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class JxcReportController extends Controller {

    @Inject
    private JxcReportService jxcReportService;

    //报备申请
    public void reportStart(@Para("") JxcReportRequest jxcReportRequest) throws Exception {
        //保存从表数据
        JSONArray ja =  JSONArray.parseArray(jxcReportRequest.getJsondata());
        //报备单号
        PortEmp portEmp = PortEmp.dao.findById(jxcReportRequest.getNo());
        String serialNum = (new DecimalFormat("00")).format(jxcReportService.getCountByOrderNum());//流水号格式化
        String reportNumber = "R"+ DateUtil.changeDateTOStr2(new Date())+ ThreadLocalRandom.current().nextInt(10, 100)+serialNum;
        //发送流程
        Hashtable myht = new Hashtable();
        myht.put("TB_Title", URLEncoder.encode(portEmp.getName()+"在"+ DataType.getCurrentDateTime()+"发起.", "UTF-8"));
        myht.put("TB_FID", 0);
        myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
        myht.put("TB_Rec", portEmp.getNo());
        myht.put("TB_Emps", portEmp.getNo());
        myht.put("TB_FK_Dept", 100);
        myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
        myht.put("TB_MyNum", 1);
        myht.put("reportNumber",reportNumber);
        myht.put("teamNo",portEmp.getTeamNo());
        myht.put("totalFee",ja.size()*20);
        myht.put("num",ja.size());
        WebUser.SignInOfGenerAuth(new Emp(portEmp.getNo()), portEmp.getNo());
        long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("010",myht,null,"admin",
                portEmp.getName()+"在"+ DataType.getCurrentDateTime()+"发起."
                ,0,0,null,0,null,0,null,null,null);
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("010",workID,myht,null,0,null);

        for(int i=0;i<ja.size();i++){
            JSONObject item = ja.getJSONObject(0);
            JxcOrderReportDetail jxcOrderReportDetail = new JxcOrderReportDetail();
            jxcOrderReportDetail.setMcuid(item.getString("mcuid"));
            jxcOrderReportDetail.setCreateTime(DateUtils.format(new Date(),DateUtils.YMDHMS_PATTERN));
            jxcOrderReportDetail.setReportNumber(reportNumber);
            jxcOrderReportDetail.setRefPK(String.valueOf(workID));
            jxcOrderReportDetail.save();
        }
        renderJson(R.ok().put("data",returnObjs));
    }

    //校验是否报备

    //审批通过
    public void reportPass(@Para("") JxcReportRequest jxcReportRequest) throws Exception {
        Hashtable myht = new Hashtable();
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("010",1002,myht,null,0,null);
        renderJson(R.ok().put("data",returnObjs));
    }
}
