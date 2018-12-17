<%@attribute name="date" required="true" type="java.util.GregorianCalendar"  %>
<%@attribute name="suppressContextMenu" required="false" %>

<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:choose><c:when test="${null eq date}">
   <!-- date not specified -->
</c:when><c:when test="${_MHTML }">
    <span title="GMT"><fmt:formatDate value="${date.time}"  pattern="yyyy-MM-dd hh:mm:ss" timeZone="GMT" /></span>
</c:when><c:otherwise>
<a title="<fmt:formatDate value="${date.time}" dateStyle="full" type="both" timeZone="GMT" /> GMT"
  onclick='return warnChange()'
  href="${pageContext.request['contextPath']}/Day/<fmt:formatDate value="${date.time}" dateStyle="short" pattern="yyyy-MM-dd" />"
  >
  <script>
    document.write(timestampToString(${date.timeInMillis}));
  </script>
</a>
</c:otherwise></c:choose>