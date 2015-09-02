package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.CustProfVO;

public interface CustomerService extends Service {

	public CustProfVO getCust(int custId) throws FamilyBizException;
	
	public List<CustProfVO> getCusts() throws FamilyBizException;
	
	public List<CustProfVO> getCusts(String custNme) throws FamilyBizException;
	
	public void addCust(CustProfVO cust) throws FamilyBizException;

	public void modifyCust(CustProfVO cust) throws FamilyBizException;
	
	public int removeCust(int custId) throws FamilyBizException;
	
	@SuppressWarnings("unchecked")
	public int removeCusts(List custIds) throws FamilyBizException;
}
