<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="optionValue" required="true"  %>
<%@attribute name="currentValue" required="true"  %>
<%@attribute name="alias" required="true"  %><%-- may be HTML, caller must escape --%>
<%@attribute name="helpText" required="false"  %>
<%-- The "*" below is a separator that is removed in the pimsForm:select tag--%>
<c:choose><c:when test="${optionValue eq currentValue}">
    <option value="${optionValue}" title="${helpText}" selected="selected">${alias}</option>*
</c:when><c:otherwise>
    <option value="${optionValue}" title="${helpText}">${alias}</option>*
</c:otherwise></c:choose>