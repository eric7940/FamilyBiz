<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.offer.3"/></title>
<script type="text/javascript" src="<c:url value="/scripts/tigra/calendar_zh.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.autocomplete.js"/>"></script>
<script language="JavaScript">
function qryDiscountOnClickDate() {
alert('arguments[0]'+arguments[0]+'\narguments[1]'+arguments[1]+'\narguments[2]'+arguments[2]);
}
</script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.autocomplete.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/tigra/calendar.css"/>">
</head>

<body>
<html:form action="offerQry" method="post" onsubmit="return false">
<html:hidden property="state" value=""/>
<bean:define id="form" name="OfferQryForm"/>
<bean:define name="form" property="deliveryUsers" id="deliveryUsers" />

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.offer.3"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<h3><a name="qryPrice"></a>查詢最新商品售價：</h3>
<p>
<table cellspacing="0" cellpadding="0" border="0" width="600">
<tr>
<td>
客戶：
<input type="text" id="qryPriceCustNme" size="25" onBlur="qryPriceCustOnBlur(this)" /><input type="hidden" id="qryPriceCustId" />
</td>
<td>
產品：
<input type="text" id="qryPriceProdNme" size="35" disabled="disabled" /><input type="hidden" id="qryPriceProdId" />
</td>
</tr>
</table>

<h3>查詢結果：</h3>
<ol id="qryPriceResult"></ol>

<hr>

<h3><a name="qryProdOffers"></a>查詢近半年商品出貨記錄：</h3>
<p>
<table cellspacing="0" cellpadding="0" border="0" width="600">
<tr>
<td>
客戶：
<input type="text" id="qryProdOffersCustNme" size="25" onBlur="qryProdOffersCustOnBlur(this)" /><input type="hidden" id="qryProdOffersCustId" />
</td>
<td>
產品：
<input type="text" id="qryProdOffersProdNme" size="35" disabled="disabled" /><input type="hidden" id="qryProdOffersProdId" />
</td>
</tr>
</table>

<h3>查詢結果：</h3>
<ol id="qryProdOffersResult"></ol>

<hr>

<h3><a name="qrySale"></a>查詢當月銷售狀況：</h3>
<p>
<table cellspacing="0" cellpadding="0" border="0" width="600">
<tr>
<td>
查詢月份：<input type="text" id="qrySaleDate" name="qrySaleDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'qrySaleDate'});</script>
產品：
<input type="text" id="qrySaleProdNme" size="35" onBlur="qrySaleProdOnBlur(this)"/>
</td>
</tr>
</table>

<h3>查詢結果：</h3>
<ol id="qrySaleResult"></ol>

<hr>

<h3><a name="qryUserOffers"></a>查詢當日送貨記錄：</h3>
<p>
<table cellspacing="0" cellpadding="0" border="0" width="600">
<tr>
<td>
查詢日期：<input type="text" id="qryUserOffersDate" name="qryUserOffersDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'qryUserOffersDate'});</script>
物流士：
<select name="deliveryUserId" class="select" onChange="qryUserOffersOnChange(this)">
<logic:iterate id="deliveryUser" name="form" property="deliveryUsers">
<option value='<bean:write name="deliveryUser" property="userId"/>'><bean:write name="deliveryUser" property="userNme" /></option>
</logic:iterate>
</select>
</td>
</tr>
</table>

<h3>查詢結果：</h3>
<ol id="qryUserOffersResult"></ol>

<hr>

<h3><a name="qrySale"></a>查詢當月折讓金額：</h3>
<p>
<table cellspacing="0" cellpadding="0" border="0" width="600">
<tr>
<td>
查詢月份：<input type="text" id="qryDiscountDate" name="qryDiscountDate" size="9"/><script language="JavaScript">new tcal ({'controlname': 'qryDiscountDate'});</script>
<input type="button" value="<bean:message key="all.btn.4"/>" onclick="queryDiscount();"/>
</td>
</tr>
</table>

<h3>查詢結果：</h3>
<dl id="qryDiscountResult"></dl>
<dl id="qryDiscountCustResult"></dl>
</td>
</tr>
</table>

</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

