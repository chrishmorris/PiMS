<%--
Create Experiment Group form
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page import="org.pimslims.model.sample.*" %>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Record a new experiment group" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' /> 
</jsp:include>


<script type="text/javascript">

function expt_exptypeOnchange(){
  
  var val=$("experimentType").value;
  if(""==val) {
    //$("protocoldiv").innerHTML="&nbsp;";
    return false; 
  }
  
  new Ajax.Request(contextPath+"/Create/org.pimslims.model.experiment.ExperimentGroup", { 
	method:"get",
	parameters:"isAJAX=true&isGROUP=true&experimentType="+val, 
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
	var attr=protocols[0];
	var hook=attr.getAttribute("hook");
    var name=attr.getAttribute("name");
	$("protocol").options.length=0;
	$("protocol").options[0]=new Option(name, hook, true, false);
	expt_protocolOnchange();
  } else {
	$("protocol").options.length=0;
	$("protocol").options[0]=new Option("Choose..", "", false, false);
    for(var i=0;i<protocols.length;i++) {
      var attr=protocols[i];
	  var hook=attr.getAttribute("hook");
      var name=attr.getAttribute("name");
      $("protocol").options[i+1]=new Option(name, hook, false, false);
    }	
  }
}

function expt_protocolOnchange(){
	var val=$("protocol").value;
	  if(""==val) {
	    return false; 
	  }

	var group=document.getElementById("groupName");
    if(""!=group.value && (undefined==group.lastSuggested || group.lastSuggested!=group.value)){
        return false;
    }
	  
	new Ajax.Request(contextPath+"/CreatePlate", {
		method:"get",
		parameters:"isAJAX=true&experimentProtocolHook="+val,
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

	var group=document.getElementById("groupName");
	if(1==names.length){
		group.value=names[0].getAttribute('name');
	}
   group.lastSuggested=group.value;
    $("suggestspinner").style.display="none";
    $("suggestlink").style.display="";
}

function suggestExperimentName(){ //user clicked "Suggest" link
    if(""==$("protocol").value || ""==$("experimentType").value){
        alert("To suggest a name, both experiment type and protocol are required.");
        return false;
    }
    $("suggestlink").style.display="none";
    $("suggestspinner").style.display="";
    getSuggestedExperimentName();
}

function getSuggestedExperimentName(){
    var val=$("protocol").value;
    new Ajax.Request(contextPath+"/CreatePlate", {
        method:"get",
        parameters:"isAJAX=true&experimentProtocolHook="+val,
        onSuccess:function(transport){ 
          expt_getExpNameOnSuccess(transport);
        },
        onFailure:function(transport){ 
          ajax_default_onFailure(transport); 
        }
    });  
}


</script>

<pimsWidget:pageTitle icon="experimentgroup.png" title="New experiment group" />

<pimsWidget:box title="Details of new experiment group" initialState="open">
<pimsForm:form mode="edit" action="/Create/org.pimslims.model.experiment.ExperimentGroup" method="post" enctype="multipart/form-data">
    <pimsForm:formBlock>
    <h4>Basic details</h4>
        <pimsForm:column1>
            
            <pimsForm:select alias="Experiment type" name="experimentType" validation="required:true" onchange="expt_exptypeOnchange()">
            	<pimsForm:option optionValue="" currentValue="${experimentType.hook}" alias="Choose.." />
            	<c:forEach items="${experimentTypes}" var="elem">
           			<pimsForm:option optionValue="${elem.hook}" currentValue="${experimentType.hook}" alias="${elem.name}" />
         		</c:forEach>
            </pimsForm:select>
            
            <pimsForm:select alias="Protocol" name="protocol" validation="required:true" onchange="expt_protocolOnchange()">
              <c:if test="${!empty protocol}"><option value="${protocol._Hook}">${protocol.name}</option></c:if>
            </pimsForm:select>
            
            <pimsForm:text alias="Group name" name="groupName" onchange="ajax_validate(this,'org.pimslims.model.experiment.ExperimentGroup','name');" validation="required:true" /> 
            
            <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook these experiments belong to" objects="${accessObjects}" validation="required:true"/>
            
        </pimsForm:column1>
        
        <pimsForm:column2>
            <pimsForm:date alias="Start date" name="startDate" />         
            <pimsForm:date alias="End date" name="End Date" />        
            <pimsForm:text alias="Number of Experiments" value="${param['numExperiments']}" name="numExperiments" validation="required:true, wholeNumber:true, minimum:0" /> 
        </pimsForm:column2>
    </pimsForm:formBlock>
    
<script type="text/javascript">
var protocolHook="${protocol._Hook}";

var groupField=$("groupName");
var label=groupField.up(".formitem").down(".fieldname");
label.update(label.innerHTML+'<a href="#" onclick="suggestExperimentName();return false"' +
        'style="margin-left:0.5em;font-size:80%" id="suggestlink">Suggest<\/a>' +
        '<img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" id="suggestspinner" style="display:none">');

//this function will get called after we change the experiment type
function autoSelectProtocol(){
	//try to set the protocol value to the hook of the protocol we want
	$("protocol").value=protocolHook;
	if($("protocol").value != protocolHook){
		//that value isn't in the SELECT (yet), so round we go again...
		setTimeout(autoSelectProtocol,500);
	} else {
		var elSel = document.getElementById('protocol');
		var i;
		for (i = elSel.length - 1; i>=0; i--) {
		  if (!elSel.options[i].selected) {
		    elSel.remove(i);
		  }
		}
		expt_protocolOnchange();
	}
}

if ("${experimentType.hook}") {
	document.getElementById("experimentType").value="${experimentType.hook}";
	var elSel = document.getElementById('experimentType');
	var i;
	for (i = elSel.length - 1; i>=0; i--) {
  		if (!elSel.options[i].selected) {
    		elSel.remove(i);
  		}
	}
}

if("${experimentType.hook}" != document.getElementById("experimentType").value){
	//setting the SELECT failed, give up
} else {
	expt_exptypeOnchange();
	//fire its onchange event to kick off the AJAX - but only if the value matches
	//what we set, otherwise the right exp type wasn't in the SELECT!
	setTimeout(autoSelectProtocol,500); //wait half a second before	trying to set the protocol
}
</script>
    
    <hr/>
    <pimsForm:formBlock>
    <div id="formfields3">
If you want to upload the experiment data in a spreadsheet, choose the file here:<br/><br/>
Spreadsheet: <input type="file" name="spreadsheet"  />
</div>
    </pimsForm:formBlock>  
    <pimsForm:formBlock>
        <pimsForm:submitCreate onclick="dontWarn();"/>
    </pimsForm:formBlock>
    <hr/>
    <pimsForm:formBlock>
    <div id="formfields3">
To get a suitable spreadsheet click here: <input name="GetSpreadsheet" type="submit" value="Get Spreadsheet" onclick="dontWarn()"  />
    </div>        
    </pimsForm:formBlock>
    
</pimsForm:form>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
