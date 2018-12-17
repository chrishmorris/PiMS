<%-- Provides a link to the current page, but with changes to the query string
 --%>
<%@attribute name="text" required="true" %>
<%@attribute name="parms" required="true" %><%-- key: value, ...  --%>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${!_MHTML }">
  <a href="#" onclick="queryLink({<c:out value='${parms }' />}); " ><c:out value="${text }" /></a>
</c:if>