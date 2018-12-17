<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="title" required="true"  %><%-- may contain HTML, caller must escape user data --%>
<%@attribute name="id" required="false"  %>
<%@attribute name="initialState" required="false"  %><%-- open, closed, or fixed --%> 
<%@attribute name="extraHeader" required="false"  %>
<%@attribute name="extraClasses" required="false"  %>
<%@attribute name="src" required="false"  %><%-- URL of PiMS servlet for box contents --%>

<c:set var="stateClass" value="closedbox" />
<c:choose>
<c:when test="${null==initialState}">
    <c:set var="initialState" value="closed" />
</c:when>
<c:when test="${'open'==initialState}">
    <c:set var="stateClass" value="" />
</c:when>
<c:when test="${'fixed'==initialState}">
	<c:set var="stateClass" value="fixedbox" />
</c:when>
</c:choose>

<c:set var="idInsert" value="" />
<c:set var="idInsertHeader" value="" />
<c:set var="idInsertBody" value="" />
<c:if test="${!empty id}">
	<c:set var="idInsert" value="id=\"${id}\"" />
	<c:set var="idInsertHeader" value="id=\"${id}_head\"" />
	<c:set var="idInsertBody" value="id=\"${id}_body\"" />
    <c:set var="currentBox" value="${id}" scope="request" />
</c:if>
<div class="${extraClasses} collapsiblebox ${stateClass}" ${idInsert}>
	<div class="boxheader">
		<h3 ${idInsertHeader} onclick="toggleCollapsibleBox(this, '${src}')" onmouseover="this.onselectstart='return false';">${title}
		</h3>
		<c:if test="${!empty extraHeader}"><span class="extraheader">${extraHeader}</span></c:if>
	</div>
	<div ${idInsertBody} class="boxcontent">
	    <c:if test="${null!=src}"><img src="${pageContext.request.contextPath}/skins/default/images/icons/actions/waiting.gif" title="Loading..." />Loading</c:if>
		<jsp:doBody/>
	</div>
</div>
<c:if test="${null ne id}"><script type="text/javascript">
    if(document.location.toString().endsWith('#${id}')) {
        closeCollapsibleBox(document.getElementById('${id}_head'));
        toggleCollapsibleBox(document.getElementById('${id}_head'),'${src}');
    }
</script></c:if>
<c:set var="currentBox" value="" scope="request" />
