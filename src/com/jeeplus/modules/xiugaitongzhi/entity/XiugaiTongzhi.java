/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xiugaitongzhi.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * xiugaitongzhiEntity
 * @author mxc
 * @version 2017-09-11
 */
public class XiugaiTongzhi extends DataEntity<XiugaiTongzhi> {
	
	private static final long serialVersionUID = 1L;
	private String wxopenid;		// 微信openid
	private String mobileold;		// 手机号变更前
	private String mobilenew;		// 手机号变更后
	private String status;		// 是否已阅
	private String remarkes;		// 备注信息
	
	public XiugaiTongzhi() {
		super();
	}

	public XiugaiTongzhi(String id){
		super(id);
	}

	@ExcelField(title="微信openid", align=2, sort=1)
	public String getWxopenid() {
		return wxopenid;
	}

	public void setWxopenid(String wxopenid) {
		this.wxopenid = wxopenid;
	}
	
	@ExcelField(title="手机号变更前", align=2, sort=2)
	public String getMobileold() {
		return mobileold;
	}

	public void setMobileold(String mobileold) {
		this.mobileold = mobileold;
	}
	
	@ExcelField(title="手机号变更后", align=2, sort=3)
	public String getMobilenew() {
		return mobilenew;
	}

	public void setMobilenew(String mobilenew) {
		this.mobilenew = mobilenew;
	}
	
	@ExcelField(title="是否已阅", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="备注信息", align=2, sort=9)
	public String getRemarkes() {
		return remarkes;
	}

	public void setRemarkes(String remarkes) {
		this.remarkes = remarkes;
	}
	
}