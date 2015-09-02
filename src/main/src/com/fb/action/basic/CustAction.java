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
import com.fb.form.basic.CustForm;
import com.fb.service.CustomerService;
import com.fb.util.FamilyBizException;
import com.fb.vo.CustProfVO;

public class CustAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(CustAction.class);

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
			CustForm formBean = (CustForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");
			List<CustProfVO> custs = service.getCusts();
			
			formBean.setCusts(custs);
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
		CustForm formBean = (CustForm)form;
		formBean.setCustNme("");
		formBean.setBizNo("");
		formBean.setDeliverAddr("");
		formBean.setTel("");
		formBean.setMemo("");
		
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
			CustForm formBean = (CustForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");
			
			CustProfVO cust = new CustProfVO();
			cust.setCustNme(formBean.getCustNme());
			cust.setBizNo(formBean.getBizNo());
			cust.setDeliverAddr(formBean.getDeliverAddr());
			cust.setTel(formBean.getTel());
			cust.setMemo(formBean.getMemo());
			
			service.addCust(cust);
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
		
		return new ActionForward("/cust.do?state=init", false);
	}

	public ActionForward initModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initModify start");
		ActionMessages messages = new ActionMessages();
		
		try {
			CustForm formBean = (CustForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");

			CustProfVO cust = service.getCust(formBean.getCustId());
			
			formBean.setCustNme(cust.getCustNme());
			formBean.setBizNo(cust.getBizNo());
			formBean.setDeliverAddr(cust.getDeliverAddr());
			formBean.setTel(cust.getTel());
			formBean.setMemo(cust.getMemo());
			
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
			CustForm formBean = (CustForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");
			
			CustProfVO cust = new CustProfVO();
			cust.setCustId(formBean.getCustId());
			cust.setCustNme(formBean.getCustNme());
			cust.setBizNo(formBean.getBizNo());
			cust.setDeliverAddr(formBean.getDeliverAddr());
			cust.setTel(formBean.getTel());
			cust.setMemo(formBean.getMemo());
			
			service.modifyCust(cust);
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
		
		return new ActionForward("/cust.do?state=init", false);
	}

	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("delete start");
		ActionMessages messages = new ActionMessages();

		try {
			CustForm formBean = (CustForm)form;
			CustomerService service = (CustomerService) this.getServiceFactory().getService("Customer");
			
			int result = 0;
			List<CustProfVO> custs = formBean.getCusts();
			String[] selectIdx = request.getParameterValues("selectIdx");
			if (selectIdx.length > 1) {
				List<Integer> custIds = new ArrayList<Integer>();
				for(int i = 0; i < selectIdx.length; i++) {
					int idx = Integer.parseInt(selectIdx[i]);
					CustProfVO cust = custs.get(idx);
					custIds.add(cust.getCustId());
				}
				result = service.removeCusts(custIds);
			} else {
				int idx = Integer.parseInt(selectIdx[0]);
				CustProfVO cust = (CustProfVO)custs.get(idx);
				result = service.removeCust(cust.getCustId());
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
		
		return new ActionForward("/cust.do?state=init", false);
	}
}	
