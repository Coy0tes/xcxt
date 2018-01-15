/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.member.entity.Member;

/**
 * 会员信息DAO接口
 * @author mxc
 * @version 2017-06-13
 */
@MyBatisDao
public interface MemberDao extends CrudDao<Member> {

	void phoneSave(Member member);

	void wechatSave(Member member);

	void updatePhoneAll(Member member);

	void updateWechatAll(Member member);

	void openQuanxianUpdate(Member member);

	void closeQuanxianUpdate(Member member);

	List<Member> findOnlyMobil(Member member);

	Member getByWxopenid(Member m);

	void resetPassword(Member member);
}