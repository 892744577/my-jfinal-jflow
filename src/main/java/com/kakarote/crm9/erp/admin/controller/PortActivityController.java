package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.admin.entity.vo.*;
import com.kakarote.crm9.erp.admin.service.PortActivityService;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.utils.CreateExcel;
import com.kakarote.crm9.utils.QrCodeUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.*;


/*
 * @Description //活动分享管理控制类
 * @Author wangkaida
 * @Date 15:11 2020/5/19
 * @Param
 * @return
 **/
@Slf4j
public class PortActivityController extends Controller {

    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private PortActivityService portActivityService;

    /*
     * @Description //获取活动列表接口
     * @Author wangkaida
     * @Date 15:54 2020/5/19
     * @Param []
     * @return void
     **/
    public void queryActivityList(){
        renderJson(portActivityService.queryActivityList());
    }

    /*
     * @Description //根据活动Id生成海报接口
     * @Author wangkaida
     * @Date 18:03 2020/5/19
     * @Param [portActivityReq]
     * @return void
     **/
    public void playBillCreate(@Para("") PortActivityReq portActivityReq){

        if(StrUtil.isEmpty(portActivityReq.getSourceOpenId())){
            renderJson(R.error("请输入海报发起人小程序openId!").put("data",null).put("code","000013"));
            return;
        }

        if(StrUtil.isEmpty(portActivityReq.getLeadingOpenId())){
            renderJson(R.error("请输入负责人小程序openId!").put("data",null).put("code","000014"));
            return;
        }

        if(portActivityReq.getAcId() == null){
            renderJson(R.error("请输入海报活动Id!").put("data",null).put("code","000015"));
            return;
        }

        PortActivityPlaybill portActivityPlaybillDb = PortActivityPlaybill.dao.findFirst("SELECT * FROM port_activity_playbill WHERE pb_source_openid = ? and pb_ac_id = ? LIMIT 0,1",portActivityReq.getSourceOpenId(),portActivityReq.getAcId());

        if (portActivityPlaybillDb != null) {
            if (StrUtil.isNotEmpty(portActivityPlaybillDb.getPbPlaybill())) {
                //已经生成海报
                renderJson(R.error("发起人已经生成该活动海报，请勿重复生成!").put("data",null).put("id",portActivityPlaybillDb.getId()).put("pbPath","http://app.aptenon.com/crm/PlayBill/"+portActivityPlaybillDb.getPbPlaybill()).put("code","000015"));
                return;
            }else {
                //未生成海报,重新生成海报
                //根据海报id生成小程序码
                Long pbId = portActivityPlaybillDb.getId();
//                byte[] pbWxCode = QrCodeUtil.playBillQrCodeCreate(pbId);
                BufferedImage pbWxCodeBI = QrCodeUtil.playBillWxQrCodeCreate(pbId,wxMpConfiguration);
                byte[] pbWxCode = QrCodeUtil.imageToBytes(pbWxCodeBI,"png");

                //更新小程序码到活动海报表
                //合成海报
                String pbName = QrCodeUtil.syntheticPlayBill(pbWxCode,pbId,portActivityReq.getAcId());
                //更新海报名称到活动海报表
                //将二维码以图片形式保存
                String qrName = QrCodeUtil.syntheticPlayBillQrcode(pbWxCode,pbId);

                PortActivityPlaybill portActivityPlaybill = new PortActivityPlaybill();
                portActivityPlaybill.setPbQrcode(qrName);
                portActivityPlaybill.setPbPlaybill(pbName);
                portActivityPlaybill.setId(pbId);
                portActivityPlaybill.update();

            }

        }else {
            PortActivityPlaybill portActivityPlaybill = new PortActivityPlaybill();
            portActivityPlaybill.setPbSourceOpenid(portActivityReq.getSourceOpenId());
            portActivityPlaybill.setPbLeadingOpenid(portActivityReq.getLeadingOpenId());
            portActivityPlaybill.setPbAcId(portActivityReq.getAcId());
            Boolean flag = portActivityPlaybill.save();

            //根据海报id生成小程序码
            Long pbId = portActivityPlaybill.getLong("id");
//            byte[] pbWxCode = QrCodeUtil.playBillQrCodeCreate(pbId);
            BufferedImage pbWxCodeBI = QrCodeUtil.playBillWxQrCodeCreate(pbId,wxMpConfiguration);
            byte[] pbWxCode = QrCodeUtil.imageToBytes(pbWxCodeBI,"png");
            //更新小程序码到活动海报表
            //合成海报
            String pbName = QrCodeUtil.syntheticPlayBill(pbWxCode,pbId,portActivityReq.getAcId());
            //更新海报名称到活动海报表
            //将二维码以图片形式保存
            String qrName = QrCodeUtil.syntheticPlayBillQrcode(pbWxCode,pbId);

            PortActivityPlaybill portActivityPlaybillUpdate = new PortActivityPlaybill();
            portActivityPlaybillUpdate.setPbQrcode(qrName);
            portActivityPlaybillUpdate.setPbPlaybill(pbName);
            portActivityPlaybillUpdate.setId(pbId);
            portActivityPlaybillUpdate.update();

            renderJson(R.ok().put("msg","保存成功!").put("id",pbId).put("pbPath","http://app.aptenon.com/crm/PlayBill/"+pbName).put("code","000000"));
        }

    }

