<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<logic:notEqual name="PURCHASE_BACK" scope="session" value="Y"><bean:message key="all.func.purchase.1"/></logic:notEqual>
<logic:equal name="PURCHASE_BACK" scope="session" value="Y"><bean:message key="all.func.purchase.1.2"/></logic:equal>
</title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
</head>

<body>
<html:form action="purchase" method="post" onsubmit="return false">
<html:hidden property="state" value=""/>
<html:hidden property="masterId"/>
<bean:define id="form" name="PurchaseForm"/>
<bean:define name="form" property="facts" id="facts" type="java.util.ArrayList"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：
<logic:notEqual name="PURCHASE_BACK" scope="session" value="Y"><bean:message key="all.func.purchase.1"/></logic:notEqual>
<logic:equal name="PURCHASE_BACK" scope="session" value="Y"><bean:message key="all.func.purchase.1.2"/></logic:equal>
</H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
請先選擇廠商：
<input type="text" id="qFactNme" size="25" onBlur="factOnBlur(this)" /><input type="hidden" id="qFactId" />
<input type="button" value="<bean:message key="all.btn.4"/>" onclick="queryPurchase();"/>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="del(this.form)">
</p>
<hr>

<span id="result" style="display:none;">
<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.fact.2"/></th><td width="150" id="factNme">&nbsp;</td>
<th width="100"><bean:message key="all.column.purchase.1"/></th><td width="100" id="factId">&nbsp;</td>
<th width="100"><bean:message key="all.column.purchase.2"/></th><td width="100" id="purchaseDate">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.4"/></th><td id="contact" colspan="3">&nbsp;</td>
<th><bean:message key="all.column.fact.3"/></th><td id="bizNo">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.5"/></th><td id="tel" colspan="3">&nbsp;</td><th><bean:message key="all.column.purchase.3"/></th><td id="masterId">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.6"/></th><td id="addr" colspan="3">&nbsp;</td><th><bean:message key="all.column.purchase.4"/></th><td id="invoiceNbr">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.7"/></th><td id="factMemo" colspan="5">&nbsp;</td>
</tr>
</table>

<table class="sheet" id="details" cellspacing="0" cellpadding="1" border="1" width="650">
<tr align="center">
  <th width="50"><bean:message key="all.column.purchase.5"/></th>
  <th><bean:message key="all.column.prod.2"/></th>
  <th width="70"><bean:message key="all.column.purchase.6"/></th>
  <th width="50"><bean:message key="all.column.prod.3"/></th>
  <th width="100"><bean:message key="all.column.prod.4"/></th>
  <th width="130"><bean:message key="all.column.purchase.7"/></th>
</tr>
</table>

<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.purchase.8"/></th><td width="150" id="discount" align="right">&nbsp;</td>
<th width="100"><bean:message key="all.column.purchase.9"/></th><td width="150" id="amt" align="right">&nbsp;</td>
<th width="150" colspan="2">客戶簽收</th>
</tr>
<tr>
<th>己清款</th><td>&nbsp;</td>
<th>營業稅</th><td>&nbsp;</td>
<td colspan="2" rowspan="3">&nbsp;</td>
</tr>
<tr>
<th>未清款</th><td>&nbsp;</td>
<th><bean:message key="all.column.purchase.10"/></th><td id="total" align="right">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.purchase.11"/></th><td id="memo" colspan="3" height="50" valign="top">&nbsp;</td>
</tr>
</table>
</span>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var facts = [
<logic:iterate name="form" property="facts" id="fact">
	{ 
	  id: '<bean:write name="fact" property="factId"/>',
	  name: '<bean:write name="fact" property="factNme"/>'
	},
</logic:iterate>
];

$().ready(function() {
	$("#qFactNme").autocomplete(facts, {
		matchContains: true,
		minChars: 0,
		width: 190,
		scroll: true,
		scrollHeight: 120,
		formatItem: function(row, i, max) {
			return row.name;
		},
		formatMatch: function(row, i, max) {
			return row.name;
		},
		formatResult: function(row) {
			return row.name;
		}
	});
	$("#qFactNme").result(function(event, data, formatted) {
		$("#qFactId").val("");
		if (data) {
			$("#qFactId").val(data.id);
		}
	});
	
<logic:notEmpty name="PURCHASE_ID" scope="request">
	queryPurchase('<bean:write name="PURCHASE_ID" scope="request"/>');
</logic:notEmpty>

});

function factOnBlur(obj) {
	if(obj.value == '') {
		$("#qFactId").val('');
	}
}

function queryPurchase(purchaseId) {
	if (purchaseId == null || purchaseId == '') {
		var factId = $("#qFactId").val();
		if (factId.trim() == '') {
			alert('請先選擇一位廠商進行查詢');
			document.getElementById("qFactNme").focus();
			return;
		}
		var url = '<html:rewrite page="/purchase.do"/>?state=query';
		url += '&factId=' + factId;
		callAjax(url, "callbackQueryPurchase");	
	} else {
		var url = '<html:rewrite page="/purchase.do"/>?state=query';
		url += '&purchaseId=' + purchaseId;
		callAjax(url, "callbackQueryPurchase");	
	}
	
}

function callbackQueryPurchase() {
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
		var src = '<html:rewrite page="/purchase.do"/>';
		var rtnValue = openDialogWindow(src, responseMap.get("factId"),"getPurchaseList", 700, 400);
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
		setPurchaseData(rowData);
		return true;
	} else {
		setPurchaseData(responseMap);
		return true;
	}		
}

function resetQuery() {
	$("#qFactId").val('');
	$("#qFactNme").val('');
	document.getElementById('qFactNme').focus();
}

function setPurchaseData(rowData) {
	if (rowData != null) {
		var keys = rowData.getKeys();
		var details = new Array();
		for(var i = 0; i < keys.length; i++) {
			var key = keys[i];
			if (key == 'errCde' || key == 'errMsg' || key == 'rowCount' || key == 'detailCount') continue;

			if (key.indexOf("detail-") == -1) {
				$("#" + key).html(rowData.get(key));
			} else {
				var detailIdx = parseInt(key.substring(7, key.indexOf("-", 7)), 10);
				if (details[detailIdx] == null) {
					details[detailIdx] = new Map();
				}
				var detailKey = key.substring(key.indexOf("-", 7) + 1);
				details[detailIdx].put(detailKey, rowData.get(key));
			}
		}
		setPurchaseDetailData(details);
	}
}

function setPurchaseDetailData(details){
	var tableObj = document.getElementById("details");
	if (tableObj.rows.length > 1) {
		for(var i = tableObj.rows.length - 1; i > 0 ; i--) {
			tableObj.deleteRow(i);
		}
	}

	for(var i = 0; i < details.length; i++) {
		var detail = details[i];
		var rowIdx = i + 1;
		var newRowObj = tableObj.insertRow(rowIdx);
		newRowObj.align = "right";

		var cellData = [
			rowIdx,
			detail.get("prodNme"),
			detail.get("qty"),
			detail.get("unit"),
			detail.get("price"),
			detail.get("amt")
		];
		for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
			var newCellObj = newRowObj.insertCell(cellIdx);
			if (cellIdx == 0) newCellObj.align = 'center';
			if (cellIdx == 1) newCellObj.align = 'left';
			newCellObj.innerHTML = cellData[cellIdx];
		}
	}		
}

function del(fm) {
	if (!confirm('確定刪除此筆貨單？'))
		return false;

	fm.state.value = 'delete';
	fm.masterId.value = $("#masterId").html();
	fm.submit();
}
</script>
</body>
</html>