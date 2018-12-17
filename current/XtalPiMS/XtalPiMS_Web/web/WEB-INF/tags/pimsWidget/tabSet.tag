<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="currentTab" required="true"  %>
<%@attribute name="id" required="false"  %>

<c:set var="currentTab" value="${currentTab}" scope="request" />
<c:set var="idInsert" value="" />
<c:if test="${!empty id}">
    <c:set var="idInsert">id="${id}"</c:set>
</c:if>

<div ${idInsert} class="pw_tabset">
<jsp:doBody />
</div>
<script type="text/javascript">
Event.observe(window,"load",function(){resizeTabSetsOnload()});
</script>
