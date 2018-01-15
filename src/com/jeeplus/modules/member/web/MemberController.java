/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.member.web;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import sms.SmsUtils;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.sms.SMSUtils;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.dmcard.entity.DmCard;
import com.jeeplus.modules.dmcard.service.DmCardService;
import com.jeeplus.modules.dmpackage.entity.DmPackage;
import com.jeeplus.modules.dmpackage.service.DmPackageService;
import com.jeeplus.modules.dmyewu.entity.DmYewu;
import com.jeeplus.modules.dmyewu.service.DmYewuService;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.goodsorder.service.GoodsOrderService;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.member.service.MemberService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.ywlog.entity.YwLog;
import com.jeeplus.modules.ywlog.service.YwLogService;

/**
 * 会员信息Controller
 * @author mxc
 * @version 2017-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/member/member")
public class MemberController extends BaseController {

//	private static final RedirectAttributes redirectAttributes = null;
	@Autowired
	private MemberService memberService;
	@Autowired
	private DmCardService dmCardService;
	@Autowired
	private GoodsOrderService goodsOrderService;
	@Autowired
	private DmCardService DmCardService;
	@Autowired
	private YwLogService ywLogService;
	@Autowired
	private DmPackageService dmPackageService;
	@Autowired
	private DmYewuService dmYewuService;
	
	
	@ModelAttribute
	public Member get(@RequestParam(required=false) String id) {
		Member entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = memberService.get(id);
		}
		if (entity == null){
			entity = new Member();
		}
		return entity;
	}
	
	/**
	 * 会员信息列表页面
	 * 套餐会员（packagemember）显示是：	套餐卡数量大于0
	 * 套餐会员（packagemember）显示否：	套餐卡数量等于0
	 * 业务员（yewuIsShow）显示：		会员有且只有一张套餐卡			
	 * 业务员（yewuIsShow）不显示：	会员有多于一张套餐卡			
	 */
	@RequiresPermissions("member:member:list")
	@RequestMapping(value = {"list", ""})
	public String list(Member member, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		
		/*DmCard dmCard = new DmCard();
		// 套餐会员packagemember如果有套餐卡，显示是，返回1，如果没有套餐卡，显示否，返回0
		// 判断会员有多少张会员卡，如果只有一张，则显示业务员，同时向返回1；如果多张，不显示业务员，向返回0；
		for(Member each:page.getList()){
			dmCard.setMember(each);
			List<DmCard> dmCardLists = dmCardService.findList(dmCard);
			if(dmCardLists.size()==1){
				each.setYewuIsShow("1");
			}
			if(dmCardLists.size()!=1){
				each.setYewuIsShow("0");
			}
			if(dmCardLists.size()==0){
				each.setPackagemember("0");
			}
			if(dmCardLists.size()>0){
				each.setPackagemember("1");
			}
		}*/
		
		// 需要返回的值
		// packagemember套餐会员是否显示
		// yewuIsShow 业务员是否显示
		model.addAttribute("page", page);
		return "modules/member/memberList";
	}

	/**
	 * 查看，增加，编辑会员信息表单页面
	 */
	@RequiresPermissions(value={"member:member:view","member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Member member, Model model) {
		DmCard dmCard = new DmCard();
		DmYewu dmYewu = new DmYewu();
		dmCard.setMember(member);
		List<DmYewu> dmYewuLists = dmYewuService.findList(dmYewu);
		
		// 如果会员的套餐卡不为一张时，不显示业务员
//		if(dmCardLists.size()!=1){
//			member.setYewuid("0");
//		}
		
		model.addAttribute("dmYewuLists", dmYewuLists);
		model.addAttribute("member", member);
		return "modules/member/memberForm";
	}
	
	/**
	 * 批量开通分销权限
	 * @return
	 */
	@RequestMapping(value="openQuanxian")
	public String openQuanxian(String ids, HttpServletRequest request){
		memberService.openQuanxianUpdate(ids);
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	/**
	 * 批量关闭分销权限
	 * @return
	 */
	@RequestMapping(value="closeQuanxian")
	public String closeQuanxian(String ids, HttpServletRequest request){
		memberService.closeQuanxian(ids);
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 批量修改短信提醒
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value="duanxinForm")
	public String duanxinForm(String ids,Model model){
		model.addAttribute("ids",ids);
		return "modules/member/memberDuanxinForm";
	}
	/**
	 * 批量修改微信提醒
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value="wechatForm")
	public String wechatForm(String ids, Model model){
		model.addAttribute("ids",ids);
		return "modules/member/memberWechatForm";
	}
	
	/**
	 * 批量发送短信页面
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value="smsForm")
	public String smsForm(String ids, Model model){
		model.addAttribute("ids",ids);
		return "modules/member/memberSmsForm";
	}
	
	/**
	 * 批量发送短信
	 * @param ids
	 * @param request
	 * @param member
	 * @return
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value="sendSms")
	public String sendSms(String ids, HttpServletRequest request, Member member,Model model, RedirectAttributes redirectAttributes){
		String msg = "";
		List<Member> list = Lists.newArrayList();
		StringBuffer sb = new StringBuffer();
		String idArray[] =ids.split(",");
		String isall = request.getParameter("isall");
		String contents = request.getParameter("contents");
		
		//获取需要发送短信的手机号列表
		if("1".equals(isall)){
			member.setIsPhone("1");
			list = memberService.findList(member);
		}else{
			for(String id : idArray){
				Member each = memberService.get(id);
				if("1".equals(each.getIsPhone())){
					list.add(each);
				}
			}
		}
		
		//发送短信
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Member m = list.get(i);
				if(0==i){
					sb.append(m.getMobile());
				}else{
					sb.append(","+m.getMobile());
				}
			}
			try {
				String rtn = SmsUtils.doPost(sb, new StringBuffer(contents));
				System.out.println("短信发送返回结果："+rtn);
				if(rtn.contains("提交成功")){
					msg = "短信发送成功";
				}else{
					msg = rtn;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "短信发送异常，"+e.getMessage();
			}
		}else{
			msg = "选中的人员都不需要发送短信提醒.";
		}
		
		addMessage(redirectAttributes, msg);
		
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 是否批量修改短信提醒处理
	 * @param ids
	 * @param member
	 * @return
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value="duanxinSave")
	public String duanxinSave(String ids, HttpServletRequest request, Member member){
		String idArray[] =ids.split(",");
		String isphone = member.getIsPhone();
		
		String phone = request.getParameter("phone");
		
		if(phone.equals("1")){
			memberService.updatePhoneAll(member);
		}else if(phone.equals("0") || phone.equals(null)){
			for(String id : idArray){
				member = memberService.get(id);
				member.setIsPhone(isphone);
				memberService.phoneSave(member);
			}
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	/**
	 * 是否批量修改微信提醒处理
	 * @param ids
	 * @param member
	 * @return
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value="wechatSave")
	public String wechatSave(String ids, HttpServletRequest request, Member member){
		String idArray[] =ids.split(",");
		String iswechat = member.getIsWechat();
		
		String wechat = request.getParameter("wechat");
		
		if(wechat.equals("1")){
			memberService.updateWechatAll(member);
		}else if(wechat.equals("0") || wechat.equals(null)){
			for(String id : idArray){
				member = memberService.get(id);
				member.setIsWechat(iswechat);
				memberService.wechatSave(member);
			}
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 查看所属会员下的所有套餐卡
	 * @param member
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("member:member:list")
	@RequestMapping(value = "memberPackageCard")
	public String memberPackageCard(Member member, HttpServletRequest request, HttpServletResponse response, Model model){
		//Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		/*DmCard dm = new DmCard();
		dm.setMemberid(dmCard.getId());*/
		DmCard dm = new DmCard();
		dm.setMember(member);
		
		Page<DmCard> page = dmCardService.findPage(new Page<DmCard>(request, response), dm);
		model.addAttribute("page", page);
		return "modules/member/memberPackageCard";
	}
	
	/**
	 * 查看下级会员
	 * @param member
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value = "parentsForm")
	public String parentsForm(Member member, HttpServletRequest request, HttpServletResponse response, Model model){
		Member m = new Member();
		String tjropenid = member.getWxopenid();
		if(StringUtils.isNotEmpty(tjropenid)){
			m.setTjrwxopenid(tjropenid);
		}else{
			m.setTjrwxopenid("0");
		}
		
		
		// 模糊查询，前台只把当前推荐人跟查询条件传进来
		if(member.getId()==null){
			m.setMobile(member.getMobile());
			m.setName(member.getName());
			m.setNickname(member.getNickname());
			
			// 把查询条件去掉，只留下wxopenid，根据wxopenid找到会员信息，然后重新赋值会员名、手机号、昵称
			member.setName("");
			member.setMobile("");
			member.setNickname("");
			
			// 查询符合条件的会员
			List<Member> memberParent = memberService.findList(member);
			
			// 显示当前会员信息
			member.setName(memberParent.get(0).getName());
			member.setMobile(memberParent.get(0).getMobile());
		}
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), m);
		
		model.addAttribute("member", member);	// 显示当前会员名、手机号
		model.addAttribute("mm", m);	// 查询条件
		model.addAttribute("tjrwxopenid", m.getTjrwxopenid());
		model.addAttribute("page", page);
		return "modules/member/memberParentsList";
	}
	
	/**
	 * 从会员-->查看套餐卡-->点击套餐卡页面
	 * @param goodsOrder
	 * @param model
	 * @return
	 */
	@RequiresPermissions("member:member:list")
	@RequestMapping(value = "cardXuancaiList")
	public String cardXuancaiList(GoodsOrder goodsOrder, Model model){
		List<GoodsOrder> list = goodsOrderService.findList(goodsOrder);
		model.addAttribute("goodsOrder", list);
		return "modules/member/memberCardXuancaiOrderList";
	}
	
	/**+
	 * 分配套餐卡条件查询
	 * @param dmCard
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "queryList")
	public String queryList(DmCard dmCard, Model model){
		List<DmCard> lists = dmCardService.findList(dmCard);
		model.addAttribute("dmCard", lists);
		return "modules/member/dmFenpeiCardList";
	}
	
	/**
	 * 分配套餐卡页面
	 * @return
	 */
	@RequiresPermissions("member:member:list")
	@RequestMapping(value = "fenpeiCardList")
	public String fenpeiCardList(DmCard dmCard, HttpServletRequest request, Model model){
		String memberid = request.getParameter("memberid");
		dmCard.setId("");
		String idd = request.getParameter("memberid");	// 获得当前会员的id
		
		dmCard.setStatus("0");	
		List<DmCard> lists = dmCardService.findList(dmCard);
		model.addAttribute("dmCardlist", lists);
		
		List<DmPackage> dm = dmPackageService.findList(new DmPackage());
		model.addAttribute("dmPackageName", dm);
		
		model.addAttribute("dmCard", dmCard);
		model.addAttribute("memberid", idd); // 存放会员id，分配的时候能将套餐卡id跟会员id绑定
		return "modules/member/dmFenpeiCardList";
	}
	/**
	 * 批量为会员分配套餐卡
	 * @param member
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value = "fenpeiSave")
	public String fenpeiSave(Member member, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes, Model model){
 		String str = request.getParameter("ids");	 // 从前台输入流得到餐套餐卡id
 		
		String[] i = str.split(",");	// 转换成数组，用逗号隔开
		for(int j = 0; j<i.length; j++){
			DmCard dm = new DmCard();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//设置日期格式--使用当前系统日期作为套餐卡号
			String cardid = i[j];
			dm.setId(cardid);	// 卡号赋值
			List<DmCard> dmCard = dmCardService.findList(dm);		// 查出当前dmCard的数据
			dm = dmCard.get(0);	
			dm.setMember(member);	// setMember可以赋值对象，取值从member里面取
			dm.setStatus("1");		// 状态变成激活
			dm.setActivetime(df.format(new Date()));	// 添加激活时间
			dmCardService.updatePlfp(dm);		// 保存更新
		}
		
		// 返回后页面只显示操作的会员
		Page<Member> page = memberService.findPage(new Page<Member>(request, response), member);
		addMessage(redirectAttributes, "保存成功");
		
		model.addAttribute("page", page);
		return "modules/member/memberList";
	}
	
	

	/**
	 * 保存会员信息
	 */
	@RequiresPermissions(value={"member:member:add","member:member:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Member member, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, member)){
			return form(member, model);
		}
		
		/*
		 * 如果新添加一条信息并提交，controller中的 Stringutils.isNotEmpty()先判断 member.getNewPassword() 里面有没有值，如果有值，附给password
		 * 如果编辑一条信息，密码不编辑，提交，这时controller中的member.getNewPassword()中是没值的，不附给password
		 * 如果编辑密码并提交，跟新添加信息时是一样的，member.getNewPassword() 中取到页面上新传过来的值，附给password
		 */
		if(StringUtils.isNotEmpty(member.getNewpassword())){
			member.setPassword(SystemService.entryptPassword(member.getNewpassword()));
		}
		if(!member.getIsNewRecord()){//编辑表单保存
			// 手机号唯一性效验
			List<Member> l = memberService.findOnlyMobil(member);
			if(l.size()>0){
				addMessage(redirectAttributes, "手机号已存在，请更换手机号重新添加！");
			}else{
				Member t = memberService.get(member.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(member, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				memberService.save(t);//保存
				addMessage(redirectAttributes, "保存会员信息成功！ ");
			}
		}else{//新增表单保存
			
			// 手机号唯一性效验
			List<Member> l = memberService.findOnlyMobil(member);
			if(l.size()>0){
				addMessage(redirectAttributes, "手机号已存在，请更换手机号重新添加！");
			}else{
//				member.setIsPhone("1");
				member.setIsWechat("1");
				member.setPassword(SystemService.entryptPassword("111111"));
				memberService.save(member);//保存
				addMessage(redirectAttributes, "保存会员信息成功，默认密码为 111111 ！ ");
			}
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 密码重置
	 */
	@RequiresPermissions("member:member:edit")
	@RequestMapping(value = "resetPassword")
	public String resetPassword(Member member, RedirectAttributes redirectAttributes) throws Exception{
		member.setPassword(SystemService.entryptPassword("111111"));
		memberService.resetPassword(member);//保存
		addMessage(redirectAttributes, "密码重置成功,新密码为：111111");
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	
	/**
	 * 删除会员信息
	 */
	@RequiresPermissions("member:member:del")
	@RequestMapping(value = "delete")
	public String delete(Member member, RedirectAttributes redirectAttributes) {
		memberService.delete(member);
		
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
		
		String str = "操作人："+user.getName()+" 删除商品 ："+member.getName()+" 手机号："+member.getMobile();
		ywLog.setLog(str);
		ywLog.setModulename("会员管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除会员信息成功");
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 批量删除会员信息
	 */
	@RequiresPermissions("member:member:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		StringBuffer strbuf = new StringBuffer();
		YwLog ywLog = new YwLog();
		User user = UserUtils.getUser();
	
		for(String id : idArray){
			Member member = memberService.get(id);
			
			memberService.delete(member);
			
			// 添加日志
			String name = "姓名："+member.getName();
			String mobile = ",手机号:"+member.getMobile();
			strbuf.append("["+name+mobile+"]");
		}
		String str = "操作人："+user.getName()+"  批量删除会员："+strbuf;
		ywLog.setLog(str);
		ywLog.setModulename("会员管理");
		ywLogService.save(ywLog);
		
		addMessage(redirectAttributes, "删除会员信息成功");
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("member:member:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Member member, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "会员信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Member> page = memberService.findPage(new Page<Member>(request, response, -1), member);
    		new ExportExcel("会员信息", Member.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出会员信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("member:member:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Member> list = ei.getDataList(Member.class);
			for (Member member : list){
				try{
					memberService.save(member);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条会员信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条会员信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入会员信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
    }
	
	/**
	 * 下载导入会员信息数据模板
	 */
	@RequiresPermissions("member:member:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "会员信息数据导入模板.xlsx";
    		List<Member> list = Lists.newArrayList(); 
    		new ExportExcel("会员信息数据", Member.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/member/member/?repage";
    }
	
	
	

}