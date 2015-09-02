package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.FactProfVO;

public interface FactoryService extends Service {

	public FactProfVO getFact(int factId) throws FamilyBizException;
	
	public List<FactProfVO> getFacts() throws FamilyBizException;
	
	public List<FactProfVO> getFacts(String factNme) throws FamilyBizException;
	
	public void addFact(FactProfVO fact) throws FamilyBizException;

	public void modifyFact(FactProfVO fact) throws FamilyBizException;
	
	public int removeFact(int factId) throws FamilyBizException;
	
	@SuppressWarnings("unchecked")
	public int removeFacts(List factIds) throws FamilyBizException;
}
