<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.auth.1"/></title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
</head>

<body>
<html:form action="auth" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<bean:define id="form" name="AuthForm"/>
<bean:define name="form" property="userClasses" id="userClasses" />

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.auth.1"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
角色：<html:select styleId="userClass" property="userClass" styleClass="select" onchange="pick(this.value)"><html:options collection="userClasses" property="value" labelProperty="label" /></html:select>
</div>

<table id="tb" class="grid" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.func.1"/></th>
<th width="170"><bean:message key="all.column.func.3"/></th>
<th width="170"><bean:message key="all.column.func.2"/></th>
<th>&nbsp;</th>
</tr>
</thead>

<tbody>
<logic:iterate id="func" name="form" property="funcs" indexId="idx">
<bean:define id="menu" name="func" property="menu"/>
<tr>
<td align="center"><bean:write name="func" property="funcId"/></td>
<td><bean:write name="menu" property="menuLabel"/></td>
<td><bean:write name="func" property="funcLabel"/></td>
<td><input type="checkbox" name="authFunc" id='authFunc-<bean:write name="func" property="funcId"/>' value='<bean:write name="func" property="funcId"/>'/>開放</td>
</tr>
</logic:iterate>
</tbody>
</table>

<div id="actionbar" align="left" style="width:100%;">
<input type="button" id="modifyBtn" value="<bean:message key="all.btn.3"/>" onclick="modify(this.form)"/>
</div>

 
</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(document).ready(function(){
	pick($("#userClass").val());
});

function pick(userClass) {
	$.ajax({
		url: '<html:rewrite page="/auth.do"/>',
		data: ({state: "getFuncAuth", userClass: userClass}),
		success: function(result) {
			resetAuth();
			var r = result.split('\n');
			for(var i = 0; i < r.length; i++) {
				if (r[i] == '') continue;
				var data = r[i].split('=');
				responseMap.put(data[0], data[1]);
			}
			if (responseMap.get("errCde") != '00') {
				alert(responseMap.get("errMsg"));
				$("#modifyBtn").attr("disabled", true);
			} else {
				var authFuncIds = responseMap.get("authFunc").split(',');
				for(var i = 0; i < authFuncIds.length; i++) {
					$("#authFunc-" + authFuncIds[i]).attr('checked', true);
				}
				$("#modifyBtn").attr("disabled", false);
			}
		}
	});
}

function resetAuth() {
	var authFuncs = document.getElementsByName("authFunc");
	for(var i = 0; i < authFuncs.length; i++) {
		authFuncs[i].checked = false;
	}
}

function modify(fm) {
	fm.state.value = 'modify';
	fm.submit();
}

</script>
</body>
</html>