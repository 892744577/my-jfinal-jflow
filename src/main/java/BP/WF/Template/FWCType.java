package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 类型
*/
public enum FWCType
{
	/** 
	 审核组件
	*/
	Check,
	/** 
	 日志组件
	*/
	DailyLog,
	/** 
	 周报
	*/
	WeekLog,
	/** 
	 月报
	*/
	MonthLog;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FWCType forValue(int value)
	{
		return values()[value];
	}
}