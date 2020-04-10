package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点人员
*/
public class NodeEmps extends EntitiesMM
{
	/** 
	 他的到人员
	 * @throws Exception 
	*/
	public final Emps getHisEmps() throws Exception
	{
		Emps ens = new Emps();
		for (NodeEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Emp(ns.getFK_Emp()));
		}
		return ens;
	}
	/** 
	 他的工作节点
	 * @throws Exception 
	*/
	public final Nodes getHisNodes() throws Exception
	{
		Nodes ens = new Nodes();
		for (NodeEmp ns : this.ToJavaList())
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;

	}
	/** 
	 节点人员
	*/
	public NodeEmps()
	{
	}
	/** 
	 节点人员
	 
	 @param NodeID 节点ID
	 * @throws Exception 
	*/
	public NodeEmps(int NodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	/** 
	 节点人员
	 
	 @param EmpNo EmpNo 
	 * @throws Exception 
	*/
	public NodeEmps(String EmpNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new NodeEmp();
	}
	/** 
	 取到一个到人员集合能够访问到的节点s
	 
	 @param sts 到人员集合
	 @return 
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(Emps sts) throws Exception
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Emp st : sts.ToJavaList())
		{
			tmp = this.GetHisNodes(st.getNo());
			for (Node nd : tmp.ToJavaList())
			{
				if (nds.Contains(nd))
				{
					continue;
				}
				nds.AddEntity(nd);
			}
		}
		return nds;
	}
	/** 
	 到人员对应的节点
	 
	 @param EmpNo 到人员编号
	 @return 节点s
	 * @throws Exception 
	*/
	public final Nodes GetHisNodes(String EmpNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Emp, EmpNo);
		qo.DoQuery();

		Nodes ens = new Nodes();
		for (NodeEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	/** 
	 转向此节点的集合的 Nodes
	 
	 @param nodeID 此节点的ID
	 @return 转向此节点的集合的Nodes (FromNodes) 
	 * @throws Exception 
	*/
	public final Emps GetHisEmps(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeEmpAttr.FK_Node, nodeID);
		qo.DoQuery();

		Emps ens = new Emps();
		for (NodeEmp en : this.ToJavaList())
		{
			ens.AddEntity(new Emp(en.getFK_Emp()));
		}
		return ens;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<NodeEmp> ToJavaList()
	{
		return (List<NodeEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<NodeEmp> Tolist()
	{
		ArrayList<NodeEmp> list = new ArrayList<NodeEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((NodeEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}