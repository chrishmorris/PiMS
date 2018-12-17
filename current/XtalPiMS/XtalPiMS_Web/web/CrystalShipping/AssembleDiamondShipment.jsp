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

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Assemble Diamond Shipment" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<style type="text/css">
div.collapsiblebox table.shipment_puck { margin:20px 2% 10px 2%; border:2px solid #999; width:97.5%; font-size:80%; }
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
<c:set var="title" value="Assemble Diamond Shipment"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Basic shipment details" initialState="fixed">
<pimsForm:form action="#" method="post" mode="edit">
	<pimsForm:formBlock>	
		<pimsForm:text name="destination" alias="Ship to" value="${shipmentDestination}" />
		<%-- 
		<pimsForm:text name="shipper" alias="Shipper" value="${shipper}" validation="required:true" />
		<pimsForm:text name="trackingid" alias="Tracking ID"  value="${trackingId}" validation="required:true" />
		 --%>
	</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>

<div id="dewars">
<%-- for each dewar in shipment - almost certainly none, we'll build the shipment client side then submit
<jsp:include page="Diamond_Dewar.jsp"/>
 --%>
 </div>

<pimsWidget:box title="Actions" initialState="fixed" id="actions">
<pimsForm:form action="#" method="post" mode="edit" id="adddewarform">
<pimsForm:formBlock>
	<pimsForm:column1>
	<pimsForm:text name="adddewar" alias="Add dewar by barcode" value="" />
	</pimsForm:column1>
	<pimsForm:column2>
	<input style="float:right" type="button" name="submit" id="submit" value="Submit shipment" />
	</pimsForm:column2>
</pimsForm:formBlock>
</pimsForm:form>
</pimsWidget:box>



</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<script type="text/javascript" src="${pageContext.request['contextPath']}/CrystalShipping/shipment.js"></script>
<script type="text/javascript">
var spaceGroups=new Array();
<c:forEach var="sg" items="${spaceGroups}">
spaceGroups.push("${sg}");
</c:forEach>
var heavyAtoms=new Array();
<c:forEach var="ha" items="${heavyAtoms}">
heavyAtoms.push("${ha}");
</c:forEach>
$("adddewar").addClassName("barcode");
$("adddewarform").onsubmit=function(){ return false; };
Shipment.init();
setTimeout(function(){ $("adddewar").focus() },500);
</script>
<jsp:include page="/JSP/core/Footer.jsp" />