package com.fb.form.basic;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.LabelValueBean;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public class AuthForm extends ActionForm {

	private static final long serialVersionUID = 4475144758992576653L;

	private List<LabelValueBean> userClasses;
	private List<MenuFuncVO> funcs;
	private String userClass;
	
	public List<LabelValueBean> getUserClasses() {
		return userClasses;
	}
	public void setUserClasses(List<LabelValueBean> userClasses) {
		this.userClasses = userClasses;
	}
	public List<MenuFuncVO> getFuncs() {
		return funcs;
	}
	public void setFuncs(List<MenuFuncVO> funcs) {
		this.funcs = funcs;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	

}
