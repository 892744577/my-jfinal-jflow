package com.kakarote.crm9.erp.sms.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.entity.PictureRequestDto;
import com.kakarote.crm9.erp.sms.service.SmsService;
import com.kakarote.crm9.utils.PictureUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class SmsController extends Controller {

    @Inject
    private SmsService smsService;

    /**
     * @author tangmanrong
     * 发送手机验证码
     */
    public void sendCode(@Para("") LoginRequestDto loginRequestDto){
        PortEmp emp = PortEmp.dao.findFirst(Db.getSql("admin.portEmp.getEmpAndActivityEmpByTel"),loginRequestDto.getMobile(),loginRequestDto.getMobile());
        if(emp == null){
            log.info("=========非活动邀请者不能使用该功能，请联系管理员："+ JSON.toJSONString(loginRequestDto));
            renderJson(R.ok().put("msg","非活动邀请者不能使用该功能，请联系管理员").put("code","000000"));
        }else{
            log.info("=========手机认证获取验证码请求信息："+ JSON.toJSONString(loginRequestDto));
            String appid=smsService.getAccount();
            String appSecret=smsService.getPassword();
            smsService.send(loginRequestDto);
            renderJson(R.ok().put("openId","xxx").put("code","000000"));
        }

    }

    /**
     * 根据手机号获取验证码
     * @author tangmanrong
     * @param loginRequestDto
     */
    public void getSmsByMobile(@Para("") LoginRequestDto loginRequestDto){

        renderJson(R.ok().put("验证码",smsService.getSmsByMobile(loginRequestDto)).put("code","000000"));
    }

    /**
     * 合成海报
     * @param pictureRequestDto
     */
    public void createPlayBill(@Para("") PictureRequestDto pictureRequestDto){
        try {
            //根据内容生成二维码的
            //BufferedImage code = PictureUtil.createImage(pictureRequestDto.getContent(), null, false);

            //将b作为输入流，将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(pictureRequestDto.getBt());
            BufferedImage code = ImageIO.read(in);

            BufferedImage big = PictureUtil.combineCodeAndPicToFile(pictureRequestDto.getBackPicPath(), code);
            String suffix = pictureRequestDto.getBackPicPath().substring(pictureRequestDto.getBackPicPath().lastIndexOf(".") + 1);
            ImageIO.write(big,suffix , new File(pictureRequestDto.getOutPicPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
