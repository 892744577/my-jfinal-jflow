package BP.WF;

import BP.En.*;
import BP.En.Map;

/** 
 授权
*/
public class Auth extends EntityMyPK
{
		///#region 基本属性
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFlowNo() throws Exception
	{
		return this.GetValStringByKey(AuthAttr.FlowNo);
	}
	public final void setFlowNo(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.FlowNo, value);
	}
	/** 
	 取回日期
	 * @throws Exception 
	*/
	public final String getTakeBackDT() throws Exception
	{
		return this.GetValStringByKey(AuthAttr.TakeBackDT);
	}
	public final void setTakeBackDT(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.TakeBackDT, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 授权
	*/
	public Auth()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Auth", "授权");

		map.AddMyPK();

		map.AddTBString(AuthAttr.Auther, null, "授权人", true, false, 0, 100, 10);
		map.AddTBInt(AuthAttr.AuthType, 0, "类型(0=全部流程1=指定流程)", true, false);

		map.AddTBString(AuthAttr.EmpNo, null, "委托给人员编号", true, false, 0, 100, 10);
		map.AddTBString(AuthAttr.EmpName, null, "委托给人员名称", true, false, 0, 100, 10);

		map.AddTBString(AuthAttr.FlowNo, null, "流程编号", true, false, 0, 100, 10);
		map.AddTBString(AuthAttr.FlowName, null, "流程名称", true, false, 0, 100, 10);

		map.AddTBDate(AuthAttr.TakeBackDT, null, "取回日期", true, false);
		map.AddTBDate(AuthAttr.RDT, null, "记录日期", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK( BP.DA.DBAccess.GenerGUID());
		return super.beforeInsert();
	}
}