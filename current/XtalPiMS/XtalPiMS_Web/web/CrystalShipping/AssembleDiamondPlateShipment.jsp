<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Assembling a shipment to Diamond

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

<c:choose><c:when test="${isBeamline}">
	<c:set var="pageTitle" value="Queue plate to beamline" />
</c:when><c:otherwise>
	<c:set var="pageTitle" value="Assemble Diamond Shipment" />
</c:otherwise></c:choose>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${pageTitle}" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<style type="text/css">
div.collapsiblebox table.shipment_puck { margin:20px 2% 10px 2%; border:2px solid #999; width:97.5%; font-size:80%; }
div.collapsiblebox table.shipment_plate { margin:0; border:none; width:100%; font-size:80%; }
div.collapsiblebox table.shipment_puck td { padding:0 }
table.shipment_puck th.puckhead { background-color:#ccc;}
table.shipment_puck input[disabled=disabled] { background-color:#AAD28C; opacity:0.7; filter:alpha(opacity=70); width:100%; border:1px solid transparent; box-sizing:box; -moz-box-sizing:box; }
table.shipment_puck tr.odd td,
table.shipment_puck tr.odd { background-color:#ddd; }
table.shipment_puck tr.even, 
table.shipment_puck tr.even td { }
table.shipment_puck input { background-color:transparent; width:100%; border:1px solid transparent; box-sizing:box; -moz-box-sizing:box; }
table.shipment_puck select { background-color:transparent; border:1px solid transparent;  }
table.shipment_puck .slot { width:1em; padding:0 .25em; text-align:center; }
table.shipment_puck .unitcell { width:3em; padding:0; text-align:center }
table.shipment_puck .label { width:7em; }
table.shipment_puck .comments { /* Just take up the remaining width */ }
table.shipment_puck th.slot img { height:90%; width:90%; border:none; }
table.shipment_puck tr.odd td.invalid,
table.shipment_puck tr.even td.invalid { border:1px solid red; background-color:#fcc; color:#600; }
table.shipment_puck tr.odd td.updating,
table.shipment_puck tr.even td.updating { border:1px solid orange; background-color:#ffc; color:#999; }
table.closedpuck tr.body { height:0px; }
table.closedpuck tr.body * { display:none; }
.acronymwarning { color:#600; font-weight:bold; cursor:help; }
</style>

<c:set var="breadcrumbs">
  <a href="${pageContext.request.contextPath}/">Home</a> :
  <a href="${pageContext.request.contextPath}/RecentShipments">Shipments</a> :
</c:set>

<c:set var="icon" value="plate.png" />        
<c:set var="title" value="${pageTitle}"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:choose><c:when test="${isBeamline}">

	<c:if test="${!empty plateError}">
		<pimsWidget:box title="Error" initialState="fixed">
		<p class="error">${plateError}</p>
		</pimsWidget:box>
	</c:if>

	<pimsWidget:box title="Review" initialState="fixed" id="instructions">
	<p style="margin-left:1em;">Review the plate task list here, then click "Queue plate to beamline" below.</p>
	<p style="color:#900;font-weight:bold;margin-left:1em;">Your plate will not be queued until you click "Queue plate to beamline".</p>
	<input type="hidden" name="destination" id="destination" value="Beamline" />
	</pimsWidget:box>
	
</c:when><c:otherwise>

	<pimsWidget:box title="Basic shipment details" initialState="fixed">
	<pimsForm:form action="#" method="post" mode="edit">
		<pimsForm:formBlock>	
			<pimsForm:text name="destination" alias="Ship to" value="${shipmentDestination}" />
		</pimsForm:formBlock>
	</pimsForm:form>
</pimsWidget:box>

</c:otherwise></c:choose>

<div id="plates">
</div>

<pimsWidget:box title="Actions" initialState="fixed" id="actions">
<pimsForm:form action="#" method="post" mode="edit" id="addplateform">
<pimsForm:formBlock>
	<pimsForm:column1>

		<c:choose><c:when test="${isBeamline}">
			<input type="hidden" name="addplate" id="addplate" value="${plateBarcode}"/>	
			<input type="button" name="submit" id="submit" style="background:black;color:white;cursor:pointer;border:none;padding:3px 15px;margin:1em 0" value="Queue plate to beamline" />
		</c:when><c:otherwise>
			<pimsForm:text name="addplate" alias="Add plate by barcode" value="" />
		</c:otherwise></c:choose>

	</pimsForm:column1>
	<pimsForm:column2>
		<c:choose><c:when test="${isBeamline}">
		</c:when><c:otherwise>
			<input style="float:right" type="button" name="submit" id="submit" value="Submit shipment" />
		</c:otherwise></c:choose>
	</pimsForm:column2>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>



</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<script type="text/javascript" src="${pageContext.request['contextPath']}/CrystalShipping/shipment.js"></script>
<script type="text/javascript">

<c:choose><c:when test="${isBeamline}">
var isBeamline=true;
</c:when><c:otherwise>
var isBeamline=false;
</c:otherwise></c:choose>

<c:choose><c:when test="${empty plateShipmentEndpoint}">
var plateShipmentEndpoint=null;
</c:when><c:otherwise>
var plateShipmentEndpoint="${plateShipmentEndpoint}";
</c:otherwise></c:choose>

var spaceGroups=new Array();
<c:forEach var="sg" items="${spaceGroups}">
spaceGroups.push("${sg}");
</c:forEach>
var heavyAtoms=new Array();
<c:forEach var="ha" items="${heavyAtoms}">
heavyAtoms.push("${ha}");
</c:forEach>
$("addplate").addClassName("platebarcode");
$("addplateform").onsubmit=function(){ return false; };
Shipment.init();

<c:choose><c:when test="${isBeamline}">
	Event.observe(window,"load",function(){
		Shipment.doAddPlateByBarcode($("addplate"));
	});
</c:when><c:otherwise>
	setTimeout(function(){ $("addplate").focus() },500);
</c:otherwise></c:choose>

</script>
<jsp:include page="/JSP/core/Footer.jsp" />