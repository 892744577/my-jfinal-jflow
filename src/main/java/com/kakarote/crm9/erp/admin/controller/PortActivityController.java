package com.kakarote.crm9.erp.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.admin.entity.vo.PortActivityReq;
import com.kakarote.crm9.erp.admin.service.PortActivityService;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.utils.QrCodeUtil;
import com.kakarote.crm9.utils.R;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;


/*
 * @Description //活动分享管理控制类
 * @Author wangkaida
 * @Date 15:11 2020/5/19
 * @Param
 * @return
 **/
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

        PortActivityShare portActivityShareDb = PortActivityShare.dao.findFirst("SELECT * FROM port_activity_share WHERE sr_pb_id = ? and sr_share_openid = ? and sr_to_share_openid = ? LIMIT 0,1",portActivityReq.getPbId(),portActivityReq.getShareOpenId(),portActivityReq.getToShareOpenId());

        if (portActivityShareDb != null) {
            renderJson(R.error("该活动已经分享,请勿重复分享!").put("data",null).put("code","000035"));
            return;
        }

        PortActivityShare portActivityShare = new PortActivityShare();
        portActivityShare.setSrShareOpenid(portActivityReq.getShareOpenId());
        portActivityShare.setSrToShareOpenid(portActivityReq.getToShareOpenId());
        portActivityShare.setSrPbId(portActivityReq.getPbId());
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

        Record record = Db.findFirst("SELECT a.*,b.id pbId,c.sr_share_openid,c.sr_to_share_openid FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id left join port_activity_share c on b.id = c.sr_pb_id where c.id = ? LIMIT 0,1",portActivityReq.getShareId());

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
            renderJson(R.ok().put("portActivity", portActivityDb).put("code","000000"));

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

        if(StrUtil.isEmpty(portActivityReq.getHelperAppOpenId())){
            renderJson(R.error("请输入助力人小程序openId!").put("data",null).put("code","000028"));
            return;
        }

        if(StrUtil.isEmpty(portActivityReq.getHelperOpenId())){
            renderJson(R.error("请输入助力人公众号openId!").put("data",null).put("code","000029"));
            return;
        }

        if(portActivityReq.getShareId() == null){
            renderJson(R.error("请输入分享Id!").put("data",null).put("code","000027"));
            return;
        }

        PortActivityHelper portActivityHelperDb = PortActivityHelper.dao.findFirst("SELECT * FROM port_activity_helper WHERE shareId = ? and helperAppOpenId = ? and helperOpenId = ? LIMIT 0,1",portActivityReq.getShareId(),portActivityReq.getHelperAppOpenId(),portActivityReq.getHelperOpenId());

        if (portActivityHelperDb != null) {
            renderJson(R.error("已经集赞,请勿重复提交!").put("data",null).put("code","000030"));
            return;
        }
        
        PortActivityHelper portActivityHelper = new PortActivityHelper();
        portActivityHelper.setShareId(portActivityReq.getShareId());
        portActivityHelper.setHelperAppOpenId(portActivityReq.getHelperAppOpenId());
        portActivityHelper.setHelperOpenId(portActivityReq.getHelperOpenId());
        Boolean flag = portActivityHelper.save();
        renderJson(R.ok().put("msg","保存成功!").put("id",portActivityHelper.getInt("id")).put("code","000000"));

    }

}