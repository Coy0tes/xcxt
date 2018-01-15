/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 会员信息Entity
 * @author mxc
 * @version 2017-06-13
 */
public class MemberAddress extends DataEntity<MemberAddress> {
	
	private static final long serialVersionUID = 1L;
	private Member memberid;		// 会员ID 父类
	private String province;		// 省
	private String city;		// 市
	private String county;		// 区
	private String address;		// 详细地址
	private String isdefault;		// 是否默认地址
	private String shr;
	private String shrmobile;
	
	public MemberAddress() {
		super();
	}

	public MemberAddress(String id){
		super(id);
	}

	public MemberAddress(Member memberid){
		this.memberid = memberid;
	}

	public Member getMemberid() {
		return memberid;
	}

	public void setMemberid(Member memberid) {
		this.memberid = memberid;
	}
	
	@ExcelField(title="省", align=2, sort=2)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	
	@ExcelField(title="市", align=2, sort=3)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@ExcelField(title="区", align=2, sort=4)
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	
	@ExcelField(title="详细地址", align=2, sort=5)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="是否默认地址", align=2, sort=6)
	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getShr() {
		return shr;
	}

	public void setShr(String shr) {
		this.shr = shr;
	}

	public String getShrmobile() {
		return shrmobile;
	}

	public void setShrmobile(String shrmobile) {
		this.shrmobile = shrmobile;
	}
	
}