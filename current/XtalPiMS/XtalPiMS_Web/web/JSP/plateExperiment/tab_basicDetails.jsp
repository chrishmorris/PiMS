<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:useBean id="groupBean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />
<!-- tab_basicDetails.jsp -->

<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.ExperimentGroup">Plate Experiments</a>
    
    <c:forEach items="${ currentLocationTrail }" var="container" varStatus="status" >
      : <pimsWidget:link bean="${ container }" /> 
      <c:set var="containerName" value="${ container.name }" />
    </c:forEach>
</c:set>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/move.js"></script>
<c:set var="actions">

	<%-- custom deleteLink --%>
	<pimsWidget:deleteLink bean="${groupBean}" />
	<c:if test="${isPlateExperiment && mayUpdate}">    
	  <c:choose><c:when test="${empty currentLocationTrail}">
        <a href="#" 
        onclick="containerSearch({hook:'${holderBean.hook}', name:'<c:out value="${holderBean.name}" />'}); return false;"
          title="Record where the plate is" 
        >Move</a>	     
	  </c:when><c:otherwise>
	    <a href="#" onclick="submitRemove( '${utils:escapeJS(holderBean.name)}','${holderBean.hook}','${containerName}');return false"
               title="Remove <c:out value="${holderBean.name}" /> from <c:out value="${containerName}" />"
            >Remove</a>
      </c:otherwise></c:choose>
    	&nbsp;
    </c:if>  
    
    <pimsWidget:diagramLink hook="${groupBean.hook}"  />
    
    <c:set var="safeName"><c:out value="${groupBean.name}"/></c:set>
    <pimsWidget:linkWithIcon text="Progress report" title="Export cohort outcomes to spreadsheet (in CSV format)"
        url="${pageContext.request.contextPath}/read/OutcomesCsv/${groupBean.hook}/${safeName} Outcomes.csv"
        icon="actions/export.gif" />

    <c:choose><c:when test="${isPlateExperiment}">    
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/experiment/plate/HelpCreatePlateExperiment.jsp#plateView" />
    </c:when><c:otherwise>
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/experiment/ExperimentGroup.jsp" />
    </c:otherwise></c:choose>

     
