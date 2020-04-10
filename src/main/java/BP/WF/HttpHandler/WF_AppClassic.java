package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.ContextHolderUtils;
import BP.Difference.SystemConfig;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.Web.*;
import net.sf.json.JSONObject;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.WeiXin.DingDing;
import BP.WF.WeiXin.WeiXin;
import BP.WF.WeiXin.Util.Crypto.AesException;
import BP.WF.WeiXin.Util.Crypto.WXBizMsgCrypt;
import BP.WF.WeiXin.Util.Crypto.WeiXinUtil;
import BP.WF.Data.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/** 
 椤甸潰鍔熻兘瀹炰綋
*/
public class WF_AppClassic extends WebContralBase
{


	/** 
	 鏋勯�犲嚱鏁�
	*/
	public WF_AppClassic()
	{

	}



		///#region 鎵ц鐖剁被鐨勯噸鍐欐柟娉�.
	/** 
	 榛樿鎵ц鐨勬柟娉�
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		switch (this.getDoType())
		{
			case "DtlFieldUp": //瀛楁涓婄Щ
				return "鎵ц鎴愬姛.";
			default:
				break;
		}
		// 鎵句笉涓嶅埌鏍囪灏辨姏鍑哄紓甯�.
		return "err@娌℃湁鍒ゆ柇鐨勬墽琛屾爣璁�:" + this.getDoType();
	}

		///#endregion 鎵ц鐖剁被鐨勯噸鍐欐柟娉�.


		///#region xxx 鐣岄潰 .


		///#endregion xxx 鐣岄潰鏂规硶.

	/** 
	 鍒濆鍖朒ome
	 
	 @return 
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public final String Home_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		//绯荤粺鍚嶇О.
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("CustomerName", SystemConfig.getCustomerName());

		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolist_EmpWorks());
		ht.put("Todolist_Runing", Dev2Interface.getTodolist_Runing());
		ht.put("Todolist_Sharing", Dev2Interface.getTodolist_Sharing());
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolist_CCWorks());
		ht.put("Todolist_Apply", Dev2Interface.getTodolist_Apply()); //鐢宠涓嬫潵鐨勪换鍔′釜鏁�.
		ht.put("Todolist_Draft", Dev2Interface.getTodolist_Draft()); //鑽夌鏁伴噺.
		ht.put("Todolist_Complete", Dev2Interface.getTodolist_Complete()); //瀹屾垚鏁伴噺.
		ht.put("UserDeptName", WebUser.getFK_DeptName());

		//鎴戝彂璧�
		MyStartFlows myStartFlows = new MyStartFlows();
		QueryObject obj = new QueryObject(myStartFlows);
		obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
		obj.addAnd();
		//杩愯涓璡宸插畬鎴怽鎸傝捣\閫�鍥瀄杞彂\鍔犵\鎵瑰鐞哱
		obj.addLeftBracket();
		obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
		obj.addRightBracket();
		obj.DoQuery();
		ht.put("Todolist_MyStartFlow", myStartFlows.size());

		//鎴戝弬涓�
		MyJoinFlows myFlows = new MyJoinFlows();
		obj = new QueryObject(myFlows);
		obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
		obj.DoQuery();
		ht.put("Todolist_MyFlow", myFlows.size());

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	@SuppressWarnings("unchecked")
	public final String Index_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("Todolist_Runing", Dev2Interface.getTodolist_Runing()); //杩愯涓�.
		ht.put("Todolist_EmpWorks", Dev2Interface.getTodolist_EmpWorks()); //寰呭姙
		ht.put("Todolist_CCWorks", Dev2Interface.getTodolist_CCWorks()); //鎶勯��.

		//鏈懆.
		ht.put("TodayNum", Dev2Interface.getTodolist_CCWorks()); //鎶勯��.

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}


		///#region 鐧诲綍鐣岄潰.
	public final String Portal_Login() throws Exception
	{
		String userNo = this.GetRequestVal("UserNo");

		try
		{
			BP.Port.Emp emp = new Emp(userNo);

			Dev2Interface.Port_Login(emp.getNo());
			return ".";
		}
		catch (RuntimeException ex)
		{
			return "err@鐢ㄦ埛[" + userNo + "]鐧诲綍澶辫触." + ex.getMessage();
		}

	}
	/** 
	 鐧诲綍.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Login_Submit() throws Exception
	{
		try
		{
			String userNo = this.GetRequestVal("TB_No");
			if (userNo == null)
			{
				userNo = this.GetRequestVal("TB_UserNo");
			}

			String pass = this.GetRequestVal("TB_PW");
			if (pass == null)
			{
				pass = this.GetRequestVal("TB_Pass");
			}

			BP.Port.Emp emp = new Emp();
			emp.setNo(userNo);
			if (emp.RetrieveFromDBSources() == 0)
			{
				if (DBAccess.IsExitsTableCol("Port_Emp", "NikeName") == true)
				{
					/*濡傛灉鍖呭惈鏄电О鍒�,灏辨鏌ユ樀绉版槸鍚﹀瓨鍦�.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE NikeName=" + SystemConfig.getAppCenterDBVarStr() + "NikeName";
					ps.Add("NikeName", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
					}
				}
				else if (DBAccess.IsExitsTableCol("Port_Emp", "Name") == true)
				{
					/*濡傛灉鍖呭惈Name鍒�,灏辨鏌ame鏄惁瀛樺湪.*/
					Paras ps = new Paras();
					ps.SQL = "SELECT No FROM Port_Emp WHERE Name=" + SystemConfig.getAppCenterDBVarStr() + "Name";
					ps.Add("Name", userNo);
					String no = DBAccess.RunSQLReturnStringIsNull(ps, null);
					if (no == null)
					{
						return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
					}

