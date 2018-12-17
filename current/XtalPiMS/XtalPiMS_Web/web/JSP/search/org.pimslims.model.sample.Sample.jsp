<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display a list of model objects.
This JSP is called by org.pimslims.servlet.Search

Author: Chris Morris
Date: 14 November 2005
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<%@ page import="org.pimslims.model.sample.Sample" %>
<!-- search/org.pimslims.model.sample.Sample.jsp  -->
<c:catch var="error">

<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="sampleMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="beans" scope="request" type="java.util.Collection<org.pimslims.presentation.sample.SampleBean>" />
<jsp:useBean id="sampleCategoryList" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />

<c:if test="${empty param.isInPopup && empty param.isInModalWindow}">
	<c:set var="actions">
		<pimsWidget:linkWithIcon name=""
        		icon="actions/create/sample.gif" 
        		url="${pageContext.request.contextPath}/Create/org.pimslims.model.sample.Sample" 
        		text="New sample"/>
	</c:set>
	<c:set var="breadcrumbs">
		<a href="${pageContext.request.contextPath}/">Home</a> : <a href="${pageContext.request.contextPath}/Search/">Search</a>
	</c:set>
	<pimsWidget:pageTitle icon="sample.png"
		title="Search Samples"
		actions="${actions}"
		breadcrumbs="${breadcrumbs}" />
</c:if>

<div style="margin-right:10em" class="slimline_forms">


<pimsWidget:pageControls>
	<input type="hidden" name="status" value="${fn:escapeXml(param.status)}"/>
	<c:if test="${!empty param.name}">
		<input type="hidden" name="name" value="${fn:escapeXml(param.name)}"/>
	</c:if>
	<c:if test="${!empty param.details}">
		<input type="hidden" name="details" value="${fn:escapeXml(param.details)}"/>
	</c:if>
	<c:if test="${!empty param.protocol}">
		<input type="hidden" name="protocol" value="${param.protocol}"/>
	</c:if>
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

<pimsWidget:quickSearch initialState="open" value="${fn:escapeXml(param['search_all'])}" >
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

<pimsWidget:box title="Advanced Search" initialState="closed">
	<pimsForm:form mode="edit" method="get" action="/Search/org.pimslims.model.sample.Sample">
		
		<pimsForm:formBlock>
			<pimsForm:column1>
				<pimsForm:text name="name" alias="Name" value="${fn:escapeXml(param['name'])}" />
				<pimsForm:text name="details" alias="Details" value="${fn:escapeXml(param['details'])}" />
        	</pimsForm:column1>
        	
			<pimsForm:column2>
				<%-- <pimsForm:select name="<%=Sample.PROP_ASSIGNTO %>" alias="Assigned To">
				    <c:if test="${!empty personAssignedTo}">
					<option value="${personAssignedTo.hook}" selected="selected"> ${personAssignedTo.name}</option>
					</c:if>
					<option value="${NONE}">Any</option>
					<c:forEach items="${ userPersons }" var="entry">
						<pimsForm:option currentValue="${personAssignedTo.hook}" optionValue="${entry.key}" alias="${entry.value}" />
					</c:forEach>
				</pimsForm:select> --%>
				
				<c:choose>
				<c:when test="${empty param.isInPopup && empty param.isInModalWindow}">
					<pimsForm:select alias="Category" name="<%=Sample.PROP_SAMPLECATEGORIES %>" >
				    	<option value="">[any]</option>
			    		<c:forEach items="${sampleCategoryList}" var="category">
			    			<pimsForm:option currentValue="${param['sampleCategories']}" optionValue="${category.name}" alias="${category.name}" />
			    		</c:forEach>
					</pimsForm:select>
				</c:when><c:otherwise>
					<c:if test="${!empty param.sampleCategories}">
  						<input type="hidden" name="sampleCategories" value="${param.sampleCategories}"/>
					</c:if>
				</c:otherwise>
				</c:choose>

			    <pimsForm:formItem name="" alias="">
					<input style="float:right" type="submit" name="SUBMIT" value="Search" onclick="dontWarn()" />
				</pimsForm:formItem>
			</pimsForm:column2>
		</pimsForm:formBlock>
		
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
<c:if test="${!empty param.callbackFunction}">
  <input type="hidden" name="callbackFunction" value="${param.callbackFunction}"/>
</c:if>
<c:if test="${!empty param.pagesize}">
  <input type="hidden" name="pagesize" value="${param.pagesize}"/>
</c:if>
    <c:if test="${!empty param['_metaClass']}">
      <input type="hidden" name="_metaClass" value="${param['_metaClass']}"/>
    </c:if>
		
		
	</pimsForm:form>
</pimsWidget:box>

</div>

<strong>${totalRecords}</strong> Sample(s) recorded in the database<br/>
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
    <jsp:include page="/JSP/list/org.pimslims.model.sample.Sample.jsp"></jsp:include>
</pimsWidget:box>
</c:otherwise></c:choose>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>

<!-- search/org.pimslims.model.sample.Sample.jsp  -->

<jsp:include page="/JSP/core/Footer.jsp" />
