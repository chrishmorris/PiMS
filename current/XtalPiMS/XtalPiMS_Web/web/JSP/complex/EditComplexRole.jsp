<%--
Manage an association.
This JSP is called by org.pimslims.servlet.EditComplexRole
TODO see comments in EditComplexRole
Author: Chris Morris
Date: 30 November 2005
--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<c:catch var="error">

<jsp:useBean id="results" scope="request" type="java.util.Collection" />
<jsp:useBean id="addControl" scope="request" type="java.util.Map" />
 
<jsp:useBean id="bean" scope="request" type="org.pimslims.presentation.ModelObjectBean" />

<%-- Whether or not draw a header and <table> tag  --%>
<c:set var="head" value="${true}" scope="request"/>

<h1><c:out value="Add Targets to Complex: ${bean.name}" /></h1>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<c:catch var="error">

<h2 name="search">Search for targets to Add</h2>
    <jsp:include page="/JSP/SearchForm.jsp" />

<br />
    <c:set var="control" value="${addControl}" scope="request" />
    
<c:choose>
<c:when test="${noSearch}" >
    <%-- show nothing --%>
</c:when>

<c:when test="${empty results}" >
    <hr /><h2>No Targets found</h2>
</c:when>

<c:otherwise>
  <pimsWidget:box title="Targets found" initialState="open">
  <form class="grid" action="${pageContext.request.contextPath}/update/EditComplexRole/${bean.hook}/researchObjectiveElements" method="post">
    <%-- TODO CSRF token --%>
  
  <c:if test="${empty pageing || true==pageing}">
	<jsp:include page="/JSP/list/ListBeansPaging.jsp"></jsp:include>
</c:if>

	<table ID="${tabId}" cellpadding="0" width="100%" summary=" with their attributes" class="list" >
    <tr>
    	<%-- Make a header of a table --%>
		<th >Add?</th>
		<th>Name</th>
		<th>Protein</th>
		<th>Why chosen</th>
	</tr>

	<%--Display the content of a table --%>
	
	<c:forEach items="${results}" var="mObj"	varStatus="status2">
	
        <c:set var="disabledRow" value=""/>
        <c:if test="${fn:contains(control[mObj.hook],'disabled')}">
            <c:set var="disabledRow"> class="disabledrow" title="Can't add this"</c:set>
         </c:if>
        <tr ${disabledRow}>	

			<%--Write control if some provided --%>
			<td style="padding:2px 0 0 3px;text-align:center;width:20px;">${control[mObj.hook]}</td>
			
			<td><c:out value="${mObj.name}" /></td>
			<td><c:out value="${mObj.protein_name}" /></td>
			<td><c:out value="${mObj.whyChosen}" /></td>
		
        <%-- TODO PIMS-3483 show truncation --%>
  		</tr>
	</c:forEach>
	    <tr>
	    	<td colspan="5"><input type="submit" value="Add selected items" /></td>
	    </tr>
	</table>
	
  </form>
  </pimsWidget:box>
</c:otherwise></c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include flush="true" page="/JSP/core/Footer.jsp" />
