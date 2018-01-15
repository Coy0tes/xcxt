/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 套餐管理Entity
 * @author mxc
 * @version 2017-06-16
 */
public class DmPackage extends DataEntity<DmPackage> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 套餐名称
	private String imgurl;		// 套餐图片
	private Integer pscs;		// 配送次数
	private Integer xcsl;		// 选菜数量
	private String status;		// 状态
	private String ison;		// 是否上架
	private Double price;		// 原价
	private Double xsprice;		// 销售价格
	private String contents;		// 套餐描述
	private String fxtclx;		// 分销提成类型
	private String fxtcbl;		// 分销提成比例
	private String fxtcje;		// 分销提成金额
	private String danshuang;	// 单双
	private String xszt;		// 销售状态
	
	private List<DmPackageGoodsZengsong> dmPackageGoodsZengsongList = Lists.newArrayList();		// 子表列表
	
	public DmPackage() {
		super();
	}

	public DmPackage(String id){
		super(id);
	}

	@ExcelField(title="套餐名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	@NotNull(message="配送次数不能为空")
	@ExcelField(title="配送次数", align=2, sort=2)
	public Integer getPscs() {
		return pscs;
	}

	public void setPscs(Integer pscs) {
		this.pscs = pscs;
	}
	
	@NotNull(message="选菜数量不能为空")
	@ExcelField(title="选菜数量(种)", align=2, sort=3)
	public Integer getXcsl() {
		return xcsl;
	}

	public void setXcsl(Integer xcsl) {
		this.xcsl = xcsl;
	}
	
	@ExcelField(title="状态", dictType="yes_no", align=2, sort=4)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="是否上架", dictType="yes_no", align=2, sort=5)
	public String getIson() {
		return ison;
	}

	public void setIson(String ison) {
		this.ison = ison;
	}
	
	@NotNull(message="原价不能为空")
	@ExcelField(title="原价(元)", align=2, sort=6)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@NotNull(message="销售价格不能为空")
	@ExcelField(title="销售价格(元)", align=2, sort=7)
	public Double getXsprice() {
		return xsprice;
	}

	public void setXsprice(Double xsprice) {
		this.xsprice = xsprice;
	}
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@ExcelField(title="分销提成类型", align=2, sort=8)
	public String getFxtclx() {
		return fxtclx;
	}

	public void setFxtclx(String fxtclx) {
		this.fxtclx = fxtclx;
	}

	@ExcelField(title="分销提成比例", align=2, sort=9)
	public String getFxtcbl() {
		return fxtcbl;
	}

	public void setFxtcbl(String fxtcbl) {
		this.fxtcbl = fxtcbl;
	}

	@ExcelField(title="分销提成金额", align=2, sort=10)
	public String getFxtcje() {
		return fxtcje;
	}

	public void setFxtcje(String fxtcje) {
		this.fxtcje = fxtcje;
	}

	public String getDanshuang() {
		return danshuang;
	}

	public void setDanshuang(String danshuang) {
		this.danshuang = danshuang;
	}

	public String getXszt() {
		return xszt;
	}

	public void setXszt(String xszt) {
		this.xszt = xszt;
	}
	
	public List<DmPackageGoodsZengsong> getDmPackageGoodsZengsongList() {
		return dmPackageGoodsZengsongList;
	}

	public void setDmPackageGoodsZengsongList(List<DmPackageGoodsZengsong> dmPackageGoodsZengsongList) {
		this.dmPackageGoodsZengsongList = dmPackageGoodsZengsongList;
	}
}