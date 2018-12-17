<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@attribute name="icon" required="true"  %><%-- everything after images/icons/ in the html src attribute --%>
<%@attribute name="url" required="true"  %>
<%@attribute name="text" required="true"  %>
<%@attribute name="title" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="isGreyedOut" required="false"  %>
<%@attribute name="contextMenu" required="false"  %>
<%@attribute name="isNext" required="false" type="java.lang.Boolean" %>
<%@attribute name="name" required="false"  %><%-- name to be used in alerts in context menu --%>

<c:if test="${isNext}"><c:set var="tabindex" value='tabindex="1"' /></c:if>

<c:if test="${empty onclick}">
    <c:set var="onclick" value="return warnChange()" />
</c:if>
<c:set var="greyedOutClass"></c:set>
<c:if test="${isGreyedOut}">
	<c:set var="onclick" value="return false"/>
	<c:set var="greyedOutClass" value="greyedout" />
</c:if>

<c:choose>
	<c:when test="${!empty param.isInModalWindow}">
		<c:set var="contextMenu" value=""/>
		<c:set var="links"><span class="linkwithicon ${greyedOutClass}" title="${title}"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/${icon}" alt=""/><span class="linktext"><c:out value="${text}" /></span></c:set>
	</c:when>
	<c:otherwise>
		<c:if test="${!empty contextMenu}"><c:set var="contextMenu">
		<img class="noprint" src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif" alt="" 
		  onclick="contextMenuName='${name}'; showContextMenu(this,{ ${contextMenu} })" />
		</c:set></c:if>
		<%-- annoyingly on the experiment page header, FF will render any new lines in this HTML --%>
		<c:set var="links"><span class="linkwithicon ${greyedOutClass}" ><a 
		onclick="${onclick}" href="${url}"><img title="${title}" class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/${icon}" alt=""/></a><a 
		${tabindex}
		onclick="${onclick}" id="aid${fn:replace(name,' ','_')}" href="${url}"><span class="linktext"><c:out value="${text}" /></span></a></c:set>
	</c:otherwise>
</c:choose>

${links}${contextMenu}</span>