/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardnum.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 添加次数Entity
 * @author mxc
 * @version 2017-06-23
 */
public class DmCardNum extends DataEntity<DmCardNum> {
	
	private static final long serialVersionUID = 1L;
	private String cardid;		// 套餐卡id 关联套餐卡
	private String cardidname;		// 套餐卡卡号
	private String num;		// 添加次数
	private String numhou;		// 添加后次数
	private String numadmin;		// 处理人id
	private String numremarks;		// 添加次数原因
	private String numtime;		// 添加时间
	private String remarkes;		// 备注信息
	private String numadminname;	// 处理人
	
	public DmCardNum() {
		super();
	}

	public DmCardNum(String id){
		super(id);
	}

	@ExcelField(title="套餐卡id 关联套餐卡", align=2, sort=1)
	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	
	@ExcelField(title="套餐卡卡号", align=2, sort=2)
	public String getCardidname() {
		return cardidname;
	}

	public void setCardidname(String cardidname) {
		this.cardidname = cardidname;
	}
	
	@ExcelField(title="添加次数", align=2, sort=3)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	@ExcelField(title="添加后次数", align=2, sort=4)
	public String getNumhou() {
		return numhou;
	}

	public void setNumhou(String numhou) {
		this.numhou = numhou;
	}
	
	@ExcelField(title="处理人", align=2, sort=5)
	public String getNumadmin() {
		return numadmin;
	}

	public void setNumadmin(String numadmin) {
		this.numadmin = numadmin;
	}
	
	@ExcelField(title="添加次数原因", align=2, sort=6)
	public String getNumremarks() {
		return numremarks;
	}

	public void setNumremarks(String numremarks) {
		this.numremarks = numremarks;
	}
	
	@ExcelField(title="添加时间", align=2, sort=7)
	public String getNumtime() {
		return numtime;
	}

	public void setNumtime(String numtime) {
		this.numtime = numtime;
	}
	
	@ExcelField(title="备注信息", align=2, sort=12)
	public String getRemarkes() {
		return remarkes;
	}

	public void setRemarkes(String remarkes) {
		this.remarkes = remarkes;
	}

	public String getNumadminname() {
		return numadminname;
	}

	public void setNumadminname(String numadminname) {
		this.numadminname = numadminname;
	}
	
	
	
}