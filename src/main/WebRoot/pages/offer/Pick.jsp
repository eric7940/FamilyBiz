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
<input type="text" name="offerDate" id="offerDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'offerDate'});</script>
<input type="submit" value="<bean:message key="all.btn.4"/>"/>
<logic:notEqual name="OFFER_BACK" scope="session" value="Y">
<input type="button" value="<bean:message key="all.btn.7"/>" onclick="printPick()">
</logic:notEqual>
</p>
<hr>

<span id="result" style="display:none;">
<table id="tb" class="grid tablesorter" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.prod.2"/></th>
<th width="480"><bean:message key="all.column.prod.3"/></th>
<th width="80"><bean:message key="all.column.offer.9"/></th>
<th width="120"><bean:message key="all.column.offer.3"/></th>
<th width="120"><bean:message key="all.column.cust.2"/></th>
</tr>
</thead>

<tbody>
<logic:iterate id="prod" name="form" property="products" indexId="idx">
<tr>
<td><bean:write name="prod" property="prodNme"/></td>
<td align="center"><bean:write name="prod" property="unit"/></td>
<td align="right"><bean:write name="prod" property="sumQty"/></td>
<td align="right"><bean:write name="prod" property="masterId"/></td>
<td align="right"><bean:write name="prod" property="custNme"/></td>
<td align="right"><bean:write name="prod" property="qty"/></td>
</tr>
</logic:iterate>
</tbody>

</table>


</span>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$().ready(function() {
	$("#offerDate").val(getToday());
});

function callbackQueryOffer() {
	if (httpreq.readyState == 4) {
		if (httpreq.status == 200) {
			var response = httpreq.responseText;
			var rows = response.split('\n');
			for(var i = 0; i < rows.length; i++){ 
				if (rows[i] == '') continue;
				var data = rows[i].split('=');
				responseMap.put(data[0], data[1]);
			}
			if (processQueryResult() == true) {
				displayObj("result");
			} else {
				hiddenObj("result");
				resetQuery();
			}
		} else {
			alert('檢核發生問題\n\n' + httpreq.status + '\n' + httpreq.responseText);
			hiddenObj("result");
		}				
	}
}

function processQueryResult() {
    var errCde = responseMap.get("errCde");
    var errMsg = responseMap.get("errMsg");
    if (errCde != "00") {
		alert(errMsg);
		return false;
    }

    var rowCount = responseMap.get("rowCount");
	if (rowCount > 1) {
		var src = '<html:rewrite page="/offer.do"/>';
		var feeid = responseMap.get("custId") == 'null'? responseMap.get("startDate") + ';' + responseMap.get("endDate"): responseMap.get("custId");
		var rtnValue = openDialogWindow(src, feeid, "getOfferList", 700, 400);
		if (rtnValue == null || rtnValue == '') {
			return false;
		}
		
		var rowData = new Map();
		var rows = rtnValue.split('\n');
		for(var i = 0; i < rows.length; i++){ 
			if (rows[i] == '') continue;
			var data = rows[i].split('=');
			rowData.put(data[0], data[1]);
		}
		setOfferData(rowData);
		return true;
	} else {
		setOfferData(responseMap);
		return true;
	}		
}

function resetQuery() {
	$("#qCustId").val('');
	$("#qCustNme").val('');
	document.getElementById('qCustNme').focus();
}





function printPick() {
	if (isHiddenObj('result') == true) {
		alert('請先查詢');
		return;
	}

	//var printWin = openWindow('/fb/offer.sheet?id=' + $("#masterId").html(), 'printOffer', 793, 529);
}


</script>
</body>
</html>