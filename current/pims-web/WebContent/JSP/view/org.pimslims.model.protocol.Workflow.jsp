<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Custom view of workflow
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

<!-- view/org.pimslims.model.protocol.Workflow.jsp -->
<c:set var="breadcrumbs">
    <a title="Search ${bean.classDisplayName}s" href="${pageContext.request.contextPath}/Search/${bean.className}">${bean.classDisplayName}s</a>
</c:set>
<%-- TODO icon --%>        
<c:set var="actions">
    <pimsWidget:diagramLink hook="${bean.hook}"/>
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

<pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="projects" title="Projects" 
      mayAdd="${bean.mayUpdate}"
/>  
    
<pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="protocols" title="Protocols" 
      mayAdd="${bean.mayUpdate}"
/>  




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
<!-- /view/org.pimslims.model.protocol.Workflow.jsp -->

<jsp:include page="/JSP/core/Footer.jsp" />
