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
<%@taglib prefix="bricks" uri="http://www.pims-lims.org/bricks"%>
<%@taglib prefix="xtalpims" uri="http://www.pims-lims.org/xtalpims" %>

<c:set var="HeaderName" scope="page" value="Home" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>



<%-- HTML STARTS HERE --%>
<!-- Dependencies -->

<iframe id="yui-history-iframe" class="yui-history-iframe" src="${pageContext.request['contextPath']}/xtal/blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>
<bricks:brickGrid columns="2" width="100%">
	<bricks:brickRow>
		<c:choose><c:when test="${homepageTopLeft=='inspections'}">
				<%@include file="Home_inspections.jsp" %>
			</c:when><c:when test="${homepageTopLeft=='annotations'}">
				<%@include file="Home_annotations.jsp" %>
			</c:when><c:when test="${homepageTopLeft=='microscopeImages'}">
				<%@include file="Home_microscopeImages.jsp" %>
			</c:when><c:when test="${homepageTopLeft=='groups'}">
				<%@include file="Home_groups.jsp" %>
			</c:when><c:when test="${homepageTopLeft=='imagers'}">
				<%@include file="Home_imagers.jsp" %>
			</c:when><c:when test="${homepageTopLeft=='newDiamondProject'}">
				<%@include file="Home_newDiamondProject.jsp" %>
		</c:when></c:choose>
		<c:choose><c:when test="${homepageTopRight=='inspections'}">
				<%@include file="Home_inspections.jsp" %>
			</c:when><c:when test="${homepageTopRight=='annotations'}">
				<%@include file="Home_annotations.jsp" %>
			</c:when><c:when test="${homepageTopRight=='microscopeImages'}">
				<%@include file="Home_microscopeImages.jsp" %>
			</c:when><c:when test="${homepageTopRight=='groups'}">
				<%@include file="Home_groups.jsp" %>
			</c:when><c:when test="${homepageTopRight=='imagers'}">
				<%@include file="Home_imagers.jsp" %>
			</c:when><c:when test="${homepageTopRight=='newDiamondProject'}">
				<%@include file="Home_newDiamondProject.jsp" %>
		</c:when></c:choose>
	</bricks:brickRow>
	<bricks:brickRow>
		<c:choose><c:when test="${homepageBottomLeft=='inspections'}">
				<%@include file="Home_inspections.jsp" %>
			</c:when><c:when test="${homepageBottomLeft=='annotations'}">
				<%@include file="Home_annotations.jsp" %>
			</c:when><c:when test="${homepageBottomLeft=='microscopeImages'}">
				<%@include file="Home_microscopeImages.jsp" %>
			</c:when><c:when test="${homepageBottomLeft=='groups'}">
				<%@include file="Home_groups.jsp" %>
			</c:when><c:when test="${homepageBottomLeft=='imagers'}">
				<%@include file="Home_imagers.jsp" %>
			</c:when><c:when test="${homepageBottomLeft=='newDiamondProject'}">
				<%@include file="Home_newDiamondProject.jsp" %>
		</c:when></c:choose>
		<c:choose><c:when test="${homepageBottomRight=='inspections'}">
				<%@include file="Home_inspections.jsp" %>
			</c:when><c:when test="${homepageBottomRight=='annotations'}">
				<%@include file="Home_annotations.jsp" %>
			</c:when><c:when test="${homepageBottomRight=='microscopeImages'}">
				<%@include file="Home_microscopeImages.jsp" %>
			</c:when><c:when test="${homepageBottomRight=='groups'}">
				<%@include file="Home_groups.jsp" %>
			</c:when><c:when test="${homepageBottomRight=='imagers'}">
				<%@include file="Home_imagers.jsp" %>
			</c:when><c:when test="${homepageBottomRight=='newDiamondProject'}">
				<%@include file="Home_newDiamondProject.jsp" %>
		</c:when></c:choose>
	</bricks:brickRow>

</bricks:brickGrid>

<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/history/history-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/json/json-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DataFilter.js.ycomp.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmark = '<%=(null != request.getSession().getAttribute("platesDataTable")) ? request.getSession().getAttribute("homeDataTable") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initHome);
</script>


<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>