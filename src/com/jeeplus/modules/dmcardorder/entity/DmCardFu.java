/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardorder.entity;

import com.jeeplus.modules.member.entity.Member;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐卡订单管理Entity
 * @author mxc
 * @version 2017-06-20
 */
public class DmCardFu extends DataEntity<DmCardFu> {
	
	private static final long serialVersionUID = 1L;
	private DmCardOrder cardorderid;		// 套餐卡订单id 表外键 父类
	private String cardid;		// 套餐卡号
	private String packageid;		// 所属套餐
	private Member memberid;		// 所属会员
	private String name;		// 套餐名称
	private String status;		// 状态
	private String contents;		// 套餐卡描述
	private String packageName;	//套餐名称
	private String memberName;	//会员名称
	
	public DmCardFu() {
		super();
	}

	public DmCardFu(String id){
		super(id);
	}

	public DmCardFu(DmCardOrder cardorderid){
		this.cardorderid = cardorderid;
	}

	public DmCardOrder getCardorderid() {
		return cardorderid;
	}

	public void setCardorderid(DmCardOrder cardorderid) {
		this.cardorderid = cardorderid;
	}
	
	@ExcelField(title="套餐卡号", align=2, sort=2)
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
	@ExcelField(title="所属套餐", align=2, sort=3)
	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	@ExcelField(title="所属会员", align=2, sort=4)
	public Member getMemberid() {
		return memberid;
	}

	public void setMemberid(Member memberid) {
		this.memberid = memberid;
	}
	
	@ExcelField(title="套餐名称", align=2, sort=5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="状态", dictType="status", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="套餐卡描述", align=2, sort=7)
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	
	
}