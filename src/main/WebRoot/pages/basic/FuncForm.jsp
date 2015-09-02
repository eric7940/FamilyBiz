<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<logic:equal name="action" value="createFunc"><bean:message key="all.func.func.1.1"/></logic:equal>
<logic:equal name="action" value="modifyFunc"><bean:message key="all.func.func.1.2"/></logic:equal>
</title>
</head>

<body>
<html:form action="func" method="post" onsubmit="return go(this)">
<logic:equal name="action" value="createFunc"><html:hidden property="state" value="createFunc"/></logic:equal>
<logic:equal name="action" value="modifyFunc"><html:hidden property="state" value="modifyFunc"/><html:hidden property="funcId"/></logic:equal>
<bean:define id="form" name="FuncForm"/>
<bean:define name="form" property="menus" id="menus" />
<bean:define name="form" property="linkTypes" id="linkTypes" />

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：
<logic:equal name="action" value="createFunc"><bean:message key="all.func.func.1.1"/></logic:equal>
<logic:equal name="action" value="modifyFunc"><bean:message key="all.func.func.1.2"/></logic:equal>
</H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
<logic:equal name="action" value="createFunc"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modifyFunc"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>
<table class="grid" cellspacing="0" cellpadding="1" border="1" width="800">
<tr>
<th width="100"><bean:message key="all.column.func.2"/></th><td><html:text property="funcLabel" size="20" /></td>
<th width="100"><bean:message key="all.column.func.3"/></th><td width="100"><html:select property="menuId" styleClass="select"><html:options collection="menus" property="menuId" labelProperty="menuLabel" /></html:select></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.func.5"/></th><td width="100"><html:select property="linkType" styleClass="select"><html:options collection="linkTypes" property="value" labelProperty="label" /></html:select></td>
<th width="100"><bean:message key="all.column.func.6"/></th><td width="100"><html:text property="order" size="10" styleClass="num"/></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.func.4"/></th><td colspan="3"><html:text property="url" size="60"/></td>
</tr>
</table>
<p>
<logic:equal name="action" value="createFunc"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modifyFunc"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

function go(fm) {
	if (fm.funcLabel.value.trim() == '') {
		alert('請輸入功能名稱');
		fm.funcLabel.focus();
		return false;
	}
	return true;
}
</script>
</body>
</html>