<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="value" required="true"  %>
<%@attribute name="alias" required="false"  %>
<%@attribute name="label" required="false"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="isChecked" required="false" type="java.lang.Boolean" %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="datatype" required="false"  %>

<c:set var="checkedInsert" value="" />
<c:if test="${isChecked}">
<c:set var="checkedInsert" value="checked=\"checked\" class=\"checked\" " />
</c:if>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="disabled=\"disabled\"" />
</c:if>

<pimsForm:formItem extraClasses="radio" alias="${alias}" name="${name}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="" datatype="${type}" />
	<div class="formfield">
	<label>
	<input ${checkedInsert} type="radio" name="${name}" onclick="${onclick}" value="<c:out value="${value}"/>" ${disabled} />
	<span class="radiolabel">${label}</span></label>
	</div>
</pimsForm:formItem>