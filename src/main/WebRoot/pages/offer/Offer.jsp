<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<logic:notEqual name="OFFER_BACK" scope="session" value="Y"><bean:message key="all.func.offer.1"/></logic:notEqual>
<logic:equal name="OFFER_BACK" scope="session" value="Y"><bean:message key="all.func.offer.1.2"/></logic:equal>
</title>
<script type="text/javascript" src="<c:url value="/scripts/tigra/calendar_zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/tigra/calendar.css"/>">
</head>

<body>
<html:form action="offer" method="post" onsubmit="return false">
<html:hidden property="state" value=""/>
<html:hidden property="masterId"/>
<bean:define id="form" name="OfferForm"/>
<bean:define name="form" property="custs" id="custs" type="java.util.ArrayList"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：
<logic:notEqual name="OFFER_BACK" scope="session" value="Y"><bean:message key="all.func.offer.1"/></logic:notEqual>
<logic:equal name="OFFER_BACK" scope="session" value="Y"><bean:message key="all.func.offer.1.2"/></logic:equal>
</H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
以客戶來查詢：
<input type="text" id="qCustNme" size="25" onBlur="custOnBlur(this)" /><input type="hidden" id="qCustId" />
(近二個月內)</p>
<p>
以期間來查詢：
<input type="text" name="startDate" id="startDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'startDate'});</script> ～
<input type="text" name="endDate" id="endDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'endDate'});</script>
<input type="button" value="<bean:message key="all.btn.4"/>" onclick="queryOffer();"/>
<logic:notEqual name="OFFER_BACK" scope="session" value="Y">
<input type="button" value="<bean:message key="all.btn.7"/>" onclick="printOffer()">
<input type="button" value="<bean:message key="all.btn.3"/>" onclick="modify(this.form)">
<input type="button" value="<bean:message key="offer.btn.3"/>" onclick="modifyReceiveAmt(this.form)">
</logic:notEqual>
<input type="button" value="<bean:message key="all.btn.2"/>" onclick="del(this.form)">
</p>
<hr>

<span id="result" style="display:none;">
物流士：<span id="deliveryUserNme"></span>
<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.cust.2"/></th><td width="150" id="custNme">&nbsp;</td>
<th width="100"><bean:message key="all.column.offer.1"/></th><td width="100" id="custId">&nbsp;</td>
<th width="100"><bean:message key="all.column.offer.2"/></th><td width="100" id="offerDate">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.3"/></th><td id="bizNo">&nbsp;</td>
<th><bean:message key="all.column.cust.4"/></th><td id="tel" colspan="3">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.5"/></th><td id="deliverAddr" colspan="3">&nbsp;</td><th><bean:message key="all.column.offer.3"/></th><td id="masterId">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.6"/></th><td id="custMemo" colspan="3">&nbsp;</td><th><bean:message key="all.column.offer.4"/></th><td id="invoiceNbr">&nbsp;</td>
</tr>
</table>

<table class="sheet" id="details" cellspacing="0" cellpadding="1" border="1" width="650">
<tr align="center">
  <th width="50"><bean:message key="all.column.offer.5"/></th>
  <th><bean:message key="all.column.prod.2"/></th>
  <th width="70"><bean:message key="all.column.offer.6"/></th>
  <th width="50"><bean:message key="all.column.prod.3"/></th>
  <th width="100"><bean:message key="all.column.prod.4"/></th>
  <th width="130"><bean:message key="all.column.offer.7"/></th>
</tr>
</table>

<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.offer.8"/></th><td width="150" id="discount" align="right">&nbsp;</td>
<th width="100"><bean:message key="all.column.offer.9"/></th><td width="150" id="amt" align="right">&nbsp;</td>
<th width="150" colspan="2">客戶簽收</th>
</tr>
<tr>
<th><bean:message key="all.column.offer.11"/></th><td align="right"><input type="checkbox" id="received" value="Y" onclick="receive(this)"><input type="text" name="recAmt" id="recAmt" size="15" disabled="disabled" class="num"><html:hidden property="receiveAmt" styleId="receiveAmt"/></td>
<th>營業稅</th><td>&nbsp;</td>
<td colspan="2" rowspan="3">&nbsp;</td>
</tr>
<tr>
<th>未清款</th><td>&nbsp;</td>
<th><bean:message key="all.column.offer.10"/></th><td id="total" align="right">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.offer.12"/></th><td id="memo" colspan="3" height="50" valign="top">&nbsp;</td>
</tr>
</table>
</span>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var custs = [
<logic:iterate name="form" property="custs" id="cust">
	{ 
	  id: '<bean:write name="cust" property="custId"/>',
	  name: '<bean:write name="cust" property="custNme"/>'
	},
</logic:iterate>
];

