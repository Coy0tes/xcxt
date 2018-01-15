/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackage.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购套餐管理Entity
 * @author mxc
 * @version 2017-07-20
 */
public class TuangouPackageGuige extends DataEntity<TuangouPackageGuige> {
	
	private static final long serialVersionUID = 1L;
	private TuangouPackage mainid;		// 团购ID 父类
	private String packageid;		// 套餐ID
	private Double price;		// 团购价格
	
	public TuangouPackageGuige() {
		super();
	}

	public TuangouPackageGuige(String id){
		super(id);
	}

	public TuangouPackageGuige(TuangouPackage mainid){
		this.mainid = mainid;
	}

	public TuangouPackage getMainid() {
		return mainid;
	}

	public void setMainid(TuangouPackage mainid) {
		this.mainid = mainid;
	}
	
	@ExcelField(title="套餐ID", align=2, sort=2)
	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	@NotNull(message="团购价格不能为空")
	@ExcelField(title="团购价格", align=2, sort=3)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}