/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackage.entity;

import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 团购套餐管理Entity
 * @author mxc
 * @version 2017-07-20
 */
public class TuangouPackage extends DataEntity<TuangouPackage> {
	
	private static final long serialVersionUID = 1L;
	private String guizename;		// 规则名称
	private String guizecontent;		// 规则描述
	private String ison;		// 是否启用
	private String fbsj;		// 发布时间
	private String beginsj;		// 开始时间
	private String endsj;		// 结束时间
	private String istip;		// 是否设置开团提醒
	private String tiptype;		// 开团提醒方式
	private Integer minnum;		// 最低起团数量
	private Integer maxnumperson;		// 每人限购数量
	private String istimetip;		// 是否设置倒计时
	private Integer initsellnum;		// 初始销售量
	private Integer zdtgsl;		// 最大团购数量
	private String imgurl;		// 图片
	private String price;		// 价格
	private List<TuangouPackageGuige> tuangouPackageGuigeList = Lists.newArrayList();		// 子表列表
	
	public TuangouPackage() {
		super();
	}

	public TuangouPackage(String id){
		super(id);
	}

	@ExcelField(title="规则名称", align=2, sort=1)
	public String getGuizename() {
		return guizename;
	}

	public void setGuizename(String guizename) {
		this.guizename = guizename;
	}
	
	@ExcelField(title="规则描述", align=2, sort=2)
	public String getGuizecontent() {
		return guizecontent;
	}

	public void setGuizecontent(String guizecontent) {
		this.guizecontent = guizecontent;
	}
	
	@ExcelField(title="是否启用", dictType="yes_no", align=2, sort=3)
	public String getIson() {
		return ison;
	}

	public void setIson(String ison) {
		this.ison = ison;
	}
	
	@ExcelField(title="发布时间", align=2, sort=4)
	public String getFbsj() {
		return fbsj;
	}

	public void setFbsj(String fbsj) {
		this.fbsj = fbsj;
	}
	
	@ExcelField(title="开始时间", align=2, sort=5)
	public String getBeginsj() {
		return beginsj;
	}

	public void setBeginsj(String beginsj) {
		this.beginsj = beginsj;
	}
	
	@ExcelField(title="结束时间", align=2, sort=6)
	public String getEndsj() {
		return endsj;
	}

	public void setEndsj(String endsj) {
		this.endsj = endsj;
	}
	
	@ExcelField(title="是否设置开团提醒", dictType="yes_no", align=2, sort=7)
	public String getIstip() {
		return istip;
	}

	public void setIstip(String istip) {
		this.istip = istip;
	}
	
	@ExcelField(title="开团提醒方式", dictType="tiptype", align=2, sort=8)
	public String getTiptype() {
		return tiptype;
	}

	public void setTiptype(String tiptype) {
		this.tiptype = tiptype;
	}
	
	@NotNull(message="最低起团数量不能为空")
	@ExcelField(title="最低起团数量", align=2, sort=9)
	public Integer getMinnum() {
		return minnum;
	}

	public void setMinnum(Integer minnum) {
		this.minnum = minnum;
	}
	
	@NotNull(message="每人限购数量不能为空")
	@ExcelField(title="每人限购数量", align=2, sort=10)
	public Integer getMaxnumperson() {
		return maxnumperson;
	}

	public void setMaxnumperson(Integer maxnumperson) {
		this.maxnumperson = maxnumperson;
	}
	
	@ExcelField(title="是否设置倒计时", dictType="yes_no", align=2, sort=11)
	public String getIstimetip() {
		return istimetip;
	}

	public void setIstimetip(String istimetip) {
		this.istimetip = istimetip;
	}
	
	@NotNull(message="初始销售量不能为空")
	@ExcelField(title="初始销售量", align=2, sort=12)
	public Integer getInitsellnum() {
		return initsellnum;
	}

	public void setInitsellnum(Integer initsellnum) {
		this.initsellnum = initsellnum;
	}
	
	@NotNull(message="最大团购数量不能为空")
	@ExcelField(title="最大团购数量", align=2, sort=13)
	public Integer getZdtgsl() {
		return zdtgsl;
	}

	public void setZdtgsl(Integer zdtgsl) {
		this.zdtgsl = zdtgsl;
	}
	
	
	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public List<TuangouPackageGuige> getTuangouPackageGuigeList() {
		return tuangouPackageGuigeList;
	}

	public void setTuangouPackageGuigeList(List<TuangouPackageGuige> tuangouPackageGuigeList) {
		this.tuangouPackageGuigeList = tuangouPackageGuigeList;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}