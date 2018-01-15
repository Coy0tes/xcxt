package com.jeeplus.modules.goodsorder.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;

public class GoodsOrderExcel {

//	@Autowired
//	private 
	
	private String id;		//	id
	private String ddh;		// 订单号
	private String shrname;		// 收货人姓名 *
	private String lxdh;		// 收货人联系电话 *
	private String address;		// 收货地址
	private String packageName;		// 套餐名称
	private String orderList;	// 订单明细
	private String kdgs;	// 快递公司
	private String wldh;	// 物流单号
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ExcelField(title="订单号", align=2, sort=1)
	public String getDdh() {
		return ddh;
	}
	public void setDdh(String ddh) {
		this.ddh = ddh;
	}
	@ExcelField(title="收货人姓名", align=2, sort=2)
	public String getShrname() {
		return shrname;
	}
	public void setShrname(String shrname) {
		this.shrname = shrname;
	}
	@ExcelField(title="收货人联系电话", align=2, sort=3)
	public String getLxdh() {
		return lxdh;
	}
	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}
	
	@ExcelField(title="收货地址", align=2, sort=4)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="所选套餐", align=2, sort=5)
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	@ExcelField(title="订单明细", align=2, sort=6)
	public String getOrderList() {
		return orderList;
	}
	public void setOrderList(String orderList) {
		this.orderList = orderList;
	}
	
	@ExcelField(title="快递公司", align=2, sort=7)
	public String getKdgs() {
		return kdgs;
	}
	public void setKdgs(String kdgs) {
		this.kdgs = kdgs;
	}
	
	@ExcelField(title="物流单号", align=2, sort=8)
	public String getWldh() {
		return wldh;
	}
	public void setWldh(String wldh) {
		this.wldh = wldh;
	}
	
	
}
