/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcard.entity;

import com.jeeplus.modules.dmyewu.entity.DmYewu;
import com.jeeplus.modules.member.entity.Member;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐卡管理Entity
 * @author mxc
 * @version 2017-06-16
 */
public class DmCard extends DataEntity<DmCard> {
	
	private static final long serialVersionUID = 1L;
	private String cardid;		// 套餐卡号
	private String packageid;		// 所属套餐id
	private Member member;		// grid所属会员id
	private String name;		// 套餐卡名称
	private String status;		// 状态
	private String contents;		// 套餐卡描述
	private String memberName;	//所属会员名
	private String packageName;	//所属套餐名
	private int cardNum;		//批量自动生成卡数量
	private String cardOrderId;	//套餐卡订单id
	private String fxtclx;	//　分销提成类型
	private String fxtcbl;	// 分销提成比例(%)
	private String fxtcje;	// 分销提成金额
	private String numuse;	// 使用次数
	private String numshengyu;	// 剩余次数
	private String numremarks;	// 修改次数备注
	private String numadmin;	// 修改人id
	private String numtime;		// 修改时间
	private String numpscs;		// 配送次数
	private String numadminname;	// 修改人名字
	private String activetime;	// 激活时间
	private String packagedanshuang;	//套餐频次
	private DmYewu dmYewu;		// grid所属业务员id
	private String dmyewuname;	// 业务员姓名
	private String price;		// 价格.
	private String membermobile; // 注册手机号
	private String startTime;	// 订单下单开始时间
	private String endTime;		// 订单下单结束时间
	private String thisNum;		// 规定时间内使用次数
	private String tcsclx;		// 套餐时长类型
	
	
	public DmCard() {
		super();
	}

	public DmCard(String id){
		super(id);
	}

	@ExcelField(title="套餐卡号", align=2, sort=1)
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
	
	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="状态", dictType="taoCanKaZT", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@NotNull(message="所属会员不能为空")
	@ExcelField(title="所属会员", align=2, sort=3)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@ExcelField(title="所属套餐", dictType="", align=2, sort=2)
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getCardNum() {
		return cardNum;
	}

	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}

	public String getCardOrderId() {
		return cardOrderId;
	}

	public void setCardOrderId(String cardOrderId) {
		this.cardOrderId = cardOrderId;
	}

	

	@ExcelField(title="使用次数", dictType="", align=2, sort=7)
	public String getNumuse() {
		return numuse;
	}

	public void setNumuse(String numuse) {
		this.numuse = numuse;
	}

	@ExcelField(title="剩余次数", dictType="", align=2, sort=8)
	public String getNumshengyu() {
		return numshengyu;
	}

	public void setNumshengyu(String numshengyu) {
		this.numshengyu = numshengyu;
	}

	@ExcelField(title="剩余次数修改原因", dictType="", align=2, sort=9)
	public String getNumremarks() {
		return numremarks;
	}

	public void setNumremarks(String numremarks) {
		this.numremarks = numremarks;
	}

	@ExcelField(title="修改人", dictType="", align=2, sort=10)
	public String getNumadmin() {
		return numadmin;
	}

	public void setNumadmin(String numadmin) {
		this.numadmin = numadmin;
	}

	@ExcelField(title="修改时间", dictType="", align=2, sort=11)
	public String getNumtime() {
		return numtime;
	}

	public void setNumtime(String numtime) {
		this.numtime = numtime;
	}

	public String getNumpscs() {
		return numpscs;
	}

	public void setNumpscs(String numpscs) {
		this.numpscs = numpscs;
	}

	public String getNumadminname() {
		return numadminname;
	}

	public void setNumadminname(String numadminname) {
		this.numadminname = numadminname;
	}

	public String getFxtclx() {
		return fxtclx;
	}

	public void setFxtclx(String fxtclx) {
		this.fxtclx = fxtclx;
	}

	public String getFxtcbl() {
		return fxtcbl;
	}

	public void setFxtcbl(String fxtcbl) {
		this.fxtcbl = fxtcbl;
	}

	public String getFxtcje() {
		return fxtcje;
	}

	public void setFxtcje(String fxtcje) {
		this.fxtcje = fxtcje;
	}

	public String getActivetime() {
		return activetime;
	}

	public void setActivetime(String activetime) {
		this.activetime = activetime;
	}

	public String getPackagedanshuang() {
		return packagedanshuang;
	}

	public void setPackagedanshuang(String packagedanshuang) {
		this.packagedanshuang = packagedanshuang;
	}

	public DmYewu getDmYewu() {
		return dmYewu;
	}

	public void setDmYewu(DmYewu dmYewu) {
		this.dmYewu = dmYewu;
	}

	public String getDmyewuname() {
		return dmyewuname;
	}

	public void setDmyewuname(String dmyewuname) {
		this.dmyewuname = dmyewuname;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMembermobile() {
		return membermobile;
	}

	public void setMembermobile(String membermobile) {
		this.membermobile = membermobile;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@ExcelField(title="使用次数", dictType="", align=2, sort=12)
	public String getThisNum() {
		return thisNum;
	}

	public void setThisNum(String thisNum) {
		this.thisNum = thisNum;
	}

	@ExcelField(title="套餐时长类型", dictType="tcsclx", align=2, sort=5)
	public String getTcsclx() {
		return tcsclx;
	}

	public void setTcsclx(String tcsclx) {
		this.tcsclx = tcsclx;
	}
	
	
}