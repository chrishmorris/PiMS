<%--
First attempt to create jsp for Target_ExperimentReport

Author: Susy Griffithe
Date: November 2006
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Target-experiment Report' />
</jsp:include>

<!-- OLD --> 

<!--
	Setting up so we can keep a record of the boxes written
	- later we'll use that to close them all
-->
<script type="text/javascript">
var path="";
</script>
<!-- end of header -->

obsolete
<%
   String itNum = (String)request.getParameter("pagerSize");
   if(itNum == null || itNum.trim().length() ==0) {
	   	pageContext.setAttribute("itemNum", session.getAttribute("pagerSize"));
   } else {
	   	pageContext.setAttribute("itemNum", itNum);
   }
//

%>

Set the pager size
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=10" title="Set pager size equal 10 ">10</a>
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=20" title="Set pager size equal 20 ">20</a>
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=30" title="Set pager size equal 30 ">30</a>
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=50" title="Set pager size equal 50 ">50</a>
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=100" title="Set pager size equal 100 ">100</a>
<a href="${pageContext.request.contextPath}/JSP/report/Target_experiment?pagerSize=-1" title="Set pager size equal unlimited ">unlimited</a>
<br />

<!-- Modified from TargetGroupReport.JSP needs scope for list to process-->
  <display:table class="list" id="row" 
  							  pagesize="${itemNum}"
  							 requestURI="/JSP/TargetExperimentReport.jsp"
  							 decorator="org.pimslims.servlet.target.TargetDecorator">

     <display:column title="Experiment type"/>
     <display:column sortable="true" headerClass="sortable" title="Experiment ID" />
     <display:column sortable="true" headerClass="sortable" title="Status" />
     <display:column sortable="true" headerClass="sortable" title="Start date"/>
     <display:column sortable="true" headerClass="sortable" title="End date"/>
     <display:column sortable="true" headerClass="sortable" title="Scientist"/>
     <display:column headerClass="details" title="Details"/>
	<display:setProperty name="paging.banner.group_size" value="10" />
	<display:setProperty name="export.decorated" value="true" />
  <display:setProperty name="paging.banner.item_name" value="Target" />
  <display:setProperty name="paging.banner.items_name" value="Targets" />
   </display:table>


<c:if test="${includeHeader}">
	<jsp:include page="/JSP/core/Footer.jsp" />
</c:if>

<c:set var="includeHeader" value="${true}" scope="session" target="includeHeader"/>
<!-- /Target_experiment.jsp -->
