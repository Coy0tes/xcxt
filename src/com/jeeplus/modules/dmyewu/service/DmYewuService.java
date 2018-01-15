/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmyewu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.dmyewu.entity.DmYewu;
import com.jeeplus.modules.dmyewu.dao.DmYewuDao;
import com.jeeplus.modules.member.entity.Member;

/**
 * dmyewuService
 * @author mxc
 * @version 2017-08-03
 */
@Service
@Transactional(readOnly = true)
public class DmYewuService extends CrudService<DmYewuDao, DmYewu> {

	public DmYewu get(String id) {
		return super.get(id);
	}
	
	public List<DmYewu> findList(DmYewu dmYewu) {
		return super.findList(dmYewu);
	}
	
	public Page<DmYewu> findPage(Page<DmYewu> page, DmYewu dmYewu) {
		return super.findPage(page, dmYewu);
	}
	
	@Transactional(readOnly = false)
	public void save(DmYewu dmYewu) {
		super.save(dmYewu);
	}
	
	@Transactional(readOnly = false)
	public void delete(DmYewu dmYewu) {
		super.delete(dmYewu);
	}

	@Transactional(readOnly = false)
	public Page<DmYewu> findPageBydmYewu(Page<DmYewu> page, DmYewu dmYewu) {
		dmYewu.setPage(page);
		page.setList(dao.findListBydmYewu(dmYewu));
		return page;
	}
	
	
	
	
}