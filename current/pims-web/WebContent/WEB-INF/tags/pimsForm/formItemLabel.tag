<%@tag body-content="empty" %>
<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="false"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="datatype" required="false"  %>

<c:set var="tooltip" value="" />
<c:if test="${!empty helpText}">
    <c:set var="tooltip" value="title=\"${helpText}\"" />
</c:if>

<c:set var="requiredSign" value=""/>
<c:if test="${fn:contains(validation,'required:true')}">
<c:set var="requiredSign"><span class="required">*</span></c:set>
</c:if>

<div class="fieldname" >
<label for="${name}" ${tooltip}><pimsForm:dataTypeIcon datatype="${datatype}" />
    <c:choose><c:when test="${!empty alias}"><c:out value='${alias}'/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose>
</label>${requiredSign}
</div>