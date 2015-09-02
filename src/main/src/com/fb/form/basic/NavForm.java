package com.fb.form.basic;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public class NavForm extends ActionForm {

	private static final long serialVersionUID = 4475144758992576653L;

	private List<MenuVO> menus;	
	private List<MenuFuncVO> menuFuncs;	
	
	public List<MenuFuncVO> getMenuFuncs() {
		return menuFuncs;
	}
	public void setMenuFuncs(List<MenuFuncVO> menuFuncs) {
		this.menuFuncs = menuFuncs;
	}
	public List<MenuVO> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}

}
