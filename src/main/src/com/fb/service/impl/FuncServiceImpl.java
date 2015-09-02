package com.fb.service.impl;

import java.util.List;

import com.fb.service.FuncService;
import com.fb.util.FamilyBizException;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public class FuncServiceImpl extends ServiceImpl implements FuncService {

	public MenuFuncVO getFunc(int funcId) throws FamilyBizException {
		MenuFuncVO func = new MenuFuncVO();
		func.setFuncId(Integer.valueOf(funcId));
		return (MenuFuncVO) this.getFbDao().queryForObject("selectMenuFunc", func);
	}
	
	public MenuVO getMenu(int menuId) throws FamilyBizException {
		MenuVO menu = new MenuVO();
		menu.setMenuId(Integer.valueOf(menuId));
		return (MenuVO) this.getFbDao().queryForObject("selectMenu", menu);
	}

	@SuppressWarnings("unchecked")
	public List<MenuFuncVO> getFuncs() throws FamilyBizException {
		MenuFuncVO func = new MenuFuncVO();
		return this.getFbDao().queryForList("selectMenuFunc", func);
	}

	public List<MenuVO> getMenus() throws FamilyBizException {
		MenuVO menu = new MenuVO();
		return this.getFbDao().queryForList("selectMenu", menu);
	}

	public void addFunc(MenuFuncVO func) throws FamilyBizException {
		this.getFbDao().insert("insertMenuFunc", func);
	}

	public void addMenu(MenuVO menu) throws FamilyBizException {
		this.getFbDao().insert("insertMenu", menu);
	}

	public void modifyFunc(MenuFuncVO func) throws FamilyBizException {
		this.getFbDao().update("updateMenuFunc", func);
	}

	public void modifyMenu(MenuVO menu) throws FamilyBizException {
		this.getFbDao().update("updateMenu", menu);
	}

	public int removeFunc(int funcId) throws FamilyBizException {
		return this.getFbDao().update("deleteMenuFunc", Integer.valueOf(funcId));
	}

	public int removeMenu(int menuId) throws FamilyBizException {
		return this.getFbDao().update("deleteMenu", Integer.valueOf(menuId));
	}

}
