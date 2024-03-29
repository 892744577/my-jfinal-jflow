package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 流程测试.
*/
public class TestCase extends EntityMyPK
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 流程测试的事务编号
	 * @throws Exception 
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(TestCaseDtlAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		SetValByKey(TestCaseDtlAttr.FK_Node, value);
	}
	public final String getParaType() throws Exception
	{
		return this.GetValStringByKey(TestCaseDtlAttr.ParaType);
	}
	public final void setParaType(String value) throws Exception
	{
		SetValByKey(TestCaseDtlAttr.ParaType, value);
	}
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(TestCaseDtlAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(TestCaseDtlAttr.FK_Flow, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 流程测试
	*/
	public TestCase()
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

		Map map = new Map("WF_TestCase", "自定义流程测试");

		map.AddMyPK();
		map.AddTBString(TestCaseDtlAttr.FK_Flow, null, "流程编号", true, false, 0, 100, 100, true);
		map.AddTBString(TestCaseDtlAttr.ParaType, null, "参数类型", true, false, 0, 100, 100, true);
		map.AddTBString(TestCaseDtlAttr.Vals, null, "值s", true, false, 0, 500, 300, true);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}