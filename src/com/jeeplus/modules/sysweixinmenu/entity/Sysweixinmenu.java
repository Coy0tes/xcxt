/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysweixinmenu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.TreeEntity;

/**
 * 微信菜单管理Entity
 * @author zhaoliangdong
 * @version 2017-07-17
 */
public class Sysweixinmenu extends TreeEntity<Sysweixinmenu> {
	
	private static final long serialVersionUID = 1L;
	private Sysweixinmenu parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private String name;		// 菜单标题
	private Integer sort;		// 排序
	private String type;		// 动作类型
	private String key;		// 菜单KEY值
	private String url;		// 网页链接
	private String mediaid;		// 媒体ID
	
	public Sysweixinmenu() {
		super();
	}

	public Sysweixinmenu(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public Sysweixinmenu getParent() {
		return parent;
	}

	public void setParent(Sysweixinmenu parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMediaid() {
		return mediaid;
	}

	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}

	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}