<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="name" required="true"  %>
<%@attribute name="extraClasses" required="false"  %>
<%@attribute name="validation" required="false"  %>

<c:if test="${empty extraClasses}"><c:set var="extraClasses" value="" /></c:if>

<jsp:doBody var="body"/>
<div class="formitem <c:out value='${extraClasses}'/>">
${body}
<c:if test="${!empty validation }">
	<script type="text/javascript">
	attachValidation("${name}", {${validation}, alias:"${alias}"} );
	</script>
</c:if>
</div>