</c:set>

		<c:choose>
		<c:when test="${isPlateExperiment}"> 
		<c:set var="icon" value="plate.png" />
		</c:when><c:otherwise>
		<c:set var="icon" value="experimentgroup.png" />
		</c:otherwise>
		</c:choose> 
		
        <pimsWidget:pageTitle  breadcrumbs="${breadcrumbs }" title="${groupBean.name}" icon="${icon}" actions="${actions}" />
        <pimsForm:form action="/update/BasicUpdatePlateExperiment" mode="view" method="post">
            <pimsForm:formBlock>
                <pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${groupBean.hook}:pageNumber" value="${groupBean.values['pageNumber']}"
    />
                    <pimsForm:text alias="Group name" name="${groupBean.hook}:name" value="${groupBean.name}" validation="required:true" />
					 
                    <pimsForm:nonFormFieldInfo label="Protocol"><pimsWidget:link bean="${protocolBean}"/></pimsForm:nonFormFieldInfo>
   					<pimsForm:nonFormFieldInfo label="Lab Notebook"><c:out value="${groupBean.access.name}" /></pimsForm:nonFormFieldInfo>
   					
                </pimsForm:column1>
                <pimsForm:column2> 
                    <pimsForm:date alias="Start date" name="${groupBean.hook}:startDate" value="${groupBean.values['startDate']}" validation="required:true, date:true" />
                    <pimsForm:date alias="End date" name="${groupBean.hook}:endDate" value="${groupBean.values['endDate']}" validation="required:true,date:true,custom:function(val,alias){ return isNotBeforeOtherDate(val,alias,'${groupBean.hook}:startDate','Start date') }"/>
                    
                    <c:set var="isActive" value="${isActive}" />
			    	<pimsForm:radio name="${groupBean.hook}:isActive" value="true" label="Yes" isChecked="${isActive eq 'true'}" alias="Samples active" />
					<pimsForm:radio name="${groupBean.hook}:isActive" value="false"  label="No"  isChecked="${isActive ne 'true'}" alias="" />
                    
                </pimsForm:column2>
            </pimsForm:formBlock>
            <pimsForm:formBlock>
            	<c:if test="${isPlateExperiment}">
            		<pimsForm:nonFormFieldInfo label="Plate type"><pimsWidget:link bean="${holderTypeBean}"/></pimsForm:nonFormFieldInfo>                	
                </c:if>
            </pimsForm:formBlock>
            <pimsForm:formBlock>
                <pimsForm:editSubmit />
            </pimsForm:formBlock>
     	</pimsForm:form> 

        <hr/>
     	 
                <h3>Export to spreadsheet (.csv)</h3>
	            <form method="get"
                  action="${pageContext.request.contextPath}/read/PlateExperimentCsv/${groupBean.hook}/<c:out value='${groupBean.name}' />.csv"
                  >                                 
                    <input type="submit" value="Export" 
                    title="Click to get data about this plate as spreadsheet (in CSV format)"
                    />
	            </form>
	            <hr />
	            <%-- TODO <h3>Export to spreadsheet (.xlsx)</h3>
	            <form method="get" enctype="multipart/form-data"
	              action="${pageContext.request.contextPath}/read/PlateExperimentXls/${groupBean.hook}/<c:out value='${groupBean.name}' />.xlsx"
	            >
	                Template (.xltx): <input type="file" name="template" title="(Optional) the data will be merged into this template" />	                
	                <input type="submit" value="Export" 
                    title="Click to get data about this plate as spreadsheet (in XLSX format)"
                    />
	            </form><hr /> --%>
                <h3>Import from spreadsheet (.csv)</h3>
        <form action="${pageContext.request.contextPath}/update/UpdatePlateFromSpreadsheet/${groupBean.hook}"  method="post" enctype="multipart/form-data">
    <%-- TODO CSRF token --%> 
        
			  	<input type="file" name="spreadsheet" id="spreadsheet" title="edited .csv file" />
			  	<input class="button" value="Save" type="submit" onclick="return confirmCorrectSpreadsheet()" 
			  	/>
			  	</form>

		    <script type="text/javascript">
		    attachValidation("spreadsheet", {required:true, alias:"Spreadsheet"} );

		    function confirmCorrectSpreadsheet(){
			    var filename=$("spreadsheet").value;
                if(""==filename){
                    return true; //and let the normal form validation deal with it
                }
                if(!filename.endsWith(".csv")){
                    alert("The chosen file ("+filename+") does not appear to be a "
                            +"CSV (Comma-Separated Values) file. Please ensure that you save your"
                            +"spreadsheet as a CSV, with the '.csv' file extension.");
                    return false; //no point continuing.
                }
		        var message="You are about to upload this spreadsheet:\n\n";
		        message+=filename;
		        message+="\n\ninto experiment group:\n\n<c:out value="${groupBean.name}" />.";
			    message+="\n\nThis will overwrite all details currently saved in PiMS, and replace them with those in the spreadsheet.";
                message+="\n\nClick OK to continue with the upload.";
                message+="\nClick Cancel to cancel the upload.";
		        return confirm(message);
			}
		    </script>
            
        
        <c:if test="${!isPlateExperiment}"> 
        	<hr/>   
        	<pimsForm:form action="/update/AddExperimentToGroup/${groupBean.hook}" mode="view" method="post"> 
            <input type="hidden" name="group" value="${groupBean.hook}"/>
            <pimsForm:formBlock>
            <pimsForm:column1>
	            <input type="submit" value="Add Experiment to Group"/>
            </pimsForm:column1>
    		</pimsForm:formBlock>
    		</pimsForm:form>
    	</c:if>
<!-- /tab_basicDetails.jsp -->       
             
