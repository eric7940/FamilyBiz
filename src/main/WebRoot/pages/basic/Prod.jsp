<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.basic.2"/></title>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.tablesorter.css"/>" media="all">
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.tablesorter.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.uitablefilter.js"/>"></script>
</head>

<body>
<html:form action="prod" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<html:hidden property="prodId" value=""/>
<bean:define id="form" name="ProdForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.basic.2"/></H2>
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
<th width="30">&nbsp;</th>
<th width="40"><bean:message key="all.column.prod.1"/></th>
<th width="480"><bean:message key="all.column.prod.2"/></th>
<th width="80"><bean:message key="all.column.prod.3"/></th>
<th width="120"><bean:message key="all.column.prod.4"/></th>
<th width="120"><bean:message key="all.column.prod.5"/></th>
<th width="120"><bean:message key="all.column.prod.6"/></th>
<th width="50">&nbsp;</th>
</tr>
</thead>

<tbody>
<logic:iterate id="prod" name="form" property="prods" indexId="idx">
<tr>
<td><input type="checkbox" name="selectIdx" value='<%=idx.toString()%>'></td>
<td align="center"><bean:write name="prod" property="prodId"/></td>
<td><bean:write name="prod" property="prodNme"/></td>
<td align="center"><bean:write name="prod" property="unit"/></td>
<td align="right"><bean:write name="prod" property="price"/></td>
<td align="right"><bean:write name="prod" property="cost"/></td>
<td align="right"><bean:write name="prod" property="saveQty"/></td>
<td><input type="button" value='<bean:message key="all.btn.3"/>' onclick="modify(this.form, '<bean:write name="prod" property="prodId"/>')"/></td>
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
		headers: {0:{sorter: false},1:{sorter: false},7: {sorter: false}}
	});

	$("#filter").keyup(function() {
		$.uiTableFilter($("#tb"), this.value, '<bean:message key="all.column.prod.2"/>');
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
		alert("請選擇欲刪除的產品");
		fm.selectIdx[0].focus();
		return false;
	}

	fm.state.value = 'delete';
	fm.submit();
}

function modify(fm, id) {
	fm.state.value = 'initModify';
	fm.prodId.value = id;
	fm.submit();
}

</script>
</body>
</html>