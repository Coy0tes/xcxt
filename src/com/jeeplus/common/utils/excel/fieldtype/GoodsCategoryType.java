package com.jeeplus.common.utils.excel.fieldtype;

import com.jeeplus.modules.goodscategory.entity.GoodsCategory;


public class GoodsCategoryType {
	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((GoodsCategory)val).getName() != null){
			return ((GoodsCategory)val).getName();
		}
		return "";
	}
}
