<!-- experimentAttributes.jsp -->


<%--Current user name and MRU --%>
<%@page import="org.pimslims.model.target.Target"%>
<%@page import="org.pimslims.model.experiment.Experiment"%>
<%@page import="org.pimslims.model.target.ResearchObjective"%>
<%@page import="org.pimslims.presentation.mru.MRUController"%>
<%@page import="org.pimslims.presentation.mru.MRU"%>
<%@page import="org.pimslims.servlet.PIMSServlet"%>
<%@page import="java.util.*"%>

<%@taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<jsp:useBean id="modelObject" scope="request" type="org.pimslims.presentation.ModelObjectBean" />
<jsp:useBean id="experimentType" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<jsp:useBean id="milestoneAchieved" scope="request" type="java.lang.Boolean" />

<c:set var="pimsFormSelectValue" value="from page" />


<pimsForm:form id="tabsForm" action="/update/ExperimentUpdate" mode="view" method="post">
<pimsForm:formBlock>

<pimsForm:column1>
    <pimsForm:text validation=""
       alias="Page Number" helpText="Cross reference to paper lab note book" 
        name="${bean.hook}:pageNumber" value="${bean.values['pageNumber']}"
    />
	<pimsForm:text name="${modelObject.hook}:name" value="${modelObject.values['name']}" alias="Name" validation="required:true, unique:{obj:'org.pimslims.model.experiment.Experiment',prop:'name'}" />
	<pimsForm:link bean="${experimentType}" name="experimentType" alias="Type" />
	<c:if test="${!empty protocol}">
	<pimsForm:link bean="${protocol}" name="protocol" alias="Protocol" />
	
	<pimsForm:nonFormFieldInfo label ="Lab Notebook" >
                    <c:out value="${modelObject.access.name}" />
    </pimsForm:nonFormFieldInfo>
		
	</c:if>
	
		<pimsForm:select name="${modelObject.hook}:status" alias="Status"  >            
		<pimsForm:option optionValue="To_be_run" currentValue="${modelObject.values['status'] }" alias="To be run" />                   
		<pimsForm:option optionValue="In_process" currentValue="${modelObject.values['status'] }" alias="In process"/>
		<pimsForm:option optionValue="OK" currentValue="${modelObject.values['status'] }" alias="OK"/>
		<pimsForm:option optionValue="Failed" currentValue="${modelObject.values['status'] }" alias="Failed"/>
	</pimsForm:select>

	<c:if test="${!empty milestoneName}">
		<c:set var="isAchieved" value="" />
		<c:if test="${milestoneAchieved}"><c:set var="isAchieved" value="checked='checked'" /></c:if>
	    <div class="formitem">
	      <div class="fieldname"><label for="_milestoneAchieved">
              Milestone 
          </label></div>
          <div class="formfield"><input type="checkbox" ${isAchieved} disabled="disabled" name="_milestoneAchieved" id="_milestoneAchieved" /> <c:out value="${milestoneName}"/> achieved</div>
        </div>
	</c:if>
 
</pimsForm:column1>
<pimsForm:column2>
	<pimsForm:date name="${modelObject.hook}:startDate" alias="Start date" value="${modelObject.values['startDate']}" validation="required:true,date:true" />
	<pimsForm:date name="${modelObject.hook}:endDate" alias="End date" value="${modelObject.values['endDate']}" validation="required:true,date:true,custom:function(val,alias){ return isNotBeforeOtherDate(val,alias,'${modelObject.hook}:startDate','Start date') }" /> 
	<pimsForm:mruSelect hook="${modelObject.hook}" rolename="researchObjective" alias="Project" helpText="The construct or target under investigation" required="${false}" />
	<pimsForm:writer  hook="${modelObject.hook}" rolename="creator" required="${false}" alias="Scientist" helpText="The scientist responsible for the Target" />
	<c:if test="${!mayUpdate}">Owner: ${owner}</c:if>
</pimsForm:column2>
</pimsForm:formBlock>

<pimsForm:formBlock>
	<pimsForm:textarea name="${modelObject.hook}:details" alias="Details"><c:out value="${modelObject.values['details']}" /></pimsForm:textarea>
</pimsForm:formBlock>

<pimsForm:column2>
	<pimsForm:editSubmit isNext="${'To_be_run' eq modelObject.values['status'] }" />
</pimsForm:column2>

</pimsForm:form>

    
<!-- /experimentAttributes.jsp -->
