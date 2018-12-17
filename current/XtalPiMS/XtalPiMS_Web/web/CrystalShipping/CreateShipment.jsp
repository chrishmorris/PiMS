<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Specify the basic details of a crystal shipment - where to, tracking number, etc.

Work package: Crystal shipping
Copyright Edward Daniel, 2013
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%-- 
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Create Crystal Shipment" />
    <jsp:param name="extraStylesheets" value="/JSP/CrystalShipping/shipment.css" />
</jsp:include>

<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/">Home</a> :
  <a href="${pageContext.request.contextPath}/RecentShipments">Shipments</a> :
</c:set>
<c:set var="icon" value="plate.png" />        
<c:set var="title" value="Create Crystal Shipment"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Basic shipment details" initialState="fixed">
<pimsForm:form action="/AssembleShipment/" method="get" mode="edit">
	<pimsForm:formBlock>
	<pimsForm:select name="destination" alias="Ship to">
		<option value="Diamond">Diamond</option>
	</pimsForm:select>
	<%-- 
	<pimsForm:select name="shipper" alias="Shipper">
		<option value="">(none)</option>
		<option value="FedEx">FedEx</option>
		<option value="DHL">DHL</option>
	</pimsForm:select>
	
	<pimsForm:text name="trackingid" alias="Tracking ID"></pimsForm:text>
	--%>
	<div style="text-align:right">
		<input type="submit" value="Next &gt;" onclick="dontWarn()" />
	</div>
	</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

<pimsWidget:box title="Access rights" initialState="fixed">
	<pimsForm:formBlock>
	<p>To assemble a shipment, you need the following access rights:</p>
	<ul><li>Read and Update permissions on the pins, pucks and dewars used</li>
	<li>Read and Update permissions on all the crystals in the shipment</li>
	<li>Create, Read and Update permissions on the "Shipments" Lab notebook</li></ul>
	</pimsForm:formBlock>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    


<jsp:include page="/JSP/core/Footer.jsp" />