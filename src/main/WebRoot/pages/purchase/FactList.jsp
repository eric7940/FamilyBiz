<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.purchase.2.1"/></title>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
</head>

<body>
<html:form action="purchase" method="post" onsubmit="return false">
<html:hidden property="state" value=""/>
<bean:define id="form" name="PurchaseForm"/>

<table class="popup grid" cellspacing="0" cellpadding="1" border="1">
<tr>
<th width="150"><bean:message key="all.column.fact.2"/></th><td width="500" colspan="3"><input type="text" id="factNme" size="35" onBlur="factOnBlur(this)" /><input type="hidden" id="factId" /></td>
</tr>
<tr>
<th width="100"><bean:message key="all.column.fact.4"/></th><td width="225" id="contact">&nbsp;</td>
<th width="100"><bean:message key="all.column.fact.3"/></th><td width="225" id="bizNo">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.5"/></th><td colspan="3" id="tel">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.6"/></th><td colspan="3" id="addr">&nbsp;</td>
</tr>
<tr>
<th><bean:message key="all.column.fact.7"/></th><td colspan="3" id="memo">&nbsp;</td>
</tr>
</table>

<p align="center"> 
<input type="button" value="<bean:message key="all.btn.5"/>" onClick="go(this.form)">
<input type="button" value="<bean:message key="all.btn.6"/>" onClick="window.close();">
</p>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var facts = [
<logic:iterate name="form" property="facts" id="fact">
	{ 
	  id: '<bean:write name="fact" property="factId"/>',
	  name: '<bean:write name="fact" property="factNme"/>',
	  bizNo: '<bean:write name="fact" property="bizNo"/>',
	  contact: '<bean:write name="fact" property="contact"/>',
	  tel: '<bean:write name="fact" property="tel"/>',
	  addr: '<bean:write name="fact" property="addr"/>',
	  memo: '<bean:write name="fact" property="memo"/>'
	},
</logic:iterate>
];

$().ready(function() {
	$("#factNme").autocomplete(facts, {
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
	$("#factNme").result(function(event, data, formatted) {
		$("#factId").val("");
		if (data) {
			$("#factId").val(data.id);
			$("#bizNo").html(data.bizNo + '&nbsp;');
			$("#contact").html(data.contact + '&nbsp;');
			$("#tel").html(data.tel + '&nbsp;');
			$("#addr").html(data.addr + '&nbsp;');
			$("#memo").html(data.memo + '&nbsp;');
		}
	});
});

function factOnBlur(obj) {
	if(obj.value == '') {
		$("#factId").val('');
	}
}

function go(fm){
	var factId = $("#factId").val();
	if (factId == '') {
		alert("請選擇廠商");
		document.getElementById("factNme").focus();
		return false;
	}

	var rtnValue = "";
	rtnValue += "factId="      + factId + "\n";
	rtnValue += "factNme="     + $("#factNme").val() + "\n";
	rtnValue += "bizNo="       + $("#bizNo").html() + "\n";
	rtnValue += "contact="     + $("#contact").html() + "\n";
	rtnValue += "tel="         + $("#tel").html() + "\n";
	rtnValue += "addr="        + $("#addr").html() + "\n";
	rtnValue += "factMemo="    + $("#memo").html() + "\n";
		
	window.returnValue = rtnValue;
	window.close();
}

</script>
</body>
</html>