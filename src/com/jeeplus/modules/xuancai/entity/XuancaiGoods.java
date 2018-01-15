/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 选菜管理Entity
 * @author mxc
 * @version 2017-06-17
 */
public class XuancaiGoods extends DataEntity<XuancaiGoods> {
	
	private static final long serialVersionUID = 1L;
	private Xuancai mainid;		// 选菜ID 父类
	private String goodsid;		// 菜品ID
	private String num;			// 选菜编号
	private String sqsj;		// 售罄时间
	
	public XuancaiGoods() {
		super();
	}

	public XuancaiGoods(String id){
		super(id);
	}

	public XuancaiGoods(Xuancai mainid){
		this.mainid = mainid;
	}

	public Xuancai getMainid() {
		return mainid;
	}

	public void setMainid(Xuancai mainid) {
		this.mainid = mainid;
	}
	
	@ExcelField(title="菜品ID", dictType="", align=2, sort=2)
	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
}