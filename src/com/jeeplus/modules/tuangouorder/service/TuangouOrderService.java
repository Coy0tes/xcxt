/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangouorder.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sms.SmsUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.goods.dao.GoodsDao;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tuangou.dao.TuangouDao;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangou.service.TuangouService;
import com.jeeplus.modules.tuangouorder.dao.TuangouOrderDao;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;
import com.job.thread.WxNickImgUtils;

/**
 * 团购订单管理Service
 * @author mxc
 * @version 2017-06-28
 */
@Service
@Transactional(readOnly = true)
public class TuangouOrderService extends CrudService<TuangouOrderDao, TuangouOrder> {
	
	@Autowired
	private TuangouDao tuangouDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private TuangouService tuangouService;
	@Autowired
	private GoodsService goodsService;
	
	public TuangouOrder get(String id) {
		TuangouOrder tuangouOrder = super.get(id);
		return tuangouOrder;
	}
	
	public List<TuangouOrder> findList(TuangouOrder tuangouOrder) {
		return super.findList(tuangouOrder);
	}
	
	public Page<TuangouOrder> findPage(Page<TuangouOrder> page, TuangouOrder tuangouOrder) {
		return super.findPage(page, tuangouOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(TuangouOrder tuangouOrder) {
		super.save(tuangouOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(TuangouOrder tuangouOrder) {
		super.delete(tuangouOrder);
	}

	@Transactional(readOnly = false)
	public String saveFaHuo(TuangouOrder tuangouOrder) throws Exception {
		String msg = "发货成功！";
		//更新冻结库存
		Tuangou tuangou = tuangouOrder.getTuangou();
		tuangou = tuangouDao.get(tuangou);
		String goodsid = tuangou.getGoods().getId();
		Goods goods = new Goods(goodsid);
		goods.setDjkc(tuangouOrder.getNum());
		goodsDao.updateCard(goods);
		
		//更新物流信息
		tuangouOrder.setStatus("1");
		dao.saveFaHuo(tuangouOrder);
		
		
		//启动一个线程推送微信消息和发送短信
		final TuangouOrder entity = tuangouOrder;
		new Thread() {
			public void run() {
				try {
					//推送微信消息
					systemService.sendFahuo_tuangouOrder(entity);
					
					//发送短信
					Member m = memberDao.get(entity.getMemberid());
					if("1".equals(m.getIsPhone())){
						StringBuffer contextString = new StringBuffer();
						contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的商品已发货。快递单号："+entity.getWldh()+"。如有疑问，请咨询400-007-0011。");
						String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
						System.out.println(rtn);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}.start();
		
		return msg;
	}
	
	/**
	 * 团购订单
	 * 待付款：库存+，冻结库存-	
	 * 待发货：库存+，冻结库存-  退款金额状态：已取消、部分退款、全额退款
	 * @param tuangouOrder
	 * @return
	 */
	@Transactional(readOnly = false)
	public String quxiaoSave(TuangouOrder tuangouOrder) throws Exception {

		String msg = "订单取消成功！！";
		// 团购订单中有团购id，根据团购id找到goodsid
		if(tuangouOrder.getStatus().equals("3")){
			
		}
		if(tuangouOrder.getStatus().equals("0")){
			// 待发货状态
			
		}
		//----------------------------------- 作废订单处理 END--------------------------------
		// -------------------------库存+1，冻结库存-1 BEGIN-----------------------
		Tuangou tuangou = new Tuangou();
		Goods goods = new Goods();
		
		// 得到团购id
		tuangou.setId(tuangouOrder.getTuangou().getId());
  		tuangou = tuangouDao.get(tuangou);
		// 得到菜品id
		goods.setId(tuangou.getGoods().getId());
		goods = goodsDao.get(goods);
		goods.setKcsl(tuangouOrder.getNum());
		goods.setDjkc(tuangouOrder.getNum());
		goodsDao.saveZuoFei(goods);
		//-------------------------库存+1，冻结库存-1 END-----------------------
		
		//************************退款处理 BEGIN**************************

//		 如果团购退款金额为空，则直接显示已取消；如果金额相等，则显示全额退款；如果退款金额少于实付金额，显示部分退款
		
		if(tuangouOrder.getTkje()!=null){
		
		//-1表示小于，0是等于，1是大于
			BigDecimal tkje = new BigDecimal(tuangouOrder.getTkje());
			BigDecimal sfprice = new BigDecimal(tuangouOrder.getSfprice());
			if(tkje.compareTo(sfprice)==0){
				tuangouOrder.setQxzt("2");
			}else{
				tuangouOrder.setQxzt("1");
			}
		}else{
			tuangouOrder.setQxzt("0");
		}
		
		//************************退款处理 END****************************
		
		// 得到当前用户
		User user = UserUtils.getUser();
		// 得到当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//设置日期格式
		
		//赋值 用户id、 当前操作时间、 作废原因 、退款金额 、是否退款、 订单状态
		tuangouOrder.setPrincipal(user.getId());
		tuangouOrder.setZuofeitime(df.format(new Date()));
		tuangouOrder.setZuofeireason(tuangouOrder.getZuofeireason());
		tuangouOrder.setTkje(tuangouOrder.getTkje());
		tuangouOrder.setSftk(tuangouOrder.getSftk());
		tuangouOrder.setStatus("4");
		
		dao.quxiaoSave(tuangouOrder);
		
		//启动一个线程推送微信消息和发送短信
		final TuangouOrder entity = tuangouOrder;
		new Thread(){
			public void run(){
				try {
					//推送消息
					systemService.sendCancel_tuangouOrder(entity);
					
					//发送短信
					Member m = memberDao.get(entity.getMemberid());
					if("1".equals(m.getIsPhone())){
						StringBuffer contextString = new StringBuffer();
						contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的订单已取消。如有疑问，请咨询400-007-0011。");
						String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
						System.out.println(rtn);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}.start();
		
		return msg;
	}

	@Transactional(readOnly = false)
	public void addRobot(TuangouOrder tuangouOrder,String tuangouid) {
		Tuangou tuangou = new Tuangou();
		
		int seed = new Random().nextInt(90)%(90-10+1)+10;
		Member m = new Member();
		m.preInsert();
		m.setNickname(WxNickImgUtils.nicknames[(seed)%100]);
		m.setHeadimgurl(Global.getConfig("hosturl")+"static/wxheadimg/img%20("+((seed)%100)+").jpg");
		memberDao.insert(m);
		
		tuangouOrder.preInsert();
		tuangouOrder.setMemberid(m.getId());
		tuangou = tuangouService.get(tuangouid);
		tuangouOrder.setTuangou(tuangou);		// 团购id
		tuangouOrder.setDdh("000000000000000");		// 订单号，写死
		tuangouOrder.setFlag("1");		// 虚拟人标识
		dao.addRobotSave(tuangouOrder);
	}

	@Transactional(readOnly = false)
	public String piliangFahuo(String ids) {
		String msg = "批量发货处理成功";
		
		List<TuangouOrder> list0 = Lists.newArrayList();
		List<Map> list = JSON.parseArray(ids, Map.class);	// 得到要发货的订单id
		for(int i=0;i<list.size();i++){
			TuangouOrder tuangouOrder = new TuangouOrder();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map map = list.get(i);
			
			tuangouOrder.setId((String)map.get("id"));
			tuangouOrder = dao.get(tuangouOrder);
			
			
			if(tuangouOrder.getStatus().equals("0")){	// 对发货订单状态判断，如果是0（待发货），则执行下面代码  
				
				// 找到对应团购的菜品id
				String taungouid = tuangouOrder.getTuangou().getId();
				Tuangou tuangou = tuangouDao.get(taungouid);
				Goods goods = tuangou.getGoods();
				// 发货后库存处理
				goods.setDjkc(tuangouOrder.getNum());
				goodsService.updateCard(goods);
				
				// 非空效验
				if(!map.get("kdgs").equals("")){
					tuangouOrder.setKdgs((String)map.get("kdgs"));		// 从map得到快递公司
				}
				if(!map.get("wldh").equals("")){
					tuangouOrder.setWldh((String)map.get("wldh"));		// 从map得到物流单号
				}
				tuangouOrder.setStatus("1");							// 状态更新，已发货
				tuangouOrder.setFahuotime(df.format(new Date()));		// 添加发货时间吗
				dao.up(tuangouOrder);	
				
				list0.add(tuangouOrder);
			}
		}
		
		//启动一个线程推送微信消息和发送短信
		final List<TuangouOrder> orderlist = list0;
		if(orderlist.size()>0){
			new Thread(){
				public void run(){
					try {
							for(TuangouOrder entity : orderlist){
								//推送微信消息
								systemService.sendFahuo_tuangouOrder(entity);
								
								//发送短信
								Member m = memberDao.get(entity.getMemberid());
								if("1".equals(m.getIsPhone())){
									StringBuffer contextString = new StringBuffer();
									contextString.append("【有机汇】您好，订单号为"+entity.getDdh()+"的商品已发货。快递单号："+entity.getWldh()+"。如有疑问，请咨询400-007-0011。");
									String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
									System.out.println(rtn);
								}
							}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		return msg;
	}

}