package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.*;
import java.util.*;

/** 
 用户日志
*/
public class UserLog extends EntityMyPK
{
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}


		///#region 用户日志信息键值列表

		///#endregion


		///#region 基本属性
	public final String getIP() throws Exception
	{
		return this.GetValStringByKey(UserLogAttr.IP);
	}
	public final void setIP(String value) throws Exception
	{
		this.SetValByKey(UserLogAttr.IP, value);
	}
	/** 
	 日志标记键
	 * @throws Exception 
	*/
	public final String getLogFlag() throws Exception
	{
		return this.GetValStringByKey(UserLogAttr.LogFlag);
	}
	public final void setLogFlag(String value) throws Exception
	{
		this.SetValByKey(UserLogAttr.LogFlag, value);
	}
	/** 
	 FK_Emp
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(UserLogAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(UserLogAttr.FK_Emp, value);
	}
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(UserLogAttr.RDT);
	}
	public final void setRDT(String value) throws Exception
	{
		this.SetValByKey(UserLogAttr.RDT, value);
	}

	public final String getDocs() throws Exception
	{
		return this.GetValStringByKey(UserLogAttr.Docs);
	}
	public final void setDocs(String value) throws Exception
	{
		this.SetValByKey(UserLogAttr.Docs, value);
	}


		///#endregion


		///#region 构造方法
	/** 
	 用户日志
	*/
	public UserLog()
	{
	}

	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_UserLogT", "用户日志");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();

		map.AddTBString(UserLogAttr.FK_Emp, null, "用户", true, false, 0, 30, 20);
		map.AddTBString(UserLogAttr.IP, null, "IP", true, false, 0, 200, 20);
		map.AddTBString(UserLogAttr.LogFlag, null, "标识", true, false, 0, 300, 20);
		map.AddTBString(UserLogAttr.Docs, null, "说明", true, false, 0, 300, 20);
		map.AddTBString(UserLogAttr.RDT, null, "记录日期", true, false, 0, 20, 20);

		map.GetAttrByKey(this.getPK()).setUIVisible(false);

		map.DTSearchKey = UserLogAttr.RDT;
		map.DTSearchWay = DTSearchWay.ByDate;

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 重写
	@Override
	public Entities getGetNewEntities()
	{
		return new UserLogs();
	}

		///#endregion 重写
}