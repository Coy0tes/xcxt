/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcardnum.web;

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
import com.jeeplus.modules.dmcardnum.entity.DmCardNum;
import com.jeeplus.modules.dmcardnum.service.DmCardNumService;

/**
 * 添加次数Controller
 * @author mxc
 * @version 2017-06-23
 */
@Controller
@RequestMapping(value = "${adminPath}/dmcardnum/dmCardNum")
public class DmCardNumController extends BaseController {

	@Autowired
	private DmCardNumService dmCardNumService;
	
	@ModelAttribute
	public DmCardNum get(@RequestParam(required=false) String id) {
		DmCardNum entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dmCardNumService.get(id);
		}
		if (entity == null){
			entity = new DmCardNum();
		}
		return entity;
	}
	
	/**
	 * 添加次数列表页面
	 */
	@RequiresPermissions("dmcardnum:dmCardNum:list")
	@RequestMapping(value = {"list", ""})
	public String list(DmCardNum dmCardNum, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DmCardNum> page = dmCardNumService.findPage(new Page<DmCardNum>(request, response), dmCardNum); 
		model.addAttribute("page", page);
		return "modules/dmcardnum/dmCardNumList";
	}

	/**
	 * 查看，增加，编辑添加次数表单页面
	 */
	@RequiresPermissions(value={"dmcardnum:dmCardNum:view","dmcardnum:dmCardNum:add","dmcardnum:dmCardNum:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DmCardNum dmCardNum, Model model) {
		model.addAttribute("dmCardNum", dmCardNum);
		return "modules/dmcardnum/dmCardNumForm";
	}

	/**
	 * 保存添加次数
	 */
	@RequiresPermissions(value={"dmcardnum:dmCardNum:add","dmcardnum:dmCardNum:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DmCardNum dmCardNum, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, dmCardNum)){
			return form(dmCardNum, model);
		}
		if(!dmCardNum.getIsNewRecord()){//编辑表单保存
			DmCardNum t = dmCardNumService.get(dmCardNum.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(dmCardNum, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			dmCardNumService.save(t);//保存
		}else{//新增表单保存
			dmCardNumService.save(dmCardNum);//保存
		}
		addMessage(redirectAttributes, "保存添加次数成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
	}
	
	/**
	 * 删除添加次数
	 */
	@RequiresPermissions("dmcardnum:dmCardNum:del")
	@RequestMapping(value = "delete")
	public String delete(DmCardNum dmCardNum, RedirectAttributes redirectAttributes) {
		dmCardNumService.delete(dmCardNum);
		addMessage(redirectAttributes, "删除添加次数成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
	}
	
	/**
	 * 批量删除添加次数
	 */
	@RequiresPermissions("dmcardnum:dmCardNum:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			dmCardNumService.delete(dmCardNumService.get(id));
		}
		addMessage(redirectAttributes, "删除添加次数成功");
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dmcardnum:dmCardNum:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DmCardNum dmCardNum, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "添加次数"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DmCardNum> page = dmCardNumService.findPage(new Page<DmCardNum>(request, response, -1), dmCardNum);
    		new ExportExcel("添加次数", DmCardNum.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出添加次数记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dmcardnum:dmCardNum:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DmCardNum> list = ei.getDataList(DmCardNum.class);
			for (DmCardNum dmCardNum : list){
				try{
					dmCardNumService.save(dmCardNum);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条添加次数记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条添加次数记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入添加次数失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
    }
	
	/**
	 * 下载导入添加次数数据模板
	 */
	@RequiresPermissions("dmcardnum:dmCardNum:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "添加次数数据导入模板.xlsx";
    		List<DmCardNum> list = Lists.newArrayList(); 
    		new ExportExcel("添加次数数据", DmCardNum.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcardnum/dmCardNum/?repage";
    }
	
	
	

}