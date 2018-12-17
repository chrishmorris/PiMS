<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Imager contents' />
</jsp:include>
<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="plate.png" />        
<c:set var="title" value="Imager contents: ${serial}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<style type="text/css">
table { font-size:80%; }
th { cursor:pointer; }
th.asc, th.desc {background-color:#ff9; }
tr.expired, tr.expired td { background-color:#fdd; }
</style>

<script type="text/javascript">
var formulatrixBaseUrl="${formulatrixBaseUrl}";
var sortColumn="barcode";
var isAscending=false;
var imager={};
var contextPath="${pageContext.request['contextPath']}";

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
	imager=JSON.parse(responseText);
	imager.contents = Object.keys(imager.contents).map(function (key) {return imager.contents[key]});
	document.getElementById("friendlyName").up(".formfield").down(".inputnoedit").innerHTML=imager.friendlyName;
	document.getElementById("capacity").up(".formfield").down(".inputnoedit").innerHTML=imager.capacity;
	document.getElementById("active").up(".formfield").down(".inputnoedit").innerHTML=imager.active;
	document.getElementById("expired").up(".formfield").down(".inputnoedit").innerHTML=imager.expired;
	renderContentsTable();
}
function init_onFailure(responseText){
	document.getElementById("contents_body").innerHTML="Could not fetch imager contents. The network could be down, xtalPiMS might not be configured to find the imager contents, or the imager serial may not exist.";
}

function sortTable(elem){
	elem=$(elem);
	elem.className.split(" ").each(function(cn){
		if("asc"==cn){
			isAscending=false; //looks backward, but we're toggling this
		} else if("desc"==cn){
			isAscending=true;
		} else {
			sortColumn=cn;
		}
	});
	renderContentsTable();
}

function renderContentsTable(){
	var th=$("contents_body").down("th."+sortColumn);
	if(th){
		if(isAscending){
			th.addClassName("asc");
			th.removeClassName("desc");
		} else{
			th.addClassName("desc");
			th.removeClassName("asc");
		}
	}
	imager.contents.sort(function(a,b){
		a=a[sortColumn];
		b=b[sortColumn];
		if("inspectionsRemaining"==sortColumn){ //all others are strings
			a=parseInt(a);
			b=parseInt(b);
		}
		if(null==a){ a=''; }
		if(null==b){ b=''; }
		var ret=0;
		if(a>b){ 
			ret=1;
		} else if(a<b){
			ret=-1;
		}
		if(!isAscending){ ret *=-1; }
		return ret;
	});
	html='<table>';
	html+='<tr><th onclick="sortTable(this)" class="barcode">Barcode</th>';
	html+='<th onclick="sortTable(this)" class="owner">Owner</th>';
	html+='<th onclick="sortTable(this)" class="inspectionsRemaining">Inspections left</th>';
	html+='<th onclick="sortTable(this)" class="nextInspection">Next inspection</th>';
	html+='<th onclick="sortTable(this)" class="finalInspection">Final inspection</th></tr>';
	imager.contents.each(function(plate){
		var expiredClass='';
		var nextInsp=plate.nextInspection;
		var finalInsp=plate.finalInspection;
		if(!plate.inspectionsRemaining){
			expiredClass=' class="expired"';
			nextInsp='Expired';
			finalInsp='Expired';
		}
		html+='<tr'+expiredClass+'><td><a href="'+contextPath+'/ViewTrialDrops.jsp?barcode='+plate.barcode+'">'+plate.barcode+'</a></td><td>'+plate.owner+'</td><td>'+plate.inspectionsRemaining+'</td><td>'+nextInsp+'</td><td>'+finalInsp+'</td></tr>';
	});
	html+='</table>';
	document.getElementById("contents_body").innerHTML=html;
	if(isAscending){
		$("contents_body").down("th."+sortColumn).addClassName("asc");
	} else {
		$("contents_body").down("th."+sortColumn).addClassName("desc");
	}
}

ajax(formulatrixBaseUrl+"/imagerContents/${serial}/${serial}-latest.json?ts="+Date.now(),"get","",init_onSuccess,init_onFailure);
</script>
<pimsWidget:box title="Imager ${serial}" initialState="fixed" id="basics">
<pimsForm:form action="#" mode="view" method="get">
<pimsForm:formBlock>
	<pimsForm:column1>
		<pimsForm:text name="friendlyName" alias="Name"	value="${serial}" />
		<pimsForm:text name="serial" alias="Serial"	value="${serial}" />
	</pimsForm:column1>
	<pimsForm:column2>
		<pimsForm:text name="capacity" alias="Capacity"	value="..." />
		<pimsForm:text name="active" alias="Active plates"	value="..." />
		<pimsForm:text name="expired" alias="Expired plates"	value="..." />
	
	</pimsForm:column2>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>
<pimsWidget:box title="Contents of ${serial}" initialState="fixed" id="contents">
</pimsWidget:box>


<jsp:include page="/JSP/core/Footer.jsp" />
