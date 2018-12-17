<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Move: ${hostobject.name}" />
    <jsp:param name="extraStylesheets" value="custom/createexperiment" />
</jsp:include>
obsolete?
<!--  holder/MoveObject.jsp -->
<script type="text/javascript">

var contextPath="${pageContext.request.contextPath}";
var n = 0;   

function parentHookOnchange(thisSelect){

  var selectedValue = thisSelect.options[thisSelect.selectedIndex].value;
  document.getElementById("locationhook").value = selectedValue;
  
  var divId = thisSelect.parentNode.id;
  var x = divId.substring(11);
  
  for(n;n>x;n--) {
    $("locationdiv"+n).innerHTML="&nbsp;";
  }
  new Ajax.Request(contextPath+"/update/Move/"+"${hostobject.hook}", {
	method:"get",
	parameters:"isAJAX=true&parentHook="+selectedValue,
	onSuccess:function(transport){ 
	  expt_getLocationsOnSuccess(transport, selectedValue);
	},
	onFailure:function(transport){ 
	  ajax_default_onFailure(transport); 
	},
	onCreate:function(transport) {
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 10000); //10-second timeout
	},
	onComplete:function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}

  });  
}

function expt_getLocationsOnSuccess(transport){

  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var locations=docRoot.getElementsByTagName("object");
  var html="Move to: ";
  document.getElementById("locationsubmit").disabled="disabled"; 
   
  if(0==locations.length){
    document.getElementById("locationsubmit").disabled=""; 
    return true; 
  }
  
  html+="<select id='parentHook' name='parentHook' onchange='parentHookOnchange(this)'>";
  html+="<option value=''>Choose...<\/option>";
  for(var i=0;i<locations.length;i++) {
    var attr=locations[i];
    var hook=attr.getAttribute("hook");
    var name=attr.getAttribute("name");
	html+="<option id='prot"+i+"' value='"+hook+"'>"+name+"<\/option>";
  }	
  html+="<\/select>"
  n++;
  $("locationdiv"+n).innerHTML=html;
}
</script>

<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/${hostobject.className}">${hostobject.classDisplayName}s</a>    
    <c:forEach items="${ locationTrail }" var="location" >
            : <a href="${pageContext.request.contextPath}/View/${location.hook}"><c:out value="${location.name}"/></a> 
    </c:forEach>        
</c:set>
        
<c:set var="actions">
    <pimsWidget:deleteLink bean="${bean}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${hostobject.name}" actions="${actions}" icon="${icon}" />



<div id="expt_createexperiment"><!-- wrapper, for width-->
<c:set var="head" value="Update ${hostobject.classDisplayName} Location" />

<pimsWidget:box title="${head}" initialState="fixed" >
<div class="expt_createform">
<p class="help">This allows you to create a hierarchy of holders so that you can describe exactly where your sample is stored
e.g. in a box on a shelf of a fridge. First choose the top level holder from the list. 
<script type="text/javascript">document.write("You can then choose from a new list of holders within that just chosen. (If there is only one suitable holder, it will be selected for you).  When there are no more holders, the Next button will activate. ");</script></p>

<form id="expt_exptypeform" action="${pageContext.request.contextPath}/update/Move/<c:out value="${hostobject.hook}"/>"  method="post">

    <%-- TODO CSRF token --%>
<div id="locationdiv0" style="height:2em;padding:0.5em 0 0 0em">

Move to:
 
<select name="parentHook" id="parentHook" onchange="parentHookOnchange(this)">

<script type="text/javascript">
// Only write this option if JS - submitting with it causes AssertionError. 
// Don't write if no JS, because we can't disable the submit button
document.write('<option value="">Choose...<\/option>');
$("expt_exptypeform").method="post";
</script>

<c:forEach var="location" items="${results}">
  <option value="${location.hook}">${location.name}</option>
</c:forEach>
</select>
</div>

<div id="locationdiv1" style="height:2em;padding:0 0 0 1em">
&nbsp;
</div>

<div id="locationdiv2" style="height:2em;padding:0 0 0 2em">
&nbsp;
</div>

<div id="locationdiv3" style="height:2em;padding:0 0 0 3em">
&nbsp;
</div>

<div id="locationdiv4" style="height:2em;padding:0 0 0 4em">
&nbsp;
</div>

<input type="hidden" id="locationhook" name="locationhook" value="${param['parentLocationHook']}" />
<input type="submit" id="locationsubmit" value="Next &gt;&gt;&gt;" style="margin-left:28em"/>

Record a <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.location.Location">new location</a>

</form>
</div>
</pimsWidget:box>

</div>

<script type="text/javascript">
document.getElementById("locationsubmit").disabled="disabled";  
</script>

<!--  /MoveObject.jsp -->
<jsp:include page="/JSP/core/Footer.jsp" />
