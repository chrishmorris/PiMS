<%--
Create Plate form
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page import="org.pimslims.model.sample.*" %>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Record a new plate experiment" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<c:set var="actions">
    <a href="${pageContext.request.contextPath}/View/${bean.hook}">${bean.name}</a>
</c:set>

<pimsWidget:pageTitle icon="plate.png" title="Spreadsheet Errors" actions="${actions}" />

<pimsWidget:box title="Details of Spreadsheet Errors" initialState="open">

    <pimsForm:formBlock>
    <h4>Error details</h4>
    <c:forEach var="e" items="${spreadsheetErrors}">
         ${e} <br />
    </c:forEach> 
    </pimsForm:formBlock>
    
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
