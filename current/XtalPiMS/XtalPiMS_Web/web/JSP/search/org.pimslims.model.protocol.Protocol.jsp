<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display a list of model objects.
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
<%@ page import="org.pimslims.model.protocol.Protocol" %>

<!-- Search_protocol.jsp  -->
<c:catch var="error">

<jsp:useBean id="protocolMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="protocols" scope="request" type="java.util.Collection<org.pimslims.presentation.protocol.ProtocolBean>" />
<jsp:useBean id="experimentTypes" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:set var="actions">
	<pimsWidget:linkWithIcon name=""
        		icon="actions/create/protocol.gif" 
        		url="${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.Protocol" 
        		text="New protocol"/>
</c:set>
<c:set var="breadcrumbs">
	<a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/Search/">Search</a>
</c:set>
<pimsWidget:pageTitle icon="protocol.png"
	title="Search protocols"
	actions="${actions}"
	breadcrumbs="${breadcrumbs}" />

<div style="margin-right:10em" class="slimline_forms">


<pimsWidget:pageControls>
	<input type="hidden" name="status" value="${fn:escapeXml(param.status)}"/>
	<c:if test="${!empty param.experimentType}"><input type="hidden" name="experimentType" value="${param.experimentType}"/></c:if>
	<c:if test="${!empty param.name}"><input type="hidden" name="name" value="${fn:escapeXml(param.name)}"/></c:if>
	<c:if test="${!empty param.details}"><input type="hidden" name="details" value="${fn:escapeXml(param.details)}"/></c:if>
	<c:if test="${!empty param.objective}"><input type="hidden" name="objective" value="${param.objective}"/></c:if>
</pimsWidget:pageControls>

<pimsWidget:quickSearch initialState="open" value="${fn:escapeXml(param['search_all'])}" />

<pimsWidget:box title="Advanced Search" initialState="closed">
	<pimsForm:form mode="edit" method="get" action="/Search/org.pimslims.model.protocol.Protocol">
		<input type="hidden" name="pagesize" value="${param.pagesize}"/>
		<pimsForm:formBlock>
			<pimsForm:column1>
				<pimsForm:text name="name" alias="Name" value="${fn:escapeXml(param['name'])}" />

				<pimsForm:text name="details" alias="Details" value="${fn:escapeXml(param['details'])}" />
				
				<pimsForm:text name="objective" alias="Objective" value="${fn:escapeXml(param['objective'])}" />

			</pimsForm:column1>
			<pimsForm:column2>
				<pimsForm:select alias="Experiment Type" name="<%=Protocol.PROP_EXPERIMENTTYPE %>" >
				    <option value="">[any]</option>
			    	<c:forEach items="${experimentTypes}" var="type">
			    		<pimsForm:option currentValue="${param['experimentType']}" optionValue="${type.hook}" alias="${type.name}" />
			    	</c:forEach>
				</pimsForm:select>
				
                <pimsForm:select alias="Instrument Type" name="<%=Protocol.PROP_INSTRUMENTTYPE %>" >
                    <option value="">[any]</option>
                    <c:forEach items="${instrumentTypes}" var="type">
                        <pimsForm:option currentValue="${param['instrumentType']}" optionValue="${type.hook}" alias="${type.name}" />
                    </c:forEach>
                </pimsForm:select>
				
				<pimsForm:formItem name="" alias="">
					<input style="float:right" type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" />
				</pimsForm:formItem>

			</pimsForm:column2>
		</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>

</div>

<strong>${totalRecords}</strong> protocols recorded in the database<br/>
<strong>${resultSize}</strong> match search criteria


<hr />

<form id="selectedProtocols" action="${pageContext.request.contextPath}/report/ReportExperimentParameters" method="get"  style="background-color: transparent; width: auto;">

<h2>
  <c:if test="${null!=experimentType}">
    <a href="../View/${experimentType._Hook}"><c:out value="${experimentType.name}" /></a>
  </c:if>
  ${resultSize} Protocols
</h2>


<c:if test="${!empty experimentType._Hook}" >
	<a href="${pageContext.request.contextPath}/Create/org.pimslims.model.protocol.Protocol?experimentTypeHook=${experimentType._Hook}">
	Record a new <c:out value="${experimentType.name}" /> Protocol</a><br /><br />
</c:if>

 
<c:choose><c:when test="${empty protocols}" >
    <h2>No Protocols found</h2>
</c:when><c:otherwise>
    <script type="text/javascript">	 focusElement =null </script>
    <%
        request.setAttribute("metaClass", protocolMetaClass);
        request.setAttribute("beans", protocols);
        request.setAttribute("chooseExp", true);
    %>
    <c:set var="protocolClassName"><%=Protocol.class.getName()%></c:set>
    <pimsWidget:box title="Protocols found" initialState="open">
	    <jsp:include page="/JSP/list/org.pimslims.model.protocol.Protocol.jsp" />
	</pimsWidget:box>
</c:otherwise>
</c:choose>

<br/>
</form>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>


<jsp:include page="/JSP/core/Footer.jsp" />
