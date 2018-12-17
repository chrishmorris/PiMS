<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.sample.*"  %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Author: cm65
Date: 25 Jun 2007
Servlets: Index page

--%>
<jsp:useBean id="sampleBeans" scope="request" type="java.util.Collection<SampleBean>" /> 

<c:catch var="error">

<c:choose>
<c:when test="${empty sampleBeans}"><p style="text-align:center;margin:4em 0 2em 0;">None</p></c:when>
<c:otherwise><ul>
<c:forEach var="sample" items="${sampleBeans}" >
  <li><pimsWidget:link bean="${sample}"/></li>
<%--
  <li><a title="${sample.noProgressDays} days since ${sample.lastExpType}" href="${pageContext.request.contextPath}/View/${sample.sampleHook}">
      <c:out value="${sample.sampleName}" />
  </a></li>
--%>
</c:forEach>
</ul></c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<!-- OLD -->
