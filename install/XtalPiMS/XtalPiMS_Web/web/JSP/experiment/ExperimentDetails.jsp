<%@ page language="java" contentType="text/html; charset=utf-8"
    %>
<%-- Standard PiMS objects for page --%>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${bean.experimentTypeName}: ${bean.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- ExperimentDetails.jsp  -->
<%-- obsolete, move this into experimentTabs, this file adds little value --%>
<c:catch var="error">
<div>
   <jsp:include page="/JSP/experiment/experimentTabs.jsp" />
</div>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>


<script type="text/javascript">//<!--

function onLoadPims(){
	//if we can set the status of the expt.
	if($("${modelObject.hook}:status" && $("_milestoneAchieved"))){
		//attach the onchange handler
		$("${modelObject.hook}:status").observe("change",function(){
			statusOnchange();
		});
		$("${modelObject.hook}:researchObjective").observe("change",function(){
			statusOnchange();
		});
		statusOnchange(); //set the checkbox to match the status
	}
}

function statusOnchange(){
    var milestoneFormitemDiv=$("_milestoneAchieved").up("div.formitem");
	if("OK"==$("${modelObject.hook}:status").value && "[none]"!=$("${modelObject.hook}:researchObjective").value.toLowerCase() ){
		milestoneFormitemDiv.style.visibility="visible";
		$("_milestoneAchieved").disabled="";
	} else {
		$("_milestoneAchieved").disabled="disabled";
		milestoneFormitemDiv.style.visibility="hidden";
	}
}

function datesCheck(element) {
    var frm = element;
    if(element.form) frm=element.form;
  	var inputs=Element.extend(frm).getInputs();
  	var sdate;
  	var edate;
  	//iterate through inputs
  	inputs.each(function(input){

  		if (input.name.match("startDate"))
  			sdate = getDate(input.value);

		if (input.name.match("endDate"))
  			edate = getDate(input.value);

  	});
  	//end iterate
  	//alert ("start date ["+sdate.toLocaleString()+"]");
  	//alert ("end date ["+edate.toLocaleString()+"]");
  	if (sdate > edate) {
  		alert("start date can not be after end date");
    	return false;
    }
    return true;
}

function getDate(datestring) {
	if(!isDateFormat(datestring)) { return false; }
  	var year = datestring.substr(6,4);
 	var month = datestring.substr(3,2);
  	var day = datestring.substr(0,2);
  	var date = new Date(year,month-1,day);
  	return date;
}

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
 * obsolete? Now use MRU handling functions in widgets.js
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

function experiment_checkNumOutputs(){
}
//--></script>


<jsp:include page="/JSP/core/Footer.jsp" />