					emp.setNo(no);
					int i = emp.RetrieveFromDBSources();
					if (i == 0)
					{
						return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
					}


				}
				else
				{
					return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
				}
			}

			if (emp.CheckPass(pass) == false)
			{
				return "err@鐢ㄦ埛鍚嶆垨鑰呭瘑鐮侀敊璇�.";
			}

			//璋冪敤鐧诲綍鏂规硶.
			BP.WF.Dev2Interface.Port_Login(emp.getNo());

			return "";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 鎵ц鐧诲綍
	 
	 @return 
	 * @throws Exception 
	*/
	@SuppressWarnings("unchecked")
	public final String Login_Init() throws Exception
	{
		Hashtable ht = new Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		if (WebUser.getNoOfRel() == null)
		{
			ht.put("UserNo", "");
			ht.put("UserName", "");
		}
		else
		{
			ht.put("UserNo", WebUser.getNo());

			String name = WebUser.getName();

			if (DataType.IsNullOrEmpty(name) == true)
			{
				ht.put("UserName", WebUser.getNo());
			}
			else
			{
				ht.put("UserName", name);
			}
		}

		return BP.Tools.Json.ToJsonEntityModel(ht);
	}
	
	/**
	 * 寰俊鍥炶皟/楠岃瘉
	 * @throws Exception
	 */
	public void WeiXin_Init() throws Exception{

		//寰俊鍥炶皟
		//1.鑾峰彇绛惧悕锛坰ignature锛夈�佹椂闂存埑(timestamp)銆侀殢鏈哄瓧绗︿覆(nonce) 鍜岄獙璇佸洖璋冪殑URL
		String signature = this.GetRequestVal("msg_signature");//url涓殑绛惧悕
	
		String timestamp = this.GetRequestVal("timestamp");//url涓殑鏃堕棿鎴�
	
		String nonce = this.GetRequestVal("nonce");//url涓殑闅忔満瀛楃涓�
		
		String echostr = this.GetRequestVal("echostr");// 鍒涘缓濂椾欢鏃堕獙璇佸洖璋僽rl鏈夋晥鎬ф椂浼犲叆
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(SystemConfig.getWX_WeiXinToken(), SystemConfig.getWX_EncodingAESKey(), SystemConfig.getWX_CorpID());
        
		String sEchoStr  = wxcpt.VerifyURL(signature, timestamp, nonce, echostr);
        //蹇呴』瑕佽繑鍥炶В瀵嗕箣鍚庣殑鏄庢枃
        getResponse().getWriter().write(sEchoStr);
        
        //鏍规嵁 InfoType寰楀埌涓嶅悓绫诲瀷鐨勫洖璋冩暟鎹�
		 String postData = getStringInputstream(getRequest());
		 String returnMsg = wxcpt.DecryptMsg(signature, timestamp, nonce, postData);
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     DocumentBuilder db = dbf.newDocumentBuilder();
	     StringReader sr = new StringReader(returnMsg);
	     InputSource is = new InputSource(sr);
	     Document document = db.parse(is);
	
	     Element root = document.getDocumentElement();
	     String msgType = root.getElementsByTagName("MsgType").item(0).toString();//淇℃伅绫诲瀷
	     String userID = root.getElementsByTagName("FromUserName").item(0).toString();//鍛樺伐鐨処D
	     String CorpID = root.getElementsByTagName("ToUserName").item(0).toString();//浼佷笟鐨� CorpID
         
         String eventStr = "";
         String sb="";
         if (msgType.equals("event") || msgType == "event")
         {
             eventStr = root.getElementsByTagName("Event").item(0).toString();
             if (eventStr.equals("subscribe"))
             {
                 //鍏虫敞鎴�
                 sb = WeiXinUtil.ResponseMsg(userID, null, null, "text", " 鍏虫敞浜�");
             }
             if (eventStr.equals("unsubscribe"))
             {
                 //鍙栨秷鍏虫敞浜嗘垜浠�
             }
             

             if (eventStr.equals("enter_agent"))
             {
                 sb = WeiXinUtil.ResponseMsg(userID, null, null, "text", "娆㈣繋鎮細" + userID + "<a href=\"www.baidu.com\">鐐瑰嚮璺宠浆</a>");
             }

         }
         if(DataType.IsNullOrEmpty(sb)==true){
        	//鍙戦�佹秷鎭垨鑰呯櫥闄嗕俊鎭�
             boolean IsSend = new WeiXin().PostWeiXinMsg(sb);
             if (IsSend == true)
            	 getResponse().getWriter().write("鏁版嵁璇锋眰鎴愬姛");
             else	 
            	 getResponse().getWriter().write("err@鏁版嵁璇锋眰澶辫触");
         }
         
	}
   
	
	public String WeiXin_Login() throws Exception{
		//鑾峰彇code
		String code = this.GetRequestVal("code");
		System.out.println("code="+code);
		if(DataType.IsNullOrEmpty(code) == true)
			return "err@涓存椂鐧诲綍鍑瘉code涓虹┖";
		//鑾峰彇token
		String access_token = new WeiXin().GenerAccessToken();
		if(access_token.contains("err@")==true)
			return access_token;
		if(DataType.IsNullOrEmpty(WebUser.getNo()) == true){
			//鏍规嵁token鑾峰彇鐢ㄦ埛淇℃伅(鐢ㄦ埛鍚�/鎵嬫満鍙�)
			String userId = new WeiXin().getUserInfo(code, access_token);
			if(DataType.IsNullOrEmpty(userId)==true)
				return "err@鐢ㄦ埛娌℃湁鏉冮檺鎴栬�呮暟鎹姹傚け璐ワ紝璇疯仈绯荤鐞嗗憳";
			Emp emp = new Emp();
			emp.setNo(userId);
			if(emp.RetrieveFromDBSources() == 0){ 
				BP.GPM.Emps emps = new BP.GPM.Emps();
				int num = emps.Retrieve(BP.GPM.EmpAttr.Tel, userId);
				if(num ==0)
					return "err@鍦ㄧ郴缁熶腑娌℃湁鎵惧埌璐﹀彿涓�"+userId+"鐨勪俊鎭紝璇疯仈绯荤鐞嗗憳";
				
				BP.WF.Dev2Interface.Port_Login(emps.getItem(0).getNo());
				int expiry = 60 * 60 * 24 * 2;
				ContextHolderUtils.addCookie("Token", expiry, access_token);
				WebUser.setToken(access_token);
				return "鐧婚檰鎴愬姛";
			}
			BP.WF.Dev2Interface.Port_Login(userId);
			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("Token", expiry, access_token);
			WebUser.setToken(access_token);
		}
		
		return "鐧婚檰鎴愬姛";
		
	}
	
	public String DingDing_Login() throws Exception{
		
		String err=""; 
		try
		{
			err +=" 在创建实体的时候出现错误.";
		DingDing ding = new DingDing();
		//鑾峰彇code
		String code = this.GetRequestVal("code");
		System.out.println("code="+code);
		if(DataType.IsNullOrEmpty(code) == true)
			return "err@涓存椂鐧诲綍鍑瘉code涓虹┖";
		//鑾峰彇token
		String access_token = ding.GenerAccessToken();
		if(access_token.contains("err@")==true)
			return access_token;
		if(DataType.IsNullOrEmpty(WebUser.getNo()) == true){
			//鏍规嵁token鑾峰彇鐢ㄦ埛淇℃伅(鐢ㄦ埛鍚�/鎵嬫満鍙�)
			String json = ding.getUserInfo(code, access_token);
			if(DataType.IsNullOrEmpty(json)==true)
				return "err@鐢ㄦ埛娌℃湁鏉冮檺鎴栬�呮暟鎹姹傚け璐ワ紝璇疯仈绯荤鐞嗗憳";
			String userId="";
			JSONObject jd = JSONObject.fromObject(json);
			if(jd.get("errcode").toString().equals("0") == false)
				return "err@鐢ㄦ埛娌℃湁鏉冮檺鎴栬�呮暟鎹姹傚け璐ワ紝璇疯仈绯荤鐞嗗憳";
			
			userId  = jd.get("userid").toString();
			Emp emp = new Emp();
			emp.setNo(userId);
			if(emp.RetrieveFromDBSources() == 0){ 
				//鑾峰彇name
				String name = jd.get("name").toString();
				BP.GPM.Emps emps = new BP.GPM.Emps();
				int num = emps.Retrieve(BP.GPM.EmpAttr.Name, name);
				if(num ==0)
					return "err@鍦ㄧ郴缁熶腑娌℃湁鎵惧埌璐﹀彿涓�"+userId+"鐨勪俊鎭紝璇疯仈绯荤鐞嗗憳";
				
				BP.WF.Dev2Interface.Port_Login(emps.getItem(0).getNo());
				int expiry = 60 * 60 * 24 * 2;
				ContextHolderUtils.addCookie("Token", expiry, access_token);
				WebUser.setToken(access_token);
				return "鐧婚檰鎴愬姛";
			}
			BP.WF.Dev2Interface.Port_Login(userId);
			int expiry = 60 * 60 * 24 * 2;
			ContextHolderUtils.addCookie("Token", expiry, access_token);
			WebUser.setToken(access_token);
		}
		
		return "鐧婚檰鎴愬姛";
		
		}catch(Exception e)
		{

			  e.printStackTrace();
			  return "";
		}
		
	}
	
    private  String getStringInputstream(HttpServletRequest request) {
        StringBuffer strb = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String str = null;
            while (null != (str = reader.readLine())) {
                strb.append(str);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strb.toString();
    }

}