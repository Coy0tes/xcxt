/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.entity;

import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 菜品管理Entity
 * @author mxc
 * @version 2017-06-12
 */
public class Goods extends DataEntity<Goods> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 菜品名称
	private String category;		// 菜品分类
	private String description;		// 菜品简述
	private String imgurl;		// 菜品图片
	private String guige;		// 菜品规格
	private String ison;		//是否上架
	private String contents;		// 菜品描述
	private Double price;		// 原价
	private Double xsprice;		// 销售价格
	private String checkreport;		// 检验报告
	private Integer sort;		// 排序
	private String fxtclx;	//　分销提成类型
	private String fxtcbl;	// 分销提成比例(%)
	private String fxtcje;	// 分销提成金额
//	private int kcsl;	// 库存数量
//	private int djkc;	// 冻结库存
	private Integer kcsl;
	private Integer djkc;
	private String ischeckreport;	//是否上传检测报告
	
	
	public Goods() {
		super();
	}

	public Goods(String id){
		super(id);
	}

	@ExcelField(title="菜品名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="菜品分类", align=2, sort=2)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@ExcelField(title="菜品简述", align=2, sort=3)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	
	@ExcelField(title="菜品规格", align=2, sort=4)
	public String getGuige() {
		return guige;
	}

	public void setGuige(String guige) {
		this.guige = guige;
	}
	
	
	@ExcelField(title="是否上架",dictType="yes_no" , align=2, sort=5)
	public String getIson() {
		return ison;
	}

	public void setIson(String ison) {
		this.ison = ison;
	}

	@ExcelField(title="菜品描述", align=2, sort=6)
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@NotNull(message="原价不能为空")
	@ExcelField(title="原价", align=2, sort=7)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@NotNull(message="销售价格不能为空")
	@ExcelField(title="销售价格", align=2, sort=8)
	public Double getXsprice() {
		return xsprice;
	}

	public void setXsprice(Double xsprice) {
		this.xsprice = xsprice;
	}
	
	
	public String getCheckreport() {
		return checkreport;
	}

	public void setCheckreport(String checkreport) {
		this.checkreport = checkreport;
	}
	
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=11)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@ExcelField(title="分销提成类型", dictType="fxtclx" ,align=2, sort=12)
	public String getFxtclx() {
		return fxtclx;
	}

	public void setFxtclx(String fxtclx) {
		this.fxtclx = fxtclx;
	}

	@ExcelField(title="分销提成比例", align=2, sort=13)
	public String getFxtcbl() {
		return fxtcbl;
	}

	public void setFxtcbl(String fxtcbl) {
		this.fxtcbl = fxtcbl;
	}

	@ExcelField(title="分销提成金额", align=2, sort=14)
	public String getFxtcje() {
		return fxtcje;
	}

	public void setFxtcje(String fxtcje) {
		this.fxtcje = fxtcje;
	}

	@ExcelField(title="库存数量", align=2, sort=9)
	public Integer getKcsl() {
		return kcsl;
	}

	public void setKcsl(Integer kcsl) {
		this.kcsl = kcsl;
	}

	@ExcelField(title="冻结库存数量", align=2, sort=10)
	public Integer getDjkc() {
		return djkc;
	}

	public void setDjkc(Integer djkc) {
		this.djkc = djkc;
	}

	public String getIscheckreport() {
		return ischeckreport;
	}

	public void setIscheckreport(String ischeckreport) {
		this.ischeckreport = ischeckreport;
	}
	
}