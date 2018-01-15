/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.caiwuliushui.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.caiwuliushui.entity.CaiwuLiushui;
import com.jeeplus.modules.caiwuliushui.dao.CaiwuLiushuiDao;

/**
 * 财务-流水Service
 * @author mxc
 * @version 2017-05-15
 */
@Service
@Transactional(readOnly = true)
public class CaiwuLiushuiService extends CrudService<CaiwuLiushuiDao, CaiwuLiushui> {

	public CaiwuLiushui get(String id) {
		return super.get(id);
	}
	
	public List<CaiwuLiushui> findList(CaiwuLiushui caiwuLiushui) {
		return super.findList(caiwuLiushui);
	}
	
	public Page<CaiwuLiushui> findPage(Page<CaiwuLiushui> page, CaiwuLiushui caiwuLiushui) {
		return super.findPage(page, caiwuLiushui);
	}
	
	@Transactional(readOnly = false)
	public void save(CaiwuLiushui caiwuLiushui) {
		super.save(caiwuLiushui);
	}
	
	@Transactional(readOnly = false)
	public void delete(CaiwuLiushui caiwuLiushui) {
		super.delete(caiwuLiushui);
	}
	
	
	
	
}