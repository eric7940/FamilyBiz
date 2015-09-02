package com.fb.vo;

import java.io.Serializable;

public class FactProdHisVO implements Serializable {

	private static final long serialVersionUID = -4319792409036669614L;

	private Integer factId;
	private FactProfVO fact;
	private Integer prodId;
	private ProdProfVO prod;
	private Double price;
	private String purchaseId;
	
	public Integer getFactId() {
		return factId;
	}
	public void setFactId(Integer factId) {
		this.factId = factId;
	}
	public FactProfVO getFact() {
		return fact;
	}
	public void setFact(FactProfVO fact) {
		this.fact = fact;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPurchaseId() {
		return purchaseId;
	}
	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}
	
}
