package com.fb.action.basic;

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
import com.fb.form.basic.ProdForm;
import com.fb.service.CommonService;
import com.fb.service.ProductService;
import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;
import com.fb.vo.ProdProfVO;

public class ProdAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(ProdAction.class);

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
			ProdForm formBean = (ProdForm)form;
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");

			List<ProdProfVO> prods = service.getProds();
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

	public ActionForward initCreate(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initCreate start");
		ProdForm formBean = (ProdForm)form;
		CommonService service = (CommonService) this.getServiceFactory().getService("Common");

		formBean.setProdNme("");
		formBean.setUnit("");
		formBean.setPrice(new Double(0));
		formBean.setCost(new Double(0));
		formBean.setSaveQty(new Integer(0));

		List<LabelValueBean> units = service.getUnits();
		formBean.setUnits(units);

		request.setAttribute("action", "create");
		
		logger.info("initCreate end");
		return mapping.findForward("form");
	}	

	public ActionForward create(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("create start");
		ActionMessages messages = new ActionMessages();

		try {
			ProdForm formBean = (ProdForm)form;
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");
			
			ProdProfVO prod = new ProdProfVO();
			prod.setProdNme(formBean.getProdNme());
			prod.setUnit(formBean.getUnit());
			prod.setPrice(formBean.getPrice());
			prod.setCost(new Double(0));
			prod.setSaveQty(formBean.getSaveQty());

			service.addProd(prod);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("basic.msg.1"));
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("create end");
		saveMessages(request, messages);
		
		return new ActionForward("/prod.do?state=init", false);
	}

	public ActionForward initModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initModify start");
		ActionMessages messages = new ActionMessages();
		
		try {
			ProdForm formBean = (ProdForm)form;
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");
			CommonService service2 = (CommonService) this.getServiceFactory().getService("Common");

			ProdProfVO prod = service.getProd(formBean.getProdId());
			
			formBean.setProdNme(prod.getProdNme());
			formBean.setUnit(prod.getUnit());
			formBean.setPrice(prod.getPrice());
			formBean.setCost(prod.getCost());
			formBean.setSaveQty(prod.getSaveQty());
			
			List<LabelValueBean> units = service2.getUnits();
			formBean.setUnits(units);

		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}

		request.setAttribute("action", "modify");

		logger.info("initModify end");
		return mapping.findForward("form");
	}
	
	public ActionForward modify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modify start");
		ActionMessages messages = new ActionMessages();

		try {
			ProdForm formBean = (ProdForm)form;
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");
			
			ProdProfVO prod = new ProdProfVO();
			prod.setProdId(formBean.getProdId());
			prod.setProdNme(formBean.getProdNme());
			prod.setUnit(formBean.getUnit());
			prod.setPrice(formBean.getPrice());
			prod.setSaveQty(formBean.getSaveQty());
			
			service.modifyProd(prod);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("basic.msg.3"));
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("modify end");
		saveMessages(request, messages);
		
		return new ActionForward("/prod.do?state=init", false);
	}

	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("delete start");
		ActionMessages messages = new ActionMessages();

		try {
			ProdForm formBean = (ProdForm)form;
			ProductService service = (ProductService) this.getServiceFactory().getService("Product");
			
			int result = 0;
			List<ProdProfVO> prods = formBean.getProds();
			String[] selectIdx = request.getParameterValues("selectIdx");
			if (selectIdx.length > 1) {
				List<Integer> prodIds = new ArrayList<Integer>();
				for(int i = 0; i < selectIdx.length; i++) {
					int idx = Integer.parseInt(selectIdx[i]);
					ProdProfVO prod = prods.get(idx);
					prodIds.add(prod.getProdId());
				}
				result = service.removeProds(prodIds);
			} else {
				int idx = Integer.parseInt(selectIdx[0]);
				ProdProfVO prod = (ProdProfVO)prods.get(idx);
				result = service.removeProd(prod.getProdId());
			}
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
		
		return new ActionForward("/prod.do?state=init", false);
	}
}	
