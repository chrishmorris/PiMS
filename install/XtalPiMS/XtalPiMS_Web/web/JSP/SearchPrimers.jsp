<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
Author: pvt43
Date: 8 May 2008
Servlets: This is to be used by custom searches.
Please note that due to the number of parameters stripped it cannot be used to display generic search results
--%> 

<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<!-- jsp:useBean id="attributes" scope="request" type="java.util.Collection" / -->
<jsp:useBean id="displayName" scope="request" type="java.lang.String" />
<%-- The caller must sent the header --%>
<c:set var="MAX" value="<%=Integer.MAX_VALUE %>" />

<c:set var="ACTION" value="<%= org.pimslims.presentation.AttributeToHTML.ACTION %>" />
<%-- Generate control box --%>

<pimsWidget:box title="Page-display controls"  initialState="closed" >
<form method="get" action="${requestScope['javax.servlet.forward.request_uri']}"  >
<p>
Set number of elements displayed on one page
<c:forTokens items="10 20 30 50 100 -1" delims=" " var="token" >
	<c:set var="checked" value=""/>

	<c:if test="${token == pagesize}">
		<c:set var="checked" value="checked"/>
	</c:if>

	<c:choose>
	<c:when test="${token == '-1'}">
		<input type="radio" ${checked} value="${MAX}" name="pagesize" />unlimited
	</c:when>
	<c:otherwise>
		<input type="radio" ${checked} value="${token}" name="pagesize" />${token}
	</c:otherwise>
	</c:choose>
</c:forTokens>
&nbsp;&nbsp;
<input style="width:auto;" type="submit"	name="Submit" value="Update view" />

            <input name="search_all" value="${fn:escapeXml(criteria['search_all'])}" />

<c:forEach items="${searchAttributes}" var="attribute">
  <c:if test="${!searchMetaClass.attributes[attribute].hidden}">
      <input type="hidden" name="${attribute}" value="<c:out value='${criteria[attribute]}' />" />
  </c:if>
</c:forEach>
</p>
</form>
</pimsWidget:box>

<!-- Search all field and start of a search criteria table -->
<pimsWidget:box title="Search Criteria"  initialState="open"   >
<form method="get" action="${requestScope['javax.servlet.forward.request_uri']}"  onSubmit="return validSearch(this);">
<%--Pass on ACTION parameter for CustomCreate--%>

<input name="${ACTION}" value="${param.ACTION}" type="hidden" />

<table style="border-collapse:collapse">

<%-- Generate search in all fields field--%>
<c:if test="${searchAll}" >
   <c:import url="/JSP/customSearch/SearchAllFields.jsp"/>
</c:if>

<tr><td colspan="2"><br /></td></tr>


<!-- Include custom parameters form here -->
<c:if test="${!empty _form}">
	<c:import url="/JSP/customSearch/${_form}"/>
</c:if>
  
	
	<tr><td colspan="2"><input type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" /></td></tr>

</table>

</form>
</pimsWidget:box>

<!-- end of Search form -->

<%-- TODO Fix this later
<strong>${totalRecords}</strong> ${displayname}(s)  recorded in the database<br/>
--%>
<strong>${resultSize}</strong> ${displayName}(s) match search criteria

<hr />


<c:if test="${!empty results}">

	<c:if test="${!empty _view}">

		<!-- Include results rendering table here -->
		<c:import url="/JSP/customSearch/${_view}"/>

	</c:if>

</c:if>

<c:import url="/JSP/customSearch/SearchJSCheck.jsp" />

<!-- OLD -->
