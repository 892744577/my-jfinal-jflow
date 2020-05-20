package com.kakarote.crm9.erp.sms.controller;

import com.google.zxing.WriterException;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.entity.PictureRequestDto;
import com.kakarote.crm9.erp.sms.service.SmsService;
import com.kakarote.crm9.utils.PictureUtil;
import com.kakarote.crm9.utils.R;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SmsController extends Controller {

    @Inject
    private SmsService smsService;

    /**
     * @author tangmanrong
     * 发送手机验证码
     */
    public void sendCode(@Para("") LoginRequestDto loginRequestDto){
        String appid=smsService.getAccount();
        String appSecret=smsService.getPassword();
        smsService.send(loginRequestDto);

        renderJson(R.ok().put("openId","xxx").put("code","000000"));
    }

    /**
     * 根据手机号获取验证码
     * @author tangmanrong
     * @param loginRequestDto
     */
    public void getSmsByMobile(@Para("") LoginRequestDto loginRequestDto){

        renderJson(R.ok().put("验证码",smsService.getSmsByMobile(loginRequestDto)).put("code","000000"));
    }

    public void createPlayBill(@Para("") PictureRequestDto pictureRequestDto){
        try {
            BufferedImage code = PictureUtil.createImage(pictureRequestDto.getContent(), null, false);
            BufferedImage big = PictureUtil.combineCodeAndPicToFile(pictureRequestDto.getBackPicPath(), code);
            String suffix = pictureRequestDto.getBackPicPath().substring(pictureRequestDto.getBackPicPath().lastIndexOf(".") + 1);
            ImageIO.write(big,suffix , new File(pictureRequestDto.getOutPicPath()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
