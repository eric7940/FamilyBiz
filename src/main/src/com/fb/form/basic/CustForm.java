package com.fb.form.basic;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.CustProfVO;

public class CustForm extends ActionForm {

	private static final long serialVersionUID = -3724120956037035599L;

	private List<CustProfVO> custs;
	private Integer custId;
	private String custNme;
	private String bizNo;
	private String deliverAddr;
	private String tel;
	private String memo;
	
	public List<CustProfVO> getCusts() {
		return custs;
	}
	public void setCusts(List<CustProfVO> custs) {
		this.custs = custs;
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
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getDeliverAddr() {
		return deliverAddr;
	}
	public void setDeliverAddr(String deliverAddr) {
		this.deliverAddr = deliverAddr;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
