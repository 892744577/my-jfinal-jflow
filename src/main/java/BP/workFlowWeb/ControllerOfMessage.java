package BP.workFlowWeb;

import BP.Difference.ContextHolderUtils;
import BP.WF.WeiXin.DingDing;
import BP.WF.WeiXin.WeiXin;
import BP.workFlowWeb.config.RestFulInterceptor;
import com.jfinal.aop.Before;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Before(RestFulInterceptor.class)
public class ControllerOfMessage {

    /**
     * 消息类型
     * @return
     */
    public String getDoType() {

        return ContextHolderUtils.getRequest().getParameter("DoType");
    }

    public String getSender() {

        return ContextHolderUtils.getRequest().getParameter("sender");
    }

    public String getSenderTo() {

        return ContextHolderUtils.getRequest().getParameter("sendTo");
    }

    public String getContent() {

        return ContextHolderUtils.getRequest().getParameter("content");
    }

    public String getTel() {

        return ContextHolderUtils.getRequest().getParameter("tel");
    }

    public String getTitle() {

        return ContextHolderUtils.getRequest().getParameter("title");
    }

    /**
     * 消息参数
     * @return
     */
    public String getOpenUrl() {

        return ContextHolderUtils.getRequest().getParameter("openUrl");
    }


    public boolean SendMessage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        /*ServletInputStream in = request.getInputStream();
        String message = readLine(in);
        System.out.println(message);
        //获取参数
        JSONObject jd = JSONObject.fromObject(message);
        String sender="";
        String sendTo="";
        String msgInfo = "";
        String tel ="";
        String openUrl="";
        String title ="";
        if(jd!=null){
            sender = jd.get("sender").toString();
            sendTo = jd.get("sendTo").toString();
            msgInfo = jd.get("content").toString();
            tel = jd.get("tel").toString();
            title = jd.get("title").toString();
            openUrl = jd.get("openUrl").toString();
        }*/
        //web服务
        if(this.getDoType().equals("SendToWebServices")){

        }
        //钉钉
        if(this.getDoType().equals("SendToDingDing")) {
            DingDing dingding = new DingDing();
            String postJson = dingding.ResponseMsg(this.getTel(), "", "", "text", this.getContent());
            boolean flag = dingding.PostDingDingMsg(postJson,this.getSenderTo());
            if(flag == false)
                throw new Exception("发送消息失败");
            return true;
        }
        //微信
        if(this.getDoType().equals("SendToWeiXin")){
            WeiXin weiXin = new WeiXin();
            String postJson = weiXin.ResponseMsg(this.getTel(), "", "", "text", this.getContent());
            boolean flag = new WeiXin().PostWeiXinMsg(postJson);
            if(flag == false)
                throw new Exception("发送消息失败");
            return true;
        }
        //即时通
        if(this.getDoType().equals("SendToCCMSG")){

        }
        throw new Exception("暂时不支持的消息类型"+this.getDoType());

    }

    private static String readLine(ServletInputStream in) throws Exception {
        byte[] buf = new byte[8 * 1024];
        StringBuffer sbuf = new StringBuffer();
        int result;

        do {
            result = in.readLine(buf, 0, buf.length); // does +=
            if (result != -1) {
                sbuf.append(new String(buf, 0, result, "UTF-8"));
            }
        } while (result == buf.length);

        if (sbuf.length() == 0) {
            return null;
        }

        int len = sbuf.length();
        if (sbuf.charAt(len - 2) == '\r') {
            sbuf.setLength(len - 2); // cut \r\n
        } else {
            sbuf.setLength(len - 1); // cut \n
        }
        return sbuf.toString();
    }

}
