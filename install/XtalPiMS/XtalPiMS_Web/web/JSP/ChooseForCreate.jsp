<%--
Manage an association for a new object.
This JSP is called by org.pimslims.servlet.ChooseForCreate
That shows a progress bar, so the header is not written here

Author: Chris Morris
Date: December 2005
--%>
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget"   tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" import="java.util.*" %>
<!-- ChooseForCreate.jsp -->
<%-- jsp:useBean id="results" scope="request" type="java.util.Collection" /--%>
<%-- if a result is null the collection may not be set --%>
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="actions" scope="request" type="java.util.Map" />
<jsp:useBean id="metaRole" scope="request" type="org.pimslims.metamodel.MetaRole" />

<c:if test="${empty displayName}">
    <c:set var="displayName" value="${metaClass.alias}" />
</c:if>

<h1>Choose ${otherName} for new ${displayName}</h1>

<c:if test="${! empty createNewLink}">
  <a href="<%= request.getContextPath() %>/Create/${metaClass.metaClassName}:${metaRole.name}?${parametersString}">
  Create a new ${metaRole.alias} for the new ${displayName}</a><br/>
</c:if>


<hr />
<!-- begin search form-->
<h2>Search for ${otherName}s to Use</h2>
<%-- <a name="search" /> --%>
 <jsp:include page="/JSP/SearchForm.jsp" /> 
<!-- end search form-->

<c:set var="controlHeader" value="Use?" scope="request" />
<br />
<c:choose><c:when test="${empty beans}" >
    <hr /><h2>No ${otherName}s found</h2>
</c:when><c:otherwise>
  <pimsWidget:box title="${metaRole.otherMetaClass.alias}s found" initialState="open" >
	 <script type="text/javascript">	 focusElement =null </script>
    <form name="ChooseForCreate" action="#" method="post">
    <%-- TODO CSRF token --%>
    <input type="hidden" name="METACLASSNAME" value="${metaClass.metaClassName}"/>
    <input type="hidden" name="METAROLENAME" value="${metaRole.roleName}"/>
    <input type="hidden" name="parametersString" value="${parametersString}"/>
   <jsp:include page="/read/FindJsp" >
           <jsp:param name="_JSP" value="/list/${metaRole.otherMetaClass.metaClassName}.jsp" />
   </jsp:include>
    <input type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn()" /> 
    </form>    
  </pimsWidget:box>
  <%--<input type="submit" value="Next &gt;&gt;&gt;" onclick="dontWarn()" /> --%>
</c:otherwise></c:choose>


<jsp:include page="/JSP/core/Footer.jsp" />
