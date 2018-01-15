/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixinmenu.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.sysaccesstoken.dao.SysaccesstokenDao;
import com.jeeplus.modules.sysaccesstoken.entity.Sysaccesstoken;
import com.jeeplus.modules.sysweixin.dao.SysweixinDao;
import com.jeeplus.modules.sysweixin.entity.Sysweixin;
import com.jeeplus.modules.sysweixinmenu.dao.SysweixinmenuDao;
import com.jeeplus.modules.sysweixinmenu.entity.Sysweixinmenu;
import com.wechat.WeixinMessageUtil;

/**
 * 微信菜单管理Service
 * @author zhaoliangdong
 * @version 2017-07-17
 */
@Service
@Transactional(readOnly = true)
public class SysweixinmenuService extends TreeService<SysweixinmenuDao, Sysweixinmenu> {
	
	@Autowired
	private SysaccesstokenDao sysaccesstokenDao;
	
	@Autowired
	private SysweixinDao sysweixinDao;
	

	public Sysweixinmenu get(String id) {
		return super.get(id);
	}
	
	public List<Sysweixinmenu> findList(Sysweixinmenu sysweixinmenu) {
		if (StringUtils.isNotBlank(sysweixinmenu.getParentIds())){
			sysweixinmenu.setParentIds(","+sysweixinmenu.getParentIds()+",");
		}
		return super.findList(sysweixinmenu);
	}
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = false)
	public void save(Sysweixinmenu sysweixinmenu) {
		super.save(sysweixinmenu);
		syncWxMenu();
	}
	
	@Transactional(readOnly = false)
	public void syncWxMenu(){
		//1.获取数据库中所有的微信菜单
		JSONObject jsonObjOuter = new JSONObject();
		JSONArray jsonarr = new JSONArray();
		
		//先获取所有的一级菜单
		Sysweixinmenu wxmenu = new Sysweixinmenu();
		Sysweixinmenu wxpmenu = new Sysweixinmenu();
		wxpmenu.setId("0");
		wxmenu.setParent(wxpmenu);
		List<Sysweixinmenu> topmenulist = dao.findList(wxmenu);
		
		for(Sysweixinmenu menu : topmenulist){
			//一级菜单信息
			JSONObject jsonObjv1 = new JSONObject();
	    	jsonObjv1.put("name", menu.getName());
	    	jsonObjv1.put("type", menu.getType());
	    	if("view".equals(menu.getType())){
	    		jsonObjv1.put("url", menu.getUrl());
	    	}
	    	if("media_id".equals(menu.getType())){
	    		jsonObjv1.put("media_id", menu.getMediaid());
	    	}
	    	
	    	//二级菜单信息
	    	Sysweixinmenu pmenu = new Sysweixinmenu();
	    	pmenu.setId(menu.getId());
	    	Sysweixinmenu bmenu = new Sysweixinmenu();
	    	bmenu.setParent(pmenu);
	    	List<Sysweixinmenu> bmenulist = dao.findList(bmenu);
	    	JSONArray jsonarr20 = new JSONArray();
	    	for(Sysweixinmenu submenu : bmenulist){
	    		JSONObject jsonObjv2 = new JSONObject();
	    		jsonObjv2.put("name", submenu.getName());
	    		jsonObjv2.put("type", submenu.getType());
		    	if("view".equals(submenu.getType())){
		    		jsonObjv2.put("url", submenu.getUrl());
		    	}
		    	if("media_id".equals(submenu.getType())){
		    		jsonObjv2.put("media_id", submenu.getMediaid());
		    	}
		    	jsonarr20.add(jsonObjv2);
	    	}
	    	
	    	if(bmenulist.size()>0){
	    		jsonObjv1.put("sub_button", jsonarr20);
	    	}
	    	
	    	jsonarr.add(jsonObjv1);
		}
		
		if(topmenulist.size()>0){
			jsonObjOuter.put("button", jsonarr);
			String menubody = jsonObjOuter.toString();
			
			List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
			List<Sysweixin> wxlist = sysweixinDao.findAllList();
			Sysweixin wx = wxlist.get(0);
			
			String accesstoken = null;
			String tokentime = "0";
			String expiresin = "0";
			
			if(tokenlist.size()>0){
				Sysaccesstoken token = tokenlist.get(0);
				accesstoken = token.getAccesstoken();
				tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
				expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
			}
			
			WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
					accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin));
			try {
				String rtns = tms.createMenuWithBody(menubody);
				System.out.println("wx:更新菜单接口返回："+rtns);
				
				//是否更新access_token
			    JSONObject jobj = JSONObject.parseObject(rtns);
			    
			    if(jobj!=null){
				    if(jobj.get("isRequest").toString().equals("1")){
					  //将access_token信息写入数据库
					  String accesstokenOut = jobj.get("accesstoken").toString();
					  long expireinOut = Long.parseLong(jobj.get("expirein").toString());
					  long tokentimeOut = Long.parseLong(jobj.get("tokentime").toString());
					  
					  if(sysaccesstokenDao.findAllList().size()==0){
						  Sysaccesstoken token = new Sysaccesstoken();
						  token.preInsert();
						  token.setAccesstoken(accesstokenOut);
						  token.setExpiresin(expireinOut);
						  token.setTokentime(String.valueOf(tokentimeOut));
						  sysaccesstokenDao.insert(token);
					  }else{
						  Sysaccesstoken token = new Sysaccesstoken();
						  token.preUpdate();
						  token.setAccesstoken(accesstokenOut);
						  token.setExpiresin(expireinOut);
						  token.setTokentime(String.valueOf(tokentimeOut));
						  sysaccesstokenDao.update(token);
					  }
				   }
			   }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Sysweixinmenu sysweixinmenu) {
		super.delete(sysweixinmenu);
		syncWxMenu();
	}

	/**
	 * 获取媒体数量
	 */
	@Transactional(readOnly = false)
	public String getMediaCount() {
		String rtns = null;
		
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin));
		
		Map<String,Object> rtnmap = tms.getMediaCount();
		rtns = (String)rtnmap.get("rtn");
		
		//是否更新access_token
	    if(rtnmap!=null){
	    	updateAccessToken(rtnmap);
	    }
	    
		return rtns;
	}

	/**
	 * 获取媒体列表
	 * @param type
	 * @param offset
	 * @param count
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public String getMediaList(String type, int offset, int count) throws Exception {
		String rtns = null;
		
		List<Sysaccesstoken> tokenlist = sysaccesstokenDao.findAllList();
		List<Sysweixin> wxlist = sysweixinDao.findAllList();
		Sysweixin wx = wxlist.get(0);
		
		String accesstoken = null;
		String tokentime = "0";
		String expiresin = "0";
		
		if(tokenlist.size()>0){
			Sysaccesstoken token = tokenlist.get(0);
			accesstoken = token.getAccesstoken();
			tokentime = token.getTokentime()==null ? "0" : token.getTokentime();
			expiresin = token.getExpiresin()==null ? "0" : String.valueOf(token.getExpiresin());
		}
		
		WeixinMessageUtil tms = new WeixinMessageUtil(wx.getWeixinappid(),wx.getWeixinappsecret(),
				accesstoken, Long.parseLong(tokentime), Long.parseLong(expiresin));
		
		Map<String,Object> rtnmap = tms.getMediaList(type,offset,count);
		rtns = (String)rtnmap.get("rtn");
		
		//是否更新access_token
	    if(rtnmap!=null){
	    	updateAccessToken(rtnmap);
	    }
	    
	   return rtns;
	}
	
	/**
	 * 更新数据库中存储的accessToken
	 */
	private void updateAccessToken(Map<String,Object> rtnmap){
		if(rtnmap.get("isRequest").toString().equals("1")){
			  //将access_token信息写入数据库
			  String accesstokenOut = rtnmap.get("accesstoken").toString();
			  long expireinOut = Long.parseLong(rtnmap.get("expirein").toString());
			  long tokentimeOut = Long.parseLong(rtnmap.get("tokentime").toString());
			  
			  if(sysaccesstokenDao.findAllList().size()==0){
				  Sysaccesstoken token = new Sysaccesstoken();
				  token.preInsert();
				  token.setAccesstoken(accesstokenOut);
				  token.setExpiresin(expireinOut);
				  token.setTokentime(String.valueOf(tokentimeOut));
				  sysaccesstokenDao.insert(token);
			  }else{
				  Sysaccesstoken token = new Sysaccesstoken();
				  token.preUpdate();
				  token.setAccesstoken(accesstokenOut);
				  token.setExpiresin(expireinOut);
				  token.setTokentime(String.valueOf(tokentimeOut));
				  sysaccesstokenDao.update(token);
			  }
		   }
	}
	
}