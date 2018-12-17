<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Choose experiment type for new experiment.
See ExperimentCreationAction

Author: Chris Morris
Date: July 2006
--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>
<jsp:useBean id="results" scope="request" type="java.util.Collection" />

<c:catch var="error">
obsolete <obsolete />
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="New Experiment" />
    <jsp:param name="extraStylesheets" value="custom/createexperiment" />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment">Experiments</a></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Record a new experiment"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<!--  ChooseExperimentType.jsp -->obsolete
<script type="text/javascript">var contextPath="${pageContext.request.contextPath}";</script>

<script type="text/javascript">
function setSubmitButtonDisability(){
  //called during page load - use document.getElementById because can't guarantee Prototype's $() method available yet
  document.getElementById("protocolsubmit").disabled="disabled";  
  if(""==document.getElementById("experimentType").value) return;
  if(!document.forms["expt_exptypeform"]["protocolHook"] || ""==document.forms["expt_exptypeform"]["protocolHook"].value) return;
  if(""==document.getElementById("experimentName").value) return;
  if(""==document.getElementById("projectHook").value) return;
  document.getElementById("protocolsubmit").disabled="";  
}

function setCreateButtonDisability(){
  //called during page load - use document.getElementById because can't guarantee Prototype's $() method available yet
  document.getElementById("protocolcreate").disabled="disabled";  
  if(""==document.getElementById("experimentName").value) return;
  if(""==document.getElementById("projectHook").value) return;
  document.getElementById("protocolcreate").disabled="";  
}


function expt_exptypeOnchange(){
  setSubmitButtonDisability();
  var val=$("experimentType").value;
  if(""==val) {
    $("protocoldiv").innerHTML="&nbsp;";
    return false; 
  }
  $("protocoldiv").innerHTML="Protocol: <img src='"+contextPath+"/skins/default/images/icons/actions/waiting.gif'/>";
  
  new Ajax.Request(contextPath+"/Create/org.pimslims.model.experiment.ExperimentGroup", {
	method:"get",
	parameters:"isAJAX=true&experimentType="+val,
	onSuccess:function(transport){ 
	  expt_getProtocolsOnSuccess(transport);
	},
	onFailure:function(transport){ 
	  ajax_default_onFailure(transport); 
	},
	onCreate: function(transport) {
	  transport['timeoutId'] = window.setTimeout( function(){
	    if (callInProgress(transport)) {
		  transport.abort();
	      showTimedOutMessage();
		}
	  }, 10000); //10-second timeout
	},
	onComplete: function(request) {
	  // Clear the timeout, the request completed ok
	  window.clearTimeout(request['timeoutId']);
	}
  });  
}

function expt_getProtocolsOnSuccess(transport){
  ajax_checkStillLoggedIn(transport);
  var docRoot=transport.responseXML.documentElement;
  var protocols=docRoot.getElementsByTagName("object");

  var html="Protocol: ";
  if(1==protocols.length){
    html+=protocols[0].getAttribute("name");
    html+="<input type='hidden' name='protocolHook' id='protocolHook' value='"+protocols[0].getAttribute("hook")+"'/>";
  } else {
    html+="<select id='protocolHook' name='protocolHook' onchange='expt_protocolOnchange()'>";
    html+="<option value=''>Choose...<\/option>";
    for(var i=0;i<protocols.length;i++) {
      var attr=protocols[i];
	  var hook=attr.getAttribute("hook");
      var name=attr.getAttribute("name");
	  html+="<option id='prot"+i+"' value='"+hook+"'>"+name+"<\/option>";
    }	
    html+="<\/select>";
  }
  $("protocoldiv").innerHTML=html;
  /*
  setSubmitButtonDisability();
  */
  if(1==protocols.length){
	  expt_protocolOnchange();
  }	else if ("" != protocol_hook){
	  document.getElementById("protocolHook").value=protocol_hook;
	  if(protocol_hook != document.getElementById("protocolHook").value){
	    //setting SELECT failed, give up
	  } else {
		expt_protocolOnchange();
	  }
	  protocol_hook="";
  }
}

