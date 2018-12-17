<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<%@page session = "true" %>
<%@page contentType = "text/html"%>
<%@page pageEncoding = "UTF-8"%>
<%@page isThreadSafe="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<style type="text/css">
body {background:transparent; width:80%; max-width:600px; margin:0 auto; }
.imager {margin:0 10px; }
.imager+.imager { margin-top:10px; }
.imager .info { font-family:verdana,arial,hevetica,sans-serif; font-size:14px;}
.imager .info a { float:right; }
.imagerbar { clear:both; position:relative; top:0; font-size:0; background-color:#eee; height:12px; ; 
	border-radius:3px; margin-top:5px;; box-shadow:inset 2px 2px 2px #666; overflow:hidden;}
.active  { position:absolute;top:0;left:0; height:14px; background-color:#0f0; opacity:0.9; border-color:#030; 
						border-radius:3px 0px 0px 3px; border-width:1px; border-style:solid; border-right-width:0; border-bottom-width:0; }
.expired { position:absolute;top:0;left:0; height:14px; background-color:#060; opacity:0.9; border-color:#030; 
						border-radius:3px 0px 0px 3px; border-width:1px; border-style:solid; border-right-width:0; border-bottom-width:0; }
.active+.expired { border-left-width:0; }

.almostfull .active  { background-color:#fc0;  border-color:#660;}
.almostfull .expired { background-color:#960;  border-color:#330;}

.full .active  { background-color:#f00; border-color:#600;}
.full .expired { background-color:#900; border-color:#300;}

.scanning .scroller { position:absolute;top:0; left:45%; background-color:#33c; border-top:1px solid #003; width:10%; height:100%; }

</style>
<script type="text/javascript">
var defaultAlertThreshold="85%";
var defaultWarningThreshold="95%";
var formulatrixBaseUrl="${formulatrixBaseUrl}";
var rigakuBaseUrl="${rigakuBaseUrl}";
var contextPath="${pageContext.request['contextPath']}";
	
function init(){
	if(""==formulatrixBaseUrl && ""==rigakuBaseUrl){
		document.getElementById("imagers").innerHTML="<p>xtalPiMS is not configured for imager overviews.</p><p>Define formulatrix.imagerJSON.baseUrl in the application's context XML.</p>";
		return false;
	}
	if(""!=formulatrixBaseUrl){
		ajax(formulatrixBaseUrl+"/imagerContents/overview/overview-latest.json?ts="+Date.now(),"get","",init_onSuccess,init_onFailure);
	}
}
function init_onSuccess(responseText){
	var imagers=JSON.parse(responseText);
	renderImagers(imagers);
}
function init_onFailure(responseText){
	document.getElementById("imagers").innerHTML="Could not fetch imagers overview.";
}

function renderImagers(imagers){
	document.getElementById("imagers").innerHTML='';
	var imagerScanning=false;
	var serials=Object.keys(imagers);
	for(i=0;i<serials.length;i++){
		var serial=serials[i];
		var imager=imagers[serial];
		var barContent='';
		var barTitle='';
		var moreLink='';
		var imagerClass="imager ";
		var imagerName=serial;
		if(imager.friendlyName && imager.friendlyName!=""){
			imagerName=imager.friendlyName+" ("+imager.serial+")";
		}
		var capacity=imager.capacity;
		var expired=imager.expired;
		var active=imager.active;
		var total=active+expired;
		var alertThreshold=defaultAlertThreshold;
		var warningThreshold=defaultWarningThreshold;
		if(imager.alertThreshold)  { alertThreshold=imager.alertThreshold;     }
		if(imager.warningThreshold){ warningThreshold=imager.warningThreshold; }
		if(alertThreshold.indexOf("%")){
			alertThreshold=Math.floor( capacity*parseFloat(alertThreshold)/100 );
		}
		if(warningThreshold.indexOf("%")){
			warningThreshold=Math.floor( capacity*parseFloat(warningThreshold)/100 );
		}
		
		barTitle=capacity+' slots: '+active+' active plates, '+expired+' expired plates, '+(capacity-total)+' empty'
		if(total>=warningThreshold){
			imagerClass+="full";
		} else if(total>=alertThreshold){
			imagerClass+="almostfull";
		}
		var activePercent=100*active/capacity;
		var expiredPercent=100*expired/capacity;
		if(0<active){
			barContent+='<div class="active" style="width:'+activePercent+'%;">&nbsp;</div>';
		}
		if(0<expired){
			barContent+='<div class="expired" style="left:'+activePercent+'%;width:'+expiredPercent+'%;">&nbsp;</div>';
		}
		moreLink='<a target="_top" href="'+contextPath+'/ImagerContents/?serial='+serial+'" title="Full plate inventory">See contents</a>';
		var out='';
		out+='<div class="'+imagerClass+'" title="'+barTitle+'">';
		out+='<div class="info">'+imagerName+moreLink+'</div>';
		out+='<div class="imagerbar">'+barContent+'</div>';
		out+='</div>';
		document.getElementById("imagers").innerHTML+=out;
	}
}

function ajax(url,method,parameters,onSuccess,onFailure){
    var httpRequest = new XMLHttpRequest();
    httpRequest.onreadystatechange = function(){
        if(httpRequest.readyState === 4) {
            if(httpRequest.status === 200) {
                onSuccess(httpRequest.responseText);
            } else {
            	alert(httpRequest.status)
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

</script>
</head>
<body onload="init()">
<div id="imagers">Loading imager data...</div>
 </body>
</html>