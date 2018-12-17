<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="labelValuePairs" required="true" type="java.util.LinkedHashMap" %>
<%@attribute name="checkedValue" required="true"  %>
<%@attribute name="onclick" required="false"  %>

<c:forEach items="${labelValuePairs}" var="lvp">
	<pimsForm:radio name="${name}" alias="${alias}" value="${lvp.key}" label="${lvp.value}" onclick="${onclick}" isChecked="${checkedValue==lvp.key}"/>
	<c:set var="alias" value="" />
</c:forEach>