    /*
     * @Description //生成二维码图片
     * @Author wangkaida
     * @Date 15:37 2020/5/20
     * @Param [pbWxCode]
     * @return java.lang.String
     **/
//    private String syntheticPlayBillQrcode(byte[] pbWxCode, Long pbId) {
//
//        String outPicName = "";
//        try {
//            PictureRequestDto pictureRequestDto = new PictureRequestDto();
//            pictureRequestDto.setBt(pbWxCode);
//            String picPath = BaseConstant.UPLOAD_PATH + "/PlayBill/";
//            outPicName = "HB_QR"+pbId+".jpg";
//            pictureRequestDto.setOutPicPath(picPath+outPicName);
//
//            //将b作为输入流，将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
//            ByteArrayInputStream in = new ByteArrayInputStream(pictureRequestDto.getBt());
//            BufferedImage code = ImageIO.read(in);
//            ImageIO.write(code ,"png", new File(pictureRequestDto.getOutPicPath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return outPicName;
//    }

    /*
     * @Description //合成海报
     * @Author wangkaida
     * @Date 15:37 2020/5/20
     * @Param [pbWxCode]
     * @return java.lang.String
     **/
//    private String syntheticPlayBill(byte[] pbWxCode, Long pbId, Integer acId) {
//
//        String outPicName = "";
//        try {
//            //获取背景图片名称
//            PortActivity portActivityDb = PortActivity.dao.findFirst("SELECT * FROM port_activity WHERE id = ? LIMIT 0,1",acId);
//
//            PictureRequestDto pictureRequestDto = new PictureRequestDto();
//            pictureRequestDto.setBt(pbWxCode);
//
//            String picPath = BaseConstant.UPLOAD_PATH + "/PlayBill/";
//            pictureRequestDto.setBackPicPath(picPath + portActivityDb.getAcPlaybillImg());
//            outPicName = "HB_"+pbId+".jpg";
//            pictureRequestDto.setOutPicPath(picPath+outPicName);
//
//            //将b作为输入流，将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
//            ByteArrayInputStream in = new ByteArrayInputStream(pictureRequestDto.getBt());
//            BufferedImage code = ImageIO.read(in);
//
//            BufferedImage big = PictureUtil.combineCodeAndPicToFile(pictureRequestDto.getBackPicPath(), code);
//            String suffix = pictureRequestDto.getBackPicPath().substring(pictureRequestDto.getBackPicPath().lastIndexOf(".") + 1);
//            ImageIO.write(big,suffix , new File(pictureRequestDto.getOutPicPath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return outPicName;
//    }

