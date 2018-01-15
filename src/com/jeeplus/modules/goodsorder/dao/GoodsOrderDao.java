/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodsorder.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;

/**
 * 菜品订单DAO接口
 * @author mxc
 * @version 2017-06-22
 */
@MyBatisDao
public interface GoodsOrderDao extends CrudDao<GoodsOrder> {

	void up(GoodsOrder goodsOrder);

	List<GoodsOrder> findListToday(GoodsOrder goodsOrder);

	void saveFa(GoodsOrder goodsOrder);

	void saveZuoFei(GoodsOrder goodsOrder);

	List<GoodsOrder> findTimesList(GoodsOrder goodsOrder);

}
