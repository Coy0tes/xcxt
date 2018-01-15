/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixinmenu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sysweixinmenu.entity.Sysweixinmenu;
import com.jeeplus.modules.sysweixinmenu.service.SysweixinmenuService;

/**
 * 微信菜单管理Controller
 * @author zhaoliangdong
 * @version 2017-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/sysweixinmenu/sysweixinmenu")
public class SysweixinmenuController extends BaseController {

	@Autowired
	private SysweixinmenuService sysweixinmenuService;
	
	@ModelAttribute
	public Sysweixinmenu get(@RequestParam(required=false) String id) {
		Sysweixinmenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysweixinmenuService.get(id);
		}
		if (entity == null){
			entity = new Sysweixinmenu();
		}
		return entity;
	}
	
	/**
	 * 菜单列表页面
	 */
	@RequiresPermissions("sysweixinmenu:sysweixinmenu:list")
	@RequestMapping(value = {"list", ""})
	public String list(Sysweixinmenu sysweixinmenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Sysweixinmenu> list = sysweixinmenuService.findList(sysweixinmenu); 
		model.addAttribute("list", list);
		return "modules/sysweixinmenu/sysweixinmenuList";
	}

	/**
	 * 查看，增加，编辑菜单表单页面
	 */
	@RequiresPermissions(value={"sysweixinmenu:sysweixinmenu:view","sysweixinmenu:sysweixinmenu:add","sysweixinmenu:sysweixinmenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Sysweixinmenu sysweixinmenu, Model model) {
		if (sysweixinmenu.getParent()!=null && StringUtils.isNotBlank(sysweixinmenu.getParent().getId())){
			sysweixinmenu.setParent(sysweixinmenuService.get(sysweixinmenu.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(sysweixinmenu.getId())){
				Sysweixinmenu sysweixinmenuChild = new Sysweixinmenu();
				sysweixinmenuChild.setParent(new Sysweixinmenu(sysweixinmenu.getParent().getId()));
				List<Sysweixinmenu> list = sysweixinmenuService.findList(sysweixinmenu); 
				if (list.size() > 0){
					sysweixinmenu.setSort(list.get(list.size()-1).getSort());
					if (sysweixinmenu.getSort() != null){
						sysweixinmenu.setSort(sysweixinmenu.getSort() + 30);
					}
				}
			}
		}
		if (sysweixinmenu.getSort() == null){
			sysweixinmenu.setSort(30);
		}
		model.addAttribute("sysweixinmenu", sysweixinmenu);
		return "modules/sysweixinmenu/sysweixinmenuForm";
	}

	/**
	 * 保存菜单
	 */
	@RequiresPermissions(value={"sysweixinmenu:sysweixinmenu:add","sysweixinmenu:sysweixinmenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Sysweixinmenu sysweixinmenu, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysweixinmenu)){
			return form(sysweixinmenu, model);
		}
		if(!sysweixinmenu.getIsNewRecord()){//编辑表单保存
			Sysweixinmenu t = sysweixinmenuService.get(sysweixinmenu.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysweixinmenu, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysweixinmenuService.save(t);//保存
		}else{//新增表单保存
			sysweixinmenuService.save(sysweixinmenu);//保存
		}
		addMessage(redirectAttributes, "保存菜单成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixinmenu/sysweixinmenu/?repage";
	}
	
	/**
	 * 删除菜单
	 */
	@RequiresPermissions("sysweixinmenu:sysweixinmenu:del")
	@RequestMapping(value = "delete")
	public String delete(Sysweixinmenu sysweixinmenu, RedirectAttributes redirectAttributes) {
		sysweixinmenuService.delete(sysweixinmenu);
		addMessage(redirectAttributes, "删除菜单成功");
		return "redirect:"+Global.getAdminPath()+"/sysweixinmenu/sysweixinmenu/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Sysweixinmenu> list = sysweixinmenuService.findList(new Sysweixinmenu());
		for (int i=0; i<list.size(); i++){
			Sysweixinmenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 获取媒体素材接口
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getMediaList")
	public String getMediaList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String mediacount = sysweixinmenuService.getMediaCount();
		JSONObject jo = JSON.parseObject(mediacount);
		Integer news_count = jo.getInteger("news_count");
		List<Map<String,String>> listmap = Lists.newArrayList();
		if(news_count>0){
			int offset = 0;
			int count = 20;
			String medialist = sysweixinmenuService.getMediaList("news",offset,count);
			
			JSONObject obj = JSON.parseObject(medialist);
			
			if(obj!=null){
				JSONArray arr = obj.getJSONArray("item");
				for(int i=0;i<arr.size();i++){
					JSONObject eachobj = arr.getJSONObject(i);
					String media_id = eachobj.getString("media_id");
					String update_time = eachobj.getString("update_time");
					
					Long timestamp = Long.parseLong(update_time)*1000;    
					String update = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp)); 
					  
					JSONObject contentobj = eachobj.getJSONObject("content");
					JSONArray items = contentobj.getJSONArray("news_item");
					JSONObject item0 = items.getJSONObject(0);	
					String title = item0.getString("title");
					String thumb_url = item0.getString("thumb_url");
					Map<String,String> map = new HashMap<String, String>();
					map.put("media_id", media_id);
					map.put("title", title);
					map.put("thumb_url", thumb_url);
					map.put("update_time", update);
					listmap.add(map);
				}
			}
			
		}
		
		model.addAttribute("listmap", listmap);
		return "modules/sysweixinmenu/menuselect";
	}
	
}