    /*
     * @Description //根据海报id生成小程序码
     * @Author wangkaida
     * @Date 14:12 2020/5/20
     * @Param [pbId]
     * @return byte[]
     **/
//    private byte[] playBillQrCodeCreate(Long pbId) {
//
//        String appid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();
//        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
//
//        byte[] wxCode = null;
//        try {
//            String scene = "pid="+pbId;
//            String page = "activityPages/agentActivity/agentActivityDetail";
//            wxCode = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, 280, true, null, false);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
//
//        return wxCode;
//    }

    /*
     * @Description //活动分享接口
     * @Author wangkaida
     * @Date 20:39 2020/5/19
     * @Param [portActivityReq]
     * @return void
     **/
    public void activityShare(@Para("") PortActivityReq portActivityReq){

        if(StrUtil.isEmpty(portActivityReq.getShareOpenId())){
            renderJson(R.error("请输入分享人公众号openId!").put("data",null).put("code","000018"));
            return;
        }

        if(portActivityReq.getPbId() == null){
            renderJson(R.error("请输入海报Id!").put("data",null).put("code","000020"));
            return;
        }
        if(portActivityReq.getToShareOpenId() == null){
            renderJson(R.error("请输入被分享人公众号Id!").put("data",null).put("code","000021"));
            return;
        }

//        if(portActivityReq.getAssistId() == null){
//            renderJson(R.error("请输入需被助力记录Id!").put("data",null).put("code","000027"));
//            return;
//        }

        PortActivityShare portActivityShareDb = PortActivityShare.dao.findFirst("SELECT * FROM port_activity_share WHERE sr_pb_id = ? and sr_share_openid = ? and sr_to_share_openid = ? LIMIT 0,1",portActivityReq.getPbId(),portActivityReq.getShareOpenId(),portActivityReq.getToShareOpenId());

        if (portActivityShareDb != null) {
            renderJson(R.error("该活动已经分享!").put("data",portActivityShareDb).put("code","000000"));
            return;
        }

        PortActivityShare portActivityShare = new PortActivityShare();
        portActivityShare.setSrShareOpenid(portActivityReq.getShareOpenId());
        portActivityShare.setSrToShareOpenid(portActivityReq.getToShareOpenId());
        portActivityShare.setSrPbId(portActivityReq.getPbId());
        portActivityShare.setSrAsId(portActivityReq.getAssistId());
        portActivityShare.setCreateTime(new Date());

        if (!portActivityReq.getShareOpenId().equals(portActivityReq.getToShareOpenId())) {

            List<PortActivityShare> portActivityShareList = PortActivityShare.dao.find(Db.getSql("admin.portActivityShare.secondStep"),portActivityReq.getToShareOpenId());

            if (portActivityShareList.size() == 0) {
                Record record = Db.findFirst(Db.getSql("admin.portActivityShare.thirdStep"),portActivityReq.getToShareOpenId(),portActivityReq.getToShareOpenId());
                if (record != null) {
                    //查到数据则无效
                    portActivityShare.setValidFlag("0");
                }else {
                    //查不到数据则有效
                    portActivityShare.setValidFlag("1");
                }
            }else {
                //有数据则无效
                portActivityShare.setValidFlag("0");
            }

        }else {
            portActivityShare.setValidFlag("0");
        }

        Boolean flag = portActivityShare.save();
        renderJson(R.ok().put("msg","保存成功!").put("data",portActivityShare).put("code","000000"));

    }

    /*
     * @Description //根据海报Id获取活动信息接口
     * @Author wangkaida
     * @Date 9:28 2020/5/20
     * @Param [portActivityReq]
     * @return void
     **/
    public void getActByPbId(@Para("") PortActivityReq portActivityReq){

        if(portActivityReq.getPbId() == null){
            renderJson(R.error("请输入海报Id!").put("data",null).put("code","000020"));
            return;
        }

        Record record = Db.findFirst("SELECT a.*,b.pb_source_openid FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id where b.id = ? LIMIT 0,1",portActivityReq.getPbId());

        if (record != null) {
            renderJson(R.ok().put("data", record).put("code","000000"));

        }else {
            renderJson(R.error("获取到的活动信息为空!").put("data",null).put("code","000021"));
            return;
        }

    }

