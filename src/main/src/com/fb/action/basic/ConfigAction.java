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
import com.fb.form.basic.ConfigForm;
import com.fb.service.CommonService;
import com.fb.util.CommonUtil;
import com.fb.util.FamilyBizException;
import com.fb.vo.LabelValueBean;

public class ConfigAction extends BaseDispatchAction {

	private static Logger logger = Logger.getLogger(ConfigAction.class);

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
			ConfigForm formBean = (ConfigForm)form;
			CommonService service = (CommonService) this.getServiceFactory().getService("Common");
			
			List<LabelValueBean> units = service.getUnits();
			formBean.setUnits(units);
			
			String offerMemo = service.getOfferDefaultMemo();
			formBean.setOfferMemo(offerMemo);

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

	public ActionForward modifyUnits(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modifyUnits start");
		ActionMessages messages = new ActionMessages();

		try {
			ConfigForm formBean = (ConfigForm)form;
			CommonService service = (CommonService) this.getServiceFactory().getService("Common");
			
			String[] labels = request.getParameterValues("label");
			String[] idx = request.getParameterValues("unitDeleteIdx");

			String deleteIdx = "";
			if (idx != null)
				deleteIdx = "," + CommonUtil.convertListToString(idx, ",", false) + ",";

			List<String> units = new ArrayList<String>();
			for(int i = 0; i < labels.length; i++) {
				if (labels[i] == null || "".equals(labels[i])) continue;

				if (i < formBean.getUnits().size()) {
					//屬於原有的資料
					
					//刪除的不予理會
					if (deleteIdx.indexOf("," + String.valueOf(i) + ",") != -1) continue; 
					
					units.add(labels[i]);
				} else {
					//屬於新增的資料
					units.add(labels[i]);
				}
			}
			
			service.modifyUnits(units);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("config.msg.1"));
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("modifyUnits end");
		saveMessages(request, messages);
		
		return new ActionForward("/config.do?state=init", false);
	}

	public ActionForward modifyOfferMemo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("modifyOfferMemo start");
		ActionMessages messages = new ActionMessages();

		try {
			ConfigForm formBean = (ConfigForm)form;
			CommonService service = (CommonService) this.getServiceFactory().getService("Common");
			
			service.modifyOfferDefaultMemo(formBean.getOfferMemo());
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("config.msg.1"));
			
		} catch (FamilyBizException sce) {
			logger.error("",sce);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.0", sce.getMessage()));
			saveMessages(request, messages);
		} catch (Exception e) {
			logger.error("",e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("all.msg.1"));
			saveMessages(request, messages);
		}
		
		logger.info("modifyOfferMemo end");
		saveMessages(request, messages);
		
		return new ActionForward("/config.do?state=init", false);
	}
}	
