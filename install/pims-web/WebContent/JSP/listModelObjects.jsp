<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: Marc Savitsky
Date: Mar 2008
Servlets: org.pimslims.servlet.Barcode
--%>

<jsp:useBean id="modelObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Objects found with Barcode ${barcode}' />
    <jsp:param name="mayUpdate" value='$${mayUpdate}' />
</jsp:include>


<%-- page body here --%>

<table class="list" cellpadding="0" width="20%" summary=" with their attributes">
    <tr class="rowHeader">
    <th>Barcode <c:out value="${barcode}" /></th>
	</tr>

<%--Display the content of a table --%>
    <c:forEach items="${modelObjects}" var="modelobject">
        <tr>
             <td><pimsWidget:link bean="${modelobject}" /></td>
    	</tr>
    </c:forEach>
</table>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
