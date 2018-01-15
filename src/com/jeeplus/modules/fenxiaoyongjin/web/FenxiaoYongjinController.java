/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.fenxiaoyongjin.web;

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
import com.jeeplus.modules.dmcardorder.entity.DmCardOrder;
import com.jeeplus.modules.dmcardorder.service.DmCardOrderService;
import com.jeeplus.modules.fenxiaoyongjin.entity.FenxiaoYongjin;
import com.jeeplus.modules.fenxiaoyongjin.service.FenxiaoYongjinService;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;
import com.jeeplus.modules.tuangouorder.entity.TuangouOrder;
import com.jeeplus.modules.tuangouorder.service.TuangouOrderService;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrder;
import com.jeeplus.modules.tuangoupackageorder.service.TuangouPackageOrderService;

/**
 * 分销佣金管理Controller
 * @author mxc
 * @version 2017-05-16
 */

/**
 * 请求比如是'/fenxiaoyongjin/fenxiaoYongjin/save',先匹配controller的requesetmapping,匹配到了FenxiaoYongjinController类，再
 * 匹配FenxiaoYongjinController类中的方法，方法的requesetmapping必须等于save
 * @author PG01
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/fenxiaoyongjin/fenxiaoYongjin")
public class FenxiaoYongjinController extends BaseController {

	@Autowired
	private FenxiaoYongjinService fenxiaoYongjinService;
	@Autowired
	private DmCardOrderService dmCardOrderService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private TuangouOrderService tuangouOrderService;
	@Autowired
	private TuangouPackageOrderService tuangouPackageOrderService ;
	
	
	/**
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public FenxiaoYongjin get(@RequestParam(required=false) String id) {
		FenxiaoYongjin entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fenxiaoYongjinService.get(id);
		}
		if (entity == null){
			entity = new FenxiaoYongjin();
		}
		return entity;
	}
	
	/**
	 * 分销佣金记录列表页面
	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:list")
	@RequestMapping(value = {"list", ""})
	public String list(FenxiaoYongjin fenxiaoYongjin, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FenxiaoYongjin> page = fenxiaoYongjinService.findPage(new Page<FenxiaoYongjin>(request, response), fenxiaoYongjin); //分页查询，显示某页的多行记录，里边需要调用findList方法
		// 0：套餐卡订单	1：菜品订单		2：团购订单
		// 根据订单类型判断是哪里的订单
		
		for(FenxiaoYongjin each : page.getList()){
			if(each.getOrdertype().equals("0")){
				DmCardOrder dmCardOrder = new DmCardOrder();
				// 根据分销记录的orderid找到相应的订单信息
				dmCardOrder = dmCardOrderService.get(each.getOrderid());
				// 将订单号附给分销佣金的orderid
				if(dmCardOrder != null && !dmCardOrder.getId().equals("")){
					each.setOrderddh(dmCardOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("1")){
				// 菜品订单
				GoodsOrder goodsOrder = new GoodsOrder();
				goodsOrder = goodsOrderService.get(each.getOrderid());
				if(goodsOrder != null && !goodsOrder.getId().equals("")){
					each.setOrderddh(goodsOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("2")){
				// 团购订单
				TuangouOrder tuangouOrder = new TuangouOrder();
				tuangouOrder = tuangouOrderService.get(each.getOrderid());
				if(tuangouOrder != null && !tuangouOrder.getId().equals("")){
					each.setOrderddh(tuangouOrder.getDdh());
				}
			}else if(each.getOrdertype().equals("3")){
				// 团购套餐订单
				TuangouPackageOrder tuangouPackageOrder = new TuangouPackageOrder();
				tuangouPackageOrder = tuangouPackageOrderService.get(each.getOrderid());
				if(tuangouPackageOrder != null && !tuangouPackageOrder.getId().equals("")){
					each.setOrderddh(tuangouPackageOrder.getDdh());
				}
			}
		}
		
		model.addAttribute("page", page);  
		return "modules/fenxiaoyongjin/fenxiaoYongjinList"; //返回到页面，webpage/modules/fenxiaoyongjin/fenxiaoYongjinList.jsp,页面中就能使用model中保存的属性了
	}

	/**
	 * 查看，增加，编辑分销佣金记录表单页面
	 */
	@RequiresPermissions(value={"fenxiaoyongjin:fenxiaoYongjin:view","fenxiaoyongjin:fenxiaoYongjin:add","fenxiaoyongjin:fenxiaoYongjin:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FenxiaoYongjin fenxiaoYongjin, Model model) {
		model.addAttribute("fenxiaoYongjin", fenxiaoYongjin);
		return "modules/fenxiaoyongjin/fenxiaoYongjinForm";
	}

	/**
	 * 保存分销佣金记录
	 */
	@RequiresPermissions(value={"fenxiaoyongjin:fenxiaoYongjin:add","fenxiaoyongjin:fenxiaoYongjin:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(FenxiaoYongjin fenxiaoYongjin, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, fenxiaoYongjin)){
			return form(fenxiaoYongjin, model);
		}
		if(!fenxiaoYongjin.getIsNewRecord()){//编辑表单保存
			FenxiaoYongjin t = fenxiaoYongjinService.get(fenxiaoYongjin.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(fenxiaoYongjin, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			fenxiaoYongjinService.save(t);//保存
		}else{//新增表单保存
			fenxiaoYongjinService.save(fenxiaoYongjin);//保存
		}
		addMessage(redirectAttributes, "保存分销佣金记录成功");
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
	}
	
	/**
	 * 删除分销佣金记录
	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:del")
	@RequestMapping(value = "delete")
	public String delete(FenxiaoYongjin fenxiaoYongjin, RedirectAttributes redirectAttributes) {
		fenxiaoYongjinService.delete(fenxiaoYongjin);
		addMessage(redirectAttributes, "删除分销佣金记录成功");
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
	}
	
	/**
	 * 批量删除分销佣金记录
	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			fenxiaoYongjinService.delete(fenxiaoYongjinService.get(id));
		}
		addMessage(redirectAttributes, "删除分销佣金记录成功");
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(FenxiaoYongjin fenxiaoYongjin, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分销佣金记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FenxiaoYongjin> page = fenxiaoYongjinService.findPage(new Page<FenxiaoYongjin>(request, response, -1), fenxiaoYongjin);
    		new ExportExcel("分销佣金记录", FenxiaoYongjin.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出分销佣金记录记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FenxiaoYongjin> list = ei.getDataList(FenxiaoYongjin.class);
			for (FenxiaoYongjin fenxiaoYongjin : list){
				try{
					fenxiaoYongjinService.save(fenxiaoYongjin);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条分销佣金记录记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条分销佣金记录记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入分销佣金记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
    }
	
	/**
	 * 下载导入分销佣金记录数据模板
	 */
	@RequiresPermissions("fenxiaoyongjin:fenxiaoYongjin:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "分销佣金记录数据导入模板.xlsx";
    		List<FenxiaoYongjin> list = Lists.newArrayList(); 
    		new ExportExcel("分销佣金记录数据", FenxiaoYongjin.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fenxiaoyongjin/fenxiaoYongjin/?repage";
    }
	
	
	

}