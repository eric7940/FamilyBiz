/*
 * Copyright (c) 2005-2008 Taiwan Mobile Co., Ltd.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Taiwan
 * Mobile Co., Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of license agreement you entered into with Taiwan Mobile Co., Ltd.
 * Date        version  author		description										MR No.		Code Reviewer
 * ===============================================================================================================
 * 2007/05/10			Wayne		修改api回傳訊息裡有包括單引號(')都替換成雙引號(")	 	  		Phoebe
 */
package com.fb.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.DispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fb.service.ServiceFactory;

public abstract class BaseDispatchAction extends DispatchAction {

	protected static Logger logger = Logger.getLogger(BaseDispatchAction.class);

	private ServiceFactory serviceFactory;

	protected ServiceFactory getServiceFactory() {
		return this.serviceFactory;
	}

	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
		if (actionServlet != null) {
			ServletContext servletContext = actionServlet.getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			this.serviceFactory = (ServiceFactory) wac.getBean("serviceFactoryProxy");
		}
	}

	protected ActionForward sendAjaxXMLResponse(HttpServletResponse response, String str) {
        return sendAjaxResponse(response, "<?xml version=\"1.0\" ?>" + str, "text/xml;charset=utf-8");
	}

	protected ActionForward sendAjaxResponse(HttpServletResponse response, String str) {
        return sendAjaxResponse(response, str, "text/html;charset=utf-8");
	}

	protected ActionForward sendAjaxResponse(HttpServletResponse response, String str, String contentType) {
        try {
        	response.addHeader("Cache-Control","no-cache; no-store; must-revalidate;");
        	response.addHeader("Pragma","no-cache;");
    		response.setContentType(contentType);
			response.getWriter().write(str);
			logger.info("◆◆◆◆send Response:\n" + str);
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
}
