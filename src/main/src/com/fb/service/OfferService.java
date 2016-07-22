package com.fb.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.fb.vo.UserProfVO;

public interface OfferService extends Service {

	public OfferMasterVO getOffer(int masterId) throws FamilyBizException;
	
	public List<OfferMasterVO> getOffers(int custId, boolean back) throws FamilyBizException;
	
	public List<OfferMasterVO> getOffers(int custId, Date startDate, Date endDate, boolean back) throws FamilyBizException;

	public List<OfferMasterVO> getOffers(List<String> masterIds, boolean back) throws FamilyBizException;

	public List<OfferMasterVO> getOffers(Date startDate, Date endDate, boolean back) throws FamilyBizException;

	public List<OfferMasterVO> getOffers(int custId, String prodNme, Date startDate, Date endDate, boolean back) throws FamilyBizException;
	
	public List<OfferMasterVO> getOffers(String deliveryUserId, Date offerDate, boolean back) throws FamilyBizException;

	public List<OfferMasterVO> getOffers(String deliveryUserId, Date startDate, Date endDate, boolean back) throws FamilyBizException;

	public String addOffer(OfferMasterVO master, List<OfferDetailVO> details, boolean back) throws FamilyBizException;

	public void modifyOfferReceiveAmt(OfferMasterVO offer) throws FamilyBizException;

	public List<OfferMasterVO> getUnReceivedOffers(Integer custId, Date startDate, Date endDate, boolean beforeFlag) throws FamilyBizException;

	public int modifyOffer(OfferMasterVO master, List<OfferDetailVO> details) throws FamilyBizException;
	
	public int removeOffer(String masterId, boolean back) throws FamilyBizException;

	@SuppressWarnings("unchecked")
	public List getOfferQty(Integer prodId, String month) throws FamilyBizException;

	public List<UserProfVO> getDeliveryUsers() throws FamilyBizException;

	public BigDecimal getTotalDiscount(String month) throws FamilyBizException;
	
	public List getTopDiscountCusts(String month) throws FamilyBizException;

	public List getTopDiscountOffers(String month, String custId) throws FamilyBizException;
	
	public List getProdQty(Date offerDate, List<String> custs) throws FamilyBizException;

	public List getCustByOfferDate(Date offerDate) throws FamilyBizException;
}
