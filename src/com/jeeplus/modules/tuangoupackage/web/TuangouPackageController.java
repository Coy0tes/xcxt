/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackage.web;

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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.tuangoupackage.dao.TuangouPackageGuigeDao;
import com.jeeplus.modules.tuangoupackage.entity.TuangouPackage;
import com.jeeplus.modules.tuangoupackage.entity.TuangouPackageGuige;
import com.jeeplus.modules.tuangoupackage.service.TuangouPackageService;

/**
 * 团购套餐管理Controller
 * @author mxc
 * @version 2017-07-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tuangoupackage/tuangouPackage")
public class TuangouPackageController extends BaseController {

	@Autowired
	private TuangouPackageService tuangouPackageService;
	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private TuangouPackageGuigeDao tuangouPackageGuigeDao;
	
	@ModelAttribute
	public TuangouPackage get(@RequestParam(required=false) String id) {
		TuangouPackage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tuangouPackageService.get(id);
		}
		if (entity == null){
			entity = new TuangouPackage();
		}
		return entity;
	}
	
	/**
	 * 团购套餐管理列表页面
	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:list")
	@RequestMapping(value = {"list", ""})
	public String list(TuangouPackage tuangouPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TuangouPackage> page = tuangouPackageService.findPage(new Page<TuangouPackage>(request, response), tuangouPackage); 
		model.addAttribute("page", page);
		return "modules/tuangoupackage/tuangouPackageList";
	}

	/**
	 * 查看，增加，编辑团购套餐管理表单页面
	 */
	@RequiresPermissions(value={"tuangoupackage:tuangouPackage:view","tuangoupackage:tuangouPackage:add","tuangoupackage:tuangouPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TuangouPackage tuangouPackage, Model model) {
		
		// 获得套餐信息
		DmPackage dmPackage = new DmPackage();
		List<DmPackage> t = new ArrayList<DmPackage>();
		
		dmPackage.setStatus("1");	// 过滤效验，只显示已启用的套餐
		//List<DmPackage> lists = dmPackageService.findList(dmPackage);	// 将套餐信息加载到页面供子菜单选择菜品
		for(TuangouPackageGuige tuangouPackageGuigeLists : tuangouPackage.getTuangouPackageGuigeList()){	//循环子表信息
			DmPackage dm = new DmPackage(); 
			dm.setId(tuangouPackageGuigeLists.getPackageid());
			dm = dmPackageService.get(dm);
			t.add(dm);
		}
		//model.addAttribute("dmPackage", lists);	//子菜单选菜用
		
		model.addAttribute("tuangouPackage", tuangouPackage);
		model.addAttribute("packages",t);	// 查看或编辑显示子表菜品
		return "modules/tuangoupackage/tuangouPackageForm";
	}
	
	@RequestMapping(value = "xinzengForm")
	public String xinzengForm(TuangouPackage tuangouPackage, Model model){
		DmPackage dmPackage = new DmPackage();
		List<TuangouPackageGuige> guigelist = Lists.newArrayList();
		dmPackage.setStatus("1");
		dmPackage.setXszt("1");
		// 查询所有上架的套餐
		List<DmPackage> lists = dmPackageService.findList(dmPackage);
		for(DmPackage each : lists){
			TuangouPackageGuige e = new TuangouPackageGuige();
			e.setPackageid(each.getId());
			guigelist.add(e);
		}
		tuangouPackage.setTuangouPackageGuigeList(guigelist);
		
		model.addAttribute("tuangouPackage", tuangouPackage);
		model.addAttribute("packages",lists);
		
		return "modules/tuangoupackage/tuangouPackageXinzengForm";
	}

	/**
	 * 保存团购套餐管理
	 */
	@RequiresPermissions(value={"tuangoupackage:tuangouPackage:add","tuangoupackage:tuangouPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TuangouPackage tuangouPackage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tuangouPackage)){
			return form(tuangouPackage, model);
		}
		if(!tuangouPackage.getIsNewRecord()){//编辑表单保存
			TuangouPackage t = tuangouPackageService.get(tuangouPackage.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tuangouPackage, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tuangouPackageService.save(t);//保存
		}else{//新增表单保存
			tuangouPackageService.save(tuangouPackage);//保存
		}
		addMessage(redirectAttributes, "保存团购套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
	}
	
	@RequestMapping(value = "checkDelete")
	@ResponseBody
	public Map<String, String> checkDelete(HttpServletRequest request){
		String msg1 = "";
		String msg2 = "";
		String code = "00";
		String msg4 = "";
		String msg3 = "";
		String packid = request.getParameter("idd");	// 子表id
		String id = request.getParameter("packid");	// 主表id
		Map<String,String> map = new HashMap<String, String>();
		// 根据主表id查询该团购套餐是否上架
		TuangouPackage tuangouPackage = tuangouPackageService.get(id);
		if(tuangouPackage.getIson().equals("1")){
			code = "11";
			msg1 = "团购套餐：【"+tuangouPackage.getGuizename()+"】未下架 \r\n";
		}
		
		// 根据子表id查询该套餐是否下架
		TuangouPackageGuige tuangouPackageGuige = tuangouPackageGuigeDao.get(packid);
		DmPackage dmPackage = dmPackageService.get(tuangouPackageGuige.getPackageid());
		if(dmPackage.getStatus().equals("1")){
//			// 根据goodsid查询菜品的名称跟规格
			code = "11";
			msg2 = "套餐：【";
//			+dmPackage.getDanshuang()+"】未下架,不能删除！"
			msg4 = dmPackage.getDanshuang();
			msg3 = "】未下架,不能删除！";
			// 套餐**+单双+未下架
			// 完整的输出信息：msg = 1 + 2 + 3 
		}
		map.put("code", code);
		map.put("msg1", msg1);
		map.put("msg2", msg2);
		map.put("msg3", msg3);
		map.put("msg4", msg4);
		
		if(code.equals("00")){
			//xuancaiGoodsDao.delete(xuancaiGoods);
		}
		return map;
	}
	
	/**
	 * 选菜批量上架
	 * @param request
	 * @return
	 */
	@RequestMapping(value="isonUp")
	@ResponseBody
	public Map<String, String> isonUp(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		tuangouPackageService.isonUp(request);
		map.put("msg", "批量上架成功！");
		return map;
	}
	/**
	 * 选菜批量下架
	 * @param request
	 * @return
	 */
	@RequestMapping(value="isonDown")
	@ResponseBody
	public Map<String, String> isonDown(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		tuangouPackageService.isonDown(request);
		map.put("msg", "批量下架成功！");
		return map;
	}
	
	/**
	 * 删除团购套餐管理
	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:del")
	@RequestMapping(value = "delete")
	public String delete(TuangouPackage tuangouPackage, RedirectAttributes redirectAttributes) {
		tuangouPackageService.delete(tuangouPackage);
		addMessage(redirectAttributes, "删除团购套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
	}
	
	/**
	 * 批量删除团购套餐管理
	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tuangouPackageService.delete(tuangouPackageService.get(id));
		}
		addMessage(redirectAttributes, "删除团购套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TuangouPackage tuangouPackage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购套餐管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TuangouPackage> page = tuangouPackageService.findPage(new Page<TuangouPackage>(request, response, -1), tuangouPackage);
    		new ExportExcel("团购套餐管理", TuangouPackage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出团购套餐管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TuangouPackage> list = ei.getDataList(TuangouPackage.class);
			for (TuangouPackage tuangouPackage : list){
				try{
					tuangouPackageService.save(tuangouPackage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团购套餐管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团购套餐管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团购套餐管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
    }
	
	/**
	 * 下载导入团购套餐管理数据模板
	 */
	@RequiresPermissions("tuangoupackage:tuangouPackage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购套餐管理数据导入模板.xlsx";
    		List<TuangouPackage> list = Lists.newArrayList(); 
    		new ExportExcel("团购套餐管理数据", TuangouPackage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackage/tuangouPackage/?repage";
    }
	
	
	

}