<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 
History is written in three places - if you're changing one, you probably want to
update the others too:

* JSP/MRUInfo.jsp - the full history page
* JSP/historyMenu.jsp - the pull-down menu in the menu bar
* JSP/homepage-bricks/history.jsp - the homepage brick
 
--%>

<c:choose><c:when test="${empty requestScope.menuMRUs }">
        <li title="You have no history in PiMS, or it was cleared after an error" >[ None ]</li>
</c:when><c:otherwise>
      <c:forEach var="m" items="${ requestScope.menuMRUs }">
        <li><pimsWidget:link bean="${m}" suppressContextMenu="true" /></li>
      </c:forEach>
      <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/JSP/core/MRUInfo.jsp">More...</a></li>
</c:otherwise></c:choose>