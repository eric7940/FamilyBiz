package com.fb.form.offer;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.fb.vo.CustProfVO;
import com.fb.vo.ProdProfVO;
import com.fb.vo.UserProfVO;

public class QryForm extends ActionForm {

	private static final long serialVersionUID = -8838820695135425122L;

	private List<CustProfVO> custs;
	private List<ProdProfVO> prods;
	private List<UserProfVO> deliveryUsers;

	public List<CustProfVO> getCusts() {
		return custs;
	}

	public void setCusts(List<CustProfVO> custs) {
		this.custs = custs;
	}

	public List<ProdProfVO> getProds() {
		return prods;
	}

	public void setProds(List<ProdProfVO> prods) {
		this.prods = prods;
	}

	public List<UserProfVO> getDeliveryUsers() {
		return deliveryUsers;
	}

	public void setDeliveryUsers(List<UserProfVO> deliveryUsers) {
		this.deliveryUsers = deliveryUsers;
	}
}
