package com.fb.form.basic;

import org.apache.struts.action.ActionForm;

public class IndexForm extends ActionForm {

	private static final long serialVersionUID = 4475144758992576653L;

	private String account;
	private String passwd;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	

}
