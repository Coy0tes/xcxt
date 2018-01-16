/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sms.SmsUtils;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.dmcard.dao.DmCardDao;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmpackage.dao.DmPackageDao;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tuangoupackageorder.dao.TuangouPackageOrderDao;
import com.jeeplus.modules.tuangoupackageorder.dao.TuangouPackageOrderDetailDao;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrder;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrderDetail;

/**
 * 团购套餐订单管理Service
 * @author mxc
 * @version 2017-07-22
 */
@Service
@Transactional(readOnly = true)
public class TuangouPackageOrderService extends CrudService<TuangouPackageOrderDao, TuangouPackageOrder> {

	@Autowired
	private TuangouPackageOrderDetailDao tuangouPackageOrderDetailDao;
	
	@Autowired
	private DmCardDao dmCardDao;
	
	@Autowired
	private DmPackageDao dmPackageDao;
	
	@Autowired
	private MemberDao memberDao;
	
	public TuangouPackageOrder get(String id) {
		TuangouPackageOrder tuangouPackageOrder = super.get(id);
		if(tuangouPackageOrder != null && !tuangouPackageOrder.getId().equals("")){
			//改成从套餐卡列表中获取当前订单生成的套餐卡
			DmCard entity = new DmCard();
			entity.setCardOrderId(tuangouPackageOrder.getId());
			List<DmCard> cardlist = dmCardDao.findList(entity);
			List<TuangouPackageOrderDetail> detaillist = Lists.newArrayList();
			for(DmCard eachcard : cardlist){
				TuangouPackageOrderDetail detail = new TuangouPackageOrderDetail();
				detail.setCardName(eachcard.getCardid());
				String pkgid = eachcard.getPackageid();
				String pkgname = "";
				if(StringUtils.isNotEmpty(pkgid)){
					DmPackage pkg = dmPackageDao.get(pkgid);
					if(pkg!=null){
						pkgname = pkg.getName() + " | " + (pkg.getDanshuang().equals("0")?"双配/周":"单配/周");
						detail.setPackageName(pkgname);
					}
				}
				
				detaillist.add(detail);
			}
			
			tuangouPackageOrder.setTuangouPackageOrderDetailList(detaillist);
		}
		return tuangouPackageOrder;
	}
	
	public List<TuangouPackageOrder> findList(TuangouPackageOrder tuangouPackageOrder) {
		return super.findList(tuangouPackageOrder);
	}
	
	public Page<TuangouPackageOrder> findPage(Page<TuangouPackageOrder> page, TuangouPackageOrder tuangouPackageOrder) {
		return super.findPage(page, tuangouPackageOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(TuangouPackageOrder tuangouPackageOrder) {
		super.save(tuangouPackageOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(TuangouPackageOrder tuangouPackageOrder) {
		super.delete(tuangouPackageOrder);
		tuangouPackageOrderDetailDao.delete(new TuangouPackageOrderDetail(tuangouPackageOrder));
	}

	@Transactional(readOnly = false)
	public void zuogeiSave(TuangouPackageOrder tuangouPackageOrder) {
		User user = UserUtils.getUser();
		
		// 退款金额效验
		if(tuangouPackageOrder.getTkje()!=null){
			// 不等于空，1部分退款和2全额退款
			if(tuangouPackageOrder.getTkje().equals(tuangouPackageOrder.getSfprice())){
				tuangouPackageOrder.setQxzt("2");
			}else if(!tuangouPackageOrder.getTkje().equals(tuangouPackageOrder.getSfprice())){
				tuangouPackageOrder.setQxzt("1");
			}
		}else{	// 等于空，0已取消
			tuangouPackageOrder.setQxzt("0");
		}
		
		tuangouPackageOrder.setPrincipal(user.getId());
		tuangouPackageOrder.setStatus("4");
		
		dao.zuofeiSave(tuangouPackageOrder);
		
//		//启动一个线程推送微信消息和发送短信
//		final TuangouPackageOrder entity = tuangouPackageOrder;
//		new Thread(){
//			public void run(){
//				try {					
//					//发送短信
//					Member m = memberDao.get(entity.getMemberid());
//					if("1".equals(m.getIsPhone())){
//						StringBuffer contextString = new StringBuffer();
//						contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的订单已取消。如有疑问，请咨询400-007-0011。");
//						String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
//						System.out.println(rtn);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}.start();
		
	}

}