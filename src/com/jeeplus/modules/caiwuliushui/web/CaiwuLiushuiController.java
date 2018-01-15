/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.caiwuliushui.web;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.caiwuliushui.entity.CaiwuLiushui;
import com.jeeplus.modules.caiwuliushui.service.CaiwuLiushuiService;
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;
import com.jeeplus.modules.dmcardorder.service.DmCardOrderService;
import com.jeeplus.modules.fenxiaoyongjin.entity.FenxiaoYongjin;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;
import com.jeeplus.modules.tuangouorder.service.TuangouOrderService;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrder;
import com.jeeplus.modules.tuangoupackageorder.service.TuangouPackageOrderService;

/**
 * 财务-流水Controller
 * @author mxc
 * @version 2017-05-15
 */
@Controller
@RequestMapping(value = "${adminPath}/caiwuliushui/caiwuLiushui")
public class CaiwuLiushuiController extends BaseController {

	@Autowired
	private CaiwuLiushuiService caiwuLiushuiService;
	@Autowired
	private DmCardOrderService dmCardOrderService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private TuangouOrderService tuangouOrderService;
	@Autowired
	private TuangouPackageOrderService tuangouPackageOrderService ;
	
	@ModelAttribute
	public CaiwuLiushui get(@RequestParam(required=false) String id) {
		CaiwuLiushui entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = caiwuLiushuiService.get(id);
		}
		if (entity == null){
			entity = new CaiwuLiushui();
		}
		return entity;
	}
	
	/**
	 * 财务流水列表页面
	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:list")
	@RequestMapping(value = {"list", ""})
	public String list(CaiwuLiushui caiwuLiushui, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CaiwuLiushui> page = caiwuLiushuiService.findPage(new Page<CaiwuLiushui>(request, response), caiwuLiushui); 
		
		// 0：套餐卡订单	1：菜品订单		2：团购订单	 	3.团购套餐
		// 根据订单类型判断是哪里的订单
		for(CaiwuLiushui each : page.getList()){
			if(each.getOrdertype().equals("0")){
				DmCardOrder dmCardOrder = new DmCardOrder();
				// 根据分销记录的orderid找到相应的订单信息
				dmCardOrder = dmCardOrderService.get(each.getOrderid());
				// 将订单号附给分销佣金的orderid
				if(dmCardOrder != null && dmCardOrder.getId() != null){
					each.setOrderddh(dmCardOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("1")){
				// 菜品订单
				GoodsOrder goodsOrder = new GoodsOrder();
				goodsOrder = goodsOrderService.get(each.getOrderid());
				if(goodsOrder != null && goodsOrder.getId() != null){
					each.setOrderddh(goodsOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("2")){
				// 团购订单
				TuangouOrder tuangouOrder = new TuangouOrder();
				tuangouOrder = tuangouOrderService.get(each.getOrderid());
				if(tuangouOrder != null && tuangouOrder.getId() != null){
					each.setOrderddh(tuangouOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("3")){
				// 团购套餐订单
				TuangouPackageOrder tuangouPackageOrder = new TuangouPackageOrder();
				tuangouPackageOrder = tuangouPackageOrderService.get(each.getOrderid());
				if(tuangouPackageOrder != null && tuangouPackageOrder.getId() != null){
					each.setOrderddh(tuangouPackageOrder.getDdh());
				}
			}
		}
		
		model.addAttribute("page", page);
		return "modules/caiwuliushui/caiwuLiushuiList";
	}

	/**
	 * 查看，增加，编辑财务流水表单页面
	 */
	@RequiresPermissions(value={"caiwuliushui:caiwuLiushui:view","caiwuliushui:caiwuLiushui:add","caiwuliushui:caiwuLiushui:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(CaiwuLiushui caiwuLiushui, Model model) {
		model.addAttribute("caiwuLiushui", caiwuLiushui);
		return "modules/caiwuliushui/caiwuLiushuiForm";
	}

	/**
	 * 保存财务流水
	 */
	@RequiresPermissions(value={"caiwuliushui:caiwuLiushui:add","caiwuliushui:caiwuLiushui:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(CaiwuLiushui caiwuLiushui, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, caiwuLiushui)){
			return form(caiwuLiushui, model);
		}
		if(!caiwuLiushui.getIsNewRecord()){//编辑表单保存
			CaiwuLiushui t = caiwuLiushuiService.get(caiwuLiushui.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(caiwuLiushui, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			caiwuLiushuiService.save(t);//保存
		}else{//新增表单保存
			caiwuLiushuiService.save(caiwuLiushui);//保存
		}
		addMessage(redirectAttributes, "保存财务流水成功");
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
	}
	
	/**
	 * 删除财务流水
	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:del")
	@RequestMapping(value = "delete")
	public String delete(CaiwuLiushui caiwuLiushui, RedirectAttributes redirectAttributes) {
		caiwuLiushuiService.delete(caiwuLiushui);
		addMessage(redirectAttributes, "删除财务流水成功");
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
	}
	
	/**
	 * 批量删除财务流水
	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			caiwuLiushuiService.delete(caiwuLiushuiService.get(id));
		}
		addMessage(redirectAttributes, "删除财务流水成功");
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(CaiwuLiushui caiwuLiushui, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "财务流水"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CaiwuLiushui> page = caiwuLiushuiService.findPage(new Page<CaiwuLiushui>(request, response, -1), caiwuLiushui);
    		new ExportExcel("财务流水", CaiwuLiushui.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出财务流水记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CaiwuLiushui> list = ei.getDataList(CaiwuLiushui.class);
			for (CaiwuLiushui caiwuLiushui : list){
				try{
					caiwuLiushuiService.save(caiwuLiushui);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条财务流水记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条caiwuliushui记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入财务流水失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
    }
	
	/**
	 * 下载导入财务流水数据模板
	 */
	@RequiresPermissions("caiwuliushui:caiwuLiushui:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "财务流水数据导入模板.xlsx";
    		List<CaiwuLiushui> list = Lists.newArrayList(); 
    		new ExportExcel("财务流水数据", CaiwuLiushui.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/caiwuliushui/caiwuLiushui/?repage";
    }
	
	
	

}