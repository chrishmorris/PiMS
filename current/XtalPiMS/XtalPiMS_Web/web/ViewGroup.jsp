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

<c:set var="HeaderName" scope="page" value="View Group" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>


<h3>View Group</h3>
<table class="list">
	<tr>
		<th>Name:</th>
		<td id="group_name"></td>
	</tr>
	<tr>
		<th>Group Head:</th>
		<td id="group_head"></td>
	</tr>
	<tr>
		<th>Organisation:</th>
		<td id="group_organisation"></td>
	</tr>
</table>
<a href="${pageContext.request['contextPath']}/ViewByName/org.pimslims.model.accessControl.UserGroup:<%=request.getParameter("name")%>">Edit</a>
<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<h3>Group Plates</h3>
<div style="padding-bottom: 1em;" id="show_hide_plate_filter"><img src='xtal/images/16x16/plus.png' alt='' />&nbsp;Show Filters</div>
<div style="padding-bottom: 1em;display:none;" id="plate_filter">
<%@include file="./WEB-INF/jspf/filterPlates.jspf"%>
</div>
<div id="platestable" class="yui-skin-sam"></div>

<h3>Group Constructs</h3>
<div style="padding-bottom: 1em" id="show_hide_construct_filter"><img alt='' src='xtal/images/16x16/plus.png' />&nbsp;Show Filters</div>
<div style="padding-bottom: 1em;display:none;" id="construct_filter">
<%@include file="./WEB-INF/jspf/filterConstructs.jspf"%>
</div>
<div id="constructstable" class="yui-skin-sam"></div>

<h3>Group Samples</h3>
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
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewConstructs.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewSamples.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewGroup.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkPlates = '<%=(null != request.getSession().getAttribute("sb_g_p")) ? request.getSession().getAttribute("sb_g_p") : "" %>';
PIMS.xtal.sessionBookmarkConstructs = '<%=(null != request.getSession().getAttribute("sb_g_ct")) ? request.getSession().getAttribute("sb_g_ct") : "" %>';
PIMS.xtal.sessionBookmarkSamples = '<%=(null != request.getSession().getAttribute("sb_g_s")) ? request.getSession().getAttribute("sb_g_s") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.groupName = '<%=(null != request.getParameter("name")) ? request.getParameter("name") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewGroup);
</script>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>