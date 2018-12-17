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

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="plate.png" />        
<c:set var="title" value="${shipment.name}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Samples" initialState="open">
<c:forEach var="sample" items="${samples}">
	<table style="font-size:80%; page-break-inside:avoid;border-top:2px solid #999">
		<tr><%-- Pin barcode, experiment type (OSC/SAD), on-screen link to treatment page --%>
			<th colspan="5">
			<c:if test="${!empty sample.treatmentURL}"><a class="noprint" style="float:right;margin-right:1em" href="${sample.treatmentURL}">Crystal treatment</a></c:if>
			Pin: ${sample.pinBarcode} <span style="display:inline-block;color:#600;margin:0 1em;padding:0 0.5em;border:2px solid #600">${sample.dataCollectionType}</span> Sample: ${sample.sampleName}
			</th>
		</tr>
		<tr><%-- Unit cell, etc., row 1 --%>
			<td>Heavy atom: <strong>${sample.heavyAtom}</strong></td>	
			<td style="width:20%" rowspan="2">Predicted unit cell dimensions</td>	
			<td style="width:20%;border-width:1px 0 0 0">a: <strong>${sample.a}</strong></td>
			<td style="width:20%;border-width:1px 0 0 0">b: <strong>${sample.b}</strong></td>
			<td style="width:20%;border-width:1px 0 0 0">c: <strong>${sample.c}</strong></td>
		</tr>
		<tr><%-- Unit cell, etc., row 2 --%>
			<td>Space group: <strong>${sample.spaceGroup}</strong></td>
			<td style="border:none">&#945;: <strong>${sample.alpha}</strong></td>
			<td style="border:none">&#946;: <strong>${sample.beta}</strong></td>
			<td style="border:none">&#947;: <strong>${sample.gamma}</strong></td>
		</tr>
		<tr><%-- Protein --%>
			<td>Protein: <pimsWidget:link bean="${sample.project}" /></td>
			<td colspan="4" style="font-family:monospace;font-size:140%">${sample.proteinSequence}</td>
		</tr>
		<tr><%-- Shipping comment --%>
			<td>Shipping comment</td>
			<td colspan="4">${sample.shippingComment}</td>
		</tr>
		<tr class="printonly"><%-- Print-only row - QR code, space for hand-written notes--%>
			<td><c:if test="${!empty sample.treatmentURL}"><pimsWidget:qrCode content="${sample.treatmentURL}"/></c:if></td>
			<td colspan="4" valign="top" style="color:#999;"><span style="color:#999;">(Notes)</span></td>
		</tr>
	</table>
</c:forEach>
</table></pimsWidget:box>
<div class="noprint">
<pimsWidget:files bean="${shipment}"/>
</div>
<pimsWidget:notes bean="${shipment}"/>
</c:catch>
<jsp:include page="/JSP/core/Footer.jsp" />