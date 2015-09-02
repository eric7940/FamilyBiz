package com.fb.form.basic;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.LabelValueBean;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public class FuncForm extends ActionForm {

	private static final long serialVersionUID = 4475144758992576653L;

	private List<MenuFuncVO> funcs;
	private List<MenuVO> menus;
	private List<LabelValueBean> linkTypes;
	private List<LabelValueBean> folderFlags;
	private Integer funcId;
	private String funcLabel;
	private Integer menuId;
	private String url;
	private String linkType;
	private String statusFlag;
	private Integer order;
	private String menuLabel;
	private String folderFlag;
	
	public List<MenuFuncVO> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<MenuFuncVO> funcs) {
		this.funcs = funcs;
	}
	public List<MenuVO> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}
	public List<LabelValueBean> getLinkTypes() {
		return linkTypes;
	}
	public void setLinkTypes(List<LabelValueBean> linkTypes) {
		this.linkTypes = linkTypes;
	}
	public List<LabelValueBean> getFolderFlags() {
		return folderFlags;
	}
	public void setFolderFlags(List<LabelValueBean> folderFlags) {
		this.folderFlags = folderFlags;
	}
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
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
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
}
