package com.jeeplus.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import wfaisino.bean.Route;
import wfaisino.bean.RouteResponse;
import wfaisino.utils.SFHelper;

@Controller
@RequestMapping(value = "${adminPath}/sf")
public class SFController {

	@RequestMapping(value = "showList")
	public String showList(HttpServletRequest request,HttpServletResponse response,Model model){
		List<Route> newlist = Lists.newArrayList();
		String wldh = request.getParameter("wldh");
		RouteResponse rr = SFHelper.getRoute(wldh);
		if(rr!=null){
			List<Route> routeList = rr.getRouteList();
		    if(routeList!=null){
		    	for(int i=routeList.size()-1;i>=0;i--){
			    	newlist.add(routeList.get(i));
			    }
		    }
		}
	    
	    model.addAttribute("list", newlist);
		return "modules/sf/sfList";
	}
	
	@RequestMapping(value = "getList")
	@ResponseBody
	public String getList(HttpServletRequest request,HttpServletResponse response,Model model){
		List<Route> newlist = Lists.newArrayList();
		String wldh = request.getParameter("wldh");
		RouteResponse rr = SFHelper.getRoute(wldh);
		if(rr!=null){
			List<Route> routeList = rr.getRouteList();
			if(routeList!=null){
				for(int i=routeList.size()-1;i>=0;i--){
			    	newlist.add(routeList.get(i));
			    }
			}
		}
		return JSON.toJSONString(newlist);
	}
}
