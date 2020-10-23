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
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.wx.config.WxCpAgentIdEmun;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import com.kakarote.crm9.erp.yeyx.entity.*;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanAreaRelationRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanLogRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRepairRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.service.*;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

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
    private HrGongdanFjfService hrGongdanFjfService;
    @Inject
    private HrGongdanLogService hrGongdanLogService;
    @Inject
    private HrGongdanZmnLogService hrGongdanZmnLogService;
    @Inject
    private HrGongdanWsfLogService hrGongdanWsfLogService;
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
        //发送通知
        //获取售后客服的微信公众号openId
        List<PortEmp> portEmpList = PortEmp.dao.find(Db.getSql("admin.portEmp.queryAfterSalePortEmpList"));
        if (portEmpList.size() > 0) {
            //推送企业微信信息
            sendCpBook(portEmpList,hrGongdanBook);
        }
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
            sendMpMsg(portEmpList,hrGongdanRepair);
            //推送企业微信信息
            sendCpRepair(portEmpList,hrGongdanRepair);
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
     * 查询啄木鸟接口记录
     */
    public void queryPageWsfLog(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",hrGongdanWsfLogService.queryPageList(basePageRequest)));
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
     * 分页工单查询数据 -- 特定服务商 -- 团员
     */
    public void queryPageListFwsTy(BasePageRequest basePageRequest) throws Exception{
        basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("master", WebUser.getNo()));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }
    /**
     * @author tmr
     * 分页工单查询数据 -- 啄木鸟
     */
    public void queryPageListYXServiceSystem(BasePageRequest basePageRequest) throws Exception{
        basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("serviceSystem", "YX"));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /**
     * @author tmr
     * 分页工单查询数据 -- 啄木鸟
     */
    public void queryPageListWSFServiceSystem(BasePageRequest basePageRequest) throws Exception{
        basePageRequest.setJsonObject(basePageRequest.getJsonObject().fluentPut("serviceSystem", "WSF"));
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /**
     * 保存附加费申请
     */
    public void HrGongdanFjfSave() throws Exception {
        log.info("=======保存附加费申请");
        HrGongdanFjf hrGongdanFjf = getModel(HrGongdanFjf.class,"",true);
        String serialNum = (new DecimalFormat("00")).format(hrGongdanFjfService.getFjfByOrderNum());//流水号格式化
        hrGongdanFjf.setOrderNumber("FJF"+ DateUtil.changeDateTOStr2(new Date())+ThreadLocalRandom.current().nextInt(10, 100)+serialNum);
        hrGongdanFjf.setCreatetor(WebUser.getNo());
        hrGongdanFjf.setCreateTime(new Date());
        hrGongdanFjf.save();
        //获取售后客服的微信公众号openId,发送通知
        List<PortEmp> portEmpList = PortEmp.dao.find(Db.getSql("admin.portEmp.queryAfterSalePortEmpList"));
        if (portEmpList.size() > 0) {
            //推送企业微信信息
            sendCpFjf(portEmpList,hrGongdanFjf);
        }
        renderJson(R.ok());
    }

    /**
     * 附加费申请-查询
     */
    public void HrGongdanFjfquery(BasePageRequest basePageRequest){
        log.info("=======附加费申请查询");
        renderJson(R.ok().put("data",hrGongdanFjfService.queryPageList(basePageRequest)));
    }

    /**
     * 附加费申请-审批
     */
    public void HrGongdanFjfSp() throws Exception {
        log.info("=======附加费申请-审批");
        HrGongdanFjf hrGongdanFjf = getModel(HrGongdanFjf.class,"",true);
        HrGongdanFjf hrGongdanFjfUpdate = new HrGongdanFjf();
        hrGongdanFjfUpdate.setId(hrGongdanFjf.getId());
        hrGongdanFjfUpdate.setSp(hrGongdanFjf.getSp());
        hrGongdanFjfUpdate.setSpTime(new Date());
        hrGongdanFjfUpdate.setSpRemark(hrGongdanFjf.getSpRemark());
        hrGongdanFjfUpdate.setSptor(WebUser.getNo());
        hrGongdanFjfUpdate.update();
        if(1==hrGongdanFjf.getSp()){
            HrGongdanFjf hrGongdanFjfQuery = HrGongdanFjf.dao.findById(hrGongdanFjf.getId());
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(hrGongdanFjfQuery.getWorkId());
            hrGongdan.setServiceExtraCharge(hrGongdanFjfQuery.getFjf());
            hrGongdan.setServiceExtraChargeBz(hrGongdanFjf.getSpRemark());
            hrGongdan.update();
        }
        renderJson(R.ok());
    }

    /**
     * 审批
     * @param hrGongdanRequest
     */
    public void validate(@Para("") HrGongdanRequest hrGongdanRequest){
        renderJson(R.ok().put("data",hrGongDanService.update(hrGongdanRequest)));
    }

    /**
     * 批量审批
     */
    public void validateMore(){
        JSONArray array = JSON.parseArray(this.getRawData());
        List<HrGongdanRequest> list= array.toJavaList(HrGongdanRequest.class);
        for(int i=0;i<list.size();i++){
            hrGongDanService.update(list.get(i));
        }
        renderJson(R.ok());
    }

    /**
     * @Description 根据出货单号获取保修卡信息接口
     * @Author wangkaida
     * @Date 11:06 2020/7/14
     * @Param [hrGongdanRequest]
     * @return void
     **/
    public void getWarrantyCardByShippingOrderNo(@Para("") HrGongdanRequest hrGongdanRequest){
        String shippingOrderNo = hrGongdanRequest.getShippingOrderNo();
        String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByShippingOrderNo";
        try {
            if (StrUtil.isNotEmpty(shippingOrderNo)) {
                Map currentPrama = new HashMap();
                currentPrama.put("shippingOrderNo", shippingOrderNo);
                String result = tokenService.gatewayRequest(url, currentPrama);
                JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
                renderJson(R.ok().put("data", resultArray).put("code","000000"));
            }else{
                renderJson(R.error("出货单号为空").put("code","000001"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据机身码获取数据
     * @param hrGongdanRequest
     */
    public void getWarrantyCardByMcuId(@Para("") HrGongdanRequest hrGongdanRequest){
        String fuselageCode = hrGongdanRequest.getFuselageCode();
        String url = "http://app.aptenon.com:80/api/v1/tenon-social-adapter/tenon/weixin/warrantyCards/getWarrantyCardByMcuId";
        try {
            if (StrUtil.isNotEmpty(fuselageCode)) {
                Map currentPrama = new HashMap();
                currentPrama.put("mcuId", fuselageCode);
                String result = tokenService.gatewayRequest(url, currentPrama);
                JSONArray resultArray = JSONObject.parseObject(result).getJSONArray("data");
                renderJson(R.ok().put("data", resultArray).put("code","000000"));
            }else{
                renderJson(R.error("机身码为空").put("code","000001"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
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

    /**
     * @Description 进行公众号信息推送
     * @Author wangkaida
     * @Date 16:39 2020/9/4
     * @Param [portEmpList]
     * @return void
     **/
    public void sendMpMsg(List<PortEmp> portEmpList,HrGongdanRepair hrGongdanRepairRequest) {

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

    /**
     * @Description //进行企业微信信息推送1
     * @Author wangkaida
     * @Date 10:11 2020/9/8
     * @Param [hrGongdanRepairRequest]
     * @return void
     **/
    private void sendCpBook(List<PortEmp> portEmpList,HrGongdanBook hrGongdanBookRequest) {
        sendCpMsg(portEmpList,null,hrGongdanBookRequest,null);
    }

    /**
     * @Description //进行企业微信信息推送2
     * @Author wangkaida
     * @Date 10:11 2020/9/8
     * @Param [hrGongdanRepairRequest]
     * @return void
     **/
    private void sendCpRepair(List<PortEmp> portEmpList,HrGongdanRepair hrGongdanRepairRequest) {
        sendCpMsg(portEmpList,hrGongdanRepairRequest,null,null);
    }

    /**
     * @Description //进行企业微信信息推送3
     * @Author wangkaida
     * @Date 10:11 2020/9/8
     * @Param [hrGongdanRepairRequest]
     * @return void
     **/
    private void sendCpFjf(List<PortEmp> portEmpList,HrGongdanFjf hrGongdanFjf) {
        sendCpMsg(portEmpList,null,null,hrGongdanFjf);
    }

    /**
     * @Description //进行企业微信信息推送
     * @Author wangkaida
     * @Date 10:11 2020/9/8
     * @Param [hrGongdanRepairRequest]
     * @return void
     **/
    private void sendCpMsg(List<PortEmp> portEmpList,HrGongdanRepair hrGongdanRepairRequest,HrGongdanBook hrGongdanBookRequest,HrGongdanFjf hrGongdanFjf) {
        WxCpMessageReq wxCpMessageReq = new WxCpMessageReq();
        wxCpMessageReq.setAgentId(WxCpAgentIdEmun.agent2.getCode());
        String toUser = "";
        for (PortEmp portEmp: portEmpList) {
            toUser = toUser + portEmp.getTel() + "|";
        }
        if (StringUtils.isNotBlank(toUser)) {
            toUser = toUser.substring(0, toUser.lastIndexOf("|"));
        }
        wxCpMessageReq.setUser(toUser);

        String redirectUrl = "";
        String title = "";
        String sendContent = "";
        if (hrGongdanRepairRequest != null) {
            redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WxCpAgentIdEmun.corpId+"&redirect_uri=http%3a%2f%2fapp.aptenon.com%2fcrm%2fcrmAdmin%2findex.html%3ftype%3drepaireQuery%23%2fwx%2fwxWorkAuthPage&response_type=code&scope=snsapi_base&state=#wechat_redirect";
            title = "你有新的报修单! <a href=\""+redirectUrl+"\">"+hrGongdanRepairRequest.getOrderNumber()+"</a>";
            sendContent = title + "\n联系人:"+hrGongdanRepairRequest.getContact() + "\n联系电话:"+hrGongdanRepairRequest.getPhone() + "\n地址:"+hrGongdanRepairRequest.getAddress() + "\n故障描述:"+hrGongdanRepairRequest.getRemark();
        }
        if (hrGongdanBookRequest != null) {
            redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WxCpAgentIdEmun.corpId+"&redirect_uri=http%3A%2F%2Fapp.aptenon.com%2Fcrm%2FcrmAdmin%2Findex.html%3Ftype%3DappointQuery%23%2Fwx%2FwxWorkAuthPage&response_type=code&scope=snsapi_base&state=#wechat_redirect";
            title = "你有新的预约单! <a href=\""+redirectUrl+"\">"+hrGongdanBookRequest.getOrderNumber()+"</a>";
            sendContent = title + "\n联系人:"+hrGongdanBookRequest.getContact() + "\n联系电话:"+hrGongdanBookRequest.getPhone() + "\n地址:"+hrGongdanBookRequest.getAddress() + "\n预约描述:"+hrGongdanBookRequest.getRemark();
        }
        if (hrGongdanFjf != null) {
            redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WxCpAgentIdEmun.corpId+"&redirect_uri=http%3A%2F%2Fapp.aptenon.com%2Fcrm%2FcrmAdmin%2Findex.html%3Ftype%3DappointQuery%23%2Fwx%2FwxWorkAuthPage&response_type=code&scope=snsapi_base&state=#wechat_redirect";
            title = "你有新的附加费反馈单! <a href=\""+redirectUrl+"\">"+hrGongdanFjf.getOrderNumber()+"</a>";
            sendContent = title + "\n工单单号:"+hrGongdanFjf.getServiceNo() + "\n附加费用(元):"+hrGongdanFjf.getFjf() + "\n申请修改原因:"+hrGongdanFjf.getRemark();
        }


        wxCpMessageReq.setContent(sendContent);
        Aop.get(CpService.class).sendTextMsg(wxCpMessageReq);
    }

}