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

<c:set var="HeaderName" scope="page" value="View Conditions" />

<%@include file="./WEB-INF/jspf/header-min.jspf"%>

<%-- HTML STARTS HERE --%>
<h3>Crystal Trial Conditions</h3>
<%--
<p>There are a couple of issues with this page at the moment. Firstly, the list of
conditions isn't paged. As a consequence, if you search for all conditions then
none will be returned, as otherwise too many are returned. Secondly, if you filter
by component, the conditions returned show only those components that match the
filter. This should show all the components for those conditions that have a
component that matches the filter. The two problems are related, and its going to
take a little while to figure out how to solve them.</p>
--%>
<c:if test="${!empty param.localName}">
    <a href="${pageContext.request['contextPath']}/ExportScreen/<%=request.getParameter("localName")%>">Export as XML</a>
    <%-- TODO export as CSV --%>
    <a href="${pageContext.request['contextPath']}/update/EditScreen/<%=request.getParameter("localName")%>">Edit</a>
</c:if>
<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<div style="padding-bottom:1em;">
<%@include file="./WEB-INF/jspf/filterConditions.jspf" %>
</div>
<div id="conditionstable" class="yui-skin-sam"></div>

<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/history/history-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/json/json-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/calendar/calendar-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DataFilter.js.ycomp.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewConditions.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkConditions = '<%=(null != request.getSession().getAttribute("conditionsDataTable")) ? request.getSession().getAttribute("conditionsDataTable") : "" %>';
PIMS.xtal.localName = '<%=(null != request.getParameter("localName")) ? request.getParameter("localName") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewConditions);
</script>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>