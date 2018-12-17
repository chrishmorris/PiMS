<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.presentation.sample.*"  %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%--
Author: Marc Savitsky
Date: 29 January 2008
Servlets: DivideSample

--%>
<jsp:useBean id="sample" scope="request" type="org.pimslims.presentation.ModelObjectBean" /> 

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Divide Sample or Stock: ${sample.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' /> 
</jsp:include>

<!-- OLD -->

<script language="javascript" type="text/javascript">
function validateForm(theForm) {
	var aliquots;
	for (var i = 0; i < theForm.elements.length; ++i) {
      	var element = theForm.elements[i];
      	if (element.name.match("aliquots"))
      		aliquots = element.value;
    }
	
	if (aliquots > 1)
		return true;
	alert("It doesn't make sense to divide into 0 or 1 aliquot!");
   	return false;
}
</script>

<h2>Divide sample <a href="${pageContext.request.contextPath}/View/${sample.hook}"><c:out value="${sample.name}" /></a> into equal aliquots</h2>

<c:choose>
<c:when test="${fn:length(sample.name) > 70}">
<p>The Sample name is too long, please shorten it before performing this function</p> 
</c:when>
<c:otherwise>
    	
<form method="post" action="${pageContext.request.contextPath}/update/DivideSample/${sample.hook}" onSubmit="return validateForm(this);">
    <%-- TODO CSRF token --%>
	<p>Divide the sample into&nbsp;
    <input style="width: 6em;" type="text" class="divide" size="3" name="aliquots" value="2" />
    &nbsp;aliquots</p>
    <script type="text/javascript">	if (null==focusElement) focusElement = document.getElementsByName('aliquots')[0] </script>
    <input type="submit" class="submit"  value="Save" />
</form>

</c:otherwise>
</c:choose>

</c:catch>
<c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
