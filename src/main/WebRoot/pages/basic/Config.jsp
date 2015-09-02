<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<%@ include file="/pages/inc/head.jsp"%> 
<title><bean:message key="all.func.config.1"/></title>
</head>

<body>
<bean:define id="form" name="ConfigForm"/>

<table cellspacing="0" cellpadding="0" border="0" width="100%" style="margin-top: 1px;">
<tr>
<td align="left" valign="top">
<H2 class="funcheader">功能：<bean:message key="all.func.config.1"/></H2>
<%@ include file="/pages/inc/title.jsp"%> 

<!--
<div id="transbar" align="right" width="100%">
<div id="google_translate_element"></div>
<script>function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'zh-TW',includedLanguages: 'en,vi'}, 'google_translate_element');}</script>
<script src="http://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
</div>
<br>
-->

<table>
<tr>
<td width="400" valign="top">

<table class="grid" cellspacing="0" cellpadding="5" border="1" width="100%">
<thead><tr><th align="left"><bean:message key="all.func.config.1.2"/></th></tr></thead>
<tbody>
<tr>
<td>
<html:form action="config" method="post">
<html:hidden property="state" value="modifyOfferMemo"/>
<div>
<bean:message key="all.column.config.3"/>：
</div>
<br>
<html:text property="offerMemo" size="40"/> <input type="submit" value="<bean:message key="all.btn.3"/>"/>
</html:form>
</td>
</tr>
</tbody>
</table>

</td>
<td width="400" valign="top">

<table class="grid" cellspacing="0" cellpadding="5" border="1" width="100%">
<thead><tr><th align="left"><bean:message key="all.func.config.1.1"/></th></tr></thead>
<tbody><tr>
<td>
<html:form action="config" method="post" onsubmit="return modify()">
<html:hidden property="state" value="modifyUnits"/>
<div>
<input type="submit" value="<bean:message key="all.btn.3"/>"/>
</div><br>
<table class="grid" id="units" cellspacing="0" cellpadding="1" border="1" width="200">
<thead><tr>
<th width="30"><bean:message key="all.btn.2"/></th>
<th width="40"><bean:message key="all.column.config.1"/></th>
<th width="70"><bean:message key="all.column.config.2"/></th>
</tr></thead>
<tbody>
<logic:iterate id="unit" name="form" property="units" indexId="idx">
<tr align="center">
<td><input type="checkbox" name="unitDeleteIdx" id="unitIdx-<%=idx.toString()%>" value='<%=idx.toString()%>' onClick="disableUnitLabel(this, <%=idx.toString()%>)"></td>
<td><bean:write name="unit" property="value"/></td>
<td>
<html:text name="unit" property="label" styleId='<%="unitLabel-" + idx.toString()%>' size="8" onchange="onLabelChange(this)"/>
<input type="hidden" name="label1" id="unitLabel1-<%=idx.toString()%>" value='<bean:write name="unit" property="label"/>'/>
</td>
</tr>
</logic:iterate>
</tbody></table>
</html:form>
</td>
</tr></tbody>
</table>

</td>
</tr>
</table>

</td>
</tr>
</table>

<script type="text/javascript">
<%@ include file="/pages/inc/message.js"%>

$(document).ready(function(){
	addUnitRow();	
});

function getObjIdx(id) {
	return parseInt(id.substring(id.indexOf("-") + 1), 10);
}

function disableUnitLabel(obj, idx) {
	if (obj.checked) {
		$("#unitLabel-" + idx).val($("#unitLabel1-" + idx).val());
		disableObj('unitLabel-' + idx);
	} else {
		enableObj('unitLabel-' + idx);
	}
}

function onLabelChange(obj) {
	var objIdx = getObjIdx(obj.id);
	if (obj.value.trim() == '') {
		if (document.getElementById("unitIdx-" + objIdx) != null) {
			alert('原先已儲存的資料, 若要清除設定, 請勾選刪除');
			obj.value = $("#unitLabel1-" + objIdx).val();
			return;
		}
		var blankIdx = getBlankObjIdx();
		if (blankIdx >= 0 && blankIdx != objIdx) {
			deleteUnitRow(blankIdx);
		}
	} else {
		addUnitRow();
	}
}

function addUnitRow(){
	if (getBlankObjIdx() >= 0) // 若目前已有空的資料時, 則不再新增一筆空的資料
		return;

	var tableObj = document.getElementById("units");
	var rowIdx = tableObj.rows.length;
	var newRowObj = tableObj.insertRow(rowIdx);
	newRowObj.align = "center";
	
	var objIdx = rowIdx - 1;
	var cellData = [
		{id: null, html: ''},
		{id: null, html: ''},
		{id: null, html: '<input type="text" name="label" size="8" value="" id="unitLabel-' + objIdx + '" onblur="onLabelChange(this)"/>'}
	];
	for (var cellIdx = 0; cellIdx < cellData.length; cellIdx++) {
		var newCellObj = newRowObj.insertCell(cellIdx);
		newCellObj.innerHTML = cellData[cellIdx].html;
		if (cellData[cellIdx].id != null) {
			newCellObj.id = cellData[cellIdx].id;
		}
	}
}

function deleteUnitRow(objIdx) {
	var tableObj = document.getElementById("units");
	if (tableObj.rows.length > 1) {
		tableObj.deleteRow(objIdx + 1);
	}
}

function getBlankObjIdx() {
	var tableObj = document.getElementById("units");
	for (var idx = tableObj.rows.length - 1; idx > 0; idx--) {
		var objIdx = idx - 1;
		var chkboxObj = document.getElementById("unitIdx-" + objIdx);
		var labelObj = document.getElementById("unitLabel-" + objIdx);
		if (chkboxObj != null) {
			if (chkboxObj.checked == false && labelObj.value.trim() == '')
				return objIdx;
		} else {
			if (labelObj.value.trim() == '')
				return objIdx;
		}
	}
	return -1;
}

function modify() {
	var modifyCnt = 0;
	var tableObj = document.getElementById("units");
	for (var idx = 1; idx < tableObj.rows.length; idx++) {
		var objIdx = idx - 1;
		var chkboxObj = document.getElementById("unitIdx-" + objIdx);
		var labelObj  = document.getElementById("unitLabel-" + objIdx);
		var label1Obj = document.getElementById("unitLabel1-" + objIdx);
		
		if ((chkboxObj != null && chkboxObj.checked == true) ||
				(label1Obj != null && labelObj.value != label1Obj.value) ||
				(chkboxObj == null && labelObj.value.trim() != '')) {
			modifyCnt++;
		} 
	}
	if (modifyCnt == 0) {
		alert('未做任何修改');
		return false;
	}
	
	return true;
}

</script>
</body>
</html>