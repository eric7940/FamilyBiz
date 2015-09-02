package com.fb.action.offer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.fb.action.BaseDispatchAction;
import com.fb.form.offer.QryForm;
import com.fb.service.CustomerService;
import com.fb.service.OfferService;
import com.fb.service.ProductService;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.fb.vo.ProdProfVO;
import com.fb.vo.UserProfVO;

public class QryAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(QryAction.class);
	private static DecimalFormat df = new DecimalFormat("#0.00");
	
	public ActionForward init(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("init start");
		try {
			QryForm formBean = (QryForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");
			formBean.setCusts(service.getCusts());
			ProductService service2 = (ProductService) this.getServiceFactory().getService("Product");
			formBean.setProds(service2.getProds());
			OfferService service3 = (OfferService) this.getServiceFactory().getService("Offer");
			UserProfVO emptyUserOption = new UserProfVO();
			emptyUserOption.setUserId("-");
			emptyUserOption.setUserNme("--請選擇--");
			List<UserProfVO> deliveryUsers = service3.getDeliveryUsers();
			deliveryUsers.add(0, emptyUserOption);
			formBean.setDeliveryUsers(deliveryUsers);
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			return this.sendAjaxXMLResponse(response, "<complete><option>" + sce.getMessage() + "</option></complete>");
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			return this.sendAjaxXMLResponse(response, "<complete><option>" + mr.getMessage("all.msg.1") + "</option></complete>");
		}
		logger.info("init end");
		
		return mapping.findForward("init");
	}

	public ActionForward qryPrice(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryPrice start");
		StringBuffer sb = new StringBuffer();
		try {
			String custId = request.getParameter("custId");
			//String prodNme = request.getParameter("q");
			String prodNme = new String(request.getParameter("q").getBytes("ISO8859_1"), "UTF-8");
			
			logger.info("param: custId=" + custId);
			logger.info("param: prodNme=" + prodNme);
			
			if (prodNme != null && ("".equals(prodNme.trim()) || "　".equals(prodNme.trim()))) prodNme = null;
	
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");
			List<ProdProfVO> prods = service.getProds(Integer.parseInt(custId), prodNme);
			
			if (prods != null && prods.size() > 0) {
				Iterator<ProdProfVO> it = prods.iterator();
				while(it.hasNext()) {
					ProdProfVO prod = it.next();
					sb.append(prod.getProdId() + "|" + prod.getProdNme() + "|" + df.format(prod.getPrice()) + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryPrice end");
		return this.sendAjaxResponse(response, sb.toString());
	}

	public ActionForward qryProdOffers(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryProdOffers start");
		StringBuffer sb = new StringBuffer();
		try {
			String custId = request.getParameter("custId");
			String prodId = request.getParameter("prodId");
			
			logger.info("param: custId=" + custId);
			logger.info("param: prodId=" + prodId);
			
			Calendar c = Calendar.getInstance();
			Date today = c.getTime();
			c.set(Calendar.MONTH, -6);
			Date startDate = c.getTime();
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			List<OfferMasterVO> offers = service.getOffers(Integer.parseInt(custId), prodId, startDate, today, false);
			
			if (offers != null && offers.size() > 0) {
				Iterator<OfferMasterVO> it = offers.iterator();
				while(it.hasNext()) {
					OfferMasterVO master = it.next();
					OfferDetailVO detail = (OfferDetailVO) master.getDetails().get(0);
					ProdProfVO prod = (ProdProfVO) detail.getProd();
					sb.append(master.getId() + "|" + DateUtil.getDateString(master.getOfferDate()) + "|" + prod.getProdId() + "|" + prod.getProdNme() + "|" + df.format(detail.getQty()) + "|" + prod.getUnit() + "|" + df.format(detail.getAmt() / detail.getQty()) + "|" + df.format(detail.getAmt()) + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryProdOffers end");
		return this.sendAjaxResponse(response, sb.toString());
	}

	@SuppressWarnings("unchecked")
	public ActionForward qrySale(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qrySale start");
		StringBuffer sb = new StringBuffer();
		try {
			String prodId = request.getParameter("prodId");
			String month = request.getParameter("month");

			month = DateUtil.getDateString(DateUtil.getDateObject(month), "yyyyMM");
			
			logger.info("param: prodId=" + prodId);
			logger.info("param: month=" + month);
			
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			List prods = service.getOfferQty(Integer.parseInt(prodId), month);
			
			if (prods != null && prods.size() > 0) {
				Iterator it = prods.iterator();
				while(it.hasNext()) {
					Map result = (Map) it.next();
					sb.append(result.get("CUST_ID") + "|" + result.get("CUST_NME") + "|" + result.get("QTY") + "|" + result.get("UNIT") + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qrySale end");
		return this.sendAjaxResponse(response, sb.toString());
	}
	
	public ActionForward qryUserOffers(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryUserOffers start");
		StringBuffer sb = new StringBuffer();
		try {
			String userId = request.getParameter("userId");
			String date = request.getParameter("date");

			logger.info("param: userId=" + userId);
			logger.info("param: date=" + date);

			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			List<OfferMasterVO> offers = service.getOffers(userId, DateUtil.getDateObject(date), false);
			
			if (offers != null && offers.size() > 0) {
				Iterator<OfferMasterVO> it = offers.iterator();
				while(it.hasNext()) {
					OfferMasterVO master = it.next();
//					OfferDetailVO detail = (OfferDetailVO) master.getDetails().get(0);
//					ProdProfVO prod = (ProdProfVO) detail.getProd();
					sb.append(master.getId() + "|" + DateUtil.getDateString(master.getOfferDate()) + "|" + master.getCust().getCustNme() + "|" + df.format(master.getTotal()) + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryUserOffers end");
		return this.sendAjaxResponse(response, sb.toString());
	}
	
	public ActionForward qryDiscount(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryDiscount start");
		StringBuffer sb = new StringBuffer();
		try {
			String month = request.getParameter("date");

			month = DateUtil.getDateString(DateUtil.getDateObject(month), "yyyyMM");
			
			logger.info("param: month=" + month);

			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			BigDecimal totalDiscount = service.getTotalDiscount(month);
			sb.append(totalDiscount + "\n");

			List offers = service.getTopDiscountCusts(month);
			
			if (offers != null && offers.size() > 0) {
				Iterator<Map> it = offers.iterator();
				while(it.hasNext()) {
					Map master = it.next();
					sb.append(master.get("CUST_ID") + "|" + master.get("CUST_NME") + "|" + df.format(master.get("DISCOUNT")) + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryDiscount end");
		return this.sendAjaxResponse(response, sb.toString());
	}	

	public ActionForward qryDiscountCust(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryDiscountCust start");
		StringBuffer sb = new StringBuffer();
		try {
			String month = request.getParameter("date");
			String custId = request.getParameter("id");
			
			month = DateUtil.getDateString(DateUtil.getDateObject(month), "yyyyMM");
			
			logger.info("param: month=" + month);
			logger.info("param: custId=" + custId);

			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			List offers = service.getTopDiscountOffers(month, custId);
			if (offers != null && offers.size() > 0) {
				Iterator<OfferMasterVO> it = offers.iterator();
				while(it.hasNext()) {
					OfferMasterVO master = it.next();
					sb.append(master.getId() + "|" + df.format(master.getDiscount()) + "\n");
				}
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryDiscountCust end");
		return this.sendAjaxResponse(response, sb.toString());
	}	
}	
