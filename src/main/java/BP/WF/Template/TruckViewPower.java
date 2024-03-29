package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Port.*;
import BP.Web.WebUser;
import BP.WF.*;
import java.util.*;

/** 
 流程轨迹权限
*/
public class TruckViewPower extends EntityNoName
{

		///#region 属性
	/** 
	 发起人可看
	 * @throws Exception 
	*/
	public final boolean getPStarter() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PStarter);
	}
	public final void setPStarter(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PStarter, value);
	}
	/** 
	 参与人可见
	 * @throws Exception 
	*/
	public final boolean getPWorker() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PWorker);
	}
	public final void setPWorker(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PWorker, value);
	}
	/** 
	 被抄送人可见
	 * @throws Exception 
	*/
	public final boolean getPCCer() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PCCer);
	}
	public final void setPCCer(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PCCer, value);
	}
	/** 
	 本部门可见
	 * @throws Exception 
	*/
	public final boolean getPMyDept() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PMyDept);
	}
	public final void setPMyDept(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PMyDept, value);
	}
	/** 
	 直属上级部门可见
	 * @throws Exception 
	*/
	public final boolean getPPMyDept() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PPMyDept);
	}
	public final void setPPMyDept(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PPMyDept, value);
	}
	/** 
	 上级部门可见
	 * @throws Exception 
	*/
	public final boolean getPPDept() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PPDept);
	}
	public final void setPPDept(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PPDept, value);
	}
	/** 
	 平级部门可见
	 * @throws Exception 
	*/
	public final boolean getPSameDept() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PSameDept);
	}
	public final void setPSameDept(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSameDept, value);
	}
	/** 
	 指定部门可见
	 * @throws Exception 
	*/
	public final boolean getPSpecDept() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PSpecDept);
	}
	public final void setPSpecDept(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecDept, value);
	}
	/** 
	 部门编号
	*/
	public final String getPSpecDeptExt() throws Exception
	{
		return this.GetValStrByKey(TruckViewPowerAttr.PSpecDeptExt);
	}
	public final void setPSpecDeptExt(String value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecDeptExt, value);
	}
	/** 
	 指定岗位可见
	*/
	public final boolean getPSpecSta() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PSpecSta);
	}
	public final void setPSpecSta(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecSta, value);
	}
	/** 
	 岗位编号
	*/
	public final String getPSpecStaExt() throws Exception
	{
		return this.GetValStrByKey(TruckViewPowerAttr.PSpecStaExt);
	}
	public final void setPSpecStaExt(String value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecStaExt, value);
	}

	/** 
	 权限组
	*/
	public final boolean getPSpecGroup() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PSpecGroup);
	}
	public final void setPSpecGroup(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecGroup, value);
	}

	/** 
	 权限组编号
	*/
	public final String getPSpecGroupExt() throws Exception
	{
		return this.GetValStrByKey(TruckViewPowerAttr.PSpecGroupExt);
	}
	public final void setPSpecGroupExt(String value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecGroupExt, value);
	}

	/** 
	 指定的人员
	*/
	public final boolean getPSpecEmp() throws Exception
	{
		return this.GetValBooleanByKey(TruckViewPowerAttr.PSpecEmp);
	}
	public final void setPSpecEmp(boolean value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecEmp, value);
	}
	/** 
	 指定编号
	*/
	public final String getPSpecEmpExt() throws Exception
	{
		return this.GetValStrByKey(TruckViewPowerAttr.PSpecEmpExt);
	}
	public final void setPSpecEmpExt(String value) throws Exception
	{
		this.SetValByKey(TruckViewPowerAttr.PSpecEmpExt, value);
	}









		///#endregion


		///#region 构造方法
	/** 
	 流程轨迹权限
	*/
	public TruckViewPower()
	{
	}

	public TruckViewPower(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Flow", "流程模版主表");

		map.Java_SetDepositaryOfEntity(Depositary.Application);


		map.AddTBStringPK(TruckViewPowerAttr.No, null, "编号", true, true, 1, 10, 3);
		map.AddTBString(TruckViewPowerAttr.Name, null, "名称", true, false, 0, 50, 10, true);



			///#region 权限控制. 此部分与流程属性同步.
		map.AddBoolean(TruckViewPowerAttr.PStarter, true, "发起人可看(必选)", true, false, true);
		map.AddBoolean(TruckViewPowerAttr.PWorker, true, "参与人可看(必选)", true, false, true);
		map.AddBoolean(TruckViewPowerAttr.PCCer, true, "被抄送人可看(必选)", true, false, true);

		map.AddBoolean(TruckViewPowerAttr.PMyDept, true, "本部门人可看", true, true, true);
		map.AddBoolean(TruckViewPowerAttr.PPMyDept, true, "直属上级部门可看(比如:我是)", true, true, true);

		map.AddBoolean(TruckViewPowerAttr.PPDept, true, "上级部门可看", true, true, true);
		map.AddBoolean(TruckViewPowerAttr.PSameDept, true, "平级部门可看", true, true, true);

		map.AddBoolean(TruckViewPowerAttr.PSpecDept, true, "指定部门可看", true, true, false);
		map.AddTBString(TruckViewPowerAttr.PSpecDeptExt, null, "部门编号", true, false, 0, 200, 100, false);


		map.AddBoolean(TruckViewPowerAttr.PSpecSta, true, "指定的岗位可看", true, true, false);
		map.AddTBString(TruckViewPowerAttr.PSpecStaExt, null, "岗位编号", true, false, 0, 200, 100, false);

		map.AddBoolean(TruckViewPowerAttr.PSpecGroup, true, "指定的权限组可看", true, true, false);
		map.AddTBString(TruckViewPowerAttr.PSpecGroupExt, null, "权限组", true, false, 0, 200, 100, false);

		map.AddBoolean(TruckViewPowerAttr.PSpecEmp, true, "指定的人员可看", true, true, false);
		map.AddTBString(TruckViewPowerAttr.PSpecEmpExt, null, "指定的人员编号", true, false, 0, 200, 100, false);

			///#endregion 权限控制.


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 公用方法.
	/** 
	 检查指定的人员是否可以产看该轨迹图.
	 
	 @param workid 流程ID
	 @param userNo 操作员
	 @return 
	 * @throws Exception 
	*/
	public final boolean CheckICanView(long workid, String userNo) throws Exception
	{
		if (userNo == null)
		{
			userNo = WebUser.getNo();
		}
		return true;
	}

		///#endregion
}