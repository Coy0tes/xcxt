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
public class TuangouOrderExcal extends DataEntity<TuangouOrderExcal> {
	
	private static final long serialVersionUID = 1L;
	private Tuangou tuangou;		// 团购关联id
	private String ddh;		// 订单编号
	private Integer num;		// 选菜数量
	private Double sfprice;		// 实付价格
	private String shrname;		// 收货人姓名
	private String lxdh;		// 收货人联系电话
	private String address;		// 收货人所在地址
	private String kdgs;		// 快递公司
	private String wldh;		// 物流单号
	private String membername;	// 购买会员名字
	private String tuangouname;	// 团购名称
	private String flag;	// 虚拟人标识
	private String startTime;	// 订单下单开始时间
	private String endTime;		// 订单下单结束时间
	private String rem;			// 订单明细
	
	
	public TuangouOrderExcal() {
		super();
	}

	public TuangouOrderExcal(String id){
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
	
	@ExcelField(title="选菜数量", align=2, sort=3)
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	@ExcelField(title="实付价格", align=2, sort=4)
	public Double getSfprice() {
		return sfprice;
	}

	public void setSfprice(Double sfprice) {
		this.sfprice = sfprice;
	}
	
	
	@ExcelField(title="收货人姓名", align=2, sort=6)
	public String getShrname() {
		return shrname;
	}

	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	
	@ExcelField(title="收货人联系电话", align=2, sort=7)
	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
	@ExcelField(title="快递公司", align=2, sort=8)
	public String getKdgs() {
		return kdgs;
	}

	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
	
	@ExcelField(title="物流单号", align=2, sort=9)
	public String getWldh() {
		return wldh;
	}

	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	
	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	@ExcelField(title="团购规则名", align=2, sort=2)
	public String getTuangouname() {
		return tuangouname;
	}

	public void setTuangouname(String tuangouname) {
		this.tuangouname = tuangouname;
	}

	@ExcelField(title="收货地址", align=2, sort=7)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	@ExcelField(title="订单明细", align=2, sort=5)
	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}
	
}