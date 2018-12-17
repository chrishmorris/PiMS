<%--
Create Plate form
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page import="org.pimslims.model.sample.*" %>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Record a new plate experiment" />
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
	parameters:"isAJAX=true&isPLATE=true&experimentType="+val,
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
	validSpreadsheet();
		
    var group=document.getElementById("groupName");
    if(""!=group.value && (undefined==group.lastSuggested || group.lastSuggested!=group.value)){
    	return false;
    }
	getSuggestedExperimentName();
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

function checkForm(elem) { 
	
    var frm=elem.form; //not used at present. Added to make any future work easier.

    var selectedHolderType=$("holderType").value;
    if(""==selectedHolderType || !holderTypes[selectedHolderType]){
        //Holder type isn't selected; bail and let the form validation catch it and tell the user.
        //Oddly, this means returning true even though we aren't happy with the form; returning
        //true allows the event to bubble up to the form, whose onsubmit does the validation.
        return true;
    }
	
    var filename=$("spreadsheet").value;
    if(""!=filename && !filename.endsWith(".csv")){
        alert("The chosen file ("+filename+") does not appear to be a "
                +"CSV (Comma-Separated Values) file. Please ensure that you save your"
                +"spreadsheet as a CSV, with the '.csv' file extension.");
        return false; //no point continuing.
    }
	
	var count="${experimentCount}";
	var maxRow = holderTypes[selectedHolderType]["maxRow"];
	var maxCol = holderTypes[selectedHolderType]["maxColumn"]
	var plateWells = maxRow*maxCol;

	var plateCount=0;
	var NWField = document.getElementById('plateId_NW');
	if(!NWField.disabled) {
		plateCount++;
	}	
	var NEField = document.getElementById('plateId_NE');
	if(!NEField.disabled) {
		plateCount++;
	}	
	var SWField = document.getElementById('plateId_SW');
	if(!SWField.disabled) {
		plateCount++;
	}	
	var SEField = document.getElementById('plateId_SE');
	if(!SEField.disabled) {
		plateCount++;
	}

	var groupWells = plateWells*plateCount
	
	if(groupWells<count) {
		alert("Too few wells for group experiments.\n\nCheck that the 'plate type' has the same number of wells as the plates used in the last experiment.");
		return false;
	}
	
	return true;
}

var holderTypes={
	         	<c:forEach items="${holderTypes}" var="elem">
	               "${elem.hook}":{ 
	         		name:"${elem.name}",
	         		hook:"${elem.hook}",
	         		maxRow:"${elem.maxRow}",
	         		maxColumn:"${elem.maxColumn}"
	         	},           
	         	</c:forEach>
	         	"dummy":{ /* prevent trailing-comma errors */ }
}

var inputPlateMaxRow;
var inputPlateMaxCol;
<c:if test="${!empty inputPlateMaxRow}">inputPlateMaxRow = ${inputPlateMaxRow};</c:if>
<c:if test="${!empty inputPlateMaxCol}">inputPlateMaxCol = ${inputPlateMaxCol};</c:if>

