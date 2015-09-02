package com.fb.vo;

import java.io.Serializable;

public class OfferDetailVO implements Serializable{
	private static final long serialVersionUID = -6362953251880593277L;

	private Integer id;
	private String masterId;
	private Integer prodId;
	private ProdProfVO prod;
	private Double qty;
	private Double amt;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	public Integer getProdId() {
		return prodId;
	}
	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}
	public ProdProfVO getProd() {
		return prod;
	}
	public void setProd(ProdProfVO prod) {
		this.prod = prod;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}

}
