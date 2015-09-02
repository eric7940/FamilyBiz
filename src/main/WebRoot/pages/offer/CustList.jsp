<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.offer.2.1"/></title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
</head>

<body>
<html:form action="offer" method="post" onsubmit="return false">
<html:hidden property="state" value=""/>
<bean:define id="form" name="OfferForm"/>

<table class="popup grid" cellspacing="0" cellpadding="1" border="1">
<tr>
<th width="150"><bean:message key="all.column.cust.2"/></th><td width="500" colspan="3"><input type="text" id="custNme" size="35" onBlur="custOnBlur(this)" /><input type="hidden" id="custId" /></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.cust.4"/></th><td width="225" id="tel">&nbsp;</td>
<th width="100"><bean:message key="all.column.cust.3"/></th><td width="225" id="bizNo">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.5"/></th><td colspan="3" id="deliverAddr">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.cust.6"/></th><td colspan="3" id="memo">&nbsp;</td>
</tr>
</table>

<p align="center"> 
<input type="button" value="<bean:message key="all.btn.5"/>" onClick="go(this.form)">
<input type="button" value="<bean:message key="all.btn.6"/>" onClick="window.close();">
</p>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var custs = [
<logic:iterate name="form" property="custs" id="cust">
	{ 
	  id: '<bean:write name="cust" property="custId"/>',
	  name: '<bean:write name="cust" property="custNme"/>',
	  bizNo: '<bean:write name="cust" property="bizNo"/>',
	  tel: '<bean:write name="cust" property="tel"/>',
	  deliverAddr: '<bean:write name="cust" property="deliverAddr"/>',
	  memo: '<bean:write name="cust" property="memo"/>'
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
			$("#bizNo").html(data.bizNo + '&nbsp;');
			$("#tel").html(data.tel + '&nbsp;');
			$("#deliverAddr").html(data.deliverAddr + '&nbsp;');
			$("#memo").html(data.memo + '&nbsp;');
		}
	});
});

function custOnBlur(obj) {
	if(obj.value == '') {
		$("#custId").val('');
	}
}

function go(fm){
	var custId = $("#custId").val();
	if (custId == '') {
		alert("請選擇客戶");
		document.getElementById("custNme").focus();
		return false;
	}

	var rtnValue = "";
	rtnValue += "custId="      + custId + "\n";
	rtnValue += "custNme="     + $("#custNme").val() + "\n";
	rtnValue += "bizNo="       + $("#bizNo").html() + "\n";
	rtnValue += "tel="         + $("#tel").html() + "\n";
	rtnValue += "deliverAddr=" + $("#deliverAddr").html() + "\n";
	rtnValue += "custMemo="    + $("#memo").html() + "\n";
		
	window.returnValue = rtnValue;
	window.close();
}

</script>
</body>
</html>