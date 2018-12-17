<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Selecting a crystal from a well and mounting it on a pin,
with optional in-well cryoprotection.

Author: Ed Daniel
Date: April 2012
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%-- 
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
--%>
<c:catch var="error">

<c:set var="latestExperiment" value="" />
<c:set var="lastOutputSample" value=""/>
<c:if test="${not empty selectedCrystalSample}">
	<c:set var="lastOutputSample" value="${selectedCrystalSample}"/>
</c:if>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Crystal treatment" />
    <jsp:param name="extraStylesheets" value="../../../css/viewtrialdrops3" />
</jsp:include>
<script type="text/javascript" src="${pageContext.request['contextPath']}/CrystalHandling/treatment.js"></script>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Crystal Treatment: ${barcode}:${well} Crystal ${crystal}"/>
<c:set var="actions"><%-- "Delete" link - only if no following steps --%>
	<c:if test="${!empty crystal && empty experimentChain && empty finalMountExperiment}">
		<pimsForm:form action="/update/SelectCrystal/?barcode=${barcode}&well=${well}&crystal=${crystal}" mode="${formMode}" method="post" id="form_${expt._Hook}">
		<input type="hidden" name="delete" value="Delete" />
		<input type="hidden" name="hook" value="${selectionExperiment.hook}" />
		<input type="hidden" name="hook" value="${selectedCrystalSample}" />
		<pimsWidget:linkWithIcon icon="actions/delete.gif" text="Delete this selection" url="#" onclick="if(confirm('Delete this crystal selection?')){ this.up('form').submit(); } return false;" />
		</pimsForm:form>
	</c:if>
</c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<%-- ************************************************************
Harvest/selection - show image with coordinates marked, read-only
************************************************************ --%>
<c:set var="lastOutputSample" value="${selectedCrystalSample}"/>
<c:set var="harvestReadonly" scope="request" value="true"/>
<jsp:include page="/CrystalHandling/HarvestingBox.jsp" />

<%-- ************************************************************
Protein sequence
************************************************************ --%>
<jsp:include page="/CrystalHandling/SequenceBox.jsp" />

<%-- ************************************************************
Treatment chain: Show all subsequent experiments
************************************************************ --%>

<script type="text/javascript">
var cumulativeDuration=0;<%-- TODO add to this for each experiment. Unit is seconds. --%>
var durationHooks=[];

function setDurationFields(parameterHook){
	var duration=$(parameterHook+":value").value;
	var unit=$(parameterHook+":displayUnit").value;
	var parts=getDurationAsArray(duration,unit);

	$(parameterHook+":days").value=parts['days'];
	$(parameterHook+":hr").value=parts['hr'];
	$(parameterHook+":min").value=parts['min'];
	$(parameterHook+":sec").value=parts['sec'];
	
	if("sec"!=unit){
		$(parameterHook+":sec").value="00";
		$(parameterHook+":sec").readOnly="readonly";
		if("min"!=unit){
			$(parameterHook+":min").value="00";
			$(parameterHook+":min").readOnly="readonly";
			if("hr"!=unit){
				$(parameterHook+":hr").value="00";
				$(parameterHook+":hr").readOnly="readonly";
			}
		}
	}
	$(parameterHook+":duration").innerHTML=getSecondsAsDurationString(parts["total_sec"]);
	return parts['total_sec'];	
}

function parseDurationFields(){
	durationHooks.each(function(hook){
		var seconds=parseInt($(hook+":sec").value);
		seconds+=(60* parseInt($(hook+":min").value));
		seconds+=(3600* parseInt($(hook+":hr").value));
		seconds+=(86400* parseInt($(hook+":days").value));
		$(hook+":value").value=seconds;
	});
}

/** 
 * Initialises all the duration fields and populates the cumulative durations in the box headers
 */
function setAllDurationFields(){
	durationHooks.each(function(hook){
		cumulativeDuration+=setDurationFields(hook);
		$(hook+":value").up(".collapsiblebox").down(".cumulativeduration").innerHTML="Elapsed time: "+getSecondsAsDurationString(cumulativeDuration);
	});
}

/**
 * Returns the supplied duration as an array of days, hr, min, sec, which added together as seconds give total_sec, the supplied duration in seconds
 */
