/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcard.dao;

import com.jeeplus.modules.member.entity.Member;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;

/**
 * 套餐卡管理DAO接口
 * @author mxc
 * @version 2017-06-16
 */
@MyBatisDao
public interface DmCardDao extends CrudDao<DmCard> {

	public List<Member> findListBymember(Member member);

	public void updatePlfp(DmCard dm);

	public void saveZuoFei(DmCard dmCard);

	public List<DmCard> hasCardSave(DmCard dmCard);

	public DmCard getCardOrderId(String cardOrderid);

	public List<DmCard> findTimesList(DmCard dmCard);
	
	
}