<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title>
<logic:equal name="action" value="create"><bean:message key="all.func.basic.2.1"/></logic:equal>
<logic:equal name="action" value="modify"><bean:message key="all.func.basic.2.2"/></logic:equal>
</title>
</head>

<body>
<html:form action="prod" method="post" onsubmit="return go(this)">
<logic:equal name="action" value="create"><html:hidden property="state" value="create"/></logic:equal>
<logic:equal name="action" value="modify"><html:hidden property="state" value="modify"/><html:hidden property="prodId"/></logic:equal>
<bean:define id="form" name="ProdForm"/>
<bean:define id="units" name="form" property="units"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：
<logic:equal name="action" value="create"><bean:message key="all.func.basic.2.1"/></logic:equal>
<logic:equal name="action" value="modify"><bean:message key="all.func.basic.2.2"/></logic:equal>
</H2>
<%@ include file="/pages/inc/title.jsp"%> 

<p>
<logic:equal name="action" value="create"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modify"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>
<table class="grid" cellspacing="0" cellpadding="1" border="1" width="800">
<tr>
<th width="100"><bean:message key="all.column.prod.2"/></th><td colspan="3"><html:text property="prodNme" size="80" onkeyup="convertNum(this)"/></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.prod.3"/></th><td width="100"><html:select property="unit" styleClass="select"><html:options collection="units" property="label" labelProperty="label" /></html:select></td>
<th width="100"><bean:message key="all.column.prod.4"/></th><td width="100"><html:text property="price" size="10" onkeyup="convertNum(this);processPrice(this);" styleClass="num"/></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.prod.6"/></th><td width="100"><html:text property="saveQty" size="10" onkeyup="convertNum(this);processSaveQty(this);" styleClass="num"/></td>
<th width="100"><bean:message key="all.column.prod.5"/></th><td width="100" align="right"><bean:write name="form" property="cost" /></td>
</tr>
</table>
<p>
<logic:equal name="action" value="create"><input type="submit" value="<bean:message key="all.btn.1"/>"></logic:equal>
<logic:equal name="action" value="modify"><input type="submit" value="<bean:message key="all.btn.3"/>"></logic:equal>
<input type="reset" value="<bean:message key="all.btn.8"/>">
</p>

</td>
</tr>
</table>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

function processPrice(obj) {
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
}

function processSaveQty(obj) {
	if (!isInt(obj.value) || parseInt(obj.value, 10) < 0) {
		alert('請輸入數字，且不可為負數');
		obj.value = 0;
		return;
	}
}

function go(fm) {
	if (fm.prodNme.value.trim() == '') {
		alert('請輸入品名/規格');
		fm.prodNme.focus();
		return false;
	}
	
	var priceObj = fm.price;
	if (priceObj.value != '' && (!isDecimal(priceObj.value) || parseFloat(priceObj.value) < 0)) {
		alert('請輸入數字，且不可為負數');
		priceObj.value = 0;
		priceObj.focus();
		return;
	}
	priceObj.value = parseFloat(priceObj.value).toFixed(2);
	
	var saveQtyObj = fm.saveQty;
	if (saveQtyObj.value != '' && (!isInt(saveQtyObj.value) || parseInt(saveQtyObj.value, 10) < 0)) {
		alert('請輸入數字，且不可為負數');
		saveQtyObj.value = 0;
		saveQtyObj.focus();
		return;
	}
	saveQtyObj.value = parseInt(saveQtyObj.value, 10);
	
	return true;
}
</script>
</body>
</html>