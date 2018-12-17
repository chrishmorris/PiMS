<%--
/**
 * JSP to display a summary of all the target groups
 *
 * @author Marc Savitsky
 */
--%>

<%@ page contentType="text/html; charset=utf-8" language="java"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<!-- TargetGroups.jsp -->
<jsp:useBean id="targetGroups" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" /> 

<c:forEach items="${targetGroups}" var="group">
<c:set var="groupLink"><pimsWidget:link bean="${group}" /></c:set>
<pimsWidget:box id="group" title="Target Group" extraHeader="${groupLink}" initialState="closed" 
    src="${pageContext.request.contextPath}/read/ListRole/${group.hook}/targets" 
/>
</c:forEach>



