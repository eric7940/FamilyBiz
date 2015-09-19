package com.fb.action.offer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.springframework.util.StringUtils;

import com.fb.action.BaseDispatchAction;
import com.fb.form.offer.PickForm;
import com.fb.service.OfferService;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProfVO;
import com.fb.vo.PickProdVO;

public class PickAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(PickAction.class);
	
	public ActionForward initQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		PickForm formBean = (PickForm) form;
		formBean.setOfferDate(DateUtil.getDateString(new Date()));
		formBean.setProducts(new ArrayList<PickProdVO>());
		request.setAttribute("qry_prod_flag", "N");
		return mapping.findForward("init");
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward query(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("query start");
		
		try {
			PickForm formBean = (PickForm)form;
			String offerDate = formBean.getOfferDate();
			String[] custs = request.getParameterValues("custs");
			formBean.setCustIds(StringUtils.collectionToCommaDelimitedString(Arrays.asList(custs)));
			if (custs.length > 0) {
				OfferService service = (OfferService) getServiceFactory().getService("Offer");
				List<PickProdVO> prods = service.getProdQty(DateUtil.getDateObject(offerDate), Arrays.asList(custs));
				formBean.setProducts(prods);
			} else {
				formBean.setProducts(new ArrayList<PickProdVO>());
			}
			
			request.setAttribute("qry_prod_flag", "Y");

			logger.info("query end");
			return mapping.findForward("init");
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("init");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("init");
		}
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward qryCusts(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("qryCusts start");
		StringBuffer sb = new StringBuffer();
		try {
			String offerDate = request.getParameter("date");
	
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			List<CustProfVO> custs = service.getCustByOfferDate(DateUtil.getDateObject(offerDate));
			
			if (custs != null && custs.size() > 0) {
				Iterator<CustProfVO> it = custs.iterator();
				while(it.hasNext()) {
					CustProfVO cust = it.next();
					sb.append(cust.getCustId() + "|" + cust.getCustNme() + "\n");
				}
				request.setAttribute("qry_cust_flag", "Y");
			}
		} catch (FamilyBizException sce) {
			logger.error("", sce);
			sb.append(sce.getMessage());
		} catch (Exception e) {
			logger.error("", e);
			MessageResources mr = this.getResources(request);
			sb.append(mr.getMessage("all.msg.1"));
		}
		
		logger.info("qryCusts end");
		return this.sendAjaxResponse(response, sb.toString());
	}

}	
