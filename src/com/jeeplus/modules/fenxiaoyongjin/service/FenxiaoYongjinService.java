/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.fenxiaoyongjin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.fenxiaoyongjin.entity.FenxiaoYongjin;
import com.jeeplus.modules.fenxiaoyongjin.dao.FenxiaoYongjinDao;

/**
 * 分销佣金管理Service
 * @author mxc
 * @version 2017-05-16
 */
@Service
@Transactional(readOnly = true)
public class FenxiaoYongjinService extends CrudService<FenxiaoYongjinDao, FenxiaoYongjin> {

	public FenxiaoYongjin get(String id) {
		return super.get(id);
	}
	
	public List<FenxiaoYongjin> findList(FenxiaoYongjin fenxiaoYongjin) {
		return super.findList(fenxiaoYongjin);
	}
	
	public Page<FenxiaoYongjin> findPage(Page<FenxiaoYongjin> page, FenxiaoYongjin fenxiaoYongjin) {
		
		return super.findPage(page, fenxiaoYongjin);
	}
	
	@Transactional(readOnly = false)
	public void save(FenxiaoYongjin fenxiaoYongjin) {
		super.save(fenxiaoYongjin);
	}
	
	@Transactional(readOnly = false)
	public void delete(FenxiaoYongjin fenxiaoYongjin) {
		super.delete(fenxiaoYongjin);
	}
	
	
	
	
}