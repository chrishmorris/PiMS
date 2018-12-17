<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<jsp:useBean id="sequenceplates" scope="request" type="java.util.Collection<org.pimslims.presentation.plateExperiment.PlateBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record Sequencing Results' />
</jsp:include>

<c:catch var="error">

<c:if test="${! empty param.linkto}" >
	<c:set var="prm" value="?linkto=${param.linkto}" />
</c:if>

<h2>Update a Plate Experiment with Sequencing Results</h2>

<div style="padding-left: 1em;" class="tabcontent"> 
  <form action="${pageContext.request.contextPath}/SequenceResult" method="post" enctype="multipart/form-data" 
        name="myForm" onSubmit="return submitForm(this);">
    <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,'/SequenceResult')}" />

	<p>
	Please select results file<br />
	Browse to the zip file provided by the sequencing contractor.
	</p>
	
	<pimsWidget:box title="Results Zip File" initialState="open">
	
		<table>
		<tr>
		<th style="width:10em;">File</th>
		<th>Description</th>
		</tr>
	
		<tr>
		<td style="width:10em;white-space:nowrap">Upload a file: 
			<input onchange="onActivate(document.myForm);" style="" type="file" name="file" id="file" /></td>
		<td><input style="width:99%" ${readonly} type="text" name="fileDescription" 
			id="fileDescription" maxlength="253" value="${descrValue}" /></td>
		</tr>
	
	</table>

	</pimsWidget:box> 
	
	<p>
	Please select the Plate experiment from the list below.
	</p>

	<pimsWidget:box title="Plate Sequence Experiments" initialState="open">
      
      <div class="formrow">
		<c:if test="${! empty sequenceplates}">
			<table class="list">
				<tr>
					<th>&nbsp;</th><!-- Empty cell above view icons  -->
					<th>Name</th><th>Select</th>
				</tr>
				<c:forEach var="plate" items="${sequenceplates}"> 
				<tr>
				<td style="padding:2px 0 0 3px;width:20px;">
	    	        <pimsWidget:link bean="${plate}" />
				</td>
				<td><c:out value="${plate.name}" /></td>
				
				<td style="text-align:center;">
		      		<input onchange="onActivate(document.myForm);" type="radio" name="sequenceplate" value="${plate.hook}" />
				</td>
				</tr>
	  			</c:forEach>
			</table>
		</c:if>
      </div>
    </pimsWidget:box>
      
    <p>
	When both the file and the plate have been selected, press the Upload File button.<br>
	The file will be attached to the plate experiment and the sequence results collated to the sequence parameter.
	</p>
	
	<input type="submit" value="Upload File" name="submit" />
	
  </form>
</div> 

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p> </c:if>
    
<script type="text/javascript">
<!--
function onLoadPims() {
	document.myForm.submit.disabled=true;
}

function onActivate(myform) {
	
	var radio_value = get_radio_value(myform);
    
    /* alert("onActivate ["+myform.file.value+":"+radio_value+"]"); */
    
	if (''==document.myForm.file.value) {
		return;
	}
	if (''==radio_value) {
		return;
	}
	myform.submit.disabled=false;
	return;
}

function get_radio_value(myform) {
	var radio_value = "";
	for (var i = 0; i < myform.elements.length; i++ ) {
		if (myform.elements[i].type == 'radio') {
        	if (myform.elements[i].checked == true) {
            	radio_value = myform.elements[i].value + ' ';
        	}
    	}
    }
   	return radio_value;
}

function submitForm(myform) {

	var radio_value = get_radio_value(myform);
	if (''==radio_value) {
		return false;
	}
	var action = myform.action+"/"+radio_value;
	myform.action=action;
    return true;
} // -->
</script>
    
<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
