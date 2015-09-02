package com.fb.vo;

import java.io.Serializable;

public class ProdStockQtyVO implements Serializable {

	private static final long serialVersionUID = -3210237349045821638L;

	private Integer stockId;
	private Integer prodId;
	private Double qty;
	
	private StockProfVO stock;
	private ProdProfVO prod;
	
	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	public Integer getProdId() {
		return prodId;
	}
	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}
	public Double getQty() {
		return qty;
	}
	public void setQty(Double qty) {
		this.qty = qty;
	}
	public StockProfVO getStock() {
		return stock;
	}
	public void setStock(StockProfVO stock) {
		this.stock = stock;
	}
	public ProdProfVO getProd() {
		return prod;
	}
	public void setProd(ProdProfVO prod) {
		this.prod = prod;
	}

}
