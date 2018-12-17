<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="label" required="true"  %>
<%@attribute name="isChecked" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="onchange" required="false"  %>

<c:set var="onclickInsert" value="" />
<c:set var="onchangeInsert" value="" />
<c:if test="${!empty onclick}"><c:set var="onclickInsert">onclick="${onclick}"</c:set></c:if>
<c:set var="onchangeInsert">onchange="onEdit()"</c:set>
<c:if test="${!empty onchange}"><c:set var="onchangeInsert">onchange="${onchange}"</c:set></c:if>

<c:set var="checkedInsert" value="" />
<c:if test="${isChecked}">
<c:set var="checkedInsert" value="checked=\"checked\" class=\"checked\"" />
</c:if>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="disabled=\"disabled\"" />
</c:if>

<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<label for="${name}"><input ${disabled} type="checkbox" ${onclickInsert} name="${name}" ${onchangeInsert} id="${name}" ${checkedInsert} /> ${label}</label>
</pimsForm:formItem>