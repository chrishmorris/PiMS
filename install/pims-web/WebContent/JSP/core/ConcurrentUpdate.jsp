<%@ page isErrorPage="true"
contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: cm65
Date: 20 Nov 2012
Servlets: org.pimslims.servlet.Update
--%>


<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${pageContext.errorData.throwable.message}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

Sorry, someone else has just updated <pimsWidget:link bean="${pageContext.errorData.throwable.bean}" />

<c:set var="referer"><%= request.getHeader("referer") %></c:set>
<c:choose><c:when test="${empty referer }">
  <a href="javascript:history.back()">Back</a>
</c:when><c:otherwise>
  <form action="${referer }"><input type="submit" value="Try Again" /></form>
</c:otherwise></c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
