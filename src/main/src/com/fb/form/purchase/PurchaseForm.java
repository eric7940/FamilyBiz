package com.fb.form.purchase;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.FactProfVO;
import com.fb.vo.PurchaseMasterVO;

public class PurchaseForm extends ActionForm {

	private static final long serialVersionUID = 4875061445148737362L;

	private List<FactProfVO> facts;
	private List<PurchaseMasterVO> purchases;

	private String masterId;
	private Integer factId;
	private String invoiceNbr;
	private String purchaseDate;
	private Double amt;
	private Double discount;
	private Double total;
	private String memo;
	
	public List<FactProfVO> getFacts() {
		return facts;
	}
	public void setFacts(List<FactProfVO> facts) {
		this.facts = facts;
	}
	public List<PurchaseMasterVO> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<PurchaseMasterVO> purchases) {
		this.purchases = purchases;
	}
	public String getMasterId() {
		return masterId;
	}
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	public Integer getFactId() {
		return factId;
	}
	public void setFactId(Integer factId) {
		this.factId = factId;
	}
	public String getInvoiceNbr() {
		return invoiceNbr;
	}
	public void setInvoiceNbr(String invoiceNbr) {
		this.invoiceNbr = invoiceNbr;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
