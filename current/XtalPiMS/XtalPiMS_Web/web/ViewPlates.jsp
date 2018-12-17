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

<c:set var="HeaderName" scope="page" value="View Plate Experiments" />

<%@include file="./WEB-INF/jspf/header-min.jspf"%>

<%-- HTML STARTS HERE --%>

<h3>My Plate Experiments</h3>
<a href="${pageContext.request['contextPath']}/CreateTrialPlate">Create new plate</a>

<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<div style="padding-bottom:1em;">
<%@include file="./WEB-INF/jspf/filterPlates.jspf"%>
</div>
<div id="platestable" class="yui-skin-sam"></div>

<div class="yui-skin-sam">
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
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/initViewPlates.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmarkPlates = '<%=(null != request.getSession().getAttribute("platesDataTable")) ? request.getSession().getAttribute("platesDataTable") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initViewPlates);
</script>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>