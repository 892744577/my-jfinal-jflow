package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 累加表单方案s
*/
public class FoolTruckNodeFrms extends EntitiesMyPK
{

		///#region 构造方法..
	/** 
	 累加表单方案
	*/
	public FoolTruckNodeFrms()
	{
	}

		///#endregion 构造方法..


		///#region 公共方法.
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new FoolTruckNodeFrm();
	}

		///#endregion 公共方法.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FoolTruckNodeFrm> ToJavaList()
	{
		return (List<FoolTruckNodeFrm>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FoolTruckNodeFrm> Tolist()
	{
		ArrayList<FoolTruckNodeFrm> list = new ArrayList<FoolTruckNodeFrm>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FoolTruckNodeFrm)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}