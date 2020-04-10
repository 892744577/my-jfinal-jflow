package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的人员
 表单ID的到人员有两部分组成.	 
 记录了从一个表单ID到其他的多个表单ID.
 也记录了到这个表单ID的其他的表单ID.
*/
public class EmpCreate extends EntityMM
{

		///#region 基本属性
	/** 
	表单ID
	 * @throws Exception 
	*/
	public final int getFrmID() throws Exception
	{
		return this.GetValIntByKey(EmpCreateAttr.FrmID);
	}
	public final void setFrmID(int value) throws Exception
	{
		this.SetValByKey(EmpCreateAttr.FrmID,value);
	}
	/** 
	 到人员
	 * @throws Exception 
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(EmpCreateAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(EmpCreateAttr.FK_Emp,value);
	}
	public final String getFK_EmpT() throws Exception
	{
		return this.GetValRefTextByKey(EmpCreateAttr.FK_Emp);
	}

		///#endregion


		///#region 构造方法
	/** 
	 单据可创建的人员
	*/
	public EmpCreate()
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

		Map map = new Map("Frm_EmpCreate", "单据可创建的人员");

		map.AddTBStringPK(EmpCreateAttr.FrmID,null,"表单",true,true,1,100,100);
		map.AddDDLEntitiesPK(EmpCreateAttr.FK_Emp, null, "人员", new Emps(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}