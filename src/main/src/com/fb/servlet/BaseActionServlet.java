package com.fb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;

public class BaseActionServlet extends ActionServlet {
	
	private static final long serialVersionUID = 1936161014701326941L;

	protected static Logger logger = Logger.getLogger(BaseActionServlet.class);

	protected void process(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		response.setHeader("Cache-Control","must-revalidate,max-age=0"); 
		response.addHeader("Pragma","no-cache;");
		super.process(request, response);  
	}
	
	

}
