<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
View a shipment to Diamond

Work package: Crystal shipping
Copyright Edward Daniel, 2013
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Shipment: ${shipment.name}" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<style type="text/css">

</style>

<script type="text/javascript" src="${pageContext.request['contextPath']}/CrystalShipping/shipment.js"></script>

<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/">Home</a> :
  <a href="${pageContext.request.contextPath}/RecentShipments">Shipments</a> :
</c:set>

<c:set var="icon" value="plate.png" />        
<c:set var="title" value="${shipment.name}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Samples" initialState="open">
<c:forEach var="sample" items="${samples}">
	<table style="font-size:80%; page-break-inside:avoid;border-top:2px solid #999">
		<tr><%-- Pin barcode, experiment type (OSC/SAD), on-screen link to treatment page --%>
			<th colspan="5">
			<c:set var="dcType">${sample.dataCollectionType}</c:set>
			<c:if test="${empty dcType}"><c:set var="dcType">???</c:set></c:if>
			<c:if test="${!empty sample.treatmentURL}"><a class="noprint" style="float:right;margin-right:1em" href="<c:out value="${sample.treatmentURL}"/>">Crystal treatment</a></c:if>
			<c:choose><c:when test="${sample.pinBarcode eq 'PLATE'}">
				Plate ${sample.plateBarcode}, well ${sample.well}
			</c:when><c:otherwise>
				Pin: <c:out value="${sample.pinBarcode}"/> 
			</c:otherwise></c:choose>
			<span style="display:inline-block;color:#600;margin:0 1em;padding:0 0.5em;border:2px solid #600"><c:out value="${dcType}"/></span> Sample: <c:out value="${sample.sampleName}"/>
			</th>
		</tr>
		<tr><%-- Unit cell, etc., row 1 --%>
			<td>Heavy atom: <strong><c:out value="${sample.heavyAtom}"/></strong></td>	
			<td style="width:20%" rowspan="2">Predicted unit cell dimensions</td>	
			<td style="width:20%;border-width:1px 0 0 0">a: <strong><c:out value="${sample.a}"/></strong></td>
			<td style="width:20%;border-width:1px 0 0 0">b: <strong><c:out value="${sample.b}"/></strong></td>
			<td style="width:20%;border-width:1px 0 0 0">c: <strong><c:out value="${sample.c}"/></strong></td>
		</tr>
		<tr><%-- Unit cell, etc., row 2 --%>
			<td>Space group: <strong>${sample.spaceGroup}</strong></td>
			<td style="border:none">&#945;: <strong><c:out value="${sample.alpha}"/></strong></td>
			<td style="border:none">&#946;: <strong><c:out value="${sample.beta}"/></strong></td>
			<td style="border:none">&#947;: <strong><c:out value="${sample.gamma}"/></strong></td>
		</tr>
		<tr><%-- Protein <pimsWidget:link bean="${sample.project}" />  --%>
			<td>Protein: <strong><c:out value="${sample.proteinAcronym}"/></strong></td>
			<td colspan="4" style="font-family:monospace;font-size:140%"><c:out value="${sample.proteinSequence}"/></td>
		</tr>
		<tr><%-- Shipping comment --%>
			<td>Shipping comment</td>
			<td colspan="4"><c:out value="${sample.shippingComment}"/></td>
		</tr>
		<tr class="printonly"><%-- Print-only row - QR code, space for hand-written notes--%>
			<td><c:if test="${!empty sample.treatmentURL}"><pimsWidget:qrCode content="${sample.treatmentURL}"/></c:if></td>
			<td colspan="4" valign="top" style="color:#999;"><span style="color:#999;">(Notes)</span></td>
		</tr>
	</table>
</c:forEach>
</table></pimsWidget:box>

<div class="noprint">
	<pimsWidget:box title="Post-visit actions" initialState="closed">
	<pimsForm:form action="#" mode="edit" method="post"><pimsForm:formBlock>
	<strong>Empty all pucks, pins and dewars: </strong> - Set all containers used in the shipment to "empty" ready for re-use. <input type="submit" name="emptyall" id="emptyall" value="Empty all containers" onclick="Shipment.emptyContainers('${experimentGroupHook}');return false"/>
	</pimsForm:formBlock></pimsForm:form>
	</pimsWidget:box>
	
	<pimsWidget:files bean="${shipment}"/>
</div>
<pimsWidget:notes bean="${shipment}"/>
</c:catch>
<jsp:include page="/JSP/core/Footer.jsp" />