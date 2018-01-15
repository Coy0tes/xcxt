/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.MemberAddress;
import com.jeeplus.modules.member.dao.MemberAddressDao;

/**
 * 会员信息Service
 * @author mxc
 * @version 2017-06-13
 */
@Service
@Transactional(readOnly = true)
public class MemberService extends CrudService<MemberDao, Member> {

	@Autowired
	private MemberAddressDao memberAddressDao;
	
	public Member get(String id) {
		Member member = super.get(id);
		member.setMemberAddressList(memberAddressDao.findList(new MemberAddress(member)));
		return member;
	}
	
	public List<Member> findList(Member member) {
		return super.findList(member);
	}
	
	public Page<Member> findPage(Page<Member> page, Member member) {
		return super.findPage(page, member);
	}
	
	@Transactional(readOnly = false)
	public void save(Member member) {
		super.save(member);
		for (MemberAddress memberAddress : member.getMemberAddressList()){
			if (memberAddress.getId() == null){
				continue;
			}
			if (MemberAddress.DEL_FLAG_NORMAL.equals(memberAddress.getDelFlag())){
				if (StringUtils.isBlank(memberAddress.getId())){
					memberAddress.setMemberid(member);
					memberAddress.preInsert();
					memberAddressDao.insert(memberAddress);
				}else{
					memberAddress.preUpdate();
					memberAddressDao.update(memberAddress);
				}
			}else{
				memberAddressDao.delete(memberAddress);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Member member) {
		super.delete(member);
		memberAddressDao.delete(new MemberAddress(member));
	}

	@Transactional(readOnly = false)
	public void phoneSave(Member member) {
		dao.phoneSave(member);
	}

	@Transactional(readOnly = false)
	public void wechatSave(Member member) {
		dao.wechatSave(member);
	}

	@Transactional(readOnly = false)
	public void updatePhoneAll(Member member) {
		dao.updatePhoneAll(member);
	}
	
	@Transactional(readOnly = false)
	public void updateWechatAll(Member member) {
		dao.updateWechatAll(member);
	}

	@Transactional(readOnly = false)
	public void openQuanxianUpdate(String ids) {
		String idArray[] =ids.split(",");
		for(String id:idArray){
			Member member = new Member();
			member.setId(id);
			member.setFenxiaoquanxian("1");
			dao.openQuanxianUpdate(member);
		}
	}

	@Transactional(readOnly = false)
	public void closeQuanxian(String ids) {
		String idArray[] =ids.split(",");
		for(String id:idArray){
			Member member = new Member();
			member.setId(id);
			member.setFenxiaoquanxian("0");
			dao.closeQuanxianUpdate(member);
		}
	}

	public List<Member> findOnlyMobil(Member member) {
		return dao.findOnlyMobil(member);
	}

	@Transactional(readOnly = false)
	public void resetPassword(Member member) {
		dao.resetPassword(member);
	}

}