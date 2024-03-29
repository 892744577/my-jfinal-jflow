package BP.WF.DTS;

import BP.DA.*;
import BP.Web.WebUser;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;

/** 
 修复表单物理表字段长度 的摘要说明
*/
public class ReLoadNDxxxxxxRpt extends Method
{
	/** 
	 不带有参数的方法
	*/
	public ReLoadNDxxxxxxRpt()
	{
		this.Title = "清除并重新装载流程报表";
		this.Help = "删除NDxxxRpt表数据，重新装载，此功能估计要执行很长时间，如果数据量较大有可能在web程序上执行失败。";
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
		else
		{
			return false;
		}
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		String msg = "";

		Flows fls = new Flows();
		fls.RetrieveAllFromDBSource();
		for (Flow fl : fls.ToJavaList())
		{
			try
			{
				msg += fl.DoReloadRptData();
			}
			catch (RuntimeException ex)
			{
				msg += "@在处理流程(" + fl.getName() + ")出现异常" + ex.getMessage();
			}
		}
		return "提示：" + fls.size() + "个流程参与了体检，信息如下：@" + msg;
	}
}