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
<%@attribute name="options" required="true" type="java.util.Collection" %>
<%@attribute name="selected" required="true" type="java.lang.String[]" %>

<%-- see http://harvesthq.github.io/chosen/index.proto.html  
Could also use it for single selects. See also footerJavascript.jsp.
  --%>


<c:set var="onclickInsert" value="" />
<c:set var="onchangeInsert" value="" />
<c:if test="${!empty onclick}"><c:set var="onclickInsert">onclick="${onclick}"</c:set></c:if>
<c:set var="onchangeInsert">onchange="onEdit()"</c:set>
<c:if test="${!empty onchange}"><c:set var="onchangeInsert">onchange="${onchange}"</c:set></c:if>


<pimsForm:formItem name="${name}" alias="Choose ${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" datatype="${datatype}" validation="${validation}" />
	<div class="formfield"  ${isNext ? ' tabindex="1" ' : '' } >
		<select name="${name}" ${onclickInsert} id="${name}" ${onchangeInsert}
		data-placeholder="${alias}"
		 multiple="multiple" class="chosen-select" title="${helpText}" >
            <c:forEach items="${options}" var="option">
              <c:set var="selectedattr" value="" />
              <c:forEach items="${selected }" var="p">
                <c:if test="${option eq p}">
                  <c:set var="selectedattr" value=" selected=\"selected\" " />
                </c:if>
              </c:forEach>
              <option value="${option}" ${selectedattr }>${option}</option>             
            </c:forEach>
		</select>
        <span class="selectnoedit">
            <c:forEach items="selected" var="option">
                      ${option}
            </c:forEach>
        </span>
	</div>
</pimsForm:formItem>