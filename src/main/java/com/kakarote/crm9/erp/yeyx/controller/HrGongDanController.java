package com.kakarote.crm9.erp.yeyx.controller;

import BP.DA.Log;
import BP.Tools.StringUtils;
import BP.Web.WebUser;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanBook;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanLog;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanRepair;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanAreaRelationRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanLogRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRepairRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.service.*;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.CreateExcel;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class HrGongDanController extends Controller {

    @Inject
    private HrGongDanService hrGongDanService;
    @Inject
    private HrGongDanAreaRelationService hrGongDanAreaRelationService;
    @Inject
    private HrGongdanRepairService hrGongdanRepairService;
    @Inject
    private HrGongdanAppointService hrGongdanAppointService;
    @Inject
    private HrGongdanLogService hrGongdanLogService;
    @Inject
    private HrGongdanZmnLogService hrGongdanZmnLogService;
    @Inject
    private TokenService tokenService;
    @Inject
    private AdminSceneService adminSceneService;

    /**
     * 保存预约单
     */
    public void saveHrGongdanAppoint(@Para("") HrGongdanRepairRequest hrGongdanRepairRequest) throws Exception  {
        log.info("=======保存预约单");
        HrGongdanBook hrGongdanBook = getModel(HrGongdanBook.class,"",true);
        String serialNum = (new DecimalFormat("00")).format(hrGongdanAppointService.getAppointByOrderNum());//流水号格式化
        hrGongdanBook.setOrderNumber("YY"+DateUtil.changeDateTOStr2(new Date())+ThreadLocalRandom.current().nextInt(10, 100)+serialNum);
        hrGongdanBook.setCreateTime(new Date());
        renderJson(R.ok().put("data",hrGongdanBook.save()));
    }
    /**
     * 预约单查询
     */
    public void queryPageListAppoint(BasePageRequest basePageRequest) throws Exception  {
        log.info("=======预约单查询");
        HrGongdanBook hrGongdanBook = getModel(HrGongdanBook.class,"",true);
        renderJson(R.ok().put("data",hrGongdanAppointService.queryPageList(basePageRequest)));
    }
    /**
     * 保存预约单处理记录
     */
    public void saveHrGongdanLogBook(@Para("") HrGongdanLogRequest hrGongdanLogRequest) {
        log.info("=======保存预约单处理记录");
        HrGongdanLog hrGongdanLog = getModel(HrGongdanLog.class,"",true);
        hrGongdanLog.setCreateTime(new Date());
        if(hrGongdanLogRequest.getDeal() == 2){
            HrGongdanBook hrGongdanBook = new HrGongdanBook();
//            hrGongdanBook.setOrderNumber(hrGongdanLog.getPreServiceNo());
            hrGongdanBook.setId(hrGongdanLogRequest.getGongDanId());
            hrGongdanBook.setDeal("2");
            hrGongdanBook.update();
        }
        renderJson(R.ok().put("data",hrGongdanLog.save()));
    }

    /**
     * 保存报修单
     */
    public void saveHrGongdanRepair(@Para("") HrGongdanRepairRequest hrGongdanRepairRequest) throws Exception  {
        log.info("=======保存报修单");
        HrGongdanRepair hrGongdanRepair = getModel(HrGongdanRepair.class,"",true);
        String serialNum = (new DecimalFormat("00")).format(hrGongdanRepairService.getRepairByOrderNum());//流水号格式化
        hrGongdanRepair.setOrderNumber("BX"+ DateUtil.changeDateTOStr2(new Date())+ThreadLocalRandom.current().nextInt(10, 100)+serialNum);
        String photos = "";
        if(getFiles().size()>0){
            photos = upload(getFiles()).stream().map(item->item.get(FileUploadUtil.ACCESS_PATH)).collect(Collectors.joining(";"));
        }
        hrGongdanRepair.setPhoto(photos);
        hrGongdanRepair.setCreateTime(new Date());
        hrGongdanRepair.setCreator(WebUser.getNo());
        //发送通知
        //获取售后客服的微信公众号openId
        List<PortEmp> portEmpList = PortEmp.dao.find(Db.getSql("admin.portEmp.queryAfterSalePortEmpList"));
        if (portEmpList.size() > 0) {
            sendMpMsg(portEmpList,hrGongdanRepairRequest);
        }

        renderJson(R.ok().put("result",hrGongdanRepair.save()).put("data",hrGongdanRepair));
    }
    /**
     * 报修单上传文件
     * @param list
     * @return
     */
    @NotAction
    public List<Map<String, String>> upload(List<UploadFile> list ) {
        log.info("开始执行文件上传方法!");
        return FileUploadUtil.upload(list,BaseConstant.UPLOAD_PATH_GDCX,"");

    }
    /**
     * 报修单查询
     */
    public void queryPageListRepair(BasePageRequest basePageRequest) throws Exception  {
        log.info("=======报修单查询");
        renderJson(R.ok().put("data",hrGongdanRepairService.queryPageList(basePageRequest)));
    }
    /**
     * 保存报修单处理记录
     */
    public void saveHrGongdanLogRepair(@Para("") HrGongdanLogRequest hrGongdanLogRequest) throws Exception  {
        log.info("=======保存预约单处理记录");
        HrGongdanLog hrGongdanLog = getModel(HrGongdanLog.class,"",true);
        hrGongdanLog.setCreateTime(new Date());
        if(hrGongdanLogRequest.getDeal() == 2){
            HrGongdanRepair hrGongdanRepair = new HrGongdanRepair();
//            hrGongdanRepair.setOrderNumber(hrGongdanLog.getPreServiceNo());
            hrGongdanRepair.setId(hrGongdanLogRequest.getGongDanId());
            hrGongdanRepair.setDeal("2");
            hrGongdanRepair.update();
        }
        renderJson(R.ok().put("data",hrGongdanLog.save()));
    }

    /**
     * @author tmr
     * 查询工单记录
     */
    public void queryPageListLog(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongdanLogService.queryPageList(basePageRequest)));
    }

    /**
     * @author tmr
     * 查询啄木鸟接口记录
     */
    public void queryPageZmnLog(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongdanZmnLogService.queryPageList(basePageRequest)));
    }

    /**
     * @author tmr
     * 查询地区师傅、地区系统的优先级关系
     */
    public void queryPageAreaRelation(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongDanAreaRelationService.queryPageList(basePageRequest)));
    }
    /**
     * @author tmr
     * 保存地区师傅、地区系统的优先级关系
     */
    public void saveAreaRelation(@Para("") HrGongdanAreaRelationRequest hrGongdanAreaRelationRequest) {
        renderJson(R.ok().put("data",hrGongDanAreaRelationService.save(hrGongdanAreaRelationRequest)));
    }

    /**
     * @author tmr
     * 分页工单查询数据
     */
    public void queryPageList(BasePageRequest basePageRequest) {
//        renderJson(R.ok().put("data",hrGongDanService.queryPageList(basePageRequest)));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /**
     * @author tmr
     * 分页工单查询数据 -- 特定服务商
     */
    public void queryPageListFws(BasePageRequest basePageRequest) throws Exception{
        basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("acceptor", WebUser.getNo()));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }
    /**
     * @author tmr
     * 分页工单查询数据 -- 啄木鸟
     */
    public void queryPageListServiceSystem(BasePageRequest basePageRequest) throws Exception{
        basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("serviceSystem", "YX"));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /**
     * 校验数据
     * @param hrGongdanRequest
     */
    public void validate(@Para("") HrGongdanRequest hrGongdanRequest){

        renderJson(R.ok().put("data",hrGongDanService.update(hrGongdanRequest)));
    }

    /**
     * 批量校验数据
     */
    public void validateMore(){
        JSONArray array = JSON.parseArray(this.getRawData());
        List<HrGongdanRequest> list= array.toJavaList(HrGongdanRequest.class);
        for(int i=0;i<list.size();i++){
            hrGongDanService.update(list.get(i));
        }
        renderJson(R.ok());
    }

    /*
     * @Description //根据订单号获取保修卡信息接口
     * @Author wangkaida
     * @Date 11:06 2020/7/14
     * @Param [hrGongdanRequest]
     * @return void
     **/
    public void getWarrantyCardByShippingOrderNo(@Para("") HrGongdanRequest hrGongdanRequest){
        //根据orderId获取出货单号
        HrGongdan hrGongdanDb = HrGongdan.dao.findFirst(Db.getSqlPara("admin.hrGongDan.getHrGongDanByOrderId", Kv.by("orderId",hrGongdanRequest.getOrderId())));
        if (hrGongdanDb != null) {
            String shippingOrderNo = hrGongdanDb.getShippingOrderNo();

            String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByShippingOrderNo";
            try {
                if (StrUtil.isNotEmpty(shippingOrderNo)) {
                    Map currentPrama = new HashMap();
                    currentPrama.put("shippingOrderNo", shippingOrderNo);
                    String result = tokenService.gatewayRequest(url, currentPrama);
                    JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
                    renderJson(R.ok().put("data", resultArray).put("code","000000"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            renderJson(R.error("根据orderId获取到的工单信息为空!").put("data",null).put("code","000048"));
            return;
        }

    }

    /*
     * @Description //根据订单号批量获取保修卡信息接口
     * @Author wangkaida
     * @Date 15:24 2020/7/14
     * @Param [hrGongdanRequest]
     * @return void
     **/
    public void getWarrantyCardByShippingOrderNoArray(@Para("") HrGongdanRequest hrGongdanRequest){
        String orderIdJson = hrGongdanRequest.getOrderId();
        JSONArray orderIdArray = JSONObject.parseObject(orderIdJson).getJSONArray("orderId");
        List<String> shippingOrderNoList = new ArrayList<String>();
        for (Object orderId: orderIdArray) {
            //根据orderId获取出货单号
            HrGongdan hrGongdanDb = HrGongdan.dao.findFirst(Db.getSqlPara("admin.hrGongDan.getHrGongDanByOrderId", Kv.by("orderId",orderId.toString())));
            if (hrGongdanDb != null) {
                shippingOrderNoList.add(hrGongdanDb.getShippingOrderNo());
            }
        }
        String[] shippingOrderNoArray = shippingOrderNoList.toArray(new String[shippingOrderNoList.size()]);

        String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByShippingOrderNoArray";
        try {
            String result = tokenService.gatewayRequestJson(url, JSONObject.toJSONString(shippingOrderNoArray));
            JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
            renderJson(R.ok().put("data", resultArray).put("code","000000"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExcel(BasePageRequest basePageRequest) {
        HttpServletResponse response = this.getResponse();
        JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) adminSceneService.filterConditionAndGetPageList(basePageRequest).get("data");
        List<Record> list = (List<Record>) jsonObject.get("list");
        try {
            // 获得输出流
            OutputStream output = response.getOutputStream();

            // 设置应用类型，以及编码
            response.setContentType("application/msexcel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "filename=" + new String(("hr_gongdan"+".xls").getBytes("gb2312"), "iso8859-1"));
            //转格式
            List row = new ArrayList<String>();


            for(int i=0;i<list.size();i++){
                List<String> col = new ArrayList<String>();
                Record record = list.get(i);
                String [] recordNames = {"serviceNo","Title","NodeName","contactName","telephone","readToday","address"};
                String [] recordNamesZw = {"工单单号","标题","工单状态","联系人","联系电话","阅数（今）","地址"};
                if(i==0){
                    List<String> colName = new ArrayList<String>();
                    for(String recordName:recordNamesZw){
                        colName.add(recordName);
                    }
                    row.add(colName);
                }
                for(String recordName:recordNames){
                    Object value = record.get(recordName) ;
                    String temp = value!=null?value.toString():"";
                    col.add(temp);
                }
                row.add(col);
            }
            CreateExcel.CreateSheet(row,output);
            renderNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * @Description //进行公众号信息推送
     * @Author wangkaida
     * @Date 16:39 2020/9/4
     * @Param [portEmpList]
     * @return void
     **/
    public void sendMpMsg(List<PortEmp> portEmpList,HrGongdanRepairRequest hrGongdanRepairRequest) {

        for (PortEmp portEmp: portEmpList) {
            String openId = portEmp.getWxOpenId();
            String acceptor = portEmp.getNo();

            if(!StringUtils.isEmpty(openId)) {
                //进行信息推送
                MpMsgSendReq mpReq = new MpMsgSendReq();
                mpReq.setTouser(openId);
                mpReq.setTemplate_id("XTmM0MzNMV-9ZKjPh4AFRwOgrGFM1nnFDGsLoS-erA0");
                mpReq.setPage("pages/index/index");

                JSONArray jsonArray=new JSONArray();
                String title = "你有新的报修单! "+ hrGongdanRepairRequest.getOrderNumber();
                jsonArray.add(new JSONObject().fluentPut("name","first").fluentPut("value",title));
                jsonArray.add(new JSONObject().fluentPut("name","keyword1").fluentPut("value","报修单"));
                jsonArray.add(new JSONObject().fluentPut("name","keyword2").fluentPut("value",hrGongdanRepairRequest.getContact()+"\n"+hrGongdanRepairRequest.getPhone()));
                jsonArray.add(new JSONObject().fluentPut("name","remark").fluentPut("value",hrGongdanRepairRequest.getRemark()));

                mpReq.setData(jsonArray.toJSONString());
                Log.DebugWriteInfo("=====================发送通知请求参数："+jsonArray.toJSONString());
                Aop.get(MpService.class).send(mpReq);
            }else {
                Log.DebugWriteInfo("进行小程序信息推送获取到的员工小程序openId为空!"+acceptor);
            }
        }
    }

}
