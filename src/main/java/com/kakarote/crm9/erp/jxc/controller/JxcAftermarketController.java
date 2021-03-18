package com.kakarote.crm9.erp.jxc.controller;

import BP.DA.DataType;
import BP.Port.Emp;
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
public class JxcAftermarketController extends Controller {

    @Inject
    private JxcReportService jxcReportService;

    //报备申请
    public void reportStart(@Para("") JxcReportRequest jxcReportRequest) throws Exception {

        //报备单号
        PortEmp portEmp = PortEmp.dao.findById(jxcReportRequest.getNo());
        String serialNum = (new DecimalFormat("00")).format(jxcReportService.getCountByOrderNum());//流水号格式化
        String reportNumber = "R"+ DateUtil.changeDateTOStr2(new Date())+ ThreadLocalRandom.current().nextInt(10, 100)+serialNum;
        //从表数据
        /*DataSet detailList = new DataSet();
        detailList.setName("jxc_order_report_detail");
        List<DataTable> list = new ArrayList<DataTable>();
        JSONArray ja =  JSONArray.parseArray(jxcReportRequest.getJsondata());
        for(int i=0;i<ja.size();i++){
            JSONObject item = ja.getJSONObject(0);
        }
        detailList.setTables(list);*/
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
        myht.put("TB_reportNumber",reportNumber);
        myht.put("TB_teamNo",portEmp.getTeamNo());
        myht.put("TB_num",jxcReportRequest.getNum());
        WebUser.SignInOfGenerAuth(new Emp(portEmp.getNo()), portEmp.getNo());
        long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("010",myht,null,"admin",
                portEmp.getName()+"在"+ DataType.getCurrentDateTime()+"发起."
                ,0,0,null,0,null,0,null,null,null);
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("010",workID,myht,null,0,null);
        //发送成功记录报备明细
        JSONArray ja =  JSONArray.parseArray(jxcReportRequest.getJsondata());
        for(int i=0;i<ja.size();i++){
            JSONObject item = ja.getJSONObject(0);
            JxcOrderReportDetail jxcOrderReportDetail = new JxcOrderReportDetail();
            jxcOrderReportDetail.setMcuid(item.getString("mcuid"));
            jxcOrderReportDetail.setReportId(item.getString("mcuid"));
            jxcOrderReportDetail.setReportId(item.getString("mcuid"));

            jxcOrderReportDetail.save();

        }
        renderJson(R.ok().put("data",returnObjs));
    }

    //校验

    //审批通过
}
