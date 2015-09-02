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
import com.fb.form.basic.FuncForm;
import com.fb.service.FuncService;
import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;
import com.fb.vo.MenuFuncVO;
import com.fb.vo.MenuVO;

public class FuncAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(FuncAction.class);

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
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			List<MenuVO> menus = service.getMenus();
			formBean.setMenus(menus);
			
			List<MenuFuncVO> funcs = service.getFuncs();
			formBean.setFuncs(funcs);
			
			List<LabelValueBean> linkTypes = new ArrayList<LabelValueBean>();
			LabelValueBean linkTypeL = new LabelValueBean();
			linkTypeL.setLabel("連結");
			linkTypeL.setValue("L");
			linkTypes.add(linkTypeL);
			LabelValueBean linkTypeO = new LabelValueBean();
			linkTypeO.setLabel("另開");
			linkTypeO.setValue("O");
			linkTypes.add(linkTypeO);
			formBean.setLinkTypes(linkTypes);

			List<LabelValueBean> folderFlags = new ArrayList<LabelValueBean>();
			LabelValueBean folderFlagO = new LabelValueBean();
			folderFlagO.setLabel("展開");
			folderFlagO.setValue("O");
			folderFlags.add(folderFlagO);
			LabelValueBean folderFlagC = new LabelValueBean();
			folderFlagC.setLabel("收回");
			folderFlagC.setValue("C");
			folderFlags.add(folderFlagC);
			formBean.setFolderFlags(folderFlags);

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

	public ActionForward initCreateFunc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initCreateFunc start");
		FuncForm formBean = (FuncForm)form;
		formBean.setFuncLabel("");
		formBean.setUrl("");
		formBean.setLinkType("L");
		formBean.setOrder(0);
		
		request.setAttribute("action", "createFunc");
		
		logger.info("initCreateFunc end");
		return mapping.findForward("funcForm");
	}	

	public ActionForward createFunc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("createFunc start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			MenuFuncVO func = new MenuFuncVO();
			func.setFuncLabel(formBean.getFuncLabel());
			func.setMenuId(formBean.getMenuId());
			func.setUrl(formBean.getUrl());
			func.setLinkType(formBean.getLinkType());
			func.setOrder(formBean.getOrder());
			
			service.addFunc(func);
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
		
		logger.info("createFunc end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}

	public ActionForward initModifyFunc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initModifyFunc start");
		ActionMessages messages = new ActionMessages();
		
		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");

			MenuFuncVO func = service.getFunc(formBean.getFuncId());
			
			formBean.setFuncLabel(func.getFuncLabel());
			formBean.setLinkType(func.getLinkType());
			formBean.setMenuId(func.getMenu().getMenuId());
			formBean.setOrder(func.getOrder());
			formBean.setUrl(func.getUrl());
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}

		request.setAttribute("action", "modifyFunc");

		logger.info("initModifyFunc end");
		return mapping.findForward("funcForm");
	}
	
	public ActionForward modifyFunc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modifyFunc start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			MenuFuncVO func = new MenuFuncVO();
			func.setFuncId(formBean.getFuncId());
			func.setFuncLabel(formBean.getFuncLabel());
			func.setMenuId(formBean.getMenuId());
			func.setUrl(formBean.getUrl());
			func.setLinkType(formBean.getLinkType());
			func.setOrder(formBean.getOrder());
			
			service.modifyFunc(func);
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
		
		logger.info("modifyFunc end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}

	public ActionForward deleteFunc(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("deleteFunc start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			int result = service.removeFunc(formBean.getFuncId());
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
		
		logger.info("deleteFunc end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}
	
	public ActionForward initCreateMenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initCreateMenu start");
		FuncForm formBean = (FuncForm)form;
		formBean.setMenuLabel("");
		formBean.setFolderFlag("O");
		formBean.setOrder(0);
		
		request.setAttribute("action", "createMenu");
		
		logger.info("initCreateMenu end");
		return mapping.findForward("menuForm");
	}	

	public ActionForward createMenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("createMenu start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			MenuVO menu = new MenuVO();
			menu.setMenuLabel(formBean.getMenuLabel());
			menu.setFolderFlag(formBean.getFolderFlag());
			menu.setOrder(formBean.getOrder());
			
			service.addMenu(menu);
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
		
		logger.info("createMenu end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}

	public ActionForward initModifyMenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
		logger.info("initModifyMenu start");
		ActionMessages messages = new ActionMessages();
		
		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");

			MenuVO menu = service.getMenu(formBean.getMenuId());
			
			formBean.setMenuLabel(menu.getMenuLabel());
			formBean.setFolderFlag(menu.getFolderFlag());
			formBean.setOrder(menu.getOrder());
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}

		request.setAttribute("action", "modifyMenu");

		logger.info("initModifyMenu end");
		return mapping.findForward("menuForm");
	}
	
	public ActionForward modifyMenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modifyMenu start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			MenuVO menu = new MenuVO();
			menu.setMenuId(formBean.getMenuId());
			menu.setMenuLabel(formBean.getMenuLabel());
			menu.setFolderFlag(formBean.getFolderFlag());
			menu.setOrder(formBean.getOrder());
			
			service.modifyMenu(menu);
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
		
		logger.info("modifyMenu end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}

	public ActionForward deleteMenu(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("deleteMenu start");
		ActionMessages messages = new ActionMessages();

		try {
			FuncForm formBean = (FuncForm)form;
			FuncService service = (FuncService) this.getServiceFactory().getService("Func");
			
			int result = service.removeMenu(formBean.getMenuId());
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
		
		logger.info("deleteMenu end");
		saveMessages(request, messages);
		
		return new ActionForward("/func.do?state=init", false);
	}
}	
