/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xiugaitongzhi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.xiugaitongzhi.entity.XiugaiTongzhi;
import com.jeeplus.modules.xiugaitongzhi.dao.XiugaiTongzhiDao;

/**
 * xiugaitongzhiService
 * @author mxc
 * @version 2017-09-11
 */
@Service
@Transactional(readOnly = true)
public class XiugaiTongzhiService extends CrudService<XiugaiTongzhiDao, XiugaiTongzhi> {

	public XiugaiTongzhi get(String id) {
		return super.get(id);
	}
	
	public List<XiugaiTongzhi> findList(XiugaiTongzhi xiugaiTongzhi) {
		return super.findList(xiugaiTongzhi);
	}
	
	public Page<XiugaiTongzhi> findPage(Page<XiugaiTongzhi> page, XiugaiTongzhi xiugaiTongzhi) {
		return super.findPage(page, xiugaiTongzhi);
	}
	
	@Transactional(readOnly = false)
	public void save(XiugaiTongzhi xiugaiTongzhi) {
		super.save(xiugaiTongzhi);
	}
	
	@Transactional(readOnly = false)
	public void delete(XiugaiTongzhi xiugaiTongzhi) {
		super.delete(xiugaiTongzhi);
	}

	@Transactional(readOnly = false)
	public void saveStatus(XiugaiTongzhi xiugaiTongzhi) {
		dao.saveStatus(xiugaiTongzhi);
	}
	
	
	
	
}