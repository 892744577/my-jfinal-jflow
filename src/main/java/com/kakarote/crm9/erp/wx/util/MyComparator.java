package com.kakarote.crm9.erp.wx.util;

import java.util.Comparator;
import java.util.Map;

/**
 * 
 * ClassName: MyComparator
 * @Description: 自定义Entry对象的比较器。每个Entry对象可通过getKey()、getValue()获得Key或Value用于比较。换言之：我们也可以通过Entry对象实现按Key排序。
 * @author wangkaida
 * @date 2020年11月10日
 */
public class MyComparator implements Comparator<Map.Entry> {
	public int compare(Map.Entry o1, Map.Entry o2) {
		return ((Integer) o2.getValue()).compareTo((Integer) o1.getValue());
	}
}