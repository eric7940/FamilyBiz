package com.fb.action.stock;

import java.util.ArrayList;
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

import com.fb.action.BaseDispatchAction;
import com.fb.form.stock.StockForm;
import com.fb.service.StockService;
import com.fb.util.FamilyBizException;
import com.fb.vo.ProdStockQtyVO;

public class StockAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(StockAction.class);

	public ActionForward init(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("init start");

		ActionMessages messages;
		if(request.getAttribute(Globals.MESSAGE_KEY) != null){
			 messages = (ActionMessages)request.getAttribute(Globals.MESSAGE_KEY);
		}else{
			 messages = new ActionMessages();
		}

		try {
			StockForm formBean = (StockForm)form;
			StockService service = (StockService) this.getServiceFactory().getService("Stock");

			List<ProdStockQtyVO> prods = service.getProdsQty();
			formBean.setProds(prods);
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("init end");
		saveMessages(request, messages);
		return mapping.findForward("init");
	}	

	public ActionForward initAdjustQty(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initAdjustQty start");
		
		ActionMessages messages = new ActionMessages();

		try {
			StockForm formBean = (StockForm)form;
			
			if (formBean.getProds() == null) {
				StockService service = (StockService) this.getServiceFactory().getService("Stock");
	
				List<ProdStockQtyVO> prods = service.getProdsQty();
				formBean.setProds(prods);
			}
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}

		logger.info("initAdjustQty end");
		return mapping.findForward("initAdjust");
	}	

	public ActionForward adjustQty(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ActionMessages messages = new ActionMessages();
		logger.info("adjustQty start");

		try {
			StockService service = (StockService) this.getServiceFactory().getService("Stock");
			
			int detailCount = Integer.parseInt(request.getParameter("detailCount"));
			List<ProdStockQtyVO> details = new ArrayList<ProdStockQtyVO>();
			int i = 0;
			while(i < detailCount) {
				String prodId = request.getParameter("adjust-" + i + "-prodId");
				if (prodId == null || "".equals(prodId)) {
					i++;
					continue;
				}
				
				String adjust = request.getParameter("adjust-" + i + "-num");
				
				ProdStockQtyVO detail = new ProdStockQtyVO();
				detail.setProdId(Integer.parseInt(prodId));
				detail.setQty(Double.parseDouble(adjust));
				details.add(detail);
				i++;
			}
			
			service.adjustQty(details);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("stock.msg.1"));
			
			logger.info("adjustQty end");
			saveMessages(request, messages);
			return new ActionForward("/stock.do?state=init", false);

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
			return mapping.findForward("initAdjust");
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
			return mapping.findForward("initAdjust");
		}
	}
}	
