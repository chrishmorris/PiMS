
<%--
This JSP is called by org.pimslims.servlet.QuickSearch

Note that there has been some cut and paste with SearchForm.jsp.
If you need to  change this, you probably need to change that. --Chris.
TODO call SearchForm.jsp from here.

Author: Peter Troshin
Date: August 2007
--%>

<%@page import="org.pimslims.model.core.LabBookEntry"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@page import="org.pimslims.model.sample.Sample"%>

<c:catch var="error" >
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="displayName" scope="request" type="java.lang.String" />
<%-- The caller must sent the header --%>
<!-- /JSP/search/org.pimslims.model.target.ResearchObjective.jsp --> 

<c:if test="${empty param.isInPopup && empty param.isInModalWindow}">
	<c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}">Home</a> :
        <a href="${pageContext.request.contextPath}/Search/">Search</a>	
	</c:set>
	<c:set var="icon" value="blank.png" />
	<c:choose>
        <c:when test="${displayName eq 'Target'}">
            <c:set var="icon" value="target.png" />
        </c:when>
        <c:when test="${displayName eq 'Project'}">
            <c:set var="icon" value="construct.png" />
        </c:when>
        <c:when test="${displayName eq 'Target group'}">
            <c:set var="icon" value="targetgroup.png" />
        </c:when>
	    <c:when test="${displayName eq 'Protocol'}">
	        <c:set var="icon" value="protocol.png" />
	    </c:when>
	    <c:when test="${displayName eq 'Sample'}">
	        <c:set var="icon" value="sample.png" />
	    </c:when>
        <c:when test="${displayName eq 'Person'}">
            <c:set var="icon" value="person.png" />
        </c:when>
        <c:when test="${displayName eq 'Group'}">
            <c:set var="icon" value="peoplegroup.png" />
        </c:when>
        <c:when test="${displayName eq 'Organisation'}">
            <c:set var="icon" value="organisation.png" />
        </c:when>
	    <c:when test="${displayName eq 'User'}">
	        <c:set var="icon" value="user.png" />
	    </c:when>
	    <c:when test="${displayName eq 'Molecule'}">
	        <c:set var="icon" value="molecule.png" />
	    </c:when>
	</c:choose>

	<c:set var="title" value="Search ${displayName}s"/>
	<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}" />
</c:if>

<c:set var="ACTION" value="<%= org.pimslims.presentation.AttributeToHTML.ACTION %>" />
<c:set var="NONE" value="[NONE]" />


<%-- Generate control box --%>
<pimsWidget:pageControls>
	<!-- This will set values if the request is in popup window-->
	<c:if test="${!empty param.isInPopup}">
	  <input type="hidden" name="isInPopup" value="yes"/>
	</c:if>
	<c:if test="${!empty param.isInModalWindow}">
	  <input type="hidden" name="isInModalWindow" value="yes"/>
	</c:if>
	<c:if test="${!empty param.hook}">
	  <input type="hidden" name="hook" value="${param.hook}"/>
	</c:if>
	<c:if test="${!empty param.barcodeSearch}">
	  <input type="hidden" name="barcodeSearch" value="${param.barcodeSearch}"/>
	</c:if>
	<c:if test="${!empty param.sampleCategories}">
	  <input type="hidden" name="sampleCategories" value="${param.sampleCategories}"/>
	</c:if>
	<c:if test="${!empty param.experimentGroup}">
	  <input type="hidden" name="experimentGroup" value="${param.experimentGroup}"/>
	</c:if>
	<c:if test="${!empty param.experimentType}">
	  <input type="hidden" name="experimentType" value="${param.experimentType}"/>
	</c:if>
	<c:if test="${!empty param.targetGroup}">
	  <input type="hidden" name="targetGroup" value="${param.targetGroup}"/>
	</c:if>
	<c:if test="${!empty param.callbackFunction}">
	  <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
	</c:if>
    <c:if test="${!empty param.search_all}">
      <input type="hidden" name="search_all" value="${fn:escapeXml(param.search_all)}"/>
    </c:if>
    <c:if test="${!empty param['_metaClass']}">
      <input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
    </c:if>
	<c:forEach items="${searchAttributes}" var="attribute">
	  <c:if test="${!searchMetaClass.attributes[attribute].hidden}">
	      <input type="hidden" name="${attribute}" value="<c:out value='${criteria[attribute]}' />" />
	  </c:if>
	</c:forEach>
</pimsWidget:pageControls>

<pimsWidget:quickSearch  value="${fn:escapeXml(param['search_all'])}" initialState="open" ><%--
    Pass on ACTION parameter for CustomCreate. Do we really need that in this page?
    --%><input name="${AttributeToHTML['ACTION']}" value="${param.ACTION}" type="hidden" />
    
    <%-- for pop-up role handling --%>
<c:if test="${!empty param.isInPopup}">
  <input type="hidden" name="isInPopup" value="yes"/>
</c:if>
<c:if test="${!empty param.isInModalWindow}">
  <input type="hidden" name="isInModalWindow" value="yes"/>
</c:if>
<c:if test="${!empty param.hook}">
  <input type="hidden" name="hook" value="${param.hook}"/>
</c:if>
<c:if test="${!empty param.barcodeSearch}">
  <input type="hidden" name="barcodeSearch" value="${param.barcodeSearch}"/>
