package com.fb.service.impl;

import java.util.List;

import com.fb.service.CustomerService;
import com.fb.util.CommonUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProfVO;

public class CustomerServiceImpl extends ServiceImpl implements CustomerService {

	public CustProfVO getCust(int custId) throws FamilyBizException {
		CustProfVO cust = new CustProfVO();
		cust.setCustId(Integer.valueOf(custId));
		return (CustProfVO) this.getFbDao().queryForObject("selectCustProf", cust);
	}
	
	public List<CustProfVO> getCusts() throws FamilyBizException {
		return getCusts(null);
	}

	@SuppressWarnings("unchecked")
	public List<CustProfVO> getCusts(String custNme) throws FamilyBizException {
		CustProfVO cust = new CustProfVO();
		if (custNme != null) {
			cust.setCustNme(custNme + "%");
		}
		return this.getFbDao().queryForList("selectCustProf", cust);
	}

	public void addCust(CustProfVO cust) throws FamilyBizException {
		this.getFbDao().insert("insertCust", cust);
	}

	public void modifyCust(CustProfVO cust) throws FamilyBizException {
		this.getFbDao().update("updateCust", cust);
	}

	public int removeCust(int custId) throws FamilyBizException {
		return this.getFbDao().update("deleteCust", Integer.valueOf(custId));
	}

	@SuppressWarnings("unchecked")
	public int removeCusts(List custIds) throws FamilyBizException {
		String s = CommonUtil.convertListToString(custIds, ",", true);
		return this.getFbDao().update("deleteCusts", s);
	}
}
