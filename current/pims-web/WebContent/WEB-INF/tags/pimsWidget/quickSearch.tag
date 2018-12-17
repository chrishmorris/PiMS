<%-- Makes a "quick search" form--%>
<%@ attribute name="value" required="false" %>
<%@ attribute name="initialState" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget"  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm"   	tagdir="/WEB-INF/tags/pimsForm" %>
<!-- quickSearch.tag -->

<c:set var="form">
  <form method="get" class="grid " action="${requestScope['javax.servlet.forward.request_uri']}" mode="edit">
    <jsp:doBody/>
      <div class="width:98%; padding:0 1%;   "  >
    	<div style="float:left;  width:48%; padding-left: 1.9em; ">
			<pimsForm:text name="search_all" value="${value}" alias="Search For:"  isNext="${empty pageContext.request.queryString}" />
    	</div>
    	&nbsp;&nbsp;&nbsp;    	<input  type="submit" name="SUBMIT" value="Quick Search" onclick="dontWarn()" />
      
    </div>
  </form>
</c:set>

<c:choose><c:when test="${empty value && initialState ne 'open'}">
    <pimsWidget:box title="Quick Search">${form}</pimsWidget:box>
</c:when><c:otherwise>
    <pimsWidget:box title="Quick Search" initialState="open" >${form}</pimsWidget:box>
    <script type="text/javascript">   if (null==focusElement) focusElement = document.getElementsByName('search_all')[0] </script>
</c:otherwise></c:choose>

