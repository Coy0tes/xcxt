/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangouorder.entity;

import com.jeeplus.modules.tuangou.entity.Tuangou;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购订单管理Entity
 * @author mxc
 * @version 2017-06-28
 */
public class TuangouOrder extends DataEntity<TuangouOrder> {
	
	private static final long serialVersionUID = 1L;
	private Tuangou tuangou;		// 团购关联id
	private String ddh;		// 订单编号
	private String memberid;		// 购买会员
	private Integer num;		// 选菜数量
	private Double sfprice;		// 实付价格
	private String status;		// 订单状态
	private String shrprovince;		// 收货人所在省
	private String shrcity;		// 收货人所在市
	private String shrcounty;		// 收货人所在区
	private String shraddress;		// 收货详细地址
	private String shrname;		// 收货人姓名
	private String lxdh;		// 收货人联系电话
	private String kdgs;		// 快递公司
	private String wldh;		// 物流单号
	private String zuofeireason;		// 作废原因
	private String zuofeitime;		// 作废时间
	private String sftk;		// 是否退款
	private Double tkje;		// 退款金额
	private String principal;		// 作废处理人
	private String membername;	// 购买会员名字
	private String tuangouname;	// 团购名称
	private String principal1;	// 处理人姓名
	private String address;		// 总收货地址
	private String qxzt;	// 取消订单退款状态
	private String flag;	// 虚拟人标识
	private String membermobile;
	private String startTime; // 订单下单开始时间
	private String endTime;		// 订单下单结束时间 
	private String rem;		// 菜品明细
	
	
	private int gongneng;	// 发货还是作废
	
	private String fahuotime;
	
	
	public TuangouOrder() {
		super();
	}

	public TuangouOrder(String id){
		super(id);
	}

	
	public Tuangou getTuangou() {
		return tuangou;
	}

	public void setTuangou(Tuangou tuangou) {
		this.tuangou = tuangou;
	}
	
	@ExcelField(title="订单编号", align=2, sort=1)
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	
	
	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	
	@ExcelField(title="选菜数量", align=2, sort=4)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@ExcelField(title="实付价格", align=2, sort=6)
	public Double getSfprice() {
		return sfprice;
	}

	public void setSfprice(Double sfprice) {
		this.sfprice = sfprice;
	}
	
	@ExcelField(title="订单状态", dictType="orderStatus", align=2, sort=7)
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
	
	@ExcelField(title="作废原因", align=2, sort=15)
	public String getZuofeireason() {
		return zuofeireason;
	}

	public void setZuofeireason(String zuofeireason) {
		this.zuofeireason = zuofeireason;
	}
	
	@ExcelField(title="作废时间", align=2, sort=16)
	public String getZuofeitime() {
		return zuofeitime;
	}

	public void setZuofeitime(String zuofeitime) {
		this.zuofeitime = zuofeitime;
	}
	
	@ExcelField(title="是否退款", dictType="yes_no", align=2, sort=17)
	public String getSftk() {
		return sftk;
	}

	public void setSftk(String sftk) {
		this.sftk = sftk;
	}
	
	@ExcelField(title="退款金额", align=2, sort=18)
	public Double getTkje() {
		return tkje;
	}

	public void setTkje(Double tkje) {
		this.tkje = tkje;
	}
	
	
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@ExcelField(title="购买会员", align=2, sort=3)
	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public int getGongneng() {
		return gongneng;
	}

	public void setGongneng(int gongneng) {
		this.gongneng = gongneng;
	}

	@ExcelField(title="团购规则名", align=2, sort=2)
	public String getTuangouname() {
		return tuangouname;
	}

	public void setTuangouname(String tuangouname) {
		this.tuangouname = tuangouname;
	}

	@ExcelField(title="作废处理人", align=2, sort=19)
	public String getPrincipal1() {
		return principal1;
	}

	public void setPrincipal1(String principal1) {
		this.principal1 = principal1;
	}

	@ExcelField(title="收货人地址", align=2, sort=10)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQxzt() {
		return qxzt;
	}

	public void setQxzt(String qxzt) {
		this.qxzt = qxzt;
	}

	public String getFahuotime() {
		return fahuotime;
	}

	public void setFahuotime(String fahuotime) {
		this.fahuotime = fahuotime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	@ExcelField(title="菜品明细", align=2, sort=5)
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}
	
}