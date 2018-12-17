<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.presentation.sample.*"  %> <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
Author: cm65
Date: 22 Nov 2007

--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<jsp:useBean id="hostobject" scope="request" type="org.pimslims.presentation.ModelObjectBean" /> 
<jsp:useBean id="objects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" /> 
<jsp:useBean id="categories" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Add Category to ${hostobject.classDisplayName}: ${hostobject.name}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' /> 
</jsp:include>

<c:choose>
    <c:when test="${empty categories}">
        <p><c:out value="${hostobject.classDisplayName}"/>&nbsp;<a href="${pageContext.request.contextPath}/View/${hostobject.hook}"><c:out value="${hostobject.name}"/></a> 
        has no category recorded.</p>
    </c:when>
    <c:otherwise>
    <a href="${pageContext.request.contextPath}/View/${hostobject.hook}"><c:out value="${hostobject.name}"/></a>
     is in 
     <c:forEach var="category" items="${categories}">
    <a href="${pageContext.request.contextPath}/View/${category.hook}">${category.name}</a>
    </c:forEach> 
    Categories.
    </c:otherwise>
</c:choose>

<c:choose><c:when test="${empty objects}">
    <p>No <c:out value="${hostobject.classDisplayName}"/>s are recorded, so you cannot move your <c:out value="${hostobject.classDisplayName}"/>.</p>
</c:when><c:otherwise>
<h2>Add Category: </h2>
<form method="post" action="${pageContext.request.contextPath}/update/AddSampleCategory/${hostobject.hook}" >
    <%-- TODO CSRF token --%>
    <select name="object" />
        <c:forEach var="object" items="${objects}" >
            <option value="${object.hook}"><c:out value="${object.name}" /></option>
        </c:forEach>
    </select> 
    <script type="text/javascript">	if (null==focusElement) focusElement = document.getElementsByName('object')[0] </script>   
    <input type="submit" class="submit"  value="Save" />
</form>
</c:otherwise></c:choose>

<a href="${pageContext.request.contextPath}/Create/org.pimslims.model.reference.SampleCategory">Record a new SampleCategory</a>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
