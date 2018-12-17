<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="alias" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="id" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="onchange" required="false"  %>
<%@attribute name="onkeyup" required="false"  %>
<%@attribute name="extraClasses" required="false"  %>


<c:set var="disabled" value="" />
<c:if test="${'view'==formMode}">
	<c:set var="disabled" value="readonly=\"readonly\"" />
</c:if>

<c:if test="${empty id}">
	<c:set var="id" value="${name}" />
</c:if>
<jsp:doBody var="body"/>
<pimsForm:formItem extraClasses="textarea ${extraClasses}" name="${id}" alias="${alias}" validation="${validation}">
	<pimsForm:formItemLabel name="${name}" alias="${alias}" helpText="${helpText}" validation="${validation}" />
	<div class="formfield">
	    <c:choose><c:when test="${_MHTML eq 'true'}">
	       <br /><div class="${extraClasses}">${body}</div>
	    </c:when><c:otherwise>
		    <textarea class="noprint" onchange="onEdit(); ${onchange}" 
		        onkeyup="matchTextareaHeightToContent(this); ${onkeyup}" 
		        onfocus="matchTextareaHeightToContent(this)" ${disabled} 
		        name="${name}" id="${id}"
		     ><%-- ${fn:escapeXml(body)} // double escape --%>${body}</textarea>
		     <%-- the above must all be on one line - newlines show up inside the textarea --%>		     
             <div class="printonly">${body}</div>
        </c:otherwise></c:choose>
		<script type="text/javascript">
		matchTextareaHeightToContent(document.getElementById("${id}"));
		</script>
	</div>
</pimsForm:formItem>