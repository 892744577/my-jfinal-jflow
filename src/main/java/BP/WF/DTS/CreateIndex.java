package BP.WF.DTS;

import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 创建索引
*/
public class CreateIndex extends Method
{
	/** 
	 创建索引
	*/
	public CreateIndex()
	{
		this.Title = "创建索引（为所有的流程,NDxxxTrack, NDxxRpt, 创建索引.）";
		this.Help = "创建索引字段,调高流程的运行效率.";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	 * @throws Exception 
	*/
	@Override
	public boolean getIsCanDo() throws Exception
	{
		if (WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	@Override
	public Object Do() throws NumberFormatException, Exception
	{
		String info = "开始为Track表创建索引.";

		Flows fls = new Flows();
		for (Flow fl : fls.ToJavaList())
		{
			info += fl.CreateIndex();
		}
		return info;

	}
}