function expt_protocolOnchange(){
    var val=document.forms["expt_exptypeform"]["protocolHook"].value;
    var project = document.forms["expt_exptypeform"]["researchObjective"].value;

    if($("experimentName") && $("experimentName")["userchanged"]){
        return false;
    }
    
    if(""==val) {
	    $("expnamediv").innerHTML="&nbsp;";
	    return false; 
	  }
	  $("expnamediv").innerHTML="Experiment Name: <img src='"+contextPath+"/skins/default/images/icons/actions/waiting.gif'/>";
		 
	new Ajax.Request(contextPath+"/Create/org.pimslims.model.experiment.ExperimentGroup", {
		method:"get",
		parameters:"isAJAX=true&experimentProtocolHook="+val+"&researchObjective="+project,
		onSuccess:function(transport){ 
		  expt_getExpNameOnSuccess(transport);
		},
		onFailure:function(transport){ 
		  ajax_default_onFailure(transport); 
		}
	});  
}

function expt_getExpNameOnSuccess(transport){
	ajax_checkStillLoggedIn(transport);
	var docRoot=transport.responseXML.documentElement;
	var names=docRoot.getElementsByTagName("experiment");
	var projects=docRoot.getElementsByTagName("project");

	var html="Experiment name: ";
	if(1==names.length){
		html+="<input type='text' name='experimentName' id='experimentName' value='"+names[0].getAttribute('name')+"' onkeyup='this.userchanged=true' onChange='checkUnique(this);setSubmitButtonDisability();' />";
	}
	$("expnamediv").innerHTML=html;
	attachValidation("experimentName", { required:true, unique:{obj:'org.pimslims.model.experiment.Experiment', prop:'name'} }); 

	var html="Lab Notebook: ";
	  if(1==projects.length){
	    html+=projects[0].getAttribute("name");
	    html+="<input type='hidden' name='projectHook' id='projectHook' value='"+projects[0].getAttribute("hook")+"'/>";
	  } else {
	    html+="<select id='projectHook' name='projectHook' onchange='setSubmitButtonDisability()'>";
	    html+="<option value=''>Choose...<\/option>";
	    for(var i=0;i<projects.length;i++) {
	      var attr=projects[i];
		  var hook=attr.getAttribute("hook");
	      var name=attr.getAttribute("name");
		  html+="<option id='prot"+i+"' value='"+hook+"'>"+name+"<\/option>";
	    }	
	    html+="<\/select>";
	  }
	
	$("projectdiv").innerHTML=html;
	attachValidation("projectHook", { required:true }); 
	setSubmitButtonDisability();
}

function expt_protocolOnClick(hook){
	 if(""==hook) {
	    $("recentexpnamediv").innerHTML="&nbsp;";
	    return false; 
	 }
	 document.getElementById("protocolcreatehook").value=hook
	 $("recentexpnamediv").innerHTML="<img src='"+contextPath+"/skins/default/images/icons/actions/waiting.gif'/>";
	  
	new Ajax.Request(contextPath+"/Create/org.pimslims.model.experiment.Experiment", {
		method:"get",
		parameters:"isAJAX=true&experimentProtocolHook="+hook,
		onSuccess:function(transport){ 
		  expt_getRecentExpNameOnSuccess(transport);
		},
		onFailure:function(transport){ 
		  ajax_default_onFailure(transport); 
		}
	});  
}

function expt_getRecentExpNameOnSuccess(transport){
	ajax_checkStillLoggedIn(transport);
	var docRoot=transport.responseXML.documentElement;
	var names=docRoot.getElementsByTagName("experiment");
	var projects=docRoot.getElementsByTagName("project");

	var html="Experiment name: ";
	if(1==names.length){
		html+="<input type='text' name='experimentName' id='experimentName' value='"+names[0].getAttribute('name')+"' onChange='checkUnique(this);setSubmitButtonDisability();' />";
	}
	$("recentexpnamediv").innerHTML=html;

	var html="Lab Notebook: ";
	  if(1==projects.length){
	    html+=projects[0].getAttribute("name");
	    html+="<input type='hidden' name='projectHook' id='projectHook' value='"+projects[0].getAttribute("hook")+"'/>";
	  } else {
	    html+="<select id='projectHook' name='projectHook' onchange='setCreateButtonDisability()'>";
	    html+="<option value=''>Choose...<\/option>";
	    for(var i=0;i<projects.length;i++) {
	      var attr=projects[i];
		  var hook=attr.getAttribute("hook");
	      var name=attr.getAttribute("name");
		  html+="<option id='prot"+i+"' value='"+hook+"'>"+name+"<\/option>";
	    }	
	    html+="<\/select>";
	  }
	
	$("recentprojectdiv").innerHTML=html;
	attachValidation("projectHook", { required:true }); 

	attachValidation("experimentName", { required:true, unique:{obj:'org.pimslims.model.experiment.Experiment', prop:'name'} }); 
	setCreateButtonDisability();
}

