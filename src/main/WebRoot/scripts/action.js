window.defaultStatus = document.title;

String.prototype.trim=trim;
function trim(){
	re = /(^\s*)|(\s*$)/g;
	return this.replace(re,"");
}

String.prototype.replaceAll = strReplace;
function strReplace(findText, replaceText) {
   var str = new String(this);
   while (str.indexOf(findText)!=-1) {
      str = str.replace(findText, replaceText);
   }
   return str;
}

function getFormObj(id) {
	return document.getElementById(id);
}

function enableObj(id) {
    var obj = document.getElementById(id);
    if (obj != undefined && obj != null) {
        obj.disabled = false;
    }
}

function disableObj(id) {
    var obj = document.getElementById(id);
    if (obj != undefined && obj != null) {
        obj.disabled = true;
    }
}

function hiddenObj(id) {
	var obj = document.getElementById(id);
	obj.style.display = 'none';
}

function displayObj(id) {
	var obj = document.getElementById(id);
	obj.style.display = 'block';
}

function isHiddenObj(id) {
	var obj = document.getElementById(id);
	return obj.style.display == 'none';
}

function getToday() {
	var date = new Date();
	var year = new String(date.getFullYear());
	var month = new String(date.getMonth() + 1);
	var day = new String(date.getDate());
	if(month.length < 2){
		month = "0" + month;
	}
	if(day.length < 2){
		day = "0" + day;
	}
	return year + "/" + month + "/" + day;
}

function isDate(s) {
	var re_date = /^\s*(\d{2,4})\/(\d{1,2})\/(\d{1,2})\s*$/;
	if (!re_date.exec(s)) {
		//alert ("日期格式錯誤: '" + s + "'.\n請輸入 yyyy/mm/dd.");
		return false;
	}
	var n_day = Number(RegExp.$3),
		n_month = Number(RegExp.$2),
		n_year = Number(RegExp.$1);
	
	if (n_year < 100)
		n_year += (n_year < this.a_tpl.centyear ? 2000 : 1900);
	if (n_month < 1 || n_month > 12) {
		//alert ("月份請輸入 01-12");
		return false;
	}
	var d_numdays = new Date(n_year, n_month, 0);
	if (n_day > d_numdays.getDate()) {
		//alert(n_month + "月份日期應介於 01-" + d_numdays.getDate());
		return false;
	}

	return true;
}

function isInt(theNum) {
	return ((theNum + '').match(/^(-)?\d+$/) != null);
}

function isDecimal(theNum) {
	return ((theNum + '').match(/^(((-)?\d+(\.\d*)?)|((-)?(\d*\.)?\d+))$/) != null);
}
    
//function isNum(s){
//	return isCharsInBag(s, "0123456789");
//}

function convertNum(obj) {
	var charCodeDash = 65123; // 全形﹣
	var charCodeDot  = 65294; // 全形．: 46+65248
	var charCode0    = 65296; // 全形０: 48+65248
	var charCode9    = 65305; // 全形９: 57+65248
	var tmp =  new Array();
	var a = obj.value;
	var b = "";
	for(var i = 0; i < a.length; i++) {
		if ((a.charCodeAt(i) <= charCode9 && a.charCodeAt(i) >= charCode0) || a.charCodeAt(i) == charCodeDot){
			tmp[i] = a.charCodeAt(i) - 65248; //轉半形 (unicode-65248)
		} else if (a.charCodeAt(i) == charCodeDash) {
			tmp[i] = 45;
		} else {
			tmp[i] = a.charCodeAt(i);
		}
		b += String.fromCharCode(tmp[i]);
	}
	obj.value = b;
}

//function isCharsInBag(s, bag){
//	for (var i = 0; i < s.length; i++) { 
//		var c = s.charAt(i);
//		if (bag.indexOf(c) == -1) return false;
//	}
//	return true;
//}

function openDialogWindow(action,id,state,dialogWidth,dialogHeight){
	var src = action + "?state=" + state + "&feeid=" + id;
	
	var screenW = screen.width/2;
	var screenH = screen.height/2;
	var winW = (window.screen.availWidth - 600);
	var winH = (window.screen.availHeight - 280);
	var winL = screenW - (winW/2);
	var winT = screenH - (winH/2); 

	dialogTop = winT;
	dialogLeft = winL;
	
	if(dialogWidth == null && dialogHeight == null){
		dialogWidth = 410;
		dialogHeight = 550;
	}
	
	rtnValue = window.showModalDialog(src, state,
		"dialogTop=" + dialogTop + "px;" + 
		"dialogLeft=" + dialogLeft + "px;" +
		"dialogWidth=" + dialogWidth + "px;" +
		"dialogHeight=" + dialogHeight + "px;" +
		"status=no;scroll=yes;help=no;scrollbars=yes;border=thin;");

	return rtnValue;
}

function openWindow(url,id,dialogWidth,dialogHeight){
	var screenW = screen.width/2;
	var screenH = screen.height/2;
	var winW = (window.screen.availWidth - 600);
	var winH = (window.screen.availHeight - 280);
	var winL = screenW - (winW/2);
	var winT = screenH - (winH/2); 

	dialogTop = winT;
	dialogLeft = winL;
	

	if(dialogWidth == null && dialogHeight == null){
		dialogWidth = 410;
		dialogHeight = 550;
	}
	
	var winObj = window.open(url, id,
		"top=" + dialogTop + "," + 
		"left=" + dialogLeft + "," +
		"width=" + dialogWidth + "," +
		"height=" + dialogHeight + "," +
		"toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no");

	return winObj;
}

function Map() {

	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}
 
 	var put = function(key, value){
  		for (var i = 0; i < this.arr.length; i++) {
   			if ( this.arr[i].key === key ) {
    			this.arr[i].value = value;
                //alert('the same key = ' + key);
    			return;
   			}
  		}
   		this.arr[this.arr.length] = new struct(key, value);
	}
 
 	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if ( this.arr[i].key === key ) {
				return this.arr[i].value;
			}
		}
		return null;
	}
 
	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if ( v.key === key ) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

 	var getKeys = function(){
 		var keys = new Array();
  		for (var i = 0; i < this.arr.length; i++) {
   			keys[keys.length] = this.arr[i].key;
  		}
   		return keys;
	}
 
	var size = function() {
		return this.arr.length;
	}
 
	var isEmpty = function() {
		return this.arr.length <= 0;
	}

	var toString = function() {
		var str = "";
		for (var i = 0; i < this.arr.length; i++) {
			str += "'" + this.arr[i].key + "' : '" + this.arr[i].value + "' ";
		}
		return str;
	}
    

	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.getKeys = getKeys;
	this.size = size;
	this.isEmpty = isEmpty;
	this.toString = toString;
}