    /*
     * @Description //根据分享Id获取活动信息接口
     * @Author wangkaida
     * @Date 14:28 2020/5/20
     * @Param [portActivityReq]
     * @return void
     **/
    public void getActByShareId(@Para("") PortActivityReq portActivityReq){

        if(portActivityReq.getShareId() == null){
            renderJson(R.error("请输入分享Id!").put("data",null).put("code","000022"));
            return;
        }

        Record record = Db.findFirst("SELECT a.*,b.id pbId,c.sr_share_openid,c.sr_to_share_openid,c.sr_as_id FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id left join port_activity_share c on b.id = c.sr_pb_id where c.id = ? LIMIT 0,1",portActivityReq.getShareId());

        if (record != null) {
            renderJson(R.ok().put("data", record).put("code","000000"));

        }else {
            renderJson(R.error("获取到的活动信息为空!").put("data",null).put("code","000021"));
            return;
        }

    }

    /*
     * @Description //根据发起人openId,活动Id获取活动信息接口
     * @Author wangkaida
     * @Date 16:23 2020/5/22
     * @Param [portActivityReq]
     * @return void
     **/
    public void queryPortActivity(@Para("") PortActivityReq portActivityReq){

        if(portActivityReq.getAcId() == null){
            renderJson(R.error("请输入活动Id!").put("data",null).put("code","000026"));
            return;
        }

        if(StrUtil.isEmpty(portActivityReq.getSourceOpenId())){
            renderJson(R.error("请输入海报发起人小程序openId!").put("data",null).put("code","000027"));
            return;
        }

        PortActivityPlaybill portActivityPlaybillDb = PortActivityPlaybill.dao.findFirst("SELECT * FROM port_activity_playbill WHERE pb_source_openid = ? and pb_ac_id = ? LIMIT 0,1",portActivityReq.getSourceOpenId(),portActivityReq.getAcId());

        if (portActivityPlaybillDb == null) {
            //海报为空就返回活动信息
            PortActivity portActivityDb = PortActivity.dao.findFirst("SELECT * FROM port_activity WHERE id = ? LIMIT 0,1",portActivityReq.getAcId());
            renderJson(R.ok().put("data", portActivityDb).put("code","000000"));

        }else {

            Record record = Db.findFirst("SELECT a.*,b.id pbId FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id where b.pb_source_openid = ? and b.pb_ac_id = ? LIMIT 0,1",portActivityReq.getSourceOpenId(),portActivityReq.getAcId());

            if (record != null) {
                renderJson(R.ok().put("data", record).put("code","000000"));

            }else {
                renderJson(R.error("获取到的活动信息为空!").put("data",null).put("code","000021"));
                return;
            }

        }

    }

    /*
     * @Description //保存集赞接口
     * @Author wangkaida
     * @Date 10:52 2020/5/23
     * @Param [portActivityReq]
     * @return void
     **/
    public void savePortActivityHelper(@Para("") PortActivityReq portActivityReq){

//        if(StrUtil.isEmpty(portActivityReq.getHelperAppOpenId())){
//            renderJson(R.error("请输入助力人小程序openId!").put("data",null).put("code","000028"));
//            return;
//        }

        if(StrUtil.isEmpty(portActivityReq.getHelperOpenId())){
            renderJson(R.error("请输入助力人公众号openId!").put("data",null).put("code","000029"));
            return;
        }

        if(portActivityReq.getAssistId() == null){
            renderJson(R.error("请输入需被助力记录Id!").put("data",null).put("code","000027"));
            return;
        }

        PortActivityHelper portActivityHelperDb = PortActivityHelper.dao.findFirst("SELECT * FROM port_activity_helper WHERE assistId = ? and helperOpenId = ? LIMIT 0,1",portActivityReq.getAssistId(),portActivityReq.getHelperOpenId());

        if (portActivityHelperDb != null) {
            renderJson(R.error("已经集赞,请勿重复提交!").put("data",null).put("code","000030"));
            return;
        }
        
        PortActivityHelper portActivityHelper = new PortActivityHelper();
        portActivityHelper.setAssistId(Integer.parseInt(portActivityReq.getAssistId()));
        portActivityHelper.setHelperAppOpenId(portActivityReq.getHelperAppOpenId());
        portActivityHelper.setHelperOpenId(portActivityReq.getHelperOpenId());
        portActivityHelper.setHelperInfo(portActivityReq.getHelperInfo());
        Boolean flag = portActivityHelper.save();
        renderJson(R.ok().put("msg","保存成功!").put("data",portActivityHelper).put("code","000000"));

    }

