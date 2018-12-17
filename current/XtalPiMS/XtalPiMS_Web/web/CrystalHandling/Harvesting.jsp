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
    <jsp:param name="HeaderName" value="Crystal selection: ${barcode}:${well}" />
    <jsp:param name="extraStylesheets" value="../../../css/viewtrialdrops3" />
</jsp:include>
<script type="text/javascript" src="${pageContext.request['contextPath']}/CrystalHandling/treatment.js"></script>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Crystal selection: ${barcode}:${well}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<c:set var="harvestReadonly" scope="request" value="false"/>
<jsp:include page="/CrystalHandling/HarvestingBox.jsp" />

<script type="text/javascript">
var plateBarcode="${barcode}";
var well="${well}";
Treatment.initNav();
</script>

<%--
<c:if test="${isBeamline}">
	<pimsWidget:box title="Plate task list" initialState="fixed" extraClasses="noscroll" extraHeader="">
	<table>
	<c:forEach var="xtal" items="${coords}">
		<tr>
		<th>${xtal.crystalNumber}</th>
		<td>${xtal.x}</td>
		<td>${xtal.y}</td>
		<td>${xtal.r}</td>
		</tr>	
	</c:forEach>
	</table>

	</pimsWidget:box>
</c:if>
 --%>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<jsp:include page="/JSP/core/Footer.jsp" />