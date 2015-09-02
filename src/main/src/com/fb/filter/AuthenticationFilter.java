package com.fb.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fb.service.AuthenticationService;
import com.fb.service.ServiceFactory;
import com.fb.vo.UserProfVO;

public class AuthenticationFilter implements Filter {

	private static Logger logger = Logger.getLogger(AuthenticationFilter.class);
	private ServiceFactory serviceFactory;
	
	public void init(FilterConfig config) throws ServletException {
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		this.serviceFactory = (ServiceFactory) wac.getBean("serviceFactoryProxy");
	}

	public void doFilter(ServletRequest aRequest, ServletResponse aResponse,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) aRequest;
		HttpServletResponse response = (HttpServletResponse) aResponse;
		logger.info("Authentication start");

		String requestedPage = getRequestedPage(request);
		logger.info("requestPage=" + requestedPage);

		if (requestedPage.equals("") || (requestedPage.equals("index.do") && request.getParameter("state") == null)) {
			response.sendRedirect("/fb/Login.jsp");
			return;
		}
		
		if ("Login.jsp".equals(requestedPage) || "index.do".equals(requestedPage) || "logout.do".equals(requestedPage)) {
			chain.doFilter(aRequest, aResponse);
			return;
		}

		HttpSession session = request.getSession();
		UserProfVO userProfVO = (UserProfVO) session.getAttribute("USER_INFO");
		if (userProfVO != null) {
			if (requestedPage.equals("nav.do")) {
				logger.info("authenticated");
				chain.doFilter(aRequest, aResponse);
				return;
			}
			
			boolean auth = false;
			try {
				AuthenticationService service = (AuthenticationService) this.serviceFactory.getService("Authentication");
				auth = service.auth(userProfVO, requestedPage);
			} catch (Exception e) {
				logger.error("check auth fail", e);
				auth = false;
			}
			if (auth == false) {
				logger.info("not authenticated");
//				request.setAttribute("errorMessage", "您未授權執行此功能");
				response.sendRedirect("/fb/Login.jsp");
			} else {
				logger.info("authenticated");
				chain.doFilter(aRequest, aResponse);
				return;
			}
			
		} else {
			logger.info("not authenticated");
			response.sendRedirect("/fb/Login.jsp");
		}
	}

	private String getRequestedPage(HttpServletRequest request) {
		String url = request.getRequestURI();
		int firstSlash = url.indexOf("/", 1);
		String requestedPage = null;
		if (firstSlash != -1)
			requestedPage = url.substring(firstSlash + 1, url.length());
		return requestedPage;
	}

	public void destroy() {
	}
}
