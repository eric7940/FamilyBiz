package com.fb.vo;

import java.io.Serializable;

public class UserProfVO implements Serializable{
	private static final long serialVersionUID = -8617030213822592181L;

	private String userId;
	private String userNme;
	private String userPwd;
	private String userClass;
	private String statusFlag;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNme() {
		return userNme;
	}
	public void setUserNme(String userNme) {
		this.userNme = userNme;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserClass() {
		return userClass;
	}
	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	
}
