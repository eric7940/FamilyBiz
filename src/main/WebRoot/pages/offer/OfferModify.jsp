<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fb.vo.OfferDetailVO" %>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<bean:message key="all.func.offer.6"/>
</title>
<script type="text/javascript" src="<c:url value="/scripts/tigra/calendar_zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/tigra/calendar.css"/>">
</head>

<body>
<html:form action="offer" method="post" onsubmit="return false;">
<html:hidden property="state" value="modify"/>
<bean:define id="form" name="OfferForm"/>
<bean:define id="cust" name="form" property="cust"/>
<bean:define id="deliveryUsers" name="form" property="deliveryUsers"/>
<bean:size id="sizeOfDetails" name="form" property="details"/>
<input type="hidden" name="detailCount" value="<bean:write name="sizeOfDetails"/>"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.offer.6"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
-->

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.3"/>" onclick="go(this.form)">
<input type="button" value="<bean:message key="all.btn.6"/>" onclick="cancel()">
</div>
物流士：<html:select property="deliveryUserId" styleId="deliveryUserId" styleClass="select"><html:options collection="deliveryUsers" property="userId" labelProperty="userNme" /></html:select>
<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.cust.2"/></th><td width="150"><bean:write name='cust' property='custNme'/>&nbsp;</td>
<th width="100"><bean:message key="all.column.offer.1"/></th><td width="100" id="custId"><bean:write name='cust' property='custId'/></td>
<th width="100"><bean:message key="all.column.offer.2"/></th><td width="100"><html:text styleId="offerDate" property="offerDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'offerDate'});</script></td>
</tr>
<tr>
<th><bean:message key="all.column.cust.3"/></th><td><bean:write name='cust' property='bizNo'/>&nbsp;</td>
<th><bean:message key="all.column.cust.4"/></th><td colspan="3"><bean:write name='cust' property='tel'/>&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.5"/></th><td colspan="3"><bean:write name="cust" property="deliverAddr"/>&nbsp;</td><th><bean:message key="all.column.offer.3"/></th><td><bean:write name="form" property='masterId'/></td>
</tr>
<tr>
<th><bean:message key="all.column.cust.6"/></th><td colspan="3"><bean:write name='cust' property='memo'/>&nbsp;</td><th><bean:message key="all.column.offer.4"/></th><td><html:text styleId="invoiceNbr" property="invoiceNbr" size="10"/></td>
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
<logic:iterate name="form" property="details" id="detail" indexId="idx">
<bean:define id="prod" name="detail" property="prod"/>
<tr align="right">
  <td align="center"><%=(idx + 1)%><input type="hidden" name="detail-<%=idx%>-prodId" id="detail-<%=idx%>-prodId" value="<bean:write name="prod" property="prodId"/>"/></td>
  <td align="left"><bean:write name="prod" property="prodNme"/></td>
  <td><input type="text" name="detail-<%=idx%>-qty" id="detail-<%=idx%>-qty" size="5" value="<bean:write name="detail" property="qty"/>" onblur="computeDetailAmt(this, 'qty')" onkeyup="convertNum(this)" class="num"/></td>
  <td><bean:write name="prod" property="unit"/></td>
  <td>
  <input type="text" name="detail-<%=idx%>-price" id="detail-<%=idx%>-price" size="10" value="<%=((OfferDetailVO)detail).getAmt()/((OfferDetailVO)detail).getQty() %>" onblur="computeDetailAmt(this, 'price')" onkeyup="convertNum(this)" class="num"/>
  <input type="hidden" name="detail-<%=idx%>-cost" id="detail-<%=idx%>-cost" value="<bean:write name="prod" property="cost"/>"/></td>
  <td id="detail-<%=idx%>-amt"><bean:write name="detail" property="amt"/></td>
</tr>
</logic:iterate>
</table>

<table class="sheet" cellspacing="0" cellpadding="1" border="1" width="650">
<tr>
<th width="100"><bean:message key="all.column.offer.8"/></th><td width="150"><html:text styleId="discount" property="discount" onkeyup="convertNum(this);computeDiscount(this);" styleClass="num"/></td>
<th width="100"><bean:message key="all.column.offer.9"/></th><td width="150" id="amt" align="right"><bean:write name='form' property='amt'/></td>
<th width="150" colspan="2">客戶簽收</th>
</tr>
<tr>
<th><bean:message key="all.column.offer.11"/></th><td></td>
<th>營業稅</th><td></td>
<td colspan="2" rowspan="3"></td>
</tr>
<tr>
<th>未清款</th><td></td>
<th><bean:message key="all.column.offer.10"/></th><td id="total" align="right"><bean:write name="form" property="total"/></td>
</tr>
<tr>
<th><bean:message key="all.column.offer.12"/></th><td colspan="3" height="50" valign="top"><html:text styleId="memo" property="memo" size="50"/></td>
</tr>
</table>