var custs = [
<logic:iterate name="form" property="custs" id="cust">
	{ id: '<bean:write name="cust" property="custId"/>', name: '<bean:write name="cust" property="custNme"/>' },
</logic:iterate>
];

var prods = [
<logic:iterate name="form" property="prods" id="prod">
	{ id: '<bean:write name="prod" property="prodId"/>', name: '<bean:write name="prod" property="prodNme"/>' },
</logic:iterate>
];

$().ready(function() {
	$("#qrySaleDate").val(getToday());
	$("#qryUserOffersDate").val(getToday());
	$("#qryDiscountDate").val(getToday());
	$("#qryPriceCustNme").autocomplete(custs, {
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
	$("#qryPriceProdNme").autocomplete('<html:rewrite page="/offerQry.do"/>', {
		matchContains: true,
		minChars: 0,
		width: 263,
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
			state: 'qryPrice',
			custId: function() {
				return $("#qryPriceCustId").val();
			}
		}
	});
	$("#qryProdOffersCustNme").autocomplete(custs, {
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
	$("#qryProdOffersProdNme").autocomplete(prods, {
		matchContains: true,
		minChars: 0,
		width: 263,
		cacheLength: 1,
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
	$("#qrySaleProdNme").autocomplete(prods, {
		matchContains: true,
		minChars: 1,
		width: 263,
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
	$("#qryPriceCustNme").result(function(event, data, formatted) {
		$("#qryPriceCustId").val("");
		if (data) {
			$("#qryPriceCustId").val(data.id);
			$("#qryPriceProdNme").removeAttr("disabled");
		}
	});
	$("#qryPriceProdNme").result(function(event, data, formatted) {
		$("#qryPriceProdId").val("");
		if (data) {
			$("<li>").html( !data ? "查無符合資料！": $("#qryPriceCustNme").val() + " [" + data[1] + "] 售價：$" + data[2]).appendTo("#qryPriceResult");
			$("#qryPriceProdId").val(data[0]);
		}
	});
	$("#qryProdOffersCustNme").result(function(event, data, formatted) {
		if (data) {
			$("#qryProdOffersCustId").val(data.id);
			$("#qryProdOffersProdNme").removeAttr("disabled");
		}
	});
	$("#qryProdOffersProdNme").result(function(event, data, formatted) {
		if (data) {
			$.ajax({
				url: '<html:rewrite page="/offerQry.do"/>',
				data: ({state: "qryProdOffers", custId: $("#qryProdOffersCustId").val(), prodId: data.id}),
				success: function(result){
					if (result == '') {
						$("<li>").html($("#qryProdOffersCustNme").val() + " - " + data.name + " 出貨記錄：查無符合資料！").appendTo("#qryProdOffersResult");
					} else {
						$("<li>").html($("#qryProdOffersCustNme").val() + " - " + data.name + " 出貨記錄：").appendTo("#qryProdOffersResult");
						var r = result.split('\n');
						for(var i = 0; i < r.length; i++) {
							if (!r[i]) continue;
							var d = r[i].split('|');
							$("<ol>").html("<a href='<html:rewrite page="/offer.do"/>" + "?state=initQuery&id=" + d[0] + "' target='_blank'>" + d[0] + "</a> " + d[1] + " " + $("#qryProdOffersCustNme").val() + " [" + d[3] + "] 數量：" + d[4] + d[5] + " 單價：" + d[6] + " 金額：$" + d[7]).appendTo("#qryProdOffersResult");
						}
					
					}
				}
			});
		}
	});
	$("#qrySaleProdNme").result(function(event, data, formatted) {
		if (data) {
			$.ajax({
				url: '<html:rewrite page="/offerQry.do"/>',
				data: ({state: "qrySale", prodId: data.id, month: $("#qrySaleDate").val()}),
				success: function(result){
					if (result == '') {
						$("<li>").html(data.name + " 當月銷售：查無符合資料！").appendTo("#qrySaleResult");
					} else {
						$("<li>").html(data.name + " 當月銷售：").appendTo("#qrySaleResult");
						var r = result.split('\n');
						for(var i = 0; i < r.length; i++) {
							if (!r[i]) continue;
							var d = r[i].split('|');
							$("<ol>").html(d[1] + "：" + d[2] + " " + d[3]).appendTo("#qrySaleResult");
						}
					}
				}
			});
		}
	});
});

function qryPriceCustOnBlur(obj) {
	if(obj.value == '') {
		$("#qryPriceCustId").val('');
		$("#qryPriceProdNme").attr("disabled", "disabled");
		$("#qryPriceProdNme").val("");
		$("#qryPriceProdId").val("");
	}
}

function qryProdOffersCustOnBlur(obj) {
	if(obj.value == '') {
		$("#qryProdOffersCustId").val('');
		$("#qryProdOffersProdNme").attr("disabled", "disabled");
		$("#qryProdOffersProdNme").val("");
		$("#qryProdOffersProdId").val("");
	}
}

function qrySaleProdOnBlur(obj) {
	if(obj.value == '') {
		$("#qrySaleResult").html('');
	}
}

function qryUserOffersOnChange(obj) {
	if(obj.value != '-') {
		$.ajax({
			url: '<html:rewrite page="/offerQry.do"/>',
			data: ({state: "qryUserOffers", userId: obj.value, date: $("#qryUserOffersDate").val()}),
			success: function(result){
				if (result == '') {
					$("<li>").html(obj.options[obj.selectedIndex].text + " " + $("#qryUserOffersDate").val() + " 當日送貨記錄：查無符合資料！").appendTo("#qryUserOffersResult");
				} else {
					$("<li>").html(obj.options[obj.selectedIndex].text + " " + $("#qryUserOffersDate").val() + " 當日送貨記錄：").appendTo("#qryUserOffersResult");
					var r = result.split('\n');
					for(var i = 0; i < r.length; i++) {
						if (!r[i]) continue;
						var d = r[i].split('|');
						$("<ol>").html("<a href='<html:rewrite page="/offer.do"/>" + "?state=initQuery&id=" + d[0] + "' target='_blank'>" + d[0] + "</a>：" + d[2] + " " + d[3]).appendTo("#qryUserOffersResult");
					}
				}
			}
		});
	}
}

function queryDiscount() {
	$.ajax({
		url: '<html:rewrite page="/offerQry.do"/>',
		data: ({state: "qryDiscount", date: $("#qryDiscountDate").val()}),
		success: function(result){
			$("#qryDiscountResult").html("");
			$("#qryDiscountCustResult").html("");
			if (result == '') {
				$("<ol>").html($("#qryDiscountDate").val() + " 當月查無出貨單資料！").appendTo("#qryDiscountResult");
			} else {
				var r = result.split('\n');
				$("#qryDiscountResult").append($("<dt>").html($("#qryDiscountDate").val() + " 當月折讓金額：" + r[0]));
				var ol = "<ol>";
				for(var i = 1; i < r.length; i++) {
					if (!r[i]) continue;
					var d = r[i].split('|');
					ol += "<li><a href='javascript:void(0)' onclick='queryDiscountCust(" + d[0] + ", \"" + d[1] + "\")'>" + d[1] + "</a>：" + d[2] + "</li>";
				}
				ol += "</ol>";
				$("#qryDiscountResult").append($("<dd>").html(ol));
			}
		}
	});
}

function queryDiscountCust(id, name) {
	$.ajax({
		url: '<html:rewrite page="/offerQry.do"/>',
		data: ({state: "qryDiscountCust", date: $("#qryDiscountDate").val(), id: id}),
		success: function(result){
			if (result != '') {
				$("#qryDiscountCustResult").html("");
				$("#qryDiscountCustResult").append($("<dt>").html(name + " " + $("#qryDiscountDate").val() + " 當月折讓金額："));
				var r = result.split('\n');
				var ul = "<ul>";
				for(var i = 0; i < r.length; i++) {
					if (!r[i]) continue;
					var d = r[i].split('|');
					ul += "<li><a href='<html:rewrite page="/offer.do"/>" + "?state=initQuery&id=" + d[0] + "' target='_blank'>" + d[0] + "</a>：" + d[1] + "</li>";
				}
				ul += "</ul>";
				$("#qryDiscountCustResult").append($("<dd>").html(ul));
			}
		}
	});
}
</script>
</body>
</html>