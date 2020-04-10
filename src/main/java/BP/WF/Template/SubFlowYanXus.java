package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 延续子流程集合
*/
public class SubFlowYanXus extends EntitiesMyPK
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new SubFlowYanXu();
	}

		///#endregion


		///#region 构造方法
	/** 
	 延续子流程集合
	*/
	public SubFlowYanXus()
	{
	}
	/** 
	 延续子流程集合.
	 
	 @param fk_node
	 * @throws Exception 
	*/
	public SubFlowYanXus(int fk_node) throws Exception
	{
		this.Retrieve(SubFlowYanXuAttr.FK_Node, fk_node, SubFlowYanXuAttr.SubFlowType, SubFlowType.YanXuFlow.getValue());
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<SubFlowYanXu> ToJavaList()
	{
		return (List<SubFlowYanXu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SubFlowYanXu> Tolist()
	{
		ArrayList<SubFlowYanXu> list = new ArrayList<SubFlowYanXu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SubFlowYanXu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}