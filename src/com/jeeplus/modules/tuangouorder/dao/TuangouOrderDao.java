/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangouorder.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;

/**
 * 团购订单管理DAO接口
 * @author mxc
 * @version 2017-06-28
 */
@MyBatisDao
public interface TuangouOrderDao extends CrudDao<TuangouOrder> {

	void saveFaHuo(TuangouOrder tuangouOrder);

	void quxiaoSave(TuangouOrder tuangouOrder);

	void addRobotSave(TuangouOrder tuangouOrder);

	List<TuangouOrder> findRealList(TuangouOrder order);

	void up(TuangouOrder tuangouOrder);

}