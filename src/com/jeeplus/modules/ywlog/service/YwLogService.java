/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.ywlog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.dao.YwLogDao;

/**
 * 操作日志Service
 * @author mxc
 * @version 2017-07-04
 */
@Service
@Transactional(readOnly = true)
public class YwLogService extends CrudService<YwLogDao, YwLog> {

	public YwLog get(String id) {
		return super.get(id);
	}
	
	public List<YwLog> findList(YwLog ywLog) {
		return super.findList(ywLog);
	}
	
	public Page<YwLog> findPage(Page<YwLog> page, YwLog ywLog) {
		return super.findPage(page, ywLog);
	}
	
	@Transactional(readOnly = false)
	public void save(YwLog ywLog) {
		super.save(ywLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(YwLog ywLog) {
		super.delete(ywLog);
	}
	
	
	
	
}