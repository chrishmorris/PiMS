<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ tag body-content="tagdependent" %>
<!-- deleteLink -->
<c:choose>
<c:when test="${bean.mayDelete}">
	<pimsWidget:linkWithIcon 
		text="Delete" title="Delete ${bean.classDisplayName} ${utils:escapeJS(bean.name)}" icon="actions/delete.gif"
		url="${pageContext.request.contextPath}/Delete/${bean.hook}" 
		onclick="contextmenu_delete('${utils:escapeJS(bean.name)}','${bean.hook}', '${bean.classDisplayName}');return false" />
</c:when><c:otherwise>
	<pimsWidget:linkWithIcon 
		text="Can't delete" title="You can't delete this record" icon="actions/delete_no.gif"
		url="#" isGreyedOut="true"
		onclick="return false" />
</c:otherwise>
</c:choose>
<!-- /deleteLink -->