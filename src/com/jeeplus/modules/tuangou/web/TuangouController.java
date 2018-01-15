/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangou.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.service.GoodsService;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tuangou.entity.Tuangou;
import com.jeeplus.modules.tuangou.service.TuangouService;
import com.jeeplus.modules.tuangouorder.service.TuangouOrderService;

/**
 * 团购管理Controller
 * @author mxc
 * @version 2017-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/tuangou/tuangou")
public class TuangouController extends BaseController {

	@Autowired
	private TuangouService tuangouService;
	@Autowired
	private TuangouOrderService tuangouOrderService;
	@Autowired
	private GoodsService goodsService;
	
	@ModelAttribute
	public Tuangou get(@RequestParam(required=false) String id) {
		Tuangou entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tuangouService.get(id);
		}
		if (entity == null){
			entity = new Tuangou();
		}
		return entity;
	}
	
	/**
	 * 团购管理列表页面
	 */
	@RequiresPermissions("tuangou:tuangou:list")
	@RequestMapping(value = {"list", ""})
	public String list(Tuangou tuangou, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tuangou> page = tuangouService.findPage(new Page<Tuangou>(request, response), tuangou); 
		model.addAttribute("page", page);
		return "modules/tuangou/tuangouList";
	}

	/**
	 * 查看，增加，编辑团购管理表单页面
	 */
	@RequiresPermissions(value={"tuangou:tuangou:view","tuangou:tuangou:add","tuangou:tuangou:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tuangou tuangou, Model model) {
		Goods goods = new Goods();
		// 如果id不为空，非新增页面，查询菜品信息
		if(tuangou.getId()!=null){
			String id = tuangou.getGoods().getId();
			goods = goodsService.get(id);
			String goodsName = goods.getName()+" | "+goods.getGuige();
			tuangou.setGoodsName(goodsName.toString());
			model.addAttribute("goods", goods);
		}
		model.addAttribute("tuangou", tuangou);
		return "modules/tuangou/tuangouForm";
	}

	/**
	 * 保存团购管理
	 */
	@RequiresPermissions(value={"tuangou:tuangou:add","tuangou:tuangou:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Tuangou tuangou, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tuangou)){
			return form(tuangou, model);
		}
		if(!tuangou.getIsNewRecord()){//编辑表单保存
			Tuangou t = tuangouService.get(tuangou.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tuangou, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tuangouService.save(t);//保存
		}else{//新增表单保存
			tuangouService.save(tuangou);//保存
		}
		addMessage(redirectAttributes, "保存团购管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
	}
	
	/**
	 * 删除团购
	 */
	@RequiresPermissions("tuangou:tuangou:del")
	@RequestMapping(value = "delete")
	public String delete(Tuangou tuangou, RedirectAttributes redirectAttributes) {
		tuangouService.delete(tuangou);
		addMessage(redirectAttributes, "删除团购记录成功");
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
	}
	
	/**
	 * 批量删除团购
	 */
	@RequiresPermissions("tuangou:tuangou:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tuangouService.delete(tuangouService.get(id));
		}
		addMessage(redirectAttributes, "删除团购记录成功");
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tuangou:tuangou:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Tuangou tuangou, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tuangou> page = tuangouService.findPage(new Page<Tuangou>(request, response, -1), tuangou);
    		new ExportExcel("团购记录", Tuangou.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出团购记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tuangou:tuangou:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tuangou> list = ei.getDataList(Tuangou.class);
			for (Tuangou tuangou : list){
				try{
					tuangouService.save(tuangou);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团购记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团购记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团购记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
    }
	
	/**
	 * 下载导入团购数据模板
	 */
	@RequiresPermissions("tuangou:tuangou:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购记录数据导入模板.xlsx";
    		List<Tuangou> list = Lists.newArrayList(); 
    		new ExportExcel("团购记录数据", Tuangou.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangou/tuangou/?repage";
    }
	
	
	/**
	 * 选择所属菜品
	 */
	@RequestMapping(value = "selectgoods")
	public String selectgoods(Goods goods, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Goods> page = tuangouService.findPageBygoods(new Page<Goods>(request, response),  goods);
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
		model.addAttribute("obj", goods);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}