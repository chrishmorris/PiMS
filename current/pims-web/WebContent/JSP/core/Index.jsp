<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%--
The PiMS front page

Author: Chris Morris
Date: Jan 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="PiMS ${PIMS_VERSION}" />
    <jsp:param name="bodyCSSClass" value="bricks" />
    <jsp:param name="extraStylesheets" value="custom/bricks5.0.0" />
    <jsp:param name="extraStylesheets_IE" value="custom/bricks-IE" />
</jsp:include>

<c:catch var="error">
obsolete, see Dashboard.jsp and Bookmark servlet

<pimsWidget:brick row="0" column="0" name="calendar" height="doubleheight" />
<pimsWidget:brick row="2" column="0" name="activeTargets" height="doubleheight" />

<pimsWidget:brick row="0" column="1" name="newTarget" height="doubleheight" />
<pimsWidget:brick row="2" column="1" name="quickSearch" height="doubleheight" />

<pimsWidget:brick row="0" column="2" name="history"  height="tripleheight" />


<pimsWidget:brick row="3" column="2" name="barcodeSearch" />


<%-- Bricks not in use
<pimsWidget:homepageBrick row="0" column="2" name="constructsNoProgress" />
<pimsWidget:homepageBrick row="2" column="1" name="targetManagement" />
--%>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />