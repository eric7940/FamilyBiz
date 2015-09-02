package com.fb.vo;

import java.io.Serializable;

public class MenuFuncVO implements Serializable{
	private static final long serialVersionUID = -1890362551254939268L;

	private Integer funcId;
	private String funcLabel;
	private Integer menuId;
	private String url;
	private String linkType;
	private Integer order;
	private MenuVO menu;
	
	public Integer getFuncId() {
		return funcId;
	}
	public void setFuncId(Integer funcId) {
		this.funcId = funcId;
	}
	public String getFuncLabel() {
		return funcLabel;
	}
	public void setFuncLabel(String funcLabel) {
		this.funcLabel = funcLabel;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public MenuVO getMenu() {
		return menu;
	}
	public void setMenu(MenuVO menu) {
		this.menu = menu;
	}
		
}
