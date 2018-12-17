<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="label" required="false"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="datatype" required="false"  %>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<pimsForm:formItem name="${name}" alias="${label}">
	<pimsForm:formItemLabel name="${name}" alias="${label}" helpText="${helpText}" datatype="${datatype}" />
	<div class="formfield">
	<jsp:doBody />
	</div>
</pimsForm:formItem>