function setRequiredPlateIDs(sel){
	if(undefined==inputPlateMaxRow || undefined==inputPlateMaxCol){
	    return false;
	}
	var hook=$(sel).value;
	if(""==hook) return false;
    var rows=holderTypes[hook].maxRow;
    var cols=holderTypes[hook].maxColumn;

    if(rows>inputPlateMaxRow && cols>inputPlateMaxCol){
        // need this layout: ** --
        //                   -- --
    	$("plateId_NW").disabled="";
    	$("plateId_NW").validation.required=false;        
        $("plateId_SW").disabled="disabled";
        $("plateId_SW").validation.required=false;        
        $("plateId_NE").disabled="disabled";
        $("plateId_NE").validation.required=false;        
        $("plateId_SE").disabled="disabled";
        $("plateId_SE").validation.required=false;        
    } else if(rows*2>inputPlateMaxRow && cols>inputPlateMaxCol){
        // need this layout: ** --
        //                   ** --
        $("plateId_NW").disabled="";
        $("plateId_NW").validation.required=true;        
        $("plateId_SW").disabled="";
        $("plateId_SW").validation.required=true;        
        $("plateId_NE").disabled="disabled";
        $("plateId_NE").validation.required=false;        
        $("plateId_SE").disabled="disabled";
        $("plateId_SE").validation.required=false;        
    } else if(rows>inputPlateMaxRow && cols*2>inputPlateMaxCol){
        // need this layout: ** **
        //                   -- --
        $("plateId_NW").disabled="";
        $("plateId_NW").validation.required=true;        
        $("plateId_NE").disabled="";
        $("plateId_NE").validation.required=true;        
        $("plateId_SW").disabled="disabled";
        $("plateId_SW").validation.required=false;        
        $("plateId_SE").disabled="disabled";
        $("plateId_SE").validation.required=false;        
    } else if(rows*2>inputPlateMaxRow && cols*2>inputPlateMaxCol){
        // need this layout: ** **
        //                   ** **
        $("plateId_NW").disabled="";
        $("plateId_NW").validation.required=true;        
        $("plateId_NE").disabled="";
        $("plateId_NE").validation.required=true;        
        $("plateId_SW").disabled="";
        $("plateId_SW").validation.required=true;        
        $("plateId_SE").disabled="";
        $("plateId_SE").validation.required=true;        
    } else {
        // can't fit previous experiments into
        // a 2x2 layout with this plate type
        alert("Can't map previous experiments using this plate type.\n\nFour of these would give "+
                2*holderTypes[hook].maxRow +"x"+ 2*holderTypes[hook].maxColumn+ "wells, but the previous "+
                "experiments need at least "+inputPlateMaxRow+"x"+inputPlateMaxCol+". Please choose a "+
                "type of plate with more wells.");
        return false;
    }
}

function validSpreadsheet(){
	//alert("checkSpreadsheet("+elem+")");
	
	var prField = $("protocol");
	var spField = $("spreadsheet");
	
	if(""==prField.value || ""==spField.value){
        return false; //no point continuing.
    }
	
    if(""!=spField.value && !spField.value.endsWith(".csv")){
    	highlightField(spField.name);
        setAjaxError(spField.name);
        alert("The chosen file ("+spField.value+") does not appear to be a "
                +"CSV (Comma-Separated Values) file. Please ensure that you save your"
                +"spreadsheet as a CSV, with the '.csv' file extension.");
        return false; //no point continuing.
    }
	
	clearAjaxError(spField.name);
	spField.style.backgroundColor='#ffffff';
    
	var sCreate = document.getElementById('submitCreate');
	sCreate.disabled=true;
	var frm=spField.form;	
	
	var el = document.createElement("input");
	el.type = "hidden";
	el.name = "checkSpreadsheet";
	el.id = "checkSpreadsheet";
	el.value = "true";
	frm.appendChild(el);
	//DisplayFormValues();
	
	frm.target="upload_target";
	frm.submit();

	setTimeout('wakeUp()', 500);
	frm.removeChild(el);
}

function wakeUp() {
	
	var iFrame = document.getElementById('upload_target');
	var iContent = iFrame.contentWindow.document.body.innerHTML;
	var myString = iContent.substring(5);
	var messageString = myString.substring(0, myString.length - 7);
	if (messageString) {
		alert("Warning, Labels in this spreadsheet do not match the protocol\n\n" + messageString);
	}
	var sCreate = document.getElementById('submitCreate');
	sCreate.disabled=false;
}

function DisplayFormValues() { 
	 
	var elem = document.getElementById('createplateform').elements; 
	for(var i = 0; i < elem.length; i++) { 
		var str = '';
		str += "[Type:" + elem[i].type; 
		str += " Name:" + elem[i].name; 
		str += " Value:" + elem[i].value +"]"; 
		alert(str);
	} 
} 

</script>

<pimsWidget:pageTitle icon="plate.png" title="New plate experiment" />

