/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.entity.DmPackageGoodsZengsong;
import com.jeeplus.modules.dmpackage.dao.DmPackageDao;
import com.jeeplus.modules.dmpackage.dao.DmPackageGoodsZengsongDao;

/**
 * 套餐管理Service
 * @author mxc
 * @version 2017-06-16
 */
@Service
@Transactional(readOnly = true)
public class DmPackageService extends CrudService<DmPackageDao, DmPackage> {

	@Autowired
	private DmPackageGoodsZengsongDao dmPackageGoodsZengsongDao;
	
	public DmPackage get(String id) {
		DmPackage dmPackage = super.get(id);
		if(id!=null){
			dmPackage.setDmPackageGoodsZengsongList(dmPackageGoodsZengsongDao.findList(new DmPackageGoodsZengsong(dmPackage)));
		}
		return dmPackage;
	}
	
	public List<DmPackage> findList(DmPackage dmPackage) {
		return super.findList(dmPackage);
	}
	
	
	public Page<DmPackage> findPage(Page<DmPackage> page, DmPackage dmPackage) {
		return super.findPage(page, dmPackage);
	}
	
	@Transactional(readOnly = false)
	public void save(DmPackage dmPackage) {
		super.save(dmPackage);
		for (DmPackageGoodsZengsong dmPackageGoodsZengsong : dmPackage.getDmPackageGoodsZengsongList()){
			if (dmPackageGoodsZengsong.getId() == null){
				continue;
			}
			if (DmPackageGoodsZengsong.DEL_FLAG_NORMAL.equals(dmPackageGoodsZengsong.getDelFlag())){
				if (StringUtils.isBlank(dmPackageGoodsZengsong.getId())){
					dmPackageGoodsZengsong.setMainid(dmPackage);
					dmPackageGoodsZengsong.preInsert();
					dmPackageGoodsZengsongDao.insert(dmPackageGoodsZengsong);
				}else{
					dmPackageGoodsZengsong.preUpdate();
					dmPackageGoodsZengsongDao.update(dmPackageGoodsZengsong);
				}
			}else{
				dmPackageGoodsZengsongDao.delete(dmPackageGoodsZengsong);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(DmPackage dmPackage) {
		super.delete(dmPackage);
	}
	
	@Transactional(readOnly = false)
	public List<DmPackage> findNoEditList(DmPackage dmPackage) {
		return dao.findNoEditList(dmPackage);
	}

	@Transactional(readOnly = false)
	public List<DmPackage> findListCard(DmPackage dmPackage) {
		return dao.findListCard(dmPackage);
	}

	@Transactional(readOnly = false)
	public void hasCardSave(DmPackage dmPackage) {
		dao.hasCardSave(dmPackage);
		for (DmPackageGoodsZengsong dmPackageGoodsZengsong : dmPackage.getDmPackageGoodsZengsongList()){
			if (dmPackageGoodsZengsong.getId() == null){
				continue;
			}
			if (DmPackageGoodsZengsong.DEL_FLAG_NORMAL.equals(dmPackageGoodsZengsong.getDelFlag())){
				if (StringUtils.isBlank(dmPackageGoodsZengsong.getId())){
					dmPackageGoodsZengsong.setMainid(dmPackage);
					dmPackageGoodsZengsong.preInsert();
					dmPackageGoodsZengsongDao.insert(dmPackageGoodsZengsong);
				}else{
					dmPackageGoodsZengsong.preUpdate();
					dmPackageGoodsZengsongDao.update(dmPackageGoodsZengsong);
				}
			}else{
				dmPackageGoodsZengsongDao.delete(dmPackageGoodsZengsong);
			}
		}
	}

}