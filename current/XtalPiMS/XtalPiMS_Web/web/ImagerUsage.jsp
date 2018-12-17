<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Imager usage' />
</jsp:include>
<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="plate.png" />
<c:set var="title" value="Imager usage"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Reporting period" initialState="fixed" id="dates">
<pimsForm:form action="#" mode="edit" method="get">
<pimsForm:formBlock>
	<pimsForm:column1>
		<pimsForm:text name="startdate" alias="Start date" value="2015-01-01" />
		<pimsForm:text name="enddate" alias="End date" value="2015-01-31" />
	</pimsForm:column1>
	<pimsForm:column2>
		<div id="imagercheckboxes">Getting imager list...</div>
	</pimsForm:column2>
</pimsForm:formBlock>
<pimsForm:formBlock>
	<pimsForm:column1>
		<input type="button" value="Get usage data" id="getusagedata" disabled="disabled" onclick="getUsageData()"/>
	</pimsForm:column1>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

<style type="text/css">
.box {border:1px solid black; margin:2px; }
.box .boxhead  { background:#ccc; font-weight:bold; }
.box .boxcontrol{ float:left; width:1em; margin-left:0.5em;font-weight:bold; font-size:120%; cursor:pointer; } 
.box .boxheadtext { font-weight:bold; font-size:120%; margin:0; } 
.box .boxbody { border-top:1px solid black; font-family:verdana,arial,helvetica,sans-serif; }
.closed .boxbody { display:none; }
.boxbody .box {margin-left:2em; }
.boxbody .platebar { padding:0.25em 1em; text-align:right; }
.boxbody .platebar+.platebar { border-top:1px solid #666; }
</style>

<script type="text/javascript">
var formulatrixBaseUrl="${formulatrixBaseUrl}";
var contextPath="${pageContext.request['contextPath']}";

var imagers;
var startDate;
var endDate;
var imagerSerials;
var currentDate;
var currentImager;


function doBoxControl(ctrl){
	var box=ctrl.parentNode.parentNode;
	if(box.className.contains("closed")){
		box.className="box";
		ctrl.innerHTML="-";
	} else {
		box.className="box closed";
		ctrl.innerHTML="+";
	}
}

function box(id, header,content, isOpen, parentElement){
	var html='';
	var openClass="";
	var control="-";
	if(!isOpen){ 
		openClass="closed"; 
		control="+";
	}
	if(!content){ content=''; }
	html+='<div class="box '+openClass+'" id="'+id+'">';
	html+='<div class="boxhead" id="'+id+'_head">';
		html+='<div class="boxcontrol" onclick="doBoxControl(this)">'+control+'</div><h3 class="boxheadtext">'+header+'</h3>';
	html+='</div>';
	html+='<div class="boxbody" id="'+id+'_body">';
	html+=content;
	html+='</div>';
	html+='</div>';
	if(!parentElement){ parentElement= document.getElementById("content"); }
	parentElement.innerHTML+=html;
}

function ajax(url,method,parameters,onSuccess,onFailure){
    var httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState === 4) {
            if(httpRequest.status === 200) {
                onSuccess(httpRequest.responseText);
            } else {
                onFailure(httpRequest.responseText);
            }
        }
    }
    if("post"==method){
        httpRequest.open(method, url, true);
        httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        httpRequest.send(parameters);          
    } else {
        httpRequest.open(method, url+"?"+parameters, true);
        httpRequest.send();
    }
}

function init_onSuccess(responseText){
	imagers=JSON.parse(responseText);
	document.getElementById("imagercheckboxes").innerHTML='';
	Object.keys(imagers).map(function (key){
		imager=imagers[key];
		document.getElementById("imagercheckboxes").innerHTML+='<label style="display:block"><input type="checkbox" checked="checked" name="'+imager.serial+'" id="'+imager.serial+'" />'+imager.friendlyName+' ('+imager.serial+')</label>';
	});
	document.getElementById("getusagedata").disabled=false;
}
function init_onFailure(responseText){
	alert("Could not get latest imager overview, so can't know what imagers are installed.");
}

function checkDate(str) {
    var matches = str.match(/(\d){4}-(\d){2}-(\d){2}/);
    if (!matches){ return false; }
    //is regex match the whole string? (Should be able to do this with ^$ but ^ before ( seems to be a problem))
    if(str.length!=10){ return false; }
    // parse each piece and see if it makes a valid date object
    var day = parseInt(matches[3], 10);
    var month = parseInt(matches[2], 10);
    var year = parseInt(matches[1], 10);
    var d = new Date();
    d.setFullYear(year);
    d.setMonth(month-1);
    d.setDate(day);
    if (!d || !d.getTime()) { return false; }
    // make sure we have no funny rollovers that the date object sometimes accepts
    // month > 12, day > what's allowed for the month
    if (d.getMonth() != month-1 ||
        d.getFullYear() != year ||
        d.getDate() != day) {
            return false;
        }
    return true;
}

