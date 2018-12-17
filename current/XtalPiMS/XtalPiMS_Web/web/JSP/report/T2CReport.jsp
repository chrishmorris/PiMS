<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.pimslims.presentation.sample.SampleBean"%>
<%@page import="org.pimslims.presentation.construct.ConstructBean"%>
<html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<style type="text/css">
   table.experiment {border-width: 1; border: solid; text-align: center; background-color: #dff; }
   table.sample {border-width: 1; border: solid; text-align: center; background-color: #ffd; }
   table.expblueprint {border-width: 1; border: solid; text-align: center; background-color: #fdf; }
 </style>
 
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ page import="java.util.*"  %>
<title>Provenance of Sample: ${sample.name}</title>
</head>
<body>

<!-- T2CReport.jsp -->
<c:catch var="error">
<jsp:useBean id="experiments" scope="request" type="java.util.Collection<org.pimslims.presentation.experiment.ExperimentBean>" />
<jsp:useBean id="images" scope="request" type="java.util.Map<java.lang.String, Object>" />
<jsp:useBean id="expblueprints" scope="request" type="java.util.Collection<org.pimslims.presentation.construct.ConstructBean>" />
<jsp:useBean id="targets" scope="request" type="java.util.Collection<org.pimslims.presentation.TargetBean>" />
<jsp:useBean id="complexes" scope="request" type="java.util.Collection<org.pimslims.presentation.ComplexBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Sample History Report for ${headerTitle}' />
</jsp:include>


<c:forEach var="complex" items="${complexes}"> 

<pimsWidget:box title="Complex: ${complex.name}" initialState="closed" >
	
	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
        <%-- TODO remove --%>
		<pimsForm:text name="Why Chosen" value="${complex.whyChosen}" alias="Why Chosen" />
		<pimsForm:text name="Description" value="${complex.details}" alias="Description" />
	</pimsForm:formBlock>
	</pimsForm:form>
	
</pimsWidget:box>

</c:forEach>


<c:forEach var="target" items="${targets}"> 

<pimsWidget:box title="Target: ${target.name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Organism" value="${target.organismBean.name}" alias="Organism" />
		<pimsForm:text name="Scientist" value="${target.creator.name}" alias="Scientist" />
        <%-- TODO remove --%>
		<pimsForm:text name="Why Chosen" value="${target.whyChosen}" alias="Why Chosen" />
		<pimsForm:text name="Description" value="${target.func_desc}" alias="Description" />
		<pimsForm:text name="Comments" value="${target.comments}" alias="Comments" />
	</pimsForm:formBlock>
	</pimsForm:form>

</pimsWidget:box>

</c:forEach>


<c:forEach var="expblueprint" items="${expblueprints}"> 

<pimsWidget:box title="Construct: ${expblueprint.name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Organism" value="${expblueprint.organismName}" alias="Organism" />
		<%-- TODO remove --%>
		<pimsForm:text name="Why Chosen" value="${expblueprint.whyChosen}" alias="Why Chosen" />
		<pimsForm:text name="Description" value="${expblueprint.description}" alias="Description" />
		<pimsForm:text name="Comments" value="${expblueprint.comments}" alias="Comments" />
		<pimsForm:text name="Forward Primer" value="${expblueprint.fwdPrimerName} - ${expblueprint.fwdPrimer}" alias="Forward Primer" />
		<pimsForm:text name="Reverse Primer" value="${expblueprint.revPrimerName} - ${expblueprint.revPrimer}" alias="Reverse Primer" />
	</pimsForm:formBlock>
	</pimsForm:form>

</pimsWidget:box>

</c:forEach>

 
<c:forEach var="experiment" items="${experiments}"> 
  <pimsWidget:box title="Experiment: ${experiment.name} - ${experiment.experimentProtocolName}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Protocol" value="${experiment.experimentProtocolName}" alias="Protocol" />
		<pimsForm:text name="Details" value="${experiment.details}" alias="Details" />
		<pimsForm:text name="Scientist" value="${experiment.creator}" alias="Scientist" />
		<pimsForm:text name="Start date" value="${experiment.startDateOfExperimentString}" alias="Start date" />
		<pimsForm:text name="End date" value="${experiment.endDateOfExperimentString}" alias="End date" />
	
		<c:forEach var="input" items="${experiment.inputSampleBeans}" varStatus="status">
			<c:choose>
			<c:when test="${status.first}">
			<c:set var="alias" value="Input Samples" />
			</c:when>
			<c:otherwise>
			<c:set var="alias" value="" />
			</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${empty input.sampleHook}">
			        <pimsForm:text name="Input Sample" value="${input.inputSampleName}" alias="${alias}" />
		        </c:when> 
		        <c:otherwise>
		            <pimsForm:text name="Input Sample" value="${input.inputSampleName} : ${input.amount} ${input.sampleName}" alias="${alias}" />
		        </c:otherwise>
		    </c:choose>
			
		</c:forEach>
		
		<c:forEach var="parameter" items="${experiment.modelObject.parameters}" varStatus="status">
			<c:choose>
			<c:when test="${status.first}">
			<c:set var="alias" value="Parameters" />
			</c:when>
			<c:otherwise>
			<c:set var="alias" value="" />
			</c:otherwise>
			</c:choose>
			
			<c:choose>
		    	<c:when test="${fn:startsWith(parameter.parameterDefinition.name, '__')}" >
		        	<pimsForm:text name="Parameter" value="${parameter.parameterDefinition.label} : ${parameter.value}" alias="${alias}" />
		    	</c:when>
                <c:when test="${fn:contains(parameter.name, 'Forward Tag')}" >
                    <pimsForm:text name="Parameter" value="${'N-terminal Tag'} : ${parameter.value}" alias="${alias}" />
                </c:when>
                <c:when test="${fn:contains(parameter.name, 'Reverse Tag')}" >
                    <pimsForm:text name="Parameter" value="${'C-terminal Tag'} : ${parameter.value}" alias="${alias}" />
                </c:when>
		   	<c:otherwise>
					<pimsForm:text name="Parameter" value="${parameter.name} : ${parameter.value}" alias="${alias}" />
				</c:otherwise>
			</c:choose>
			
		</c:forEach>
		
		<c:set var="imageFiles" value="${images[experiment.hook]}" />
	
		<c:forEach items="${imageFiles}" var="file" varStatus="status">
		
			<c:choose>
				<c:when test="${status.first}">
				<c:set var="alias" value="Images" />
				</c:when>
				<c:otherwise>
				<c:set var="alias" value="" />
				</c:otherwise>
			</c:choose>
			
			<div class="formitem ">
			<div class="fieldname" >
				<label for="Image" ><c:out value="${alias}" /></label>
			</div>
			<div class="formfield" >
				<table>
				<tr>
				<td style="padding-left: 0px;">
					<input readonly="readonly" type="text" name="Image" id="Image"  value="${file.title}" />
					<input readonly="readonly" type="text" name="Image" id="Image"  value="${file.legend}" />
				</td>
				<td style="border-left-width: 0px;">
					<a href="${pageContext.request.contextPath}/read/ViewFile/${file.hook}/${file.name}"
						onclick='return warnChange()'
						title="view file" >
					<img class="thumbnail" src="${pageContext.request.contextPath}/read/ViewFile/${file.hook}" />
					</a>
				</td>
				</tr>
				</table>
				
			</div>
			</div>
			
		</c:forEach>
		
	</pimsForm:formBlock>
	</pimsForm:form>
	
  </pimsWidget:box>

</c:forEach>

<c:set var="name"><c:out value="${sample.name}" /></c:set>
<pimsWidget:box title="Sample: ${name}"  initialState="closed" >

	<pimsForm:form  action="" mode="view" method="get">
	<pimsForm:formBlock>
		<pimsForm:text name="Role" value="${role}" alias="Role" />
		
		<c:forEach var="category" items="${sampleCategories}" varStatus="status">
            
            <c:choose>
            <c:when test="${status.first}">
            <c:set var="alias" value="Categories" />
            </c:when>
            <c:otherwise>
            <c:set var="alias" value="" />
            </c:otherwise>
            </c:choose>
            
            <pimsForm:text name="Category" value="${category.name}" alias="${alias}" />
            
        </c:forEach>
		
	</pimsForm:formBlock>
	</pimsForm:form>

</pimsWidget:box>

<a href="${pageContext.request.contextPath}/read/Provenance/<c:out value="${sample.hook}" />?presentationtype=pdf" >
View as PDF
</a>


<br />
<br /> 


<img src="${pageContext.request.contextPath}/read/Dot/${sample.hook}?format=png" usemap="#Graph" />
<jsp:include page="/read/Dot/" >
    <jsp:param name="hook" value="${sample.hook}" />
    <jsp:param name="format" value="cmapx" />
</jsp:include>
<br />
Cite this page as:<br />
<c:forEach var="author" items="${authors}" varStatus="status">
<c:out value="${author.person.name}"></c:out>
<c:if test="${!status.last}">, </c:if> 
<c:if test="${status.last}">;</c:if>    
</c:forEach>

Provenance of <c:out value="${sample.name}" />; viewed 
<script type="text/javascript">
<!--
var currentTime = new Date()
var month = currentTime.getMonth() + 1
var day = currentTime.getDate()
var year = currentTime.getFullYear()
document.write(day + "/" + month + "/" + year+ ";")
//-->
</script>
<c:out value="${requesturl}"></c:out>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
</body>
</html>
