package com.fb.vo;

import java.io.Serializable;

public class CustProdHisVO implements Serializable {

	private static final long serialVersionUID = 6713630479715436371L;

	private Integer custId;
	private CustProfVO cust;
	private Integer prodId;
	private ProdProfVO prod;
	private Double price;
	private String offerId;
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	public CustProfVO getCust() {
		return cust;
	}
	public void setCust(CustProfVO cust) {
		this.cust = cust;
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
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
}
