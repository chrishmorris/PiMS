<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="hook" required="true"  %>
<%@attribute name="label" required="false"  %>

<c:set var="url">${pageContext.request.contextPath}/Graph/${hook}</c:set>
<c:set var="modalWindowURL">${url}</c:set>

<c:if test="${empty label}">
    <c:set var="label" value="Diagram" />
</c:if>


<pimsWidget:linkWithIcon 
	text="${label}"
	title="View diagram"
	url="${url}"
    onclick="openModalWindow('${modalWindowURL}','Diagram');return false;"
	icon="actions/viewdiagram.gif" />	
	