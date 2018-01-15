/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.xiugaitongzhi.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.xiugaitongzhi.entity.XiugaiTongzhi;

/**
 * xiugaitongzhiDAO接口
 * @author mxc
 * @version 2017-09-11
 */
@MyBatisDao
public interface XiugaiTongzhiDao extends CrudDao<XiugaiTongzhi> {

	void saveStatus(XiugaiTongzhi xiugaiTongzhi);

	
}