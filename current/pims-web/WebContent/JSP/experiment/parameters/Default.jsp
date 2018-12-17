<%@page import="org.pimslims.model.target.Target"%>
<%@page import="org.pimslims.model.experiment.Experiment"%>
<%@page import="org.pimslims.model.target.ResearchObjective"%>
<%@page import="org.pimslims.presentation.mru.MRUController"%>
<%@page import="org.pimslims.presentation.mru.MRU"%>
<%@page import="org.pimslims.servlet.PIMSServlet"%>
<%@page import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<!-- exp_param.jsp -->
<jsp:useBean id="parameters" scope="request" type="java.util.List<org.pimslims.model.experiment.Parameter>" />

<c:set var="longtype" value="Long" scope="page" />
<c:set var="doubletype" value="Double" scope="page" />
<c:set var="floattype" value="Float" scope="page" />
<c:set var="stringtype" value="String" scope="page" />
<c:set var="booleantype" value="Boolean" scope="page" />
<c:set var="datetimetype" value="DateTime" scope="page" />
<c:set var="intervaltype" value="Interval" scope="page" />


<h4 class="printonly">Conditions and Results </h4>
<c:choose><c:when test="${empty parameters}">
    <p class="empty_parameters">No parameters have been defined</p>
</c:when></c:choose>


<table>
<tr>
<th style="width:45%">Parameter</th>
<th>Value</th>
</tr>
<c:forEach items="${parameters}" var="parameter">

	<c:set var="labelName" value="${parameter.parameterDefinition.name}" />
	<c:if test="${null==parameter.parameterDefinition}">
        <c:set var="labelName" value="${parameter.name}" />
	</c:if>
	<c:if test="${fn:startsWith(parameter.parameterDefinition.name, '__')}" >
		<c:set var="labelName" value="${parameter.parameterDefinition.label}" />
	</c:if>

	<c:set var="type" value="blank"/>
		<c:choose>
		<c:when test="${'Int'==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="number"/>
		</c:when><c:when test="${'Integer'==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="number"/>
		</c:when><c:when test="${floattype==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="number"/>
		</c:when><c:when test="${longtype==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="number"/>
		</c:when><c:when test="${doubletype==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="number"/>
		</c:when><c:when test="${booleantype==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="boolean"/>
		</c:when><c:when test="${stringtype==parameter.parameterDefinition.paramType}">
			<c:set var="type" value="string"/>
		</c:when>
	</c:choose>
	
	<tr>
		<th style="text-align:left;">
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/${type}icon.gif" alt="${type}"/>
		${labelName}
		<c:if test="${''!=parameter.parameterDefinition.displayUnit}"> (${parameter.parameterDefinition.displayUnit})</c:if>
		<c:if test="${parameter.parameterDefinition.isMandatory}"><span class="required">*</span></c:if>
		</th>
		<td><%@include file="/JSP/experiment/parameter.jsp"%></td>
	</tr>
</c:forEach>
<tr><td style="padding-right:1em" colspan="2"><pimsForm:editSubmit isNext="${'To_be_run' eq modelObject.values['status'] }" /></td></tr>
</table>


<!-- /exp_param.jsp -->
