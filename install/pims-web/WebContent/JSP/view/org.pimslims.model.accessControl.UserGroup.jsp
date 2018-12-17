<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<c:catch var="error">

<%-- Mandatory parameters --%>
<jsp:useBean id="ObjName" scope="request" type="java.lang.String" />
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${ObjName}: ${bean.name}" />
</jsp:include>  

<!-- /view/org.pimslims.model.accessControl.UserGroup.jsp -->
<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/${bean.className}">${bean.classDisplayName}s</a>
</c:set>
<%-- TODO icon --%>        
<c:set var="actions">
    <a href="${pageContext.request.contextPath}/Create/<%=org.pimslims.model.accessControl.User.class.getName() %>?userGroups=${bean.hook}">
    New User</a>
    <pimsWidget:deleteLink bean="${bean}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${bean.name}" actions="${actions}" icon="${icon}" />

<c:set var="fulltitle">
    <span class="linkwithicon" style="float:right; padding:0; padding-right: 1em; white-space: nowrap;">
      <a onclick='return warnChange()' href="${pageContext.request.contextPath}/Create/${bean.className}">Record New</a>
      <a onclick='return warnChange()' href="${pageContext.request.contextPath}/Search/${bean.className}">Search</a>
    </span>
	<c:out value="${bean.classDisplayName}" />:
	<pimsWidget:link bean="${bean}" />
</c:set>


<pimsWidget:details bean="${bean}" initialState="open" />
<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
</script>


<c:forEach var="entry" items="${bean.metaClass.metaRoles}">
  <c:if test="${entry.value.high!=1}">
    <pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="${entry.key}" title="${utils:deCamelCase(entry.key)}" />    
  </c:if>
</c:forEach>



</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>

<!-- /ViewUserGroup.jsp -->

<jsp:include page="/JSP/core/Footer.jsp" />
