package com.kakarote.crm9.utils;

import BP.Difference.SystemConfig;
import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.PortActivity;
import com.kakarote.crm9.erp.sms.entity.PictureRequestDto;
import com.kakarote.crm9.erp.wx.config.WxMaConfiguration;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Hashtable;

/*
 * @Description //生成二维码工具类
 * @Author wangkaida
 * @Date 11:10 2020/5/25
 * @Param 
 * @return 
 **/
public class QrCodeUtil {

    /*
     * @Description //生成二维码图片
     * @Author wangkaida
     * @Date 15:37 2020/5/20
     * @Param [pbWxCode]
     * @return java.lang.String
     **/
    public static String syntheticPlayBillQrcode(byte[] pbWxCode, Long pbId) {

        String outPicName = "";
        try {
            PictureRequestDto pictureRequestDto = new PictureRequestDto();
            pictureRequestDto.setBt(pbWxCode);
            String picPath = BaseConstant.UPLOAD_PATH + "/PlayBill/";
            outPicName = "HB_QR"+pbId+".jpg";
            pictureRequestDto.setOutPicPath(picPath+outPicName);

            //将b作为输入流，将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(pictureRequestDto.getBt());
            BufferedImage code = ImageIO.read(in);
            ImageIO.write(code ,"png", new File(pictureRequestDto.getOutPicPath()));
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
    public static byte[] playBillQrCodeCreate(Long pbId) {

        String appid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        byte[] wxCode = null;
        try {
            String scene = "pid="+pbId;
            String page = "activityPages/agentActivity/agentActivityDetail";
            wxCode = wxService.getQrcodeService().createWxaCodeUnlimitBytes(scene, page, 280, true, null, false);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return wxCode;
    }

    /*
     * @Description //根据海报id生成微信二维码
     * @Author wangkaida
     * @Date 14:55 2020/5/25
     * @Param [pbId]
     * @return byte[]
     **/
    public static BufferedImage playBillWxQrCodeCreate(Long pbId,WxMpConfiguration wxMpConfiguration) {

        String appid = SystemConfig.getCS_AppSettings().get("MP.APPID").toString();

        BufferedImage wxCode = null;
        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        wxMpService.switchover(appid);

        try {
            String proPath = SystemConfig.getCS_AppSettings().get("PRO.PATH").toString();
            String params = "pid="+pbId;
            String content = proPath+"dist/index.html?"+params;

            //生成二维码
            wxCode = PictureUtil.createImage(content, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wxCode;
    }

    /*
     * @Description //合成海报
     * @Author wangkaida
     * @Date 15:37 2020/5/20
     * @Param [pbWxCode]
     * @return java.lang.String
     **/
    public static String syntheticPlayBill(byte[] pbWxCode, Long pbId, Integer acId) {

        String outPicName = "";
        try {
            //获取背景图片名称
            PortActivity portActivityDb = PortActivity.dao.findFirst("SELECT * FROM port_activity WHERE id = ? LIMIT 0,1",acId);

            PictureRequestDto pictureRequestDto = new PictureRequestDto();
            pictureRequestDto.setBt(pbWxCode);

            String picPath = BaseConstant.UPLOAD_PATH + "/PlayBill/";
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

    /**
     * 转换BufferedImage 数据为byte数组
     * @param bImage
     * Image对象
     * @param format
     * image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

}
