package com.fb.form.offer;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.CustProfVO;
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.fb.vo.UserProfVO;

public class OfferForm extends ActionForm {

	private static final long serialVersionUID = -1280520869794655435L;

	private List<CustProfVO> custs;
	private List<UserProfVO> deliveryUsers;
	private List<OfferMasterVO> offers;
	private CustProfVO cust;
	private List<OfferDetailVO> details;

	private String masterId;
	private Integer custId;
	private String deliveryUserId;
	private String offerDate;
	private String invoiceNbr;
	private Double amt;
	private Double discount;
	private Double total;
	private Double receiveAmt;
	private String memo;
	
	public List<OfferMasterVO> getOffers() {
		return offers;
	}
	public void setOffers(List<OfferMasterVO> offers) {
		this.offers = offers;
	}
	public List<CustProfVO> getCusts() {
		return custs;
	}
	public void setCusts(List<CustProfVO> custs) {
		this.custs = custs;
	}
	public List<UserProfVO> getDeliveryUsers() {
		return deliveryUsers;
	}
	public void setDeliveryUsers(List<UserProfVO> deliveryUsers) {
		this.deliveryUsers = deliveryUsers;
	}
	public CustProfVO getCust() {
		return cust;
	}
	public void setCust(CustProfVO cust) {
		this.cust = cust;
	}
	public void setDetails(List<OfferDetailVO> details) {
		this.details = details;
	}
	public List<OfferDetailVO> getDetails() {
		return details;
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
	public String getOfferDate() {
		return offerDate;
	}
	public void setOfferDate(String offerDate) {
		this.offerDate = offerDate;
	}
	public String getInvoiceNbr() {
		return invoiceNbr;
	}
	public void setInvoiceNbr(String invoiceNbr) {
		this.invoiceNbr = invoiceNbr;
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
	public Double getReceiveAmt() {
		return receiveAmt;
	}
	public void setReceiveAmt(Double receiveAmt) {
		this.receiveAmt = receiveAmt;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeliveryUserId() {
		return deliveryUserId;
	}
	public void setDeliveryUserId(String deliveryUserId) {
		this.deliveryUserId = deliveryUserId;
	}

}
