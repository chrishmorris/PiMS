<%@ tag body-content="tagdependent" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="value" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="onchange" required="false"  %>
<%@attribute name="datatype" required="false"  %>
<%@attribute name="isNext" required="false" type="java.lang.Boolean" %>


<c:if test="${isNext}"><c:set var="tabindex" value='tabindex="1"' /></c:if>

<c:set var="onclickInsert" value="" />
<c:set var="onchangeInsert" value="" />
<c:if test="${!empty onclick}"><c:set var="onclickInsert">onclick="${onclick}"</c:set></c:if>

<%-- begin build onchange attribute --%>
<%-- first, set it to the standard taint check... --%>
<c:set var="onchangeInsert">onEdit();</c:set>

<%-- ...if validation includes uniqueness check, add that... --%>
<c:if test="${fn:contains(validation, 'unique:')}">
    <c:set var="onchangeInsert">${onchangeInsert}checkUnique(this);</c:set>
</c:if>

<%-- ...if there's an additional onchange handler, add that... --%>
<c:if test="${!empty onchange}">
    <c:set var="onchangeInsert">${onchangeInsert}${onchange}</c:set>
</c:if>

<%-- ...and stuff it all into onchange="" --%>
<c:set var="onchangeInsert">onchange="${onchangeInsert}"</c:set>
<%--end build onchange attribute--%>

<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" datatype="${datatype}" />
	<div class="formfield" >
	<input ${disabled}  ${tabindex} type="text" name="${name}" id="${name}" ${onclickInsert} value="<c:out value='${value}'/>" ${onchangeInsert} />
	<c:if test="${null ne value and '' ne value}">
	    <span class="inputnoedit"><c:out value='${value}'/></span>
	</c:if>
	</div>
</pimsForm:formItem>