<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="onchange" required="false"  %>

<c:set var="onclickInsert" value="" />
<c:set var="onchangeInsert" value="" />
<c:if test="${!empty onclick}"><c:set var="onclickInsert">onclick="${onclick}"</c:set></c:if>
<c:set var="onchangeInsert">onchange="onEdit()"</c:set>
<c:if test="${!empty onchange}"><c:set var="onchangeInsert">onchange="${onchange}"</c:set></c:if>


<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" />
	<div class="formfield">
	<input ${disabled} type="password" name="${name}" id="${name}" ${onclickInsert} value="<c:out value='${value}'/>" ${onchangeInsert} />
	<span class="inputnoedit">*****</span>
	</div>
</pimsForm:formItem>