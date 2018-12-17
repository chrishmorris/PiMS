<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
List of recent shipments

Work package: Crystal shipping
Copyright Edward Daniel, 2013
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Recent shipments" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<style type="text/css">

</style>

<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/">Home</a> :
  <a href="${pageContext.request.contextPath}/RecentShipments">Shipments</a> :
</c:set>
<c:set var="icon" value="plate.png" />        
<c:set var="title" value="Recent shipments"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Shipments" initialState="fixed">
	<c:choose>
	<c:when test="${empty shipments}">No shipments found</c:when>
	<c:otherwise>	
		<c:forEach var="shipment" items="${shipments}">
		<a href="ViewShipment?shipment=${shipment.hook}" style="display:block">${shipment.name }</a>
		</c:forEach>
	</c:otherwise>
	</c:choose>
</pimsWidget:box>
</c:catch>
<jsp:include page="/JSP/core/Footer.jsp" />