<div id="actionbar" align="left" style="width:100%;">
<input type="button" value="<bean:message key="all.btn.3"/>" onclick="go(this.form)">
<input type="button" value="<bean:message key="all.btn.6"/>" onclick="cancel()">
</div>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(document).ready(function(){
	addDetailRow();
});

function getObjIdx(id) {
	return parseInt(id.substring(7, id.indexOf("-", 7)), 10);
}

function addDetailRow(){
	if (getBlankRowIdx() >= 0) // 若目前已有空的資料時, 則不再新增一筆空的資料
		return;

	var tableObj = document.getElementById("details");
	var rowIdx = tableObj.rows.length;
	
	var maxDetailLength = 99;
	if (rowIdx >= maxDetailLength + 1) {
		//alert('一張出貨單只允許填入' + maxDetailLength + '筆出貨明細');
		return;
	}

	var newRowObj = tableObj.insertRow(rowIdx);
	newRowObj.align = "right";

	var objIdx = rowIdx - 1;
	var cellData = [
		{id: null, html: rowIdx},
		{id: null, html: '<input type="text" name="detail-' + objIdx + '-prodNme" id="detail-' + objIdx + '-prodNme" size="35" onBlur="prodOnBlur(this)"/><input type="hidden" name="detail-' + objIdx + '-prodId" id="detail-' + objIdx + '-prodId"/>'},
		{id: null, html: '<input type="text" name="detail-' + objIdx + '-qty" id="detail-' + objIdx + '-qty" size="5" value="1" onblur="computeDetailAmt(this, \'qty\')" onkeyup="convertNum(this)" class="num"/>'},
		{id: 'detail-' + objIdx + '-unit', html: ''},
		{id: null, html: '<input type="text" name="detail-' + objIdx + '-price" id="detail-' + objIdx + '-price" size="10" value="" onblur="computeDetailAmt(this, \'price\')" onkeyup="convertNum(this)" class="num"/><input type="hidden" name="detail-' + objIdx + '-cost" id="detail-' + objIdx + '-cost"/>'},
		{id: 'detail-' + objIdx + '-amt', html: ''}
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
	
	$("#detail-" + objIdx + "-prodNme").autocomplete('<html:rewrite page="/offer.do"/>', {
		matchContains: true,
		minChars: 2,
		width: 260,
		cacheLength: 1,
		scroll: true,
		scrollHeight: 120,
		formatItem: function(row, i, max) {
			return row[1];
		},
		formatMatch: function(row, i, max) {
			return row[1];
		},
		formatResult: function(row) {
			return row[1];
		},
		extraParams: {
			state: 'getProds',
			custId: function() {
				return $("#custId").html();
			}
		}
	});
	
	$("#detail-" + objIdx + "-prodNme").result(function(event, data, formatted) {
		var idx = getObjIdx(this.id);
		$("#detail-" + idx + "-prodId").val("");
		if (data) {
			$("#detail-" + idx + "-prodId").val(data[0]);
			$("#detail-" + idx + "-unit").html(data[3]);
			$("#detail-" + idx + "-price").val(parseFloat(data[2]).toFixed(2));
			$("#detail-" + idx + "-amt").html(parseFloat(data[2]).toFixed(2));
			$("#detail-" + idx + "-cost").val(parseFloat(data[4]).toFixed(2));
			computePrice();
			addDetailRow();
		}
	});
}

function prodOnBlur(obj) {
	if(obj.value == '') {
		var objIdx = getObjIdx(obj.id);
		$("#detail-" + objIdx + "-prodId").val("");
		$("#detail-" + objIdx + "-unit").html("");
		$("#detail-" + objIdx + "-price").val("");
		$("#detail-" + objIdx + "-amt").html("");
		$("#detail-" + objIdx + "-cost").val("");
	}
}

function getBlankRowIdx() {
	var tableObj = document.getElementById("details");
	for (var idx = tableObj.rows.length - 1; idx > 0; idx--) {
		var objIdx = idx - 1;
		var prodId = $("#detail-" + objIdx + "-prodId").val();
		if (prodId == '') {
			return objIdx;
		}
	}
	return -1;
}

function computeDetailAmt(obj, flag) {
	var objIdx = getObjIdx(obj.id);
	if (flag == 'price') {
		var price = obj.value;
		var qty = $("#detail-" + objIdx + "-qty").val();
	} else {
		var qty = obj.value;
		var price = $("#detail-" + objIdx + "-price").val();
	}
	
	if (qty == '' || price == '') {
		$("#detail-" + objIdx + "-amt").html('');
	} else {
		if (flag == 'price') {
			if (!isDecimal(obj.value) || parseFloat(obj.value) < 0) {
				alert('請輸入數字，且不可小於0');
				obj.value = 0;
				return;
			}
		} else {
			if (!isDecimal(obj.value)) {
				alert('請輸入數字');
				obj.value = 1;
				return;
			}
		}
		
		if (flag == 'price' && parseFloat(obj.value) < parseFloat($("#detail-" + objIdx + "-cost").val())) {
			alert('低於目前成本: ' + $("#detail-" + objIdx + "-cost").val());
		}
		
		qty = parseFloat(qty);
		price = parseFloat(price);
		
		$("#detail-" + objIdx + "-price").val(price.toFixed(2));
		$("#detail-" + objIdx + "-amt").html(parseFloat(price * qty).toFixed(2));
	}

	computePrice();
}

function computeDiscount(obj) {
	if (!isDecimal(obj.value) || parseFloat(obj.value) < 0) {
		alert('請輸入數字，且不可為負數');
		obj.value = 0;
		return;
	}
	if(obj.value.indexOf('.') == obj.value.length - 1) {
		obj.value = parseFloat(obj.value) + '.';
	} else {
		obj.value = parseFloat(obj.value);
	}
	computePrice();
}

function computePrice() {
	var tableObj = document.getElementById("details");
	var rowCount = tableObj.rows.length;
	
	var sum = 0;
	for (var i = 1; i < rowCount; i++) {
		var objIdx = i - 1;
		var detailAmt = $("#detail-" + objIdx + "-amt").html();
		if (detailAmt != '' && isDecimal(detailAmt)) {
			detailAmt = parseFloat(detailAmt);
		} else {
			detailAmt = 0;
		}
		sum += detailAmt;
	}
	
	$("#amt").html(parseFloat(sum).toFixed(2));

	var discount = $("#discount").val();
	if (discount != '' && isDecimal(discount)) {
		discount = parseFloat(discount);
	} else {
		discount = 0;
		$("#discount").val(discount);
	}
	
	var total = sum - discount;
	$("#total").html(parseFloat(total).toFixed(2)); 
}

function checkDetailPrice(objIdx) {
	var price = $("#detail-" + objIdx + "-price").val();
	var cost = $("#detail-" + objIdx + "-cost").val();
	
	if (price == '' || !isDecimal(price) || parseFloat(price) < 0) {
		alert('請輸入數字，且不可小於0');
		return false;
	}
			
	if (parseFloat(price) < parseFloat(cost)) {
		alert('低於目前成本: ' + cost);
		//return false;
	}
	
	return true;	
}

function checkDetailQty(objIdx) {
	var qty = $("#detail-" + objIdx + "-qty").val();
	
	if (qty == '' || !isDecimal(qty)) {
		alert('請輸入數字');
		return false;
	}
}

function go(fm) {
	var tableObj = document.getElementById("details");
	var rowCount = tableObj.rows.length;
	
	var count = 0;
	for (var i = 1; i < rowCount; i++) {
		var objIdx = i - 1;
		var prodId = $("#detail-" + objIdx + "-prodId").val();
		if (prodId != '') {
			count++;

			if (checkDetailPrice(objIdx) == false) {
				document.getElementById("detail-" + objIdx + "-price").focus();
				$("#detail-" + objIdx + "-price").val("0");
				return false;
			}
	
			if (checkDetailQty(objIdx) == false) {
				document.getElementById("detail-" + objIdx + "-qty").focus();
				$("#detail-" + objIdx + "-qty").val("1");
				return false;
			}
			
			var qty = $("#detail-" + objIdx + "-qty").val();
			var price = $("#detail-" + objIdx + "-price").val();
			qty = parseFloat(qty);
			price = parseFloat(price);

			$("#detail-" + objIdx + "-price").val(price.toFixed(2));
			$("#detail-" + objIdx + "-amt").html(parseFloat(price * qty).toFixed(2));
		} else {
			$("#detail-" + objIdx + "-price").val("");
			$("#detail-" + objIdx + "-qty").val("");
			$("#detail-" + objIdx + "-amt").html("");
		}
	}
	
	if (count == 0) {
		alert('請輸入至少一筆產品');
		return false;
	}

	computePrice();

	if (!confirm('請再次確認貨單內容是否正確？'))
		return false;
		
	fm.detailCount.value = count;	
	fm.submit();
}

</script>
</body>
</html>