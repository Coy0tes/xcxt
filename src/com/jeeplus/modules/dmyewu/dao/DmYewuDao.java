/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.dmyewu.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.dmyewu.entity.DmYewu;

/**
 * dmyewuDAO接口
 * @author mxc
 * @version 2017-08-03
 */
@MyBatisDao
public interface DmYewuDao extends CrudDao<DmYewu> {

	List<DmYewu> findListBydmYewu(DmYewu dmYewu);

	
}