/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardorder.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;
import com.jeeplus.modules.dmcardorder.service.DmCardOrderService;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.member.entity.Member;

/**
 * 套餐卡订单管理Controller
 * @author mxc
 * @version 2017-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/dmcardorder/dmCardOrder")
public class DmCardOrderController extends BaseController {

	@Autowired
	private DmCardOrderService dmCardOrderService;
	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private DmCardService dmCardService;
	
	@ModelAttribute
	public DmCardOrder get(@RequestParam(required=false) String id) {
		DmCardOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dmCardOrderService.get(id);
		}
		if (entity == null){
			entity = new DmCardOrder();
		}
		return entity;
	}
	
	/**
	 * 套餐卡订单管理列表页面
	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(DmCardOrder dmCardOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		DmPackage dmPackage = new DmPackage();
		
//		dmPackage.setIson("1");		//加条件只选择已上架的套餐
		dmPackage.setStatus("1");	//加条件只选择启用的套餐
		
		List<DmPackage> dm = dmPackageService.findList(dmPackage);		//页面查询套餐名称
		Page<DmCardOrder> page = dmCardOrderService.findPage(new Page<DmCardOrder>(request, response), dmCardOrder);
		
		model.addAttribute("dmPackageName", dm);
		model.addAttribute("page", page);
		return "modules/dmcardorder/dmCardOrderList";
	}

	/**
	 * 查看，增加，编辑套餐卡订单管理表单页面
	 */
	@RequiresPermissions(value={"dmcardorder:dmCardOrder:view","dmcardorder:dmCardOrder:add","dmcardorder:dmCardOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DmCardOrder dmCardOrder, Model model) {
		DmPackage dmPackage = new DmPackage();
		
		List<DmPackage> dm = dmPackageService.findList(dmPackage);
		
		model.addAttribute("dmPackage", dm);
		model.addAttribute("dmCardOrder", dmCardOrder);
		return "modules/dmcardorder/dmCardOrderForm";
	}

	/**
	 * 保存套餐卡订单管理
	 */
	@RequiresPermissions(value={"dmcardorder:dmCardOrder:add","dmcardorder:dmCardOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DmCardOrder dmCardOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, dmCardOrder)){
			return form(dmCardOrder, model);
		}
		if(!dmCardOrder.getIsNewRecord()){//编辑表单保存
			DmCardOrder t = dmCardOrderService.get(dmCardOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(dmCardOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			dmCardOrderService.save(t);//保存
		}else{//新增表单保存
			dmCardOrderService.save(dmCardOrder);//保存
		}
		addMessage(redirectAttributes, "保存套餐卡订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
	}
	
	/**
	 * 批量发货页面
	 * @param ids
	 * @param dmCardOrder
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "piliangFahuoList")
	public String piliangFahuoList(String ids, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String idArray[] =ids.split(",");

		ArrayList<DmCardOrder> lists = new ArrayList<DmCardOrder>();
		//根据id找到菜品订单相应信息
		for(String id : idArray){
			 DmCardOrder dmCardOrder = new DmCardOrder();
			
			// 根据id查找相关信息
			System.out.println(id);
			dmCardOrder.setId(id);
			dmCardOrder = dmCardOrderService.get(id);
			// 如果状态等于0（待发货），就添加进数组
			if(dmCardOrder.getStatus().equals("0")){
				lists.add(dmCardOrder);
			}
		}
		model.addAttribute("lists", lists);
		return "modules/dmcardorder/dmCardOrderPilFahList";
	}
	/**
	 * 批量发货处理
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "piliangFahuoSave")
	public String piliangFahuoSave(DmCardOrder dmCardOrder, HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
		String ids = request.getParameter("ids");	
		
		String msg = this.dmCardOrderService.piliangFahuo(ids);
		
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
	}
	
	/**
	 * 发货页面
	 */
	@RequiresPermissions(value={"dmcardorder:dmCardOrder:view","dmcardorder:dmCardOrder:add","dmcardorder:dmCardOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "fahuoForm")
	public String fahuoForm(DmCardOrder dmCardOrder, Model model) {
		DmPackage dmPackage = new DmPackage();
		
		List<DmPackage> dm = dmPackageService.findList(dmPackage);
		
		model.addAttribute("dmPackage", dm);
		model.addAttribute("dmCardOrder", dmCardOrder);
		return "modules/dmcardorder/dmCardOrderWuliu";
	}
	
	
	/**
	 * 保存物流信息
	 * @param dmCardOrder
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmcardorder:dmCardOrder:add","dmcardorder:dmCardOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "savewuliu")
	public String savewuliu(DmCardOrder dmCardOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		DmCardOrder t = dmCardOrderService.get(dmCardOrder.getId());//从数据库取出记录的值

		dmCardOrder.setStatus("1");
		
		MyBeanUtils.copyBeanNotNull2Bean(dmCardOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
		dmCardOrderService.saveWuliu(t);//保存
		addMessage(redirectAttributes, "该订单"+dmCardOrder.getDdh()+"发货成功！");
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
	}
	
	
	/**
	 * 删除套餐卡订单管理
	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:del")
	@RequestMapping(value = "delete")
	public String delete(DmCardOrder dmCardOrder, RedirectAttributes redirectAttributes) {
		dmCardOrderService.delete(dmCardOrder);
		addMessage(redirectAttributes, "删除套餐卡订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
	}
	
	/**
	 * 批量删除套餐卡订单管理
	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			dmCardOrderService.delete(dmCardOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除套餐卡订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DmCardOrder dmCardOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		// 导出时间。如果为空，则不限制，如果不为空，则限制时间
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    	if(!StringUtils.isEmpty(dmCardOrder.getStartTime())){
//    		dmCardOrder.setStartTime(sdf.format(new Date()));
//    	}
//    	if(!StringUtils.isEmpty(dmCardOrder.getEndTime())){
//    		dmCardOrder.setEndTime(sdf.format(new Date()));
//    	}
		
		try {
            String fileName = "套餐卡订单管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<DmCardOrder> page = dmCardOrderService.findPage(new Page<DmCardOrder>(request, response, -1), dmCardOrder);
            List<DmCardOrder> lists = dmCardOrderService.findList(dmCardOrder);
            // 导出套餐卡单独设置
            for(DmCardOrder each:lists){
				DmCard dmcard = dmCardService.getCardOrderId(each.getId());
				
				if(dmcard != null ){
					each.setCardName(dmcard.getCardid());
				}
            }
    		new ExportExcel("套餐卡订单管理", DmCardOrder.class).setDataList(lists).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出套餐卡订单管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DmCardOrder> list = ei.getDataList(DmCardOrder.class);
			for (DmCardOrder dmCardOrder : list){
				try{
					dmCardOrderService.save(dmCardOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条套餐卡订单管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条套餐卡订单管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入套餐卡订单管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
    }
	
	/**
	 * 下载导入套餐卡订单管理数据模板
	 */
	@RequiresPermissions("dmcardorder:dmCardOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐卡订单管理数据导入模板.xlsx";
    		List<DmCardOrder> list = Lists.newArrayList(); 
    		new ExportExcel("套餐卡订单管理数据", DmCardOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardorder/dmCardOrder/?repage";
    }
	
	
	/**
	 * 选择购卡会员
	 */
	@RequestMapping(value = "selectmember")
	public String selectmember(Member member, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Member> page = dmCardOrderService.findPageBymember(new Page<Member>(request, response),  member);
		try {
			
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", member);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}