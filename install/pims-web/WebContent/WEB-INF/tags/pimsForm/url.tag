<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" required="false"  %>
<%@attribute name="validation" required="false"  %>

<c:set var="trimmedValue" value="${value}" />
<%-- TODO trim middle from URL if too long for display  --%>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" />
	<div class="formfield hyperlink">
	<input onchange="onEdit()" ${disabled} type="text" name="${name}" id="${name}" value="<c:out value='${value}'/>" />
	<span class="urlnoedit"><a onclick='return warnChange()' href="<c:out value='${value}'/>" title="<c:out value='${value}'/>">${trimmedValue}</a></span>
	</div>
</pimsForm:formItem>