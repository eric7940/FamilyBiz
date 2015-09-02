package com.fb.action.offer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.fb.action.BaseDispatchAction;
import com.fb.form.offer.OfferForm;
import com.fb.service.CommonService;
import com.fb.service.CustomerService;
import com.fb.service.OfferService;
import com.fb.service.ProductService;
import com.fb.service.QryPriceService;
import com.fb.util.CommonUtil;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProfVO;
import com.fb.vo.OfferDetailVO;
import com.fb.vo.OfferMasterVO;
import com.fb.vo.ProdProfVO;
import com.fb.vo.UserProfVO;

public class OfferAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(OfferAction.class);
	private static DecimalFormat df = new DecimalFormat("#0.00");
	
	public ActionForward initQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initQuery start");
		OfferForm formBean = (OfferForm) form;

		ActionMessages messages;
		if(request.getAttribute(Globals.MESSAGE_KEY) != null){
			messages = (ActionMessages)request.getAttribute(Globals.MESSAGE_KEY);
		}else{
			messages = new ActionMessages();
			request.getSession().setAttribute("OFFER_BACK", "N");
			if ("Y".equals(request.getParameter("back")))
				request.getSession().setAttribute("OFFER_BACK", "Y");

		}

		CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");

		formBean.setCusts(service.getCusts());

		if (request.getParameter("id") != null && request.getParameter("id").length() > 0) {
			request.setAttribute("OFFER_ID", request.getParameter("id"));
		}
		
		logger.info("initQuery end");
		saveMessages(request, messages);
		return mapping.findForward("query");
	}
	
	public ActionForward query(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("query start");
		MessageResources mr = this.getResources(request);
		StringBuffer sb = new StringBuffer();

		String custId = request.getParameter("custId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String offerId = request.getParameter("offerId");

		try {
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			
			OfferMasterVO offer = null;
			if (offerId == null) {
				boolean back = "Y".equals(request.getSession().getAttribute("OFFER_BACK"));
				List<OfferMasterVO> offers = null;
				if (custId != null && !"".equals(custId)) {
					offers = service.getOffers(Integer.parseInt(custId), back);
				} else {
					offers = service.getOffers(DateUtil.getDateObject(startDate), DateUtil.getDateObject(endDate), back);
				}
				if (offers != null && !offers.isEmpty()) {
					if (offers.size() > 1) {
						sb.append("errCde=00\n");
						sb.append("errMsg=\n");
						sb.append("rowCount=" + offers.size() + "\n");
						sb.append("custId=" + custId + "\n");
						sb.append("startDate=" + startDate + "\n");
						sb.append("endDate=" + endDate + "\n");
						this.sendAjaxResponse(response, sb.toString());
						return null;
					} else {
						offer = (OfferMasterVO)offers.get(0);	
					}
				}
			} else {
				offer = service.getOffer(Integer.parseInt(offerId));
			}
			
			if (offer == null) {
				sb.append("errCde=99\n");
				sb.append("errMsg=" + mr.getMessage("offer.msg.1") + "\n");
				this.sendAjaxResponse(response, sb.toString());
				return null;
			}
			
			CustProfVO cust = offer.getCust();
			UserProfVO deliveryUser = offer.getDeliveryUser();
			
			sb.append("errCde=00\n");
			sb.append("errMsg=\n");
			sb.append("rowCount=1\n");
			
			sb.append("masterId=" + offer.getId() + "\n");
			sb.append("offerDate=" + DateUtil.getDateString(offer.getOfferDate()) + "\n");
			sb.append("invoiceNbr=" + offer.getInvoiceNbr() + "\n");
			sb.append("amt=" + df.format(offer.getAmt()) + "\n");
			sb.append("discount=" + df.format(offer.getDiscount()) + "\n");
			sb.append("total=" + df.format(offer.getTotal()) + "\n");
			sb.append("receiveAmt=" + df.format(offer.getReceiveAmt()) + "\n");
			sb.append("memo=" + offer.getMemo() + "\n");
			
			sb.append("custId=" + cust.getCustId() + "\n");
			sb.append("custNme=" + cust.getCustNme() + "\n");
			sb.append("deliveryUserNme=" + deliveryUser.getUserNme() + "\n");
			sb.append("bizNo=" + cust.getBizNo() + "\n");
			sb.append("tel=" + cust.getTel() + "\n");
			sb.append("deliverAddr=" + cust.getDeliverAddr() + "\n");
			sb.append("custMemo=" + cust.getMemo() + "\n");
			
			List<OfferDetailVO> details = offer.getDetails();
			if (details != null && !details.isEmpty()) {
				sb.append("detailCount=" + details.size() + "\n");
				for(int i = 0; i < details.size(); i++) {
					OfferDetailVO detail = details.get(i);
					ProdProfVO product = detail.getProd();
					sb.append("detail-" + i + "-qty=" + detail.getQty() + "\n");
					sb.append("detail-" + i + "-amt=" + df.format(detail.getAmt()) + "\n");
					sb.append("detail-" + i + "-prodId=" + product.getProdId() + "\n");
					sb.append("detail-" + i + "-prodNme=" + product.getProdNme() + "\n");
					sb.append("detail-" + i + "-unit=" + product.getUnit() + "\n");
					sb.append("detail-" + i + "-price=" + df.format(detail.getAmt() / detail.getQty()) + "\n");
				}
			} else {
				sb.append("detailCount=0\n");
			}
			
		} catch (FamilyBizException e) {
			logger.error("", e);
			sb.append("errCde=01\n");
			sb.append("errMsg=" + e.getMessage() + "\n");
			this.sendAjaxResponse(response, sb.toString());
			return null;
		} catch (Exception e) {
			logger.error("", e);
			sb.append("errCde=99\n");
			sb.append("errMsg=" + mr.getMessage("all.msg.1") + "\n");//todo:
			this.sendAjaxResponse(response, sb.toString());
			return null;
		}
		logger.info("query end");
		this.sendAjaxResponse(response, sb.toString());
		return null;
	}
	
	public ActionForward getOfferList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("getOfferList start");

		try {
			OfferForm formBean = (OfferForm)form;
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			
			String feeid = request.getParameter("feeid");

			boolean back = "Y".equals(request.getSession().getAttribute("OFFER_BACK"));
			
			List<OfferMasterVO> offers = null;
			if (feeid.indexOf(";") < 0) {
				String custId = feeid;
				offers = service.getOffers(Integer.parseInt(custId), back);
			} else {
				String startDate = feeid.substring(0, feeid.indexOf(";"));
				String endDate = feeid.substring(feeid.indexOf(";") + 1);
				offers = service.getOffers(DateUtil.getDateObject(startDate), DateUtil.getDateObject(endDate), back);
			}

			formBean.setOffers(offers);

			request.setAttribute("ERR_CDE", "00");
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			request.setAttribute("ERR_CDE", "01");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			request.setAttribute("ERR_CDE", "99");
		}
		logger.info("getOfferList end");
		saveMessages(request, messages);
		
		return mapping.findForward("offerlist");
	}
	
	public ActionForward initCreate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("initCreate start");

		try {
			OfferForm formBean = (OfferForm)form;
			CustomerService service1 = (CustomerService) this.getServiceFactory().getService("Customer");
			CommonService service2 = (CommonService) this.getServiceFactory().getService("Common");
			OfferService service3 = (OfferService) this.getServiceFactory().getService("Offer");
			
			formBean.setCusts(service1.getCusts());
			formBean.setDeliveryUsers(service3.getDeliveryUsers());

			formBean.setDiscount(new Double(0));
			formBean.setMemo(service2.getOfferDefaultMemo());
			formBean.setInvoiceNbr("");
			
			request.getSession().setAttribute("OFFER_BACK", "N");
			if ("Y".equals(request.getParameter("back")))
				request.getSession().setAttribute("OFFER_BACK", "Y");
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		logger.info("initCreate end");
		saveMessages(request, messages);
		
		return mapping.findForward("form");
	}

	public ActionForward getCustList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("getCustList start");
		logger.info("getCustList end");
		return mapping.findForward("custlist");
	}
	
	public ActionForward getProds(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("getProds start");
		StringBuffer sb = new StringBuffer();

		try {
			String custId = request.getParameter("custId");
			//String prodNme = request.getParameter("q");
			String prodNme = new String(request.getParameter("q").getBytes("ISO8859_1"), "UTF-8");

			if (prodNme != null && ("".equals(prodNme.trim()) || "　".equals(prodNme.trim()))) prodNme = null;

			logger.info("param: custId=" + custId);
			logger.info("param: prodNme=" + prodNme);
			
			ProductService service1 = (ProductService) this.getServiceFactory().getService("Product");
			QryPriceService service2 = (QryPriceService) this.getServiceFactory().getService("QryPrice");
			
			List<ProdProfVO> prods = service1.getProds(prodNme);
			if (prods != null && prods.size() > 0) {
				Iterator<ProdProfVO> it = prods.iterator();
				while(it.hasNext()) {
					ProdProfVO prod = it.next();
					double price = service2.getCustProdPrice(Integer.parseInt(custId), prod.getProdId());
					sb.append(prod.getProdId() + "|" + prod.getProdNme() + "|" + df.format(price) + "|" + prod.getUnit() + "|" + df.format(prod.getCost()) + "\n");
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
		
		logger.info("getProds end");
		return this.sendAjaxResponse(response, sb.toString());
	}
	
	public ActionForward create(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("create start");

		try {
			OfferForm formBean = (OfferForm)form;
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			
			logger.info("custId:" + formBean.getCustId());

			double sum = 0;
			double total = 0;
			double totalCost = 0;
			double discount = CommonUtil.round(formBean.getDiscount().doubleValue());
			
			OfferMasterVO master = new OfferMasterVO();
			master.setOfferDate(DateUtil.getDateObject(formBean.getOfferDate()));
			master.setCustId(formBean.getCustId());
			master.setDeliveryUserId(formBean.getDeliveryUserId());
			master.setInvoiceNbr(formBean.getInvoiceNbr());
			master.setDiscount(formBean.getDiscount());
			master.setMemo(formBean.getMemo());
			master.setBack("Y".equals(request.getSession().getAttribute("OFFER_BACK"))? "Y": null);
			
			int detailCount = Integer.parseInt(request.getParameter("detailCount"));
			List<OfferDetailVO> details = new ArrayList<OfferDetailVO>();
			int i = 0;
			while(i < detailCount) {
				String prodId = request.getParameter("detail-" + i + "-prodId");
				if (prodId == null || "".equals(prodId)) {
					i++;
					continue;
				}
				
				String price = request.getParameter("detail-" + i + "-price");
				String cost = request.getParameter("detail-" + i + "-cost");
				String qty = request.getParameter("detail-" + i + "-qty");
				BigDecimal a = BigDecimal.valueOf(Double.parseDouble(price));
				BigDecimal b = BigDecimal.valueOf(Double.parseDouble(qty));
				BigDecimal c = BigDecimal.valueOf(Double.parseDouble(cost));
				BigDecimal x = a.multiply(b);
				BigDecimal y = c.multiply(b);
				double amt = CommonUtil.round(x.doubleValue());
				double amtCost = CommonUtil.round(y.doubleValue());
				
				OfferDetailVO detail = new OfferDetailVO();
				detail.setProdId(Integer.parseInt(prodId));
				detail.setQty(Double.parseDouble(qty));
				detail.setAmt(amt);
			
				sum += amt;
				totalCost += amtCost;
				
				details.add(detail);
				i++;
			}

			total = sum - discount;
			
			master.setAmt(sum);
			master.setTotal(total);
			master.setCost(totalCost);
			master.setReceiveAmt(new Double(0));
			
			String masterId = service.addOffer(master, details, "Y".equals(request.getSession().getAttribute("OFFER_BACK")));
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("offer.msg.2", masterId));
			
			request.setAttribute("OFFER_ID", masterId);
			
			logger.info("create end");
			saveMessages(request, messages);
			return new ActionForward("/offer.do?state=initQuery", false);

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("form");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("form");
		}
	}
	
	public ActionForward initModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("initModify start");

		try {
			OfferForm formBean = (OfferForm)form;
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			
			formBean.setDeliveryUsers(service.getDeliveryUsers());
			
			OfferMasterVO master = service.getOffer(Integer.parseInt(formBean.getMasterId()));
			formBean.setMasterId(master.getId());
			formBean.setOfferDate(DateUtil.getDateString(master.getOfferDate()));
			formBean.setInvoiceNbr(master.getInvoiceNbr());
			formBean.setAmt(master.getAmt());
			formBean.setDiscount(master.getDiscount());
			formBean.setTotal(master.getTotal());
			formBean.setReceiveAmt(master.getReceiveAmt());
			formBean.setMemo(master.getMemo());
			formBean.setCustId(master.getCust().getCustId());
			formBean.setCust(master.getCust());
			formBean.setDeliveryUserId(master.getDeliveryUser().getUserId());
			formBean.setDetails(master.getDetails());
			
			logger.info("initModify end");
			saveMessages(request, messages);
			return mapping.findForward("modify");

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("query");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("query");
		}
	}

	public ActionForward modify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("modify start");

		try {
			OfferForm formBean = (OfferForm)form;
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");
			
			String masterId = formBean.getMasterId();
			Integer custId = formBean.getCustId();
			logger.info("masterId:" + masterId);
			logger.info("custId:" + custId);

			double sum = 0;
			double total = 0;
			double totalCost = 0;
			double discount = CommonUtil.round(formBean.getDiscount().doubleValue());
			
			OfferMasterVO master = new OfferMasterVO();
			master.setId(masterId);
			master.setCustId(custId);
			master.setDeliveryUserId(formBean.getDeliveryUserId());
			master.setOfferDate(DateUtil.getDateObject(formBean.getOfferDate()));
			master.setInvoiceNbr(formBean.getInvoiceNbr());
			master.setDiscount(formBean.getDiscount());
			master.setMemo(formBean.getMemo());
			
			int detailCount = Integer.parseInt(request.getParameter("detailCount"));
			List<OfferDetailVO> details = new ArrayList<OfferDetailVO>();
			int i = 0;
			while(i < detailCount) {
				String prodId = request.getParameter("detail-" + i + "-prodId");
				if (prodId == null || "".equals(prodId)) {
					i++;
					continue;
				}
				
				String price = request.getParameter("detail-" + i + "-price");
				String cost = request.getParameter("detail-" + i + "-cost");
				String qty = request.getParameter("detail-" + i + "-qty");
				double q = Double.parseDouble(qty);
				if (q == 0) {
					i++;
					continue;
				}
				
				BigDecimal a = BigDecimal.valueOf(Double.parseDouble(price));
				BigDecimal b = BigDecimal.valueOf(Double.parseDouble(qty));
				BigDecimal c = BigDecimal.valueOf(Double.parseDouble(cost));
				BigDecimal x = a.multiply(b);
				BigDecimal y = c.multiply(b);
				double amt = CommonUtil.round(x.doubleValue());
				double amtCost = CommonUtil.round(y.doubleValue());
				
				OfferDetailVO detail = new OfferDetailVO();
				detail.setProdId(Integer.parseInt(prodId));
				detail.setQty(Double.parseDouble(qty));
				detail.setAmt(amt);
			
				sum += amt;
				totalCost += amtCost;

				details.add(detail);
				i++;
			}

			total = sum - discount;
			
			master.setAmt(sum);
			master.setTotal(total);
			master.setCost(totalCost);
			master.setReceiveAmt(new Double(0));
			
			service.modifyOffer(master, details);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("offer.msg.4", masterId));
			
			request.setAttribute("OFFER_ID", masterId);
			
			logger.info("modify end");
			saveMessages(request, messages);
			return new ActionForward("/offer.do?state=initQuery", false);

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("form");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("form");
		}
	}

	public ActionForward modifyReceiveAmt(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("modifyReceiveAmt start");

		try {
			OfferForm formBean = (OfferForm)form;
			OfferService service = (OfferService) this.getServiceFactory().getService("Offer");

			double receiveAmt = formBean.getReceiveAmt().doubleValue();
			
			OfferMasterVO master = new OfferMasterVO();
			master.setId(formBean.getMasterId());
			master.setReceiveAmt(receiveAmt);
			
			service.modifyOfferReceiveAmt(master);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("offer.msg.3", formBean.getMasterId()));
			
			logger.info("modifyReceiveAmt end");
			saveMessages(request, messages);
			return mapping.findForward("query");

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("query");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("query");
		}
	}
	
	public ActionForward initQueryUnReceived(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initQueryUnReceived start");
		ActionMessages messages = new ActionMessages();

		try {
			OfferForm formBean = (OfferForm) form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");

			formBean.setCusts(service.getCusts());
			formBean.setOffers(new ArrayList<OfferMasterVO>());
	
			logger.info("initQueryUnReceived end");
			saveMessages(request, messages);
			return mapping.findForward("unreceived");
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("unreceived");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("unreceived");
		}
	}
	
	public ActionForward queryUnReceived(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("queryUnReceived start");
		MessageResources mr = this.getResources(request);
		StringBuffer sb = new StringBuffer();

		boolean beforeFlag = "Y".equals(request.getParameter("beforeFlag"));
		boolean allCustFlag = "Y".equals(request.getParameter("allCustFlag"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		Integer custId = null;
		if (allCustFlag == false) {
			custId = new Integer(request.getParameter("custId"));
		}
		
		try {
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			List<OfferMasterVO> unreceivedOffers = service.getUnReceivedOffers(custId, DateUtil.getDateObject(startDate), DateUtil.getDateObject(endDate), beforeFlag);

			sb.append("errCde=00\n");
			sb.append("errMsg=\n");
	
			if (unreceivedOffers != null && !unreceivedOffers.isEmpty()) {
				sb.append("rowCount=" + unreceivedOffers.size() + "\n");
				for(int i = 0; i < unreceivedOffers.size(); i++) {
					OfferMasterVO offer = (OfferMasterVO)unreceivedOffers.get(i);
					CustProfVO cust = offer.getCust();
					String id = offer.getId();
					String before = "";
					if (id.startsWith("-")) {
						id = id.substring(1);
						before = "Y";
					}
					sb.append("offer-" + i + "-id=" + id + "\n");
					sb.append("offer-" + i + "-offerDate=" + DateUtil.getDateString(offer.getOfferDate()) + "\n");
					sb.append("offer-" + i + "-custNme=" + cust.getCustNme() + "\n");
					sb.append("offer-" + i + "-total=" + df.format(offer.getTotal()) + "\n");
					sb.append("offer-" + i + "-cost=" + df.format(offer.getCost()) + "\n");
					sb.append("offer-" + i + "-receivedAmt=" + df.format(offer.getReceiveAmt()) + "\n");
					sb.append("offer-" + i + "-before=" + before + "\n");
				}
			} else {
				sb.append("rowCount=0\n");
			}
			
		} catch (FamilyBizException e) {
			logger.error("", e);
			sb.append("errCde=01\n");
			sb.append("errMsg=" + e.getMessage() + "\n");
			this.sendAjaxResponse(response, sb.toString());
			return null;
		} catch (Exception e) {
			logger.error("", e);
			sb.append("errCde=99\n");
			sb.append("errMsg=" + mr.getMessage("all.msg.1") + "\n");//todo:
			this.sendAjaxResponse(response, sb.toString());
			return null;
		}
		logger.info("queryUnReceived end");
		this.sendAjaxResponse(response, sb.toString());
		return null;
	}
	
	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("delete start");
		ActionMessages messages = new ActionMessages();

		int result = 0;
		try {
			OfferForm formBean = (OfferForm) form;
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			
			result = service.removeOffer(formBean.getMasterId(), "Y".equals(request.getSession().getAttribute("OFFER_BACK")));
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("basic.msg.2", result));
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("delete end");
		saveMessages(request, messages);
		
		return new ActionForward("/offer.do?state=initQuery", false);
	}

}	
