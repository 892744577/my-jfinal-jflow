package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点属性.
*/
public class NodeSheet extends Entity
{

		///#region 属性.
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID,value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(NodeAttr.Name, value);
	}

		///#endregion 属性.


		///#region 构造函数
	/** 
	 节点
	*/
	public NodeSheet()
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

			//map 的基 础信息.
		Map map = new Map("WF_Node", "节点");


			///#region  基础属性
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.SetHelperUrl(NodeAttr.NodeID, "http://ccbpm.mydoc.io/?v=5404&t=17901");
		map.AddTBInt(NodeAttr.Step, 0, "步骤(无计算意义)", true, false);
		map.SetHelperUrl(NodeAttr.Step, "http://ccbpm.mydoc.io/?v=5404&t=17902");
			//map.SetHelperAlert(NodeAttr.Step, "它用于节点的排序，正确的设置步骤可以让流程容易读写."); //使用alert的方式显示帮助信息.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 3, 3, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17023");
		map.AddTBString(NodeAttr.Name, null, "名称", true, true, 0, 100, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17903");

			///#endregion  基础属性


			///#region 对应关系
			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "节点绑定岗位", StationAttr.FK_StationType);

		map.getAttrsOfOneVSM().AddGroupListModel(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, "节点绑定岗位AddGroupListModel", StationAttr.FK_StationType);


			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new BP.WF.Template.NodeDepts(), new BP.Port.Depts(), BP.WF.Template.NodeDeptAttr.FK_Node, BP.WF.Template.NodeDeptAttr.FK_Dept, "节点绑定部门AddBranches", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new BP.WF.Template.NodeEmps(), new BP.Port.Emps(), BP.WF.Template.NodeEmpAttr.FK_Node, BP.WF.Template.NodeEmpAttr.FK_Emp, "节点绑定接受人", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

		map.AddDtl(new NodeToolbars(), NodeToolbarAttr.FK_Node);

			// 傻瓜表单可以调用的子流程. 2014.10.19 去掉.
			//map.getAttrsOfOneVSM().Add(new BP.WF.NodeFlows(), new Flows(), NodeFlowAttr.FK_Node, NodeFlowAttr.FK_Flow, DeptAttr.Name, DeptAttr.No,
			//    "傻瓜表单可调用的子流程");

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}