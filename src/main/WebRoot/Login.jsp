<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>FamilyBiz Application</title>
</head>

<body>

<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%" style="margin-top: 1px;">
<tr>
<td width="100%" valign="middle" align="center">

<html:form action="/index" method="post" onsubmit="return login(this)">
<html:hidden property="state" value="login"/>
<table class="grid" cellspacing="0" cellpadding="5" border="1" width="400" align="center">
<thead><tr><th align="left" style="font-size:18">FamilyBiz Application</th></tr></thead>
<tbody>
<tr>
<td align="center">
<logic:present name="errorMessage">
<font color="red"><bean:write name="errorMessage"/></font><br>
</logic:present>
帳號: <html:text property="account" styleId="account" size="30" maxlength="10" /><br><br>
密碼: <html:password property="passwd" styleId="passwd" size="30" maxlength="10" /><br><br>
<input type="submit" value='<bean:message key="all.btn.10"/>' />
<input type="reset" value='<bean:message key="all.btn.8"/>' />
</td>
</tr>
</tbody>
</table>
</html:form>

</td>
</tr>
</table>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(document).ready(function(){
	$("#account").val("");
	$("#passwd").val("");
	$("#account").focus();
});

function login() {
	var account = $("#account").val();
	var passwd = $("#passwd").val();
	if (account == '') {
		alert('請輸入帳號');
		$("#account").focus();
		return false;
	}
	if (passwd == '') {
		alert('請輸入密碼');
		$("#passwd").focus();
		return false;
	}
	return true;
}

</script>
</body>
</html>