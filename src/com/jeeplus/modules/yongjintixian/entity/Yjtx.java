/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yongjintixian.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 佣金提现管理Entity
 * @author mxc
 * @version 2017-06-30
 */
public class Yjtx extends DataEntity<Yjtx> {
	
	private static final long serialVersionUID = 1L;
	private String wxopenid;		// 微信OPENID
	private Double jine;		// 提现金额
	private String status;		// 领取状态
	private Date sdate;		// 申请时间
	private String clzt;		// 后台处理状态
	private String clremark;		// 处理备注
	private Date cldate;		// 处理日期
	private String bank;		// 开户银行
	private String cardid;		// 银行卡号
	private String username;		// 银行户名
	private String mobile;		// 手机号
	private String wxname;		// 微信用户名
	
	public Yjtx() {
		super();
	}

	public Yjtx(String id){
		super(id);
	}

	@ExcelField(title="微信OPENID", align=2, sort=1)
	public String getWxopenid() {
		return wxopenid;
	}

	public void setWxopenid(String wxopenid) {
		this.wxopenid = wxopenid;
	}
	
	@ExcelField(title="提现金额", align=2, sort=2)
	public Double getJine() {
		return jine;
	}

	public void setJine(Double jine) {
		this.jine = jine;
	}
	
	@ExcelField(title="领取状态", dictType="lqzt", align=2, sort=3)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="申请时间", align=2, sort=4)
	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}
	
	@ExcelField(title="后台处理状态", dictType="htclzt", align=2, sort=5)
	public String getClzt() {
		return clzt;
	}

	public void setClzt(String clzt) {
		this.clzt = clzt;
	}
	
	@ExcelField(title="处理备注", align=2, sort=6)
	public String getClremark() {
		return clremark;
	}

	public void setClremark(String clremark) {
		this.clremark = clremark;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="处理日期", align=2, sort=7)
	public Date getCldate() {
		return cldate;
	}

	public void setCldate(Date cldate) {
		this.cldate = cldate;
	}
	
	@ExcelField(title="开户银行", align=2, sort=8)
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
	
	@ExcelField(title="银行卡号", align=2, sort=9)
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
	@ExcelField(title="银行户名", align=2, sort=10)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@ExcelField(title="手机号", align=2, sort=11)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWxname() {
		return wxname;
	}

	public void setWxname(String wxname) {
		this.wxname = wxname;
	}
	
}