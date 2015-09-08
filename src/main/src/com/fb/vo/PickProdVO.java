package com.fb.vo;

import java.io.Serializable;
import java.util.List;

public class PickProdVO implements Serializable{
	private static final long serialVersionUID = -6362953251880593277L;

	private Integer prodId;
	private String prodNme;
	private String unit;
	private Double sumQty;
	private String masterId;
	private Integer custId;
	private String custNme;
	private Double qty;
		
	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProdNme() {
		return prodNme;
	}

	public void setProdNme(String prodNme) {
		this.prodNme = prodNme;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getSumQty() {
		return sumQty;
	}

	public void setSumQty(Double sumQty) {
		this.sumQty = sumQty;
	}

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
