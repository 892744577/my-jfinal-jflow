package BP.Frm;

import BP.DA.*;
import BP.En.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 单据可创建的工作岗位
*/
public class StationCreates extends EntitiesMM
{

		///#region 构造函数.
	/** 
	 单据可创建的工作岗位
	*/
	public StationCreates()
	{
	}
	/** 
	 单据可创建的工作岗位
	 
	 @param nodeID 单据ID
	 * @throws Exception 
	*/
	public StationCreates(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StationCreateAttr.FrmID, nodeID);
		qo.DoQuery();
	}
	/** 
	 单据可创建的工作岗位
	 
	 @param StationNo StationNo 
	 * @throws Exception 
	*/
	public StationCreates(String StationNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(StationCreateAttr.FK_Station, StationNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new StationCreate();
	}

		///#endregion 构造函数.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationCreate> ToJavaList()
	{
		return (List<StationCreate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationCreate> Tolist()
	{
		ArrayList<StationCreate> list = new ArrayList<StationCreate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationCreate)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}