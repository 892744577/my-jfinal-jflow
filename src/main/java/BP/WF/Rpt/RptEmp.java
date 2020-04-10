package BP.WF.Rpt;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import BP.Web.WebUser;

import java.util.*;

/** 
 RptEmp 的摘要说明。
*/
public class RptEmp extends Entity
{

	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsView = true;
			uac.IsDelete = true;
			uac.IsInsert = true;
			uac.IsUpdate = true;
			uac.IsAdjunct = true;
		}
		return uac;
	}


		///#region 基本属性
	/** 
	 报表ID
	 * @throws Exception 
	*/
	public final String getFK_Rpt() throws Exception
	{
		return this.GetValStringByKey(RptEmpAttr.FK_Rpt);
	}
	public final void setFK_Rpt(String value) throws Exception
	{
		SetValByKey(RptEmpAttr.FK_Rpt, value);
	}
	public final String getFK_EmpT() throws Exception
	{
		return this.GetValRefTextByKey(RptEmpAttr.FK_Emp);
	}
	/** 
	人员
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(RptEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		SetValByKey(RptEmpAttr.FK_Emp, value);
	}

		///#endregion


		///#region 扩展属性


		///#endregion


		///#region 构造函数
	/** 
	 报表人员
	*/
	public RptEmp()
	{
	}
	/** 
	 报表人员对应
	 
	 @param _empoid 报表ID
	 @param wsNo 人员编号 	
	 * @throws Exception 
	*/
	public RptEmp(String _empoid, String wsNo) throws Exception
	{
		this.setFK_Rpt(_empoid);
		this.setFK_Emp(wsNo);
		if (this.Retrieve() == 0)
		{
			this.Insert();
		}
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

		Map map = new Map("Sys_RptEmp", "报表人员对应信息");
		map.Java_SetEnType(EnType.Dot2Dot);

		map.AddTBStringPK(RptEmpAttr.FK_Rpt, null, "报表", false, false, 1, 15, 1);
		map.AddDDLEntitiesPK(RptEmpAttr.FK_Emp, null, "人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}