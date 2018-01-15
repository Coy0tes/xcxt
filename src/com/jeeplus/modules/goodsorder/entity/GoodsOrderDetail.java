/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodsorder.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 菜品订单Entity
 * @author mxc
 * @version 2017-06-22
 */
public class GoodsOrderDetail extends DataEntity<GoodsOrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private GoodsOrder main;		// 订单ID
	private String goodsid;		// 菜品ID
	private String goodsname;		// 产品名称
	private String goodsguige;		// 菜品规格
	private Integer num;		// 选菜数量
	
	public GoodsOrderDetail() {
		super();
	}

	public GoodsOrderDetail(String id){
		super(id);
	}

	public GoodsOrderDetail(GoodsOrder goodsOrder) {
		this.main = goodsOrder;
	}

	@ExcelField(title="订单ID", align=2, sort=1)
	public GoodsOrder getMain() {
		return main;
	}

	public void setMain(GoodsOrder main) {
		this.main = main;
	}
	
	@ExcelField(title="菜品ID", align=2, sort=2)
	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	
	@ExcelField(title="产品名称", align=2, sort=3)
	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	
	@ExcelField(title="菜品规格", align=2, sort=4)
	public String getGoodsguige() {
		return goodsguige;
	}

	public void setGoodsguige(String goodsguige) {
		this.goodsguige = goodsguige;
	}
	
	@ExcelField(title="选菜数量", align=2, sort=5)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}