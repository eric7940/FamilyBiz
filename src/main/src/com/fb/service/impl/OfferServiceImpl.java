package com.fb.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fb.service.OfferService;
import com.fb.util.CommonUtil;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProdHisVO;
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.fb.vo.ProdStockQtyVO;
import com.fb.vo.UserProfVO;

public class OfferServiceImpl extends ServiceImpl implements OfferService {

	public OfferMasterVO getOffer(int masterId) throws FamilyBizException {
		return (OfferMasterVO) this.getFbDao().queryForObject("selectOffer", new Integer(masterId));
	}

	@SuppressWarnings("unchecked")
	public List getOffers(int custId, boolean back) throws FamilyBizException {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -2);
		Date startDate = c.getTime();
		return this.getOffers(custId, startDate, today, back);
	}

	@SuppressWarnings("unchecked")
	public List getOffers(int custId, Date startDate, Date endDate, boolean back) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("custId", new Integer(custId));
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("back", back? "Y": null);
		List<OfferMasterVO> queryForList = this.getFbDao().queryForList("selectOffers", paramMap);
		return queryForList;
	}

	@SuppressWarnings("unchecked")
	public List getOffers(List<String> masterIds, boolean back) throws FamilyBizException {
		String s = CommonUtil.convertListToString(masterIds, ",", true);
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("masterId", s);
		paramMap.put("back", back? "Y": null);
		return this.getFbDao().queryForList("selectOffers", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List getOffers(Date startDate, Date endDate, boolean back) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("back", back? "Y": null);
		return this.getFbDao().queryForList("selectOffers", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List getOffers(int custId, String prodId, Date startDate, Date endDate, boolean back) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("custId", new Integer(custId));
		paramMap.put("prodId", new Integer(prodId));
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("back", back? "Y": null);
		return this.getFbDao().queryForList("selectOffers", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<OfferMasterVO> getOffers(String deliveryUserId, Date offerDate, boolean back) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("deliveryUserId", deliveryUserId);
		paramMap.put("offerDate", offerDate);
		paramMap.put("back", back? "Y": null);
		return this.getFbDao().queryForList("selectOffers", paramMap);
	}
	
	public String addOffer(OfferMasterVO master, List<OfferDetailVO> details, boolean back) throws FamilyBizException {
		Integer seqNbr = this.getSequenceNbr("SEQ_TB_OFFER_MASTER_ID");
		DecimalFormat df = new DecimalFormat("0000"); //todo:
		String seq = df.format(seqNbr.intValue());
		  
		String masterId = DateUtil.getDateString(new Date(), "yyMMdd") + seq;
		Integer stockId = new Integer(1); //todo:

		master.setId(masterId);
		master.setStockId(stockId);
		master.setStatus("N");
		this.getFbDao().insert("insertOfferMaster", master);
		
		for(int i = 0; i < details.size(); i++) {
			OfferDetailVO detail = details.get(i);
			detail.setMasterId(masterId);
			this.getFbDao().insert("insertOfferDetail", detail);
			
			CustProdHisVO his = new CustProdHisVO();
			his.setCustId(master.getCustId());
			his.setProdId(detail.getProdId());
			his.setOfferId(masterId);
			his.setPrice(detail.getAmt() / detail.getQty());
			this.getFbDao().insert("insertCustProdHis", his);
			
			ProdStockQtyVO qty = new ProdStockQtyVO();
			qty.setStockId(stockId);
			qty.setProdId(detail.getProdId());
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
		return masterId;
	}
	
	public void modifyOfferReceiveAmt(OfferMasterVO offer) throws FamilyBizException {
		this.getFbDao().update("updateOfferReceiveAmt", offer);
	}

	@SuppressWarnings("unchecked")
	public List<OfferMasterVO> getUnReceivedOffers(Integer custId, Date startDate, Date endDate, boolean beforeFlag) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("custId", custId);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("beforeFlag", beforeFlag? "Y": null);
		List<OfferMasterVO> result = this.getFbDao().queryForList("selectUnReceivedOffers", paramMap);
		
		if (beforeFlag == true) {
			Iterator<OfferMasterVO> it = result.iterator();
			while(it.hasNext()) {
				OfferMasterVO offer = it.next();
				if (offer.getOfferDate().before(startDate)) {
					offer.setId("-" + offer.getId());
				}
			}
		}
		return result;
	}

	public int modifyOffer(OfferMasterVO master, List<OfferDetailVO> details) throws FamilyBizException {
		String masterId = master.getId();
		OfferMasterVO offer = this.getOffer(Integer.parseInt(masterId));
		
		ProdStockQtyVO qty1 = new ProdStockQtyVO();
		Integer stockId = offer.getStock().getStockId();
		qty1.setStockId(stockId);
		for(int i = 0; i < offer.getDetails().size(); i++) {
			OfferDetailVO detail = (OfferDetailVO) offer.getDetails().get(i);
			qty1.setProdId(detail.getProd().getProdId());
			qty1.setQty(detail.getQty());
			this.getFbDao().update("updateProdStockQty", qty1);
		}

		this.getFbDao().delete("deleteCustProdHis", masterId);
		this.getFbDao().delete("deleteOfferDetails", masterId);
		
		master.setStockId(stockId);
		master.setStatus("N");
		this.getFbDao().insert("updateOfferMaster", master);
		
		for(int i = 0; i < details.size(); i++) {
			OfferDetailVO detail = details.get(i);
			detail.setMasterId(masterId);
			this.getFbDao().insert("insertOfferDetail", detail);
			
			CustProdHisVO his = new CustProdHisVO();
			his.setCustId(master.getCustId());
			his.setProdId(detail.getProdId());
			his.setOfferId(masterId);
			his.setPrice(detail.getAmt() / detail.getQty());
			this.getFbDao().insert("insertCustProdHis", his);
			
			ProdStockQtyVO qty = new ProdStockQtyVO();
			qty.setStockId(stockId);
			qty.setProdId(detail.getProdId());
			if (detail.getQty() < 0)
				qty.setQty(new Double(Math.abs(detail.getQty().doubleValue())));
			else
				qty.setQty(new Double("-" + detail.getQty()));
			this.getFbDao().update("updateProdStockQty", qty);
		}
		return Integer.parseInt(masterId);
	}

	public int removeOffer(String masterId, boolean back) throws FamilyBizException {
		OfferMasterVO offer = this.getOffer(Integer.parseInt(masterId));
		
		ProdStockQtyVO qty = new ProdStockQtyVO();
		qty.setStockId(offer.getStock().getStockId());
		for(int i = 0; i < offer.getDetails().size(); i++) {
			OfferDetailVO detail = (OfferDetailVO) offer.getDetails().get(i);
			qty.setProdId(detail.getProd().getProdId());
			if (back == false) {
				qty.setQty(detail.getQty());
			} else {
				if (detail.getQty() < 0)
					qty.setQty(new Double(Math.abs(detail.getQty().doubleValue())));
				else
					qty.setQty(new Double("-" + detail.getQty()));
			}
			this.getFbDao().update("updateProdStockQty", qty);
		}

		return this.getFbDao().update("deleteOffer", masterId);
	}

	@SuppressWarnings("unchecked")
	public List getOfferQty(Integer prodId, String month) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("prodId", prodId);
		paramMap.put("back", null);
		paramMap.put("month", month);
		List result1 = this.getFbDao().queryForList("selectOfferQty", paramMap);

		paramMap.put("back", "Y");
		List result2 = this.getFbDao().queryForList("selectOfferQty", paramMap);

		Iterator it2 = result2.iterator();
		while(it2.hasNext()) {
			Map qty2 = (HashMap)it2.next();
			double q2 = ((BigDecimal)qty2.get("QTY")).doubleValue();

			Iterator it1 = result1.iterator();
			while(it1.hasNext()) {
				Map qty1 = (HashMap)it1.next();
				if (qty1.get("CUST_ID").equals(qty2.get("CUST_ID"))) {
					double q1 = ((BigDecimal)qty2.get("QTY")).doubleValue();
					double q = q1 - q2;
					qty1.put("QTY", new Double(q));
				}
			}
		}
		return result1;
	}

	@SuppressWarnings("unchecked")
	public List getDeliveryUsers() throws FamilyBizException {
		List<String> classes = new ArrayList<String>();
		classes.add("BOSS");
		classes.add("STAF");
		UserProfVO user = new UserProfVO();
		user.setUserClass(CommonUtil.convertListToString(classes, ",", true));
		return this.getFbDao().queryForList("selectUserProf", user);
	}

	public BigDecimal getTotalDiscount(String month) throws FamilyBizException {
		return (BigDecimal) this.getFbDao().queryForObject("selectTotalDiscount", month);
	}

	public List getTopDiscountCusts(String month) throws FamilyBizException {
		return this.getFbDao().queryForList("selectTopDiscountCusts", month);
	}

	public List getTopDiscountOffers(String month, String custId) throws FamilyBizException {
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("month", month);
		paramMap.put("custId", custId);
		return this.getFbDao().queryForList("selectDiscountOffers", paramMap);
	}

	@Override
	public List getProdQty(Date offerDate) throws FamilyBizException {
		return this.getFbDao().queryForList("selectProdQtyByOfferDate", DateUtil.getDateString(offerDate, "yyyy/MM/dd"));
	}

}
