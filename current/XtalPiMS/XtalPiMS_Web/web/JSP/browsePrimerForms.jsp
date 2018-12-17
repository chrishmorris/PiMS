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
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<fmt:setLocale value='en_UK' />
<%@ page buffer = "32kb" %>



<c:if test="${includeHeader}">
 <jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value='List all Primer forms' />
 </jsp:include>
</c:if>
<!-- browsePrimerForms.jsp -->
<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
var thingsToHide = new Array();
</script>


<c:set var="actions"><a class href='${pageContext.request.contextPath}/NewPrimerForm'>Record new primers</a></c:set>
<pimsWidget:pageTitle title="Primer forms" actions="${actions}" />

<c:set var="maxValue"><%=Integer.MAX_VALUE %></c:set>
<pimsWidget:box initialState="open" title="Page display controls">
<pimsForm:form method="get" action="#" mode="edit">
Set number of elements displayed on one page
<c:forTokens items="10 20 30 50 100 -1" delims=" " var="token" >
	<c:set var="checked" value=""/>
	<c:if test="${token == param.pagerSize}">
		<c:set var="checked">checked="checked"</c:set>
	</c:if>
	<c:choose>
	<c:when test="${token == '-1'}">
		<input type="radio" ${checked} value="${maxValue}" name="pagerSize" />unlimited	
	</c:when>
	<c:otherwise>
		<input type="radio" ${checked} value="${token}" name="pagerSize" />${token}
	</c:otherwise>
	</c:choose>
</c:forTokens>
<input style="width:auto;" type="submit"	name="Submit" value="Update view" />
<table>
<%-- Put a row number column on the page --%>
<tr valign="top">
<td>
	<c:if test="${param.rowNum == 'on'}">
		<c:set var="numberIsOn">checked="checked"</c:set>
	</c:if>
	<input type="checkbox" ${numberIsOn} value="on" name="rowNum" />Show row numbers	
</td>
</tr>
</table>
</pimsForm:form>
</pimsWidget:box>



<pimsWidget:box initialState="open" title="Primer forms">
  <display:table class="list" id="row" name="${requestScope._List_}" 
  							  defaultsort="1" pagesize="${param.pagerSize}">

  <%-- Use this if you want to provide automatic row numeration

// Use this approach to access the row object
   <c:url var="url" value="/JSP/viewTarget?hook=${row.hook}" />
   <a href='${url}'>
    	<c:out value="${row.commonName}"/>
   </a>

   --%>

 <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="" >
      <a href="${pageContext.request.contextPath}/FullPrimerForm/${row.hook}">${row.name}</a>
 </display:column>

 <c:if test="${param.rowNum == 'on'}">
  	 <display:column style="padding:2px 0 0 3px;width:20px;" title="Row num" >${row_rowNum}</display:column>
 </c:if>

   		<display:column sortable="true" headerClass="sortable" title="Designed By" >
					<pims:getter version="${row.version}" hook="${row.designedBy}" attributes="name" delimiter=" " />
   		</display:column>
			<display:column sortable="true" headerClass="sortable" title="Location" >
				<pims:getter version="${row.version}" hook="${row.forwardPrimer.location}" attributes="name" />
   		</display:column>
   		<display:column sortable="true" headerClass="sortable" title="Box" >
				<pims:getter version="${row.version}" hook="${row.forwardPrimer.box}" attributes="name" />
   		</display:column>
 			<display:column sortable="true" headerClass="sortable" title="Position" >  	
 				${row.forwardPrimer.position} 
 				<c:if test="${! empty row.reversePrimer.position}"> 
 				 / ${row.reversePrimer.position}
 				</c:if>
			</display:column>
 			<display:column escapeXml="true"  property="PCRProductLength" sortable="true" headerClass="sortable" title="PCR p.length" />
			<display:column sortable="true" headerClass="sortable" title="Plasmid" >
				 <pims:isHookValid version="${row.version}" hook="${row.plasmid}">
			   	<c:set var="valid" value="valid"/>
			    <c:set var="plasmid">
								<pims:getter version="${row.version}" hook="${row.plasmid}" attributes="name" />
					</c:set>
         	<a href="${pageContext.request.contextPath}/Construct/${row.plasmid}" title="View Plasmid Concerned" >${plasmid}</a>
				</pims:isHookValid>
				<c:if test="${empty valid}"> 
					${row.plasmid}
				</c:if>
			  <c:remove var="valid" />
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
<!-- ToolTips -->
<script type="text/javascript">
Element.observe(window,"load",function(){
  addTooltip("positions","Primers positions","Forward/Reverse primers positions");
});
</script>
<c:set var="includeHeader" value="${true}" scope="session" target="includeHeader"/>
<!-- /browseConstruct.jsp -->