<pimsWidget:box title="Details of new plate experiment" initialState="open">
<pimsForm:form mode="edit" id="createplateform" action="/CreatePlate" method="post" enctype="multipart/form-data">
    <pimsForm:formBlock>
    <h4>Basic details</h4>
        <pimsForm:column1>
                        
            <pimsForm:select alias="Experiment type" name="experimentType" validation="required:true" onchange="expt_exptypeOnchange()">
            	<pimsForm:option optionValue="" currentValue="${experimentType._Hook}" alias="Choose.." />
            	<c:forEach items="${experimentTypes}" var="elem">
           			<pimsForm:option optionValue="${elem.hook}" currentValue="${experimentType._Hook}" alias="${elem.name}" />
         		</c:forEach>
            </pimsForm:select>
            
            <pimsForm:select alias="Protocol" name="protocol" validation="required:true" onchange="expt_protocolOnchange()">
                <c:if test="${!empty protocol}"><option value="${protocol._Hook}">${protocol.name}</option></c:if>
            </pimsForm:select>
            
            <pimsForm:text alias="Group name" name="groupName" onchange="ajax_validate(this,'org.pimslims.model.experiment.ExperimentGroup','name');" validation="required:true" /> 
            <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook this plate belongs to" objects="${accessObjects}" validation="required:true" />
       
        </pimsForm:column1>
        
        <pimsForm:column2>
            <pimsForm:date alias="Start date" name="startDate"  validation="required:true, date:true" />        
            <pimsForm:date alias="End date" name="End Date" validation="required:true,date:true,custom:function(val,alias){ return isNotBeforeOtherDate(val,alias,'startDate','Start date') }"/>        
            <pimsForm:select alias="Plate type" name="holderType" validation="required:true" onchange="setRequiredPlateIDs(this)">
            	<pimsForm:option optionValue="" currentValue="${inputPlate.holderType._Hook}" alias="Choose.." />
            	<c:forEach items="${holderTypes}" var="elem">
           			<pimsForm:option optionValue="${elem.hook}" currentValue="${inputPlate.holderType._Hook}" alias="${elem.name}" />          		
         		</c:forEach>
            </pimsForm:select>
        
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

if ("${experimentType._Hook}") {
	document.getElementById("experimentType").value="${experimentType._Hook}";
	var elSel = document.getElementById('experimentType');
	var i;
	for (i = elSel.length - 1; i>=0; i--) {
  		if (!elSel.options[i].selected) {
    		elSel.remove(i);
  		}
	}
}

if("${experimentType._Hook}" != document.getElementById("experimentType").value){
	//setting the SELECT failed, give up
} else {
	expt_exptypeOnchange();
	//fire its onchange event to kick off the AJAX - but only if the value matches
	//what we set, otherwise the right exp type wasn't in the SELECT!
	setTimeout(autoSelectProtocol,500); //wait half a second before	trying to set the protocol
}

