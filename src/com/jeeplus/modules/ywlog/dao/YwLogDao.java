/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.ywlog.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.ywlog.entity.YwLog;

/**
 * 操作日志DAO接口
 * @author mxc
 * @version 2017-07-04
 */
@MyBatisDao
public interface YwLogDao extends CrudDao<YwLog> {

	
}