function getUsageData(){
	startDate=document.getElementById("startdate").value;
	endDate=document.getElementById("enddate").value;
	if(!checkDate(startDate) || !checkDate(endDate)){
		alert("Start and end date must be in YYYY-MM-DD format and valid dates");
		return false;
	} if(startDate>endDate){
		alert("Start date must be before end date");
		return false;
	}
	imagerSerials=[];
	var checkboxes=document.getElementById("imagercheckboxes").querySelectorAll("input");
	for(i=0;i<checkboxes.length;i++){
		var cb=checkboxes[i];
		if(cb.checked){
			imagerSerials.push(cb.name);
		}
	}
	if(0==imagerSerials.length){
		alert("Select at least one imager");
		return false;
	}
	currentDate=startDate;
	currentImager=0;
	if(document.getElementById("errors")){ document.getElementById("errors").remove(); }
	if(document.getElementById("imagergraphs")){ document.getElementById("imagergraphs").remove(); }
	if(document.getElementById("users")){ document.getElementById("users").remove(); }
	box("errors","Errors: <span id='errorcount'>0</span>",false);
	box("imagergraphs","Imagers","",false);
	box("users","Users","",false);
	var graphsbox=document.getElementById("imagergraphs_body");
	for(i=0;i<imagerSerials.length;i++){
		var serial=imagerSerials[i];
		var imager=imagers[serial];
		var platedays='&nbsp;&nbsp;&nbsp;&nbsp;Plate days active: <span id="'+serial+'_platedaysactive">0</span>&nbsp;&nbsp;&nbsp;&nbsp;Plate days expired: <span id="'+serial+'_platedaysexpired">0</span>';
		var content='<div id="graph_'+serial+'" style="background:#ccc; margin:1em; height:200px;position:relative;top:0; overflow:hidden;"></div>';
		box("imagergraphs_"+serial,imager.friendlyName+" ("+serial+")"+platedays,content,false,graphsbox);
		document.getElementById("graph_"+serial).dataset.plateDaysActive=0;
		document.getElementById("graph_"+serial).dataset.plateDaysExpired=0;
	}

	window.setTimeout(forceFormValuesToCurrent, 50);
	
	getUsageDataForImagerAndDate(imagerSerials[currentImager],currentDate);
}

function forceFormValuesToCurrent(){
	//Forces the correct form values - Firefox (at least) resets it!
	var checkboxes=document.getElementById("imagercheckboxes").querySelectorAll("input");
	for(i=0;i<checkboxes.length;i++){
		checkboxes[i].checked=false;
	}
	for(i=0;i<imagerSerials.length;i++){
		var serial=imagerSerials[i];
		document.getElementById(serial).checked="checked";
	}
	
	document.getElementById("startdate").value=startDate;
	document.getElementById("enddate").value=endDate;

}

