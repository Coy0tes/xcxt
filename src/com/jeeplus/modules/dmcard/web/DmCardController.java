/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmcard.web;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.dmcardnum.entity.DmCardNum;
import com.jeeplus.modules.dmcardnum.service.DmCardNumService;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.dmyewu.entity.DmYewu;
import com.jeeplus.modules.dmyewu.service.DmYewuService;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;

/**
 * 套餐卡管理Controller
 * @author mxc
 * @version 2017-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/dmcard/dmCard")
public class DmCardController extends BaseController {

	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private DmCardNumService dmCardNumService;
	@Autowired
	private YwLogService ywLogService;
	@Autowired
	private DmYewuService dmYewuService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	
	
	/**
	 * 生成15位随机数，并且做重复校验
	 * @param num 
	 * @param id 
	 * @param dmCardLists 
	 * @return 
	 */
	public String randomNum(){
 		DmCard dmCard = new DmCard();
		Random random = new Random();
		Boolean b = new Boolean(true);
		int randNum = 0;
		String card = null;
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");	//设置日期格式--使用当前系统日期作为套餐卡号
		
		
		while(b){
			List<DmCard> dmCardLists = dmCardService.findList(dmCard);
			// 生成一个随机数
			randNum = random.nextInt(99999-10000+1)+10000;	//随机生成10000到99999的随机数
			// 作为一个判断值，判断是否重新生成随机数
			boolean isexists = false;
			
			String nn = String.valueOf(randNum);				//int转String
			String cardid = df.format(new Date())+nn;	// new Date()为获取当前系统时间
			
			// 循环随机数跟数据库里的卡号后五位是否相等
			for(DmCard each : dmCardLists){
				// 数据库的套餐卡号非空校验
				String eachcardid = each.getCardid();
				if(eachcardid != null && eachcardid.length()>5){
					if(eachcardid.substring(eachcardid.length()-5, eachcardid.length()).equals(nn)){
						// 如果跟数据库cardid后五位有重复，再生成一个随机数
						isexists = true;
						break;
					}
				}
			}
			
			// 判断是否又生成了一个随机数。如果生成随机数，则返回一个true继续循环，同时num重新赋值；如果没有重新生成随机数，返回false，退出while循环
			if(isexists){
				b = true;
			}else{
				b = false;
			}
			card = cardid;
		}
		
		return card;
	}
	
	
	
	@ModelAttribute
	public DmCard get(@RequestParam(required=false) String id) {
		DmCard entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dmCardService.get(id);
		}
		if (entity == null){
			entity = new DmCard();
		}
		return entity;
	}
	
	/**
	 * 套餐卡管理列表页面
	 */
	@RequiresPermissions("dmcard:dmCard:list")
	@RequestMapping(value = {"list", ""})
	public String list(DmCard dmCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		DmPackage dmPackage = new DmPackage();
//		dmPackage.setIson("1");
		dmPackage.setStatus("1");
		List<DmPackage> dm = dmPackageService.findList(dmPackage);
		Page<DmCard> page = dmCardService.findPage(new Page<DmCard>(request, response), dmCard);
		
		model.addAttribute("dmPackageName", dm);
		model.addAttribute("page", page);
		return "modules/dmcard/dmCardList";
	}

	/**
	 * 查看，增加，编辑套餐卡管理表单页面
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DmCard dmCard, Model model) {
		DmYewu dmYewu = new DmYewu();
		String num = null ;
		if(dmCard.getId()==null){
			num = randomNum();
			dmCard.setCardid(num);
		}
		DmPackage dmPackage = new DmPackage();
		List<DmPackage> dm = dmPackageService.findListCard(dmPackage);
		List<DmYewu> dmYewuLists = dmYewuService.findList(dmYewu);
		model.addAttribute("dmYewuLists", dmYewuLists);
		model.addAttribute("dmPackageName", dm);	// 所属套餐下拉列表
		model.addAttribute("dmCard", dmCard);
		return "modules/dmcard/dmCardForm";
	}
	
	/**
	 * 选择套餐时异步获取套餐销售价格
	 * @param packageid
	 * @return
	 */
	@RequestMapping(value = "packageprice")
	@ResponseBody
	public Map<String, String> packageprice(String packageid){
		Map<String, String> map = new HashMap<String, String>();
		DmPackage dmPackage = new DmPackage();
		dmPackage = dmPackageService.get(packageid);
		BigDecimal big = new BigDecimal(dmPackage.getXsprice());
		String str = big.toString();
		map.put("packageprice", str);
		map.put("pscs", String.valueOf(dmPackage.getPscs()));
		return map;
	}
	
	
	/**
	 * 修改可用配送次数界面
	 * @param dmCard
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "numForm")
	public String numForm(DmCard dmCard, HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("dmCard", dmCard);
		return "modules/dmcard/dmCardNumForm";
	}
	
	/**
	 * 套餐配送次数修改
	 * 主页面显示最新记录
	 * 提交后更改信息保存在新的表里
	 * @param dmCard
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "numSave")
	public String numSave(DmCard dmCard, Model model, RedirectAttributes redirectAttributes) throws Exception{
		DmCardNum dmCardNum = new DmCardNum();
		User user = UserUtils.getUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//设置日期格式--使用当前系统日期作为套餐卡号
		
		//赋值到修改记录表
		dmCardNum.setCardid(dmCard.getId());
		dmCardNum.setCardidname(dmCard.getCardid());
		dmCardNum.setNumhou(dmCard.getNumshengyu());
		dmCardNum.setNumremarks(dmCard.getNumremarks());
		dmCardNum.setNumadmin(user.getId());
		dmCardNum.setNumtime(df.format(new Date()));
		
		//赋值到当前表
		dmCard.setNumadmin(user.getId());
		dmCard.setNumtime(df.format(new Date()));
		
		dmCardService.save(dmCard);//保存
		dmCardNumService.save(dmCardNum);
		
		// 操作日志保存
		YwLog log = new YwLog();
		String logs = "操作员："+user.getName()+"，将套餐卡（"+ dmCard.getCardid()+"）的剩余次数修改为："+dmCard.getNumshengyu();
		
		log.setModulename("套餐卡管理");
		log.setLog(logs.toString());
		ywLogService.save(log);
		
					
		addMessage(redirectAttributes, "修改成功！");
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	
	/**
	 * 从会员表--》查看套餐卡==》修改可用配送次数界面
	 * @param dmCard
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "memberCardNumForm")
	public String memberCardNumForm(DmCard dmCard, HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("dmCard", dmCard);
		return "modules/member/memberCardNumForm";
	}
	
	/**
	 * 从会员表--》查看套餐卡==》套餐配送次数修改保存
	 * 主页面显示最新记录
	 * 提交后更改信息保存在新的表里
	 * @param dmCard
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "memberCardNumFormSave")
	public String memberCardNumFormSave(DmCard dmCard, Model model, HttpServletRequest request ,HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception{
		DmCardNum dmCardNum = new DmCardNum();
		User user = UserUtils.getUser();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//设置日期格式--使用当前系统日期作为套餐卡号
		
		//赋值到修改记录表
		dmCardNum.setCardid(dmCard.getId());
		dmCardNum.setCardidname(dmCard.getCardid());
		dmCardNum.setNumhou(dmCard.getNumshengyu());
		dmCardNum.setNumremarks(dmCard.getNumremarks());
		dmCardNum.setNumadmin(user.getId());
		dmCardNum.setNumtime(df.format(new Date()));
		
		//赋值到当前表
		dmCard.setNumadmin(user.getId());
		dmCard.setNumtime(df.format(new Date()));
		
		dmCardService.save(dmCard);//保存
		dmCardNumService.save(dmCardNum);
		
		// 操作日志保存
		YwLog log = new YwLog();
		String logs = "操作员："+user.getName()+"，将套餐卡（"+ dmCard.getCardid()+"）的剩余次数修改为："+dmCard.getNumshengyu();
		
		log.setModulename("套餐卡管理");
		log.setLog(logs.toString());
		ywLogService.save(log);
		
		addMessage(redirectAttributes, "修改成功！");
		
		Page<DmCard> page = dmCardService.findPage(new Page<DmCard>(request, response), dmCard);
		model.addAttribute("page", page);
		return "modules/member/memberPackageCard";
	}
	

	/**
	 * 保存套餐卡管理
	 */
	@RequiresPermissions(value={"dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DmCard dmCard, Model model, RedirectAttributes redirectAttributes) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!beanValidator(model, dmCard)){
			return form(dmCard, model);
		}
		if(!dmCard.getIsNewRecord()){//编辑表单保存
 			// 套餐卡如果修改成未激活，把套餐卡绑定会员去掉
 			if(dmCard.getStatus().equals("0")){
 				dmCard.setMember(null);
 			}
			dmCardService.save(dmCard);//保存
		}else{//新增表单保存
			// 新增套餐卡如果激活，添加套餐卡激活时间
			if(dmCard.getStatus().equals("1")){
				dmCard.setActivetime(df.format(new Date()));
			}
			
			// 将配送次数附给剩余次数，使用次数为0
			dmCard.setNumshengyu(dmCard.getNumpscs());
			dmCard.setNumuse("0");
			
			// 如果选择业务员，则将业务员编号作为卡号的前两位，如果不选，默认为00
			if(StringUtils.isEmpty(dmCard.getDmYewu().getId())){
				dmCard.setCardid("00"+dmCard.getCardid());
 			}else{
 				DmYewu dmYewu = dmYewuService.get(dmCard.getDmYewu().getId());
 				dmCard.setCardid(dmYewu.getNum()+dmCard.getCardid());
			}
			
			dmCardService.save(dmCard);//保存
		}
		addMessage(redirectAttributes, "保存套餐卡管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	/**
	 * 批量生成套餐卡表单页面
	 * @param dmCard
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "zdForm")
	public String zdForm(DmCard dmCard, Model model) {
		DmPackage dmPackage = new DmPackage();
		DmYewu dmYewu = new DmYewu();
		List<DmPackage> dm = dmPackageService.findListCard(dmPackage);
		List<DmYewu> dmYewuLists = dmYewuService.findList(dmYewu);
		
		model.addAttribute("dmYewuLists", dmYewuLists);
		model.addAttribute("dmPackageName", dm);
		model.addAttribute("dmCard", dmCard);
		return "modules/dmcard/zdscDmCardForm";
	}
	
	/**
	 * 批量生成套餐卡处理
	 * @param dmCard
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "zdscSave")
	public String zdscSave(DmCard dmCard, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		// 得到当前套餐的信息
		DmPackage dmPackage = new DmPackage();
		dmPackage.setId(dmCard.getPackageid());
		DmPackage dmPackageOne = dmPackageService.get(dmPackage.getId());
		for(int i = 0; i < dmCard.getCardNum(); i++){
			
			// 将套餐的配送次数附给套餐卡的剩余次数,使用次数附0，
			dmCard.setNumshengyu(dmCard.getNumpscs());
			dmCard.setNumuse("0");
			
			// 套餐的价格附给套餐卡
//			DmPackage dmPack = dmPackageService.get(dmCard.getPackageid());
//			BigDecimal decimal = new BigDecimal(dmPack.getXsprice());
//			String xsprice = decimal.toString();
//			dmCard.setPrice(xsprice);
			
			// 调用randomNum（）方法生成套餐卡号
			String num = randomNum();
			// 业务员处理：如果不选业务员，默认00为卡号开头，如果选业务员，则将业务员编号前两位作为卡号开头
			if(StringUtils.isEmpty(dmCard.getDmYewu().getId())){
				dmCard.setCardid("00"+num);
 			}else{
 				DmYewu dmYewu = dmYewuService.get(dmCard.getDmYewu().getId());
 				dmCard.setCardid(dmYewu.getNum()+num);
			}
//			dmCard.setCardid(num);		// 套餐卡卡号保存
			dmCard.setStatus("0");		// 状态保存
			
			// 将套餐的分佣提成信息附给生成的套餐卡
			dmCard.setFxtcbl(dmPackageOne.getFxtcbl());
			dmCard.setFxtcje(dmPackageOne.getFxtcje());
			dmCard.setFxtclx(dmPackageOne.getFxtclx());
			
			dmCardService.saveCard(dmCard);//保存
		}
		addMessage(redirectAttributes, "批量生成套餐卡成功");
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	/**
	 * 批量修改佣金提成页面
	 */
	@RequiresPermissions(value={"dmcard:dmCard:view","dmcard:dmCard:add","dmcard:dmCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "fxtcForm")
	public String fxtcForm(String ids, RedirectAttributes redirectAttributes,Model model){
//		String idArray[] =ids.split(",");
	
		
		/*	批量逐条修改
//		ArrayList<DmCard> lists = new ArrayList<DmCard>();
//		// 根据套餐卡id查询出每条套餐卡的信息
//		for(String id : idArray){
//			
//			DmCard dmCard = dmCardService.get(id);
//			lists.add(dmCard);
//			System.out.println(dmCard);
//		}
//		
//		model.addAttribute("lists", lists);	//批量逐条修改
*/
		model.addAttribute("ids",ids);
		return "modules/dmcard/dmCardFenyongForm";
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
	public String fxtcSave(String ids,DmCard dmCard, RedirectAttributes redirectAttributes,Model model){
		String idArray[] =ids.split(",");
		// 根据套餐卡id查询出每条套餐卡的信息
		for(String id : idArray){
			 System.out.println(id);
			DmCard dm = dmCardService.get(id);
			dm.setFxtclx(dmCard.getFxtclx());
			dm.setFxtcbl(dmCard.getFxtcbl());
			dm.setFxtcje(dmCard.getFxtcje());
			dmCardService.save(dm);
		}
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	/**
	 * 删除套餐卡管理
	 */
	@RequiresPermissions("dmcard:dmCard:del")
	@RequestMapping(value = "delete")
	public String delete(DmCard dmCard, RedirectAttributes redirectAttributes) {
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		
		dmCardService.delete(dmCard);
		
		String cardid = "套餐卡号:"+dmCard.getCardid();
		String packagename = ", 所属套餐："+dmCard.getPackageName();
		String member = ", 所属会员："+dmCard.getMemberName();
		strbuf.append(" ["+cardid+packagename+member+"]");
		System.out.println(strbuf);
		
		String str = "操作人："+user.getName()+" 删除套餐卡："+strbuf;
		ywLog.setModulename("套餐卡管理");
		ywLog.setLog(str);
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除套餐卡管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	/**
	 * 批量删除套餐卡管理
	 */
	@RequiresPermissions("dmcard:dmCard:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		
		for(String id : idArray){
			DmCard dmCard = dmCardService.get(id);
			dmCardService.delete(dmCard);
			
			String cardid = "套餐卡号:"+dmCard.getCardid();
			String packagename = ", 所属套餐："+dmCard.getPackageName();
			String member = ", 所属会员："+dmCard.getMemberName();
			strbuf.append(" ["+cardid+packagename+member+"]");
			System.out.println(strbuf);
			
		}
		String str = "操作人："+user.getName()+" 批量删除套餐卡："+strbuf;
		System.out.println(str);
		ywLog.setModulename("套餐卡管理");
		ywLog.setLog(str);
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除套餐卡管理成功");
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dmcard:dmCard:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DmCard dmCard, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐卡管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<DmCard> page = dmCardService.findPage(new Page<DmCard>(request, response, -1), dmCard);
            List<DmCard> lists = dmCardService.findTimesList(dmCard);	// 导出单独写方法，内有某段时间内套餐卡使用次数控制
            
//            if(!dmCard.getStartTime().equals("") || !dmCard.getEndTime().equals("")){
    			
    			GoodsOrder goodsOrder = new GoodsOrder();
    			goodsOrder.setBegintime(dmCard.getStartTime());
    			goodsOrder.setEndtime(dmCard.getEndTime());
    			
    			for(DmCard dm:lists){
    				goodsOrder.setCardid(dm.getId());
    				
    				// 导出获得规定时间内该套餐卡下的菜品订单信息		相关订单
    				List<GoodsOrder> goodsOrderlists = goodsOrderService.findTimeList(goodsOrder);
    				String thisNum = String.valueOf(goodsOrderlists.size());
    				System.out.println(thisNum);
    				dm.setThisNum(thisNum);
    			}
//    		}
            
    		new ExportExcel("套餐卡管理", DmCard.class).setDataList(lists).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出套餐卡管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("dmcard:dmCard:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DmCard> list = ei.getDataList(DmCard.class);
			for (DmCard dmCard : list){
				try{
					dmCardService.save(dmCard);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条套餐卡管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条套餐卡管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入套餐卡管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
    }
	
	/**
	 * 下载导入套餐卡管理数据模板
	 */
	@RequiresPermissions("dmcard:dmCard:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "套餐卡管理数据导入模板.xlsx";
    		List<DmCard> list = Lists.newArrayList(); 
    		new ExportExcel("套餐卡管理数据", DmCard.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dmcard/dmCard/?repage";
    }
	
	
	/**
	 * 选择所属会员
	 */
	@RequestMapping(value = "selectmember")
	public String selectmember(Member member, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Member> page = dmCardService.findPageBymember(new Page<Member>(request, response),  member);
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
		model.addAttribute("obj", member);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	
	/**
	 * 选择所属业务员
	 */
	@RequestMapping(value = "selectyewuyuan")
	public String selectyewu(DmYewu dmYewu, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DmYewu> page = dmYewuService.findPageBydmYewu(new Page<DmYewu>(request, response),  dmYewu);
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
		model.addAttribute("obj", dmYewu);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
	/**
	 * APP接口，生成前台使用的订单号17位,00+年份后2位+月日十分+5位随机数
	 * @return
	 */
	@RequestMapping(value = "getPkgCardId")
	@ResponseBody
	public String getPkgCardId(){
		String prefix = "00";
 		DmCard dmCard = new DmCard();
		Random random = new Random();
		Boolean b = new Boolean(true);
		int randNum = 0;
		String card = null;
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmm");	//设置日期格式--使用当前系统日期作为套餐卡号
		
		
		while(b){
			List<DmCard> dmCardLists = dmCardService.findList(dmCard);
			// 生成一个随机数
			randNum = random.nextInt(99999-10000+1)+10000;	//随机生成10000到99999的随机数
			// 作为一个判断值，判断是否重新生成随机数
			boolean isexists = false;
			
			String nn = String.valueOf(randNum);				//int转String
			String cardid = df.format(new Date())+nn;	// new Date()为获取当前系统时间
			
			// 循环随机数跟数据库里的卡号后五位是否相等
			for(DmCard each : dmCardLists){
				// 数据库的套餐卡号非空校验
				String eachcardid = each.getCardid();
				if(eachcardid != null){
					if(eachcardid.substring(eachcardid.length()-5, eachcardid.length()).equals(nn)){
						// 如果跟数据库cardid后五位有重复，再生成一个随机数
						isexists = true;
						break;
					}
				}
			}
			
			// 判断是否又生成了一个随机数。如果生成随机数，则返回一个true继续循环，同时num重新赋值；如果没有重新生成随机数，返回false，退出while循环
			if(isexists){
				b = true;
			}else{
				b = false;
			}
			card = cardid;
		}
		
		return prefix+card;
	}

}