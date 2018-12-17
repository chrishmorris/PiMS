<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%-- @Author Petr Troshin aka pvt43 --%>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Custom view of a target group
--%>

<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.model.target.*;import org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.presentation.ServletUtil" %>

<c:catch var="error"> 

<%-- Mandatory parameters --%>
<jsp:useBean id="head" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<jsp:scriptlet>
    String style = "even";
    pageContext.setAttribute("style", style);
</jsp:scriptlet>



<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Target Group: ${bean.name}" />
</jsp:include>  
<!-- View_TargetGroup.jsp -->

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a></c:set>
<c:set var="title" value="${bean.name}"/>
<c:set var="icon" value="targetgroup.png"/>
<c:set var="actions">
    
<a href="${pageContext.request.contextPath}/report/TargetGroupReport/${bean.hook} " >Reports</a>
&nbsp;&nbsp;
<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective?targetGroup=${bean.hook} " >Constructs for Targets in group</a>
&nbsp;&nbsp;
<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.ResearchObjective?experimentType=Order&targetGroup=${bean.hook} " >Constructs: Primers not ordered</a>
<pimsWidget:deleteLink bean="${bean}" />
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:details bean="${bean}" initialState="closed" />


<pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="targets" title="Targets" />


<pimsWidget:box title="Upload Target Details" initialState="closed">    
<pimsForm:form mode="edit" action="/update/CreateTargetsForGroup?targetGroup=${bean.hook}" method="post" enctype="multipart/form-data">
  <c:choose><c:when test="${bean.mayUpdate}">
       FASTA file:&nbsp;<input style="" type="file" name="fasta" id="fasta" />
    Coding sequences <input type="Checkbox" checked="checked" name="isProtein" title="Are these sequences ORFs?" />
    <input type="submit" value="Save"  
      onClick="if (''==document.getElementById('fasta').value) {alert('Please choose a file to upload'); return false;}; dontWarn(); return true" />
</c:when><c:otherwise>
    You do not have access rights to add targets to <c:out value="${bean.name}" />
  </c:otherwise></c:choose>
</pimsForm:form>
</pimsWidget:box>


<pimsWidget:multiRoleBox objectHook="${bean.hook}" roleName="subTargetGroups" title="Subgroups" />

<pimsWidget:files bean="${bean}" />

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error />
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
