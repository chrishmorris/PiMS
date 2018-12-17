<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true" rtexprvalue="true" %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="onchange" required="false"  %>
<%@attribute name="datatype" required="false"  %>
<%@attribute name="isNext" required="false" type="java.lang.Boolean" %>

<%-- Note that validation breaks multi select.
You can suppress it by  pimsForm:form onsubmit="return true;" --%>
<%@attribute name="multiple" required="false"  %>

<c:if test="${!empty multiple }">
   <c:set var="multiple" value=" multiple=\"multiple\" " />
</c:if>

<c:set var="onclickInsert" value="" />
<c:set var="onchangeInsert" value="" />
<c:if test="${!empty onclick}"><c:set var="onclickInsert">onclick="${onclick}"</c:set></c:if>
<c:set var="onchangeInsert">onchange="onEdit()"</c:set>
<c:if test="${!empty onchange}"><c:set var="onchangeInsert">onchange="${onchange}"</c:set></c:if>

<jsp:doBody var="body"/>
<pimsForm:formItem name="${name}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" datatype="${datatype}" validation="${validation}" />
	<div class="formfield"  ${isNext ? ' tabindex="1" ' : '' } >
		<select name="${name}" ${onclickInsert} id="${name}" ${onchangeInsert}
		 ${multiple} >
            <c:forEach items="${fn:split(body, '*')}" var="option">
              ${option}
            </c:forEach>
		</select>
        <span class="selectnoedit">
            <c:forEach items="${fn:split(body, '*')}" var="option">
              <c:if test="${fn:contains(option,'selected=\"selected\"')}">
                  <c:set var="option" value="${fn:substringAfter(option, '>')}" />
                      ${fn:replace(option, '</option>', '')}
              </c:if>
            </c:forEach>
        </span>
	</div>
</pimsForm:formItem>