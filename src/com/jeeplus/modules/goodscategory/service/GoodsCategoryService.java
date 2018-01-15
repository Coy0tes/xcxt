/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodscategory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goodscategory.entity.GoodsCategory;
import com.jeeplus.modules.goodscategory.dao.GoodsCategoryDao;

/**
 * 菜品分类管理Service
 * @author mxc
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class GoodsCategoryService extends CrudService<GoodsCategoryDao, GoodsCategory> {

	public GoodsCategory get(String id) {
		return super.get(id);
	}
	
	public List<GoodsCategory> findList(GoodsCategory goodsCategory) {
		return super.findList(goodsCategory);
	}
	
	public Page<GoodsCategory> findPage(Page<GoodsCategory> page, GoodsCategory goodsCategory) {
		return super.findPage(page, goodsCategory);
	}
	
	@Transactional(readOnly = false)
	public void save(GoodsCategory goodsCategory) {
		super.save(goodsCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(GoodsCategory goodsCategory) {
		super.delete(goodsCategory);
	}
	
	
	
	
}