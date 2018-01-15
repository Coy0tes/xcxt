/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangouorder.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.member.service.MemberService;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangou.service.TuangouService;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrderExcal;
import com.jeeplus.modules.tuangouorder.service.TuangouOrderService;

/**
 * 团购订单管理Controller
 * @author mxc
 * @version 2017-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/tuangouorder/tuangouOrder")
public class TuangouOrderController extends BaseController {

	@Autowired
	private TuangouOrderService tuangouOrderService;
	@Autowired
	private TuangouService tuangouService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private GoodsService goodsService;
	
	@ModelAttribute
	public TuangouOrder get(@RequestParam(required=false) String id) {
		TuangouOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tuangouOrderService.get(id);
		}
		if (entity == null){
			entity = new TuangouOrder();
		}
		return entity;
	}
	
	/**
	 * 团购订单管理列表页面
	 */
	@RequiresPermissions("tuangouorder:tuangouOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(TuangouOrder tuangouOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(tuangouOrder.getFlag())){
			tuangouOrder.setFlag("0");
		}
		Page<TuangouOrder> page = tuangouOrderService.findPage(new Page<TuangouOrder>(request, response), tuangouOrder); 
		for(TuangouOrder each:page.getList()){
			// 获取团购菜品明细,先获取团购id，再获取菜品id
        	if(!StringUtils.isEmpty(each.getTuangou().getId())){
        		Tuangou tuangou = tuangouService.get(each.getTuangou().getId());
        		if(!StringUtils.isEmpty(tuangou.getGoods().getId())){
        			Goods goods = goodsService.get(tuangou.getGoods().getId());
        			if(!StringUtils.isEmpty(goods.getId())){
        				each.setRem(goods.getName()+"*"+goods.getGuige());
        			}
        			
        		}
        	}
		}
		model.addAttribute("page", page);
		return "modules/tuangouorder/tuangouOrderList";
	}

	/**
	 * 查看，增加，编辑团购订单管理表单页面
	 * nongneng属性	: 1：发货
	 * 				： 0 ：作废
	 * 				： 3 ：查看，正常显示
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:view","tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TuangouOrder tuangouOrder, Model model) {
		// 获取团购菜品明细,先获取团购id，再获取菜品id
    	if(!StringUtils.isEmpty(tuangouOrder.getTuangou().getId())){
    		Tuangou tuangou = tuangouService.get(tuangouOrder.getTuangou().getId());
    		if(!StringUtils.isEmpty(tuangou.getGoods().getId())){
    			Goods goods = goodsService.get(tuangou.getGoods().getId());
    			if(!StringUtils.isEmpty(goods.getId())){
    				tuangouOrder.setRem(goods.getName()+"*"+goods.getGuige());
    			}
    			
    		}
    	}
    	
		model.addAttribute("tuangouOrder", tuangouOrder);
		return "modules/tuangouorder/tuangouOrderForm";
	}
	
	/**
	 * 取消订单页面
	 * @param tuangouOrder
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:view","tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "quxiaoForm")
	public String quxiaoForm(TuangouOrder tuangouOrder, Model model) {
		model.addAttribute("tuangouOrder", tuangouOrder);
		return "modules/tuangouorder/tuangouOrderQuxiaoForm";
	}
	
	/**
	 * 批量发货页面
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "piliangFahuoList")
	public String piliangFahuoList(String ids, RedirectAttributes redirectAttributes, Model model) {
		String idArray[] =ids.split(",");

		ArrayList<TuangouOrder> lists = new ArrayList<TuangouOrder>();
		//根据id找到菜品订单相应信息
		for(String id : idArray){
			
			// 根据id查找相关信息
			TuangouOrder tuangouOrder = new TuangouOrder();
			tuangouOrder.setId(id);
			tuangouOrder = tuangouOrderService.get(id);
			// 如果状态等于0（待发货），就添加进数组
			if(tuangouOrder.getStatus().equals("0")){
				lists.add(tuangouOrder);
			}
		}
		model.addAttribute("lists", lists);
//		goodsOrderService.piliangFahuo(ids);
		return "modules/tuangouorder/tuangouOrderPilFahList";
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
		
		String msg = this.tuangouOrderService.piliangFahuo(ids);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	/**
	 * 发货订单页面
	 * @param tuangouOrder
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:view","tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "fahuoForm")
	public String fahuoForm(TuangouOrder tuangouOrder, Model model) {
		model.addAttribute("tuangouOrder", tuangouOrder);
		return "modules/tuangouorder/tuangouOrderFahuoForm";
	}
	
	/**
	 * 发货处理
	 * @param tuangouOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "fahuoSave")
	public String fahuoSave(TuangouOrder tuangouOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		// 发货处理
		// * 0:待发货* 1：已发货* 2：已完成* 3：待付款* 4：已作废 
		// tuangouOrder.getGongneng()--0：表示作废处理* 1：发货处理 --
			// * 待付款不能发货 * 已作废不能发货 * 已发货或者已完成不能重复发货
			if(tuangouOrder.getStatus().equals("0")){
				String msg = tuangouOrderService.saveFaHuo(tuangouOrder);
				addMessage(redirectAttributes, msg);
			}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	/**
	 * 团购参团记录页面
	 * @param tuangou
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "tuangouOrderRecordList")
	public String tuangouOrderRecordList(Tuangou tuangou, Model model){
		TuangouOrder tuangouOrder = new TuangouOrder(); 
		tuangouOrder.setTuangou(tuangou);
		List<TuangouOrder> tuangouOrderLists = tuangouOrderService.findList(tuangouOrder);
		
		model.addAttribute("lists", tuangouOrderLists);
		model.addAttribute("tuangouid", tuangou.getId());
		return "modules/tuangou/tuangouOrderRecordList";
	}
	/**
	 * 团购参团记录处理
	 * @param tuangouid
	 * @param reuqest
	 * @return
	 */
	@RequestMapping(value = "tuangouOrderRecord")
	@ResponseBody
	public Map<String, String> tuangouOrderRecord(TuangouOrder tuangouOrder, HttpServletRequest request, RedirectAttributes redirectAttributes){
		String tuangouid = request.getParameter("tuangouid");
		Map<String, String> map = new HashMap<String, String>();
		
 		Tuangou tuangou = tuangouService.get(tuangouid);
 		// 当前团购起团数量
 		int tuangouNum = tuangou.getMinnum();
 		// 当前团购在订单中的数量
 		tuangouOrder.setTuangou(tuangou);
 		List<TuangouOrder> list = tuangouOrderService.findList(tuangouOrder);
 		// 团购数量校验，是否超过参团数量
 		if(tuangouNum <= list.size()){
 			map.put("msg", "团购人数已到达额定数量！");
 		}else{
 			tuangouOrderService.addRobot(tuangouOrder,tuangouid);
 			map.put("msg", "添加指定会员成功！");
 		}
		return map;
	}
	
	/**
	 * 团购订单添加虚拟信息
	 * @param tuangouOrder
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addRobot")
	public String addRobot(TuangouOrder tuangouOrder,HttpServletRequest request ,Model model, RedirectAttributes redirectAttributes){
		String tuangouid = request.getParameter("tuangouid");
		// 会员信息：id name
		// 团购信息：订单编号 团购id（规则名称）
		tuangouOrderService.addRobot(tuangouOrder,tuangouid);
		
		addMessage(redirectAttributes, "添加指定会员成功！");
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
	}
	
	/**
	 * 取消订单处理
	 * @param tuangouOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "quxiaoSave")
	public String quxiaoSave(TuangouOrder tuangouOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
//		TuangouOrder t = tuangouOrderService.get(tuangouOrder.getId());//从数据库取出记录的值
		if(tuangouOrder.getStatus().equals("3") || tuangouOrder.getStatus().equals("0")){
			String msg = tuangouOrderService.quxiaoSave(tuangouOrder);
			addMessage(redirectAttributes, msg);
		}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	
	/**
	 * 保存团购订单管理
	 * 发货处理
	 * 
	 */
	@RequiresPermissions(value={"tuangouorder:tuangouOrder:add","tuangouorder:tuangouOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TuangouOrder tuangouOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
 		if (!beanValidator(model, tuangouOrder)){
			return form(tuangouOrder, model);
		}
		if(!tuangouOrder.getIsNewRecord()){//编辑表单保存
			TuangouOrder t = tuangouOrderService.get(tuangouOrder.getId());//从数据库取出记录的值
			tuangouOrderService.save(t);//保存
		}else{//新增表单保存
			tuangouOrderService.save(tuangouOrder);//保存
		}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	/**
	 * 删除团购订单管理
	 */
	@RequestMapping(value = "delete")
	public String delete(TuangouOrder tuangouOrder, RedirectAttributes redirectAttributes) {
		tuangouOrderService.delete(tuangouOrder);
		addMessage(redirectAttributes, "删除团购订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	/**
	 * 批量删除团购订单管理
	 */
	@RequiresPermissions("tuangouorder:tuangouOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tuangouOrderService.delete(tuangouOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除团购订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tuangouorder:tuangouOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TuangouOrder tuangouOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
//		TuangouOrderExcal tuangouOrderExcal = new TuangouOrderExcal();
//		List<TuangouOrderExcal> excal = new ArrayList<TuangouOrderExcal>();
		try {
            String fileName = "团购订单管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<TuangouOrder> page = tuangouOrderService.findPage(new Page<TuangouOrder>(request, response, -1), tuangouOrder);
            List<TuangouOrder> lists = tuangouOrderService.findList(tuangouOrder);
            
            for(TuangouOrder each:lists){
            	// 获取团购菜品明细,先获取团购id，再获取菜品id
            	if(!StringUtils.isEmpty(each.getTuangou().getId())){
            		Tuangou tuangou = tuangouService.get(each.getTuangou().getId());
            		if(!StringUtils.isEmpty(tuangou.getGoods().getId())){
            			Goods goods = goodsService.get(tuangou.getGoods().getId());
            			if(!StringUtils.isEmpty(goods.getId())){
            				each.setRem(goods.getName()+"*"+goods.getGuige());
            			}
            			
            		}
            	}
            	
            	// 地址拼接
            	String address = "";
            	if(!StringUtils.isEmpty(each.getShrprovince())){
            		address += each.getShrprovince() + " ";
            	}
            	if(!StringUtils.isEmpty(each.getShrcity())){
            		address += each.getShrcity() + " ";
            	}
            	if(!StringUtils.isEmpty(each.getShrcounty())){
            		address += each.getShrcounty() + " ";
            	}
            	if(!StringUtils.isEmpty(each.getShraddress())){
            		address += each.getShraddress();
            	}
            	
            	each.setAddress(address.toString());
            }
            	
            
    		new ExportExcel("团购订单管理", TuangouOrder.class).setDataList(lists).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出团购订单管理记录失败！失败信息："+e.getMessage());
			System.out.println(e);
		}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tuangouorder:tuangouOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TuangouOrder> list = ei.getDataList(TuangouOrder.class);
			for (TuangouOrder tuangouOrder : list){
				try{
					tuangouOrderService.save(tuangouOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团购订单管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团购订单管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团购订单管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
    }
	
	/**
	 * 下载导入团购订单管理数据模板
	 */
	@RequiresPermissions("tuangouorder:tuangouOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购订单管理数据导入模板.xlsx";
    		List<TuangouOrder> list = Lists.newArrayList(); 
    		new ExportExcel("团购订单管理数据", TuangouOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangouorder/tuangouOrder/?repage";
    }
	
	
	

}