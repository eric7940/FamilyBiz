package com.fb.action.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fb.action.BaseDispatchAction;
import com.fb.form.basic.IndexForm;
import com.fb.service.AuthenticationService;
import com.fb.util.FamilyBizException;
import com.fb.vo.UserProfVO;

public class IndexAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(IndexAction.class);
	
	public ActionForward login(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("login start");

		IndexForm formBean = (IndexForm)form;
		String userId = formBean.getAccount();
		String userPwd = formBean.getPasswd();
		
		if (userId == null || userPwd == null) {
			return mapping.findForward("login");
		}
		
		AuthenticationService service = (AuthenticationService) this.getServiceFactory().getService("Authentication");

		UserProfVO userProfVO = null;
		try {
			userProfVO = service.login(userId.toLowerCase(), userPwd);
		} catch (FamilyBizException e) {
			logger.warn(userId + " " + e.getMessage());
			request.setAttribute("errorMessage", e.getMessage());
			return mapping.findForward("login");
		}
		
		if (userProfVO != null) {
			request.getSession().setAttribute("USER_INFO", userProfVO);
		}
		
		logger.info("login end");
		return mapping.findForward("index");
	}	

	public ActionForward logout(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("logout start");
		request.getSession().removeAttribute("USER_INFO");
		request.getSession().invalidate();
		logger.info("logout end");
		return mapping.findForward("login");
	}	
}	
