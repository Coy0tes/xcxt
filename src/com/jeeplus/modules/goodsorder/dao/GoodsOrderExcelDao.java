package com.jeeplus.modules.goodsorder.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.goodsorder.entity.GoodsOrderExcel;
@MyBatisDao
public interface GoodsOrderExcelDao extends CrudDao<GoodsOrderExcel> {
	List<GoodsOrderExcel> findListToday(GoodsOrderExcel goodsOrderExcel);
	
}
