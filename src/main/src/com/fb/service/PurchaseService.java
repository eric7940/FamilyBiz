package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.PurchaseDetailVO;
import com.fb.vo.PurchaseMasterVO;

public interface PurchaseService extends Service {

	public PurchaseMasterVO getPurchase(int masterId) throws FamilyBizException;
	
	public List<PurchaseMasterVO> getPurchases(int custId, boolean back) throws FamilyBizException;

	public String addPurchase(PurchaseMasterVO master, List<PurchaseDetailVO> details, boolean back) throws FamilyBizException;

	public int removePurchase(String masterId, boolean back) throws FamilyBizException;
}
