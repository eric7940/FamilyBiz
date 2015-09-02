package com.fb.service.impl;

import java.util.List;

import com.fb.service.FactoryService;
import com.fb.util.CommonUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.FactProfVO;

public class FactoryServiceImpl extends ServiceImpl implements FactoryService {

	public FactProfVO getFact(int factId) throws FamilyBizException {
		FactProfVO fact = new FactProfVO();
		fact.setFactId(Integer.valueOf(factId));
		return (FactProfVO) this.getFbDao().queryForObject("selectFactProf", fact);
	}
	
	public List<FactProfVO> getFacts() throws FamilyBizException {
		return getFacts(null);
	}

	@SuppressWarnings("unchecked")
	public List<FactProfVO> getFacts(String factNme) throws FamilyBizException {
		FactProfVO fact = new FactProfVO();
		if (factNme != null) {
			fact.setFactNme(factNme + "%");
		}
		return this.getFbDao().queryForList("selectFactProf", fact);
	}

	public void addFact(FactProfVO fact) throws FamilyBizException {
		this.getFbDao().insert("insertFact", fact);
	}

	public void modifyFact(FactProfVO fact) throws FamilyBizException {
		this.getFbDao().update("updateFact", fact);
	}

	public int removeFact(int factId) throws FamilyBizException {
		return this.getFbDao().update("deleteFact", Integer.valueOf(factId));
	}

	@SuppressWarnings("unchecked")
	public int removeFacts(List factIds) throws FamilyBizException {
		String s = CommonUtil.convertListToString(factIds, ",", true);
		return this.getFbDao().update("deleteFacts", s);
	}
}
