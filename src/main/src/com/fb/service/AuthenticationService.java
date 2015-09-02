package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.UserProfVO;

public interface AuthenticationService extends Service {

	public UserProfVO login(String id, String pwd) throws FamilyBizException;
	
	public boolean auth(UserProfVO user, String url) throws FamilyBizException;

	public List<MenuFuncVO> getMenuFunctions(UserProfVO user) throws FamilyBizException;
	
}
