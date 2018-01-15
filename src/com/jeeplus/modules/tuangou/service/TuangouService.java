/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangou.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangou.dao.TuangouDao;

/**
 * 团购管理Service
 * @author mxc
 * @version 2017-06-27
 */
@Service
@Transactional(readOnly = true)
public class TuangouService extends CrudService<TuangouDao, Tuangou> {

	
	public Tuangou get(String id) {
		Tuangou tuangou = super.get(id);
//		tuangou.setTuangouJietiList(tuangouJietiDao.findList(new TuangouJieti(tuangou)));
		return tuangou;
	}
	
	public List<Tuangou> findList(Tuangou tuangou) {
		return super.findList(tuangou);
	}
	
	public Page<Tuangou> findPage(Page<Tuangou> page, Tuangou tuangou) {
		return super.findPage(page, tuangou);
	}
	
	@Transactional(readOnly = false)
	public void save(Tuangou tuangou) {
		super.save(tuangou);
	}
	
	@Transactional(readOnly = false)
	public void delete(Tuangou tuangou) {
		super.delete(tuangou);
	}

	@Transactional(readOnly = false)
	public Page<Goods> findPageBygoods(Page<Goods> page, Goods goods) {
		goods.setPage(page);
		goods.setIson("1");
		page.setList(dao.findListBygoods(goods));
		return page;
	}

}