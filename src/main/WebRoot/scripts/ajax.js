var responseMap = new Map();
var httpreq = getHTTPObject();

function getHTTPObject() {
	var xRequest = null;
	if (window.XMLHttpRequest) {
		xRequest = new XMLHttpRequest();
	} else if (typeof ActiveXObject != "undefined") {
		xRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xRequest;
}

function callAjax(URL, callbackMethod, method, async) {
	if (URL == "") return ;
	if (method == null) method = "GET";
	if (async == null) async = true;
	
	httpreq.open(method, URL, async);
	httpreq.onreadystatechange = eval(callbackMethod);
	httpreq.send(null);
}