package com.fb.form.basic;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.LabelValueBean;

public class ConfigForm extends ActionForm {

	private static final long serialVersionUID = 4475144758992576653L;

	private List<LabelValueBean> units;
	private String unitCde;
	private String unitNme;
	private String offerMemo;
	
	public List<LabelValueBean> getUnits() {
		return units;
	}
	public void setUnits(List<LabelValueBean> units) {
		this.units = units;
	}
	public String getUnitCde() {
		return unitCde;
	}
	public void setUnitCde(String unitCde) {
		this.unitCde = unitCde;
	}
	public String getUnitNme() {
		return unitNme;
	}
	public void setUnitNme(String unitNme) {
		this.unitNme = unitNme;
	}
	public String getOfferMemo() {
		return offerMemo;
	}
	public void setOfferMemo(String offerMemo) {
		this.offerMemo = offerMemo;
	}
}
