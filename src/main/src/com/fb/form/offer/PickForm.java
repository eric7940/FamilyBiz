package com.fb.form.offer;

import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.PickProdVO;

public class PickForm extends ActionForm {

	private static final long serialVersionUID = -1280520869794655435L;

	private List<PickProdVO> products;
	private String offerDate;
	
	public List<PickProdVO> getProducts() {
		return products;
	}
	public void setProducts(List<PickProdVO> products) {
		this.products = products;
	}
	public String getOfferDate() {
		return offerDate;
	}
	public void setOfferDate(String offerDate) {
		this.offerDate = offerDate;
	}

}
