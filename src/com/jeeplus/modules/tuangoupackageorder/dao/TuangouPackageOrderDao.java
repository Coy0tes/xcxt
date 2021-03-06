/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrder;

/**
 * 团购套餐订单管理DAO接口
 * @author mxc
 * @version 2017-07-22
 */
@MyBatisDao
public interface TuangouPackageOrderDao extends CrudDao<TuangouPackageOrder> {

	void zuofeiSave(TuangouPackageOrder tuangouPackageOrder);

}