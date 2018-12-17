<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Browse complexes custom jsp
@param _Target_List_ - the list of the targets  
@param pagerSize  

@author Petr Troshin
@date October 2005
--%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<%@ page buffer = "64kb" %>

<script type="text/javascript">
function toggleAllCheckboxes(checkAllBoxElement){//<!--
	var isChecked = checkAllBoxElement.checked;
	var parentTable = Element.extend(checkAllBoxElement).up("table");
	<%--alert (parentTable);--%>
	var boxes=parentTable.getElementsByClassName("behaviour_selectAll");

	for(i=0;i<boxes.length; i++){
		boxes[i].checked=isChecked;
	}
	return true;
}
//--></script>

<!-- browseComplexes.jsp -->


 <jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value='Complexes' /> 
 </jsp:include>


<!-- browseComplexes.jsp -->

<c:set var="actions">
    <pimsWidget:linkWithIcon isNext="${0 eq resultSize}"
        icon="actions/create/complex.gif" 
        url="${pageContext.request.contextPath}/NewComplex" 
        text="New Complex"/>
</c:set>

<c:set var="breadcrumbs">
        <a href="${pageContext.request.contextPath}">Home</a> :
</c:set>

<c:set var="title" value="Complexes"/>
<pimsWidget:pageTitle title="${title}" icon="complex.png" breadcrumbs="${breadcrumbs}" actions="${actions }" />

<%-- Generate control box TODO use pageControls.tag --%>
<c:set var="maxIntValue"><%=Integer.MAX_VALUE %></c:set>
<pimsWidget:box title="Page-display controls">
<form method="get" style="width: auto; background-color: white;" >
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
<br />
<input style="width:auto;" type="submit"	name="Submit" value="Update view" />
</form>
</pimsWidget:box>


<%
   String itNum = (String)request.getParameter("pagerSize");
   if(itNum == null || itNum.trim().length() ==0) {
	   	pageContext.setAttribute("itemNum", session.getAttribute("pagerSize"));
   } else {
	   	pageContext.setAttribute("itemNum", itNum);
   }
//

%>

<pimsWidget:box title="Results" initialState="open">
  <display:table class="list" id="mytable" name="${sessionScope._Complexes_List_}" 
  							  pagesize="${param.pagerSize}"
  							 sort="page" defaultsort="3"
  							 requestURI="/BrowseComplex"
  							 partialList="true" size="${resultSize}"
  						 	 decorator="org.pimslims.servlet.complex.ComplexDecorator">  

  	<display:column escapeXml="false" media="html" title="" >
	     <a href="${pageContext.request.contextPath}/View/${mytable.blueprintHook}">${mytable.name}</a>   
	</display:column>
    <display:column escapeXml="false"  property="whyChosen" decorator="org.pimslims.presentation.servlet.utils.EscapeHTMLWrapper" sortable="true" headerClass="sortable" title="Why Chosen" />
    <display:column escapeXml="false"  property="details" decorator="org.pimslims.presentation.servlet.utils.EscapeHTMLWrapper" sortable="true" headerClass="sortable" title="Details" />
	<display:setProperty name="paging.banner.group_size" value="10" />
	<display:setProperty name="export.decorated" value="false" />
    <display:setProperty name="paging.banner.item_name" value="Complex" />
    <display:setProperty name="paging.banner.items_name" value="Complexes" />
	<display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
	<display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
  </display:table>
</pimsWidget:box>
  <br/>

			 
<jsp:include page="/JSP/core/Footer.jsp" />

