/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.Goods;

/**
 * 菜品管理DAO接口
 * @author mxc
 * @version 2017-06-12
 */
@MyBatisDao
public interface GoodsDao extends CrudDao<Goods> {

	void updateCard(Goods good);

	void saveZuoFei(Goods goods);

	void fxtcSave(Goods good);

}