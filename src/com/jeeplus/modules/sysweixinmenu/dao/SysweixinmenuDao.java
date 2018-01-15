/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixinmenu.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sysweixinmenu.entity.Sysweixinmenu;

/**
 * 微信菜单管理DAO接口
 * @author zhaoliangdong
 * @version 2017-07-17
 */
@MyBatisDao
public interface SysweixinmenuDao extends TreeDao<Sysweixinmenu> {
	
}