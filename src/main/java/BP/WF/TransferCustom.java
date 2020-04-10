package BP.WF;

import BP.En.*;
import BP.WF.Template.*;

/**
 * 自定义运行路径
 */
public class TransferCustom extends EntityMyPK {
	/**
	 * 节点ID
	 * 
	 * @throws Exception
	 */
	public final int getFK_Node() throws Exception {
		return this.GetValIntByKey(TransferCustomAttr.FK_Node);
	}

	public final void setFK_Node(int value) throws Exception {
		this.SetValByKey(TransferCustomAttr.FK_Node, value);
	}

	public final long getWorkID() throws Exception {
		return this.GetValInt64ByKey(TransferCustomAttr.WorkID);
	}

	public final void setWorkID(long value) throws Exception {
		this.SetValByKey(TransferCustomAttr.WorkID, value);
	}

	/**
	 * 节点名称
	 */
	public final String getNodeName() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.NodeName);
	}

	public final void setNodeName(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.NodeName, value);
	}

	/**
	 * 计划完成日期
	 */
	public final String getPlanDT() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.PlanDT);
	}

	public final void setPlanDT(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.PlanDT, value);
	}

	/**
	 * 处理人（多个人用逗号分开）
	 */
	public final String getWorker() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.Worker);
	}

	public final void setWorker(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.Worker, value);
	}

	/**
	 * 处理人显示（多个人用逗号分开）
	 */
	public final String getWorkerName() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.WorkerName);
	}

	public final void setWorkerName(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.WorkerName, value);
	}

	/**
	 * 要启用的子流程编号
	 */
	public final String getSubFlowNo() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.SubFlowNo);
	}

	public final void setSubFlowNo(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.SubFlowNo, value);
	}

	/**
	 * 多人处理工作模式
	 */
	public final TodolistModel getTodolistModel() throws Exception {
		return TodolistModel.forValue(this.GetValIntByKey(TransferCustomAttr.TodolistModel));
	}

	public final void setTodolistModel(TodolistModel value) throws Exception {
		this.SetValByKey(TransferCustomAttr.TodolistModel, value.getValue());
	}

	/**
	 * 发起时间（可以为空）
	 */
	public final String getStartDT() throws Exception {
		return this.GetValStringByKey(TransferCustomAttr.StartDT);
	}

	public final void setStartDT(String value) throws Exception {
		this.SetValByKey(TransferCustomAttr.StartDT, value);
	}

	/**
	 * 顺序
	 */
	public final int getIdx() throws Exception {
		return this.GetValIntByKey(TransferCustomAttr.Idx);
	}

	public final void setIdx(int value) throws Exception {
		this.SetValByKey(TransferCustomAttr.Idx, value);
	}

	public final boolean getIsEnable() throws Exception {
		return this.GetValBooleanByKey(TransferCustomAttr.IsEnable);
	}

	public final void setIsEnable(boolean value) throws Exception {
		this.SetValByKey(TransferCustomAttr.IsEnable, value);
	}

	/**
	 * TransferCustom
	 */
	public TransferCustom() {
	}

	/**
	 * 重写基类方法
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}
		Map map = new Map("WF_TransferCustom", "自定义运行路径");
		map.Java_SetEnType(EnType.Admin);

		map.AddMyPK(); // 唯一的主键.

		// 主键.
		map.AddTBInt(TransferCustomAttr.WorkID, 0, "WorkID", true, false);
		map.AddTBInt(TransferCustomAttr.FK_Node, 0, "节点ID", true, false);
		map.AddTBString(TransferCustomAttr.NodeName, null, "节点名称", true, false, 0, 200, 10);

		map.AddTBString(TransferCustomAttr.Worker, null, "处理人(多个人用逗号分开)", true, false, 0, 200, 10);
		map.AddTBString(TransferCustomAttr.WorkerName, null, "处理人(多个人用逗号分开)", true, false, 0, 200, 10);

		map.AddTBString(TransferCustomAttr.SubFlowNo, null, "要经过的子流程编号", true, false, 0, 3, 10);
		map.AddTBDateTime(TransferCustomAttr.PlanDT, null, "计划完成日期", true, false);
		map.AddTBInt(TransferCustomAttr.TodolistModel, 0, "多人工作处理模式", true, false);
		map.AddTBInt(TransferCustomAttr.IsEnable, 0, "是否启用", true, false);

		map.AddTBInt(TransferCustomAttr.Idx, 0, "顺序号", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

	public final String DoUp() throws Exception {
		this.DoOrderUp(TransferCustomAttr.WorkID, String.valueOf(this.getWorkID()), TransferCustomAttr.Idx);
		return "执行成功";
	}

	public final String DoDown() throws Exception {
		this.DoOrderDown(TransferCustomAttr.WorkID, String.valueOf(this.getWorkID()), TransferCustomAttr.Idx);
		return "执行成功";
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setMyPK(this.getFK_Node() + "_" + this.getWorkID());
		return super.beforeInsert();
	}

	/**
	 * 获取下一个要到达的定义路径. 要分析如下几种情况: 1, 当前节点不存在队列里面，就返回第一个. 2, 如果当前队列为空,就认为需要结束掉,
	 * 返回null. 3, 如果当前节点是最后一个并且没有连接线连到固定节点,就返回null,表示要结束流程. 4.
	 * 如果当前节点是最后一个且有连接线连到固定节点
	 * 
	 * @param workid
	 *            当前工作ID
	 * @param currNodeID
	 *            当前节点ID
	 * @return 获取下一个要到达的定义路径,如果没有就返回空.
	 * @throws Exception
	 */
	public static TransferCustom GetNextTransferCustom(long workid, int currNodeID) throws Exception {
		TransferCustoms ens = new TransferCustoms();
		ens.Retrieve(TransferCustomAttr.WorkID, workid, TransferCustomAttr.Idx);
		if (ens.size() == 0) {
			return null;
		}

		// 寻找当前节点的下一个.
		boolean isMeet = false;
		for (TransferCustom item : ens.ToJavaList()) {
			if (item.getFK_Node() == currNodeID) {
				isMeet = true;
				continue;
			}

			if (isMeet == true && item.getIsEnable() == true) {
				return item;
			}
		}

		Node node = new Node(currNodeID);
		if (node.GetParaBoolen(NodeAttr.IsYouLiTai) == false) {
			for (TransferCustom item : ens.ToJavaList()) {
				if (item.getIsEnable() == true && item.getFK_Node() != currNodeID) {
					return (TransferCustom) item;
				}
			}
		}

		// }

		// 如果当前节点是最后一个自定义节点，且有连接线连到固定节点
		if (isMeet == true) {
			// 判断当前节点是否连接到固定节点
			String sql = "SELECT AtPara FROM WF_Node WHERE NodeID In(SELECT ToNode FROM WF_Direction WHERE Node="
					+ currNodeID + ")";
			Nodes nds = new Nodes();
			nds.RetrieveInSQL(NodeAttr.NodeID, "SELECT ToNode FROM WF_Direction WHERE Node = " + currNodeID);
			for (Node nd : nds.ToJavaList()) {
				if (nd.GetParaBoolen(NodeAttr.IsYouLiTai) == true) {
					continue;
				}

				TransferCustom en = new TransferCustom();
				en.setFK_Node(nd.getNodeID());
				// 更改流程的运行状态@yuan
				GenerWorkFlow gwf = new GenerWorkFlow(workid);
				gwf.setTransferCustomType(TransferCustomType.ByCCBPMDefine);
				gwf.Update();
				return en;
			}

		}

		return null;
		// return null;
	}
}