<%--
  Brick name: activeTargets
  Rows: 2
  Columns: 1
  
  Display a list of targets active in the last 30 days.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <h3 style="margin-bottom:0em" title="Targets active in the last 30 days">
   My targets
   
  </h3><%-- 
  <div class="brickcontent">
  <span style="text-align:left;display:block;margin-top:0.5em; padding-left: 0.8em;">
   <b><a href="${pageContext.request.contextPath}/read/TargetProgress">
   All Active Targets</a></b></span> --%>
  <c:if test="${! empty username}">
  	<jsp:include page="/read/TargetProgress?isShort=Yes" />
  </c:if>
  </div>
