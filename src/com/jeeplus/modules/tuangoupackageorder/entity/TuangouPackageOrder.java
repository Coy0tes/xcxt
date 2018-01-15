/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.entity;

import javax.validation.constraints.NotNull;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购套餐订单管理Entity
 * @author mxc
 * @version 2017-07-22
 */
public class TuangouPackageOrder extends DataEntity<TuangouPackageOrder> {
	
	private static final long serialVersionUID = 1L;
	private String tuangoupackageid;		// 团购关联id
	private String ddh;		// 订单编号
	private String memberid;		// 购买会员
	private Integer num;		// 购买总数量
	private Double sfprice;		// 支付总价格
	private String status;		// 订单状态
	private String shrprovince;		// 收货人所在省
	private String shrcity;		// 收货人所在市
	private String shrcounty;		// 收货人所在区
	private String shraddress;		// 收货详细地址
	private String shrname;		// 收货人姓名
	private String lxdh;		// 收货人联电话
	private String kdgs;		// 快递公司
	private String wldh;		// 物流单号
	private String fahuotime;		// 发货时间
	private String qxzt;		// 取消状态
	private String zuofeireason;		// 作废原因
	private String zuofeitime;		// 作废时间
	private String sftk;		// 是否退款
	private Double tkje;		// 退款金额
	private String principal;		// 作废处理人
	private String memberName;	 // 购买会员名字
	private String principal1;	
	private String tuangouOrderName;	// 团购规则名
	private String startTime;	// 订单下单开始时间
	private String endTime;		// 订单下单结束时间
	private String membermobile;
	
	private List<TuangouPackageOrderDetail> tuangouPackageOrderDetailList = Lists.newArrayList();		// 子表列表
	
	public TuangouPackageOrder() {
		super();
	}

	public TuangouPackageOrder(String id){
		super(id);
	}

	@ExcelField(title="团购关联id", align=2, sort=1)
	public String getTuangoupackageid() {
		return tuangoupackageid;
	}

	public void setTuangoupackageid(String tuangoupackageid) {
		this.tuangoupackageid = tuangoupackageid;
	}
	
	@ExcelField(title="订单编号", align=2, sort=2)
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	
	@ExcelField(title="购买会员", align=2, sort=3)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	
	@ExcelField(title="购买总数量", align=2, sort=4)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@ExcelField(title="支付总价格", align=2, sort=5)
	public Double getSfprice() {
		return sfprice;
	}

	public void setSfprice(Double sfprice) {
		this.sfprice = sfprice;
	}
	
	@ExcelField(title="订单状态", dictType="orderStatus", align=2, sort=6)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="收货人所在省", align=2, sort=7)
	public String getShrprovince() {
		return shrprovince;
	}

	public void setShrprovince(String shrprovince) {
		this.shrprovince = shrprovince;
	}
	
	@ExcelField(title="收货人所在市", align=2, sort=8)
	public String getShrcity() {
		return shrcity;
	}

	public void setShrcity(String shrcity) {
		this.shrcity = shrcity;
	}
	
	@ExcelField(title="收货人所在区", align=2, sort=9)
	public String getShrcounty() {
		return shrcounty;
	}

	public void setShrcounty(String shrcounty) {
		this.shrcounty = shrcounty;
	}
	
	@ExcelField(title="收货详细地址", align=2, sort=10)
	public String getShraddress() {
		return shraddress;
	}

	public void setShraddress(String shraddress) {
		this.shraddress = shraddress;
	}
	
	@ExcelField(title="收货人姓名", align=2, sort=11)
	public String getShrname() {
		return shrname;
	}

	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	
	@ExcelField(title="收货人联电话", align=2, sort=12)
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
	@ExcelField(title="快递公司", align=2, sort=13)
	public String getKdgs() {
		return kdgs;
	}

	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
	
	@ExcelField(title="物流单号", align=2, sort=14)
	public String getWldh() {
		return wldh;
	}

	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	
	@ExcelField(title="发货时间", align=2, sort=15)
	public String getFahuotime() {
		return fahuotime;
	}

	public void setFahuotime(String fahuotime) {
		this.fahuotime = fahuotime;
	}
	
	@ExcelField(title="取消状态", align=2, sort=16)
	public String getQxzt() {
		return qxzt;
	}

	public void setQxzt(String qxzt) {
		this.qxzt = qxzt;
	}
	
	@ExcelField(title="作废原因", align=2, sort=17)
	public String getZuofeireason() {
		return zuofeireason;
	}

	public void setZuofeireason(String zuofeireason) {
		this.zuofeireason = zuofeireason;
	}
	
	@ExcelField(title="作废时间", align=2, sort=18)
	public String getZuofeitime() {
		return zuofeitime;
	}

	public void setZuofeitime(String zuofeitime) {
		this.zuofeitime = zuofeitime;
	}
	
	@ExcelField(title="是否退款", dictType="yes_no", align=2, sort=19)
	public String getSftk() {
		return sftk;
	}

	public void setSftk(String sftk) {
		this.sftk = sftk;
	}
	
	@NotNull(message="退款金额不能为空")
	@ExcelField(title="退款金额", align=2, sort=20)
	public Double getTkje() {
		return tkje;
	}

	public void setTkje(Double tkje) {
		this.tkje = tkje;
	}
	
	public String getPrincipal() {
		return principal;
	}

	@ExcelField(title="作废处理人", align=2, sort=21)
	public String getPrincipal1() {
		return principal1;
	}

	public void setPrincipal1(String principal1) {
		this.principal1 = principal1;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getTuangouOrderName() {
		return tuangouOrderName;
	}

	public void setTuangouOrderName(String tuangouOrderName) {
		this.tuangouOrderName = tuangouOrderName;
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

	public List<TuangouPackageOrderDetail> getTuangouPackageOrderDetailList() {
		return tuangouPackageOrderDetailList;
	}

	public void setTuangouPackageOrderDetailList(List<TuangouPackageOrderDetail> tuangouPackageOrderDetailList) {
		this.tuangouPackageOrderDetailList = tuangouPackageOrderDetailList;
	}

	public String getMembermobile() {
		return membermobile;
	}

	public void setMembermobile(String membermobile) {
		this.membermobile = membermobile;
	}

	
}