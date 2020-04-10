package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;
import java.math.*;

/** 
 通用从表s
*/
public class GEDtls extends EntitiesOID
{

		///#region 重载基类方法
	/** 
	 节点ID
	*/
	public String FK_MapDtl = null;

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		if (this.FK_MapDtl == null)
		{
			return new GEDtl();
		}
		return new GEDtl(this.FK_MapDtl);
	}
	/** 
	 通用从表ID
	*/
	public GEDtls()
	{
	}
	/** 
	 通用从表ID
	 
	 @param fk_mapdtl
	*/
	public GEDtls(String fk_mapdtl)
	{
		this.FK_MapDtl = fk_mapdtl;
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<GEDtl> ToJavaList()
	{
		return (List<GEDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<GEDtl> Tolist()
	{
		ArrayList<GEDtl> list = new ArrayList<GEDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((GEDtl)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}