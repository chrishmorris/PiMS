<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<%@attribute name="mode" required="true"  %>
<%@attribute name="action" required="true"  %><%-- not including context path --%>
<%@attribute name="boxToOpen" required="false"  %><%-- the id of a box which should be open on the response page --%>
<%@attribute name="method" required="true"  %><%-- This should usually be "post" --%>
<%@attribute name="id" required="false"  %>
<%@attribute name="extraClasses" required="false"  %>
<%@attribute name="enctype" required="false"  %>
<%@attribute name="onsubmit" required="false"  %><%-- default is validateFormFields, "return true;" to suppress --%>

<c:if test="${empty onsubmit }">
   <c:set var="onsubmit" value="return validateFormFields(this)" />
</c:if>

<c:set var="formMode" scope="request" value="${mode}" />

<c:set var="idInsert" value="" />
<c:if test="${!empty id}"><c:set var="idInsert" value="id=\"${id}\"" /></c:if>

<c:set var="extraCSSClasses" value="" />
<c:choose>
<c:when test="${'edit'==mode}"><c:set var="extraCSSClasses" value="" /></c:when>
<c:when test="${'create'==mode}"><c:set var="extraCSSClasses" value="create" /></c:when>
<c:otherwise><c:set var="extraCSSClasses" value="viewing" /></c:otherwise>
</c:choose>

<%-- append any user-specified CSS classes --%>
<c:if test="${!empty extraClasses}">
    <c:set var="extraCSSClasses" value="${extraCSSClasses} ${extraClasses}" />
</c:if>

<c:choose>
<c:when test="${null ne enctype}">
	<form autocomplete="off" action="${pageContext.request.contextPath}${action}" method="${method}" ${idInsert} class="grid ${extraCSSClasses}" onsubmit="${onsubmit }" enctype="${enctype}" >
</c:when>
<c:otherwise>
	<form autocomplete="off" action="${pageContext.request.contextPath}${action}" method="${method}" ${idInsert} class="grid ${extraCSSClasses}" onsubmit="${onsubmit }" >
</c:otherwise>
</c:choose>

<c:if test="${'post' eq method}">
  <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,action)}" />
  <input type="hidden" name="_fetched" value="<%= System.currentTimeMillis() %>" />
</c:if>

<c:choose>
<c:when test="${null ne boxToOpen}"><input type="hidden" name="<%= org.pimslims.servlet.Update.BOX_TO_OPEN %>" value="${boxToOpen}" /></c:when>
<c:when test="${!empty currentBox}"><input type="hidden" name="<%= org.pimslims.servlet.Update.BOX_TO_OPEN %>" value="${currentBox}" /></c:when>
</c:choose>
<c:choose><c:when test="${'' ne action}">
    <%-- TODO <input type="hidden" name="_csrf" value="${utils:csrfToken(pageContext.request,action)}" /> --%>
</c:when><c:otherwise>
    <%-- TODO show warning --%>
</c:otherwise></c:choose>
<jsp:doBody/>
<%-- 
This script block should be completely unnecessary, but Firefox insists on remembering form values.
--%>
<script type="text/javascript">
//Reset disabled form buttons along with their text
$(document.body).select("input[type='submit']").each(function(btn){
        btn.disabled="";
        if("Creating..."==btn.value){ btn.value="Create"; }        
        if("Saving..."==btn.value){ btn.value="Save changes"; }        
});
<c:if test="${'view' eq formMode}">
//Also radio buttons - if form served as "viewing", select the radio button with class "checked"
$(document.body).select("input[type='radio']").each(function(rad){
    if(rad.hasClassName("checked")){
        rad.checked="checked";
    } else {
        rad.checked="";
    }
});
</c:if>
</script>
</form>
<c:set var="formMode" scope="request" value="${null}" />
