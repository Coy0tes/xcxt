/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.xuancai.entity.XuancaiGoods;

/**
 * 选菜管理DAO接口
 * @author mxc
 * @version 2017-06-17
 */
@MyBatisDao
public interface XuancaiGoodsDao extends CrudDao<XuancaiGoods> {

	void saveSq(XuancaiGoods xuancaiGoods);

	List<XuancaiGoods> getGoodsid(XuancaiGoods xuancaiGoods);

}