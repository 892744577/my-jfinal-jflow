package BP.Sys;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 部门岗位对应 
*/
public class EnVerDtls extends EntitiesMyPK
{

		///#region 构造

	public EnVerDtls()
	{
	}

	public EnVerDtls(String enVerPK) throws Exception
	{
		this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK);
	}

	public EnVerDtls(String enVerPK, int ver) throws Exception
	{
		this.Retrieve(EnVerDtlAttr.EnVerPK, enVerPK, EnVerDtlAttr.EnVer, ver);
	}

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new EnVerDtl();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<EnVerDtl> ToJavaList()
	{
		return (List<EnVerDtl>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnVerDtl> Tolist()
	{
		ArrayList<EnVerDtl> list = new ArrayList<EnVerDtl>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnVerDtl)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}