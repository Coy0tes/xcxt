/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goodscategory.web;

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
import com.jeeplus.modules.goodscategory.entity.GoodsCategory;
import com.jeeplus.modules.goodscategory.service.GoodsCategoryService;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 菜品分类管理Controller
 * @author mxc
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/goodscategory/goodsCategory")
public class GoodsCategoryController extends BaseController {

	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private YwLogService ywLogService;
	
	@ModelAttribute
	public GoodsCategory get(@RequestParam(required=false) String id) {
		GoodsCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = goodsCategoryService.get(id);
		}
		if (entity == null){
			entity = new GoodsCategory();
		}
		return entity;
	}
	
	/**
	 * 菜品分类管理列表页面
	 */
	@RequiresPermissions("goodscategory:goodsCategory:list")
	@RequestMapping(value = {"list", ""})
	public String list(GoodsCategory goodsCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GoodsCategory> page = goodsCategoryService.findPage(new Page<GoodsCategory>(request, response), goodsCategory); 
		model.addAttribute("page", page);
		return "modules/goodscategory/goodsCategoryList";
	}

	/**
	 * 查看，增加，编辑菜品分类管理表单页面
	 */
	@RequiresPermissions(value={"goodscategory:goodsCategory:view","goodscategory:goodsCategory:add","goodscategory:goodsCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GoodsCategory goodsCategory, Model model) {
		List<GoodsCategory> list = goodsCategoryService.findList(goodsCategory);
		//sort自动赋值，先判断是否为空，空就赋值
		if(goodsCategory.getSort() == null){
			goodsCategory.setSort(list.size()+1);
		}
		model.addAttribute("goodsCategory", goodsCategory);
		return "modules/goodscategory/goodsCategoryForm";
	}

	/**
	 * 保存菜品分类管理
	 */
	@RequiresPermissions(value={"goodscategory:goodsCategory:add","goodscategory:goodsCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GoodsCategory goodsCategory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, goodsCategory)){
			return form(goodsCategory, model);
		}
		if(!goodsCategory.getIsNewRecord()){//编辑表单保存
			GoodsCategory t = goodsCategoryService.get(goodsCategory.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goodsCategory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			goodsCategoryService.save(t);//保存
		}else{//新增表单保存
			goodsCategoryService.save(goodsCategory);//保存
		}
		
		addMessage(redirectAttributes, "保存菜品分类管理成功");
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
	}
	
	/**
	 * 删除菜品分类管理
	 */
	@RequiresPermissions("goodscategory:goodsCategory:del")
	@RequestMapping(value = "delete")
	public String delete(GoodsCategory goodsCategory, RedirectAttributes redirectAttributes) {
		goodsCategoryService.delete(goodsCategory);
		addMessage(redirectAttributes, "删除菜品分类管理成功");
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
	}
	
	/**
	 * 批量删除菜品分类管理
	 */
	@RequiresPermissions("goodscategory:goodsCategory:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			goodsCategoryService.delete(goodsCategoryService.get(id));
		}
		addMessage(redirectAttributes, "删除菜品分类管理成功");
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goodscategory:goodsCategory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GoodsCategory goodsCategory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜品分类管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<GoodsCategory> page = goodsCategoryService.findPage(new Page<GoodsCategory>(request, response, -1), goodsCategory);
    		new ExportExcel("菜品分类管理", GoodsCategory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出菜品分类管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("goodscategory:goodsCategory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GoodsCategory> list = ei.getDataList(GoodsCategory.class);
			for (GoodsCategory goodsCategory : list){
				try{
					goodsCategoryService.save(goodsCategory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条菜品分类管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条菜品分类管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入菜品分类管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
    }
	
	/**
	 * 下载导入菜品分类管理数据模板
	 */
	@RequiresPermissions("goodscategory:goodsCategory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜品分类管理数据导入模板.xlsx";
    		List<GoodsCategory> list = Lists.newArrayList(); 
    		new ExportExcel("菜品分类管理数据", GoodsCategory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goodscategory/goodsCategory/?repage";
    }
	
	
	

}