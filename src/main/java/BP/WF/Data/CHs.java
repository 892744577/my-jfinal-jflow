package BP.WF.Data;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 时效考核s
*/
public class CHs extends Entities
{

		///#region 构造方法属性
	/** 
	 时效考核s
	*/
	public CHs()
	{
	}

		///#endregion


		///#region 属性
	/** 
	 时效考核
	*/
	@Override
	public Entity getNewEntity()
	{
		return new CH();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<CH> ToJavaList()
	{
		return (List<CH>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CH> Tolist()
	{
		ArrayList<CH> list = new ArrayList<CH>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CH)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}