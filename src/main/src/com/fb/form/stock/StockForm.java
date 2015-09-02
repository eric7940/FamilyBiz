package com.fb.form.stock;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.LabelValueBean;
import com.fb.vo.ProdStockQtyVO;

public class StockForm extends ActionForm {

	private static final long serialVersionUID = -6816710669557416088L;

	private List<ProdStockQtyVO> prods;
	private List<LabelValueBean> units;
	private Integer prodId;
	private String prodNme;
	private String unit;
	private Integer price;
	private Integer cost;
	private Integer saveQty;
	
	public List<ProdStockQtyVO> getProds() {
		return prods;
	}
	public void setProds(List<ProdStockQtyVO> prods) {
		this.prods = prods;
	}
	public List<LabelValueBean> getUnits() {
		return units;
	}
	public void setUnits(List<LabelValueBean> units) {
		this.units = units;
	}
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
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getSaveQty() {
		return saveQty;
	}
	public void setSaveQty(Integer saveQty) {
		this.saveQty = saveQty;
	}
	
}
