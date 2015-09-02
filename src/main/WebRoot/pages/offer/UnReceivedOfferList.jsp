<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.offer.5"/></title>
<script type="text/javascript" src="<c:url value="/scripts/tigra/calendar_zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/tigra/calendar.css"/>">
</head>

<body>
<html:form action="offer" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<bean:define id="form" name="OfferForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.offer.5"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
請選擇查詢期間：
<input type="text" name="startDate" id="startDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'startDate'});</script> ～
<input type="text" name="endDate" id="endDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'endDate'});</script>
<input type="checkbox" name="beforeFlag" id="beforeFlag" value="Y"> 是否包含前期應收款項
</div>
<div id="actionbar" align="left" style="width:100%;">
客戶：<input type="text" id="custNme" size="35" onBlur="custOnBlur(this)" /><input type="hidden" name="custId" id="custId"/>
<input type="checkbox" name="allCustFlag" id="allCustFlag" value="Y" onclick="clickAllCustFlag(this)"> 全部客戶
<input type="button" value="<bean:message key="all.btn.4"/>" onclick="query(this.form)"/>
<input type="button" value="<bean:message key="all.btn.7"/>" onclick="printUnReceived()"/>
</div>
<hr>

<span id="result" style="display:none;">
<table id="offers" class="grid" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="40"><bean:message key="all.column.offer.5"/></th>
<th width="140" colspan="2"><bean:message key="all.column.offer.2"/></th>
<th width="250"><bean:message key="all.column.cust.2"/></th>
<th width="100"><bean:message key="all.column.offer.3"/></th>
<th width="135"><bean:message key="all.column.offer.10"/></th>
<th width="135"><bean:message key="all.column.offer.11"/></th>
<th width="135"><bean:message key="all.column.offer.13"/></th>
</tr>
</thead>
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
	$("#custNme").autocomplete(custs, {
		matchContains: true,
		minChars: 0,
		width: 260,
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
	$("#custNme").result(function(event, data, formatted) {
		$("#custId").val("");
		if (data) {
			$("#custId").val(data.id);
		}
	});

	$("#startDate").val(getThisMonthFirstDate());
	$("#endDate").val(getToday());

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
		$("#custId").val('');
	}
}

function clickAllCustFlag(obj) {
	if (obj.checked) {
		$("#custNme").attr("disabled", true);
		$("#custNme").val('');
		$("#custId").val('');
	} else {
		$("#custNme").attr("disabled", false);
		$("#custNme").val('');
		$("#custId").val('');
	}
}

function query(fm) {
	if ($("#startDate").val() == '') {
		alert('查詢起始日期不可以為空');
		return;
	}
	if ($("#endDate").val() == '') {
		alert('查詢結束日期不可以為空');
		return;
	}

	if ($("#allCustFlag").attr("checked") == false) {
		if ($("#custId").val() == '') {
			alert("請選擇客戶");
			return;
		}
	}
	
	var date1 = $("#startDate").val();
	var date2 = $("#endDate").val();
	if (date1 > date2) {
		$("#startDate").val(date2);
		$("#endDate").val(date1);
	}

	var beforeFlag = $("#beforeFlag").attr("checked")? "Y": "";
	var allCustFlag = $("#allCustFlag").attr("checked")? "Y": "";
	var custId = $("#custId").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	
	var url = '<html:rewrite page="/offer.do"/>?state=queryUnReceived';
	url += '&beforeFlag=' + beforeFlag;
	url += '&allCustFlag=' + allCustFlag;
	url += '&startDate=' + startDate;
	url += '&endDate=' + endDate;
	url += '&custId=' + custId;
	callAjax(url, "callbackQueryUnReceived");	
}

