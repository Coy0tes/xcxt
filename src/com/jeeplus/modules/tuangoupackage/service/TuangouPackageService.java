/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackage.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.dmpackage.dao.DmPackageDao;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.tuangoupackage.entity.TuangouPackage;
import com.jeeplus.modules.tuangoupackage.dao.TuangouPackageDao;
import com.jeeplus.modules.tuangoupackage.entity.TuangouPackageGuige;
import com.jeeplus.modules.tuangoupackage.dao.TuangouPackageGuigeDao;
import com.jeeplus.modules.xuancai.entity.Xuancai;

/**
 * 团购套餐管理Service
 * @author mxc
 * @version 2017-07-20
 */
@Service
@Transactional(readOnly = true)
public class TuangouPackageService extends CrudService<TuangouPackageDao, TuangouPackage> {

	@Autowired
	private TuangouPackageGuigeDao tuangouPackageGuigeDao;
	@Autowired
	private DmPackageDao dmPackageDao;
	
	public TuangouPackage get(String id) {
		TuangouPackage tuangouPackage = super.get(id);
		tuangouPackage.setTuangouPackageGuigeList(tuangouPackageGuigeDao.findList(new TuangouPackageGuige(tuangouPackage)));
		return tuangouPackage;
	}
	
	public List<TuangouPackage> findList(TuangouPackage tuangouPackage) {
		return super.findList(tuangouPackage);
	}
	
	public Page<TuangouPackage> findPage(Page<TuangouPackage> page, TuangouPackage tuangouPackage) {
		return super.findPage(page, tuangouPackage);
	}
	
	@Transactional(readOnly = false)
	public void save(TuangouPackage tuangouPackage) {
		super.save(tuangouPackage);
		for (TuangouPackageGuige tuangouPackageGuige : tuangouPackage.getTuangouPackageGuigeList()){
			if (tuangouPackageGuige.getId() == null){
				continue;
			}
			if (TuangouPackageGuige.DEL_FLAG_NORMAL.equals(tuangouPackageGuige.getDelFlag())){
				if (StringUtils.isBlank(tuangouPackageGuige.getId())){
					tuangouPackageGuige.setMainid(tuangouPackage);
					tuangouPackageGuige.preInsert();
					tuangouPackageGuigeDao.insert(tuangouPackageGuige);
				}else{
					tuangouPackageGuige.preUpdate();
					tuangouPackageGuigeDao.update(tuangouPackageGuige);
				}
			}else{
				tuangouPackageGuigeDao.delete(tuangouPackageGuige);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TuangouPackage tuangouPackage) {
		super.delete(tuangouPackage);
		tuangouPackageGuigeDao.delete(new TuangouPackageGuige(tuangouPackage));
	}

	@Transactional(readOnly = false)
	public void isonUp(HttpServletRequest request) {
		String ids = request.getParameter("ids");		// 从页面上获得ids
		List<Map> list = JSON.parseArray(ids, Map.class);	// 将ids转换成数组
		for(Map eachid : list){
			String id = (String) eachid.get("id");		// Object java.util.Map.get(Object key)
			TuangouPackage tuangouPackage = dao.get(id);
			tuangouPackage.setIson("1");
			dao.isonUp(tuangouPackage);
		}
	}
	
	@Transactional(readOnly = false)
	public void isonDown(HttpServletRequest request) {
		String ids = request.getParameter("ids");		// 从页面上获得ids
		List<Map> list = JSON.parseArray(ids, Map.class);	// 将ids转换成数组
		for(Map eachid : list){
			String id = (String) eachid.get("id");		// Object java.util.Map.get(Object key)
			TuangouPackage tuangouPackage = dao.get(id);
			tuangouPackage.setIson("0");
			dao.isonUp(tuangouPackage);
		}
	}
	
}