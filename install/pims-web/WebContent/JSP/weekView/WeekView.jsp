<%@ page language="java" contentType="text/html; charset=utf-8"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Week View: ${displaydate}" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<c:catch var="error">

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a></c:set>
<c:set var="icon" value="date.png" />        
<c:set var="title" value="Week of ${displaydate}"/>
<c:set var="actions">
	<a href="${pageContext.request.contextPath}/Week/${prevweek}">Previous week</a>
	<a href="${pageContext.request.contextPath}/Week/${nextweek}">Next week</a>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:set var="day" scope="request" value="${monday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${tuesday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${wednesday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${thursday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${friday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${saturday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />
<c:set var="day" scope="request" value="${sunday}" />
<jsp:include page="/JSP/weekView/WeekViewDay.jsp" />

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