function getDurationAsArray(duration,unit){
	if("sec"==unit){ return getSecondsAsDurationArray(duration); }
	if("min"==unit){ return getSecondsAsDurationArray(duration*600); }
	if("hr"==unit){ return getSecondsAsDurationArray(duration*3600); }
	if("days"==unit){ return getSecondsAsDurationArray(duration*86400); }
}

/**
 * Parses the supplied number of seconds into an array containing days, hr, min, sec, and total_sec (the number of seconds passed in)
 */
function getSecondsAsDurationArray(sec){
	var arr=[];
	arr["total_sec"]=Math.floor(parseInt(sec));
	arr["days"]=Math.floor(sec/86400);
	var remainder=sec % 86400;
	arr["hr"]=Math.floor(remainder/3600);
	remainder=remainder % 3600;
	arr["min"]=Math.floor(remainder/60);
	remainder=remainder % 60;
	arr["sec"]=Math.floor(remainder);
	return arr;
}

/**
 * Returns the supplied number of seconds as a user-friendly string "D days, HH:MM:SS"
 */
function getSecondsAsDurationString(sec){
	var arr=getSecondsAsDurationArray(sec);
	var ret="";
	if(0<arr["days"]){ ret+= arr["days"]+" days "; }
	ret+=padToTwoDigits(arr["hr"])+":";
	ret+=padToTwoDigits(arr["min"])+":";
	ret+=padToTwoDigits(arr["sec"]);
	return ret;
}

function padToTwoDigits(num){
	num=num+"";
	while(num.length<2){
		num="0"+num;
	}
	return num;
}

</script>

