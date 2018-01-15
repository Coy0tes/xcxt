/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodscategory.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goodscategory.entity.GoodsCategory;

/**
 * 菜品分类管理DAO接口
 * @author mxc
 * @version 2017-06-12
 */
@MyBatisDao
public interface GoodsCategoryDao extends CrudDao<GoodsCategory> {

	
}