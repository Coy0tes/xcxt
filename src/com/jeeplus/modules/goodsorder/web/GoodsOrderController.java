/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodsorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.entity.DmPackageGoodsZengsong;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderDetailDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderExcel;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;
import com.jeeplus.modules.xuancai.dao.XuancaiGoodsDao;
import com.jeeplus.modules.xuancai.service.XuancaiService;

/**
 * 菜品订单Controller
 * @author mxc
 * @version 2017-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/goodsorder/goodsOrder")
public class GoodsOrderController extends BaseController {

	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private XuancaiService dmXuancaiService;
	@Autowired
	private XuancaiGoodsDao xuancaiGoodsDao;
	@Autowired
	private GoodsOrderDetailDao goodsOrderDetailDao;
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private DmPackageService dmPackageService;
	
	
	@ModelAttribute
	public GoodsOrder get(@RequestParam(required=false) String id) {
		GoodsOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = goodsOrderService.get(id);
		}
		if (entity == null){
			entity = new GoodsOrder();
		}
		return entity;
	}
	
	/**
	 * 菜品订单列表页面
	 */
	@RequiresPermissions("goodsorder:goodsOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(GoodsOrder goodsOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsOrder> page = goodsOrderService.findPage(new Page<GoodsOrder>(request, response), goodsOrder); 
		model.addAttribute("page", page);
		
		DmPackage dmPackage = new DmPackage();
		List<DmPackage> list = dmPackageService.findList(dmPackage);
		model.addAttribute("packagelist", list);
		
		return "modules/goodsorder/goodsOrderList";
	}

	/**
	 * 查看，增加，编辑菜品订单表单页面
	 */
	@RequiresPermissions(value={"goodsorder:goodsOrder:view","goodsorder:goodsOrder:add","goodsorder:goodsOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GoodsOrder goodsOrder, Model model) {
		model.addAttribute("goodsOrder", goodsOrder);
		return "modules/goodsorder/goodsOrderForm";
	}
	
	/**
	 * 发货处理
	 * 库存跟冻结库存在商品里的处理
	 * @param goodsOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveFa")
	public String saveFa(GoodsOrder goodsOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = goodsOrderService.saveFaHuo(goodsOrder);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
	}
	
	/**
	 * 作废页面
	 * @param goodsOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "formZuoFei")
	public String formZuoFei(GoodsOrder goodsOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
//		List<GoodsOrder> goods = goodsOrderService.findList(goodsOrder);
		
		return "modules/goodsorder/goodsOrderFormZuoFei";
	}
	
	/**
	 * 作废处理页面
	 * @param goodsOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	/**
	 * 订单取消
	 * 	状态：待付款3
	 * 		库存+1，冻结库存-1 
	 * 	状态：待发货0：
	 * 		冻结库存-1 库存+1 套餐卡使用次数-1，剩余次数+1
	 * 套餐卡剩余次数
	 */
	@RequestMapping(value = "saveZuoFei")
	public String saveZuoFei(GoodsOrder goodsOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "订单取消成功！";
		// 订单状态为待付款（3）或者待发货（0）才能取消点单
		if(goodsOrder.getStatus().equals("3") || goodsOrder.getStatus().equals("0")){
			goodsOrderService.saveZuoFei(goodsOrder);
		}else{
			msg = "订单不是待付款或待发货状态， 不能取消！";
		}
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage" ;
	}

	/**
	 * 保存菜品订单
	 */
	@RequiresPermissions(value={"goodsorder:goodsOrder:add","goodsorder:goodsOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GoodsOrder goodsOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, goodsOrder)){
			return form(goodsOrder, model);
		}
		if(!goodsOrder.getIsNewRecord()){//编辑表单保存
			GoodsOrder t = goodsOrderService.get(goodsOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goodsOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			goodsOrderService.save(t);//保存
		}else{//新增表单保存
			goodsOrderService.save(goodsOrder);//保存
		}
		addMessage(redirectAttributes, "保存菜品订单成功");
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
	}
	
	/**
	 * 批量发货页面
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("goodsorder:goodsOrder:list")
	@RequestMapping(value = "piliangFahuoList")
	public String piliangFahuoList(String ids, RedirectAttributes redirectAttributes, Model model) {
		String idArray[] =ids.split(",");

		ArrayList<GoodsOrder> lists = new ArrayList<GoodsOrder>();
		//根据id找到菜品订单相应信息
		for(String id : idArray){
			
			// 根据id查找相关信息
			GoodsOrder goodsOrder = new GoodsOrder();
			goodsOrder.setId(id);
			goodsOrder = goodsOrderService.get(id);
			// 如果状态等于0（待发货），就添加进数组
			if(goodsOrder.getStatus().equals("0")){
				lists.add(goodsOrder);
			}
		}
		model.addAttribute("lists", lists);
//		goodsOrderService.piliangFahuo(ids);
		return "modules/goodsorder/goodsOrderPilFahList";
	}
	
	/**
	 * 批量发货处理
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "piliangFahuo")
	public String piliangFahuo(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		String ids = request.getParameter("ids");	
		
		String msg = this.goodsOrderService.piliangFahuo(ids);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
	}
	
	/**
	 * 删除菜品订单
	 */
	@RequiresPermissions("goodsorder:goodsOrder:del")
	@RequestMapping(value = "delete")
	public String delete(GoodsOrder goodsOrder, RedirectAttributes redirectAttributes) {
		goodsOrderService.delete(goodsOrder);
		addMessage(redirectAttributes, "删除菜品订单成功");
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
	}
	
	/**
	 * 批量删除菜品订单
	 */
	@RequiresPermissions("goodsorder:goodsOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			goodsOrderService.delete(goodsOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除菜品订单成功");
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GoodsOrder goodsOrder,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
    	// 导出今日订单条件
//    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    	if(!StringUtils.isEmpty(goodsOrder.getBegintime())){
//    		goodsOrder.setBegintime(sdf.format(new Date()));
//    	}
//    	if(!StringUtils.isEmpty(goodsOrder.getEndtime())){
//    		goodsOrder.setEndtime(sdf.format(new Date()));
//    	}
    	
    	GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
		try {
			List<GoodsOrderExcel> excel = new ArrayList<GoodsOrderExcel>();
            String fileName = "菜品订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<GoodsOrder> lists = goodsOrderService.findList(goodsOrder);		// 获得今日未发货订单
            
            for(GoodsOrder list: lists){
            	GoodsOrderExcel goodsOrderExcel = new GoodsOrderExcel();
            	// 将订单号、收货人、收货人电话放入要导出的对象中
            	goodsOrderExcel.setDdh(list.getDdh());
            	goodsOrderExcel.setShrname(list.getShrname());
            	goodsOrderExcel.setLxdh(list.getLxdh());
            	
            	//将订单地址整合存放到goodsOrderExcel中的属性address中
            	list = goodsOrderService.get(list);
            	// 将订单中的 省市区详细地址 组装附给goodsOrderExcel的address属性
            	goodsOrderExcel.setAddress(list.getShrprovince()+list.getShrcity()+list.getShrcounty()+list.getShraddress());	//报错
            	
            	// 查询套餐名称/单双配
            	// 根据订单套餐卡id获得套餐卡关联的套餐id，然后获得套餐名
            	DmCard dmCard = dmCardService.get(list.getCardid());
            	DmPackage dmPackage = dmPackageService.get(dmCard.getPackageid());

            	if(dmPackage.getDanshuang().equals("1")){
            		dmPackage.setDanshuang("单配/周");
            	}else{
            		dmPackage.setDanshuang("双配/周");
            	}

            	//------------------------------------------------------------
            	// 套餐、菜品统计
            	// 筛选出关键字：套餐名、单双配
//        		String pkgStr = dmPackage.getDanshuang()+" "+dmPackage.getName();
//        		
//        		
//        		
//        		//防止数组空报错
//        		if(packageCount.size()==0){
//        			packageCount.put("pkg", 0);
//        		}
//        		System.out.println(packageCount);
//        		System.out.println(pkgStr.equals(packageCount));
//        		int a = packageCount.size();
//    			for(int j = 0;j<a; j++){
//    				// 判断map数组中的键有没有相同的，如果有，值+1
//    				// 判断如果是true，value+1，如果false，key保存，然后value=1
//    				if(pkgStr.equals(packageCount)){
//    					packageCount.put(pkgStr, 1);
//    					System.out.println(packageCount);
//    				}else{
//    					i++;
//    					System.out.println(packageCount.entrySet());
//    					packageCount.put(pkgStr, i);
//    					System.out.println(packageCount);
//    					
//    				}
//    			}
//            	System.out.println(pkgList.size());
//            	System.out.println(packageCount.size());
//            	
//            	// 统计赋值：
//            	goodsOrderExcel.setCardid(String.valueOf(packageCount.size()));
//            	
//            	
            	//---------------------------------------------------------------------
            	
            	
            	// 订单菜品信息
            	goodsOrderDetail.setMain(list);
            	List<GoodsOrderDetail> detailLists = goodsOrderDetailDao.findList(goodsOrderDetail);
            	// 获得订单菜品信息 菜品名*菜品份数 
            	StringBuffer str = new StringBuffer();
            	for(GoodsOrderDetail detailList : detailLists){
            		//追加字符串
            		str.append(detailList.getGoodsname().replace("有机", "")+"×"+detailList.getNum()+",");
            	}
            	
            	goodsOrderExcel.setOrderList(str.toString().substring(0,str.length()-1));
            	
            	// 所选套餐赋值
            	goodsOrderExcel.setPackageName(dmPackage.getName());
            	
            	excel.add(goodsOrderExcel);
            }
            new ExportExcel("菜品订单", GoodsOrderExcel.class).setDataList(excel).write(response,fileName).dispose();	// 由于setDataList只能存放数组，所以先定义一个excel数组，将goodsOrderExcel放进去
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出菜品订单记录失败！失败信息："+e.getMessage());
			e.printStackTrace();
			
		}
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("goodsorder:goodsOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		String msg = goodsOrderService.saveFile(file,redirectAttributes);
			addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
    }
	
	/**
	 * 下载导入菜品订单数据模板
	 */
	@RequiresPermissions("goodsorder:goodsOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜品订单数据导入模板.xlsx";
    		List<GoodsOrder> list = Lists.newArrayList(); 
    		new ExportExcel("菜品订单数据", GoodsOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goodsorder/goodsOrder/?repage";
    }
}