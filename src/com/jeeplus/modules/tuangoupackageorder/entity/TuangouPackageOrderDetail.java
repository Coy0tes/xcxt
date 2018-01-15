/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购套餐订单管理Entity
 * @author mxc
 * @version 2017-07-22
 */
public class TuangouPackageOrderDetail extends DataEntity<TuangouPackageOrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private TuangouPackageOrder mainid;		// 套餐订单id 父类
	private String packageid;		// 套餐ID
	private String cardid;		// 套餐卡id
	private Integer num;		// 购买数量
	private Double djprice;		// 单价
	private Double price;		// 总价
	private String packageName;	// 套餐名称
	private String cardName;	// 套餐卡卡号
	
	public TuangouPackageOrderDetail() {
		super();
	}

	public TuangouPackageOrderDetail(String id){
		super(id);
	}

	public TuangouPackageOrderDetail(TuangouPackageOrder mainid){
		this.mainid = mainid;
	}

	public TuangouPackageOrder getMainid() {
		return mainid;
	}

	public void setMainid(TuangouPackageOrder mainid) {
		this.mainid = mainid;
	}
	
	@ExcelField(title="套餐", align=2, sort=1)
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	@ExcelField(title="套餐卡卡号", align=2, sort=2)
	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	@NotNull(message="购买数量不能为空")
	@ExcelField(title="购买数量", align=2, sort=3)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@NotNull(message="单价不能为空")
	@ExcelField(title="单价", align=2, sort=4)
	public Double getDjprice() {
		return djprice;
	}

	public void setDjprice(Double djprice) {
		this.djprice = djprice;
	}
	
	@NotNull(message="总价不能为空")
	@ExcelField(title="总价", align=2, sort=5)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}


}