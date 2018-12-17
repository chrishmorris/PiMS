<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="icon" required="false"  %>
<%@attribute name="title" required="false"  %>
<%@attribute name="actions" required="false"  %>
<%@attribute name="breadcrumbs" required="false"  %>
<%@attribute name="pageType" required="false"  %>
<%@attribute name="suppressCalendar" required="false"  %>

<c:if test="${empty isCreate}"><c:set var="isCreate" value="${false}"/></c:if>
<c:if test="${empty breadcrumbs}"><c:set var="breadcrumbs"><a href="${pageContext.request['contextPath']}">Home</a></c:set></c:if>
<c:if test="${empty icon}"><c:set var="icon" value="blank.png"/></c:if>
<c:if test="${empty title}"><c:set var="title" value="Page"/></c:if>
<c:if test="${empty actions}"><c:set var="actions" value="&nbsp;"/></c:if>

<div class="pagetitle">
<c:if test="${empty suppressCalendar}">
    <div id="titlecalendar"><pimsWidget:calendar /></div>
</c:if>
<%-- <img class="mainicon" src="${pageContext.request.contextPath}/skins/default/images/icons/types/large/${icon}" /> --%>
<img class="mainicon" src="${pageContext.request['contextPath']}/skins/default/images/icons/types/large/${icon}" />

<%--
 * Commented out for 3.1 - temporary icons too ugly for release
 *
<c:if test="${!empty pageType}">
	<img class="pagetype" src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pageTypes/${pageType}.gif" />
</c:if>
--%>

<div class="pagetitletext"><div class="breadcrumbs">${breadcrumbs} </div>
<h1><c:out value="${title}" /></h1>
<span class="noprint actions">${actions}</span>
</div></div>