/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmyewu.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * dmyewuEntity
 * @author mxc
 * @version 2017-08-03
 */
public class DmYewu extends DataEntity<DmYewu> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 业务员姓名
	private String num;		// 业务员编号
	
	public DmYewu() {
		super();
	}

	public DmYewu(String id){
		super(id);
	}

	@ExcelField(title="业务员姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="业务员编号不能为空")
	@ExcelField(title="业务员编号", align=2, sort=2)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
}