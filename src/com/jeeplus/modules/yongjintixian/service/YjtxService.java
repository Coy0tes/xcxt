/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yongjintixian.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.yongjintixian.entity.Yjtx;
import com.jeeplus.modules.yongjintixian.dao.YjtxDao;

/**
 * 佣金提现管理Service
 * @author mxc
 * @version 2017-06-30
 */
@Service
@Transactional(readOnly = true)
public class YjtxService extends CrudService<YjtxDao, Yjtx> {

	public Yjtx get(String id) {
		return super.get(id);
	}
	
	public List<Yjtx> findList(Yjtx yjtx) {
		return super.findList(yjtx);
	}
	
	public Page<Yjtx> findPage(Page<Yjtx> page, Yjtx yjtx) {
		return super.findPage(page, yjtx);
	}
	
	@Transactional(readOnly = false)
	public void save(Yjtx yjtx) {
		super.save(yjtx);
	}
	
	@Transactional(readOnly = false)
	public void delete(Yjtx yjtx) {
		super.delete(yjtx);
	}
	
	
	
	
}