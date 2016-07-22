package com.fb.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fb.dao.FBDao;
import com.fb.service.Service;
import com.fb.service.ServiceFactory;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;

public abstract class ServiceImpl implements Service {

	private ServiceFactory serviceFactory;

	private FBDao fbDao;

	protected Logger logger = Logger.getLogger(this.getClass());

	public FBDao getFbDao() {
		return this.fbDao;
	}

	public void setFbDao(FBDao fbDao) {
		this.fbDao = fbDao;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	protected Integer getSequenceNbr(String seqId) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("PI_SEQ_ID", seqId);
		paramMap.put("PI_SEQ_PREFIX", DateUtil.getDateString(new Date(), "yyyyMMdd"));
		this.getFbDao().call("getSequence", paramMap);
		return (Integer) paramMap.get("PO_SEQ_NBR");
	}

}
