<%--
  Brick name: samplesReadyForUse
  Rows: 2
  Columns: 1
  
  Recently produced by successful experiments and haven't been used

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
  <h3 style="margin-bottom:0em" title="Your bookmarked PiMS pages">Bookmarks</h3>
  <jsp:include page="/read/FindBeans/" >
    <jsp:param name="pagesize" value="10" />
    <jsp:param name="_metaClass" value="org.pimslims.model.core.Bookmark" />
  </jsp:include>
  <div class="brickcontent">
  <c:forEach items="${beans}" var="bean"    >
    <pimsWidget:link bean="${bean}" />
  </c:forEach>
  <span style="text-align:center;display:block;margin-top:0.5em">
    <b><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.core.Bookmark">
    All Bookmarks</a></b></span>
   
  </div>