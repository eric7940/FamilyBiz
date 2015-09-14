package com.fb.vo;

import java.io.Serializable;

public class PickOfferVO implements Serializable{
	private static final long serialVersionUID = -2797817211546266526L;


	private String masterId;
	private Integer custId;
	private String custNme;
	private Double qty;
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
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
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	

}
