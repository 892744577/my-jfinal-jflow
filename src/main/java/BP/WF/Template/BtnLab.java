package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 节点按钮权限
*/
public class BtnLab extends Entity
{
	/** 
	 访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		uac.IsDelete = false;
		return uac;
	}


		///#region 基本属性
	/** 
	 but
	*/
	public static String getBtns()
	{
		return "Send,Save,Thread,Return,CC,Shift,Del,Rpt,Ath,Track,Opt,EndFlow,SubFlow";
	}
	/** 
	 PK
	*/
	@Override
	public String getPK()
	{
		return NodeAttr.NodeID;
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(BtnAttr.NodeID, value);
	}
	/** 
	 名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.Name, value);
	}
	/** 
	 查询标签
	 * @throws Exception 
	*/
	public final String getSearchLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SearchLab);
	}
	public final void setSearchLab(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.SearchLab, value);
	}
	/** 
	 查询是否可用
	 * @throws Exception 
	*/
	public final boolean getSearchEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.SearchEnable);
	}
	public final void setSearchEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.SearchEnable, value);
	}
	/** 
	 移交
	 * @throws Exception 
	*/
	public final String getShiftLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ShiftLab);
	}
	public final void setShiftLab(String value) throws Exception
	{
		this.SetValByKey(BtnAttr.ShiftLab, value);
	}
	/** 
	 是否启用移交
	 * @throws Exception 
	*/
	public final boolean getShiftEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ShiftEnable);
	}
	public final void setShiftEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.ShiftEnable, value);
	}
	/** 
	 选择接受人
	 * @throws Exception 
	*/
	public final String getSelectAccepterLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SelectAccepterLab);
	}
	/** 
	 选择接受人类型
	*/
	public final int getSelectAccepterEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.SelectAccepterEnable);
	}
	public final void setSelectAccepterEnable(int value) throws Exception
	{
		this.SetValByKey(BtnAttr.SelectAccepterEnable, value);
	}
	/** 
	 保存
	*/
	public final String getSaveLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SaveLab);
	}
	/** 
	 是否启用保存
	*/
	public final boolean getSaveEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.SaveEnable);
	}
	/** 
	 子线程按钮标签
	*/
	public final String getThreadLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ThreadLab);
	}
	/** 
	 子线程按钮是否启用
	*/
	public final boolean getThreadEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadEnable);
	}
	/** 
	 是否可以删除（当前分流，分合流节点发送出去的）子线程.
	*/
	public final boolean getThreadIsCanDel() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanDel);
	}
	/** 
	 是否可以移交.
	*/
	public final boolean getThreadIsCanShift() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ThreadIsCanShift);
	}
	/** 
	 子流程按钮标签
	*/
	public final String getSubFlowLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SubFlowLab);
	}
	/** 
	 跳转标签
	*/
	public final String getJumpWayLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.JumpWayLab);
	}
	public final JumpWay getJumpWayEnum() throws Exception
	{
		return JumpWay.forValue(this.GetValIntByKey(NodeAttr.JumpWay));
	}
	/** 
	 是否启用跳转
	*/
	public final boolean getJumpWayEnable() throws Exception
	{
		return this.GetValBooleanByKey(NodeAttr.JumpWay);
	}
	/** 
	 退回标签
	*/
	public final String getReturnLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ReturnLab);
	}
	/** 
	 退回字段
	*/
	public final String getReturnField() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ReturnField);
	}
	/** 
	 退回是否启用
	*/
	public final boolean getReturnEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ReturnRole);
	}
	/** 
	 挂起标签
	*/
	public final String getHungLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HungLab);
	}
	/** 
	 是否启用挂起
	*/
	public final boolean getHungEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.HungEnable);
	}
	/** 
	 打印标签
	*/
	public final String getPrintDocLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintDocLab);
	}
	/** 
	 是否启用打印
	*/
	public final boolean getPrintDocEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintDocEnable);
	}
	/** 
	 发送标签
	*/
	public final String getSendLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.SendLab);
	}
	/** 
	 是否启用发送?
	*/
	public final boolean getSendEnable() throws Exception
	{
		return true;
	}
	/** 
	 发送的Js代码
	*/
	public final String getSendJS() throws Exception
	{
		String str = this.GetValStringByKey(BtnAttr.SendJS).replace("~", "'");
		if (this.getCCRole() == BP.WF.CCRole.WhenSend)
		{
			str = str + "  if ( OpenCC()==false) return false;";
		}
		return str;
	}
	/** 
	 轨迹标签
	*/
	public final String getTrackLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.TrackLab);
	}
	/** 
	 是否启用轨迹
	*/
	public final boolean getTrackEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.TrackEnable);
	}
	/** 
	 查看父流程标签
	*/
	public final String getShowParentFormLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ShowParentFormLab);
	}

	/** 
	 是否启用查看父流程
	*/
	public final boolean getShowParentFormEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ShowParentFormEnable);
	}


	/** 
	 抄送标签
	*/
	public final String getCCLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.CCLab);
	}
	/** 
	 抄送规则
	*/
	public final CCRole getCCRole() throws Exception
	{
		return CCRole.forValue(this.GetValIntByKey(BtnAttr.CCRole));
	}
	/** 
	 删除标签
	*/
	public final String getDeleteLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.DelLab);
	}
	/** 
	 删除类型
	*/
	public final int getDeleteEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.DelEnable);
	}
	/** 
	 结束流程
	*/
	public final String getEndFlowLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.EndFlowLab);
	}
	/** 
	 是否启用结束流程
	*/
	public final boolean getEndFlowEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.EndFlowEnable);
	}
	/** 
	 是否启用流转自定义
	*/
	public final String getTCLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.TCLab);
	}
	/** 
	 是否启用流转自定义
	*/
	public final boolean getTCEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.TCEnable);
	}
	public final void setTCEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.TCEnable, value);
	}

	public final int getHelpRole() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.HelpRole);
	}

	public final String getHelpLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HelpLab);
	}

	/** 
	 审核标签
	*/
	public final String getWorkCheckLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.WorkCheckLab);
	}
	/** 
	 标签是否启用？
	*/
	//public bool SubFlowEnable111
	//{
	//    get
	//    {
	//        return this.GetValBooleanByKey(BtnAttr.SubFlowEnable);
	//    }
	//    set
	//    {
	//        this.SetValByKey(BtnAttr.SubFlowEnable, value);
	//    }
	//}
	/** 
	 审核是否可用
	*/
	public final boolean getWorkCheckEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.WorkCheckEnable);
	}
	public final void setWorkCheckEnable(boolean value) throws Exception
	{
		this.SetValByKey(BtnAttr.WorkCheckEnable, value);
	}
	/** 
	 考核 是否可用
	*/
	public final int getCHRole() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.CHRole);
	}
	/** 
	 考核 标签
	*/
	public final String getCHLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.CHLab);
	}
	/** 
	 重要性 是否可用
	*/
	public final boolean getPRIEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PRIEnable);
	}
	/** 
	 重要性 标签
	*/
	public final String getPRILab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PRILab);
	}
	/** 
	 关注 是否可用
	*/
	public final boolean getFocusEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.FocusEnable);
	}
	/** 
	 关注 标签
	*/
	public final String getFocusLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.FocusLab);
	}

	/** 
	 分配 是否可用
	*/
	public final boolean getAllotEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AllotEnable);
	}
	/** 
	 分配 标签
	*/
	public final String getAllotLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.AllotLab);
	}

	/** 
	 确认 是否可用
	*/
	public final boolean getConfirmEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.ConfirmEnable);
	}
	/** 
	 确认标签
	*/
	public final String getConfirmLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.ConfirmLab);
	}

	/** 
	 打包下载 是否可用
	*/
	public final boolean getPrintZipEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintZipEnable);
	}
	/** 
	 打包下载 标签
	*/
	public final String getPrintZipLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintZipLab);
	}
	/** 
	 pdf 是否可用
	*/
	public final boolean getPrintPDFEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintPDFEnable);
	}
	/** 
	 打包下载 标签
	*/
	public final String getPrintPDFLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintPDFLab);
	}

	/** 
	 html 是否可用
	*/
	public final boolean getPrintHtmlEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.PrintHtmlEnable);
	}
	/** 
	 html 标签
	*/
	public final String getPrintHtmlLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.PrintHtmlLab);
	}


	/** 
	 批量处理是否可用
	*/
	public final boolean getBatchEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.BatchEnable);
	}
	/** 
	 批处理标签
	*/
	public final String getBatchLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.BatchLab);
	}
	/** 
	 加签
	*/
	public final boolean getAskforEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.AskforEnable);
	}
	/** 
	 加签
	*/
	public final String getAskforLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.AskforLab);
	}
	/** 
	 会签规则
	*/
	public final HuiQianRole getHuiQianRole() throws Exception
	{
		return HuiQianRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianRole));
	}
	/** 
	 会签标签
	*/
	public final String getHuiQianLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.HuiQianLab);
	}

	public final HuiQianLeaderRole getHuiQianLeaderRole() throws Exception
	{
		return HuiQianLeaderRole.forValue(this.GetValIntByKey(BtnAttr.HuiQianLeaderRole));
	}

	/** 
	是否启用文档,@0=不启用@1=按钮方式@2=公文在前@3=表单在前
	*/
	private int getWebOfficeEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.WebOfficeEnable);
	}
	/** 
	 公文的工作模式 @0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式
	*/
	public final WebOfficeWorkModel getWebOfficeWorkModel() throws Exception
	{
		return WebOfficeWorkModel.forValue(this.getWebOfficeEnable());
	}
	public final void setWebOfficeWorkModel(WebOfficeWorkModel value) throws Exception
	{
		this.SetValByKey(BtnAttr.WebOfficeEnable, value.getValue());
	}
	/** 
	 文档按钮标签
	*/
	public final String getWebOfficeLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.WebOfficeLab);
	}
	/** 
	 打开本地文件
	*/
	public final boolean getOfficeOpenEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOpenEnable);
	}
	/** 
	 打开本地标签      
	*/
	public final String getOfficeOpenLab() throws Exception
	{
		return this.GetValStrByKey(BtnAttr.OfficeOpenLab);
	}
	/** 
	 打开模板
	*/
	public final boolean getOfficeOpenTemplateEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOpenTemplateEnable);
	}
	/** 
	 打开模板标签
	*/
	public final String getOfficeOpenTemplateLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeOpenTemplateLab);
	}
	/** 
	 保存按钮
	*/
	public final boolean getOfficeSaveEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeSaveEnable);
	}
	/** 
	 保存标签
	*/
	public final String getOfficeSaveLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeSaveLab);
	}
	/** 
	 接受修订
	*/
	public final boolean getOfficeAcceptEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeAcceptEnable);
	}
	/** 
	 接受修订标签
	*/
	public final String getOfficeAcceptLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeAcceptLab);
	}
	/** 
	 拒绝修订
	*/
	public final boolean getOfficeRefuseEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeRefuseEnable);
	}
	/** 
	 拒绝修订标签
	*/
	public final String getOfficeRefuseLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeRefuseLab);
	}
	public final String getOfficeOVerLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeOverLab);
	}
	/** 
	 是否套红
	*/
	public final boolean getOfficeOverEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeOverEnable);
	}
	/** 
	 套红按钮标签
	*/
	public final String getOfficeOverLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeOverLab);
	}
	/** 
	 是否打印
	*/
	public final boolean getOfficePrintEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficePrintEnable);
	}
	/** 
	 是否查看用户留痕
	*/
	public final boolean getOfficeMarksEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeMarksEnable);
	}
	/** 
	 打印按钮标签
	*/
	public final String getOfficePrintLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficePrintLab);
	}
	/** 
	 签章按钮
	*/
	public final boolean getOfficeSealEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeSealEnable);
	}
	/** 
	 签章标签
	*/
	public final String getOfficeSealLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeSealLab);
	}

	/** 
	插入流程
	*/
	public final boolean getOfficeInsertFlowEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeInsertFlowEnable);
	}
	/** 
	 流程标签
	*/
	public final String getOfficeInsertFlowLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeInsertFlowLab);
	}


	/** 
	 是否自动记录节点信息
	*/
	public final boolean getOfficeNodeInfo() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeNodeInfo);
	}

	/** 
	 是否自动记录节点信息
	*/ 
	public final boolean getOfficeReSavePDF() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeReSavePDF);
	}

	/** 
	 是否进入留痕模式
	*/
	public final boolean getOfficeIsMarks() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeIsMarks);
	}

	/** 
	 风险点模板
	*/
	public final String getOfficeFengXianTemplate() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeFengXianTemplate);
	}

	public final boolean getOfficeReadOnly() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeReadOnly);
	}

	/** 
	 下载按钮标签
	*/
	public final String getOfficeDownLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeDownLab);
	}
	/** 
	 下载按钮标签
	*/
	public final boolean getOfficeIsDown() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeDownEnable);
	}

	/** 
	 是否启用下载
	*/
	public final boolean getOfficeDownEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeDownEnable);
	}

	/** 
	 指定文档模板
	*/
	public final String getOfficeTemplate() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeTemplate);
	}


	/** 
	 是否使用父流程的文档
	*/
	public final boolean getOfficeIsParent() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeIsParent);
	}

	/** 
	 是否自动套红
	*/
	public final boolean getOfficeTHEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeTHEnable);
	}
	/** 
	 自动套红模板
	*/
	public final String getOfficeTHTemplate() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeTHTemplate);
	}


	/** 
	 公文标签
	*/
	public final String getOfficeBtnLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.OfficeBtnLab);
	}
	/** 
	 公文标签
	*/
	public final boolean getOfficeBtnEnable() throws Exception
	{
		return this.GetValBooleanByKey(BtnAttr.OfficeBtnEnable);
	}


	/** 
	 备注标签
	*/
	public final String getNoteLab() throws Exception
	{
		return this.GetValStringByKey(BtnAttr.NoteLab);
	}
	/** 
	备注标签
	*/
	public final int getNoteEnable() throws Exception
	{
		return this.GetValIntByKey(BtnAttr.NoteEnable);
	}


		///#endregion


		///#region 构造方法
	/** 
	 Btn
	*/
	public BtnLab()
	{
	}
	/** 
	 节点按钮权限
	 
	 @param nodeid 节点ID
	 * @throws Exception 
	*/
	public BtnLab(int nodeid) throws Exception
	{
		this.setNodeID(nodeid);
		this.RetrieveFromDBSources();
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

		Map map = new Map("WF_Node", "节点标签");


		map.Java_SetDepositaryOfEntity(Depositary.Application);

		map.AddTBIntPK(BtnAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(BtnAttr.Name, null, "节点名称", true, true, 0, 200, 10);

		map.AddTBString(BtnAttr.SendLab, "发送", "发送按钮标签", true, false, 0, 50, 10);
		map.AddTBString(BtnAttr.SendJS, "", "发送按钮JS函数", true, false, 0, 50, 10, true);

			//map.AddBoolean(BtnAttr.SendEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.JumpWayLab, "跳转", "跳转按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(NodeAttr.JumpWay, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.SaveLab, "保存", "保存按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SaveEnable, true, "是否启用", true, true);


		map.AddTBString(BtnAttr.ThreadLab, "子线程", "子线程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ThreadEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.ThreadIsCanDel, false, "是否可以删除子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);
		map.AddBoolean(BtnAttr.ThreadIsCanShift, false, "是否可以移交子线程(当前节点已经发送出去的线程，并且当前节点是分流，或者分合流有效，在子线程退回后的操作)？", true, true, true);


			// add 2019.1.9 for 东孚.
		map.AddTBString(BtnAttr.OfficeBtnLab, "打开公文", "公文按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeBtnEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.ReturnLab, "退回", "退回按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ReturnRole, true, "是否启用", true, true);
		map.AddTBString(BtnAttr.ReturnField, "", "退回信息填写字段", true, false, 0, 50, 10, true);

		map.AddDDLSysEnum(NodeAttr.ReturnOneNodeRole, 0, "单节点退回规则", true, true, NodeAttr.ReturnOneNodeRole, "@@0=不启用@1=按照[退回信息填写字段]作为退回意见直接退回@2=按照[审核组件]填写的信息作为退回意见直接退回", true);


		map.AddTBString(BtnAttr.CCLab, "抄送", "抄送按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.CCRole, 0, "抄送规则", true, true, BtnAttr.CCRole);

			//  map.AddBoolean(BtnAttr, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.ShiftLab, "移交", "移交按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShiftEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.DelLab, "删除流程", "删除流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.DelEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.EndFlowLab, "结束流程", "结束流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.EndFlowEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.HungLab, "挂起", "挂起按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.HungEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.ShowParentFormLab, "查看父流程", "查看父流程按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ShowParentFormEnable, false, "是否启用", true, true);
		map.SetHelperAlert(BtnAttr.ShowParentFormLab,"如果当前流程实例不是子流程，即时启用了，也不显示该按钮。");

		map.AddTBString(BtnAttr.PrintDocLab, "打印单据", "打印单据按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintDocEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.TrackLab, "轨迹", "轨迹按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TrackEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.SelectAccepterLab, "接受人", "接受人按钮标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.SelectAccepterEnable, 0, "方式", true, true, BtnAttr.SelectAccepterEnable);

			// map.AddBoolean(BtnAttr.SelectAccepterEnable, false, "是否启用", true, true);
			//map.AddTBString(BtnAttr.OptLab, "选项", "选项按钮标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.OptEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.SearchLab, "查询", "查询按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.SearchEnable, true, "是否启用", true, true);

			// 
		map.AddTBString(BtnAttr.WorkCheckLab, "审核", "审核按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.WorkCheckEnable, false, "是否启用", true, true);

			// 
		map.AddTBString(BtnAttr.BatchLab, "批量审核", "批量审核标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.BatchEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.AskforLab, "加签", "加签标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.AskforEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.HuiQianRole, 0, "会签模式", true, true, BtnAttr.HuiQianRole, "@0=不启用@1=协作模式@4=组长模式");

		map.AddDDLSysEnum(BtnAttr.HuiQianLeaderRole, 0, "会签组长规则", true, true, BtnAttr.HuiQianLeaderRole, "0=只有一个组长@1=最后一个组长发送@2=任意组长发送",true);

			//map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
			//map.AddDDLSysEnum(BtnAttr.HuiQianRole, 0, "会签模式", true, true, BtnAttr.HuiQianRole, "@0=不启用@1=组长模式@2=协作模式");

		   // map.AddTBString(BtnAttr.HuiQianLab, "会签", "会签标签", true, false, 0, 50, 10);
		  //  map.AddBoolean(BtnAttr.HuiQianRole, false, "是否启用", true, true);

			// add by stone 2014-11-21. 让用户可以自己定义流转.
		map.AddTBString(BtnAttr.TCLab, "流转自定义", "流转自定义", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.TCEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.WebOfficeLab, "公文", "公文标签", true, false, 0, 50, 10);
			//map.AddBoolean(BtnAttr.WebOfficeEnable, false, "是否启用", true, true);
		map.AddDDLSysEnum(BtnAttr.WebOfficeEnable, 0, "文档启用方式", true, true, BtnAttr.WebOfficeEnable, "@0=不启用@1=按钮方式@2=标签页置后方式@3=标签页置前方式"); //edited by liuxc,2016-01-18,from xc.

			// add by 周朋 2015-08-06. 重要性.
		map.AddTBString(BtnAttr.PRILab, "重要性", "重要性", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PRIEnable, false, "是否启用", true, true);

			// add by 周朋 2015-08-06. 节点时限.
		map.AddTBString(BtnAttr.CHLab, "节点时限", "节点时限", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.CHRole, 0, "时限规则", true, true, BtnAttr.CHRole, "0=禁用@1=启用@2=只读@3=启用并可以调整流程应完成时间");

			// add 2017.5.4  邀请其他人参与当前的工作.
		map.AddTBString(BtnAttr.AllotLab, "分配", "分配按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.AllotEnable, false, "是否启用", true, true);


			// add by 周朋 2015-12-24. 节点时限.
		map.AddTBString(BtnAttr.FocusLab, "关注", "关注", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.FocusEnable, true, "是否启用", true, true);

			// add 2017.5.4 确认就是告诉发送人，我接受这件工作了.
		map.AddTBString(BtnAttr.ConfirmLab, "确认", "确认按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ConfirmEnable, false, "是否启用", true, true);

			// add 2017.9.1 for 天业集团.
		map.AddTBString(BtnAttr.PrintHtmlLab, "打印Html", "打印Html标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintHtmlEnable, false, "是否启用", true, true);

			// add 2017.9.1 for 天业集团.
		map.AddTBString(BtnAttr.PrintPDFLab, "打印pdf", "打印pdf标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintPDFEnable, false, "是否启用", true, true);

			// add 2017.9.1 for 天业集团.
		map.AddTBString(BtnAttr.PrintZipLab, "打包下载", "打包下载zip按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.PrintZipEnable, false, "是否启用", true, true);

			// add 2019.3.10 增加List.
		map.AddTBString(BtnAttr.ListLab, "列表", "列表按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.ListEnable, true, "是否启用", true, true);

			//备注 流程不流转，设置备注信息提醒已处理人员当前流程运行情况
		map.AddTBString(BtnAttr.NoteLab, "备注", "备注标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.NoteEnable, 0, "启用规则", true, true, BtnAttr.NoteEnable, "0=禁用@1=启用@2=只读");

			//for 周大福.
		map.AddTBString(BtnAttr.HelpLab, "帮助", "帮助标签", true, false, 0, 50, 10);
		map.AddDDLSysEnum(BtnAttr.HelpRole, 0, "帮助显示规则", true, true, BtnAttr.HelpRole, "0=禁用@1=启用@2=强制提示@3=选择性提示");



			///#region 公文按钮
		map.AddTBString(BtnAttr.OfficeOpenLab, "打开本地", "打开本地标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOpenTemplateLab, "打开模板", "打开模板标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOpenTemplateEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSaveLab, "保存", "保存标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSaveEnable, true, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeAcceptLab, "接受修订", "接受修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeAcceptEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeRefuseLab, "拒绝修订", "拒绝修订标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeRefuseEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeOverLab, "套红", "套红标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeOverEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeMarksEnable, true, "是否查看用户留痕", true, true, true);

		map.AddTBString(BtnAttr.OfficePrintLab, "打印", "打印标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficePrintEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeSealLab, "签章", "签章标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeSealEnable, false, "是否启用", true, true);

		map.AddTBString(BtnAttr.OfficeInsertFlowLab, "插入流程", "插入流程标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeInsertFlowEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeNodeInfo, false, "是否记录节点信息", true, true);
		map.AddBoolean(BtnAttr.OfficeReSavePDF, false, "是否该自动保存为PDF", true, true);


		map.AddTBString(BtnAttr.OfficeDownLab, "下载", "下载按钮标签", true, false, 0, 50, 10);
		map.AddBoolean(BtnAttr.OfficeDownEnable, false, "是否启用", true, true);

		map.AddBoolean(BtnAttr.OfficeIsMarks, true, "是否进入留痕模式", true, true);
		map.AddTBString(BtnAttr.OfficeTemplate, "", "指定文档模板", true, false, 0, 100, 10);
		map.AddBoolean(BtnAttr.OfficeIsParent, true, "是否使用父流程的文档", true, true);

		map.AddBoolean(BtnAttr.OfficeTHEnable, false, "是否自动套红", true, true);
		map.AddTBString(BtnAttr.OfficeTHTemplate, "", "自动套红模板", true, false, 0, 200, 10);


			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		Node fl = new Node();
		fl.setNodeID(this.getNodeID());
		fl.RetrieveFromDBSources();
		fl.Update();

		BtnLab btnLab = new BtnLab();
		btnLab.setNodeID(this.getNodeID());
		btnLab.RetrieveFromDBSources();
		btnLab.Update();

		super.afterInsertUpdateAction();
	}

		///#endregion
}