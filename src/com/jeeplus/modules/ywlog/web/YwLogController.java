/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.ywlog.web;

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
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 操作日志Controller
 * @author mxc
 * @version 2017-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/ywlog/ywLog")
public class YwLogController extends BaseController {

	@Autowired
	private YwLogService ywLogService;
	
	@ModelAttribute
	public YwLog get(@RequestParam(required=false) String id) {
		YwLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ywLogService.get(id);
		}
		if (entity == null){
			entity = new YwLog();
		}
		return entity;
	}
	
	/**
	 * 操作日志列表页面
	 */
	@RequiresPermissions("ywlog:ywLog:list")
	@RequestMapping(value = {"list", ""})
	public String list(YwLog ywLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<YwLog> page = ywLogService.findPage(new Page<YwLog>(request, response), ywLog); 
		model.addAttribute("page", page);
		return "modules/ywlog/ywLogList";
	}

	/**
	 * 查看，增加，编辑操作日志表单页面
	 */
	@RequiresPermissions(value={"ywlog:ywLog:view","ywlog:ywLog:add","ywlog:ywLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(YwLog ywLog, Model model) {
		model.addAttribute("ywLog", ywLog);
		return "modules/ywlog/ywLogForm";
	}

	/**
	 * 保存操作日志
	 */
	@RequiresPermissions(value={"ywlog:ywLog:add","ywlog:ywLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(YwLog ywLog, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, ywLog)){
			return form(ywLog, model);
		}
		if(!ywLog.getIsNewRecord()){//编辑表单保存
			YwLog t = ywLogService.get(ywLog.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(ywLog, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			ywLogService.save(t);//保存
		}else{//新增表单保存
			ywLogService.save(ywLog);//保存
		}
		addMessage(redirectAttributes, "保存操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
	}
	
	/**
	 * 删除操作日志
	 */
	@RequiresPermissions("ywlog:ywLog:del")
	@RequestMapping(value = "delete")
	public String delete(YwLog ywLog, RedirectAttributes redirectAttributes) {
		ywLogService.delete(ywLog);
		addMessage(redirectAttributes, "删除操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
	}
	
	/**
	 * 批量删除操作日志
	 */
	@RequiresPermissions("ywlog:ywLog:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			ywLogService.delete(ywLogService.get(id));
		}
		addMessage(redirectAttributes, "删除操作日志成功");
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("ywlog:ywLog:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(YwLog ywLog, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "操作日志"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<YwLog> page = ywLogService.findPage(new Page<YwLog>(request, response, -1), ywLog);
    		new ExportExcel("操作日志", YwLog.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出操作日志记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("ywlog:ywLog:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<YwLog> list = ei.getDataList(YwLog.class);
			for (YwLog ywLog : list){
				try{
					ywLogService.save(ywLog);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条操作日志记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条操作日志记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入操作日志失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
    }
	
	/**
	 * 下载导入操作日志数据模板
	 */
	@RequiresPermissions("ywlog:ywLog:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "操作日志数据导入模板.xlsx";
    		List<YwLog> list = Lists.newArrayList(); 
    		new ExportExcel("操作日志数据", YwLog.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ywlog/ywLog/?repage";
    }
	
	
	

}