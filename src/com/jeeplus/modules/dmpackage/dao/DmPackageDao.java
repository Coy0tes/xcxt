/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmpackage.entity.DmPackage;

/**
 * 套餐管理DAO接口
 * @author mxc
 * @version 2017-06-16
 */
@MyBatisDao
public interface DmPackageDao extends CrudDao<DmPackage> {

	List<DmPackage> findNoEditList(DmPackage dmPackage);

	List<DmPackage> findListCard(DmPackage dmPackage);

	void hasCardSave(DmPackage dmPackage);

}