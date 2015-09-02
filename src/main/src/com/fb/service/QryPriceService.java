package com.fb.service;

import com.fb.util.FamilyBizException;

public interface QryPriceService extends Service {

	public double getCustProdPrice(int custId, int prodId) throws FamilyBizException;
	
}
