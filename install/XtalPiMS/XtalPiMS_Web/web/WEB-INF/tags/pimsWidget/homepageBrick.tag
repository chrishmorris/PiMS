<%@attribute name="row" required="true" %>
<%@attribute name="column" required="true" %>
<%@attribute name="name" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:catch var="error">

<jsp:include page="/JSP/homepage-bricks/${name}.jsp">
	<jsp:param name="row" value="${row}" />
	<jsp:param name="column" value="${column}" />
</jsp:include>


</c:catch><c:if test="${error != null}">"/>
    <div class="brick row${row} col${column}  doubleheight" id="${name}brick">
    <p class="error">error ${error}</p><error />
    </div>
    <c:set var="error" value="${null }" />
</c:if>