// Always fire this. If expt type is cached by the browser (eg back button), there are 
// no protocols - they have to be populated. If not, the function returns false anyway.
expt_exptypeOnchange();
</script>
    
    <hr/>


    <pimsForm:formBlock>
    <h4>Plate ID(s)</h4>
        <table style="width:auto">
            <tr>
                <td><input type="text" id="plateId_NW" style="margin-right: 1.8em;" name="plateId_NW" onchange="ajax_exists(this,'org.pimslims.model.holder.Holder','name');" /></td>
                <td><input type="text" id="plateId_NE" name="plateId_NE" onchange="ajax_exists(this,'org.pimslims.model.holder.Holder','name');" /></td>
            </tr>
            <tr>
                <td><input type="text" id="plateId_SW" name="plateId_SW" onchange="ajax_exists(this,'org.pimslims.model.holder.Holder','name');" /></td>
                <td><input type="text" id="plateId_SE" name="plateId_SE" onchange="ajax_exists(this,'org.pimslims.model.holder.Holder','name');" /></td>
            </tr>
        </table>
    </pimsForm:formBlock>
    
    <%-- 
    Set up plate name fields. If this is a follow-on experiment (from "Next experiment" in a previous group),
    the plate positions have to match, so we disable those fields with no corresponding plate in the last
    group and make the others required. 
    
    Otherwise, this is just a normal Create, and the user is free to enter names in any position.
    
    The servlet supplies, for example, ${northWestPlateName} - these are the names from the LAST experiment
    group, NOT defaults for the one we are creating here.
    --%>
    <c:choose>
        <c:when test="${empty northWestPlateName && empty northEastPlateName && empty southWestPlateName && empty southEastPlateName}">
            <%-- This is a normal create, no validation needed. Force all fields to be enabled. --%>
            <script type="text/javascript">
            document.getElementById("plateId_NW").disabled="";
            document.getElementById("plateId_NW").validation = {};
            document.getElementById("plateId_NE").disabled="";
            document.getElementById("plateId_NE").validation = {};
            document.getElementById("plateId_SW").disabled="";
            document.getElementById("plateId_SW").validation = {};
            document.getElementById("plateId_SE").disabled="";
            document.getElementById("plateId_SE").validation = {};
            </script>
        </c:when>
        <c:when test="${!empty northWestPlateName && empty northEastPlateName && empty southWestPlateName && empty southEastPlateName}">
            <%-- Previous experiment was a one-plate group. 
                 Block out all but north-west, but don't make it required. Default is to name the plate the same as the group. 
            --%>
            <script type="text/javascript">
            document.getElementById("plateId_NW").disabled="";
            document.getElementById("plateId_NW").validation = {};
            document.getElementById("plateId_NE").disabled="disabled";
            document.getElementById("plateId_NE").validation = {};
            document.getElementById("plateId_SW").disabled="disabled";
            document.getElementById("plateId_SW").validation = {};
            document.getElementById("plateId_SE").disabled="disabled";
            document.getElementById("plateId_SE").validation = {};
            </script>
        </c:when>
        <c:otherwise>
            <%-- Previous experiment was either multiple plates, or one plate not in NW (and therefore not the default).
                 For each name box, if there is a corresponding plate make it required, otherwise make it disabled. 
            --%>
            <script type="text/javascript">
            <c:choose><c:when test="${empty northWestPlateName}">
                document.getElementById("plateId_NW").disabled="disabled";
		    </c:when><c:otherwise>
		        document.getElementById("plateId_NW").disabled="";
		        document.getElementById("plateId_NW").validation = { required:true, alias:"North-west plate ID" }
		    </c:otherwise></c:choose>
		    <c:choose><c:when test="${empty northEastPlateName}">
		        document.getElementById("plateId_NE").disabled="disabled";
			</c:when><c:otherwise>
			    document.getElementById("plateId_NE").disabled="";
			    document.getElementById("plateId_NE").validation = { required:true, alias:"North-east plate ID" }
			</c:otherwise></c:choose>
            <c:choose><c:when test="${empty southWestPlateName}">
                document.getElementById("plateId_SW").disabled="disabled";
            </c:when><c:otherwise>
                document.getElementById("plateId_SW").disabled="";
                document.getElementById("plateId_SW").validation = { required:true, alias:"South-west plate ID" }
            </c:otherwise></c:choose>
            <c:choose><c:when test="${empty southEastPlateName}">
                document.getElementById("plateId_SE").disabled="disabled";
            </c:when><c:otherwise>
                document.getElementById("plateId_SE").disabled="";
                document.getElementById("plateId_SE").validation = { required:true, alias:"South-east plate ID" }
            </c:otherwise></c:choose>
			</script>
        </c:otherwise>        
    </c:choose>

    <script type="text/javascript">
    document.getElementById("plateId_NW").validation.alias="North-west plate ID";
    document.getElementById("plateId_NE").validation.alias="North-east plate ID";
    document.getElementById("plateId_SW").validation.alias="South-west plate ID";
    document.getElementById("plateId_SE").validation.alias="South-east plate ID";
    </script>
    <hr/>
    <pimsForm:formBlock>
    <div id="formfields3">
If you want to upload the experiment data in a spreadsheet, choose the file here:<br/><br/>
Spreadsheet: <input type="file" name="spreadsheet" id="spreadsheet" onchange="validSpreadsheet()"/>
	<iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;"></iframe>
</div>
    </pimsForm:formBlock>

    <pimsForm:formBlock>
        <!-- When the form is submitted with this button, we DO want to disable all the buttons - PIMS-3308 -->
        <pimsForm:submitCreate name="submitCreate" onclick="suppressButtonDisablingOnForm('createplateform',false);dontWarn(); return checkForm(this)"/>
    </pimsForm:formBlock><hr/>
    <pimsForm:formBlock>
    <div id="formfields3">
        <!-- When the form is submitted with this button, we DO NOT want to disable all the buttons - PIMS-3308 -->
To get a suitable spreadsheet click here: <input name="GetSpreadsheet" type="submit" value="Get Spreadsheet" onclick="suppressButtonDisablingOnForm('createplateform',true);dontWarn()"  />
    </div>        
    </pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>


<script type="text/javascript">
var htsel=$("holderType");
Object.keys(holderTypes).each(function(hook){
    var opt=htsel.down("option[value='"+hook+"']");
    if(opt){
        var ht=holderTypes[hook];
        opt.maxRow=ht.maxRow*1;
        opt.maxCol=ht.maxColumn*1;
    }
});
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>


<jsp:include page="/JSP/core/Footer.jsp" />
