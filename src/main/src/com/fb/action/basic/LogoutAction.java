package com.fb.action.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LogoutAction extends Action {

	private static Logger logger = Logger.getLogger(LogoutAction.class);
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("execute start");

		request.getSession().removeAttribute("USER_INFO");

		logger.info("execute end");
		return new ActionForward("/Login.jsp");
	}	

}	
