/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardnum.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.dmcardnum.entity.DmCardNum;
import com.jeeplus.modules.dmcardnum.dao.DmCardNumDao;

/**
 * 添加次数Service
 * @author mxc
 * @version 2017-06-23
 */
@Service
@Transactional(readOnly = true)
public class DmCardNumService extends CrudService<DmCardNumDao, DmCardNum> {

	public DmCardNum get(String id) {
		return super.get(id);
	}
	
	public List<DmCardNum> findList(DmCardNum dmCardNum) {
		return super.findList(dmCardNum);
	}
	
	public Page<DmCardNum> findPage(Page<DmCardNum> page, DmCardNum dmCardNum) {
		return super.findPage(page, dmCardNum);
	}
	
	@Transactional(readOnly = false)
	public void save(DmCardNum dmCardNum) {
		super.save(dmCardNum);
	}
	
	@Transactional(readOnly = false)
	public void delete(DmCardNum dmCardNum) {
		super.delete(dmCardNum);
	}
	
	
	
	
}