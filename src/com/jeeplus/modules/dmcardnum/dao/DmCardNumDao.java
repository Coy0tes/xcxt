/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardnum.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmcardnum.entity.DmCardNum;

/**
 * 添加次数DAO接口
 * @author mxc
 * @version 2017-06-23
 */
@MyBatisDao
public interface DmCardNumDao extends CrudDao<DmCardNum> {

	
}