</script>

<div id="expt_createexperiment"><!-- wrapper, for width-->

<c:choose>
<c:when test="${!empty recentProtocols && empty param.experimentType}"><%-- LATER if recent protocols and expt type, show recent protocols of correct type --%>
<pimsWidget:box title="Recently used protocols" initialState="fixed" extraClasses="noscroll">
<div class="expt_createform">
	<p class="help">Click the "Create experiment" button next to the protocol you wish to use.</p>
	<form id="expt_recentprotocolform" action="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment" method="post" style="text-align:right">
	
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/Create/org.pimslims.model.experiment.Experiment')}" />
	<c:forEach var="prot" items="${recentProtocols}">
	<div style="height:2em;">
    	<span style="float:left"><c:out value="${prot.name}" /></span>
    	<span style="float:right">
    	<input type="button" value="Choose" onclick="expt_protocolOnClick('${prot._Hook}')"/>
    	</span>
    </div>	
	</c:forEach>

	<div id="recentexpnamediv" style="height:2em;padding:0 0 0.5em 0">
	&nbsp;
	</div>
	
	<div id="recentprojectdiv" style="height:2em;padding:0 0 0.5em 5.4em">
	&nbsp;
	</div>
	
	<input type="hidden" id="protocolcreatehook" name="protocolHook" value=""/>
	<input type="hidden" name="researchObjective" value="${param['researchObjective']}" />
	<input type="submit" disabled="disabled" id="protocolcreate"  value="Create Experiment" />
	</form>
</div>
</pimsWidget:box>

<c:set var="head" value="Other protocols" />
</c:when>
<c:otherwise>
<c:set var="head" value="Choose a protocol" />
</c:otherwise>
</c:choose>

<pimsWidget:box title="${head}" initialState="fixed" extraClasses="noscroll" >
<div class="expt_createform">
<p class="help">Choose the experiment type from the list. <script type="text/javascript">document.write("You can then choose from the protocols of that type. (If there is only one suitable protocol, it will be selected for you.) ");</script>Then click Next.</p>
<form id="expt_exptypeform" action="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment"  method="get">

<div style="padding:0 0 0 0.6em">
Experiment type: <select name="experimentType" id="experimentType" onchange="expt_exptypeOnchange()">

<script type="text/javascript">
// Only write this option if JS - submitting with it causes AssertionError. 
// Don't write if no JS, because we can't disable the submit button
document.write('<option value="">Choose...<\/option>');
$("expt_exptypeform").method="post";
</script>

<c:forEach var="exptype" items="${results}">
  <option value="${exptype.hook}"><c:out value="${exptype.name}" /></option>
</c:forEach>
</select>
</div>

<div id="protocoldiv" style="height:2em;padding:0.5em 0 0 4.8em">
&nbsp;
</div>

<div id="expnamediv" style="height:2em;padding:0 0 0.5em 0">
&nbsp;
</div>

<div id="projectdiv" style="height:2em;padding:0 0 0.5em 5.4em">
&nbsp;
</div>

<input type="hidden" name="researchObjective" value="${param['researchObjective']}" />
<input type="submit" id="protocolsubmit" value="Next &gt;&gt;&gt;" style="float:right"/>

</form>
<div style="font-size:0;clear:both">&nbsp;</div>
</div>
</pimsWidget:box>

<c:if test="${!empty param.experimentType}">
<script type="text/javascript">
var protocol_hook="";
<c:if test="${!empty param.protocol}">
protocol_hook="${param.protocol}";
</c:if>
document.getElementById("experimentType").value="${param.experimentType}";
if("${param.experimentType}" != document.getElementById("experimentType").value){
	//setting the SELECT failed, give up
} else {
	expt_exptypeOnchange();
}
</script>
</c:if>

</div><!-- end expt_createexperiment-->

<script type="text/javascript">
setSubmitButtonDisability();
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<jsp:include page="/JSP/core/Footer.jsp" />