function getUsageDataForImagerAndDate(imager,dateStr){
	ajax(formulatrixBaseUrl+"/imagerContents/"+imager+"/"+imager+"-"+dateStr+".json?ts="+Date.now(),"get","",
			getUsageDataForImagerAndDate_onSuccess,
			getUsageDataForImagerAndDate_onFailure
	);
}
function getUsageDataForImagerAndDate_onSuccess(responseText){
	var imager=JSON.parse(responseText);
	//update the graph
	var serial=imager.serial;
	var activePercent=100*imager.active/imager.capacity;
	var expiredPercent=100*imager.expired/imager.capacity;
	var title=currentDate+" - Active:"+imager.active+", expired:"+imager.expired;
	var bar='<div style="float:left;height:200px;position:relative;top:0; width:10px;" title="'+title+'">';
		bar+='<div style="position:absolute;width:8px;height:'+activePercent+'%;bottom:0;right:0;background-color:#33f;"></div>';
		bar+='<div style="position:absolute;width:8px;height:'+expiredPercent+'%;bottom:'+activePercent+'%;right:0;background-color:#009;"></div>';
		bar+='</div>';
	document.getElementById("graph_"+serial).innerHTML+=bar;
	var activeDays=1*document.getElementById("graph_"+serial).dataset.plateDaysActive;
	var expiredDays=1*document.getElementById("graph_"+serial).dataset.plateDaysExpired;
	activeDays+=(1*imager.active);
	expiredDays+=(1*imager.expired);
	document.getElementById(serial+"_platedaysactive").innerHTML=activeDays;
	document.getElementById(serial+"_platedaysexpired").innerHTML=expiredDays;
	document.getElementById("graph_"+serial).dataset.plateDaysActive=activeDays;
	document.getElementById("graph_"+serial).dataset.plateDaysExpired=expiredDays;
	//write out the plate data
	var usersbox=document.getElementById("users_body");
	Object.keys(imager.contents).map(function (barcode){
		var plate=imager.contents[barcode];
		var userDivId=plate.owner.replace(/[^a-zA-Z0-9/]/,'_');
		if(!document.getElementById(userDivId)){
			var userplatedays='<span style="float:right">Plate days active: <span id="'+userDivId+'_platedaysactive">0</span>&nbsp;&nbsp;&nbsp;&nbsp;Plate days expired: <span id="'+userDivId+'_platedaysexpired">0</span></span>';
			box(userDivId,userplatedays+plate.owner,'',false,usersbox);
			document.getElementById(userDivId).dataset.plateDaysActive=0;
			document.getElementById(userDivId).dataset.plateDaysExpired=0;
		}
		/*
		if(!document.getElementById(userDivId)){
			var userplatedays='<span style="float:right">Plate days active: <span id="'+userDivId+'_platedaysactive">0</span>&nbsp;&nbsp;&nbsp;&nbsp;Plate days expired: <span id="'+userDivId+'_platedaysexpired">0</span></span>';
			box(userDivId,userplatedays+plate.owner,'',false,usersbox);
			document.getElementById(userDivId).dataset.plateDaysActive=0;
			document.getElementById(userDivId).dataset.plateDaysExpired=0;
		}
		*/
		if(!document.getElementById(userDivId+"_"+serial)){
			var serialplatedays='<span style="float:right;font-weight:normal;margin-right:2em;">Plate days active: <span id="'+userDivId+"_"+serial+'_platedaysactive">0</span>&nbsp;&nbsp;&nbsp;&nbsp;Plate days expired: <span id="'+userDivId+"_"+serial+'_platedaysexpired">0</span></span>';
			box(userDivId+"_"+serial,serialplatedays+serial,'',false,document.getElementById(userDivId+"_body"));
			document.getElementById(userDivId+"_"+serial).dataset.plateDaysActive=0;
			document.getElementById(userDivId+"_"+serial).dataset.plateDaysExpired=0;
		}
		if(!document.getElementById(userDivId+"_"+serial+"_"+barcode)){
			var platedays='Days active: <span id="'+userDivId+"_"+serial+"_"+barcode+'_platedaysactive">0</span>&nbsp;&nbsp;&nbsp;&nbsp;Days expired: <span id="'+userDivId+"_"+serial+"_"+barcode+'_platedaysexpired">0</span>';
			document.getElementById(userDivId+"_"+serial+"_body").innerHTML+='<div class="platebar" id="'+userDivId+"_"+serial+"_"+barcode+'"><div style="float:left">'+barcode+"</div>"+platedays+'</div>';
			document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysActive=0;
			document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysExpired=0;
		}
		if(!plate.inspectionsRemaining){
			document.getElementById(userDivId).dataset.plateDaysExpired++;
			document.getElementById(userDivId+"_"+serial).dataset.plateDaysExpired++;
			document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysExpired++;
			document.getElementById(userDivId+"_platedaysexpired").innerHTML=document.getElementById(userDivId).dataset.plateDaysExpired;
			document.getElementById(userDivId+"_"+serial+'_platedaysexpired').innerHTML=document.getElementById(userDivId+"_"+serial).dataset.plateDaysExpired;
			document.getElementById(userDivId+"_"+serial+"_"+barcode+'_platedaysexpired').innerHTML=document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysExpired;
		} else {
			document.getElementById(userDivId).dataset.plateDaysActive++;
			document.getElementById(userDivId+"_"+serial).dataset.plateDaysActive++;
			document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysActive++;
			document.getElementById(userDivId+"_platedaysactive").innerHTML=document.getElementById(userDivId).dataset.plateDaysActive;
			document.getElementById(userDivId+"_"+serial+'_platedaysactive').innerHTML=document.getElementById(userDivId+"_"+serial).dataset.plateDaysActive;
			document.getElementById(userDivId+"_"+serial+"_"+barcode+'_platedaysactive').innerHTML=document.getElementById(userDivId+"_"+serial+"_"+barcode).dataset.plateDaysActive;
		}
		
	});

	doNext();
}
function getUsageDataForImagerAndDate_onFailure(responseText){
	document.getElementById("errors_body").innerHTML+='<div>Could not get data for date '+currentDate+' imager '+imagerSerials[currentImager]+'</div>';
	var count=document.getElementById("errorcount").innerHTML;
	count*=1;
	count++;
	document.getElementById("errorcount").innerHTML=count;
	doNext();
}
function doNext(){
	currentImager++;
	if(currentImager>=imagerSerials.length){ 
		//done them all for this day. Start a new day and the first imager.
		if(currentDate==endDate){ 
			//we're done here. Do any cleanup and then
			return;
		}
		currentImager=0; 
		var parts=currentDate.split('-');
		var nextDate=new Date(1*parts[0], (1*parts[1])-1, (1*parts[2])+1);
		var y=nextDate.getFullYear();
		var m=nextDate.getMonth();
		m++;
		if(m<10){ m="0"+m; }
		var d=nextDate.getDate();
		if(d<10){ d="0"+d; }
		currentDate=y+"-"+m+"-"+d;
	}
	getUsageDataForImagerAndDate(imagerSerials[currentImager],currentDate);
}


ajax(formulatrixBaseUrl+"/imagerContents/overview/overview-latest.json?ts="+Date.now(),"get","",init_onSuccess,init_onFailure);
</script>

<jsp:include page="/JSP/core/Footer.jsp" />
