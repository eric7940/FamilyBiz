package com.fb.vo;

import java.io.Serializable;
import java.util.List;

public class PickProdVO implements Serializable{
	private static final long serialVersionUID = -6362953251880593277L;

	private Integer prodId;
	private String prodNme;
	private String unit;
	private Double sumQty;
	private List<PickOfferVO> offers;
		
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

	public List<PickOfferVO> getOffers() {
		return offers;
	}

	public void setOffers(List<PickOfferVO> offers) {
		this.offers = offers;
	}


}
