<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<bean:define id="form" name="FuncForm"/>
<title><bean:message key="all.func.func.1"/></title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
<script type="text/javascript">
var linkTypes = [
<logic:iterate name="form" property="linkTypes" id="linkType">
	{ 
	  value: '<bean:write name="linkType" property="value"/>',
	  label: '<bean:write name="linkType" property="label"/>'
	},
</logic:iterate>
];

var folderFlags = [
<logic:iterate name="form" property="folderFlags" id="folderFlag">
	{ 
	  value: '<bean:write name="folderFlag" property="value"/>',
	  label: '<bean:write name="folderFlag" property="label"/>'
	},
</logic:iterate>
];

function displayLinkType(value) {
	for (var i = 0; i < linkTypes.length; i++) {
		if (linkTypes[i].value == value) {
			document.write(linkTypes[i].label);
			break;		
		}
	}
}

function displayFolderFlag(value) {
	for (var i = 0; i < folderFlags.length; i++) {
		if (folderFlags[i].value == value) {
			document.write(folderFlags[i].label);
			break;		
		}
	}
}

</script>
</head>

<body>
<html:form action="func" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<html:hidden property="funcId" value=""/>
<html:hidden property="menuId" value=""/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.func.1"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.12"/>" onclick="addFunc(this.form)"/>
<input type="button" value="<bean:message key="all.btn.13"/>" onclick="addMenu(this.form)"/>
</div>

<table class="grid"cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.func.1"/></th>
<th width="170"><bean:message key="all.column.func.2"/></th>
<th width="80"><bean:message key="all.column.func.3"/></th>
<th width="280"><bean:message key="all.column.func.4"/></th>
<th width="30"><bean:message key="all.column.func.5"/></th>
<th width="50"><bean:message key="all.column.func.6"/></th>
<th>&nbsp;</th>
</tr>
</thead>

<tbody>
<logic:iterate id="func" name="form" property="funcs" indexId="idx">
<bean:define id="menu" name="func" property="menu"/>
<tr>
<td align="center"><bean:write name="func" property="funcId"/></td>
<td><bean:write name="func" property="funcLabel"/></td>
<td><bean:write name="menu" property="menuLabel"/></td>
<td><bean:write name="func" property="url"/></td>
<td><script>displayLinkType('<bean:write name="func" property="linkType"/>');</script></td>
<td align="right"><bean:write name="func" property="order"/></td>
<td>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="delFunc(this.form,'<bean:write name="func" property="funcId"/>','<bean:write name="func" property="funcLabel"/>')"/>
<input type="button" value='<bean:message key="all.btn.3"/>' onclick="modifyFunc(this.form, '<bean:write name="func" property="funcId"/>')"/>
</td>
</tr>
</logic:iterate>
</tbody>
</table>

<br><br>

<table class="grid"cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.func.1"/></th>
<th width="170"><bean:message key="all.column.func.3"/></th>
<th width="30"><bean:message key="all.column.func.5"/></th>
<th width="50"><bean:message key="all.column.func.6"/></th>
<th>&nbsp;</th>
</tr>
</thead>

<tbody>
<logic:iterate id="menu" name="form" property="menus" indexId="idx">
<tr>
<td align="center"><bean:write name="menu" property="menuId"/></td>
<td><bean:write name="menu" property="menuLabel"/></td>
<td><script>displayFolderFlag('<bean:write name="menu" property="folderFlag"/>');</script></td>
<td align="right"><bean:write name="menu" property="order"/></td>
<td>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="delMenu(this.form,'<bean:write name="menu" property="menuId"/>','<bean:write name="menu" property="menuLabel"/>')"/>
<input type="button" value='<bean:message key="all.btn.3"/>' onclick="modifyMenu(this.form, '<bean:write name="menu" property="menuId"/>')"/>
</td>
</tr>
</logic:iterate>
</tbody>
</table>

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.12"/>" onclick="addFunc(this.form)"/>
<input type="button" value="<bean:message key="all.btn.13"/>" onclick="addMenu(this.form)"/>
</div>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

function addFunc(fm) {
	fm.state.value = 'initCreateFunc';
	fm.submit();
}

function addMenu(fm) {
	fm.state.value = 'initCreateMenu';
	fm.submit();
}

function delFunc(fm, id, label) {
	if (!confirm('刪除' + label + '功能。你確定嗎？'))
		return;
		
	fm.state.value = 'deleteFunc';
	fm.funcId.value = id;
	fm.submit();
}

function delMenu(fm, id, label) {
	if (!confirm('刪除' + label + '選單。你確定嗎？'))
		return;
		
	fm.state.value = 'deleteMenu';
	fm.menuId.value = id;
	fm.submit();
}

function modifyFunc(fm, id) {
	fm.state.value = 'initModifyFunc';
	fm.funcId.value = id;
	fm.submit();
}

function modifyMenu(fm, id) {
	fm.state.value = 'initModifyMenu';
	fm.menuId.value = id;
	fm.submit();
}
</script>
</body>
</html>