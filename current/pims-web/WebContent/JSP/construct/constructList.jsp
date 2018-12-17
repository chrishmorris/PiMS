<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Author: cm65
Date: 25 Jun 2007
Servlets: Index page

--%>
<jsp:useBean id="ConstructProgressBeans" scope="request" type="java.util.List<org.pimslims.presentation.worklist.ConstructProgressBean>" /> 


<c:catch var="error">

<c:choose>
<c:when test="${empty ConstructProgressBeans}"><p style="text-align:center;margin:4em 0 2em 0;">None</p></c:when>
<c:otherwise><ul>
<c:forEach var="bean" items="${ConstructProgressBeans}" >
  <li><pimsWidget:link bean="${bean}"/></li>
</c:forEach>
</ul></c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<!-- OLD -->
