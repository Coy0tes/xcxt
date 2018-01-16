/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.yongjintixian.web;

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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.goodsorder.entity.GoodsOrder;
import com.jeeplus.modules.member.dao.MemberDao;
import com.jeeplus.modules.member.entity.Member;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.yongjintixian.entity.Yjtx;
import com.jeeplus.modules.yongjintixian.service.YjtxService;

/**
 * 佣金提现管理Controller
 * @author mxc
 * @version 2017-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/yongjintixian/yjtx")
public class YjtxController extends BaseController {

	@Autowired
	private YjtxService yjtxService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private MemberDao memberDao;
	
	@ModelAttribute
	public Yjtx get(@RequestParam(required=false) String id) {
		Yjtx entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = yjtxService.get(id);
		}
		if (entity == null){
			entity = new Yjtx();
		}
		return entity;
	}
	
	/**
	 * 佣金提现管理列表页面
	 */
	@RequiresPermissions("yongjintixian:yjtx:list")
	@RequestMapping(value = {"list", ""})
	public String list(Yjtx yjtx, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Yjtx> page = yjtxService.findPage(new Page<Yjtx>(request, response), yjtx); 
		model.addAttribute("page", page);
		return "modules/yongjintixian/yjtxList";
	}

	/**
	 * 查看，增加，编辑佣金提现管理表单页面
	 */
	@RequiresPermissions(value={"yongjintixian:yjtx:view","yongjintixian:yjtx:add","yongjintixian:yjtx:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Yjtx yjtx, Model model) {
		model.addAttribute("yjtx", yjtx);
		return "modules/yongjintixian/yjtxForm";
	}

	/**
	 * 保存佣金提现管理
	 */
	@RequiresPermissions(value={"yongjintixian:yjtx:add","yongjintixian:yjtx:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Yjtx yjtx, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, yjtx)){
			return form(yjtx, model);
		}
		if(!yjtx.getIsNewRecord()){//编辑表单保存
			Yjtx t = yjtxService.get(yjtx.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(yjtx, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			yjtxService.save(t);//保存
		}else{//新增表单保存
			yjtxService.save(yjtx);//保存
		}
		addMessage(redirectAttributes, "保存佣金提现管理成功");
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
	}
	
	@RequiresPermissions(value={"yongjintixian:yjtx:add","yongjintixian:yjtx:edit"},logical=Logical.OR)
	@RequestMapping(value = "dakuanSave")
	public String dakuanSave(Yjtx tixian, Model model, RedirectAttributes redirectAttributes) throws Exception{
		
		
		tixian.setClremark("已处理");
		tixian.setStatus("1");
		tixian.setClzt("1");
		
		yjtxService.save(tixian);
		
		
//		//启动一个线程推送微信消息和发送短信
//		final Yjtx entity = tixian;
//		new Thread() {
//			public void run() {
//				try {
//					//推送微信消息
//					systemService.send_yjtx(entity);
//					
//					//发送短信
//					Member m = new Member();
//					m.setWxopenid(entity.getWxopenid());
//					m = memberDao.getByWxopenid(m);
//					if("1".equals(m.getIsPhone())){
//						StringBuffer contextString = new StringBuffer();
//						contextString.append("【有机汇】您好，您本次提现"+entity.getJine()+"元已经汇款到指定账号，请注意查收。如有疑问，请咨询400-007-0011。");
//						String rtn = SmsUtils.doPost(new StringBuffer(m.getMobile()), contextString);
//						System.out.println(rtn);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		}.start();
		
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
	}
	
	/**
	 * 删除佣金提现管理
	 */
	@RequiresPermissions("yongjintixian:yjtx:del")
	@RequestMapping(value = "delete")
	public String delete(Yjtx yjtx, RedirectAttributes redirectAttributes) {
		yjtxService.delete(yjtx);
		addMessage(redirectAttributes, "删除佣金提现管理成功");
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
	}
	
	/**
	 * 批量删除佣金提现管理
	 */
	@RequiresPermissions("yongjintixian:yjtx:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			yjtxService.delete(yjtxService.get(id));
		}
		addMessage(redirectAttributes, "删除佣金提现管理成功");
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("yongjintixian:yjtx:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Yjtx yjtx, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "佣金提现管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Yjtx> page = yjtxService.findPage(new Page<Yjtx>(request, response, -1), yjtx);
    		new ExportExcel("佣金提现管理", Yjtx.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出佣金提现管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("yongjintixian:yjtx:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Yjtx> list = ei.getDataList(Yjtx.class);
			for (Yjtx yjtx : list){
				try{
					yjtxService.save(yjtx);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条佣金提现管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条佣金提现管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入佣金提现管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
    }
	
	/**
	 * 下载导入佣金提现管理数据模板
	 */
	@RequiresPermissions("yongjintixian:yjtx:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "佣金提现管理数据导入模板.xlsx";
    		List<Yjtx> list = Lists.newArrayList(); 
    		new ExportExcel("佣金提现管理数据", Yjtx.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/yongjintixian/yjtx/?repage";
    }
	
	
	

}