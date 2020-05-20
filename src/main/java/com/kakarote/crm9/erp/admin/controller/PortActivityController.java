package com.kakarote.crm9.erp.admin.controller;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Port.Emp;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.admin.entity.vo.PortActivityReq;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.erp.admin.service.PortActivityService;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.sms.entity.PictureRequestDto;
import com.kakarote.crm9.erp.wx.config.WxMaConfiguration;
import com.kakarote.crm9.utils.PictureUtil;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.common.error.WxErrorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;


/*
 * @Description //活动分享管理控制类
 * @Author wangkaida
 * @Date 15:11 2020/5/19
 * @Param
 * @return
 **/
public class PortActivityController extends Controller {

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
            renderJson(R.error("请输入海报发起人小程序openId!").put("code","000013"));
            return;
        }

        if(StrUtil.isEmpty(portActivityReq.getLeadingOpenId())){
            renderJson(R.error("请输入负责人小程序openId!").put("code","000014"));
            return;
        }

        if(portActivityReq.getAcId() == null){
            renderJson(R.error("请输入海报活动Id!").put("code","000015"));
            return;
        }

        PortActivityPlaybill portActivityPlaybillDb = PortActivityPlaybill.dao.findFirst("SELECT * FROM port_activity_playbill WHERE pb_source_openid = ? and pb_ac_id = ? LIMIT 0,1",portActivityReq.getSourceOpenId(),portActivityReq.getAcId());

        if (portActivityPlaybillDb != null) {
            renderJson(R.error("发起人已经生成该活动海报，请勿重复生成!").put("id",portActivityPlaybillDb.getId()).put("code","000015"));
            return;

        }else {
            PortActivityPlaybill portActivityPlaybill = new PortActivityPlaybill();
            portActivityPlaybill.setPbSourceOpenid(portActivityReq.getSourceOpenId());
            portActivityPlaybill.setPbLeadingOpenid(portActivityReq.getLeadingOpenId());
            portActivityPlaybill.setPbAcId(portActivityReq.getAcId());
            Boolean flag = portActivityPlaybill.save();

            //根据海报id生成小程序码
            Integer pbId = portActivityPlaybill.getInt("id");
            byte[] pbWxCode = playBillQrCodeCreate(pbId);
            //更新小程序码到活动海报表
            Paras ps = new Paras();
            ps.Add("pb_qrcode", pbWxCode);
            ps.Add("id", pbId);
            String sql = "UPDATE port_activity_playbill SET pb_qrcode="+SystemConfig.getAppCenterDBVarStr()+"pb_qrcode WHERE id=" + SystemConfig.getAppCenterDBVarStr()
                    + "id";
            int num = DBAccess.RunSQL(sql, ps);

            //合成海报
            String pbName = syntheticPlayBill(pbWxCode,pbId,portActivityReq.getAcId());
            //更新海报名称到活动海报表
            Paras psHb = new Paras();
            psHb.Add("pb_playbill", pbName);
            psHb.Add("id", pbId);
            String sqlHb = "UPDATE port_activity_playbill SET pb_playbill="+SystemConfig.getAppCenterDBVarStr()+"pb_playbill WHERE id=" + SystemConfig.getAppCenterDBVarStr()
                    + "id";
            int numHb = DBAccess.RunSQL(sqlHb, psHb);

            renderJson(R.ok().put("msg","保存成功!").put("id",pbId).put("code","000000"));
        }

    }

    /*
     * @Description //合成海报
     * @Author wangkaida
     * @Date 15:37 2020/5/20
     * @Param [pbWxCode]
     * @return java.lang.String
     **/
    private String syntheticPlayBill(byte[] pbWxCode, Integer pbId, Integer acId) {

        String outPicName = "";
        try {
            //获取背景图片名称
            PortActivity portActivityDb = PortActivity.dao.findFirst("SELECT * FROM port_activity WHERE id = ? LIMIT 0,1",acId);

            PictureRequestDto pictureRequestDto = new PictureRequestDto();
            pictureRequestDto.setBt(pbWxCode);
            String picPath = SystemConfig.getCS_AppSettings().get("PIC.PATH").toString();
            pictureRequestDto.setBackPicPath(picPath + portActivityDb.getAcPlaybillImg());
            outPicName = "HB_"+pbId+".jpg";
            pictureRequestDto.setOutPicPath(picPath+outPicName);

            //将b作为输入流，将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(pictureRequestDto.getBt());
            BufferedImage code = ImageIO.read(in);

            BufferedImage big = PictureUtil.combineCodeAndPicToFile(pictureRequestDto.getBackPicPath(), code);
            String suffix = pictureRequestDto.getBackPicPath().substring(pictureRequestDto.getBackPicPath().lastIndexOf(".") + 1);
            ImageIO.write(big,suffix , new File(pictureRequestDto.getOutPicPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outPicName;
    }

    /*
     * @Description //根据海报id生成小程序码
     * @Author wangkaida
     * @Date 14:12 2020/5/20
     * @Param [pbId]
     * @return byte[]
     **/
    private byte[] playBillQrCodeCreate(Integer pbId) {

        String appid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        byte[] wxCode = null;
        try {
            String scene = "pid="+pbId;
            String page = "/activityPages/agentActivity/agentActivityDetail";
            wxCode = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, 280, true, null, false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return wxCode;
    }

    /*
     * @Description //活动分享接口
     * @Author wangkaida
     * @Date 20:39 2020/5/19
     * @Param [portActivityReq]
     * @return void
     **/
    public void activityShare(@Para("") PortActivityReq portActivityReq){

        if(StrUtil.isEmpty(portActivityReq.getShareOpenId())){
            renderJson(R.error("请输入分享人小程序openId!").put("code","000018"));
            return;
        }

        if(portActivityReq.getPbId() == null){
            renderJson(R.error("请输入海报Id!").put("code","000020"));
            return;
        }

        PortActivityShare portActivityShare = new PortActivityShare();
        portActivityShare.setSrShareOpenid(portActivityReq.getShareOpenId());
        portActivityShare.setSrToShareOpenid(portActivityReq.getToShareOpenId());
        portActivityShare.setSrPbId(portActivityReq.getPbId());
        Boolean flag = portActivityShare.save();
        renderJson(R.ok().put("msg","保存成功!").put("id",portActivityShare.getInt("id")).put("code","000000"));

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
            renderJson(R.error("请输入海报Id!").put("code","000020"));
            return;
        }

        Record record = Db.findFirst("SELECT a.*,b.pb_source_openid FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id where b.id = ? LIMIT 0,1",portActivityReq.getPbId());

        if (record != null) {
            renderJson(R.ok().put("data", record).put("code","000000"));

        }else {
            renderJson(R.error("获取到的活动信息为空!").put("code","000021"));
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
            renderJson(R.error("请输入分享Id!").put("code","000022"));
            return;
        }

        Record record = Db.findFirst("SELECT a.*,b.id pbId,c.sr_share_openid,c.sr_to_share_openid FROM port_activity a left join port_activity_playbill b on a.id = b.pb_ac_id left join port_activity_share c on b.id = c.sr_pb_id where c.id = ? LIMIT 0,1",portActivityReq.getShareId());

        if (record != null) {
            renderJson(R.ok().put("data", record).put("code","000000"));

        }else {
            renderJson(R.error("获取到的活动信息为空!").put("code","000021"));
            return;
        }

    }

}