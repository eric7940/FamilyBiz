package com.fb.action.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


import com.fb.form.basic.NavForm;
import com.fb.service.AuthenticationService;
import com.fb.service.ServiceFactory;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;
import com.fb.vo.UserProfVO;

public class NavAction extends Action {

	private static Logger logger = Logger.getLogger(NavAction.class);
	private ServiceFactory serviceFactory = null;
	
	@Override
	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
		if (actionServlet != null) {
			ServletContext servletContext = actionServlet.getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			this.serviceFactory = (ServiceFactory) wac.getBean("serviceFactoryProxy");
		}
	}

	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("execute start");

		NavForm formBean = (NavForm)form;
		AuthenticationService service = (AuthenticationService) this.serviceFactory.getService("Authentication");
		
		UserProfVO userProfVO = (UserProfVO) request.getSession().getAttribute("USER_INFO");
		List<MenuFuncVO> menuFuncs = service.getMenuFunctions(userProfVO);
		
		Map<Integer,List<MenuFuncVO>> menuFuncMap = new HashMap<Integer,List<MenuFuncVO>>();
		List<List<MenuFuncVO>> orderMenuFuncList = new ArrayList<List<MenuFuncVO>>();
		
		for(MenuFuncVO func : menuFuncs) {
			MenuVO menu = func.getMenu();
			List<MenuFuncVO> funcList = menuFuncMap.get(menu.getMenuId());
			if (funcList == null) {
				funcList = new ArrayList<MenuFuncVO>(); 
				orderMenuFuncList.add(funcList);
			}
			funcList.add(func);
			menuFuncMap.put(menu.getMenuId(), funcList);
		}
		
		List<MenuVO> menus = new ArrayList<MenuVO>();
		Iterator<List<MenuFuncVO>> it = orderMenuFuncList.iterator();
		while(it.hasNext()) {
			List<MenuFuncVO> funcList = it.next();
			MenuVO menu = funcList.get(0).getMenu();
			menu.setFuncs(menuFuncMap.get(menu.getMenuId()));
			menus.add(menu);
		}
		
		formBean.setMenus(menus);

		logger.info("execute end");
		return mapping.findForward("init");
	}	

}	
