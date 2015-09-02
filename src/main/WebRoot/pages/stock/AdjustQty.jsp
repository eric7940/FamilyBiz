<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.stock.2"/></title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
</head>

<body>
<html:form action="stock" method="post" onsubmit="return false;">
<html:hidden property="state" value="adjustQty"/>
<input type="hidden" name="detailCount" value="0"/>
<bean:define id="form" name="StockForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.stock.2"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
<input type="text" id="prodNme" size="35" onBlur="prodOnBlur(this)" /> <input type="button" value="<bean:message key="stock.btn.1"/>" onclick="addDetailRow()">
<input type="hidden" id="prodId" />
<input type="hidden" id="qty" />
<input type="hidden" id="unit" />
</div>

<table class="sheet" id="details" cellspacing="0" cellpadding="1" border="1" width="800">
<thead>
<tr>
<th width="30"><bean:message key="all.column.stock.1"/></th>
<th><bean:message key="all.column.stock.2"/></th>
<th width="100"><bean:message key="all.column.stock.3"/></th>
<th width="50"><bean:message key="all.column.prod.3"/></th>
<th width="100"><bean:message key="all.column.stock.5"/></th>
<th width="100"><bean:message key="all.column.stock.6"/></th>
</tr>
</thead>
</table>

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.1"/>" onclick="go(this.form)">
</div>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var prods = [
<logic:iterate name="form" property="prods" id="prodstock">
<bean:define name="prodstock" property="prod" id="prod"/>
	{ 
	  id: '<bean:write name="prod" property="prodId"/>',
	  name: '<bean:write name="prod" property="prodNme"/>',
	  qty: '<bean:write name="prodstock" property="qty"/>',
	  unit: '<bean:write name="prod" property="unit"/>'
	},
</logic:iterate>
];

$().ready(function() {
	$("#prodNme").autocomplete(prods, {
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
	$("#prodNme").result(function(event, data, formatted) {
		$("#prodId").val("");
		if (data) {
			$("#prodId").val(data.id);
			$("#qty").val(data.qty);
			$("#unit").val(data.unit);
		}
	});
});

function getObjIdx(id) {
	return parseInt(id.substring(7, id.indexOf("-", 7)), 10);
}

function addDetailRow(){
	if (getBlankRowIdx() >= 0) // 若目前已有空的資料時, 則不再新增一筆空的資料
		return;

	if ($("#prodId").val() == '')
		return;

	var tableObj = document.getElementById("details");
	var rowIdx = tableObj.rows.length;
	var newRowObj = tableObj.insertRow(rowIdx);
	newRowObj.align = "right";

	var objIdx = rowIdx - 1;
	var cellData = [
		{id: null, html: rowIdx + '<input type="hidden" name="adjust-' + objIdx + '-prodId" id="adjust-' + objIdx + '-prodId" value="' + $("#prodId").val() +'"/>'},
		{id: 'adjust-' + objIdx + '-prodNme', html: $("#prodNme").val()},
		{id: 'adjust-' + objIdx + '-qty', html: $("#qty").val()},
		{id: 'adjust-' + objIdx + '-unit', html: $("#unit").val()},
		{id: null, html: '<input type="text" name="adjust-' + objIdx + '-num" id="adjust-' + objIdx + '-num" size="5" value="0" onblur="adjustQty(this)" onkeyup="convertNum(this)" class="num"/>'},
		{id: 'adjust-' + objIdx + '-result', html: $("#qty").val()}
	];
	for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
		var newCellObj = newRowObj.insertCell(cellIdx);
		if (cellIdx == 0) newCellObj.align = 'center';
		if (cellIdx == 1) newCellObj.align = 'left';
		newCellObj.innerHTML = cellData[cellIdx].html;
		if (cellData[cellIdx].id != null) {
			newCellObj.id = cellData[cellIdx].id;
		}
	}
	
	$("#prodId").val('');
	$("#prodNme").val('');
	$("#qty").val('');
	$("#unit").val('');
	
	document.getElementById('adjust-' + objIdx + '-num').focus();
	document.getElementById('adjust-' + objIdx + '-num').select();
}

function prodOnBlur(obj) {
	if(obj.value == '') {
		$("#prodId").val("");
		$("#unit").html("");
		$("#qty").val("");
	}
}

function getBlankRowIdx() {
	var tableObj = document.getElementById("details");
	for (var idx = tableObj.rows.length - 1; idx > 0; idx--) {
		var objIdx = idx - 1;
		var prodId = $("#adjust-" + objIdx + "-prodId").val();
		if (prodId == '') {
			return objIdx;
		}
	}
	return -1;
}

function adjustQty(obj) {
	var objIdx = getObjIdx(obj.id);
	var adjust = obj.value;
	var qty = $("#adjust-" + objIdx + "-qty").html();
	
	if (adjust == '') {
		$("#adjust-" + objIdx + "-result").html(qty);
	} else {
		if (!isDecimal(adjust)) {
			alert('請輸入數字');
			obj.value = 0;
			return;
		}
		adjust = parseFloat(adjust);
		qty = parseFloat(qty);
		var result = adjust + qty;
		if (result < 0) {
			alert('調整後庫存量將變成負數: ' + result);
			obj.value = 0;
			return;
		}

		$("#adjust-" + objIdx + "-result").html(result.toFixed(2));
	}
}

function checkAdjustQty(objIdx) {
	var qty = $("#adjust-" + objIdx + "-qty").html();
	var adjust = $("#adjust-" + objIdx + "-num").val();
	var result = parseInt(adjust, 10) + parseInt(qty, 10);
	
	if (result < 0) {
		alert('調整後庫存量將變成負數: ' + result);
		return false;
	}
}

function go(fm) {
	var tableObj = document.getElementById("details");
	var rowCount = tableObj.rows.length;
	
	var count = 0;
	for (var i = 1; i < rowCount; i++) {
		var objIdx = i - 1;
		var prodId = $("#adjust-" + objIdx + "-prodId").val();
		if (prodId != '') {
			count++;

			if (checkAdjustQty(objIdx) == false) {
				document.getElementById("adjust-" + objIdx + "-num").focus();
				return false;
			}
	
			
			var qty = $("#adjust-" + objIdx + "-qty").html();
			var adjust = $("#adjust-" + objIdx + "-num").val();
			var result = parseInt(qty, 10) + parseInt(adjust, 10);
			
			$("#adjust-" + objIdx + "-result").val(result);
		} else {
			$("#adjust-" + objIdx + "-num").val("");
			$("#adjust-" + objIdx + "-result").html("");
		}
	}
	
	if (count == 0) {
		alert('請輸入至少一筆資料');
		return false;
	}

	fm.detailCount.value = count;	
	fm.submit();
}

</script>
</body>
</html>