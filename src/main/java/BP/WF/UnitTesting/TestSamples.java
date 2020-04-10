package BP.WF.UnitTesting;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 测试明细
*/
public class TestSamples extends EntitiesMyPK
{
	/** 
	 测试明细s
	*/
	public TestSamples()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new TestSample();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<TestSample> ToJavaList()
	{
		return (List<TestSample>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<TestSample> Tolist()
	{
		ArrayList<TestSample> list = new ArrayList<TestSample>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((TestSample)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}