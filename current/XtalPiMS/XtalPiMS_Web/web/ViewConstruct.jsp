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

<c:set var="HeaderName" scope="page" value="View Construct" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>
<%
	if (request.getParameter("id") == null) {
		throw new javax.servlet.ServletException("Unable to show page as you have not specified a construct name correctly!");
	} else {
%>

<h3>Construct Information</h3>
<table class="list">
	<tr>
		<th>Name:</th>
		<td id="construct_name"></td>
	</tr>
	<tr>
		<th>Description:</th>
		<td id="construct_description"></td>
	</tr>
	<tr>
		<th>Further Details:</th>
		<td id="construct_history"></td>
	</tr>
	<tr>
		<th>Target:</th>
		<td id="construct_target"></td>
	</tr>
	<tr>
		<th>Owner:</th>
		<td id="construct_owner"></td>
	</tr>
	<tr>
		<th>Group:</th>
		<td id="construct_group"></td>
	</tr>
</table>
<a href="${pageContext.request['contextPath']}/View/org.pimslims.model.target.ResearchObjective:<%=request.getParameter("id")%>">Edit</a>

<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<h3>Plates</h3>
<div style="padding-bottom: 1em;" id="show_hide_plate_filter"><img src='xtal/images/16x16/plus.png' alt='' />&nbsp;Show Filters</div>
<div style="padding-bottom: 1em;display:none;" id="plate_filter">
<%@include file="./WEB-INF/jspf/filterPlates.jspf"%>
</div>
<div id="platestable" class="yui-skin-sam"></div>

<h3>Samples</h3>
<div style="padding-bottom: 1em" id="show_hide_sample_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom: 1em;display:none;" id="sample_filter">
<%@include file="./WEB-INF/jspf/filterSamples.jspf"%>
</div>
<div id="samplestable" class="yui-skin-sam"></div>

<div class="yui-skin-sam">
<%@include file="./WEB-INF/jspf/filtercalsPlates.jspf"%>
<%@include file="./WEB-INF/jspf/filtercalsSamples.jspf"%>
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
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewPlates.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewSamples.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewConstruct.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkPlates = '<%=(null != request.getSession().getAttribute("sb_c_p")) ? request.getSession().getAttribute("sb_c_p") : "" %>';
PIMS.xtal.sessionBookmarkSamples = '<%=(null != request.getSession().getAttribute("sb_c_s")) ? request.getSession().getAttribute("sb_c_s") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.constructId = '<%=(null != request.getParameter("id")) ? request.getParameter("id") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewConstruct);
</script>

<%
	}
%>
<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>