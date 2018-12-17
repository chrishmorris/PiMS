<%--
browseExperimentAndParameters.JSP
@param _List_ - the list of the specific model objects provided by ReportExperimentParameters servlet 
@param includeHeader - set by ListEntryClones to false when the page firstly
displayed the header and footer come from a servlet. But then includeHeader
set to true at the end of this jsp, and since next request come to this jsp
and header and footer must be generated from here.

@author Bill Lin
@date August 2007
--%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<fmt:setLocale value='en_UK' />
<%@ page buffer = "64kb" %>



<c:if test="${includeHeader}">
	<jsp:include page="/JSP/core/Header.jsp">
    	<jsp:param name="HeaderName" value='List ${headerName}' />
	</jsp:include>
</c:if>
    <pimsWidget:pageTitle title="Compare parameters of experiments" icon="experiment.png" />
<pimsWidget:box title="Experiments" initialState="open">
  <display:table class="list" id="row" name="${requestScope._List_}" 
  							  defaultsort="1" pagesize="${param.resultSize}">
  							   
 <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="" >
    <pimsWidget:link bean="${row}" />
 </display:column>


   	<display:column escapeXml="true"  property="targetName" title="Target" sortable="true" headerClass="sortable" />
 	
	<c:forEach var="entry" items="${row.parameters}" >
	    <c:set var="columnHeader">${entry.key}</c:set>
        <%-- special cases --%>
	    <c:if test="${'__SCORE' eq columnHeader}"><c:set var="columnHeader">Score</c:set></c:if>

	 	<display:column title="${columnHeader}" sortable="true" headerClass="sortable" >
 			<c:out value="${entry.value}"/>
 		</display:column>
	</c:forEach>

 			
	<display:setProperty name="paging.banner.group_size" value="15" />
	<display:setProperty name="export.decorated" value="false" />
	
	<display:setProperty name="export.excel.include_header" value="true" />
	<display:setProperty name="export.csv.include_header" value="true" />
	<display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
	<display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
	<display:setProperty name="paging.banner.item_name" value="Record" />
  <display:setProperty name="paging.banner.items_name" value="Records" />
  </display:table>
</pimsWidget:box>  

<c:if test="${includeHeader}">
	<jsp:include page="/JSP/core/Footer.jsp" />
</c:if>

<!-- BrowseExperimentAndParameters.jsp -->
