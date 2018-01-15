/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.entity;

import javax.validation.constraints.NotNull;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 选菜管理Entity
 * @author mxc
 * @version 2017-06-17
 */
public class Xuancai extends DataEntity<Xuancai> {
	
	private static final long serialVersionUID = 1L;
	private String packageid;		// 所属套餐id
	private String begindate;		// 选菜开始时间
	private String enddate;		// 选菜截止时间
	private String packageName;	//所属套餐名
	private String title;		// 菜单名称
	private String ison;		// 是否上架
	private String sqsj;		// 售罄时间
	private String tuijian;		// 是否推荐显示
	private List<XuancaiGoods> xuancaiGoodsList = Lists.newArrayList();		// 子表列表
	
	public Xuancai() {
		super();
	}

	public Xuancai(String id){
		super(id);
	}

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	@NotNull(message="选菜开始时间不能为空")
	@ExcelField(title="选菜开始时间", align=2, sort=2)
	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	
	@NotNull(message="选菜截止时间不能为空")
	@ExcelField(title="选菜截止时间", align=2, sort=3)
	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	public List<XuancaiGoods> getXuancaiGoodsList() {
		return xuancaiGoodsList;
	}

	public void setXuancaiGoodsList(List<XuancaiGoods> xuancaiGoodsList) {
		this.xuancaiGoodsList = xuancaiGoodsList;
	}

	@ExcelField(title="所属套餐", dictType="", align=2, sort=1)
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIson() {
		return ison;
	}

	public void setIson(String ison) {
		this.ison = ison;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getTuijian() {
		return tuijian;
	}

	public void setTuijian(String tuijian) {
		this.tuijian = tuijian;
	}

}