    /*
     * @Description //保存助力接口
     * @Author wangkaida
     * @Date 14:06 2020/6/17
     * @Param [portActivityReq]
     * @return void
     **/
    public void savePortActivityAssist(@Para("") PortActivityReq portActivityReq){

        if(StrUtil.isEmpty(portActivityReq.getAsOpenId())){
            renderJson(R.error("请输入发起助力者微信openId!").put("data",null).put("code","000035"));
            return;
        }

        if(StrUtil.isEmpty(portActivityReq.getAsProductId())){
            renderJson(R.error("请输入商品Id!").put("data",null).put("code","000037"));
            return;
        }

        if(portActivityReq.getAcId() == null){
            renderJson(R.error("请输入活动Id!").put("data",null).put("code","000036"));
            return;
        }

        PortActivityAssist portActivityAssistDb = PortActivityAssist.dao.findFirst("SELECT * FROM port_activity_assist WHERE as_ac_id = ? and as_openid = ? and as_productid = ? LIMIT 0,1",portActivityReq.getAcId(),portActivityReq.getAsOpenId(),portActivityReq.getAsProductId());

        if (portActivityAssistDb != null) {
            renderJson(R.error("已经助力,请勿重复提交!").put("data",null).put("code","000037"));
            return;
        }

        log.info("=============接收助力信息："+JSON.toJSONString(portActivityReq));
        PortActivityAssist portActivityAssist = new PortActivityAssist();
        portActivityAssist.setAsAcId(portActivityReq.getAcId());
        portActivityAssist.setAsOpenid(portActivityReq.getAsOpenId());
        portActivityAssist.setAsMobile(portActivityReq.getAsMobile());
        portActivityAssist.setAsName(portActivityReq.getAsName());
        portActivityAssist.setAsAddress(portActivityReq.getAsAddress());
        portActivityAssist.setAsProductid(portActivityReq.getAsProductId());
        portActivityAssist.setAsInfo(portActivityReq.getHelperInfo());
        portActivityAssist.setCreateTime(new Date());
        Boolean flag = portActivityAssist.save();
        renderJson(R.ok().put("msg","保存成功!").put("data",portActivityAssist).put("code","000000"));

    }

