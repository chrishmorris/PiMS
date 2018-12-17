<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Custom view of a bookmark - adds an action to visit it
--%>

<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.model.target.*;import org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.presentation.ServletUtil" %>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>

<c:catch var="error">

<%-- Mandatory parameters --%>
<jsp:useBean id="ObjName" scope="request" type="java.lang.String" />
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="${ObjName}: ${bean.name}" />
</jsp:include>  

<!-- view/...Bookmark.jsp -->
<c:set var="breadcrumbs">
    <a title="Search ${bean.classDisplayName}s" href="${pageContext.request.contextPath}/Search/${bean.className}">${bean.classDisplayName}s</a>
</c:set>
<%-- TODO icon --%>        
<c:set var="actions">
    <a href="${pageContext.request.contextPath}${bean.values['url']}" tabindex="1" title="Visit bookmarked page">Go</a>&nbsp;
    <pimsWidget:deleteLink bean="${bean}" />
</c:set>
<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${bean.name}" actions="${actions}" icon="${icon}" />

<pimsWidget:details bean="${bean}" initialState="open" />

<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
</script>


<c:forEach var="entry" items="${bean.metaClass.metaRoles}">
  <c:if test="${entry.value.high!=1 && 'notes' ne entry.key && 'annotations' ne entry.key && 'attachments' ne entry.key && 'externalDbLinks' ne entry.key && 'citations' ne entry.key  }">  
    <c:set var="mayAdd" value="${bean.mayUpdate &&!_MHTML && (null==entry.value.otherRole || entry.value.otherRole.changeable)}" />
    <pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="${entry.key}" title="${utils:deCamelCase(entry.key)}" 
      mayAdd="${mayAdd}"
    />    
  </c:if>
</c:forEach>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>

<c:set var="isLabBookEntry">
    <%= org.pimslims.model.core.LabBookEntry.class.isAssignableFrom(((org.pimslims.presentation.ModelObjectBean)request.getAttribute("bean")).getMetaClass().getJavaClass()) %>
</c:set>
<c:if test="${isLabBookEntry}">
<pimsWidget:files bean="${bean}"  />

<pimsWidget:notes bean="${bean}"  />
<%-- TODO <pimsWidget:externalDbLinks bean="${bean}" /> <pimsWidget:citations bean="${bean}" /> 
--%>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