<c:if test="${'false'==isBeamline}">
<%-- *******************************
* BEGIN TREATMENT EXPERIMENTS LOOP *
******************************* --%>
<c:forEach var="expt" items="${experimentChain}">

	<c:forEach var="os" items="${expt.outputSamples}">
		<c:set var="lastOutputSample" value="${os.sample._Hook}"/>
	</c:forEach>
	<c:set var="latestExperiment" value="${expt._Hook}" />
	<c:set var="initialState" value="closed" />
	<c:set var="formMode" value="edit" />

	<pimsWidget:box id="${expt._Hook}" title="${expt.protocol.name}" initialState="${initialState}" 
		extraHeader="<span class=\"cumulativeduration\" style=\"color:#006;font-weight:bold;font-size:1.2em;position:absolute;right:1em\">DURATION HERE</span><a class=\"noprint\" href=\"/pims/View/${expt._Hook}\">View experiment in PiMS</a>"
		extraClasses="treatmentexperiment">

	<%-- "Delete" link - only on last experiment in chain--%>
	<c:if test="${expt._Hook eq lastExperiment && empty finalMountExperiment && selectionExperiment.mayUpdate}">
			<pimsForm:form action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}" mode="${formMode}" method="post" id="form_${expt._Hook}">
		<input type="hidden" name="delete" value="Delete" />
		<input type="hidden" name="hook" value="${lastExperiment}" />
		<input type="hidden" name="hook" value="${lastOutputSample}" />
		<div style="text-align:right" class="noprint">
			<pimsWidget:linkWithIcon icon="actions/delete.gif" text="Delete this step" url="#" onclick="if(confirm('Delete this step?')){ this.up('form').submit(); } return false;" />
		</div>
		</pimsForm:form>
	</c:if>

	<pimsForm:form action="/update/CrystalTreatment/" mode="${formMode}" method="post" id="form_${expt._Hook}">
	
	<input type="hidden" name="barcode" value="${barcode}" />
	<input type="hidden" name="well" value="${well}" />
	<input type="hidden" name="crystal" value="${crystal}" />
	<input type="hidden" name="experiment" value="${latestExperiment}" />

	<pimsForm:formBlock>
 	<pimsForm:column1>

	<%-- Input samples --%>
	<c:forEach var="is" items="${expt.inputSamples}">
	<c:if test="${'Crystal' ne is.refInputSample.sampleCategory.name}">
		<c:set var="isb" value="${inputSampleBeans[is._Hook]}"/>
		<pimsForm:nonFormFieldInfo label="${isb.name}">
			<span class="viewonly">
				${isb.amount}&nbsp;
				<c:choose><c:when test="${!empty isb.sampleName}">
					<a href="${pageContext.request.contextPath}/View/${isb.sampleHook}">${fn:escapeXml(isb.sampleName)}</a>
				</c:when><c:otherwise>
					(None)
				</c:otherwise></c:choose>
			</span>
			<span class="editonly">

				<div style="padding-bottom:0.75em;position;relative;top:0;">
				<pimsForm:doAmount hook="${isb.inputSampleHook}" propertyName="amount" value="${isb.amount}" />
				<select name="${isb.inputSampleHook}:sample" id="${isb.inputSampleHook}:sample" class="sample"
	            	onchange="handleSampleSearchOnclick(this,'${inputSampleHook}','${isb.sampleCategoryName}')">
					<c:choose><c:when test="${!empty isb.sampleName}">
						<option value="">(None)</option>
						<option selected="selected" value="${isb.sampleHook}">${isb.sampleName}</option>
					</c:when><c:otherwise>
						<option selected="selected" value="">(None)</option>
					</c:otherwise></c:choose>
			     	 <optgroup label="Search">
						<option value="[SEARCH]">Search Samples</option>
					 </optgroup>
	           </select>
				</div>
			</span>
		</pimsForm:nonFormFieldInfo>
	</c:if>
	</c:forEach>

	<%-- Parameters --%>
	<c:forEach var="parameter" items="${expt.parameters}">
		<%-- Don't show Duration on this side--%>
		<c:if test="${parameter.parameterDefinition.name ne 'Duration'}">
			<c:set var="labelName" value="${parameter.parameterDefinition.name}" />
			<c:if test="${null==parameter.parameterDefinition}">
		        <c:set var="labelName" value="${parameter.name}" />
			</c:if>
			<c:if test="${fn:startsWith(parameter.parameterDefinition.name, '__')}" >
				<c:set var="labelName" value="${parameter.parameterDefinition.label}" />
			</c:if>
			<%@include file="parameter.jsp"%>
		</c:if>
	</c:forEach>
	</pimsForm:column1>
	
	<pimsForm:column2>
	<c:forEach var="parameter" items="${expt.parameters}">
		<%-- Iterate through parameters again, but only show Duration --%>
		<c:if test="${parameter.parameterDefinition.name eq 'Duration'}">
		<pimsForm:nonFormFieldInfo label="Duration">
			<span class="editonly">
			<input type="text" onchange="parseDurationFields('${parameter._Hook}')" style="width:2em" name="${parameter._Hook}:days" id="${parameter._Hook}:days" /> days, 
			<input type="text" onchange="parseDurationFields('${parameter._Hook}')" style="width:2em" name="${parameter._Hook}:hr" id="${parameter._Hook}:hr" /> : 
			<input type="text" onchange="parseDurationFields('${parameter._Hook}')" style="width:2em" name="${parameter._Hook}:min" id="${parameter._Hook}:min" /> : 
			<input type="text" onchange="parseDurationFields('${parameter._Hook}')" style="width:2em" name="${parameter._Hook}:sec" id="${parameter._Hook}:sec" /> 
			</span>
			<span class="viewonly" id="${parameter._Hook}:duration">(Duration)</span>
			<input type="hidden" name="${parameter._Hook}:value" id="${parameter._Hook}:value" value="${parameter.value}" />
			<input type="hidden" name="${parameter._Hook}:displayUnit" id="${parameter._Hook}:displayUnit" value="${parameter.parameterDefinition.displayUnit}" />
			<script type="text/javascript">
			durationHooks.push("${parameter._Hook}");
			</script>
		</pimsForm:nonFormFieldInfo>
		
		</c:if>
	</c:forEach>
	

	<pimsForm:textarea name="${expt._Hook}:details" alias="Comments">${expt.details}</pimsForm:textarea>

	<script type="text/javascript">
	$("form_${expt._Hook}").onsubmit="parseDurationFields()";
	</script>
	
	</pimsForm:column2>
	</pimsForm:formBlock>
	<c:if test="${selectionExperiment.mayUpdate}">
		<pimsForm:formBlock>
			<pimsForm:editSubmit />
		</pimsForm:formBlock>
	</c:if>
	</pimsForm:form>
	</pimsWidget:box> 

