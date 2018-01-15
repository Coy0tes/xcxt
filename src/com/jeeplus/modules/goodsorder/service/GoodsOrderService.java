/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodsorder.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sms.SmsUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.dmcard.dao.DmCardDao;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderDao;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderDetailDao;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderExcelDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderExcel;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.xuancai.dao.XuancaiGoodsDao;

/**
 * 菜品订单Service
 * @author mxc
 * @version 2017-06-22
 */
@Service
@Transactional(readOnly = true)
public class GoodsOrderService extends CrudService<GoodsOrderDao, GoodsOrder> {

	@Autowired
	private GoodsOrderDetailDao goodsOrderDetailDao;
	@Autowired
	private GoodsOrderDao goodsOrderDao;
	@Autowired
	private XuancaiGoodsDao xuancaiGoodsDao;
	@Autowired
	private GoodsOrderExcelDao goodsOrderExcelDao;
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private DmCardDao dmCardDao;
	
	public GoodsOrder get(String id) {
 		GoodsOrder goodsOrder = super.get(id);
 		if(goodsOrder != null && !goodsOrder.getId().equals("")){
 			goodsOrder.setGoodsOrderDetailList(goodsOrderDetailDao.findList(new GoodsOrderDetail(goodsOrder)));
 		}
		return goodsOrder;
	}
	
	public List<GoodsOrder> findList(GoodsOrder goodsOrder) {
		return super.findList(goodsOrder);
	}
	
