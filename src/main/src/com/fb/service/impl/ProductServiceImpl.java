package com.fb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fb.service.ProductService;
import com.fb.util.CommonUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.ProdProfVO;
import com.fb.vo.ProdStockQtyVO;

public class ProductServiceImpl extends ServiceImpl implements ProductService {

	public ProdProfVO getProd(int prodId) throws FamilyBizException {
		ProdProfVO prod = new ProdProfVO();
		prod.setProdId(prodId);
		return (ProdProfVO) this.getFbDao().queryForObject("selectProdProf", prod);
	}

	@SuppressWarnings("unchecked")
	public List getProds() throws FamilyBizException {
		return getProds(null);
	}

	@SuppressWarnings("unchecked")
	public List getProds(String prodNme) throws FamilyBizException {
		ProdProfVO prod = new ProdProfVO();
		if (prodNme != null) {
			prod.setProdNme("%" + prodNme + "%");
		}
		return this.getFbDao().queryForList("selectProdProf", prod);
	}

	@SuppressWarnings("unchecked")
	public List getProds(int custId) throws FamilyBizException {
		return getProds(custId, null);
	}

	@SuppressWarnings("unchecked")
	public List getProds(int custId, String prodNme) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("custId", new Integer(custId));
		if (prodNme != null) {
			paramMap.put("prodNme", "%" + prodNme + "%");
		}
		return this.getFbDao().queryForList("selectProdByCust", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List getOffers(int custId, String prodNme) throws FamilyBizException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("custId", new Integer(custId));
		if (prodNme != null) {
			paramMap.put("prodNme", "%" + prodNme + "%");
		}
		return this.getFbDao().queryForList("selectOfferByCustAndProd", paramMap);
	}

	public void addProd(ProdProfVO prod) throws FamilyBizException {
		this.getFbDao().insert("insertProd", prod);

		ProdStockQtyVO qty = new ProdStockQtyVO();
		qty.setStockId(new Integer(1)); //todo:
		qty.setQty(new Double(0));
		this.getFbDao().insert("insertProdStockQty", qty);
	}	

	public void modifyProd(ProdProfVO prod) throws FamilyBizException {
		this.getFbDao().update("updateProd", prod);
	}

	public int removeProd(int prodId) throws FamilyBizException {
		return this.getFbDao().update("deleteProd", Integer.valueOf(prodId));
	}

	@SuppressWarnings("unchecked")
	public int removeProds(List prodIds) throws FamilyBizException {
		String s = CommonUtil.convertListToString(prodIds, ",", true);
		return this.getFbDao().update("deleteProds", s);
	}
}
