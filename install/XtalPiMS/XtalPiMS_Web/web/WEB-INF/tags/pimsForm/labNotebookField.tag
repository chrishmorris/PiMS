<%@ taglib prefix="pimsForm" 
  tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="name" required="true"  %>
<%@attribute name="helpText" required="false"  %>
<%@attribute name="validation" required="false"  %>
<%@attribute name="objects" required="true"  type="java.util.Collection" %>
<%@attribute name="currentValue" required="false"  %>

<%-- Admin can create in reference LN, which is "", so override any "required:true" --%>
<c:if test="${isAdmin}"><c:set var="validation" value="" /></c:if>

<%-- Create requires value of _OWNER parameter to be name not a hook --%>
<c:choose><c:when test="${name eq '_OWNER'}">

	<pimsForm:select name="${name}" alias="Lab Notebook" helpText="${helpText}" validation="${validation}">
	<c:if test="${empty currentValue}">
		<option value="" selected="selected">Choose...</option>
	</c:if>
	<c:forEach var="p" items="${objects}">
        <c:choose><c:when test="${currentValue eq p.hook}">
            <option value="<c:out value="${p.name}" />" selected="selected" ><c:out value="${p.name}" /></option>
        </c:when><c:otherwise>
            <option value="<c:out value="${p.name}" />" /><c:out value="${p.name}"/></option>
        </c:otherwise></c:choose>
    </c:forEach> 
    
    <c:if test="${'administrator'==username}">
    	<option value="<c:out value="reference"/>" />reference</option>
    </c:if>
    
	</pimsForm:select>

</c:when><c:otherwise>

	<pimsForm:select name="${name}" alias="Lab Notebook" helpText="${helpText}" validation="${validation}">
	<c:forEach var="p" items="${objects}">
        <c:choose><c:when test="${currentValue eq p.hook}">
            <option value="<c:out value="${p.hook}" />" selected="selected" ><c:out value="${p.name}" /></option>
        </c:when><c:otherwise>
            <option value="<c:out value="${p.hook}"/>" /><c:out value="${p.name}"/></option>
        </c:otherwise></c:choose>
    </c:forEach> 
	
	<c:if test="${'administrator'==username}">
    	<option value="<c:out value=""/>" />reference</option>
    </c:if>

    </pimsForm:select>

</c:otherwise></c:choose>

