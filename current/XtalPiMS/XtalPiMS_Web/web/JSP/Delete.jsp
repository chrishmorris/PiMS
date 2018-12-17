<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%-- 
Author: Chris Morris
Date: Jan 2009
--%>

<c:catch var="error">
<jsp:useBean id="modelObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Deleting PiMS records' />
    <jsp:param name="mayUpdate" value="${mayUpdate}" />
</jsp:include>

<c:if test="${!empty cantDelete }">
You are not allowed to delete these PiMS records:

<ul>
    <c:forEach items="${cantDelete}" var="modelobject">
        <li>
          <pimsWidget:link bean="${modelobject}" />     
        </li>
    </c:forEach>
</ul>
</c:if>

<c:if test="${!empty modelObjects }">
Click the button to delete these PiMS records:
<pimsForm:form action="/Delete" method ="post" mode="edit" > 

<ul>
    <c:forEach items="${modelObjects}" var="modelobject">
        <li>
          <pimsWidget:link bean="${modelobject}" />
          <input type="hidden" name="hook" value="${modelobject.hook}" />        
        </li>
    </c:forEach>
</ul>
<pimsForm:formItem alias="" name="">
    <div class="savebutton">
    <input type="submit" value="Delete"  onclick="dontWarn()" />
    </div>
</pimsForm:formItem>
</pimsForm:form>
</c:if>

<script type="text/javascript">document.write('<a href=\"#\" onclick=\"history.back();return false;\">Back</a>');</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
