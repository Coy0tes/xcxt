/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackage.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tuangoupackage.entity.TuangouPackage;

/**
 * 团购套餐管理DAO接口
 * @author mxc
 * @version 2017-07-20
 */
@MyBatisDao
public interface TuangouPackageDao extends CrudDao<TuangouPackage> {

	void isonUp(TuangouPackage tuangouPackage);
	void isonDown(TuangouPackage tuangouPackage);

	
}