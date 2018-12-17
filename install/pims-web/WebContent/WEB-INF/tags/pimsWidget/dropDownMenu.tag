<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="icon" required="true"  %><%-- everything after images/icons/ in the html src attribute --%>
<%@attribute name="text" required="true"  %>
<%@attribute name="title" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="isGreyedOut" required="false"  %>
<%@attribute name="contextMenu" required="true"  %>
<%@attribute name="name" required="false"  %><%-- name to be used in alerts in context menu --%>

<c:set var="greyedOutClass"></c:set>
<c:if test="${isGreyedOut}">
	<c:set var="onclick" value="return false"/>
	<c:set var="greyedOutClass" value="greyedout" />
</c:if>
<c:set var="contextMenuImg">
<img class="noprint" src="${pageContext.request.contextPath}/skins/default/images/icons/misc/pulldown.gif" alt="" 
  onclick="var name='${name}'; showContextMenu(this,{ ${contextMenu} })" />
</c:set>
<%-- annoyingly on the experiment page header, FF will render any new lines in this HTML --%>
<c:set var="links"><span class="linkwithicon ${greyedOutClass}" title="${title}"><a onclick="var name='${name}'; showContextMenu(this,{ ${contextMenu} })" href="#"><img class="icon" src="${pageContext.request.contextPath}/skins/default/images/icons/${icon}" alt=""/></a><a onclick="var name='${name}'; showContextMenu(this,{ ${contextMenu} })" href="#">${text}</a></c:set>
${links}${contextMenuImg}</span>