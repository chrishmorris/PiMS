<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Browse Construct JSP
@param _List_ - the list of the specific model objects provided by ListEntryClones servlet 
@param pagerSize set by 20 in ListEntryClones servlet
@param rowNum - set by ListEntryClones to false when the page firstly
@param includeHeader - set by ListEntryClones to false when the page firstly
displayed the header and footer come from a servlet. But then includeHeader
set to true at the end of this jsp, and since next request come to this jsp
and header and footer must be generated from here.

@author Petr Troshin
@date December 2006
--%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<fmt:setLocale value='en_UK' />
<%@ page buffer = "32kb" %>

browseConstructs.jsp obsolete

<c:if test="${includeHeader}">
 <jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value='List all ${headerName}' />
 </jsp:include>

<!-- OLD -->
</c:if>

<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
var thingsToHide = new Array();
</script>

<c:set var="maxIntValue"><%=Integer.MAX_VALUE %></c:set>

<c:set var="actions">
<c:set var="rec" value="Entry Clone"/>
<c:if test="${expression}" >
<c:set var="req" value="?type=Expression"/>
<c:set var="rec" value="Expression construct"/>
</c:if>
<a href='${pageContext.request.contextPath}/NewConstruct${req}'>Create a new ${rec}</a>
</c:set>
<pimsWidget:pageTitle title="List all ${headerName}" actions="${actions}" />

<pimsWidget:box title="Page display controls" initialState="open">
<form method="get" style="width: auto; background-color: white;" >
<c:if test="${expression}"> 
	<input type="hidden" name="type" value="Expression" />
</c:if>
Set number of elements displayed on one page
<c:forTokens items="10 20 30 50 100 -1" delims=" " var="token" >
	<c:set var="checked" value=""/>

	<c:if test="${token == param.pagerSize}">
		<c:set var="checked" value="checked"/>
	</c:if>

	<c:choose>
	<c:when test="${token == '-1'}">
		<input type="radio" ${checked} value="${maxIntValue}" name="pagerSize" />unlimited	
	</c:when>
	<c:otherwise>
		<input type="radio" ${checked} value="${token}" name="pagerSize" />${token}
	</c:otherwise>
	</c:choose>
</c:forTokens>
<input style="width:auto;" type="submit"	name="Submit" value="Update view" />
<table>
<tbody>

<%-- Put a row number column on the page --%>
<tr valign="top">
<td>
	<c:if test="${param.rowNum == 'on'}">
		<c:set var="numberIsOn" value="checked"/>
	</c:if>
	<input type="checkbox" ${numberIsOn} value="on" name="rowNum" />Show row numbers	
</td>
</tr>
</tbody>
</table>
</form>
</pimsWidget:box>

<pimsWidget:box title="Constructs" initialState="fixed">
  <display:table class="list" id="row" name="${requestScope._List_}" 
  							  defaultsort="1" pagesize="${param.pagerSize}">

  
   
 <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="" >
     <a href="${pageContext.request.contextPath}/Construct/${row.hook}">${row.name}</a>
 </display:column>

 <c:if test="${param.rowNum == 'on'}">
  	 <display:column style="padding:2px 0 0 3px;width:20px;" title="Row num" >${row_rowNum}</display:column>
 </c:if>

   		<display:column   title="Designed By" escapeXml="true">
					<pims:getter version="${row.version}" hook="${row.designedBy}" attributes="name" delimiter=" " />
   		</display:column>
   		<display:column   title="Location" escapeXml="true">
				<pims:getter version="${row.version}" hook="${row.location1}" attributes="name" />
   		</display:column>
   		<display:column   title="Box" escapeXml="true">
				<pims:getter version="${row.version}" hook="${row.box1}" attributes="name" />
   		</display:column>
   		<display:column   title="Flanking site" escapeXml="true" >
				${row.rsite} 
   		</display:column>
   		<display:column escapeXml="true"  property="position1"   title="Position" />
		<display:column media="html" style="padding:2px 0 0 3px;width:20px;" title="VNTI map" >
			<c:set var="file" value="${row.VNTIMap}"/>
				<c:choose>
					<c:when test="${! empty file}">
		        <a href="${pageContext.request.contextPath}/ViewFile/${file.hook}/${file.name}" title="view file" >Map</a>
					</c:when>
						<c:otherwise></c:otherwise>
				</c:choose>
			</display:column>
 			<display:column media="html" style="padding:2px 0 0 3px;width:20px;" title="Sequence" >
 			<c:set var="file" value="${row.sequence}"/>
				<c:choose>
					<c:when test="${! empty file}">
		        <a href="${pageContext.request.contextPath}/ViewFile/${file.hook}/${file.name}" title="view file" >Sequence</a>
					</c:when>
						<c:otherwise></c:otherwise>
				</c:choose>
 			</display:column>
 			
	<display:setProperty name="paging.banner.group_size" value="15" />
	<display:setProperty name="export.decorated" value="true" />
	
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


	<!--
		This script block closes all the boxes to keep the page short on load
	-->
	<script type="text/javascript">
	if(window.toggleView && window.thingsToHide) {
		numThings=thingsToHide.length;
		for(i=0;i<numThings;i++) {
			toggleView(thingsToHide[i],path);
		}
	}
	</script>


<c:set var="includeHeader" value="${true}" scope="session" target="includeHeader"/>
<!-- /browseConstruct.jsp -->
