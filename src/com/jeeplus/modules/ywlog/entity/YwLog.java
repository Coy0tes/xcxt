/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.ywlog.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 操作日志Entity
 * @author mxc
 * @version 2017-07-04
 */
public class YwLog extends DataEntity<YwLog> {
	
	private static final long serialVersionUID = 1L;
	private String modulename;		// 模块名称
	private String log;		// 操作内容
	
	public YwLog() {
		super();
	}

	public YwLog(String id){
		super(id);
	}

	@ExcelField(title="模块名称", align=2, sort=1)
	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	@ExcelField(title="操作内容", align=2, sort=2)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
	
}