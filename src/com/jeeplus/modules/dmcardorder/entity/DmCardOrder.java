/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardorder.entity;

import javax.validation.constraints.NotNull;
import com.jeeplus.modules.member.entity.Member;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐卡订单管理Entity
 * @author mxc
 * @version 2017-06-20
 */
public class DmCardOrder extends DataEntity<DmCardOrder> {
	
	private static final long serialVersionUID = 1L;
	private String ddh;		// 订单编号
	private Integer num;		// 购卡数量
	private String gclx;		// 购卡类型
	private String packageid;		// 所属套餐
	private Member member;		// 购卡会员
	private String status;		// 订单状态
	private String shrprovince;		// 收货人所在省
	private String shrcity;		// 收货人所在市
	private String shrcounty;		// 收货人所在区
	private String shraddress;		// 收货详细地址
	private String address;		// 收货地址
	private String dizhi;	// 收货总地址：省+市+区+详细地址
	private String shrname;		// 收货人姓名
	private String lxdh;		// 收货人联系电话
	private String kdgs;		// 快递公司
	private String wldh;		// 物流单号
	private String packageName;	//套餐名称
	private String memberName;	//会员名称
	private String startTime;	// 订单下单开始时间
	private String endTime;		// 订单下单结束时间
	private String cardName;	// 套餐卡卡号
	private String fahuotime;	// 发货时间
	private String mobile;	// 会员手机号
	private String createTime;	// 下单时间
	
	private List<DmCardFu> dmCardFuList = Lists.newArrayList();		// 子表列表
	
	public DmCardOrder() {
		super();
	}

	public DmCardOrder(String id){
		super(id);
	}

	@ExcelField(title="订单编号", align=2, sort=1)
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	
	@NotNull(message="购卡数量不能为空")
	@ExcelField(title="购卡数量", align=2, sort=2)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@ExcelField(title="购卡类型", dictType="cardType", align=2, sort=3)
	public String getGclx() {
		return gclx;
	}

	public void setGclx(String gclx) {
		this.gclx = gclx;
	}
	
	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	@NotNull(message="购卡会员不能为空")
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	@ExcelField(title="订单状态", dictType="orderStatus", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getShrprovince() {
		return shrprovince;
	}

	public void setShrprovince(String shrprovince) {
		this.shrprovince = shrprovince;
	}
	
	public String getShrcity() {
		return shrcity;
	}

	public void setShrcity(String shrcity) {
		this.shrcity = shrcity;
	}
	
	public String getShrcounty() {
		return shrcounty;
	}

	public void setShrcounty(String shrcounty) {
		this.shrcounty = shrcounty;
	}
	
	public String getShraddress() {
		return shraddress;
	}

	public void setShraddress(String shraddress) {
		this.shraddress = shraddress;
	}
	
	@ExcelField(title="收货人姓名", align=2, sort=8)
	public String getShrname() {
		return shrname;
	}

	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	
	@ExcelField(title="收货人联系电话", align=2, sort=9)
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
	@ExcelField(title="快递公司", align=2, sort=11)
	public String getKdgs() {
		return kdgs;
	}

	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
	
	@ExcelField(title="物流单号", align=2, sort=12)
	public String getWldh() {
		return wldh;
	}

	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	
	public List<DmCardFu> getDmCardFuList() {
		return dmCardFuList;
	}

	public void setDmCardFuList(List<DmCardFu> dmCardFuList) {
		this.dmCardFuList = dmCardFuList;
	}

	@ExcelField(title="所属套餐", dictType="", align=2, sort=5)
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@ExcelField(title="购卡会员", align=2, sort=6)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ExcelField(title="套餐卡号", align=2, sort=4)
	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getFahuotime() {
		return fahuotime;
	}

	public void setFahuotime(String fahuotime) {
		this.fahuotime = fahuotime;
	}

	@ExcelField(title="注册手机号", align=2, sort=7)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="收货地址", align=2, sort=10)
	public String getDizhi() {
		return dizhi;
	}

	public void setDizhi(String dizhi) {
		this.dizhi = dizhi;
	}

	@ExcelField(title="下单时间", align=2, sort=13)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}