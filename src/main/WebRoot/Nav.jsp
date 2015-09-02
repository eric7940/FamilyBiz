<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/inc/taglib.jsp"%> 
<html>
<head>
<title>FamilyBiz Application</title>
<base target="mainFrame" />
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/style.css"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/jquery/jquery.treeview.css"/>">
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery-1.4.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/scripts/jquery/jquery.treeview.js"/>"></script>
<script>
$(document).ready(function(){
	$("#func").treeview({
		animated: "fast",
		control: "#treecontrol"
	});
});
</script>
</head>

<body>
<bean:define id="form" name="NavForm"/>
<div id="treecontrol" align="right">
<img src="images/jquery/treeview/minus.gif" border="0"/> <a title="全部收回" href="#">全部收回</a>
<img src="images/jquery/treeview/plus.gif" border="0"/> <a title="全部展開" href="#">全部展開</a>
<!-- a title="全部展開/全部收回" href="#">全部展開/全部收回</a-->
</div>
<ul id="func" class="filetree">
<logic:iterate name="form" property="menus" id="menu">
<bean:define id="func" name="menu" property="funcs"/>
<logic:equal name="menu" property="folderFlag" value="O">
<li>
</logic:equal>
<logic:equal name="menu" property="folderFlag" value="C">
<li class="closed">
</logic:equal>
<span class="folder"><bean:write name="menu" property="menuLabel"/></span>
<ul>
<logic:iterate name="menu" property="funcs" id="fn">
  <li><span class="file"><a href="<bean:write name="fn" property="url"/>"><bean:write name="fn" property="funcLabel"/></a></span></li>
</logic:iterate>
</ul>

</logic:iterate>
</ul>

</body>
</html>