<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.purchase.1.1"/></title>
</head>

<body>
<html:form action="purchase" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<bean:define id="form" name="PurchaseForm"/>

<table class="popup grid" id="purchases" cellspacing="0" cellpadding="1" border="1">
<tr align="center">
  <th width="15">&nbsp;</th>
  <th width="70"><bean:message key="all.column.purchase.3"/></th>
  <th width="70"><bean:message key="all.column.purchase.2"/></th>
  <th width="40"><bean:message key="all.column.purchase.8"/></th>
  <th width="30"><bean:message key="all.column.purchase.10"/></th>
  <th width="150"><bean:message key="all.column.prod.2"/></th>
  <th width="40"><bean:message key="all.column.prod.4"/></th>
  <th width="30"><bean:message key="all.column.purchase.6"/></th>
  <th width="40"><bean:message key="all.column.purchase.7"/></th>
</tr>
	
<logic:iterate id="purchase" name="form" property="purchases" indexId="idx">
<bean:define id="fact" name="purchase" property="fact"/>
<bean:size id="detailCount" name="purchase" property="details" />
<tr align="center">
  <td>
  <input type="radio" name="selectIdx" value='<%=idx.toString()%>' class="radio">
  <html:hidden name="purchase" property="id"/>
  <input type="hidden" name="purchaseDate" value='<fmt:formatDate value="${purchase.purchaseDate}" pattern="yyyy/MM/dd"/>'>
  <html:hidden name="purchase" property="invoiceNbr"/>
  <html:hidden name="purchase" property="amt" styleId='<%="amt-" + idx.toString() %>'/>
  <html:hidden name="purchase" property="discount"/>
  <html:hidden name="purchase" property="total"/>
  <html:hidden name="purchase" property="memo"/>

  <html:hidden name="fact" property="factId"/>
  <html:hidden name="fact" property="factNme"/>
  <html:hidden name="fact" property="bizNo"/>
  <html:hidden name="fact" property="contact"/>
  <html:hidden name="fact" property="tel"/>
  <html:hidden name="fact" property="addr"/>
  <input type="hidden" name="factMemo" value='<c:out value="${fact.memo}"/>'>
  <input type="hidden" name="detailCount" value="<%=detailCount%>">
  </td>
  <td><bean:write name="purchase" property="id"/></td>
  <td><fmt:formatDate value="${purchase.purchaseDate}" pattern="yyyy/MM/dd"/></td>
  <td align="right"><bean:write name="purchase" property="discount"/></td>
  <td align="right"><bean:write name="purchase" property="total"/></td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>

<logic:iterate id="detail" name="purchase" property="details" indexId="detailIdx">
  <logic:equal name="detailIdx" value="0"></tr></logic:equal>

<bean:define id="product" name="detail" property="prod"/>
<tr align="right">
  <td colspan="5">
  &nbsp;
  <html:hidden name="detail" property="qty" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-qty" %>' />
  <html:hidden name="detail" property="amt" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-amt" %>' />
  <html:hidden name="product" property="prodId" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-prodId" %>' />
  <html:hidden name="product" property="prodNme" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-prodNme" %>' />
  <html:hidden name="product" property="unit" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-unit" %>' />
  <input type="hidden" name="price" id="detail-<%=idx.toString() %>-<%=detailIdx.toString() %>-price" value='<fmt:parseNumber value="${detail.amt/detail.qty}" integerOnly="true"/>' >
  </td>
  <td align="left"><bean:write name="product" property="prodNme"/></td>
  <td><fmt:parseNumber value="${detail.amt/detail.qty}" integerOnly="true"/></td>
  <td><bean:write name="detail" property="qty"/></td>
  <td><bean:write name="detail" property="amt"/></td>
</tr>
</logic:iterate>
</logic:iterate>

</table>
<p align="center"> 
<input type="button" value="<bean:message key="all.btn.5"/>" onClick="go(this.form)">
<input type="button" value="<bean:message key="all.btn.6"/>" onClick="window.close();">
</p>
</html:form>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

if ('<bean:write name="ERR_CDE" scope="request"/>' != '00') {
	window.returnValue = '';
	window.close();
}

function go(fm){
	var idx = null;
	for (var i = fm.selectIdx.length; --i >= 0; fm.selectIdx[i].checked && eval('idx = fm.selectIdx[' + i + '].value'));
	if (idx == null) {
		alert("請選擇一筆進貨記錄");
		fm.selectIdx[0].focus();
		return false;
	}

	var rtnValue = "";
	rtnValue += "masterId="     + getValue(fm,'id',idx) + "\n";
	rtnValue += "purchaseDate=" + getValue(fm,'purchaseDate',idx) + "\n";
	rtnValue += "invoiceNbr="   + getValue(fm,'invoiceNbr',idx) + "\n";
	rtnValue += "amt="          + parseFloat(getValueById('amt-' + idx)).toFixed(2) + "\n";
	rtnValue += "discount="     + parseFloat(getValue(fm,'discount',idx)).toFixed(2) + "\n";
	rtnValue += "total="        + parseFloat(getValue(fm,'total',idx)).toFixed(2) + "\n";
	rtnValue += "memo="         + getValue(fm,'memo',idx) + "\n";

	rtnValue += "factId="       + getValue(fm,'factId',idx) + "\n";
	rtnValue += "factNme="      + getValue(fm,'factNme',idx) + "\n";
	rtnValue += "bizNo="        + getValue(fm,'bizNo',idx) + "\n";
	rtnValue += "contact="      + getValue(fm,'contact',idx) + "\n";
	rtnValue += "tel="          + getValue(fm,'tel',idx) + "\n";
	rtnValue += "addr="         + getValue(fm,'addr',idx) + "\n";
	rtnValue += "factMemo="     + getValue(fm,'factMemo',idx) + "\n";
		
	rtnValue += "detailCount="  + getValue(fm,'detailCount',idx) + "\n";
	
	var detailCount = fm.detailCount[idx].value;
	for(var i = 0; i < detailCount; i++) {
		var id = "detail-" + idx + "-" + i + "-";
		rtnValue += "detail-" + i + "-qty="     + getValueById(id + "qty") + "\n";
		rtnValue += "detail-" + i + "-amt="     + parseFloat(getValueById(id + "amt")).toFixed(2) + "\n";
		rtnValue += "detail-" + i + "-prodId="  + getValueById(id + "prodId") + "\n";
		rtnValue += "detail-" + i + "-prodNme=" + getValueById(id + "prodNme") + "\n";
		rtnValue += "detail-" + i + "-unit="    + getValueById(id + "unit") + "\n";
		rtnValue += "detail-" + i + "-price="   + parseFloat(getValueById(id + "price")).toFixed(2) + "\n";
	}

	window.returnValue = rtnValue;
	window.close();
}

function getValue(fm, id, idx) {
	var size = fm.selectIdx.length;
	if (size > 1) {
		return document.getElementsByName(id)[idx].value;
	} else {
		return document.getElementsByName(id).value;
	}
}

function getValueById(id) {
	return document.getElementById(id).value;
}
</script>
</body>
</html>