/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmpackage.dao;

import java.util.List;
import java.util.Map;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmpackage.entity.DmPackageGoodsZengsong;

/**
 * 套餐管理DAO接口
 * @author mxc
 * @version 2017-09-12
 */
@MyBatisDao
public interface DmPackageGoodsZengsongDao extends CrudDao<DmPackageGoodsZengsong> {

	List<DmPackageGoodsZengsong> findMainId(DmPackageGoodsZengsong dmZengSong);
	
}