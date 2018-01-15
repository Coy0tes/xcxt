/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodsorder.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 菜品订单Entity
 * @author mxc
 * @version 2017-06-22
 */
public class GoodsOrder extends DataEntity<GoodsOrder> {
	
	private static final long serialVersionUID = 1L;
	private String ddh;		// 订单编号
	private String cardid;		// 所属套餐卡id
	private String memberid;		// 购买会员
	private Integer num;		// 选菜数量
	private String status;		// 订单状态
	private String shrprovince;		// 收货人所在省
	private String shrcity;		// 收货人所在市
	private String shrcounty;		// 收货人所在区
	private String shraddress;		// 收货详细地址
	private String shrname;		// 收货人姓名 *
	private String lxdh;		// 收货人联系电话 *
	private String kdgs;		// 快递公司
	private String wldh;		// 物流单号
	private String fahuotime;	// 发货时间
	private String cardName;	// 套餐卡名称
	private String memberName; 	// 所购买会员名字
	private String zuofeireason;	// 作废原因
	private	String zuofeitime;	// 作废时间
	private String principal;	// 处理人
	private String begintime;	// 导出开始时间
	private String endtime;		// 导出结束时间
	private String membermobile;	// 注册手机号
	
	
	private List<GoodsOrderDetail> goodsOrderDetailList = Lists.newArrayList();		// 子表列表
	
	public GoodsOrder() {
		super();
	}

	public GoodsOrder(String id){
		super(id);
	}

	public String getMembermobile() {
		return membermobile;
	}

	public void setMembermobile(String membermobile) {
		this.membermobile = membermobile;
	}

	@ExcelField(title="订单编号", align=2, sort=1)
	public String getDdh() {
		return ddh;
	}

	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	
	
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
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
	
	@ExcelField(title="订单状态", align=2, sort=5)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="收货人所在省", align=2, sort=6)
	public String getShrprovince() {
		return shrprovince;
	}

	public void setShrprovince(String shrprovince) {
		this.shrprovince = shrprovince;
	}
	
	@ExcelField(title="收货人所在市", align=2, sort=7)
	public String getShrcity() {
		return shrcity;
	}

	public void setShrcity(String shrcity) {
		this.shrcity = shrcity;
	}
	
	@ExcelField(title="收货人所在区", align=2, sort=8)
	public String getShrcounty() {
		return shrcounty;
	}

	public void setShrcounty(String shrcounty) {
		this.shrcounty = shrcounty;
	}
	
	@ExcelField(title="收货详细地址", align=2, sort=9)
	public String getShraddress() {
		return shraddress;
	}

	public void setShraddress(String shraddress) {
		this.shraddress = shraddress;
	}
	
	@ExcelField(title="收货人姓名", align=2, sort=10)
	public String getShrname() {
		return shrname;
	}

	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	
	@ExcelField(title="收货人联系电话", align=2, sort=11)
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
	@ExcelField(title="快递公司", align=2, sort=12)
	public String getKdgs() {
		return kdgs;
	}

	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
	
	@ExcelField(title="物流单号", align=2, sort=13)
	public String getWldh() {
		return wldh;
	}

	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	
	public List<GoodsOrderDetail> getGoodsOrderDetailList() {
		return goodsOrderDetailList;
	}

	public void setGoodsOrderDetailList(List<GoodsOrderDetail> goodsOrderDetailList) {
		this.goodsOrderDetailList = goodsOrderDetailList;
	}
	
	@ExcelField(title="套餐卡卡号", align=2, sort=2)
	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@ExcelField(title="购买会员", align=2, sort=3)
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getZuofeireason() {
		return zuofeireason;
	}

	public void setZuofeireason(String zuofeireason) {
		this.zuofeireason = zuofeireason;
	}

	public String getZuofeitime() {
		return zuofeitime;
	}

	public void setZuofeitime(String zuofeitime) {
		this.zuofeitime = zuofeitime;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getFahuotime() {
		return fahuotime;
	}

	public void setFahuotime(String fahuotime) {
		this.fahuotime = fahuotime;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
	
}