package com.fb.vo;

import java.io.Serializable;
import java.util.List;

public class MenuVO implements Serializable{
	private static final long serialVersionUID = -1890362551254939268L;

	private Integer menuId;
	private String menuLabel;
	private String folderFlag;
	private Integer order;
	private MenuVO parent;
	private List<MenuFuncVO> funcs;
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public String getMenuLabel() {
		return menuLabel;
	}
	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}
	public String getFolderFlag() {
		return folderFlag;
	}
	public void setFolderFlag(String folderFlag) {
		this.folderFlag = folderFlag;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public MenuVO getParent() {
		return parent;
	}
	public void setParent(MenuVO parent) {
		this.parent = parent;
	}
	public List<MenuFuncVO> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<MenuFuncVO> funcs) {
		this.funcs = funcs;
	}

}
