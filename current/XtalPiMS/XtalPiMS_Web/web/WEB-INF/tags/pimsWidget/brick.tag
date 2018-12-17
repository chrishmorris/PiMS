<%@attribute name="row" required="true" %>
<%@attribute name="column" required="true" %>
<%@attribute name="name" required="true" %>
<%@attribute name="height" required="false" %>
<%@attribute name="width" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:catch var="error">


<div style="height:${height}; left:${column}; top:${row}; width:${width}" class="brick" id="${name}">
<jsp:include page="${name}" />


</c:catch><c:if test="${error != null}">"/>
    <div class="brick row${row} col${column}  ${height}" id="${name}brick">
    <p class="error">error ${error}</p><error />
    </div>
    <c:set var="error" value="${null }" />
</c:if>

</div>