<%--
  Brick name: quickSearch
  Rows: 1
  Columns: 1
  
  Quick search for any type of object.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
  <h3>Quick Search</h3>
  <div class="brickcontent">  
  <c:set var="options">
  </c:set>

  <jsp:include page="quickSearch_body.jsp" />  
  
</div>
