package com.kakarote.crm9.erp.wx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wx.util.MpUtil;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import com.kakarote.crm9.erp.wx.vo.MpUserInfoReq;
import com.kakarote.crm9.erp.wx.vo.ServerHandlerRequest;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class MpController extends Controller {

    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Inject
    private MpService mpService;

    public void getAccessTokenByInterface(){
        renderJson(R.ok().put("data",MpUtil.getAccessTokenByInterface()));
    }

    /*
     * @Description //通过code获取微信公众号openId
     * @Author wangkaida
     * @Date 16:30 2020/5/14
     * @Param [code]
     * @return void
     **/
    public void oauth2getAccessToken(@Para("code") String code){

        if (StringUtils.isBlank(code)) {
            renderJson(R.error("code不能为空!").put("code","000006"));
            return;
        }

        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        wxMpService.switchover(mpService.getAppid());
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        WxMpUser wxMpUser = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            wxMpUser = wxMpService.getOAuth2Service().getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("data",wxMpUser).put("code","000000"));
    }


    /*
     * @Description //通过code获取微信公众号openId
     * @Author wangkaida
     * @Date 16:30 2020/5/14
     * @Param [code]
     * @return void
     **/
    public void getJsapiConfig(@Para("url") String url){
        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        try {
            renderJson(R.ok().put("data",
                    wxMpService.createJsapiSignature(url))
                    .put("jsapi_ticket",wxMpService.getJsapiTicket()).
                            put("code","000000"));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }

    /**
     * 公众号发送模板消息--ma1
     */
    public void send(@Para("") MpMsgSendReq mpMsgSendReq){
        String result = mpService.send(mpMsgSendReq);
        if("ok".equals(result)){
            renderJson(R.ok().put("data",result));
        }else{
            renderJson(R.error().put("data",result));
        }
    }
    /**
     * 获取公众号用户基本信息---判断用户是否关注公众号
     */
    public void getUserData(@Para("") MpUserInfoReq mpUserInfoReq){
        renderJson(R.ok().put("subscribe",mpService.userInfo(mpUserInfoReq).getSubscribe()));
    }

    /**
     * 生成永久带参数二维码
     * @param scence
     */
    public void qrCodeCreateLastTicket(String scence) {
        renderJson(mpService.qrCodeCreateLastTicket(scence));
    }

    /**
     * 接收服务器消息推送
     */
    public void handler(@Para("") ServerHandlerRequest serverHandlerRequest) throws IOException {
        HttpServletRequest request = this.getRequest();
        //微信参数
        WxMpService wxMpService = wxMpConfiguration.wxMpService();
        WxMpMessageRouter wxMpMessageRouter = wxMpConfiguration.messageRouter(wxMpService);
        WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
        //for test
        System.out.println("开始执行验证方法。");
        String signature = serverHandlerRequest.getSignature();
        // 时间戳
        String timestamp = serverHandlerRequest.getTimestamp();
        // 随机数
        String nonce = serverHandlerRequest.getNonce();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        //1、非法请求
        if (!wxMpService.checkSignature(timestamp, nonce,signature)) {
            renderJson();
        }

        //2、说明是一个仅仅用来验证的请求，回显echostr
        String echostr = serverHandlerRequest.getEchostr();
        if (StringUtils.isNotBlank(echostr)) {
            renderText(echostr);
        }

        String encryptType = StringUtils.isBlank(serverHandlerRequest.getEncrypt_type()) ?
                "raw" :
                serverHandlerRequest.getEncrypt_type();

        String raw = getRawData();
        if ("raw".equals(encryptType) && StringUtils.isNotEmpty(raw)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(raw);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage!=null) {
                renderText(outMessage.toXml());
            }else{
                renderText("");
            }
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (StringUtils.isNotBlank(outMessage.toEncryptedXml(wxMpConfigStorage))) {
                renderText(outMessage.toEncryptedXml(wxMpConfigStorage));
            }
        }
    }
}
