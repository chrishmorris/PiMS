<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.text.*,java.util.*,org.pimslims.presentation.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<jsp:useBean id="hook" scope="request" type="java.lang.String" />
<jsp:useBean id="targets" scope="request" type="java.util.Collection<TargetBeanForLists>" />
<jsp:useBean id="nameAttr" scope="request" type="java.lang.String" />
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
<%--
@author Susy Griffiths from Petr Troshin
@date September 2006 updated June 2010 for version 4.2
Called by org.pimslims.servlet.report.TargetGroupReport.java
Servlets:  org/pimslims/servlet/report/TargetGroupReport.java
--%>

<c:catch var="error">
<!-- TargetGroupReport.jsp -->

<jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value="${nameAttr}" />
     <jsp:param name="mayUpdate" value='${mayUpdate}' />     
 </jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> : 
  <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a> : 
  <a href="${pageContext.request.contextPath}/View/${hook}">${nameAttr}</a></c:set>
<c:set var="icon" value="targetgroup.png" />        
<c:set var="title" value="Report"/>
<c:set var="actions">
    <c:choose>
        <c:when test="${! mayUpdate}" >
               <pimsWidget:linkWithIcon 
                isGreyedOut="true"
                icon="" 
                url="#" 
                title="You do not have permission to update this Target Group"
                text="Can't add a Target"/>
        </c:when>      
    </c:choose>
</c:set>

<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

  <c:choose>
   <c:when test="${empty  targets}">
     <h4><c:out value="No Targets recorded for this Target Group" /></h4>
<pimsWidget:multiRoleBox objectHook="${hook}" roleName="targets" title="Search/Add targets" />
   </c:when>
   <c:otherwise>
    <pimsWidget:pageControls>
    </pimsWidget:pageControls>

    <form action="${pageContext.request.contextPath}/report/TargetsReportAction" method="get"  style="background-color: transparent; width: auto;">

    <pimsWidget:box title="Targets in ${nameAttr}" initialState="open">
     <display:table class="list" id="mytable" name="${targets}"
                              pagesize="${pagesize}"
                             sort="list" 
                             partialList="true" size="${resultSize}"
                             decorator="org.pimslims.servlet.target.TargetDecorator">
                             
     <display:column escapeXml="false" headerClass="sortable" title="<input name='checkAll' id='checkAll' onclick='toggleAllCheckboxes(this)' type='checkbox'/> " media="html">
               <input name="${mytable.hook}" class="behaviour_selectAll"  type="checkbox" />
    </display:column>
    <display:column title="Name" escapeXml="false" style="padding:2px 0 0 3px;width:20px;" media="html" sortable="true"  >
        <pimsWidget:link bean="${mytable}"/>
    </display:column>
    <display:column  title="Name" escapeXml="true" media="xml excel csv" sortable="true" property="commonName" />
    <display:column escapeXml="true"  property="proteinName" sortable="true" headerClass="sortable" title="Protein name" />
    <display:column escapeXml="true"  property="aliases" title="Aliases"/>
    <display:column escapeXml="true"  property="functionDescription" maxLength="100" title="Function description"/>
    <display:column escapeXml="true"  property="whyChosen" headerClass="details" maxLength="100" title="Comments" />     <display:column escapeXml="true"  property="projectList" sortable="true" headerClass="sortable" maxWords="3" title="Lab Notebook" />
    <display:column escapeXml="true"  property="status" sortable="true" headerClass="sortable"/>
    <display:column escapeXml="true"  property="statusDate" title="Status date" media="html" sortable="true" headerClass="sortable" decorator="org.pimslims.servlet.utils.decorators.Date" />
    
    <display:setProperty name="paging.banner.group_size" value="10" />
    <display:setProperty name="export.decorated" value="true" />
    <display:setProperty name="paging.banner.item_name" value="Target" />
    <display:setProperty name="paging.banner.items_name" value="Targets" />
    <display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
    <display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->  
  </display:table>
</pimsWidget:box>

 Action:
    <select name="action">
    <option value="Compare Experiment Parameter" selected="selected"> Compare Experiment Parameter</option>
    <option value="Constructs Summary">Constructs Summary</option>
    </select> 
             
<input  value="Next &gt;&gt;&gt;" type="submit">
</form>

<%--OLD JSP --%>
<%-- Make the target list again if the session is expired
<c:if test="${empty pagerSize}"> 
    <c:redirect url="/Search/org.pimslims.model.target.TargetGroup" />
</c:if>

<c:if test="${includeHeader}">
<jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value="${nameAttr}" />
     <jsp:param name="mayUpdate" value='${mayUpdate}' />     
 </jsp:include>
</c:if>


<form method="get" style="width: auto; background-color: white;" >
Set number of elements displayed on one page
<c:forTokens items="10 20 30 50 100 -1" delims=" " var="token" >
	<c:set var="checked" value=""/>

	<c:if test="${token == param.pagerSize}">
		<c:set var="checked" value="checked"/>
	</c:if>

	<c:choose>
	<c:when test="${token == '-1'}">
		<input type="radio" ${checked} value="<%=Integer.MAX_VALUE %>" name="pagerSize" />unlimited	
	</c:when>
	<c:otherwise>
		<input type="radio" ${checked} value="${token}" name="pagerSize" />${token}
	</c:otherwise>
	</c:choose>
</c:forTokens>
<br />
<input style="width:auto;" type="submit"	name="Submit" value="Update view" />
</form>


<%
   String itNum = (String)request.getParameter("pagerSize");
   if(itNum == null || itNum.trim().length() ==0) {
	   	pageContext.setAttribute("itemNum", request.getAttribute("pagerSize"));
   } else {
	   	pageContext.setAttribute("itemNum", itNum);
   }
//

%>
<%--
<c:if test="${0==counter}"> 
 <c:out value="${counter} Targets with details" />
</c:if>

<form action="${pageContext.request.contextPath}/report/TargetsReportAction" method="get"  style="background-color: transparent; width: auto;">

  <display:table class="list" id="row" name="${requestScope.targets}" 
  							  pagesize="${itemNum}"
  							 decorator="org.pimslims.servlet.target.TargetDecorator">
  							 
     <display:column headerClass="sortable" title="" >
		       <input name="${row.hook}" class="behaviour_selectAll"	type="checkbox">
  	</display:column>
     <display:column escapeXml="false"  property="link" sortable="true" headerClass="sortable" media="html" />
     <display:column escapeXml="true"  property="commonName" sortable="true" headerClass="sortable" title="Name"/>
     <display:column escapeXml="true"  property="proteinName" sortable="true" headerClass="sortable" title="Protein name" />
     <display:column escapeXml="true"  property="aliases" title="Aliases" />
     <display:column escapeXml="true"  property="functionDescription" title="Function description"/>
     <c:if test="${0!=counter}">
      <display:column escapeXml="true"  property="details" headerClass="details" />
	</c:if>
     <display:column escapeXml="true"  property="status" sortable="true" headerClass="sortable"/>
   <display:setProperty name="paging.banner.group_size" value="10" />
	<display:setProperty name="export.decorated" value="false" />
  <display:setProperty name="paging.banner.item_name" value="Target" />
  <display:setProperty name="paging.banner.items_name" value="Targets" />
   </display:table>   

  <br/>
 Action:
	<select name="action">
	<option value="Compare Experiment Parameter" selected="selected"> Compare Experiment Parameter</option>
	<option value="Constructs Summary">Constructs Summary</option>
	</select> 
			 
<input  value="Next &gt;&gt;&gt;" type="submit">
</form>
--%>

  </c:otherwise>
 </c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
