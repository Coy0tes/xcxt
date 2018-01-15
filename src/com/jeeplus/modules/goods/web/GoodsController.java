/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.goods.web;

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
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.goodscategory.entity.GoodsCategory;
import com.jeeplus.modules.goodscategory.service.GoodsCategoryService;
import com.jeeplus.modules.goodsorder.dao.GoodsOrderDetailDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderDetail;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.xuancai.dao.XuancaiGoodsDao;
import com.jeeplus.modules.xuancai.entity.XuancaiGoods;
import com.jeeplus.modules.xuancai.service.XuancaiService;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 菜品管理Controller
 * @author mxc
 * @version 2017-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/goods/goods")
public class GoodsController extends BaseController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsCategoryService goodsCategoryService;
	@Autowired
	private XuancaiService xuancaiService;
	@Autowired
	private XuancaiGoodsDao xuancaiGoodsDao;
	@Autowired
	private YwLogService ywLogService;
	@Autowired
	private GoodsOrderDetailDao goodsOrderDetailDao;
	
	@ModelAttribute
	public Goods get(@RequestParam(required=false) String id) {
		Goods entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = goodsService.get(id);
		}
		if (entity == null){
			entity = new Goods();
		}
		return entity;
	}
	
	/**
	 * 菜品管理列表页面
	 */
	@RequiresPermissions("goods:goods:list")
	@RequestMapping(value = {"list", ""})
	public String list(Goods goods, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response), goods); 
		
		model.addAttribute("page", page);
		return "modules/goods/goodsList";
	}

	/**
	 * 查看，增加，编辑菜品管理表单页面
	 */
	@RequiresPermissions(value={"goods:goods:view","goods:goods:add","goods:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Goods goods, Model model) {
		GoodsCategory goodsCategory = new GoodsCategory();
		List<GoodsCategory> fenleiList = goodsCategoryService.findList(goodsCategory);		//菜品分类
		List<Goods> list = goodsService.findList(goods);
		//排序自动附最大值
		if(goods.getSort() == null){
			int j = 0;
			if(list.size()!=0){
				for(int i = 0; i < list.size(); i++){
					
					if(list.get(i).getSort() > j){
						j = list.get(i).getSort();
						goods.setSort(j+1);
					}
				}
			}else{
				goods.setSort(j+1);
			}
		}
		model.addAttribute("fenleiList", fenleiList);
		model.addAttribute("goods", goods);
		return "modules/goods/goodsForm";
	}

	/**
	 * 动态获取睡着类别改变而改变的排序
	 * dtSort 动态排序
	 */
	public String dtSort(Goods goods, Model model){
		
		return adminPath;
	}
	
	
	/**
	 * 保存菜品管理+
	 */
	@RequiresPermissions(value={"goods:goods:add","goods:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Goods goods, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg = "保存菜品管理成功";
		if (!beanValidator(model, goods)){
			return form(goods, model);
		}
		if(!goods.getIsNewRecord()){//编辑表单保存
			Goods t = goodsService.get(goods.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(goods, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			
			//编辑是判定冻结库存和库存数量
			/*
			int kcsl = t.getKcsl();
			int djsl = t.getDjkc();
			if(djsl>kcsl){
			}*/
			
			goodsService.save(t);//保存
		}else{//新增表单保存
			//新增时，判定菜品是否已经添加过
			List<Goods> list = goodsService.findList(goods);
			if(list.size()>0){
				msg = "此套餐已配置，不能重复添加！";
			}else{
				goodsService.save(goods);//保存
			}
		}
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 批量上架
	 * @param goods
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"goods:goods:add","goods:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "plUp")
	public String plUp(String ids, RedirectAttributes redirectAttributes) {
		goodsService.plUp(ids);
		addMessage(redirectAttributes,"批量上架成功！");
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 批量下架
	 * @param goods
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"goods:goods:add","goods:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "plDown")
	public String plDown(String ids, RedirectAttributes redirectAttributes) {
		goodsService.plDown(ids);
		addMessage(redirectAttributes,"批量下架成功！");
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 批量修改分销佣金信息页面
	 * @param ids
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"goods:goods:add","goods:goods:edit"},logical=Logical.OR)
	@RequestMapping(value = "fxtcForm")
	public String fxtcForm(String ids, RedirectAttributes redirectAttributes,Model model){
		String idArray[] =ids.split(",");
	
		model.addAttribute("ids",ids);
		return "modules/goods/goodsFenyongForm";
	}
	
	/**
	 * 批量修改分销提成处理保存
	 * @param ids
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "fxtcSave")
	public String fxtcSave(String ids,Goods goods, RedirectAttributes redirectAttributes,Model model){
		
		String msg = goodsService.fxtcSave(ids, goods);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 选菜赋值菜名、规格、价格
	 * @param goods
	 * @return
	 */
	@RequestMapping(value="goodsPrice")
	@ResponseBody
	public Map<String,String> goodsPrice(Goods goods){
		Map<String, String> map = new HashMap<String, String>();
		
		goods = goodsService.get(goods);
		
		map.put("name", goods.getName());
		map.put("guige", goods.getGuige());
		map.put("price", goods.getPrice().toString());
		return map;
	}
	
	
	/**
	 * 删除菜品管理
	 */
	@RequiresPermissions("goods:goods:del")
	@RequestMapping(value = "delete")
	public String delete(Goods goods, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		XuancaiGoods xuancaiGoods = new XuancaiGoods();
		GoodsOrderDetail goodsOrderDetail = new GoodsOrderDetail();
		List<XuancaiGoods> xuancaiGoodsLists = Lists.newArrayList();
		List<GoodsOrderDetail> goodsOrderDetailLists = Lists.newArrayList();
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		
		xuancaiGoods.setGoodsid(goods.getId());
		goodsOrderDetail.setGoodsid(goods.getId());
		
		// 效验此菜品是否有冻结库存，如果有冻结库存，不能删除；
		if(goods.getDjkc()>0){
			addMessage(redirectAttributes, "菜品里有冻结库存，不能删除！");
		}else{
			
			// 删除效验：
			// 选菜里有菜品不能删除
			xuancaiGoodsLists = xuancaiGoodsDao.getGoodsid(xuancaiGoods);
			// 订单里有菜品不能删
			goodsOrderDetailLists = goodsOrderDetailDao.getGoodsid(goodsOrderDetail);
			
			if(xuancaiGoodsLists!=null && xuancaiGoodsLists.size()>0){
				addMessage(redirectAttributes, "选菜管理存在此菜品，请先删除选菜管理的菜品，再删除此菜品！");
			}else if(goodsOrderDetailLists!=null && goodsOrderDetailLists.size()>0){
				addMessage(redirectAttributes, "菜品订单存在此菜品，不能删除此菜品");
			}else{
				// 删除菜品
				goodsService.delete(goods);
				
				// 删除日志
				strbuf.append(" [菜品名称："+goods.getName()+"|"+goods.getGuige()+"]");
				
				String str = "操作人："+user.getName()+" 删除菜品 ："+strbuf;
				ywLog.setLog(str);
				ywLog.setModulename("菜品管理");
				ywLogService.save(ywLog);
				
				addMessage(redirectAttributes, "删除菜品成功!");
			}
			
			
		}
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 批量删除菜品管理
	 */
	@RequiresPermissions("goods:goods:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		StringBuffer strbuf = new StringBuffer();
		StringBuffer good = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		
		for(String id : idArray){
			Goods goods = goodsService.get(id);
			goodsService.delete(goods);
			good.append(" [菜品名称："+goods.getName()+"|"+goods.getGuige()+"]");
		}
		
		strbuf.append(" 批量删除菜品:"+good);
		System.out.println(strbuf);
		// 拼接字符串
		String str = "操作人："+user.getName() + strbuf;
		ywLog.setLog(str);
		ywLog.setModulename("菜品管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除菜品管理成功");
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("goods:goods:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Goods goods, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜品管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response, -1), goods);
    		new ExportExcel("菜品管理", Goods.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出菜品管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("goods:goods:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Goods> list = ei.getDataList(Goods.class);
			for (Goods goods : list){
				try{
					goodsService.save(goods);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条菜品管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条菜品管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入菜品管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
    }
	
	/**
	 * 下载导入菜品管理数据模板
	 */
	@RequiresPermissions("goods:goods:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "菜品管理数据导入模板.xlsx";
    		List<Goods> list = Lists.newArrayList(); 
    		new ExportExcel("菜品管理数据", Goods.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/goods/goods/?repage";
    }
	
	
	

}