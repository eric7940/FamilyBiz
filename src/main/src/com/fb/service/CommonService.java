package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;

public interface CommonService extends Service {

	public List<LabelValueBean> getUnits() throws FamilyBizException;
	
	public void modifyUnits(List<String> units) throws FamilyBizException;

	public String getOfferDefaultMemo() throws FamilyBizException;
	
	public void modifyOfferDefaultMemo(String memo) throws FamilyBizException;
	
}
