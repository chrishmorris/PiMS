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

<c:set var="HeaderName" scope="page" value="View Plate Experiment" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>
<%
	if (request.getParameter("barcode") == null) {
		throw new javax.servlet.ServletException("Unable to show page as you have not specified a Plate barcode correctly!");
	} else {
%>

<div id="plateData" style="clear:both;padding:1em 0em 0em 1em">
	<h3>Plate Data</h3>
	<table class="list">
		<tr>
			<th>Barcode:</th>
			<td id="plate_barcode"></td>
		</tr>
		<tr>
			<th>Plate Type:</th>
			<td id="plate_type"></td>
		</tr>
		<tr>
			<th>Created by:</th>
			<td id="plate_runby"></td>
		</tr>
		<tr>
			<th>Created On:</th>
			<td id="plate_createDate"></td>
		</tr>
		<tr>
			<th>Destroyed On:</th>
			<td id="plate_destroyDate"></td>
		</tr>
		<tr>
			<th>Screen:</th>
			<td id="plate_screen"></td>
		</tr>
		<tr>
			<th>Last Known Imager:</th>
			<td id="plate_imager"></td>
		</tr>
		<tr>
			<th>Owner:</th>
			<td id="experiment_owner"></td>
		</tr>
		<tr>
			<th>Sample:</th>
			<td id="experiment_sample"></td>
		</tr>
		<tr>
			<th>Construct:</th>
			<td id="experiment_construct"></td>
		</tr>
		<tr>
			<th>Description:</th>
			<td id="experiment_description"></td>
		</tr>
	</table>
</div>
<a href="${pageContext.request['contextPath']}/EditPlate/org.pimslims.model.holder.Holder:<%=request.getParameter("barcode")%>">Edit</a>

<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<h3>Microscope Images</h3>
<a href="${pageContext.request['contextPath']}/Create/Inspection?holder=<%=request.getParameter("barcode")%>" title="Record a new microscope inspection">New session...</a>
<div id="microscope_images" class="yui-skin-sam">Not currently available</div>

<h3>Completed Plate Inspections</h3>
<div style="padding-bottom: 1em" id="show_hide_inspection_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom:1em;display:none" id="inspection_filter">
<%@include file="./WEB-INF/jspf/filterInspections.jspf" %>
</div>
<div id="inspectionstable" class="yui-skin-sam"></div>

<h3><a href="${pageContext.request['contextPath']}/ViewAnnotations.jsp?barcode=<%=request.getParameter("barcode")%>" title="View Annotations">Annotations</a></h3>
<div style="padding-bottom: 1em" id="show_hide_annotation_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom:1em;display:none" id="annotation_filter">
<%@include file="./WEB-INF/jspf/filterAnnotations.jspf" %>
</div>
<div id="annotationstable" class="yui-skin-sam"></div>

<h3>Future Imaging Schedule</h3>
<div style="padding-bottom: 1em" id="show_hide_schedule_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom:1em;display:none" id="schedule_filter">
<%@include file="./WEB-INF/jspf/filterSchedules.jspf" %>
</div>
<div id="schedulestable" class="yui-skin-sam">Not currently available</div>

<div class="yui-skin-sam">
<%@include file="./WEB-INF/jspf/filtercalsInspections.jspf" %>
<%@include file="./WEB-INF/jspf/filtercalsSchedules.jspf" %>
<%@include file="./WEB-INF/jspf/filtercalsAnnotations.jspf" %>
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
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewInspections.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewSchedules.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewAnnotations.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewPlate.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkInspections = '<%=(null != request.getSession().getAttribute("sb_p_i")) ? request.getSession().getAttribute("sb_p_i") : "" %>';
PIMS.xtal.sessionBookmarkAnnotations = '<%=(null != request.getSession().getAttribute("sb_p_a")) ? request.getSession().getAttribute("sb_p_a") : "" %>';
PIMS.xtal.sessionBookmarkSchedules = '<%=(null != request.getSession().getAttribute("sb_p_sh")) ? request.getSession().getAttribute("sb_p_sh") : "" %>';
PIMS.xtal.barcode = '<%=(null != request.getParameter("barcode")) ? request.getParameter("barcode") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewPlate);
</script>

<%
	}
%>
<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>