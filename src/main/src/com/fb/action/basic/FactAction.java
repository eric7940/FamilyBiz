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
import com.fb.form.basic.FactForm;
import com.fb.service.FactoryService;
import com.fb.util.FamilyBizException;
import com.fb.vo.FactProfVO;

public class FactAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(FactAction.class);

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
			FactForm formBean = (FactForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");
			List<FactProfVO> facts = service.getFacts();
			
			formBean.setFacts(facts);
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
		FactForm formBean = (FactForm)form;
		formBean.setFactNme("");
		formBean.setBizNo("");
		formBean.setAddr("");
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
			FactForm formBean = (FactForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");
			
			FactProfVO fact = new FactProfVO();
			fact.setFactNme(formBean.getFactNme());
			fact.setBizNo(formBean.getBizNo());
			fact.setContact(formBean.getContact());
			fact.setAddr(formBean.getAddr());
			fact.setTel(formBean.getTel());
			fact.setMemo(formBean.getMemo());
			
			service.addFact(fact);
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
		
		return new ActionForward("/fact.do?state=init", false);
	}

	public ActionForward initModify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initModify start");
		ActionMessages messages = new ActionMessages();
		
		try {
			FactForm formBean = (FactForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");

			FactProfVO fact = service.getFact(formBean.getFactId());
			
			formBean.setFactNme(fact.getFactNme());
			formBean.setBizNo(fact.getBizNo());
			formBean.setContact(fact.getContact());
			formBean.setAddr(fact.getAddr());
			formBean.setTel(fact.getTel());
			formBean.setMemo(fact.getMemo());
			
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
			FactForm formBean = (FactForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");
			
			FactProfVO fact = new FactProfVO();
			fact.setFactId(formBean.getFactId());
			fact.setFactNme(formBean.getFactNme());
			fact.setBizNo(formBean.getBizNo());
			fact.setContact(formBean.getContact());
			fact.setAddr(formBean.getAddr());
			fact.setTel(formBean.getTel());
			fact.setMemo(formBean.getMemo());
			
			service.modifyFact(fact);
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
		
		return new ActionForward("/fact.do?state=init", false);
	}

	public ActionForward delete(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("delete start");
		ActionMessages messages = new ActionMessages();

		try {
			FactForm formBean = (FactForm)form;
			FactoryService service = (FactoryService) this.getServiceFactory().getService("Factory");
			
			int result = 0;
			List<FactProfVO> facts = formBean.getFacts();
			String[] selectIdx = request.getParameterValues("selectIdx");
			if (selectIdx.length > 1) {
				List<Integer> factIds = new ArrayList<Integer>();
				for(int i = 0; i < selectIdx.length; i++) {
					int idx = Integer.parseInt(selectIdx[i]);
					FactProfVO fact = facts.get(idx);
					factIds.add(fact.getFactId());
				}
				result = service.removeFacts(factIds);
			} else {
				int idx = Integer.parseInt(selectIdx[0]);
				FactProfVO fact = (FactProfVO)facts.get(idx);
				result = service.removeFact(fact.getFactId());
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
		
		return new ActionForward("/fact.do?state=init", false);
	}
}	
