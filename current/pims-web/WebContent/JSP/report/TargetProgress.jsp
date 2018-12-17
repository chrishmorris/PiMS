<%--

Author: Peter Troshin
Date: February 2008
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@page import="org.pimslims.model.experiment.Experiment"%>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Target Report' />
</jsp:include>
obsolete

<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}">Home</a> : 
	<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> : 
	<a href="${pageContext.request.contextPath}/spot/SpotReports">Reports</a>
</c:set>
<pimsWidget:pageTitle icon="target.png" title="Active Targets" breadcrumbs="${breadcrumbs}" />

<pimsWidget:box title="Search criteria" initialState="open">
<pimsForm:form method="get" mode="edit" id="searchPrm" action="/read/TargetProgress" >
<%-- NO, breaches confidentiality
<pimsForm:formBlock>
	<pimsForm:column1>
		<pimsForm:formItem alias="Active in the last" name="daysback">
			<pimsForm:formItemLabel  alias="Active in the last" name="daysback"/>
			<div class="formfield">
			<input name="daysback" id="date" maxlength="4" style="width: 5em;" value="${daysback}" type="text" /> days
			</div>
		</pimsForm:formItem>
		<pimsForm:select name="dataOwner" alias="Worked at">
			<option value="anywhere">Anywhere</option>
			<c:forEach items="${dataOwners}" var="dataOwner">
		    	 <pimsForm:option currentValue="${rdataOwner}" optionValue="${dataOwner._Hook}" alias="${dataOwner.name}" />
		   </c:forEach>
		</pimsForm:select>		
		<pimsForm:select name="creator" alias="Experimenter">
			<option value="anywhere">Anyone</option>
			<c:forEach items="${users}" var="user">
					<c:set var="alias"><c:out value="${user.givenName}" />&nbsp;<c:out value="${user.familyName}" /></c:set>
		    	 <pimsForm:option currentValue="${chosenPersonHook}" optionValue="${user._Hook}" alias="${alias}" />
		   </c:forEach>
		</pimsForm:select>		
		<pimsForm:checkbox label="Display all experiments" name="allExperiments" isChecked="${checked}" />
	
		<pimsForm:submitButton buttonText="Update" />

	</pimsForm:column1>
</pimsForm:formBlock>
--%>
</pimsForm:form>
</pimsWidget:box>


<c:choose>
<c:when test="${! empty activeTargets}">
   <c:import url="/JSP/report/TargetProgressList.jsp" />
</c:when>
<c:otherwise>
	<h3>No active target found. Try to use wider criteria.</h3>
</c:otherwise>
</c:choose>


<jsp:include page="/JSP/core/Footer.jsp" />

