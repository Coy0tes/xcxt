/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.member.entity.MemberAddress;

/**
 * 会员信息DAO接口
 * @author mxc
 * @version 2017-06-13
 */
@MyBatisDao
public interface MemberAddressDao extends CrudDao<MemberAddress> {

	
}