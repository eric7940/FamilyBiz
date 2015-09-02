package com.fb.vo;

import java.io.Serializable;

public class CustProfVO implements Serializable{
	private static final long serialVersionUID = -1890362551254939268L;

	private Integer custId;
	private String custNme;
	private String bizNo;
	private String deliverAddr;
	private String tel;
	private String memo;

	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public String getCustNme() {
		return custNme;
	}
	public void setCustNme(String custNme) {
		this.custNme = custNme;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getDeliverAddr() {
		return deliverAddr;
	}
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
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
