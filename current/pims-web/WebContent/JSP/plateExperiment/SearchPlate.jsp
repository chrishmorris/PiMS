<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display a list of experiment groups
This JSP is called by org.pimslims.servlet.Search

Author: Chris Morris
Date: 14 November 2005
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>

<%-- SearchPlate.jsp  --%>
<c:catch var="error">

<c:set var="title" value="plate experiment" />
<c:if test="${param['_only_experiment_groups']}" >
  <c:set var="title" value="experiment group" />
</c:if> 


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Search ${title}s" />
</jsp:include>

<jsp:useBean id="groupMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="plates" scope="request" type="java.util.Collection<org.pimslims.presentation.plateExperiment.PlateBean>" />
<jsp:useBean id="experimentTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />
<jsp:useBean id="holderTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:set var="actions">
    <pimsWidget:linkWithIcon name=""
                icon="actions/create/plate.gif" 
                url="${pageContext.request.contextPath}/CreatePlate"
                text="New plate experiment"/>
    <pimsWidget:linkWithIcon name=""
                icon="actions/create/experimentgroup.gif" 
                url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.ExperimentGroup"
                text="New experiment group"/>        		
	<pimsWidget:linkWithIcon  name=""
        		icon="types/small/experiment.gif" 
        		url="${pageContext.request.contextPath}/Search/org.pimslims.model.experiment.Experiment" 
        		text="Search single experiments"/>
</c:set>
<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/Search/">Search</a>
</c:set>

<c:set var="icon" value="plate.png" />
<c:if test="${param['_only_experiment_groups']}" >
  <c:set var="icon" value="experimentgroup.png" />
</c:if> 

<pimsWidget:pageTitle icon="${icon}"
	title="Search ${title}s"
	actions="${actions}"
	breadcrumbs="${breadcrumbs}" />

<div style="margin-right:10em" class="slimline_forms">

<pimsWidget:pageControls>
	<input type="hidden" name="status" value="${fn:escapeXml(param.status)}"/>
	<c:if test="${!empty param.experimentType}"><input type="hidden" name="experimentType" value="${param.experimentType}"/></c:if>
	<c:if test="${!empty param.holderType}"><input type="hidden" name="holderType" value="${param.holderType}"/></c:if>
	<c:if test="${!empty param.name}"><input type="hidden" name="name" value="${fn:escapeXml(param.name)}"/></c:if>
</pimsWidget:pageControls>
 
<pimsWidget:quickSearch initialState="open"  value="${fn:escapeXml(param['search_all'])}" >
        <c:if test="${!empty param['_only_experiment_groups']}"><input type="hidden" name="_only_experiment_groups" value="${param['_only_experiment_groups']}"/></c:if>
		<input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
</pimsWidget:quickSearch>

<pimsWidget:box title="Advanced Search" initialState="closed">
	<pimsForm:form mode="edit" method="get" action="/Search/org.pimslims.model.experiment.ExperimentGroup">
        <input type="hidden" name="pagesize" value="${param.pagesize}"/>
        <c:if test="${!empty param['_only_experiment_groups']}"><input type="hidden" name="_only_experiment_groups" value="${param['_only_experiment_groups']}"/></c:if>
		<pimsForm:formBlock>
			<pimsForm:column1>
				<pimsForm:text name="name" alias="Name" value="${fn:escapeXml(param['name'])}" />
			</pimsForm:column1>
			
			<pimsForm:column2>
				<pimsForm:select alias="Experiment Type" name="experimentType">
				    <option value="">[any]</option>
			    	<c:forEach items="${experimentTypes}" var="type">
			    		<pimsForm:option currentValue="${param['experimentType']}" optionValue="${type.name}" alias="${type.name}" />
			    	</c:forEach>
				</pimsForm:select>


<c:if test="${'true' ne param['_only_experiment_groups']}" >
			    <pimsForm:select alias="Holder Type" name="holderType">
				    <option value="">[any]</option>
			    	<c:forEach items="${holderTypes}" var="type">
			    		<pimsForm:option currentValue="${param['holderType']}" optionValue="${type.name}" alias="${type.name}" />
			    	</c:forEach>
				</pimsForm:select>
</c:if>
			    <pimsForm:formItem name="" alias="">
					<input style="float:right" type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" />
				</pimsForm:formItem>
			</pimsForm:column2>
		</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>

</div>

<strong>${totalRecords}</strong> ${title}s recorded in the database<br/>
<strong>${resultSize}</strong> match search criteria

<hr />

<form id="selectedExperiments" action="${pageContext.request.contextPath}/report/ReportExperimentParameters" method="get"  style="background-color: transparent; width: auto;">

<h2>
  <c:if test="${null!=experimentType}">
    <a href="../View/${experimentType._Hook}"><c:out value="${experimentType.name}" /></a>
  </c:if>
  ${resultSize} &nbsp; ${title}s
</h2>
 
<c:choose><c:when test="${empty plates}" >
    <h2>No ${title}s found</h2>
</c:when><c:otherwise>
    <script type="text/javascript">	 focusElement =null </script>
    <%
        request.setAttribute("listMetaClass", groupMetaClass);
        request.setAttribute("results", plates);
        request.setAttribute("chooseExp", true);
    %>
    
    <pimsWidget:box title="${title}s found" initialState="open">
	    <jsp:include page="/JSP/List_Plates.jsp" />
	</pimsWidget:box>
</c:otherwise>
</c:choose>

<br/>

</form>

<jsp:include page="/JSP/core/Footer.jsp" />


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>