    /*
     * @Description //根据微信openId获取海报信息接口
     * @Author wangkaida
     * @Date 10:47 2020/6/19
     * @Param [portEmpReq]
     * @return void
     **/
    public void getPlayBillByWxOpenId(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        //手机号获取数据信息
        PortActivityEmp portEmpDb = PortActivityEmp.dao.findFirst("SELECT * FROM port_activity_emp WHERE WxOpenId = ? and accountType = ?  LIMIT 0,1", portEmpReq.getWxOpenId(),portEmpReq.getPb_ac_id());

        if (portEmpDb != null) {
            //根据WxOpenId查询海报是否存在，无则生成海报，有则返回
            PortActivityPlaybill portActivityPlaybillDb = PortActivityPlaybill.dao.findFirst("SELECT * FROM port_activity_playbill WHERE pb_source_openid = ? and pb_ac_id = ? LIMIT 0,1",portEmpReq.getWxOpenId(),portEmpReq.getPb_ac_id());
            if (portActivityPlaybillDb != null) {
                renderJson(R.ok().put("data", portActivityPlaybillDb).put("code","000000"));
            }else {
                //生成海报,默认活动Id为1
                PortActivityPlaybill portActivityPlaybill = new PortActivityPlaybill();
                portActivityPlaybill.setPbSourceOpenid(portEmpReq.getWxOpenId());
                portActivityPlaybill.setPbAcId(portEmpReq.getPb_ac_id());
                Boolean flag = portActivityPlaybill.save();

                //根据海报id生成小程序码
                Long pbId = portActivityPlaybill.getLong("id");
                BufferedImage pbWxCodeBI = QrCodeUtil.playBillWxQrCodeCreate(pbId,wxMpConfiguration);
                byte[] pbWxCode = QrCodeUtil.imageToBytes(pbWxCodeBI,"png");
                //更新小程序码到活动海报表
                //合成海报
                String pbName = QrCodeUtil.syntheticPlayBill(pbWxCode,pbId,portEmpReq.getPb_ac_id());
                //更新海报名称到活动海报表
                //将二维码以图片形式保存
                String qrName = QrCodeUtil.syntheticPlayBillQrcode(pbWxCode,pbId);

                PortActivityPlaybill portActivityPlaybillUpdate = new PortActivityPlaybill();
                portActivityPlaybillUpdate.setPbQrcode(qrName);
                portActivityPlaybillUpdate.setPbPlaybill(pbName);
                portActivityPlaybillUpdate.setId(pbId);
                portActivityPlaybillUpdate.update();

                renderJson(R.ok().put("data",portActivityPlaybill).put("code","000000"));
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }

    }

    /*
     * @Description //根据助力id获取发起助力人openid和帮助助力的人的列表接口
     * @Author wangkaida
     * @Date 13:26 2020/6/19
     * @Param [portActivityReq]
     * @return void
     **/
    public void getHelperListByAssistId(@Para("") PortActivityReq portActivityReq){

        if(portActivityReq.getAssistId() == null){
            renderJson(R.error("请输入需被助力记录Id!").put("data",null).put("code","000039"));
            return;
        }

        PortActivityAssist portActivityAssistDb = PortActivityAssist.dao.findFirst("SELECT * FROM port_activity_assist WHERE id = ? LIMIT 0,1",portActivityReq.getAssistId());

        if (portActivityAssistDb != null) {

            List<PortActivityHelper> portActivityHelperList = PortActivityHelper.dao.find("select * from port_activity_helper where assistId = ?", portActivityAssistDb.getId());

            renderJson(R.ok().put("portActivityAssist", portActivityAssistDb).put("data", portActivityHelperList).put("code","000000"));

        }else {
            renderJson(R.error("发起助力的人员信息为空!").put("data",null).put("code","000040"));
            return;
        }
    }

    /*
     * @Description //更新分享Id与需被助力记录Id关系接口
     * @Author wangkaida
     * @Date 16:02 2020/6/19
     * @Param [portActivityReq]
     * @return void
     **/
    public void updatePortActivityShare(@Para("") PortActivityReq portActivityReq){

        if(portActivityReq.getShareId() == null){
            renderJson(R.error("请输入分享Id!").put("data",null).put("code","000041"));
            return;
        }

        if(portActivityReq.getAssistId() == null){
            renderJson(R.error("请输入需被助力记录Id!").put("data",null).put("code","000039"));
            return;
        }

        PortActivityShare portActivityShare = new PortActivityShare();
        portActivityShare.setSrAsId(portActivityReq.getAssistId());
        portActivityShare.setId(portActivityReq.getShareId().longValue());
        portActivityShare.update();

        renderJson(R.ok().put("msg","更新成功!").put("data",portActivityShare).put("code","000000"));
    }

    /*
     * @Description //根据微信公众号openId获取助力信息接口
     * @Author wangkaida
     * @Date 17:03 2020/6/19
     * @Param [portEmpReq]
     * @return void
     **/
    public void getAssistByWxOpenId(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        List<PortActivityAssist> portActivityAssistDbList = PortActivityAssist.dao.find("SELECT * FROM port_activity_assist WHERE as_openid = ? ",portEmpReq.getWxOpenId());

        if (portActivityAssistDbList != null && portActivityAssistDbList.size()>0) {
            renderJson(R.ok().put("data", portActivityAssistDbList).put("code","000000"));
        }else {
            renderJson(R.error("查询到的发起助力的人员信息为空!").put("data",null).put("code","000042"));
            return;
        }

    }

    /*
     * @Description //获取助力人数接口
     * @Author wangkaida
     * @Date 19:46 2020/6/19
     * @Param []
     * @return void
     **/
    public void getAssistCount(){
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        int n = 500;
        int num=(int) (Math.random() * n+1);
        int m = 30;
//        int numSec1=(int) (Math.random() * m);
//        int numSec2=(int) (Math.random() * m);
//        int numSec3=(int) (Math.random() * m);
        int numSec1 = Db.queryInt(Db.getSql("admin.portActivityShare.countHelper"),1);
        int numSec2 = Db.queryInt(Db.getSql("admin.portActivityShare.countHelper"),2);
        int numSec3 = Db.queryInt(Db.getSql("admin.portActivityShare.countHelper"),3);
        int num1 = Db.queryInt(Db.getSql("admin.portActivityShare.countAssist"),1);
        int num2 = Db.queryInt(Db.getSql("admin.portActivityShare.countAssist"),2);
        int num3 = Db.queryInt(Db.getSql("admin.portActivityShare.countAssist"),3);
        int num4 = Db.queryInt(Db.getSql("admin.portActivityShare.countInvolvedNum"));
        int num5 = Db.queryInt(Db.getSql("admin.portActivityShare.countAssistNum"));
        int num6 = Db.queryInt(Db.getSql("admin.portActivityShare.countBrowseNum"));
        map.put("involvedCount",256+num4); //已报名人数
        map.put("assistCount",1367+num5); //已分享人数
        map.put("browseCount",4578+num6); //已浏览人数
        map.put("successCount",0+numSec1+numSec2+numSec3); //助力成功人数
        map.put("goods1Num",0+num1); //商品1参与人数
        map.put("goods1Purchase",numSec1); //商品1已抢
        map.put("goods1Remain",30-numSec1); //商品1仅剩
        map.put("goods2Num",num2); //商品2参与人数
        map.put("goods2Purchase",numSec2); //商品2已抢
        map.put("goods2Remain",30-numSec2); //商品2仅剩
        map.put("goods3Num",num3); //商品3参与人数
        map.put("goods3Purchase",numSec3); //商品3已抢
        map.put("goods3Remain",150-numSec3); //商品3仅剩
        renderJson(R.ok().put("data", map).put("code","000000"));
    }

    /**
     * 获取全部地址信息
     */
    public void getAllAddress(@Para("") PortActivityAddressReq portActivityAddressReq){
        renderJson(R.ok().put("data", PortActivityAddress.dao.find(
                Db.getSqlPara("admin.portActivityAddress.getAddress")
        )).put("code","000000"));
    }

    /**
     * 保存某条数据
     */
    public void updateAddress(@Para("") PortActivityAddressReq portActivityAddressReq){
        PortActivityAddress portActivityAddress = new PortActivityAddress();
        portActivityAddress.setId(portActivityAddressReq.getId());
        renderJson(R.ok().put("data", portActivityAddress.update()).put("code","000000"));
    }


    /**
     * 根据openid获取报名信息，若为空则是还没报名，若有信息则返回
     * @Author wangkaida
     */
    public void getEnroll(@Para("") PortActivityEnrollReq portActivityEnrollReq) {
        if(StrUtil.isEmpty(portActivityEnrollReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        PortActivityEnroll portActivityEnrollDb = PortActivityEnroll.dao.findFirst(Db.getSql("admin.portActivityEnroll.getEnroll"),portActivityEnrollReq.getWxOpenId(),portActivityEnrollReq.getEn_ac_id());

        if (portActivityEnrollDb != null) {
            renderJson(R.ok().put("data", portActivityEnrollDb).put("code","000000"));
        }else {
            renderJson(R.error("查询到的报名信息为空!").put("data",null).put("code","000042"));
            return;
        }
    }

    /**
     * 保存报名信息
     * @Author wangkaida
     */
    public void saveEnroll(@Para("") PortActivityEnrollReq portActivityEnrollReq) {
        if(StrUtil.isEmpty(portActivityEnrollReq.getWxOpenId())){
            renderJson(R.error("请输入公众号openId!").put("data",null).put("code","000043"));
            return;
        }

        if(StrUtil.isEmpty(portActivityEnrollReq.getPhone())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000044"));
            return;
        }

        if(portActivityEnrollReq.getName() == null){
            renderJson(R.error("请输入姓名!").put("data",null).put("code","000045"));
            return;
        }

        if(portActivityEnrollReq.getAddress() == null){
            renderJson(R.error("请输入地址!").put("data",null).put("code","000046"));
            return;
        }

        PortActivityEnroll portActivityEnrollDb = PortActivityEnroll.dao.findFirst(Db.getSql("admin.portActivityEnroll.getEnroll"),portActivityEnrollReq.getWxOpenId());

        if (portActivityEnrollDb != null) {
            renderJson(R.error("已经报名,请勿重复提交!").put("data",null).put("code","000047"));
            return;
        }

        PortActivityEnroll portActivityEnroll = new PortActivityEnroll();
        portActivityEnroll.setWxOpenid(portActivityEnrollReq.getWxOpenId());
        portActivityEnroll.setPhone(portActivityEnrollReq.getPhone());
        portActivityEnroll.setName(portActivityEnrollReq.getName());
        portActivityEnroll.setAddress(portActivityEnrollReq.getAddress());
        portActivityEnroll.setEnAcId(portActivityEnrollReq.getEn_ac_id());
        Boolean flag = portActivityEnroll.save();
        renderJson(R.ok().put("msg","保存成功!").put("data",portActivityEnroll).put("code","000000"));
    }

    public void getStatistics(@Para("") PortActivityStaticsReq portActivityStaticsReq) {
        Kv kv = Kv.by("openid",portActivityStaticsReq.getOpenid());
        String readTotal = Db.queryStr(Db.getSqlPara("admin.portActivityShare.statistics1",kv).getSql(),portActivityStaticsReq.getOpenid());
        String fissionTotal = Db.queryStr(Db.getSqlPara("admin.portActivityShare.statistics2",kv).getSql(),portActivityStaticsReq.getOpenid());
        String today = DateUtil.changeDateTOStr3(new Date());
        kv.set("today",today);
        String readToday = Db.queryStr(Db.getSqlPara("admin.portActivityShare.statistics1",kv).getSql(),portActivityStaticsReq.getOpenid(),today);
        String fissionToday = Db.queryStr(Db.getSqlPara("admin.portActivityShare.statistics2",kv).getSql(),portActivityStaticsReq.getOpenid(),today);

        Map map  =new HashMap<>();
        map.put("readTotal",readTotal);
        map.put("fissionTotal",fissionTotal);
        map.put("readToday",readToday);
        map.put("fissionToday",fissionToday);
        renderJson(R.ok().put("msg","保存成功!").put("data",map).put("code","000000"));
    }

    public void getExcel() {
        HttpServletResponse response = this.getResponse();
        List<Record> data = Db.find(Db.getSql("admin.portActivityShare.excel"));
        try {
            // 获得输出流
            OutputStream output = response.getOutputStream();

            // 设置应用类型，以及编码
            response.setContentType("application/msexcel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "filename=" + new String(("ceshi"+".xls").getBytes("gb2312"), "iso8859-1"));
            //转格式
            List row = new ArrayList<String>();


            for(int i=0;i<data.size();i++){
                List<String> col = new ArrayList<String>();
                Record record = data.get(i);
                String [] recordNames = {"wxopenid","name","total","readTotal","today","readToday","address"};
                String [] recordNamesZw = {"wxopenid","姓名","裂变数（总）","阅读数（总）","裂变数（今）","阅数（今）","地址"};
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
}