$().ready(function() {
	$("#qCustNme").autocomplete(custs, {
		matchContains: true,
		minChars: 0,
		width: 190,
		scroll: true,
		scrollHeight: 120,
		formatItem: function(row, i, max) {
			if (row)
				return row.name;
			else
				return "";
		},
		formatMatch: function(row, i, max) {
			if (row)
				return row.name;
			else
				return "";
		},
		formatResult: function(row) {
			if (row)
				return row.name;
			else
				return "";
		}
	});
	$("#qCustNme").result(function(event, data, formatted) {
		$("#qCustId").val("");
		if (data) {
			$("#qCustId").val(data.id);
		}
	});
	
	$("#startDate").val(getThisMonthFirstDate());
	$("#endDate").val(getToday());
	
<logic:notEmpty name="OFFER_ID" scope="request">
	queryOffer('<bean:write name="OFFER_ID" scope="request"/>');
</logic:notEmpty>

});

function getThisMonthFirstDate() {
	var date = new Date();
	var year = new String(date.getFullYear());
	var month = new String(date.getMonth() + 1);
	if(month.length < 2){
		month = "0" + month;
	}
	return year + "/" + month + "/01";
}

function custOnBlur(obj) {
	if(obj.value == '') {
		$("#qCustId").val('');
	}
}

function queryOffer(offerId) {
	if (offerId == null || offerId == '') {
		var custId = $("#qCustId").val();
		var date1 = $("#startDate").val();
		var date2 = $("#endDate").val();

		if (custId.trim() == '' && date1.trim() == '' && date2.trim() == '') {
			alert('請先選擇一位客戶或者一段期間進行查詢');
			document.getElementById("qCustNme").focus();
			return;
		}
		
		var url = '<html:rewrite page="/offer.do"/>?state=query';
		if (custId.trim() != '') {
			url += '&custId=' + custId;
		} else {
			if (date1.trim() == '') {
				alert('查詢起始日期不可以為空');
				return;
			}
			if (date2.trim() == '') {
				alert('查詢結束日期不可以為空');
				return;
			}
		
			if (date1 > date2) {
				$("#startDate").val(date2);
				$("#endDate").val(date1);
				date1 = $("#startDate").val();
				date2 = $("#endDate").val();
			}
			url += '&startDate=' + date1;
			url += '&endDate=' + date2;
		}
		callAjax(url, "callbackQueryOffer");	
	} else {
		var url = '<html:rewrite page="/offer.do"/>?state=query';
		url += '&offerId=' + offerId;
		callAjax(url, "callbackQueryOffer");	
	}
}

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

function setOfferData(rowData) {
	if (rowData != null) {
		var keys = rowData.getKeys();
		var details = new Array();
		for(var i = 0; i < keys.length; i++) {
			var key = keys[i];
			if (key == 'errCde' || key == 'errMsg' || key == 'rowCount' || key == 'detailCount') continue;

			if (key.indexOf("detail-") == -1) {
				if (key == 'receiveAmt') {
					if (rowData.get(key) != '' && parseFloat(rowData.get(key)).toFixed(2) >= parseFloat(rowData.get('total')).toFixed(2)) {
						$('#received').attr('checked', true);
						$('#received').attr('disabled', true);
					} else {
						$('#received').attr('checked', false);
						$('#received').attr('disabled', false);
					}
					$("#recAmt").val(rowData.get(key))
					$("#" + key).val(rowData.get(key));
				} else {
					$("#" + key).html(rowData.get(key));
				}
			} else {
				var detailIdx = parseInt(key.substring(7, key.indexOf("-", 7)), 10);
				if (details[detailIdx] == null) {
					details[detailIdx] = new Map();
				}
				var detailKey = key.substring(key.indexOf("-", 7) + 1);
				details[detailIdx].put(detailKey, rowData.get(key));
			}
		}
		setOfferDetailData(details);
	}
}

function setOfferDetailData(details){
	var tableObj = document.getElementById("details");
	if (tableObj.rows.length > 1) {
		for(var i = tableObj.rows.length - 1; i > 0 ; i--) {
			tableObj.deleteRow(i);
		}
	}
	
	for(var i = 0; i < details.length; i++) {
		var detail = details[i];
		var rowIdx = i + 1;
		if (detail == null) {
			detail = new Map();
			rowIdx = '&nbsp;';
		}
		var newRowObj = tableObj.insertRow(i + 1);
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

function printOffer() {
	if (isHiddenObj('result') == true) {
		alert('請先查詢出貨單');
		return;
	}

	var printWin = openWindow('/fb/offer.sheet?id=' + $("#masterId").html(), 'printOffer', 793, 529);
}

function receive(obj) {
	if (obj.checked) {
		$("#receiveAmt").val($("#total").html());
		$("#recAmt").val($("#total").html());
	} else {
		$("#receiveAmt").val("0.0");
		$("#recAmt").val("0.0");
	}
}

function modify(fm) {
	if (isHiddenObj('result') == true) {
		alert('請先查詢出貨單');
		return;
	}

	fm.state.value = 'initModify';
	fm.masterId.value = $("#masterId").html();
	fm.submit();
}

function modifyReceiveAmt(fm) {
	if (isHiddenObj('result') == true) {
		alert('請先查詢出貨單');
		return;
	}

	if ($('#received').attr('disabled') || $('#received').attr('checked') == false) {
		alert('沒有任何修改');
		return;
	}
	
	fm.state.value = 'modifyReceiveAmt';
	fm.masterId.value = $("#masterId").html();
	fm.submit();
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