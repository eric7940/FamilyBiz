package com.fb.vo;

import java.io.Serializable;

public class StockProfVO implements Serializable{

	private static final long serialVersionUID = 4580876424488415246L;

	private Integer stockId;
	private String stockNme;
	private String addr;
	
	/**
	 * @return the stockId
	 */
	public Integer getStockId() {
		return stockId;
	}
	/**
	 * @param stockId the stockId to set
	 */
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	/**
	 * @return the stockNme
	 */
	public String getStockNme() {
		return stockNme;
	}
	/**
	 * @param stockNme the stockNme to set
	 */
	public void setStockNme(String stockNme) {
		this.stockNme = stockNme;
	}
	/**
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}
	/**
	 * @param addr the addr to set
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}
	
}
