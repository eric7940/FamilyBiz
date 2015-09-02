package com.fb.service.impl;

import java.util.List;

import com.fb.service.StockService;
import com.fb.util.FamilyBizException;
import com.fb.vo.ProdStockQtyVO;

public class StockServiceImpl extends ServiceImpl implements StockService {

	@SuppressWarnings("unchecked")
	public List<ProdStockQtyVO> getProdsQty() throws FamilyBizException {
		return this.getFbDao().queryForList("selectProdStockQty", null);
	}	

	@SuppressWarnings("unchecked")
	public List<ProdStockQtyVO> getProdQty(int prodId) throws FamilyBizException {
		return this.getFbDao().queryForList("selectProdStockQty", prodId);
	}

	public void adjustQty(List<ProdStockQtyVO> details) throws FamilyBizException {
		Integer stockId = new Integer(1); //todo:
		
		for(int i = 0; i < details.size(); i++) {
			ProdStockQtyVO qty = details.get(i);
			qty.setStockId(stockId);
			this.getFbDao().update("updateProdStockQty", qty);
		}
	}
}
