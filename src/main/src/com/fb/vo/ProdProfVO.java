package com.fb.vo;

import java.io.Serializable;

public class ProdProfVO implements Serializable{

	private static final long serialVersionUID = -2256320739753652965L;

	private Integer prodId;
	private String prodNme;
	private String unit;
	private Double price;
	private Double cost;
	private Integer saveQty;
	
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getSaveQty() {
		return saveQty;
	}
	public void setSaveQty(Integer saveQty) {
		this.saveQty = saveQty;
	}
}
