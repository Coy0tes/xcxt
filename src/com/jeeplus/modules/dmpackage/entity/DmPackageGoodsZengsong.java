/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐管理Entity
 * @author mxc
 * @version 2017-09-12
 */
public class DmPackageGoodsZengsong extends DataEntity<DmPackageGoodsZengsong> {
	
	private static final long serialVersionUID = 1L;
	private DmPackage mainid;		// 选菜ID 父类
	private String goodsid;		// 菜品ID
	private String sqsj;		// 售罄时间
	
	public DmPackageGoodsZengsong() {
		super();
	}

	public DmPackageGoodsZengsong(String id){
		super(id);
	}

	public DmPackageGoodsZengsong(DmPackage mainid){
		this.mainid = mainid;
	}

	public DmPackage getMainid() {
		return mainid;
	}

	public void setMainid(DmPackage mainid) {
		this.mainid = mainid;
	}
	
	@ExcelField(title="菜品ID", align=2, sort=2)
	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	
	@ExcelField(title="售罄时间", align=2, sort=9)
	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	
}