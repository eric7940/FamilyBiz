package com.fb.vo;

import java.io.Serializable;

public class FactProfVO implements Serializable{
	private static final long serialVersionUID = -8617030213822592181L;

	private Integer factId;
	private String factNme;
	private String bizNo;
	private String contact;
	private String addr;
	private String tel;
	private String memo;
	
	public Integer getFactId() {
		return factId;
	}
	public void setFactId(Integer factId) {
		this.factId = factId;
	}
	public String getFactNme() {
		return factNme;
	}
	public void setFactNme(String factNme) {
		this.factNme = factNme;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
