/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.fenxiaoyongjin.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 分销佣金管理Entity
 * @author mxc
 * @version 2017-05-16
 */
public class FenxiaoYongjin extends DataEntity<FenxiaoYongjin> {
	
	private static final long serialVersionUID = 1L;
	private String wxopenid;		// 提成人微信Openid
	private String name;		// 提成人姓名
	private String mobile;		// 提成人手机号
	private Double jine;		// 提成人佣金
	private String layer;		// 佣金层级
	private String orderid;		// 来源订单
	private String xfzwxopenid;		// 客户微信Openid
	private String xfzname;		// 客户姓名
	private Double xfjine;		// 消费金额
	private String ffzt;		// 发放状态
	private String headimgurl;
	private String nickname;
	private String ordertype;	// 订单类型
	private String orderddh;	// 订单号
	
	public FenxiaoYongjin() {
		super();
	}

	public FenxiaoYongjin(String id){
		super(id);
	}

	@ExcelField(title="提成人微信Openid", align=2, sort=1)
	public String getWxopenid() {
		return wxopenid;
	}

	public void setWxopenid(String wxopenid) {
		this.wxopenid = wxopenid;
	}
	
	@ExcelField(title="提成人姓名", align=2, sort=2)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="提成人手机号", align=2, sort=3)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@ExcelField(title="提成人佣金", align=2, sort=4)
	public Double getJine() {
		return jine;
	}

	public void setJine(Double jine) {
		this.jine = jine;
	}
	
	@ExcelField(title="佣金层级", align=2, sort=5)
	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}
	
	@ExcelField(title="来源订单", align=2, sort=6)
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	
	@ExcelField(title="客户微信Openid", align=2, sort=8)
	public String getXfzwxopenid() {
		return xfzwxopenid;
	}

	public void setXfzwxopenid(String xfzwxopenid) {
		this.xfzwxopenid = xfzwxopenid;
	}
	
	@ExcelField(title="客户姓名", align=2, sort=9)
	public String getXfzname() {
		return xfzname;
	}

	public void setXfzname(String xfzname) {
		this.xfzname = xfzname;
	}
	
	@ExcelField(title="消费金额", align=2, sort=10)
	public Double getXfjine() {
		return xfjine;
	}

	public void setXfjine(Double xfjine) {
		this.xfjine = xfjine;
	}
	
	@ExcelField(title="发放状态", align=2, sort=11)
	public String getFfzt() {
		return ffzt;
	}

	public void setFfzt(String ffzt) {
		this.ffzt = ffzt;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@ExcelField(title="发放状态", align=2, sort=7)
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
	
}