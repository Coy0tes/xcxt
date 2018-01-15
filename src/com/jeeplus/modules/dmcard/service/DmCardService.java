/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcard.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.dmcard.dao.DmCardDao;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;
import com.jeeplus.modules.member.entity.Member;

/**
 * 套餐卡管理Service
 * @author mxc
 * @version 2017-06-16
 */
@Service
@Transactional(readOnly = true)
public class DmCardService extends CrudService<DmCardDao, DmCard> {

	@Autowired
	private GoodsOrderService goodsOrderService;
	
	private GoodsOrderDao goodsOrderDao;
	
	public DmCard get(String id) {
		return super.get(id);
	}
	
	public List<DmCard> findList(DmCard dmCard) {
		return super.findList(dmCard);
	}
	
	public Page<DmCard> findPage(Page<DmCard> page, DmCard dmCard) {
		return super.findPage(page, dmCard);
	}
	
	@Transactional(readOnly = false)
	public void save(DmCard dmCard) {
		super.save(dmCard);
	}
	
	@Transactional(readOnly = false)
	public void delete(DmCard dmCard) {
		super.delete(dmCard);
	}
	
	public Page<Member> findPageBymember(Page<Member> page, Member member) {
		member.setPage(page);
		page.setList(dao.findListBymember(member));
		return page;
	}

	@Transactional(readOnly = false)
	public void updatePlfp(DmCard dm) {
		dao.updatePlfp(dm);
		
		
	}
	@Transactional(readOnly = false)
	public void fenpeiSave(String str, Member member) {

		String[] i = str.split(",");	// 转换成数组，用逗号隔开
		for(int j = 0; j<i.length; j++){
			DmCard dm = new DmCard();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//设置日期格式--使用当前系统日期作为套餐卡号
			String cardid = i[j];
			dm.setId(cardid);	// 卡号赋值
			List<DmCard> dmCard = dao.findList(dm);		// 查出当前dmCard的数据
			dm = dmCard.get(0);	
			dm.setMember(member);	// setMember可以赋值对象，取值从member里面取
			dm.setStatus("1");		// 状态变成激活
			dm.setActivetime(df.format(new Date()));	// 添加激活时间
			dao.updatePlfp(dm);		// 保存更新
		}
	}

	@Transactional(readOnly = false)
	public List<DmCard> hasCardSave(DmCard dmCard) {
		return dao.hasCardSave(dmCard);
	}

	@Transactional(readOnly = false)
	public DmCard getCardOrderId(String cardOrderid) {
		return dao.getCardOrderId(cardOrderid);
	}

	@Transactional(readOnly = false)
	public List<DmCard> findTimesList(DmCard dmCard) {
		
		return dao.findTimesList(dmCard);
	}
	
	
	
}