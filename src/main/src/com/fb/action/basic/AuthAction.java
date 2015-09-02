package com.fb.action.basic;

import java.util.Arrays;
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
import com.fb.form.basic.AuthForm;
import com.fb.service.AccountService;
import com.fb.service.FuncService;
import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;
import com.fb.vo.MenuFuncVO;

public class AuthAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(AuthAction.class);

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
			AuthForm formBean = (AuthForm)form;
			AccountService service1 = (AccountService) this.getServiceFactory().getService("Account");
			FuncService service2 = (FuncService) this.getServiceFactory().getService("Func");
			
			List<LabelValueBean> userClasses = service1.getUserClasses();
			formBean.setUserClasses(userClasses);
			
			List<MenuFuncVO> funcs = service2.getFuncs();
			formBean.setFuncs(funcs);
			
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

	public ActionForward getFuncAuth(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("getFuncAuth start");
		MessageResources mr = this.getResources(request);
		StringBuffer sb = new StringBuffer();

		String userClass = request.getParameter("userClass");
		
		try {
			AccountService service = (AccountService) this.getServiceFactory().getService("Account");
			List<MenuFuncVO> funcs = service.getAuthFunctions(userClass);
			
			String auth = "";
			if (funcs != null && !funcs.isEmpty()) {
				StringBuffer authString = new StringBuffer();
				for(int i = 0; i < funcs.size(); i++) {
					MenuFuncVO func = funcs.get(i);
					authString.append(func.getFuncId() + ",");
				}
				auth = authString.substring(0, authString.length() - 1);
			}
			
			sb.append("errCde=00\n");
			sb.append("errMsg=\n");
			sb.append("authFunc=" + auth + "\n");
			
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

	public ActionForward modify(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modify start");
		ActionMessages messages = new ActionMessages();

		try {
			AuthForm formBean = (AuthForm)form;
			AccountService service = (AccountService) this.getServiceFactory().getService("Account");
			
			String[] authFuncs = request.getParameterValues("authFunc");
			service.modifyAuthFunctions(formBean.getUserClass(), Arrays.asList(authFuncs));
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
		
		return new ActionForward("/auth.do?state=init", false);
	}

}	