	public Page<GoodsOrder> findPage(Page<GoodsOrder> page, GoodsOrder goodsOrder) {
		return super.findPage(page, goodsOrder);
	}
	
	
	@Transactional(readOnly = false)
	public void save(GoodsOrder goodsOrder) {
		super.save(goodsOrder);
		for (GoodsOrderDetail goodsOrderDetail : goodsOrder.getGoodsOrderDetailList()){
			if (goodsOrderDetail.getId() == null){
				continue;
			}
			if (GoodsOrderDetail.DEL_FLAG_NORMAL.equals(goodsOrderDetail.getDelFlag())){
				if (StringUtils.isBlank(goodsOrderDetail.getId())){
					goodsOrderDetail.preInsert();
					goodsOrderDetailDao.insert(goodsOrderDetail);
				}else{
					goodsOrderDetail.preUpdate();
					goodsOrderDetailDao.update(goodsOrderDetail);
				}
			}else{
				goodsOrderDetailDao.delete(goodsOrderDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(GoodsOrder goodsOrder) {
		super.delete(goodsOrder);
		goodsOrderDetailDao.delete(new GoodsOrderDetail(goodsOrder));
	}
	
	/**
	 * 批量发货
	 * @param ids
	 */
	@Transactional(readOnly = false)
	public String piliangFahuo(String ids) throws Exception {
		String msg = "批量发货处理成功";
		
		List<GoodsOrder> list0 = Lists.newArrayList();
		List<Map> list = JSON.parseArray(ids, Map.class);	// 得到要发货的订单id
		for(int i=0;i<list.size();i++){
			GoodsOrder goodsOrder = new GoodsOrder();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map map = list.get(i);
			
			goodsOrder.setId((String)map.get("id"));
			goodsOrder = goodsOrderDao.get(goodsOrder);
			
			
			if(goodsOrder.getStatus().equals("0")){	// 对发货订单状态判断，如果是0（待发货），则执行下面代码  
				
				// 发货后库存处理
				GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();		// 创建子表对象
				goodsOrderDetail.setMain(goodsOrder); 		// 得到mainid，通过mainid查出goodsOrderDetail 的 goodsid 
				List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailDao.findList(goodsOrderDetail);		// 查询子表信息，以数组的方式将一条或多条子表信息保存
				
				for(GoodsOrderDetail goods : goodsOrderDetailList){
					String dmgoodsid = goods.getGoodsid();	// 得到订单对应的商品id
					Integer dmgoodsnum = goods.getNum();		// 得到订单商品的数量
					
					// 查询对应商品的信息
					Goods good = goodsService.get(dmgoodsid);

					// sql 处理冻结库存
					good.setDjkc(dmgoodsnum);
					goodsService.updateCard(good);
						
				}
				
				// 非空处理
				if(!map.get("kdgs").equals("")){
					goodsOrder.setKdgs((String)map.get("kdgs"));		// 从map得到快递公司
				}
				if(!map.get("wldh").equals("")){
					goodsOrder.setWldh((String)map.get("wldh"));		// 从map得到物流单号
				}
				goodsOrder.setStatus("1");							// 状态更新，已发货
				goodsOrder.setFahuotime(df.format(new Date()));		// 添加发货时间吗
				dao.up(goodsOrder);	
				
				list0.add(goodsOrder);
			}
		}
		
		//启动一个线程推送微信消息和发送短信
		final List<GoodsOrder> orderlist = list0;
		if(orderlist.size()>0){
			new Thread(){
				public void run(){
					try {
							for(GoodsOrder entity : orderlist){
								//推送微信消息
								systemService.sendFahuo_goodsOrder(entity);
								
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

	
	/**
	 * 导入文件实现批量发货
	 * 根据订单号匹配订单信息
	 * 状态效验
	 * 快递公司	kdgs
	 * 物流单号	wldh
	 * 发货时间
	 * 状态改变
	 * 			  ddh-->订单信息-->goodsmain					goodsid-->goodsid
	 * GoodsOrder------------------------->GoodsOrderDetail-------------------->XuancaiGoodis-->库存操作
	 * @param file, RedirectAttributes redirectAttributes
	 */
	@Transactional(readOnly = false)
	public String saveFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		String msg = "保存成功";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			// 类型为GoodsOrderExcel
			List<GoodsOrderExcel> lists = ei.getDataList(GoodsOrderExcel.class);
			//得到导入的每一条数据
      			for(GoodsOrderExcel g : lists){
      				GoodsOrder goodsOrder = new GoodsOrder();
					// 得到 导入 订单号ddh,通过订单号查询相关信息
					// 将订单号放入新建对象中
					goodsOrder.setDdh(g.getDdh());
					
					//根据ddh得到数据库数据
					goodsOrder = dao.getByDdh(goodsOrder);
	
					// 效验状态，只处理未发货
					if(goodsOrder.getStatus().equals("0")){
						// 处理库存跟冻结库存
						GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
						goodsOrderDetail.setMain(goodsOrder);
						List<GoodsOrderDetail> goodsOrderDet = goodsOrderDetailDao.findList(goodsOrderDetail);
						
						for(GoodsOrderDetail goods:goodsOrderDet){
							
							// 取得商品的id
							Goods good = new Goods();
							good.setId(goods.getGoodsid());
							good = goodsService.get(good);
							
							// 冻结库存处理
							// 类型转换
							good.setDjkc(good.getDjkc()-goods.getNum());
							goodsService.save(good);
						
						}
						
						// 从导入的数据提取快递公司跟物流单号
						goodsOrder.setKdgs(g.getKdgs());
						goodsOrder.setWldh(g.getWldh());
						goodsOrder.setKdgs(g.getKdgs());
						goodsOrder.setFahuotime(df.format(new Date()));
						goodsOrder.setStatus("1");	// 改变状态
						dao.up(goodsOrder);	
						
						//推送微信消息
						systemService.sendFahuo_goodsOrder(goodsOrder);
				 }
			}
		} catch(Exception e){
			msg = "数据读取失败";
		}
		return msg;
	}

	/**
	 * 今日未发货订单导出
	 * @param goodsOrder
	 * @return
	 */
	public List<GoodsOrder> findListToday(GoodsOrder goodsOrder) {
		return dao.findListToday(goodsOrder);
	}

	/**
	 * 
	 * @param goodsOrder
	 * @return
	 */
	@Transactional(readOnly = false)
	public String saveFaHuo(GoodsOrder goodsOrder) throws Exception {
		String msg = "发货成功！";
		
		if(goodsOrder.getStatus().equals("0")){
			// 发货后库存处理
			GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
			goodsOrderDetail.setMain(goodsOrder); // 得到mainid，通过mainid查出goodsOrderDetail 的 goodsid
			List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailDao.findList(goodsOrderDetail);
			
			for(GoodsOrderDetail goods : goodsOrderDetailList){
				String dmgoodsid = goods.getGoodsid();	// 得到订单对应的商品id
				Integer dmgoodsnum = goods.getNum();		// 得到订单商品的数量
				
				// 查询对应商品的信息
				Goods good = goodsService.get(dmgoodsid);
				// 类型转换
				good.setDjkc(dmgoodsnum);
				goodsService.updateCard(good);
			}
			
			// 写入订单发货时间
			goodsOrder.setStatus("1");
			dao.saveFa(goodsOrder);//保存
			
			//启动一个线程推送微信消息和发送短信
			final GoodsOrder entity = goodsOrder;
			new Thread() {
				public void run() {
					try {
						//推送微信消息
						systemService.sendFahuo_goodsOrder(entity);
						
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
		}
		
		return msg;
	}

	/**
	 * 取消订单
	 * 待付款 套餐卡使用次数不变，剩余次数不变，库存+，冻结库存-
	 * 待发货 套餐卡使用次数-1，剩余次数+1，库存+，冻结库存-
	 * 
	 * @param goodsOrder
	 */
	@Transactional(readOnly = false)
	public void saveZuoFei(GoodsOrder goodsOrder) throws Exception{
		// 套餐卡的id
		DmCard dmCard = new DmCard();
		dmCard.setId(goodsOrder.getCardid());
		dmCard = dmCardDao.get(dmCard);
		
		//根据订单id找到订单下的菜品
		GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
		goodsOrderDetail.setMain(goodsOrder);
		List<GoodsOrderDetail> goodsOrderDetailList = goodsOrderDetailDao.findList(goodsOrderDetail);
			
		// 如果是待付款，
		if(goodsOrder.getStatus().equals("3")){
			//套餐卡使用次数、剩余次数不操作,待付款的时候套餐卡还没有扣费
		}
		// 如果是待发货，
		if(goodsOrder.getStatus().equals("0")){
			// 1. 套餐卡使用次数-1、剩余次数+1
			dmCardDao.saveZuoFei(dmCard);
		}
		
		//2. 库存+，冻结库存-
		for(GoodsOrderDetail list:goodsOrderDetailList){
			goodsOrderDetail = list;
			//从订单下的菜品找到响应的菜品id
			Goods goods = new Goods();
			goods.setId(goodsOrderDetail.getGoodsid());
			goods = goodsService.get(goods);
			
			goods.setKcsl(goodsOrderDetail.getNum());
			goods.setDjkc(goodsOrderDetail.getNum());
			goodsService.saveZuoFei(goods);
		}
		
		// 3.写入操作者、时间、状态
		User user = UserUtils.getUser();
		goodsOrder.setStatus("4");
		goodsOrder.setPrincipal(user.getId());
		dao.saveZuoFei(goodsOrder);
		
		//启动一个线程推送微信消息和发送短信
		final GoodsOrder entity = goodsOrder;
		new Thread(){
			public void run(){
				try {
					//推送微信消息
					systemService.sendCancel_goodsOrder(entity);
					
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
	}

	@Transactional(readOnly = false)
	public List<GoodsOrder> findTimeList(GoodsOrder goodsOrder) {
		return dao.findTimesList(goodsOrder);
	}
	
}