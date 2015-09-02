package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public interface FuncService extends Service {

	public MenuFuncVO getFunc(int funcId) throws FamilyBizException;
	public MenuVO getMenu(int menuId) throws FamilyBizException;
	public List<MenuFuncVO> getFuncs() throws FamilyBizException;
	public List<MenuVO> getMenus() throws FamilyBizException;
	public void addFunc(MenuFuncVO func) throws FamilyBizException;
	public void addMenu(MenuVO menu) throws FamilyBizException;
	public void modifyFunc(MenuFuncVO func) throws FamilyBizException;
	public void modifyMenu(MenuVO menu) throws FamilyBizException;
	public int removeFunc(int funcId) throws FamilyBizException;	
	public int removeMenu(int menuId) throws FamilyBizException;	
}
