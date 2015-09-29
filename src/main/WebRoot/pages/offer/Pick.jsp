<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<bean:message key="all.func.offer.7"/>
</title>
<script type="text/javascript" src="<c:url value="/scripts/tigra/calendar_zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/tigra/calendar.css"/>">
</head>

<body>
<html:form action="pick" method="post">
<html:hidden property="state" value="query"/>
<bean:define id="form" name="PickForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.offer.7"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
以出貨日來查詢：
<html:text property="offerDate" styleId="offerDate" maxlength="10"/><script>new tcal ({'controlname': 'offerDate'});</script>

<input type="button" value="列出當日出貨單所有客戶" onclick="queryCusts(this)"/>
<logic:equal name="qry_prod_flag" scope="request" value="Y">
<input id="printBtn" type="button" value="<bean:message key="all.btn.7"/>" onclick="printPick()"/><br/>
</logic:equal>
<br/>

<html:hidden styleId="custIds" property="custIds"/>
<div id="custResult">
<div id="qryCustsResult"></div>
<input type="submit" value="<bean:message key="all.btn.4"/>"/>
</div>
</p>
<hr>

<logic:equal name="qry_prod_flag" scope="request" value="Y">
<span id="result" >
<table id="tb" class="grid tablesorter" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="35%"><bean:message key="all.column.prod.2"/></th>
<th width="5%"><bean:message key="all.column.prod.3"/></th>
<th width="10%"><bean:message key="all.column.offer.9"/></th>
<th width="50%">details</th>
</tr>
</thead>

<tbody>
<logic:iterate id="prod" name="form" property="products" indexId="idx">
<bean:size id="sizeOfOffers" name="prod" property="offers"/> 
<tr>
<td><bean:write name="prod" property="prodNme"/></td>
<td align="center"><bean:write name="prod" property="unit"/></td>
<td align="right"><bean:write name="prod" property="sumQty"/></td>
<td>
	<table id="tb2" class="grid tablesorter" cellspacing="0" cellpadding="1" border="1" width="100%">
	<logic:iterate id="offer" name="prod" property="offers" indexId="idx2">
	<tr>
	<td width="40%" align="right"><bean:write name="offer" property="masterId"/></td>
	<td width="40%" align="right"><bean:write name="offer" property="custNme"/></td>
	<td width="20%" align="right"><bean:write name="offer" property="qty"/></td>
	</tr>
	</logic:iterate>
	</table>
</td>
</tr>
</logic:iterate>
</tbody>

</table>
</span>
</logic:equal>

</td>
</tr>
</table>

</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

function queryCusts(btn) {
	$("#qryCustsResult").html('');
	$("#custIds").val('');
	$("#custResult").hide();
	$("#result").hide();
	$("#printBtn").hide();
	$.ajax({
		url: '<html:rewrite page="/pick.do"/>',
		data: ({state: "qryCusts", date: $("#offerDate").val()}),
		success: function(result){
			if (result != '') {
				var r = result.split('\n');
				var ol = "";
				var custIds = "";
				for(var i = 0; i < r.length; i++) {
					if (!r[i]) continue;
					var d = r[i].split('|');
					ol += "<input type='checkbox' name='custs' value='" + d[0] + "' checked />" + d[1] + " ";
					custIds += ',' + d[0];
				}
				$("#qryCustsResult").html(ol);
				$("#custIds").val(custIds);
				$("#custResult").show();
				$("#result").hide();
			} else {
				alert('無任何出貨資料，請重新操作');
			}
		}
	});
}


function printPick() {
	if (isHiddenObj('result') == true) {
		alert('請先查詢');
		return;
	}
	var printWin = openWindow('/fb/pick.sheet?offerDate=' + $("#offerDate").val() + '&custs=' + $("#custIds").val(), 'printPick', 793, 529);
}
$().ready(function() {
	$("#custResult").hide();
});

$(".checkbox").change(function() {
	if(this.checked) {
		$("#custIds").val($("#custIds").val() + ',' + this.value);
	} else {
		$("#custIds").val().replace(',' + this.value, '');
	}
});
</script>
</body>
</html>