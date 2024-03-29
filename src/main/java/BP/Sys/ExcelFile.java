package BP.Sys;

import BP.DA.DataType;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;

import java.util.*;

/** 
 Excel模板
*/
public class ExcelFile extends EntityNoName
{

		///#region 属性
	/** 
	 获取或设置标识
	 * @throws Exception 
	*/
	public final String getMark() throws Exception
	{
		return this.GetValStrByKey(ExcelFileAttr.Mark);
	}
	public final void setMark(String value) throws Exception
	{
		this.SetValByKey(ExcelFileAttr.Mark, value);
	}

	/** 
	 获取或设置类型
	 * @throws Exception 
	*/
	public final ExcelType getExcelType() throws Exception
	{
		return ExcelType.forValue(this.GetValIntByKey(ExcelFileAttr.ExcelType));
	}
	public final void setExcelType(ExcelType value) throws Exception
	{
		this.SetValByKey(ExcelFileAttr.ExcelType, value);
	}

	/** 
	 获取或设置上传说明
	 * @throws Exception 
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStrByKey(ExcelFileAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(ExcelFileAttr.Note, value);
	}


		///#endregion 属性


		///#region 构造方法
	public ExcelFile()
	{
	}

	public ExcelFile(String no) throws Exception
	{
		this.Retrieve(ExcelFileAttr.No, no);
	}

		///#endregion 构造方法


		///#region 权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}

		///#endregion 权限控制


		///#region EnMap
	/** 
	 Excel模板Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_ExcelFile");
		map.setEnDesc("Excel模板");

		map.AddTBStringPK(ExcelFileAttr.No, null, "编号", true, true, 1, 36, 200);
		map.AddTBString(ExcelFileAttr.Name, null, "名称", true, false, 1, 50, 100, true);
		map.AddTBString(ExcelFileAttr.Mark, null, "标识", true, false, 1, 50, 100);
		map.AddDDLSysEnum(ExcelFileAttr.ExcelType, 0, "类型", true, true, ExcelFileAttr.ExcelType, "@0=普通文件数据提取@1=流程附件数据提取");
		map.AddTBStringDoc(ExcelFileAttr.Note, null, "上传说明", true, false, true);

		map.AddSearchAttr(ExcelFileAttr.ExcelType);

		map.AddMyFile("模板文件", "*.xls|*.xlsx");

		RefMethod rm = new RefMethod();
		rm.Title = "模板配置";
		rm.ClassMethodName = this + ".ExcelConfig";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion EnMap

	public final String ExcelConfig() throws Exception
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/ExcelUploadConfig.htm?No=" + this.getNo();
	}


		///#region 重写事件
	/** 
	 记录添加前事件
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setNo(UUID.randomUUID().toString());
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (DataType.IsNullOrEmpty(this.getMark()))
		{
			this.setMark(BP.Tools.chs2py.ConvertStr2Code(this.getName()));
		}

		return super.beforeUpdateInsertAction();
	}


		///#endregion 重写事件
}