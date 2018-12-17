<%@tag  import="org.pimslims.util.File, java.util.*" %>
<%@attribute name="initialState" required="false" type="java.lang.String" %>
<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectBean" %> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm"   tagdir="/WEB-INF/tags/pimsForm" %>

<!-- files.tag -->
<c:set var="boxname"><%= org.pimslims.servlet.ListFiles.IMAGES %></c:set>
<c:choose><c:when test="${_anchor eq '<%= org.pimslims.servlet.ListFiles.IMAGES %> '}">
    <c:set var="initialState" value="open"/>
</c:when><c:otherwise>
    <c:set var="initialState" value="closed"/>
</c:otherwise></c:choose>

<pimsWidget:box title="Images"  initialState="${initialState}" id="<%= org.pimslims.servlet.ListFiles.IMAGES %>" 
    src="${pageContext.request.contextPath}/read/DoListFiles/${bean.hook}?type=image"
>
</pimsWidget:box> 

<c:set var="boxname"><%= org.pimslims.servlet.ListFiles.IMAGES %></c:set>
<c:choose><c:when test="${_anchor eq '<%= org.pimslims.servlet.ListFiles.IMAGES %> '}">
    <c:set var="initialState" value="open"/>
</c:when><c:otherwise>
    <c:set var="initialState" value="closed"/>
</c:otherwise></c:choose>

<pimsWidget:box title="Attachments"  initialState="${initialState}" id="<%= org.pimslims.servlet.ListFiles.ATTACHMENTS %>"
    src="${pageContext.request.contextPath}/read/DoListFiles/${bean.hook}"
>
</pimsWidget:box> 
<!-- /files.tag -->