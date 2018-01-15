/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购套餐订单管理Entity
 * @author mxc
 * @version 2017-07-22
 */
public class TuangouPackageOrderExcal extends DataEntity<TuangouPackageOrderExcal> {
	
	private static final long serialVersionUID = 1L;
	private String tuangoupackageid;		// 团购关联id
	private String ddh;		// 订单编号
	private String memberName;	// 购买会员
	private Integer num;		// 购买数量
	private Double djprice;		// 单价
	private Double sfprice;		// 支付总价格
	private String packageName;	// 所属套餐
	private String cardid;	// 套餐卡号
	private String orderList;	// 订单明细
	
	private List<TuangouPackageOrderDetail> tuangouPackageOrderDetailList = Lists.newArrayList();		// 子表列表
	
	public TuangouPackageOrderExcal() {
		super();
	}

	public TuangouPackageOrderExcal(String id){
		super(id);
	}

	public String getTuangoupackageid() {
		return tuangoupackageid;
	}

	public void setTuangoupackageid(String tuangoupackageid) {
		this.tuangoupackageid = tuangoupackageid;
	}
	
	@ExcelField(title="订单编号", align=2, sort=1)
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}

	@ExcelField(title="购买会员", align=2, sort=2)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@ExcelField(title="购买数量", align=2, sort=3)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer integer) {
		this.num = integer;
	}

	public Double getDjprice() {
		return djprice;
	}

	public void setDjprice(Double djprice) {
		this.djprice = djprice;
	}

	@ExcelField(title="付款金额", align=2, sort=5)
	public Double getSfprice() {
		return sfprice;
	}

	public void setSfprice(Double sfprice) {
		this.sfprice = sfprice;
	}
	
	public String getCardid() {
		return cardid;
	}
	
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@ExcelField(title="订单明细", align=2, sort=6)
	public String getOrderList() {
		return orderList;
	}

	public void setOrderList(String orderList) {
		this.orderList = orderList;
	}

	public List<TuangouPackageOrderDetail> getTuangouPackageOrderDetailList() {
		return tuangouPackageOrderDetailList;
	}

	public void setTuangouPackageOrderDetailList(
			List<TuangouPackageOrderDetail> tuangouPackageOrderDetailList) {
		this.tuangouPackageOrderDetailList = tuangouPackageOrderDetailList;
	}
	
}