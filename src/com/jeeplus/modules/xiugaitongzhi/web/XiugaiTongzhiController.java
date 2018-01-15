/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xiugaitongzhi.web;

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
import com.jeeplus.modules.xiugaitongzhi.entity.XiugaiTongzhi;
import com.jeeplus.modules.xiugaitongzhi.service.XiugaiTongzhiService;

/**
 * xiugaitongzhiController
 * @author mxc
 * @version 2017-09-11
 */
@Controller
@RequestMapping(value = "${adminPath}/xiugaitongzhi/xiugaiTongzhi")
public class XiugaiTongzhiController extends BaseController {

	@Autowired
	private XiugaiTongzhiService xiugaiTongzhiService;
	
	@ModelAttribute
	public XiugaiTongzhi get(@RequestParam(required=false) String id) {
		XiugaiTongzhi entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = xiugaiTongzhiService.get(id);
		}
		if (entity == null){
			entity = new XiugaiTongzhi();
		}
		return entity;
	}
	
	/**
	 * 用户修改通知列表页面
	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:list")
	@RequestMapping(value = {"list", ""})
	public String list(XiugaiTongzhi xiugaiTongzhi, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<XiugaiTongzhi> page = xiugaiTongzhiService.findPage(new Page<XiugaiTongzhi>(request, response), xiugaiTongzhi); 
		model.addAttribute("page", page);
		return "modules/xiugaitongzhi/xiugaiTongzhiList";
	}

	/**
	 * 查看，增加，编辑用户修改通知表单页面
	 */
	@RequiresPermissions(value={"xiugaitongzhi:xiugaiTongzhi:view","xiugaitongzhi:xiugaiTongzhi:add","xiugaitongzhi:xiugaiTongzhi:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(XiugaiTongzhi xiugaiTongzhi, Model model) {
		
		// 查看时是否已阅状态改成已阅
		xiugaiTongzhi.setStatus("1");
		xiugaiTongzhiService.saveStatus(xiugaiTongzhi);
		
		model.addAttribute("xiugaiTongzhi", xiugaiTongzhi);
		return "modules/xiugaitongzhi/xiugaiTongzhiForm";
	}

	/**
	 * 保存用户修改通知
	 */
	@RequiresPermissions(value={"xiugaitongzhi:xiugaiTongzhi:add","xiugaitongzhi:xiugaiTongzhi:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(XiugaiTongzhi xiugaiTongzhi, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, xiugaiTongzhi)){
			return form(xiugaiTongzhi, model);
		}
		if(!xiugaiTongzhi.getIsNewRecord()){//编辑表单保存
			XiugaiTongzhi t = xiugaiTongzhiService.get(xiugaiTongzhi.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(xiugaiTongzhi, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			xiugaiTongzhiService.save(t);//保存
		}else{//新增表单保存
			xiugaiTongzhiService.save(xiugaiTongzhi);//保存
		}
		addMessage(redirectAttributes, "保存用户修改通知成功");
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
	}
	
	/**
	 * 删除用户修改通知
	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:del")
	@RequestMapping(value = "delete")
	public String delete(XiugaiTongzhi xiugaiTongzhi, RedirectAttributes redirectAttributes) {
		xiugaiTongzhiService.delete(xiugaiTongzhi);
		addMessage(redirectAttributes, "删除用户修改通知成功");
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
	}
	
	/**
	 * 批量删除用户修改通知
	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			xiugaiTongzhiService.delete(xiugaiTongzhiService.get(id));
		}
		addMessage(redirectAttributes, "删除用户修改通知成功");
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(XiugaiTongzhi xiugaiTongzhi, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户修改通知"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<XiugaiTongzhi> page = xiugaiTongzhiService.findPage(new Page<XiugaiTongzhi>(request, response, -1), xiugaiTongzhi);
    		new ExportExcel("用户修改通知", XiugaiTongzhi.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户修改通知记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<XiugaiTongzhi> list = ei.getDataList(XiugaiTongzhi.class);
			for (XiugaiTongzhi xiugaiTongzhi : list){
				try{
					xiugaiTongzhiService.save(xiugaiTongzhi);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户修改通知记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户修改通知记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户修改通知失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
    }
	
	/**
	 * 下载导入用户修改通知数据模板
	 */
	@RequiresPermissions("xiugaitongzhi:xiugaiTongzhi:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "用户修改通知数据导入模板.xlsx";
    		List<XiugaiTongzhi> list = Lists.newArrayList(); 
    		new ExportExcel("用户修改通知数据", XiugaiTongzhi.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xiugaitongzhi/xiugaiTongzhi/?repage";
    }
	
	
	

}