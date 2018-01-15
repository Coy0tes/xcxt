/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmyewu.web;

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
import com.jeeplus.modules.dmyewu.entity.DmYewu;
import com.jeeplus.modules.dmyewu.service.DmYewuService;

/**
 * dmyewuController
 * @author mxc
 * @version 2017-08-03
 */
@Controller
@RequestMapping(value = "${adminPath}/dmyewu/dmYewu")
public class DmYewuController extends BaseController {

	@Autowired
	private DmYewuService dmYewuService;
	
	@ModelAttribute
	public DmYewu get(@RequestParam(required=false) String id) {
		DmYewu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dmYewuService.get(id);
		}
		if (entity == null){
			entity = new DmYewu();
		}
		return entity;
	}
	
	/**
	 * 业务员管理列表页面
	 */
	@RequiresPermissions("dmyewu:dmYewu:list")
	@RequestMapping(value = {"list", ""})
	public String list(DmYewu dmYewu, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DmYewu> page = dmYewuService.findPage(new Page<DmYewu>(request, response), dmYewu); 
		model.addAttribute("page", page);
		return "modules/dmyewu/dmYewuList";
	}

	/**
	 * 查看，增加，编辑业务员管理表单页面
	 */
	@RequiresPermissions(value={"dmyewu:dmYewu:view","dmyewu:dmYewu:add","dmyewu:dmYewu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DmYewu dmYewu, Model model) {
		model.addAttribute("dmYewu", dmYewu);
		return "modules/dmyewu/dmYewuForm";
	}

	/**
	 * 保存业务员管理
	 */
	@RequiresPermissions(value={"dmyewu:dmYewu:add","dmyewu:dmYewu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DmYewu dmYewu, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, dmYewu)){
			return form(dmYewu, model);
		}
		if(!dmYewu.getIsNewRecord()){//编辑表单保存
			DmYewu t = dmYewuService.get(dmYewu.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(dmYewu, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			dmYewuService.save(t);//保存
		}else{//新增表单保存
			dmYewuService.save(dmYewu);//保存
		}
		addMessage(redirectAttributes, "保存业务员管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
	}
	
	/**
	 * 删除业务员管理
	 */
	@RequiresPermissions("dmyewu:dmYewu:del")
	@RequestMapping(value = "delete")
	public String delete(DmYewu dmYewu, RedirectAttributes redirectAttributes) {
		dmYewuService.delete(dmYewu);
		addMessage(redirectAttributes, "删除业务员管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
	}
	
	/**
	 * 批量删除业务员管理
	 */
	@RequiresPermissions("dmyewu:dmYewu:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			dmYewuService.delete(dmYewuService.get(id));
		}
		addMessage(redirectAttributes, "删除业务员管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dmyewu:dmYewu:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DmYewu dmYewu, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "业务员管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DmYewu> page = dmYewuService.findPage(new Page<DmYewu>(request, response, -1), dmYewu);
    		new ExportExcel("业务员管理", DmYewu.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出业务员管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dmyewu:dmYewu:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DmYewu> list = ei.getDataList(DmYewu.class);
			for (DmYewu dmYewu : list){
				try{
					dmYewuService.save(dmYewu);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条业务员管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条业务员管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入业务员管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
    }
	
	/**
	 * 下载导入业务员管理数据模板
	 */
	@RequiresPermissions("dmyewu:dmYewu:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "业务员管理数据导入模板.xlsx";
    		List<DmYewu> list = Lists.newArrayList(); 
    		new ExportExcel("业务员管理数据", DmYewu.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmyewu/dmYewu/?repage";
    }
	
	
	

}