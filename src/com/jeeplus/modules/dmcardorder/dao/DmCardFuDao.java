/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardorder.dao;

import com.jeeplus.modules.member.entity.Member;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmcardorder.entity.DmCardFu;

/**
 * 套餐卡订单管理DAO接口
 * @author mxc
 * @version 2017-06-20
 */
@MyBatisDao
public interface DmCardFuDao extends CrudDao<DmCardFu> {

	public List<Member> findListBymemberid(Member memberid);
	
}