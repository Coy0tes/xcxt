/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.web;

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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.dmpackage.dao.DmPackageGoodsZengsongDao;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.entity.DmPackageGoodsZengsong;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.goods.dao.GoodsDao;
import com.jeeplus.modules.goods.entity.Goods;
import com.jeeplus.modules.goods.entity.GoodsPojo;
import com.jeeplus.modules.goods.service.GoodsService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.xuancai.dao.XuancaiGoodsDao;
import com.jeeplus.modules.xuancai.entity.Xuancai;
import com.jeeplus.modules.xuancai.entity.XuancaiGoods;
import com.jeeplus.modules.xuancai.service.XuancaiService;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 选菜管理Controller
 * @author mxc
 * @version 2017-06-17
 */
@Controller
@RequestMapping(value = "${adminPath}/xuancai/xuancai")
public class XuancaiController extends BaseController {

	@Autowired
	private XuancaiService xuancaiService;
	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private YwLogService ywLogService;
	@Autowired
	private XuancaiGoodsDao xuancaiGoodsDao;
	@Autowired
	private DmPackageGoodsZengsongDao dmZengSongDao;
	
	@ModelAttribute
	public Xuancai get(@RequestParam(required=false) String id) {
		Xuancai entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = xuancaiService.get(id);
		}
		if (entity == null){
			entity = new Xuancai();
		}
		return entity;
	}
	
	/**
	 * 选菜管理列表页面
	 */
	@RequiresPermissions("xuancai:xuancai:list")
	@RequestMapping(value = {"list", ""})
	public String list(Xuancai xuancai, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Xuancai> page = xuancaiService.findPage(new Page<Xuancai>(request, response), xuancai); 
		model.addAttribute("page", page);
		return "modules/xuancai/xuancaiList";
	}

	/**
	 * 查看，编辑选菜管理表单页面
	 */
	@RequiresPermissions(value={"xuancai:xuancai:view","xuancai:xuancai:add","xuancai:xuancai:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Xuancai xuancai, Model model) {
		DmPackage dmPackage = new DmPackage();
		Goods goods = new Goods();
		
		//套餐 只显示已上架跟启动的套餐，"1" 代表套餐 启用 已上架  "0" 代表未启用 未上架
		dmPackage.setStatus("1");
		List<DmPackage> dm = dmPackageService.findList(dmPackage);	// 上架的套餐
		
		List<DmPackage> packageList = new ArrayList<DmPackage>();
		for(DmPackage packages:dm){
			String packname = packages.getName();
			String danshuang = packages.getDanshuang().equals("0")?"双配/周":"单配/周";
			
			packages.setName(packname + " | " + danshuang);
			packageList.add(packages);
		}
		
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
		
		// 获取套餐的子表固定菜品信息
		List<Goods> goodsList = new ArrayList<Goods>();
		DmPackage dmp = new DmPackage();
		// 判断套餐子表里有没有菜品
		if(xuancai.getPackageid()!=null){
			dmp.setId(xuancai.getPackageid());
			// 得到主表信息，通过主表得到子表固定菜品的信息
			dmp = dmPackageService.get(dmp.getId());
			// 获取每个菜品的信息
			for(DmPackageGoodsZengsong each:dmp.getDmPackageGoodsZengsongList()){
				Goods g = goodsService.get(each.getGoodsid());
				goodsList.add(g);
			}
			
			model.addAttribute("dmZengsong", goodsList);
		}
		
		model.addAttribute("xuancai", xuancai);
		model.addAttribute("dmPackage",packageList);
		model.addAttribute("goods",gpj);	// 查看或编辑显示子表菜品
		model.addAttribute("goodson",gpjon);	// 查看或编辑显示子表菜品
		return "modules/xuancai/xuancaiForm";
	}
	
//	/**
//	 * 增加选菜管理表单页面
//	 * @param xuancai
//	 * @param model
//	 * @return
//	 */
//	@RequiresPermissions(value={"xuancai:xuancai:view","xuancai:xuancai:add","xuancai:xuancai:edit"},logical=Logical.OR)
//	@RequestMapping(value = "addForm")
//	public String addForm(Xuancai xuancai, Model model) {
//		DmPackage dmPackage = new DmPackage();
//		Goods goods = new Goods();
//		
//		//套餐 只显示已上架跟启动的套餐，"1" 代表套餐 启用 已上架  "0" 代表未启用 未上架
//		dmPackage.setStatus("1");
//		goods.setDjkc(0);
//		goods.setIson("1");
//		List<DmPackage> dm = dmPackageService.findList(dmPackage);	// 上架的套餐
//		List<DmPackage> packageList = new ArrayList<DmPackage>();
//		for(DmPackage packages:dm){
//			String packname = packages.getName();
//			String danshuang = packages.getDanshuang().equals("0")?"双配/周":"单配/周";
//			
//			packages.setName(packname + " | " + danshuang);
//			packageList.add(packages);
//		}
//		List<Goods> gds = goodsService.findList(goods);
//		List<GoodsPojo> gpj = Lists.newArrayList();		//
//		for(Goods g : gds){
//			GoodsPojo pj = new GoodsPojo();
//			pj.setId(g.getId());
//			pj.setName(g.getName());
//			pj.setGuige(g.getGuige());
//			gpj.add(pj);
//		}
//		
//		//获取上架的所有菜品
//		goods.setIson("1");
//		List<Goods> gdson = goodsService.findList(goods);
//		List<GoodsPojo> gpjon = Lists.newArrayList();		//
//		for(Goods g : gdson){
//			GoodsPojo pj = new GoodsPojo();
//			pj.setId(g.getId());
//			pj.setName(g.getName());
//			pj.setGuige(g.getGuige());
//			gpjon.add(pj);
//		}
//		
			// 获取套餐的子表固定菜品信息
//			List<Goods> goodsList = new ArrayList<Goods>();
//			DmPackage dmp = new DmPackage();
//			// 判断套餐子表里有没有菜品
//			if(xuancai.getPackageid()!=null){
//				dmp.setId(xuancai.getPackageid());
//				// 得到主表信息，通过主表得到子表固定菜品的信息
//				dmp = dmPackageService.get(dmp.getId());
//				// 获取每个菜品的信息
//				for(DmPackageGoodsZengsong each:dmp.getDmPackageGoodsZengsongList()){
//					Goods g = goodsService.get(each.getGoodsid());
//					goodsList.add(g);
//				}
//				
//				model.addAttribute("dmZengsong", goodsList);
//			}
//	
//		model.addAttribute("xuancai", xuancai);
//		model.addAttribute("dmPackage",dm);
//		model.addAttribute("goods",gpj);	// 查看或编辑显示子表菜品
//		model.addAttribute("goodson",gpjon);	// 查看或编辑显示子表菜品
//		return "modules/xuancai/xuancaiForm";
//	}
	

	/**
	 * 保存选菜管理
	 */
	@RequiresPermissions(value={"xuancai:xuancai:add","xuancai:xuancai:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Xuancai xuancai, Model model, RedirectAttributes redirectAttributes) throws Exception{
		String msg  ="保存选菜管理成功";
		if (!beanValidator(model, xuancai)){
			return form(xuancai, model);
		}
		if(!xuancai.getIsNewRecord()){//编辑表单保存
			boolean b = true;
			if(!xuancai.getIson().equals("0")){
				List<Xuancai> lists = xuancaiService.findPackageList(xuancai);
				for(Xuancai each:lists){
					if(each.getIson().equals("1")){
						xuancai.setIson("0");
						msg = "该套餐已上架,已将【"+xuancai.getTitle()+"】菜单下架";
						b=false;
					}
				}
			}
			if(b){
				msg = "保存选菜管理成功";
			}
			Xuancai t = xuancaiService.get(xuancai.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(xuancai, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			xuancaiService.save(t);//保存
		}else{//新增表单保存
			boolean b = true;
			if(!xuancai.getIson().equals("0")){
				List<Xuancai> lists = xuancaiService.findPackageList(xuancai);
				for(Xuancai each:lists){
					if(each.getIson().equals("1")){
						xuancai.setIson("0");
						msg = "该套餐已上架,已将【"+xuancai.getTitle()+"】菜单下架";
						b=false;
					}
				}
			}
			if(b){
				msg = "保存选菜管理成功";
			}
		}
		
		xuancaiService.save(xuancai);//保存
		addMessage(redirectAttributes, msg);
		
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
	}
	
	/**
	 * 效验多个相同套餐只有一个上架
	 * @param xuancai
	 * @return
	 */
	@RequestMapping(value="isPackage")
	@ResponseBody
	public Map<String, Object> isPackage(Xuancai xuancai){
		Map<String, Object> map = new HashMap<String, Object>();
		int i = 0;
		// 根据packageid得到有关套餐的信息
		List<Xuancai> lists = xuancaiService.findList(xuancai);
				// 循环寻找是否有多个上架的，如果有超过一个上架的，给提示
				for(Xuancai list : lists){
					if(list.getIson().equals("1")){// 如果有上架的，i+1
						i++;
						map.put("code","11");
						map.put("msg","此套餐已有上架，请注意操作！");
					}
					if(i>2){ //	如果超过来年改革上架的
					}
				}
		return map;
	}
	
	/**
	 * 根据套餐id查出相关子表固定选菜的菜品
	 * @param xuancai
	 * @return
	 */
	@RequestMapping(value="getPackageGoods")
	@ResponseBody
	public Map<String, Object> getPackageGoods(Xuancai xuancai){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Goods> goodsList = new ArrayList<Goods>();
		DmPackage dmPackage = new DmPackage();
		dmPackage.setId(xuancai.getPackageid());
		// 得到主表信息，通过主表得到子表固定菜品的信息
		dmPackage = dmPackageService.get(dmPackage.getId());
		// 获取每个菜品的信息
		for(DmPackageGoodsZengsong each:dmPackage.getDmPackageGoodsZengsongList()){
			Goods goods = goodsService.get(each.getGoodsid());
			goodsList.add(goods);
		}
		map.put("dmZengsong", goodsList);
		return map;
	}
	
	/**
	 * 批量增加页面
	 * 冻结数量默认为 0 ，在 js 中设置
	 * @param goods
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"xuancai:xuancai:add","xuancai:xuancai:edit"},logical=Logical.OR)
	@RequestMapping(value = "plList")
	public String plList(Goods goods, HttpServletRequest request, HttpServletResponse response, Model model){
//		Goods goods = new Goods();
		goods.setIson("1");
//		List<Goods> list = goodsService.findList(goods);
		Page<Goods> page = goodsService.findPage(new Page<Goods>(request, response), goods); 
		
		model.addAttribute("page", page);
		return "modules/xuancai/xuancaiPlList";
	}
	
	
	/**
	 * 套餐子表选菜验证 
	 * 菜品是否下架，是否存在
	 * @param goods
	 * @param request
	 * @param response
	 * @param model
	 * @return 非00，验证不通过；00，验证通过。
	 */
	@RequiresPermissions(value={"xuancai:xuancai:add","xuancai:xuancai:edit"},logical=Logical.OR)
	@RequestMapping(value = "isonList")
	@ResponseBody
	public Map<String, Object> isonList(Goods goods, HttpServletRequest request, HttpServletResponse response, Model model){
		String ids = request.getParameter("ids");
		Map<String, Object> map = new HashMap<String, Object>();
		String[] i = ids.split(",");
		for(int j = 0; j<i.length; j++){
			String id = i[j];
			if(!id.equals(null) && !id.equals("")){
				
				goods.setId(id);
				
				Goods good = goodsService.get(goods);
				
				if(good == null){
					map.put("code", "99");
					map.put("msg", "存在已删除的菜品！");
					return map;
				}else if(good.getIson().equals("0")){
					map.put("code", "88");
					map.put("msg", "菜品【"+good.getName()+" | "+good.getGuige()+"】已下架，不能提交");
					return map;
				}
			}else {
				map.put("code", "22");
				map.put("msg", "菜品不能为空！");
				return map;
			}
		}
		map.put("code", "00");
		map.put("msg", "数据提交成功！");
		return map;
	}
	
	/**
	 * 售罄处理
	 * @param xuancai
	 * @return
	 */
	@RequestMapping(value = "shouqing")
	@ResponseBody
	public Map<String, String> shouqing(String zbid,Model model){
		 Map<String,String> map = new HashMap<String, String>();
		 
//		 XuancaiGoods xuancaiGoods = xuancaiGoodsDao.get(zbid);
//		 
//		 // 以冻结库存数量持平库存数量
//		 xuancaiGoodsDao.saveSq(xuancaiGoods);
//		 
//		 xuancaiGoods = xuancaiGoodsDao.get(zbid);
//		 
//		 map.put("code", String.valueOf(xuancaiGoods.getKcsl()));
//		 map.put("msg", "售罄成功!");
		return map;
	}
	
	/**
	 * 检测选菜子表记录中的库存是否小于冻结库存
	 * @param zbid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkDjck")
	@ResponseBody
	public Map<String, String> checkDjck(Model model,HttpServletRequest request){
		 Map<String,String> map = new HashMap<String, String>();
		 
		 String code = "00";
		 String msg = "";
		 String newdjsl = "";
		 String zbid= "";
		 
		 String ids = request.getParameter("ids");
		 List<Map> list = JSON.parseArray(ids, Map.class);
		 for(Map eachmap : list){
			 String id = (String)eachmap.get("id");
			 Object kcslobj = eachmap.get("kcsl");
			 Integer kcsl = Integer.valueOf(kcslobj.toString());
			 
			 XuancaiGoods goods = xuancaiGoodsDao.get(id);
			 Goods good = goodsDao.get(goods.getGoodsid());
			 Integer djsl = good.getDjkc();
			 if(djsl > kcsl){
				 code = "99";
				 msg = "菜品【"+good.getName()+" | "+good.getGuige()+"】的冻结库存("+djsl+")大于库存数量("+kcsl+")，不能提交!";
				 newdjsl = String.valueOf(djsl);
				 zbid = id;
				 break;
			 }
		 }
		 
		map.put("code", code);
		map.put("msg", msg);
 		map.put("newdjsl", newdjsl);
		map.put("zbid", zbid);
		return map;
	}
	
	/**
	 * 效验套餐是否下架 冻结库存是否为0，套餐下架并且库存为零才能删除，如果不能删除，页面重新赋值数据库最新的库存跟冻结库存
	 * @param request
	 * @return
	 */
	@RequestMapping(value="checkDelete")
	@ResponseBody
	public Map<String, String> checkDelete(HttpServletRequest request){
		String msg1 = "";
		String msg2 = "";
		String code = "00";
		Integer djkc;
		Integer kcsl;
		Map<String, String> map = new HashMap<String, String>();
		String goodsid = request.getParameter("idd");	// 子表id
		String id = request.getParameter("packid");	// 主表id
		
		// 根据主表id查询该套餐是否上架
		Xuancai xuancai = xuancaiService.get(id);
		if(xuancai.getIson().equals("1")){
			code = "11";
			msg1 = "【"+xuancai.getPackageName()+"】未下架 \r\n";
		}
		
		// 根据子表id查询该菜品库存是否为0
		id = goodsid;
		Goods goods = goodsDao.get(id);
		djkc = goods.getDjkc();
		kcsl = goods.getKcsl();
		if(goods.getDjkc()!=0){
//			// 根据goodsid查询菜品的名称跟规格
			code = "11";
			msg2 = "【"+goods.getName()+" | "+goods.getGuige()+"】冻结库存为【"+goods.getDjkc()+"】,不能删除！";
		}
		map.put("djsl", String.valueOf(djkc));
		map.put("kcsl", String.valueOf(kcsl));
		map.put("code", code);
		map.put("msg1", msg1);
		map.put("msg2", msg2);
		
//		id = goodsid;
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
		String ids = request.getParameter("ids");		// 从页面上获得ids
		List<Map> list = JSON.parseArray(ids, Map.class);	// 将ids转换成数组
		for(Map eachid : list){
			String id = (String) eachid.get("id");		// Object java.util.Map.get(Object key)
			Xuancai xuancai = xuancaiService.get(id);
			xuancai.setIson("1");
			xuancaiService.save(xuancai);
		}
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
		String ids = request.getParameter("ids");		// 从页面上获得ids
		List<Map> list = JSON.parseArray(ids, Map.class);	// 将ids转换成数组
		for(Map eachid : list){
			String id = (String) eachid.get("id");		// Object java.util.Map.get(Object key)
			Xuancai xuancai = xuancaiService.get(id);
			xuancai.setIson("0");
			xuancaiService.save(xuancai);
		}
		map.put("msg", "批量下架成功！");
		return map;
	}
	
	
	/**
	 * 删除选菜管理
	 */
	@RequiresPermissions("xuancai:xuancai:del")
	@RequestMapping(value = "delete")
	public String delete(Xuancai xuancai, RedirectAttributes redirectAttributes) {
		xuancaiService.delete(xuancai);
		
		YwLog ywLog = new YwLog();
		XuancaiGoods xuancaiGoods = new XuancaiGoods();
		xuancaiGoods.setMainid(xuancai);
		List<XuancaiGoods> xuancaiGood = xuancaiGoodsDao.findList(xuancaiGoods);
		
		StringBuffer strbuf = new StringBuffer();
		for(XuancaiGoods xuancaigoods : xuancaiGood){
			Goods goods = new Goods();
			goods.setId(xuancaigoods.getGoodsid());
			Goods g = goodsDao.get(goods);
			strbuf.append("[蔬菜名称："+g.getName()+"，").append("菜品规格："+g.getGuige()+"]");
		}
			
		// 获得当前用户
		User user = UserUtils.getUser();
		
		// 拼接要存入的信息
		String str = "操作人："+user.getName()+"  删除套餐："+xuancai.getPackageName()+"  套餐下的菜品有："+strbuf;
		ywLog.setLog(str);
		ywLog.setModulename("会员管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除选菜管理成功");
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
	}
	
	/**
	 * 批量删除选菜管理
	 */
	@RequiresPermissions("xuancai:xuancai:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		YwLog ywLog = new YwLog();
		// 套餐+菜品
		StringBuffer strbuf = new StringBuffer();
		
		XuancaiGoods xuancaiGoods = new XuancaiGoods();
		
		for(String id : idArray){
			// 放套餐
			StringBuffer packages = new StringBuffer();
			// id是套餐的id
			Xuancai x = xuancaiService.get(id);
			System.out.println(x);
			xuancaiService.delete(x);
			// 得到套餐名
			String packageName = "套餐名称："+x.getPackageName();
			packages.append(packageName);
			System.out.println(packages);
			// 得到套餐下的 菜品
			// 根据选菜的id查找子表相关的菜品
			xuancaiGoods.setMainid(x);
			List<XuancaiGoods> xuancaiGood = xuancaiGoodsDao.findList(xuancaiGoods);
			for(XuancaiGoods xuancaigoods : xuancaiGood){
				// caipin:放菜品goods
				StringBuffer caipin = new StringBuffer();
				
				Goods goods = new Goods();
				goods.setId(xuancaigoods.getGoodsid());
				System.out.println(xuancaigoods.getGoodsid());
				Goods g = goodsDao.get(goods);
				caipin.append("蔬菜名称："+g.getName()+"，").append("菜品规格："+g.getGuige()+"");
				System.out.println(caipin);
				packages.append("["+caipin+"]");
				System.out.println(packages);
			}
			strbuf.append("【"+packages+"】");
			System.out.println(strbuf);
			
		}
		
		
		User user = UserUtils.getUser();
		//user.getName();
		// 拼接要存入的信息
		String str = "操作人："+user.getName()+"  批量删除套餐：《"+strbuf+"》  ";
		ywLog.setLog(str);
		ywLog.setModulename("会员管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除选菜管理成功");
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("xuancai:xuancai:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Xuancai xuancai, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "选菜管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Xuancai> page = xuancaiService.findPage(new Page<Xuancai>(request, response, -1), xuancai);
    		new ExportExcel("选菜管理", Xuancai.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出选菜管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("xuancai:xuancai:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Xuancai> list = ei.getDataList(Xuancai.class);//输出流导入到list中，此时list是数组
			
			for (Xuancai xuancai : list){
				try{
					xuancaiService.save(xuancai);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条选菜管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条选菜管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入选菜管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
    }
	
	/**
	 * 下载导入选菜管理数据模板
	 */
	@RequiresPermissions("xuancai:xuancai:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "选菜管理数据导入模板.xlsx";
    		List<Xuancai> list = Lists.newArrayList(); 
    		new ExportExcel("选菜管理数据", Xuancai.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/xuancai/xuancai/?repage";
    }
	
	
	

}