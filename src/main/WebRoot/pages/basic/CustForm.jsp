<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<logic:equal name="action" value="create"><bean:message key="all.func.basic.1.1"/></logic:equal>
<logic:equal name="action" value="modify"><bean:message key="all.func.basic.1.2"/></logic:equal>
</title>
</head>

<body>
<html:form action="cust" method="post" onsubmit="return go(this)">
<logic:equal name="action" value="create"><html:hidden property="state" value="create"/></logic:equal>
<logic:equal name="action" value="modify"><html:hidden property="state" value="modify"/><html:hidden property="custId"/></logic:equal>
<bean:define id="form" name="CustForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：
<logic:equal name="action" value="create"><bean:message key="all.func.basic.1.1"/></logic:equal>
<logic:equal name="action" value="modify"><bean:message key="all.func.basic.1.2"/></logic:equal>
</H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
<logic:equal name="action" value="create"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modify"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>
<table class="grid" cellspacing="0" cellpadding="1" border="1" width="800">
<tr>
<th width="100"><bean:message key="all.column.cust.2"/></th><td><html:text property="custNme" size="20" onkeyup="convertNum(this)"/></td>
<th width="100"><bean:message key="all.column.cust.4"/></th><td width="100"><html:text property="tel" size="20" onkeyup="convertNum(this)"/></td>
<th width="100"><bean:message key="all.column.cust.3"/></th><td width="100"><html:text property="bizNo" size="10" onkeyup="convertNum(this)"/></td>
</tr>
<tr>
<th><bean:message key="all.column.cust.5"/></th><td colspan="5"><html:text property="deliverAddr" size="90" onkeyup="convertNum(this)"/></td>
</tr>
<tr>
<th><bean:message key="all.column.cust.6"/></th><td colspan="5"><html:text property="memo" size="90"/></td>
</tr>
</table>
<p>
<logic:equal name="action" value="create"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modify"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

function go(fm) {
	if (fm.custNme.value.trim() == '') {
		alert('請輸入客戶名稱');
		fm.custNme.focus();
		return false;
	}
	return true;
}
</script>
</body>
</html>