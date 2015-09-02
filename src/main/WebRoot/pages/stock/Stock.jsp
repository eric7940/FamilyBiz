<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.stock.1"/></title>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.tablesorter.css"/>" media="all">
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.tablesorter.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.uitablefilter.js"/>"></script>
</head>

<body>
<html:form action="stock" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<bean:define id="form" name="StockForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.stock.1"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
<form id="filter-form"><span style="text-align:right;"><bean:message key="all.btn.9"/>: <input name="filter" id="filter" value="" maxlength="30" size="30" type="text" autocomplete="off"></span></form>
</div>

<table id="tb" class="grid tablesorter" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.stock.1"/></th>
<th width="480"><bean:message key="all.column.stock.2"/></th>
<th width="100"><bean:message key="all.column.stock.3"/></th>
<th width="120"><bean:message key="all.column.stock.4"/></th>
<th width="80"><bean:message key="all.column.prod.3"/></th>
</tr>
</thead>

<tbody>
<logic:iterate id="prodstock" name="form" property="prods" indexId="idx">
<bean:define name="prodstock" property="prod" id="prod"/>
<tr>
<td align="center"><bean:write name="prod" property="prodId"/></td>
<td><bean:write name="prod" property="prodNme"/></td>
<td align="right"><bean:write name="prodstock" property="qty"/></td>
<td align="right"><bean:write name="prod" property="saveQty"/></td>
<td align="right"><bean:write name="prod" property="unit"/></td>
</tr>
</logic:iterate>
</tbody>
</table>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(function () {
	$("#tb").tablesorter({
		headers: {0:{sorter: false}}
	});

	$("#filter").keyup(function() {
		$.uiTableFilter($("#tb"), this.value, '<bean:message key="all.column.stock.2"/>');
	});
	
	$('#filter-form').submit(function(){return false;}).focus(); //Give focus to input field
});


</script>
</body>
</html>