package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 节点表单s
*/
public class FrmNodeExts extends EntitiesMyPK
{

		///#region 构造方法..
	/** 
	 节点表单
	*/
	public FrmNodeExts()
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
		return new FrmNodeExt();
	}

		///#endregion 公共方法.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmNodeExt> ToJavaList()
	{
		return (List<FrmNodeExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmNodeExt> Tolist()
	{
		ArrayList<FrmNodeExt> list = new ArrayList<FrmNodeExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmNodeExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}