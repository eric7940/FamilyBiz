package com.fb.service.impl;

import com.fb.service.QryPriceService;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProdHisVO;
import com.fb.vo.ProdProfVO;

public class QryPriceServiceImpl extends ServiceImpl implements QryPriceService {

	public double getCustProdPrice(int custId, int prodId) throws FamilyBizException {
		CustProdHisVO param1VO = new CustProdHisVO();
		param1VO.setCustId(new Integer(custId));
		param1VO.setProdId(new Integer(prodId));
		CustProdHisVO hisVO = (CustProdHisVO) this.getFbDao().queryForObject("selectCustProdHis", param1VO);

		if (hisVO == null) {
			ProdProfVO param2VO = new ProdProfVO();
			param2VO.setProdId(new Integer(prodId));
			ProdProfVO prodVO = (ProdProfVO) this.getFbDao().queryForObject("selectProdProf", param2VO);
			
			if (prodVO == null) {
				throw new FamilyBizException("查無此產品資料");
			}
			return prodVO.getPrice().doubleValue();
		}
		return hisVO.getPrice().doubleValue();
	}

}
