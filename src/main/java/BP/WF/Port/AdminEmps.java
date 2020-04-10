package BP.WF.Port;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

/** 
 管理员s 
*/
public class AdminEmps extends EntitiesNoName
{

		///#region 构造
	/** 
	 管理员s
	*/
	public AdminEmps()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getNewEntity()
	{
		return new AdminEmp();
	}

	@Override
	public int RetrieveAll() throws Exception
	{
		return super.RetrieveAll("FK_Dept","Idx");
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<AdminEmp> ToJavaList()
	{
		return (List<AdminEmp>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<AdminEmp> Tolist()
	{
		ArrayList<AdminEmp> list = new ArrayList<AdminEmp>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((AdminEmp)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}