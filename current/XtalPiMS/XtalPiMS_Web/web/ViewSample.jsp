<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 

<%@page session = "true" %>
<%@page contentType = "text/html"%>
<%@page pageEncoding = "UTF-8"%>
<%@page isThreadSafe="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="pims" uri="http://www.pims-lims.org" %>
<%@taglib prefix="xtalpims" uri="http://www.pims-lims.org/xtalpims" %>

<c:set var="HeaderName" scope="page" value="View Sample" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>
<%
if (null == request.getParameter("sample")) {
	throw new javax.servlet.ServletException("Unable to show page as you have not specified a sample correctly!");
} else {
%>

<table class="list">
	<tr>
		<th>Page Notes</th>
	</tr>
	<tr>
		<td>This is a temporary page which will be better populated once data is available in PIMS</td>
	</tr>
</table>

<h3>Sample Data</h3>

<table class="list">
	<tr>
		<th>Name:</th>
		<td id="sample_name"></td>
	</tr>
	<tr>
		<th>Description:</th>
		<td id="sample_description"></td>
	</tr>
	<tr>
		<th>pH:</th>
		<td id="sample_pH"></td>
	</tr>
	<tr>
		<th>Molecular Weight:</th>
		<td id="sample_molecularWeight"></td>
	</tr>
	<tr>
		<th>Number of Sub Units:</th>
		<td id="sample_numSubUnits"></td>
	</tr>
	<tr>
		<th>Batch Reference:</th>
		<td id="sample_batchReference"></td>
	</tr>
	<tr>
		<th>Origin:</th>
		<td id="sample_origin"></td>
	</tr>
	<tr>
		<th>Type:</th>
		<td id="sample_type"></td>
	</tr>
	<tr>
		<th>Cellular Location:</th>
		<td id="sample_cellularLocation"></td>
	</tr>
	<tr>
		<th>Concentration:</th>
		<td id="sample_concentration"></td>
	</tr>
	<tr>
		<th>GI Number:</th>
		<td id="sample_giNumber"></td>
	</tr>
	<tr>
		<th>Safety Information:</th>
		<td id="sample_safteyInformation"></td>
	</tr>
	<tr>
		<th>Construct:</th>
		<td id="sample_construct"></td>
	</tr>
	<tr>
		<th>Further Details:</th>
		<td id="sample_history"></td>
	</tr>
</table>

<h3>Annotations</h3>
<div style="padding-bottom: 1em" id="show_hide_annotation_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom:1em;display:none" id="annotation_filter">
<%@include file="./WEB-INF/jspf/filterAnnotations.jspf" %>
</div>
<div id="annotationstable" class="yui-skin-sam"></div>

<h3>Plates</h3>
<div style="padding-bottom: 1em;" id="show_hide_plate_filter"><img src='xtal/images/16x16/plus.png' alt='' />&nbsp;Show Filters</div>
<div style="padding-bottom: 1em;display:none;" id="plate_filter">
<%@include file="./WEB-INF/jspf/filterPlates.jspf"%>
</div>
<div id="platestable" class="yui-skin-sam"></div>

<div class="yui-skin-sam">
<%@include file="./WEB-INF/jspf/filtercalsAnnotations.jspf" %>
<%@include file="./WEB-INF/jspf/filtercalsPlates.jspf"%>
</div>

<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/history/history-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/json/json-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/calendar/calendar-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DataFilter.js.ycomp.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewAnnotations.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewPlates.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewSample.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkAnnotations = '<%=(null != request.getSession().getAttribute("sb_sa_a")) ? request.getSession().getAttribute("sb_sa_a") : "" %>';
PIMS.xtal.sessionBookmarkPlates = '<%=(null != request.getSession().getAttribute("sb_sa_p")) ? request.getSession().getAttribute("sb_sa_p") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.sampleName = '<%=(null != request.getParameter("sample")) ? request.getParameter("sample") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewSample);
</script>

<%
	}
%>
<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>