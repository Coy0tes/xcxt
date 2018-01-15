/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.dao.GoodsDao;
import com.jeeplus.modules.tuangou.entity.Tuangou;

/**
 * 菜品管理Service
 * @author mxc
 * @version 2017-06-12
 */
@Service
@Transactional(readOnly = true)
public class GoodsService extends CrudService<GoodsDao, Goods> {

	
	public Goods get(String id) {
		return super.get(id);
	}
	
	public List<Goods> findList(Goods goods) {
		return super.findList(goods);
	}
	
	public Page<Goods> findPage(Page<Goods> page, Goods goods) {
		return super.findPage(page, goods);
	}
	
	@Transactional(readOnly = false)
	public void save(Goods goods) {
		super.save(goods);
	}
	
	
	
	@Transactional(readOnly = false)
	public void delete(Goods goods) {
		super.delete(goods);
	}

	/**
	 * 批量上架Service
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public void plUp(String ids) {

		String idArray[] =ids.split(",");
		for(String id : idArray){
			Goods goods = dao.get(id);
 			goods.setIson("1");
			super.save(goods);
		}
	}

	/**
	 * 批量下架
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public void plDown(String ids) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			Goods goods = dao.get(id);
			goods.setIson("0");
			super.save(goods);
		}		
	}

	@Transactional(readOnly = false)
	public void updateCard(Goods good) {
		dao.updateCard(good);
	}

	@Transactional(readOnly = false)
	public void saveZuoFei(Goods goods) {
		dao.saveZuoFei(goods);
	}
	
	@Transactional(readOnly = false)
	public String fxtcSave(String ids,Goods goods) {
		String msg = "批量保存成功！";
		Goods good = new Goods();
		String idArray[] =ids.split(",");
		// 根据套餐卡id查询出每条套餐卡的信息
		for(String id : idArray){
			good = dao.get(id);
			good.setFxtclx(goods.getFxtclx());
			good.setFxtcbl(goods.getFxtcbl());
			good.setFxtcje(goods.getFxtcje());
			dao.fxtcSave(good);
		}
		return msg;

	}

}