/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 会员信息Entity
 * @author mxc
 * @version 2017-06-13
 */
public class Member extends DataEntity<Member> {
	
	private static final long serialVersionUID = 1L;
	private String wxopenid;		// 微信Openid
	private String mobile;		// 手机号
	private String password;		// 密码
	private String newpassword;     // 
	private String querenPwd;		//确认密码
	private String name;		// 姓名
	private String tjrwxopenid;		// 推荐人微信Openid
	private Double yongjin;		// 佣金
	private Double yongjinytx;		// 已提现佣金
	private String headimgurl;		// 头像
	private String nickname;		// 昵称
	private String isPhone;		// 是否接收短信推动
	private String isWechat;	// 是否接收微信推送
	private String parentsid;	 //上级推荐会员id
	private String gzTime;		// 关注时间
	private String qxgzTime;	// 取消关注时间
	private String birthday;	// 生日
	private String onlyname;	// 姓名
	private String packagemember; // 套餐会员
	private String yewuid;		// 业务员id 
	private String yewuname;	// 业务员名字
	private String yewuIsShow;	// 是否显示业务员
	private String fenxiaoquanxian;	// 是否开通分销权限
	private String islook;	// 是否关注
	
	private List<MemberAddress> memberAddressList = Lists.newArrayList();		// 子表列表
	
	public Member() {
		super();
	}

	public Member(String id){
		super(id);
	}

	@ExcelField(title="微信Openid", align=2, sort=4)
	public String getWxopenid() {
		return wxopenid;
	}

	public void setWxopenid(String wxopenid) {
		this.wxopenid = wxopenid;
	}
	
	@ExcelField(title="手机号", align=2, sort=2)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getQuerenPwd() {
		return querenPwd;
	}

	public void setQuerenPwd(String querenPwd) {
		this.querenPwd = querenPwd;
	}

	@ExcelField(title="姓名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="推荐人微信Openid", align=2, sort=5)
	public String getTjrwxopenid() {
		return tjrwxopenid;
	}

	public void setTjrwxopenid(String tjrwxopenid) {
		this.tjrwxopenid = tjrwxopenid;
	}
	
	@ExcelField(title="佣金", align=2, sort=6)
	public Double getYongjin() {
		return yongjin;
	}

	public void setYongjin(Double yongjin) {
		this.yongjin = yongjin;
	}
	
	@ExcelField(title="已提现佣金", align=2, sort=7)
	public Double getYongjinytx() {
		return yongjinytx;
	}

	public void setYongjinytx(Double yongjinytx) {
		this.yongjinytx = yongjinytx;
	}
	
	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	
	@ExcelField(title="昵称", align=2, sort=3)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public List<MemberAddress> getMemberAddressList() {
		return memberAddressList;
	}

	public void setMemberAddressList(List<MemberAddress> memberAddressList) {
		this.memberAddressList = memberAddressList;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}

	public String getIsWechat() {
		return isWechat;
	}

	public void setIsWechat(String isWechat) {
		this.isWechat = isWechat;
	}

	public String getParentsid() {
		return parentsid;
	}

	public void setParentsid(String parentsid) {
		this.parentsid = parentsid;
	}

	public String getGzTime() {
		return gzTime;
	}

	public void setGzTime(String gzTime) {
		this.gzTime = gzTime;
	}

	public String getQxgzTime() {
		return qxgzTime;
	}

	public void setQxgzTime(String qxgzTime) {
		this.qxgzTime = qxgzTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getOnlyname() {
		return onlyname;
	}

	public void setOnlyname(String onlyname) {
		this.onlyname = onlyname;
	}

	public String getPackagemember() {
		return packagemember;
	}

	public void setPackagemember(String packagemember) {
		this.packagemember = packagemember;
	}

	public String getYewuid() {
		return yewuid;
	}

	public void setYewuid(String yewuid) {
		this.yewuid = yewuid;
	}

	public String getYewuname() {
		return yewuname;
	}

	public void setYewuname(String yewuname) {
		this.yewuname = yewuname;
	}

	public String getYewuIsShow() {
		return yewuIsShow;
	}

	public void setYewuIsShow(String yewuIsShow) {
		this.yewuIsShow = yewuIsShow;
	}

	public String getFenxiaoquanxian() {
		return fenxiaoquanxian;
	}

	public void setFenxiaoquanxian(String fenxiaoquanxian) {
		this.fenxiaoquanxian = fenxiaoquanxian;
	}

	public String getIslook() {
		return islook;
	}

	public void setIslook(String islook) {
		this.islook = islook;
	}
	
}