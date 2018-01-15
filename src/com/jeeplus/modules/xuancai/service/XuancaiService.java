/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.xuancai.entity.Xuancai;
import com.jeeplus.modules.xuancai.dao.XuancaiDao;
import com.jeeplus.modules.xuancai.entity.XuancaiGoods;
import com.jeeplus.modules.xuancai.dao.XuancaiGoodsDao;

/**
 * 选菜管理Service
 * @author mxc
 * @version 2017-06-17
 */
@Service
@Transactional(readOnly = true)
public class XuancaiService extends CrudService<XuancaiDao, Xuancai> {

	@Autowired
	private XuancaiGoodsDao xuancaiGoodsDao;
	
	public Xuancai get(String id) {
		Xuancai xuancai = super.get(id);
		xuancai.setXuancaiGoodsList(xuancaiGoodsDao.findList(new XuancaiGoods(xuancai)));
		return xuancai;
	}
	
	public List<Xuancai> findList(Xuancai xuancai) {
		return super.findList(xuancai);
	}
	
	public Page<Xuancai> findPage(Page<Xuancai> page, Xuancai xuancai) {
		return super.findPage(page, xuancai);
	}
	
	@Transactional(readOnly = false)
	public void save(Xuancai xuancai) {
		super.save(xuancai);
		for (XuancaiGoods xuancaiGoods : xuancai.getXuancaiGoodsList()){
			if (xuancaiGoods.getId() == null){
				continue;
			}
			if (XuancaiGoods.DEL_FLAG_NORMAL.equals(xuancaiGoods.getDelFlag())){
				if (StringUtils.isBlank(xuancaiGoods.getId())){
					xuancaiGoods.setMainid(xuancai);
					xuancaiGoods.preInsert();
					xuancaiGoodsDao.insert(xuancaiGoods);
				}else{
					xuancaiGoods.preUpdate();
					xuancaiGoodsDao.update(xuancaiGoods);
				}
			}else{
				xuancaiGoodsDao.delete(xuancaiGoods);
			}
		}
		//如果当前选菜是推荐显示，则其他选菜的推荐显示等于否
		String tuijian = xuancai.getTuijian();
		if("1".equals(tuijian)){
			dao.updateTuijianStatus(xuancai);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Xuancai xuancai) {
		super.delete(xuancai);
		xuancaiGoodsDao.delete(new XuancaiGoods(xuancai));
	}

	@Transactional(readOnly = false)
	public List<Xuancai> findPackageList(Xuancai xuancai) {
		return dao.findPackageList(xuancai);
	}

}