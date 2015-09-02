<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.offer.1.1"/></title>
</head>

<body>
<html:form action="offer" method="post" onsubmit="return false;">
<html:hidden property="state" value=""/>
<bean:define id="form" name="OfferForm"/>

<table class="popup grid" id="offers" cellspacing="0" cellpadding="1" border="1">
<tr align="center">
  <th width="15">&nbsp;</th>
  <th width="100"><bean:message key="all.column.cust.2"/></th>
  <th width="70"><bean:message key="all.column.offer.3"/></th>
  <th width="70"><bean:message key="all.column.offer.2"/></th>
  <th width="50"><bean:message key="all.column.offer.8"/></th>
  <th width="100"><bean:message key="all.column.offer.10"/></th>
</tr>
	
<logic:iterate id="offer" name="form" property="offers" indexId="idx">
<bean:define id="cust" name="offer" property="cust"/>
<bean:define id="deliveryUser" name="offer" property="deliveryUser"/>
<bean:size id="detailCount" name="offer" property="details" />
<tr align="center">
  <td>
  <input type="radio" name="selectIdx" value='<%=idx.toString()%>' class="radio">
  <html:hidden name="offer" property="id"/>
  <input type="hidden" name="offerDate" value='<fmt:formatDate value="${offer.offerDate}" pattern="yyyy/MM/dd"/>'>
  <html:hidden name="offer" property="invoiceNbr"/>
  <html:hidden name="offer" property="amt" styleId='<%="amt-" + idx.toString() %>'/>
  <html:hidden name="offer" property="discount"/>
  <html:hidden name="offer" property="total"/>
  <html:hidden name="offer" property="receiveAmt"/>
  <html:hidden name="offer" property="memo"/>

  <html:hidden name="cust" property="custId"/>
  <html:hidden name="cust" property="custNme"/>
  <html:hidden name="deliveryUser" property="userNme"/>
  <html:hidden name="cust" property="bizNo"/>
  <html:hidden name="cust" property="tel"/>
  <html:hidden name="cust" property="deliverAddr"/>
  <input type="hidden" name="custMemo" value='<c:out value="${cust.memo}"/>'>
  <input type="hidden" name="detailCount" value="<%=detailCount%>">
  </td>
  <td align="left"><bean:write name="cust" property="custNme"/></td>
  <td><bean:write name="offer" property="id"/></td>
  <td><fmt:formatDate value="${offer.offerDate}" pattern="yyyy/MM/dd"/></td>
  <td align="right"><bean:write name="offer" property="discount"/></td>
  <td align="right"><bean:write name="offer" property="total"/></td>

<logic:iterate id="detail" name="offer" property="details" indexId="detailIdx">
  <logic:equal name="detailIdx" value="0"></tr></logic:equal>

<bean:define id="product" name="detail" property="prod"/>
  <html:hidden name="detail" property="qty" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-qty" %>' />
  <html:hidden name="detail" property="amt" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-amt" %>' />
  <html:hidden name="product" property="prodId" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-prodId" %>' />
  <html:hidden name="product" property="prodNme" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-prodNme" %>' />
  <html:hidden name="product" property="unit" styleId='<%="detail-" + idx.toString() + "-" + detailIdx.toString() + "-unit" %>' />
  <input type="hidden" name="price" id="detail-<%=idx.toString() %>-<%=detailIdx.toString() %>-price" value='<fmt:parseNumber value="${detail.amt/detail.qty}" integerOnly="true"/>' >
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
		alert("請選擇一筆出貨記錄");
		fm.selectIdx[0].focus();
		return false;
	}

	var rtnValue = "";
	rtnValue += "masterId="			+ getValue(fm,'id',idx) + "\n";
	rtnValue += "offerDate="		+ getValue(fm,'offerDate',idx) + "\n";
	rtnValue += "invoiceNbr="		+ getValue(fm,'invoiceNbr',idx) + "\n";
	rtnValue += "amt="				+ parseFloat(getValueById('amt-' + idx)).toFixed(2) + "\n";
	rtnValue += "discount="			+ parseFloat(getValue(fm,'discount',idx)).toFixed(2) + "\n";
	rtnValue += "total="			+ parseFloat(getValue(fm,'total',idx)).toFixed(2) + "\n";
	rtnValue += "receiveAmt="		+ parseFloat(getValue(fm,'receiveAmt',idx)).toFixed(2) + "\n";
	rtnValue += "memo="				+ getValue(fm,'memo',idx) + "\n";

	rtnValue += "custId="			+ getValue(fm,'custId',idx) + "\n";
	rtnValue += "custNme="			+ getValue(fm,'custNme',idx) + "\n";
	rtnValue += "deliveryUserNme="	+ getValue(fm,'userNme',idx) + "\n";
	rtnValue += "bizNo="			+ getValue(fm,'bizNo',idx) + "\n";
	rtnValue += "tel="				+ getValue(fm,'tel',idx) + "\n";
	rtnValue += "deliverAddr="		+ getValue(fm,'deliverAddr',idx) + "\n";
	rtnValue += "custMemo="			+ getValue(fm,'custMemo',idx) + "\n";
		
	rtnValue += "detailCount=" + getValue(fm,'detailCount',idx) + "\n";
	
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