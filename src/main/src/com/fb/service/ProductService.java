package com.fb.service;

import java.util.List;

import com.fb.util.FamilyBizException;
import com.fb.vo.ProdProfVO;

public interface ProductService extends Service {

	public ProdProfVO getProd(int prodId) throws FamilyBizException;
	
	public List<ProdProfVO> getProds() throws FamilyBizException;
	
	public List<ProdProfVO> getProds(String prodNme) throws FamilyBizException;

	public List<ProdProfVO> getProds(int custId) throws FamilyBizException;

	public List<ProdProfVO> getProds(int custId, String prodNme) throws FamilyBizException;

	public void addProd(ProdProfVO prod) throws FamilyBizException;

	public void modifyProd(ProdProfVO prod) throws FamilyBizException;
	
	public int removeProd(int prodId) throws FamilyBizException;
	
	@SuppressWarnings("unchecked")
	public int removeProds(List prodIds) throws FamilyBizException;
}
