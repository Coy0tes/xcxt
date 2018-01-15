/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tuangoupackageorder.web;

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
import com.jeeplus.modules.tuangoupackage.service.TuangouPackageService;
import com.jeeplus.modules.tuangoupackageorder.dao.TuangouPackageOrderDetailDao;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrder;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrderDetail;
import com.jeeplus.modules.tuangoupackageorder.entity.TuangouPackageOrderExcal;
import com.jeeplus.modules.tuangoupackageorder.service.TuangouPackageOrderService;

/**
 * 团购套餐订单管理Controller
 * @author mxc
 * @version 2017-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/tuangoupackageorder/tuangouPackageOrder")
public class TuangouPackageOrderController extends BaseController {

	@Autowired
	private TuangouPackageOrderService tuangouPackageOrderService;
	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private TuangouPackageOrderDetailDao tuangouPackageOrderDetailDao;
	@Autowired
	private TuangouPackageService tuangouPackageService;
	
	@ModelAttribute
	public TuangouPackageOrder get(@RequestParam(required=false) String id) {
		TuangouPackageOrder entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tuangouPackageOrderService.get(id);
		}
		if (entity == null){
			entity = new TuangouPackageOrder();
		}
		return entity;
	}
	
	/**
	 * 团购套餐订单管理列表页面
	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:list")
	@RequestMapping(value = {"list", ""})
	public String list(TuangouPackageOrder tuangouPackageOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TuangouPackageOrder> page = tuangouPackageOrderService.findPage(new Page<TuangouPackageOrder>(request, response), tuangouPackageOrder); 
		model.addAttribute("page", page);
		return "modules/tuangoupackageorder/tuangouPackageOrderList";
	}

	/**
	 * 查看，增加，编辑团购套餐订单管理表单页面
	 */
	@RequiresPermissions(value={"tuangoupackageorder:tuangouPackageOrder:view","tuangoupackageorder:tuangouPackageOrder:add","tuangoupackageorder:tuangouPackageOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TuangouPackageOrder tuangouPackageOrder, Model model) {
		DmCard dmCard = new DmCard();
		List<TuangouPackageOrderDetail> tuangouPackageOrderDetail = tuangouPackageOrder.getTuangouPackageOrderDetailList();
		
		for(TuangouPackageOrderDetail each : tuangouPackageOrderDetail){
			// 得到套餐卡id，查询套餐卡卡号
			String id = each.getCardid();
			if(StringUtils.isNotEmpty(id)){
				dmCard.setId(id);
 				dmCard = dmCardService.get(dmCard);
				if(dmCard != null){
					tuangouPackageOrderDetail.get(0).setCardName(dmCard.getCardid());
				}
			}
			
		}
		model.addAttribute("tuangouPackageOrder", tuangouPackageOrder);
		return "modules/tuangoupackageorder/tuangouPackageOrderForm";
	}
	
	/**
	 * 保存团购套餐订单管理
	 */
	@RequiresPermissions(value={"tuangoupackageorder:tuangouPackageOrder:add","tuangoupackageorder:tuangouPackageOrder:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TuangouPackageOrder tuangouPackageOrder, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tuangouPackageOrder)){
			return form(tuangouPackageOrder, model);
		}
		if(!tuangouPackageOrder.getIsNewRecord()){//编辑表单保存
			TuangouPackageOrder t = tuangouPackageOrderService.get(tuangouPackageOrder.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tuangouPackageOrder, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tuangouPackageOrderService.save(t);//保存
		}else{//新增表单保存
			tuangouPackageOrderService.save(tuangouPackageOrder);//保存
		}
		addMessage(redirectAttributes, "保存团购套餐订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
	}
	
	/**
	 * 单条作废页面
	 * @param tuangouPackageOrder
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "zuofeiForm")
	public String zuofeiForm(TuangouPackageOrder tuangouPackageOrder, Model model){
		
		model.addAttribute("tuangouPackageOrder", tuangouPackageOrder);
		return "modules/tuangoupackageorder/tuangouPackageOrderZuofeiForm";
	}
	
	/**
	 * 作废处理
	 * @param tuangouPackageOrder
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "zuofeiSave")
	public String zuofeiSave(TuangouPackageOrder tuangouPackageOrder, Model model){
		tuangouPackageOrderService.zuogeiSave(tuangouPackageOrder);
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
	}
	
	/**
	 * 删除团购套餐订单管理
	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:del")
	@RequestMapping(value = "delete")
	public String delete(TuangouPackageOrder tuangouPackageOrder, RedirectAttributes redirectAttributes) {
		tuangouPackageOrderService.delete(tuangouPackageOrder);
		addMessage(redirectAttributes, "删除团购套餐订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
	}
	
	/**
	 * 批量删除团购套餐订单管理
	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tuangouPackageOrderService.delete(tuangouPackageOrderService.get(id));
		}
		addMessage(redirectAttributes, "删除团购套餐订单管理成功");
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TuangouPackageOrder tuangouPackageOrder, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		// 导出时间。如果为空，则不限制，如果不为空，则限制时间
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    	if(!StringUtils.isEmpty(tuangouPackageOrder.getStartTime())){
//    		tuangouPackageOrder.setStartTime(sdf.format(new Date()));
//    	}
//    	if(!StringUtils.isEmpty(tuangouPackageOrder.getEndTime())){
//    		tuangouPackageOrder.setEndTime(sdf.format(new Date()));
//    	}
		try {
			List<TuangouPackageOrderExcal> excal = new ArrayList<TuangouPackageOrderExcal>();
            String fileName = "团购套餐订单表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<TuangouPackageOrder> page = tuangouPackageOrderService.findPage(new Page<TuangouPackageOrder>(request, response, -1), tuangouPackageOrder);
            List<TuangouPackageOrder> lists = tuangouPackageOrderService.findList(tuangouPackageOrder);

            // 订单号、购买会员、购买数量、购买金额、套餐名、套餐卡号
            for(TuangouPackageOrder each : lists){
            	TuangouPackageOrderExcal tpoExacl = new TuangouPackageOrderExcal();
            	// 订单号、购买会员、购买数量、购买金额
            	tpoExacl.setDdh(each.getDdh());
            	tpoExacl.setMemberName(each.getMemberName());
            	tpoExacl.setNum(each.getNum());
            	tpoExacl.setSfprice(each.getSfprice());
            	
            	// 子表信息
            	TuangouPackageOrderDetail tuangouPackageOrderDetail = new TuangouPackageOrderDetail();
            	tuangouPackageOrderDetail.setMainid(each);
            	List<TuangouPackageOrderDetail> tpodLists = tuangouPackageOrderDetailDao.findList(tuangouPackageOrderDetail);
            	
            	// 套餐名、套餐卡号
            	StringBuffer strOrder = new StringBuffer();
            	for(TuangouPackageOrderDetail detailEach : tpodLists){
            		String orderList = new String();
            		
            		orderList = "  套餐名："+detailEach.getPackageName()+ 
            					"，套餐卡号："+detailEach.getCardName()+
            					"，单价："+detailEach.getDjprice()+"  ";
            		strOrder.append(orderList);
            	}
            	tpoExacl.setOrderList(strOrder.toString());
            	
            	excal.add(tpoExacl);
            }
            
    		new ExportExcel("团购套餐订单管理", TuangouPackageOrderExcal.class).setDataList(excal).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出团购套餐订单管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TuangouPackageOrder> list = ei.getDataList(TuangouPackageOrder.class);
			for (TuangouPackageOrder tuangouPackageOrder : list){
				try{
					tuangouPackageOrderService.save(tuangouPackageOrder);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条团购套餐订单管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条团购套餐订单管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入团购套餐订单管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
    }
	
	/**
	 * 下载导入团购套餐订单管理数据模板
	 */
	@RequiresPermissions("tuangoupackageorder:tuangouPackageOrder:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "团购套餐订单管理数据导入模板.xlsx";
    		List<TuangouPackageOrder> list = Lists.newArrayList(); 
    		new ExportExcel("团购套餐订单管理数据", TuangouPackageOrder.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tuangoupackageorder/tuangouPackageOrder/?repage";
    }
	
	
	

}