<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.basic.1"/></title>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.tablesorter.css"/>" media="all">
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.tablesorter.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.uitablefilter.js"/>"></script>
</head>

<body>
<html:form action="cust" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<html:hidden property="custId" value=""/>
<bean:define id="form" name="CustForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.basic.1"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.1"/>" onclick="add(this.form)"/>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="del(this.form)"/>
<form id="filter-form"><span style="text-align:right;"><bean:message key="all.btn.9"/>: <input name="filter" id="filter" value="" maxlength="30" size="30" type="text" autocomplete="off"></span></form>
</div>

<table id="tb" class="grid tablesorter" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th>&nbsp;</th>
<th width="40"><bean:message key="all.column.cust.1"/></th>
<th width="170"><bean:message key="all.column.cust.2"/></th>
<th width="150"><bean:message key="all.column.cust.4"/></th>
<th width="330"><bean:message key="all.column.cust.5"/></th>
<th width="100"><bean:message key="all.column.cust.6"/></th>
<th>&nbsp;</th>
</tr>
</thead>

<tbody>
<logic:iterate id="cust" name="form" property="custs" indexId="idx">
<tr>
<td><input type="checkbox" name="selectIdx" value='<%=idx.toString()%>'></td>
<td align="center"><bean:write name="cust" property="custId"/></td>
<td><bean:write name="cust" property="custNme"/></td>
<td><bean:write name="cust" property="tel"/></td>
<td><bean:write name="cust" property="deliverAddr"/></td>
<td><bean:write name="cust" property="memo"/></td>
<td><input type="button" value='<bean:message key="all.btn.3"/>' onclick="modify(this.form, '<bean:write name="cust" property="custId"/>')"/></td>
</tr>
</logic:iterate>
</tbody>
</table>

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.1"/>" onclick="add(this.form)"/>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="del(this.form)"/>
</div>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(function () {
	$("#tb").tablesorter({
		headers: {0:{sorter: false},1:{sorter: false},6: {sorter: false}}
	});

	$("#filter").keyup(function() {
		$.uiTableFilter($("#tb"), this.value, '<bean:message key="all.column.cust.2"/>');
	});
	
	$('#filter-form').submit(function(){return false;}).focus(); //Give focus to input field
});

function add(fm) {
	fm.state.value = 'initCreate';
	fm.submit();
}

function del(fm) {
	var idx = null;
	if (fm.selectIdx.length > 1) {
		for (var i = fm.selectIdx.length; --i >= 0; fm.selectIdx[i].checked && eval('idx += fm.selectIdx[' + i + '].value'));
	} else {
		if (fm.selectIdx.checked) {
			idx = fm.selectIdx.value;
		}
	}
	if (idx == null) {
		alert("請選擇欲刪除的客戶");
		fm.selectIdx[0].focus();
		return false;
	}

	fm.state.value = 'delete';
	fm.submit();
}

function modify(fm, id) {
	fm.state.value = 'initModify';
	fm.custId.value = id;
	fm.submit();
}

</script>
</body>
</html>