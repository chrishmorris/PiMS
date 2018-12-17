<%--
Display a list of PiMS records.
This fragment is used in e.g the generic view.
It is suitable for use in a delayed box

Date: May 2009
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<c:catch var="error">

<!-- ListBeansPaging.jsp -->

<c:set var="urlToUse">
    ${pageContext.request.requestURI}?<c:forEach var="entry" items="${param}" >
        <c:if test="${entry.key ne 'pagenumber' && entry.key ne 'pagesize'}">${entry.key}=${entry.value}&amp;</c:if>
    </c:forEach>    
 </c:set>
<span class="pagelinks">
<c:out value="${resultSize}" /> items found, displaying

<c:choose>
<c:when test="${resultSize <= pagesize}">
all items. <strong><c:out value="1" /></strong>
</c:when>
<c:otherwise>

<fmt:formatNumber type="Number" maxFractionDigits="0" >
<c:out value="${((pagenumber-1)*pagesize)+1}" />
</fmt:formatNumber>
to
<c:choose>
<c:when test="${(pagenumber)*pagesize > resultSize}">
<c:out value="${resultSize}" />.
</c:when>
<c:otherwise>
<fmt:formatNumber type="Number" maxFractionDigits="0" >
<c:out value="${((pagenumber)*pagesize)}" />.
</fmt:formatNumber>
</c:otherwise>
</c:choose>

<c:set var="maxPage">
<fmt:formatNumber type="Number" value="${(resultSize+(pagesize/2))/pagesize}" maxFractionDigits="0" groupingUsed="false" />
</c:set>
<c:set var="min" value="${pagenumber-4}" />
<c:if test="${min < 1}">
<c:set var="min" value="1" />
</c:if>
<c:set var="max" value="${min+7}" />
<c:if test="${max > maxPage}">
<c:set var="max" value="${maxPage}" />
</c:if>

<c:choose>
<c:when test="${pagenumber == 1}">
[First/Prev]
</c:when>
<c:otherwise>
[<a href="${urlToUse}pagenumber=1&amp;pagesize=${pagesize}" title="Go to page 1">First</a>/
<a href="${urlToUse}pagenumber=${pagenumber-1}&amp;pagesize=${pagesize}" title="Go to page ${pagenumber-1}">Prev</a>]
</c:otherwise>
</c:choose>

<c:forEach var="i" begin="${min}" end="${max}" step="1" varStatus ="status">
<c:choose>
<c:when test="${pagenumber == i}">
<strong><c:out value="${i}" /></strong><c:if test="${!status.last}">,&nbsp;</c:if>
</c:when>
<c:otherwise>
<a href="${urlToUse}pagenumber=${i}&amp;pagesize=${pagesize}" title="Go to page ${i}"><c:out value="${i}" /></a><c:if test="${!status.last}">,&nbsp;</c:if>
</c:otherwise>
</c:choose>
</c:forEach>

<c:choose>
<c:when test="${pagenumber == maxPage}">
[Next/Last]
</c:when>
<c:otherwise>
[<a href="${urlToUse}pagenumber=${pagenumber+1}&amp;pagesize=${pagesize}" title="Go to page ${pagenumber+1}">Next</a>/
<a href="${urlToUse}pagenumber=${maxPage}&amp;pagesize=${pagesize}" title="Go to page ${maxPage}">Last</a>]
</c:otherwise>
</c:choose>

</c:otherwise>
</c:choose>
</span>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- /ListBeansPaging.jsp -->

