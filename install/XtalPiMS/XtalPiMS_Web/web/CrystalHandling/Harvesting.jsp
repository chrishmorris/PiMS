<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Selecting a crystal from a well and mounting it on a pin,
with optional in-well cryoprotection.

Author: Ed Daniel
Date: April 2012
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>
<%-- 
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Harvesting" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Crystal harvesting: ${barcode}:${well}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<c:set var="harvestReadonly" scope="request" value="false"/>
<jsp:include page="/CrystalHandling/HarvestingBox.jsp" />

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<jsp:include page="/JSP/core/Footer.jsp" />