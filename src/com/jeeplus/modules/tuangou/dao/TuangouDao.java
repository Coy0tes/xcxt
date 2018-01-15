/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangou.dao;

import com.jeeplus.modules.goods.entity.Goods;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;

/**
 * 团购管理DAO接口
 * @author mxc
 * @version 2017-06-27
 */
@MyBatisDao
public interface TuangouDao extends CrudDao<Tuangou> {

	public List<Goods> findListBygoods(Goods goods);

	public void quxiaoSave(TuangouOrder tuangouOrder);

	public List<Tuangou> findTuangouList();
	
}