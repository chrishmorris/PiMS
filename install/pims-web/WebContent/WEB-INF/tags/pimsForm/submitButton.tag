<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="onclick" required="false"  %>
<%@attribute name="buttonText" required="false"  %>

<c:if test="${empty buttonText}">
	<c:set var="buttonText" value="Submit" />
</c:if>


<pimsForm:formItem alias="" name="" extraClasses="submitCreate">
<c:choose>
<c:when test="${null ne onclick}">
	<input type="submit" value="${buttonText}" onclick="${onclick}" />
</c:when>
<c:otherwise>
	<input type="submit" value="${buttonText}" onclick="dontWarn()"  />
</c:otherwise>
</c:choose>
</pimsForm:formItem>