package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 子流程模式
*/
public enum SubFlowModel
{
	/** 
	 下级
	*/
	SubLevel,
	/** 
	 同级
	*/
	SameLevel;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static SubFlowModel forValue(int value)
	{
		return values()[value];
	}
}