package com.fb.action.offer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.fb.action.BaseDispatchAction;
import com.fb.form.offer.PickForm;
import com.fb.service.OfferService;
import com.fb.util.DateUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.PickProdVO;

public class PickAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(PickAction.class);
	
	public ActionForward initQuery(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		PickForm formBean = (PickForm) form;
		formBean.setProducts(new ArrayList<PickProdVO>());
		return mapping.findForward("init");
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward query(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("query start");
		String offerDate = request.getParameter("offerDate");

		try {
			PickForm formBean = (PickForm)form;
			
			OfferService service = (OfferService) getServiceFactory().getService("Offer");
			List<PickProdVO> prods = service.getProdQty(DateUtil.getDateObject(offerDate));
			formBean.setProducts(prods);
			
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
	
}	