</c:if>
<c:if test="${!empty param.sampleCategories}">
  <input type="hidden" name="sampleCategories" value="${param.sampleCategories}"/>
</c:if>
<c:if test="${!empty param.experimentGroup}">
  <input type="hidden" name="experimentGroup" value="${param.experimentGroup}"/>
</c:if>
<c:if test="${!empty param.experimentType}">
  <input type="hidden" name="experimentType" value="${param.experimentType}"/>
</c:if>
<c:if test="${!empty param.targetGroup}">
  <input type="hidden" name="targetGroup" value="${param.targetGroup}"/>
</c:if>
<c:if test="${!empty param.callbackFunction}">
  <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
</c:if>
<c:if test="${!empty param.pagesize}">
  <input type="hidden" name="pagesize" value="${param.pagesize}"/>
</c:if>
    <c:if test="${!empty param['_metaClass']}">
      <input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
    </c:if>
    
</pimsWidget:quickSearch>

<c:if test="${!empty param.isInModalWindow}">
  <%-- override the existing value, wherever it came from --%>
  <c:set var="searchBox" value="closed" />
</c:if>
 

<pimsWidget:box title="Search Criteria"  initialState="${searchBox}"   >
<form method="get" action="${requestScope['javax.servlet.forward.request_uri']}" class="collapsibleBox_search" ><%--
    Pass on ACTION parameter for CustomCreate. Do we really need that in this page?
    --%><input name="${AttributeToHTML['ACTION']}" value="${param.ACTION}" type="hidden" />

<table style="border-collapse:collapse">
<c:forEach items="${searchAttributes}" var="attribute">
	  <!-- no null attributes -->
	  <c:if test="${!empty searchMetaClass.attributes[attribute]}">
      	<c:if test="${!searchMetaClass.attributes[attribute].hidden}">
      	<tr>
        	<td>
                <label class="label" for="${searchMetaClass.attributes[attribute].name}">
                    ${utils:deCamelCase(searchMetaClass.attributes[attribute].alias)}
                </label>
        	</td>
        	<td>
          	<c:choose>
           	 	<c:when test="${searchMetaClass.attributes[attribute].name == 'status'}">
              		<pims:input attribute="${searchMetaClass.attributes[attribute]}"
              		value="<c:out value='${criteria[attribute]}' />" />
            	</c:when>
            	<c:otherwise>
              		<input name="${attribute}" value="<c:out value='${criteria[attribute]}' />" />
            	</c:otherwise>
          	</c:choose>
        	</td>
      	</tr>
      	</c:if>
      </c:if>
</c:forEach>
   

	<c:if test="${totalRecords > 0}">
		<%-- Make button if there is something to send --%>
		<tr><td colspan="2"><input type="submit" name="SUBMIT" value="Search" onClick="dontWarn()" /></td></tr>
	</c:if>
</table>

<%-- for pop-up role handling --%>
<c:if test="${!empty param.isInPopup}">
  <input type="hidden" name="isInPopup" value="yes"/>
</c:if>
<c:if test="${!empty param.isInModalWindow}">
  <input type="hidden" name="isInModalWindow" value="yes"/>
</c:if>
<c:if test="${!empty param.hook}">
  <input type="hidden" name="hook" value="${param.hook}"/>
</c:if>
<c:if test="${!empty param.barcodeSearch}">
  <input type="hidden" name="barcodeSearch" value="${param.barcodeSearch}"/>
</c:if>
<c:if test="${!empty param.sampleCategories}">
  <input type="hidden" name="sampleCategories" value="${param.sampleCategories}"/>
</c:if>
<c:if test="${!empty param.experimentGroup}">
  <input type="hidden" name="experimentGroup" value="${param.experimentGroup}"/>
</c:if>
<c:if test="${!empty param.experimentType}">
  <input type="hidden" name="experimentType" value="${param.experimentType}"/>
</c:if>
<c:if test="${!empty param.targetGroup}">
  <input type="hidden" name="targetGroup" value="${param.targetGroup}"/>
</c:if>
<c:if test="${!empty param.callbackFunction}">
  <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
</c:if>
<c:if test="${!empty param.pagesize}">
  <input type="hidden" name="pagesize" value="${param.pagesize}"/>
</c:if>
    <c:if test="${!empty param['_metaClass']}">
      <input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
    </c:if>

</form>
</pimsWidget:box>

<!-- end of Search form -->

<strong>${totalRecords}</strong> ${searchMetaClass.alias}(s)  recorded in the database<br/>
<strong>${resultSize}</strong> match search criteria

<hr />

  
<c:choose><c:when test="${noSearch}" >
    <%-- show nothing --%>
</c:when><c:when test="${empty beans}" >
    <h2>No ${displayName}s found</h2>
</c:when><c:otherwise>
    <script type="text/javascript">	 focusElement =null </script>
    <%-- show the results --%>
    <jsp:scriptlet>request.setAttribute("listMetaClass", metaClass);</jsp:scriptlet>

    <c:choose><c:when test="${empty param.isInPopup}">
      <c:set var="export" >true</c:set>
    </c:when><c:otherwise>
	  <c:set var="export" >false</c:set>
	</c:otherwise></c:choose>

<pimsWidget:box title="${displayName}s found"  initialState="open"   >
   <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/list/${metaClass.metaClassName}.jsp" />
   </jsp:include>
</pimsWidget:box>
</c:otherwise></c:choose>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

