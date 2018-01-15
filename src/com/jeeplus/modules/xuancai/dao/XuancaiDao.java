/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xuancai.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.xuancai.entity.Xuancai;

/**
 * 选菜管理DAO接口
 * @author mxc
 * @version 2017-06-17
 */
@MyBatisDao
public interface XuancaiDao extends CrudDao<Xuancai> {

	void updateTuijianStatus(Xuancai xuancai);

	List<Xuancai> findPackageList(Xuancai xuancai);

}