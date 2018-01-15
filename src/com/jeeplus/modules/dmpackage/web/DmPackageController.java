/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.web;

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
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.entity.GoodsPojo;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 套餐管理Controller
 * @author mxc
 * @version 2017-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/dmpackage/dmPackage")
public class DmPackageController extends BaseController {

	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private YwLogService ywLogService;
	@Autowired
	private GoodsService goodsService;
	
	
	@ModelAttribute
	public DmPackage get(@RequestParam(required=false) String id) {
		DmPackage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dmPackageService.get(id);
		}
		if (entity == null){
			entity = new DmPackage();
		}
		return entity;
	}
	
	/**
	 * 套餐管理列表页面
	 */
	@RequiresPermissions("dmpackage:dmPackage:list")
	@RequestMapping(value = {"list", ""})
	public String list(DmPackage dmPackage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DmPackage> page = dmPackageService.findPage(new Page<DmPackage>(request, response), dmPackage); 
//		List<DmPackage> dm = dmPackageService.findList(dmPackage);
//		model.addAttribute("dmPackage", dm);
		model.addAttribute("page", page);
		return "modules/dmpackage/dmPackageList";
	}
	
	
	/**
	 *各个套餐下 查看有多少套餐卡
	 *实际就是查询了一次此套餐id下的套餐卡
	 * @return
	 */
	@RequiresPermissions("dmpackage:dmPackage:list")
	@RequestMapping(value = "dmPackageCard")
	public String dmPackageCard(DmPackage dmPackage, HttpServletRequest request, HttpServletResponse response, Model model){
		DmCard dmCard = new DmCard();
		dmCard.setPackageid(dmPackage.getId());
		System.out.println(dmCard.getPackageid());
		Page<DmCard> page = dmCardService.findPage(new Page<DmCard>(request, response), dmCard);
		model.addAttribute("page", page);
		return "modules/dmpackage/dmPackageCard"; 
	}
	

	/**
	 * 查看，增加，编辑套餐管理表单页面
	 */
	@RequiresPermissions(value={"dmpackage:dmPackage:view","dmpackage:dmPackage:add","dmpackage:dmPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DmPackage dmPackage, Model model) {
		Goods goods = new Goods();
		
		//套餐 只显示已上架跟启动的套餐，"1" 代表套餐 启用 已上架  "0" 代表未启用 未上架
//		dmPackage.setStatus("1");
//		List<DmPackage> dm = dmPackageService.findList(dmPackage);	// 上架的套餐
		
//		List<DmPackage> packageList = new ArrayList<DmPackage>();
//		for(DmPackage packages:dm){
//			String packname = packages.getName();
//			String danshuang = packages.getDanshuang().equals("0")?"双配/周":"单配/周";
//			
//			packages.setName(packname + " | " + danshuang);
//			packageList.add(packages);
//		}
		
		//获取所有的菜品
		List<Goods> gds = goodsService.findList(goods);
		List<GoodsPojo> gpj = Lists.newArrayList();		//
		for(Goods g : gds){
			GoodsPojo pj = new GoodsPojo();
			pj.setId(g.getId());
			pj.setName(g.getName());
			pj.setGuige(g.getGuige());
			gpj.add(pj);
		}
		
		//获取上架的所有菜品
		goods.setIson("1");
		List<Goods> gdson = goodsService.findList(goods);
		List<GoodsPojo> gpjon = Lists.newArrayList();		//
		for(Goods g : gdson){
			GoodsPojo pj = new GoodsPojo();
			pj.setId(g.getId());
			pj.setName(g.getName());
			pj.setGuige(g.getGuige());
			gpjon.add(pj);
		}
		
//		model.addAttribute("dmPackage",packageList);
		model.addAttribute("goods",gpj);	// 查看或编辑显示子表菜品
		model.addAttribute("goodson",gpjon);	// 查看或编辑显示子表菜品
		model.addAttribute("dmPackage", dmPackage);
		return "modules/dmpackage/dmPackageForm";
	}

	/**
	 * 保存套餐管理
	 */
	@RequiresPermissions(value={"dmpackage:dmPackage:add","dmpackage:dmPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DmPackage dmPackage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		DmCard dmCard = new DmCard();
		if (!beanValidator(model, dmPackage)){
			return form(dmPackage, model);
		}
		if(!dmPackage.getIsNewRecord()){//编辑表单保存
			
			dmCard.setPackageid(dmPackage.getId());
			List<DmCard> dmCards = dmCardService.findList(dmCard);
			for(DmCard dm:dmCards){
				if(dm.getStatus().equals("1")){
					if(dmPackage.getId().equals(dm.getPackageid())){
						// 如果套餐下有套餐卡，不更新频次、状态、配送次数、选菜数量
						dmPackageService.hasCardSave(dmPackage);// 套餐下有套餐卡的保存
						addMessage(redirectAttributes, "保存套餐管理成功");
						return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
					}
				}
			}
			
//			List<DmPackage> lists0 = dmPackageService.findNoEditList(dmPackage);
//			for(DmPackage dm:lists0){
//				if(dmPackage.getId().equals(dm.getId())){
//					// 如果套餐下有套餐卡，不更新频次、状态、配送次数、选菜数量
//					dmPackageService.hasCardSave(dmPackage);// 套餐下有套餐卡的保存
//					addMessage(redirectAttributes, "保存套餐管理成功");
//					return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
//				}
//			}
			DmPackage t = dmPackageService.get(dmPackage.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(dmPackage, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			dmPackageService.save(t);//保存
//			addMessage(redirectAttributes, "保存套餐管理成功");
		}else{//新增表单保存
			dmPackageService.save(dmPackage);//保存
		}
		addMessage(redirectAttributes, "保存套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
	}
	
	
	/**
	 * 每条套餐 启用/禁用
	 * @param dmPackage
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmpackage:dmPackage:add","dmpackage:dmPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "activation")
	public String activation(DmPackage dmPackage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		//1. 获取不能编辑的套餐列表（存在未激活或者已经激活的套餐卡）
		List<DmPackage> lists0 = dmPackageService.findNoEditList(dmPackage);
		if(dmPackage.getStatus().equals("1")){
			for(DmPackage dm:lists0){
				if(dmPackage.getId().equals(dm.getId())){
					addMessage(redirectAttributes, "该套餐下有套餐卡未失效，不能操作！");
					return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
				}
			}
			dmPackage.setStatus("0");
		}else{
			dmPackage.setStatus("1");
		}
		dmPackageService.save(dmPackage);//保存
		addMessage(redirectAttributes, "套餐修改成功！");
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
	}
	
	/**
	 * 套餐批量启用
	 * @param dmPackage
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmpackage:dmPackage:add","dmpackage:dmPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "enabledPackage")
	@ResponseBody
	public Map<String, Object> enabledPackage(String ids, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String ,Object>();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			// 0：未激活 1：已激活 2：失效
			DmPackage dm = dmPackageService.get(id);
			dm.setStatus("1");
			dmPackageService.save(dm);
			map.put("msg", "1");
		}
		return map;
	}
	
	/**
	 * 套餐批量禁用
	 * @param dmPackage
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmpackage:dmPackage:add","dmpackage:dmPackage:edit"},logical=Logical.OR)
	@RequestMapping(value = "disabledPackage")
	@ResponseBody
	public Map<String, Object> disabledPackage(String ids, RedirectAttributes redirectAttributes) {
		Map<String, Object> map = new HashMap<String, Object>();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			// 对应id的套餐信息
			DmPackage dmPackage = dmPackageService.get(id);
			DmCard dmCard = new DmCard();
			// 将套餐id附给套餐卡中packageid，查询出对应套餐卡是否激活，如果激活，给出响应提示 ‘该套餐下有激活的套餐卡’
			dmCard.setPackageid(dmPackage.getId());
			List<DmCard> lists = dmCardService.findList(dmCard);
			for(DmCard list : lists){
				list.getStatus();
				// 如果套餐卡状态不等于2，给出相应提示
				// 0：未激活 1：已激活 2：失效
				if(!list.getStatus().equals("2") ){
					addMessage(redirectAttributes, "套餐存在未失效的套餐卡，不能禁用此套餐！");
					map.put("msg", "0");
					return map;
				}
			}
			dmPackage.setStatus("0");
			dmPackageService.save(dmPackage);
			map.put("msg", "1");
		}
		return map;
	}	
	
	/**
	 * 删除套餐管理
	 * 条件：
	 * 	套餐卡失效 
	 * 	套餐禁用
	 * @param dmPackage
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("dmpackage:dmPackage:del")
	@RequestMapping(value = "delete")
	public String delete(DmPackage dmPackage, RedirectAttributes redirectAttributes) {
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		// 查找符合删除条件的数据： 套餐卡失效、套餐状态为禁用
		List<DmPackage> lists0 = dmPackageService.findNoEditList(dmPackage);
		// 如果有数据，
		for(DmPackage dm:lists0){
			if(dmPackage.getId().equals(dm.getId())){
				addMessage(redirectAttributes, "该套餐未禁用或者有套餐卡未失效，不能操作！");
				return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
			}
		}
		dmPackageService.delete(dmPackage);
		
		String name = " 删除套餐：["+dmPackage.getName()+"]";
		
		// 拼接要存入的信息
		String str = "操作人："+user.getName()+strbuf+name;
		ywLog.setLog(str);
		ywLog.setModulename("套餐管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
	}
	
	/**
	 * 批量删除套餐管理
	 */
	@RequiresPermissions("dmpackage:dmPackage:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		
		StringBuffer packages = new StringBuffer();
		for(String id : idArray){
			DmPackage dmPackage = dmPackageService.get(id);
			dmPackageService.delete(dmPackage);
			
			strbuf.append("["+dmPackage.getName()+"]");
			System.out.println(packages);
		}
		strbuf.append("批量删除套餐:"+packages);
		System.out.println(strbuf);
		// 拼接字符串
		String str = "操作人："+user.getName() + strbuf;
		ywLog.setLog(str);
		ywLog.setModulename("套餐管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除套餐管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dmpackage:dmPackage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DmPackage dmPackage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DmPackage> page = dmPackageService.findPage(new Page<DmPackage>(request, response, -1), dmPackage);
    		new ExportExcel("套餐管理", DmPackage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出套餐管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dmpackage:dmPackage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DmPackage> list = ei.getDataList(DmPackage.class);
			for (DmPackage dmPackage : list){
				try{
					dmPackageService.save(dmPackage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条套餐管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条套餐管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入套餐管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
    }
	
	/**
	 * 下载导入套餐管理数据模板
	 */
	@RequiresPermissions("dmpackage:dmPackage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐管理数据导入模板.xlsx";
    		List<DmPackage> list = Lists.newArrayList(); 
    		new ExportExcel("套餐管理数据", DmPackage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmpackage/dmPackage/?repage";
    }
	
	
	

}