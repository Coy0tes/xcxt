/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardorder.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sms.SmsUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;
import com.jeeplus.modules.dmcardorder.dao.DmCardOrderDao;
import com.jeeplus.modules.dmcardorder.entity.DmCardFu;
import com.jeeplus.modules.dmcardorder.dao.DmCardFuDao;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.service.SystemService;

/**
 * 套餐卡订单管理Service
 * @author mxc
 * @version 2017-06-20
 */
@Service
@Transactional(readOnly = true)
public class DmCardOrderService extends CrudService<DmCardOrderDao, DmCardOrder> {

	@Autowired
	private DmCardFuDao dmCardFuDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private MemberDao memberDao;
	
	public DmCardOrder get(String id) {
		DmCardOrder dmCardOrder = super.get(id);
		
		if(dmCardOrder != null && !dmCardOrder.getId().equals("")){
			dmCardOrder.setDmCardFuList(dmCardFuDao.findList(new DmCardFu(dmCardOrder)));
		}
		return dmCardOrder;
	}
	
	public List<DmCardOrder> findList(DmCardOrder dmCardOrder) {
		
		return super.findList(dmCardOrder);
	}
	
	public Page<DmCardOrder> findPage(Page<DmCardOrder> page, DmCardOrder dmCardOrder) {
		return super.findPage(page, dmCardOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(DmCardOrder dmCardOrder) {
		super.save(dmCardOrder);
		for (DmCardFu dmCardFu : dmCardOrder.getDmCardFuList()){
			if (dmCardFu.getId() == null){
				continue;
			}
			if (DmCardFu.DEL_FLAG_NORMAL.equals(dmCardFu.getDelFlag())){
				if (StringUtils.isBlank(dmCardFu.getId())){
					dmCardFu.setCardorderid(dmCardOrder);
					dmCardFu.preInsert();
					dmCardFuDao.insert(dmCardFu);
				}else{
					dmCardFu.preUpdate();
					dmCardFuDao.update(dmCardFu);
				}
			}else{
				dmCardFuDao.delete(dmCardFu);
			}
		}
	}
	
	/**
	 * 保存物流
	 */
	@Transactional(readOnly = false)
	public void saveWuliu(DmCardOrder dmCardOrder){
		super.saveWuliu(dmCardOrder);
		
//		//新打开一个线程，推送微信消息和发送短信
//		final DmCardOrder entity = dmCardOrder;
//		new Thread(){
//			public void run(){
//				try {
//					//推送微信消息
//					systemService.sendFahuo_cardOrder(entity);
//					
//					//发送短信
//					Member m = memberDao.get(entity.getMember());
//					if("1".equals(m.getIsPhone())){
//						StringBuffer contextString = new StringBuffer();
//						contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的商品已发货。快递单号："+entity.getWldh()+"。如有疑问，请咨询400-007-0011。");
//						String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
//						System.out.println(rtn);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
		
	}
	
	@Transactional(readOnly = false)
	public void delete(DmCardOrder dmCardOrder) {
		super.delete(dmCardOrder);
		dmCardFuDao.delete(new DmCardFu(dmCardOrder));
	}

	@Transactional(readOnly = false)
	public Page<Member> findPageBymember(Page<Member> page, Member member) {
		member.setPage(page);
		page.setList(dao.findListBymember(member));
		return page;
	}

	@Transactional(readOnly = false)
	public String piliangFahuo(String ids) {
		String msg = "批量发货处理成功";
		List<DmCardOrder> list0 = Lists.newArrayList();
		List<Map> list = JSON.parseArray(ids, Map.class);	// 得到要发货的订单id
		for(int i=0;i<list.size();i++){
			DmCardOrder dmCardOrder = new DmCardOrder(); 
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map map = list.get(i);
			
			dmCardOrder.setId((String)map.get("id"));
			dmCardOrder = dao.get(dmCardOrder);
			
			
			if(dmCardOrder.getStatus().equals("0")){	// 对发货订单状态判断，如果是0（待发货），则执行下面代码  
//				
//				// 发货后库存处理
//				GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();		// 创建子表对象
//				goodsOrderDetail.setMain(goodsOrder); 		// 得到mainid，通过mainid查出goodsOrderDetail 的 goodsid 
//				List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailDao.findList(goodsOrderDetail);		// 查询子表信息，以数组的方式将一条或多条子表信息保存
//				
//				for(GoodsOrderDetail goods : goodsOrderDetailList){
//					String dmgoodsid = goods.getGoodsid();	// 得到订单对应的商品id
//					Integer dmgoodsnum = goods.getNum();		// 得到订单商品的数量
//					
//					// 查询对应商品的信息
//					Goods good = goodsService.get(dmgoodsid);
//
//					// sql 处理冻结库存
//					good.setDjkc(dmgoodsnum);
//					goodsService.updateCard(good);
//						
//				}
//				
				// 非空处理
				if(!map.get("kdgs").equals("")){
					dmCardOrder.setKdgs((String)map.get("kdgs"));		// 从map得到快递公司
				}
				if(!map.get("wldh").equals("")){
					dmCardOrder.setWldh((String)map.get("wldh"));		// 从map得到物流单号
				}
				dmCardOrder.setStatus("1");							// 状态更新，已发货
				dmCardOrder.setFahuotime(df.format(new Date()));		// 添加发货时间
				dao.up(dmCardOrder);	
				
				list0.add(dmCardOrder);
			}
		}
		
//		//启动一个线程推送微信消息和发送短信
//		final List<DmCardOrder> orderlist = list0;
//		if(orderlist.size()>0){
//			new Thread(){
//				public void run(){
//					try {
//							for(DmCardOrder entity : orderlist){
//								//推送微信消息
//								systemService.sendFahuo_cardOrder(entity);
//								
//								//发送短信
//								Member m = memberDao.get(entity.getMember());
//								if("1".equals(m.getIsPhone())){
//									StringBuffer contextString = new StringBuffer();
//									contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的商品已发货。快递单号："+entity.getWldh()+"。如有疑问，请咨询400-007-0011。");
//									String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
//									System.out.println(rtn);
//								}
//							}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}.start();
//		}
		
		return msg;
	}


}