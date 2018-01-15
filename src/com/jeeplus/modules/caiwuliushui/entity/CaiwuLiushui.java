/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.caiwuliushui.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 财务-流水Entity
 * @author mxc
 * @version 2017-05-15
 */
public class CaiwuLiushui extends DataEntity<CaiwuLiushui> {
	
	private static final long serialVersionUID = 1L;
	private String jylx;		// 交易类型
	private String fukuanfang;		// 付款方
	private String shoukuanfang;		// 收款方
	private Double jyje;		// 交易金额
	private String orderid;		// 订单ID
	private String ordertype;	// 订单类型
	private String orderddh;	// 订单号
	
	private String fukuanren;
	private String fukuanrenmobile;
	
	public CaiwuLiushui() {
		super();
	}

	public CaiwuLiushui(String id){
		super(id);
	}

	@ExcelField(title="交易类型", align=2, sort=1)
	public String getJylx() {
		return jylx;
	}

	public void setJylx(String jylx) {
		this.jylx = jylx;
	}
	
	@ExcelField(title="付款方", align=2, sort=2)
	public String getFukuanfang() {
		return fukuanfang;
	}

	public void setFukuanfang(String fukuanfang) {
		this.fukuanfang = fukuanfang;
	}
	
	@ExcelField(title="收款方", align=2, sort=3)
	public String getShoukuanfang() {
		return shoukuanfang;
	}

	public void setShoukuanfang(String shoukuanfang) {
		this.shoukuanfang = shoukuanfang;
	}
	
	@ExcelField(title="交易金额", align=2, sort=4)
	public Double getJyje() {
		return jyje;
	}

	public void setJyje(Double jyje) {
		this.jyje = jyje;
	}
	
	@ExcelField(title="订单ID", align=2, sort=5)
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getOrderddh() {
		return orderddh;
	}

	public void setOrderddh(String orderddh) {
		this.orderddh = orderddh;
	}

	public String getFukuanren() {
		return fukuanren;
	}

	public void setFukuanren(String fukuanren) {
		this.fukuanren = fukuanren;
	}

	public String getFukuanrenmobile() {
		return fukuanrenmobile;
	}

	public void setFukuanrenmobile(String fukuanrenmobile) {
		this.fukuanrenmobile = fukuanrenmobile;
	}
	
}