</c:forEach>
<%-- ********************************
* END OF TREATMENT EXPERIMENTS LOOP *
******************************** --%>

<script type="text/javascript">setAllDurationFields();</script>

<c:choose><c:when test="${empty sequence or empty acronym}">
	<%-- **************************************************
	User must provide sequence and acronym before continuing
	************************************************** --%>
	<%@include file="/CrystalHandling/FixTargetInfo.jsp" %>	
</c:when><c:when test="${not empty finalMountExperiment}">
	<%-- **********************************************************************
	finished and mounted - "Crystal mounted on pin NNNN" box, and pre-ship form
	********************************************************************** --%>
	<%@include file="/CrystalHandling/PreShipInfoBox.jsp" %>	
	<%--
	<%@include file="/CrystalHandling/CrystalMountedBox.jsp" %>	
	--%>
</c:when><c:otherwise>
	<%-- *******************************************************************
	not finished treatment - "Next experiment" and "Finish treatment" boxes
	******************************************************************* --%>
	<%@include file="/CrystalHandling/NextStepBox.jsp" %>
	<%@include file="/CrystalHandling/FinalMountBox.jsp" %>
</c:otherwise></c:choose>

</c:if><%-- end "if not beamline" --%>


<%-- Images, attachments, notes --%>
<pimsWidget:files bean="${selectionExperiment}" />
<pimsWidget:notes bean="${selectionExperiment}" />
 <script type="text/javascript">focusFirstElement();</script>


<script type="text/javascript">//<!--

var plateBarcode="${barcode}";
var well="${well}";

search=new Object();

var currentSampleSearchSelect=null;

function handleSampleSearchOnclick(sel,inputSampleHook,sampleCategoryName){
	if ('[NONE]'==sel.value) {
		sel.selectedIndex=0;
		top.document.location='../Search/org.pimslims.model.sample.RefSample?sampleCategory='+sampleCategoryName;
	} else if ('[SEARCH]'==sel.value) {
		 sel.selectedIndex=0;
		 showSampleSearch(sel,inputSampleHook, sampleCategoryName);
	}
}

function showSampleSearch(elem,sampleDefID,sampleCategory) {
	currentSampleSearchSelect=elem;
	search.sampleDef=sampleDefID; //unused
    mruToUpdate=elem;
    openModalWindow(contextPath+"/Search/org.pimslims.model.sample.Sample?isInModalWindow=yes&callbackFunction=chooseSample&sampleCategories="+sampleCategory,"Choose sample");
}

/**
 * @deprecated? Now use MRU handling functions in widgets.js
 */
//called by popup iframe
function chooseSample(sampleObject) {
	sampleID  = (null!=sampleObject) ? "sample"+sampleObject.sampleID : "";
	if(null!=sampleObject) {
		sampleObject.sampleID=sampleObject.hook.split(":")[1];
	}

	var elem = currentSampleSearchSelect;
	var numOptions=elem.options.length;
	var found=false;
	for (var j=0;j<numOptions;j++) {
		if (elem.options[j].value == sampleObject.hook) {
			elem.options[j].selected = true;
			found=true;
		}
	}
	if(!found){
		elem.innerHTML+='<option value="'+sampleObject.hook+'">'+sampleObject.name+'</option>';
		elem.selectedIndex=numOptions;
	}

	closeModalWindow();
}

if($("mountform")){ Treatment.initMountForm($("mountform")); }
if($("harvestmountform")){ Treatment.initMountForm($("harvestmountform")); }

var lastPuck=Treatment.getLastPuck();
if(lastPuck){
	if($("mountform") && $("mountform").down("[name='puck']")){
		$("mountform").down("[name='puck']").value=lastPuck;
		Treatment.verifyPuckBarcode($("mountform").down("[name='puck']"));
	}
	if($("harvestmountform") && $("harvestmountform").down("[name='puck']")){
		$("harvestmountform").down("[name='puck']").value=lastPuck;
		Treatment.verifyPuckBarcode($("harvestmountform").down("[name='puck']"));
		$("harvestmountform").down("[name='pin']").focus();
	}
}
//--></script>

<script type="text/javascript">
Treatment.initNav();
</script>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    


<jsp:include page="/JSP/core/Footer.jsp" />