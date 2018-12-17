<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
//----------------------------------------------------------------------------------------------
//
//
//		 	Created by Johan van Niekerk,SSPF-Dundee				14 February 2006
//			Modified by														Date

Modified by PIMS developers 
Last modification Peter Troshin Feb 2008

//----------------------------------------------------------------------------------------------
--%>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="PIMS Reports" />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="Reports"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />

<pimsWidget:box initialState="fixed" title="Reports available">
	<table>
		<tr><th>Report name</th><th>Description</th></tr>
		<tr><td><a href="${pageContext.request.contextPath}/read/Blast/BlastWeekly">Scheduled Blast Report</a></td><td>PDB and TargetDB Top hits for all Targets</td></tr>
		<tr><td><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a></td><td>List of target groups, with links to reports</td></tr>
		<!-- 
		<tr><td><a href="${pageContext.request.contextPath}/read/TargetProgress">Active Targets</a></td><td>
		List of targets for which new experiments have been recently recorded &nbsp;<a href="${pageContext.request.contextPath}/help/HelpActiveTargetsReport.jsp">(read more...)</a></td></tr>
		-->
	</table>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
