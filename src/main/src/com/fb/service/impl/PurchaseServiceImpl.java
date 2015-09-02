package com.fb.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.service.PurchaseService;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.FactProdHisVO;
import com.fb.vo.ProdProfVO;
import com.fb.vo.ProdStockQtyVO;
import com.fb.vo.PurchaseDetailVO;
import com.fb.vo.PurchaseMasterVO;

public class PurchaseServiceImpl extends ServiceImpl implements PurchaseService {

	public PurchaseMasterVO getPurchase(int masterId) throws FamilyBizException {
		return (PurchaseMasterVO) this.getFbDao().queryForObject("selectPurchase", new Integer(masterId));
	}

	@SuppressWarnings("unchecked")
	public List getPurchases(int factId, boolean back) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("factId", new Integer(factId));
		paramMap.put("back", back? "Y": null);
		return this.getFbDao().queryForList("selectPurchases", paramMap);
	}
	
	public String addPurchase(PurchaseMasterVO master, List<PurchaseDetailVO> details, boolean back) throws FamilyBizException {
		Integer seqNbr = this.getSequenceNbr("SEQ_TB_PURCHASE_MASTER_ID");
		DecimalFormat df = new DecimalFormat("0000"); //todo:
		String seq = df.format(seqNbr.intValue());
		  
		String masterId = DateUtil.getDateString(new Date(), "yyMMdd") + seq;
		Integer stockId = new Integer(1); //todo:
		
		master.setId(masterId);
		master.setStockId(stockId);
		master.setStatus("N");
		this.getFbDao().insert("insertPurchaseMaster", master);
		
		for(int i = 0; i < details.size(); i++) {
			PurchaseDetailVO detail = details.get(i);
			detail.setMasterId(masterId);
			this.getFbDao().insert("insertPurchaseDetail", detail);
			
			FactProdHisVO his = new FactProdHisVO();
			his.setFactId(master.getFactId());
			his.setProdId(detail.getProdId());
			his.setPurchaseId(masterId);
			his.setPrice(detail.getAmt() / detail.getQty());
			this.getFbDao().insert("insertFactProdHis", his);
			
			ProdStockQtyVO qty = new ProdStockQtyVO();
			qty.setStockId(stockId);
			qty.setProdId(detail.getProdId());
			if (back == false) {
				qty.setQty(detail.getQty());
			} else {
				if (detail.getQty() < 0)
					qty.setQty(new Double(Math.abs(detail.getQty().doubleValue())));
				else
					qty.setQty(new Double("-" + detail.getQty()));
			}
			this.getFbDao().update("updateProdStockQty", qty);
			
			ProdProfVO prod = new ProdProfVO();
			prod.setProdId(detail.getProdId());
			prod.setCost(detail.getAmt() / detail.getQty());
			this.getFbDao().update("updateProdCost", prod);
		}
		return masterId;
	}

	public int removePurchase(String masterId, boolean back) throws FamilyBizException {
		PurchaseMasterVO purchase = this.getPurchase(Integer.parseInt(masterId));
		
		ProdStockQtyVO qty = new ProdStockQtyVO();
		qty.setStockId(purchase.getStock().getStockId());
		for(int i = 0; i < purchase.getDetails().size(); i++) {
			PurchaseDetailVO detail = (PurchaseDetailVO) purchase.getDetails().get(i);
			qty.setProdId(detail.getProd().getProdId());
			if (back == false) {
				if (detail.getQty() < 0)
					qty.setQty(new Double(Math.abs(detail.getQty().doubleValue())));
				else
					qty.setQty(new Double("-" + detail.getQty()));
			} else {
				qty.setQty(detail.getQty());
			}
			this.getFbDao().update("updateProdStockQty", qty);
		}

		return this.getFbDao().update("deletePurchase", masterId);
	}
}