function callbackQueryUnReceived() {
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

function resetQuery() {
	$("#startDate").val(getThisMonthFirstDate());
	$("#endDate").val(getToday());
	$("#beforeFlag").attr("checked", false);

	$("#custId").val('');
	$("#custNme").val('');
	$("#allCustFlag").attr("checked", false);
}

function processQueryResult() {
 	var errCde = responseMap.get("errCde");
	var errMsg = responseMap.get("errMsg");
	if (errCde != "00") {
		alert(errMsg);
		return false;
	}

	var tableObj = document.getElementById("offers");
	if (tableObj.rows.length > 1) {
		for(var i = tableObj.rows.length - 1; i > 0 ; i--) {
			tableObj.deleteRow(i);
		}
	}

	var sum = 0;
	var rec = 0;
	var cost = 0;
	var rowCount = responseMap.get("rowCount");
	for(var i = 0; i < rowCount; i++){ 
		var rowIdx = i + 1;
		var newRowObj = tableObj.insertRow(rowIdx);
		newRowObj.align = "left";

		var cellData = [
			rowIdx,
			(responseMap.get("offer-" + i + "-before") == 'Y')? "前期": "&nbsp;",
			responseMap.get("offer-" + i + "-offerDate"),
			responseMap.get("offer-" + i + "-custNme"),
			responseMap.get("offer-" + i + "-id"),
			responseMap.get("offer-" + i + "-total"),
			responseMap.get("offer-" + i + "-receivedAmt"),
			responseMap.get("offer-" + i + "-cost")
		];
		for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
			var newCellObj = newRowObj.insertCell(cellIdx);
			if (cellIdx == 0) newCellObj.align = 'center';
			if (cellIdx == 2) newCellObj.align = 'center';
			if (cellIdx == 5) newCellObj.align = 'right';
			if (cellIdx == 6) newCellObj.align = 'right';
			if (cellIdx == 7) newCellObj.align = 'right';
			if (cellIdx == 1) newCellObj.width = '40';
			if (cellIdx == 2) newCellObj.width = '100';
			if (cellIdx == 4) {
				newCellObj.innerHTML = "<a href='<c:url value="/offer.do"/>?state=initQuery&id=" + cellData[cellIdx] + "' target='_blank'>" + cellData[cellIdx] + "</a>";
			} else {
				newCellObj.innerHTML = cellData[cellIdx];
			}
		}
		sum += parseFloat(responseMap.get("offer-" + i + "-total"));
		rec += parseFloat(responseMap.get("offer-" + i + "-receivedAmt"));
		cost += parseFloat(responseMap.get("offer-" + i + "-cost"));
	}
	
	var profit = sum - cost;
	var profitColor1 = 'green';
	var profitColor2 = 'red';
	
	var rowIdx = i + 1;
	var newRowObj = tableObj.insertRow(rowIdx);
	newRowObj.align = "left";

	var cellData = [
		"&nbsp;",
		"&nbsp;",
		"&nbsp;",
		"&nbsp;",
		"總計",
		"<font color='blue'>" + sum.toFixed(2) + "</font>",
		"<font color='blue'>" + rec.toFixed(2) + "</font>",
		"<font color='blue'>" + cost.toFixed(2) + "</font>"
	];
	for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
		var newCellObj = newRowObj.insertCell(cellIdx);
		if (cellIdx == 0) newCellObj.align = 'center';
		if (cellIdx == 2) newCellObj.align = 'center';
		if (cellIdx == 5) newCellObj.align = 'right';
		if (cellIdx == 6) newCellObj.align = 'right';
		if (cellIdx == 7) newCellObj.align = 'right';
		if (cellIdx == 1) newCellObj.width = '40';
		if (cellIdx == 2) newCellObj.width = '100';
		newCellObj.innerHTML = cellData[cellIdx];
	}

	var rowIdx = i + 1 + 1;
	var newRowObj = tableObj.insertRow(rowIdx);
	newRowObj.align = "left";

	var cellData = [
		"&nbsp;",
		"&nbsp;",
		"&nbsp;",
		"&nbsp;",
		"<font color='" + profitColor1 + "'>盈</font><font color='" + profitColor2 + "'>虧</font>",
		"&nbsp;",
		"&nbsp;",
		"<font color='" + ((profit >= 0)? profitColor1: profitColor2) + "'>" + ((profit >= 0)? "+": "-") + " $" + profit.toFixed(2) + "</font>"
	];
	for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
		var newCellObj = newRowObj.insertCell(cellIdx);
		if (cellIdx == 0) newCellObj.align = 'center';
		if (cellIdx == 2) newCellObj.align = 'center';
		if (cellIdx == 5) newCellObj.align = 'right';
		if (cellIdx == 6) newCellObj.align = 'right';
		if (cellIdx == 7) newCellObj.align = 'right';
		if (cellIdx == 1) newCellObj.width = '40';
		if (cellIdx == 2) newCellObj.width = '100';
		newCellObj.innerHTML = cellData[cellIdx];
	}
	
	return true;
}

function printUnReceived() {
	if (isHiddenObj("result")) {
		alert('請先查詢');
		return;
	}

	var tableObj = document.getElementById("offers");
	if (tableObj.rows.length == 1) {
		alert('無任何查詢結果可供列印');
		return;
	}

	var id = "";
	var before = "";
	for(var i = 1; i < tableObj.rows.length - 1; i++) {
		var masterId = tableObj.rows[i].cells[4].innerText;
		if (masterId == undefined)
			masterId = tableObj.rows[i].cells[4].innerHTML.replace(/<.+?>/gim, '');
		if (tableObj.rows[i].cells[1].innerHTML == '前期') {
			before += "," + masterId;
		}
		id += "," + masterId;
	}
	
	var printWin = openWindow('/fb/unreceived.sheet?id=' + id + '&before=' + before, 'printUnReceived', 793, 529);
}
</script>
</body>
</html>