package com.fb.action.purchase;

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
import com.fb.form.purchase.PurchaseForm;
import com.fb.service.FactoryService;
import com.fb.service.ProductService;
import com.fb.service.PurchaseService;
import com.fb.util.CommonUtil;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.FactProfVO;
import com.fb.vo.ProdProfVO;
import com.fb.vo.PurchaseDetailVO;
import com.fb.vo.PurchaseMasterVO;

public class PurchaseAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(PurchaseAction.class);
	private static DecimalFormat df = new DecimalFormat("#0.00");
	
	public ActionForward initQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initQuery start");
		PurchaseForm formBean = (PurchaseForm) form;

		ActionMessages messages;
		if(request.getAttribute(Globals.MESSAGE_KEY) != null){
			messages = (ActionMessages)request.getAttribute(Globals.MESSAGE_KEY);
		}else{
			messages = new ActionMessages();
			request.getSession().setAttribute("PURCHASE_BACK", "N");
			if ("Y".equals(request.getParameter("back")))
				request.getSession().setAttribute("PURCHASE_BACK", "Y");
		}

		FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");

		formBean.setFacts(service.getFacts());

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

		String factId = request.getParameter("factId");
		String purchaseId = request.getParameter("purchaseId");
		
		try {
			PurchaseService service = (PurchaseService) getServiceFactory().getService("Purchase");
			
			PurchaseMasterVO purchase = null;
			if (purchaseId == null) {
				boolean back = "Y".equals(request.getSession().getAttribute("PURCHASE_BACK"));
				List<PurchaseMasterVO> purchases = service.getPurchases(Integer.parseInt(factId), back);
				if (purchases != null && !purchases.isEmpty()) {
					if (purchases.size() > 1) {
						sb.append("errCde=00\n");
						sb.append("errMsg=\n");
						sb.append("rowCount=" + purchases.size() + "\n");
						sb.append("factId=" + factId + "\n");
						this.sendAjaxResponse(response, sb.toString());
						return null;
					} else {
						purchase = (PurchaseMasterVO)purchases.get(0);	
					}
				}
			} else {
				purchase = service.getPurchase(Integer.parseInt(purchaseId));
			}
			
			if (purchase == null) {
				sb.append("errCde=99\n");
				sb.append("errMsg=" + mr.getMessage("purchase.msg.1") + "\n");
				this.sendAjaxResponse(response, sb.toString());
				return null;
			}
			
			FactProfVO fact = purchase.getFact();
			
			sb.append("errCde=00\n");
			sb.append("errMsg=\n");
			sb.append("rowCount=1\n");
			
			sb.append("masterId=" + purchase.getId() + "\n");
			sb.append("purchaseDate=" + DateUtil.getDateString(purchase.getPurchaseDate()) + "\n");
			sb.append("invoiceNbr=" + purchase.getInvoiceNbr() + "\n");
			sb.append("amt=" + df.format(purchase.getAmt()) + "\n");
			sb.append("discount=" + df.format(purchase.getDiscount()) + "\n");
			sb.append("total=" + df.format(purchase.getTotal()) + "\n");
			sb.append("memo=" + purchase.getMemo() + "\n");
			
			sb.append("factId=" + fact.getFactId() + "\n");
			sb.append("factNme=" + fact.getFactNme() + "\n");
			sb.append("bizNo=" + fact.getBizNo() + "\n");
			sb.append("contact=" + fact.getContact() + "\n");
			sb.append("tel=" + fact.getTel() + "\n");
			sb.append("addr=" + fact.getAddr() + "\n");
			sb.append("factMemo=" + fact.getMemo() + "\n");
			
			List<PurchaseDetailVO> details = purchase.getDetails();
			if (details != null && !details.isEmpty()) {
				sb.append("detailCount=" + details.size() + "\n");
				for(int i = 0; i < details.size(); i++) {
					PurchaseDetailVO detail = details.get(i);
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
	
	public ActionForward getPurchaseList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("getPurchaseList start");

		try {
			PurchaseForm formBean = (PurchaseForm)form;
			PurchaseService service = (PurchaseService) getServiceFactory().getService("Purchase");
			
			String factId = request.getParameter("feeid");

			boolean back = "Y".equals(request.getSession().getAttribute("PURCHASE_BACK"));
			List<PurchaseMasterVO> purchases = service.getPurchases(Integer.parseInt(factId), back);
			formBean.setPurchases(purchases);

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
		logger.info("getPurchaseList end");
		saveMessages(request, messages);
		
		return mapping.findForward("purchaselist");
	}
	
	public ActionForward initCreate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("initCreate start");

		try {
			PurchaseForm formBean = (PurchaseForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");
			
			formBean.setFacts(service.getFacts());

			formBean.setDiscount(new Double(0));
			formBean.setInvoiceNbr("");
			
			request.getSession().setAttribute("PURCHASE_BACK", "N");
			if ("Y".equals(request.getParameter("back")))
				request.getSession().setAttribute("PURCHASE_BACK", "Y");

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

	public ActionForward getFactList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("getFactList start");
		logger.info("getFactList end");
		return mapping.findForward("factlist");
	}
	
	public ActionForward getProds(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("getProds start");
		StringBuffer sb = new StringBuffer();

		try {
			String factId = request.getParameter("factId");
			//String prodNme = request.getParameter("q");
			String prodNme = new String(request.getParameter("q").getBytes("ISO8859_1"), "UTF-8");

			if (prodNme != null && ("".equals(prodNme.trim()) || "　".equals(prodNme.trim()))) prodNme = null;

			logger.info("param: factId=" + factId);
			logger.info("param: prodNme=" + prodNme);
			
			ProductService service1 = (ProductService) this.getServiceFactory().getService("Product");

			List<ProdProfVO> prods = service1.getProds(prodNme);
			if (prods != null && prods.size() > 0) {
				Iterator<ProdProfVO> it = prods.iterator();
				while(it.hasNext()) {
					ProdProfVO prod = it.next();
					sb.append(prod.getProdId() + "|" + prod.getProdNme() + "|" + df.format(prod.getCost()) + "|" + prod.getUnit() + "\n");
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
			PurchaseForm formBean = (PurchaseForm)form;
			PurchaseService service = (PurchaseService) this.getServiceFactory().getService("Purchase");
			
			logger.info("factId:" + formBean.getFactId());

			double sum = 0;
			double total = 0;
			double discount = formBean.getDiscount();
			
			PurchaseMasterVO master = new PurchaseMasterVO();
			master.setPurchaseDate(DateUtil.getDateObject(formBean.getPurchaseDate()));
			master.setFactId(formBean.getFactId());
			master.setInvoiceNbr(formBean.getInvoiceNbr());
			master.setDiscount(formBean.getDiscount());
			master.setMemo(formBean.getMemo());
			master.setBack("Y".equals(request.getSession().getAttribute("PURCHASE_BACK"))? "Y": null);
			
			int detailCount = Integer.parseInt(request.getParameter("detailCount"));
			List<PurchaseDetailVO> details = new ArrayList<PurchaseDetailVO>();
			int i = 0;
			while(i < detailCount) {
				String prodId = request.getParameter("detail-" + i + "-prodId");
				if (prodId == null || "".equals(prodId)) {
					i++;
					continue;
				}
				
				String price = request.getParameter("detail-" + i + "-price");
				String qty = request.getParameter("detail-" + i + "-qty");
				BigDecimal a = BigDecimal.valueOf(Double.parseDouble(price));
				BigDecimal b = BigDecimal.valueOf(Double.parseDouble(qty));
				BigDecimal c = a.multiply(b);
				double amt = CommonUtil.round(c.doubleValue());
				
				PurchaseDetailVO detail = new PurchaseDetailVO();
				detail.setProdId(Integer.parseInt(prodId));
				detail.setQty(Double.parseDouble(qty));
				detail.setAmt(amt);
			
				sum += amt;
				
				details.add(detail);
				i++;
			}

			total = sum - discount;
			
			master.setAmt(sum);
			master.setTotal(total);

			String masterId = service.addPurchase(master, details, "Y".equals(request.getSession().getAttribute("PURCHASE_BACK")));
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("purchase.msg.2", masterId));
			
			request.setAttribute("PURCHASE_ID", masterId);
			
			logger.info("create end");
			saveMessages(request, messages);
			return new ActionForward("/purchase.do?state=initQuery", false);

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
	
	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("delete start");
		ActionMessages messages = new ActionMessages();

		int result = 0;
		try {
			PurchaseForm formBean = (PurchaseForm) form;
			PurchaseService service = (PurchaseService) getServiceFactory().getService("Purchase");
			
			result = service.removePurchase(formBean.getMasterId(), "Y".equals(request.getSession().getAttribute("PURCHASE_BACK")));
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
		
		return new ActionForward("/purchase.do?state=initQuery", false);
	}

}	
