<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display a list of targets
--%> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%-- Required to make export work, in older versions of Tomcat --%>
<%@ page buffer="64kb" %>

<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<%@ page import="org.pimslims.model.target.Target" %>
<!-- Search_target.jsp  -->
<c:catch var="error">

<jsp:useBean id="targetMetaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:useBean id="results" scope="request" type="java.util.Collection<org.pimslims.presentation.TargetBeanForLists>" />
<jsp:useBean id="targetStatus" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" />
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
<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a></c:set>

<c:set var="actions">
    <pimsWidget:linkWithIcon isNext="${0 eq resultSize}"
        icon="actions/create/target.gif" 
        url="${pageContext.request.contextPath}/JSP/Upload.jsp" 
        text="New Target"/>
</c:set>
<pimsWidget:pageTitle icon="target.png"
	title="Search Targets"
	actions="${actions}"
	breadcrumbs="${breadcrumbs}" />

<div style="margin-right:10em" class="slimline_forms">


<pimsWidget:pageControls>
<c:if test="${!empty param.name}">
  <input type="hidden" name="name" value="${fn:escapeXml(param.name)}"/>
</c:if>
<c:if test="${!empty param.proteinName}">
  <input type="hidden" name="proteinName" value="${fn:escapeXml(param.proteinName)}"/>
</c:if>
<c:if test="${!empty param.alias}">
  <input type="hidden" name="alias" value="${fn:escapeXml(param.alias)}"/>
</c:if>
<c:if test="${!empty status}">
  <input type="hidden" name="milestones" value="${fn:escapeXml(status)}"/>
</c:if>
<c:if test="${!empty targetGroup}">
  <input type="hidden" name="targetGroups" value="${fn:escapeXml(targetGroup)}"/>
</c:if>
<c:if test="${!empty project}">
  <input type="hidden" name="projects" value="${fn:escapeXml(project)}"/>
</c:if>
    <c:if test="${!empty param.search_all}">
      <input type="hidden" name="search_all" value="${fn:escapeXml(param.search_all)}"/>
    </c:if>
</pimsWidget:pageControls>



<pimsWidget:quickSearch initialState="open"  value="${fn:escapeXml(param['search_all'])}" />

<c:set var="prop_projects"><%=Target.PROP_ACCESS %></c:set>
<c:set var="prop_targetgroups"><%=Target.PROP_TARGETGROUPS %></c:set>
<c:set var="prop_milestones"><%=Target.PROP_MILESTONES %></c:set>
<pimsWidget:box title="Advanced Search" initialState="closed">

	<pimsForm:form action="/Search/org.pimslims.model.target.Target" mode="edit" method="get">
		<input type="hidden" name="pagesize" value="${pagesize}"/>

		<pimsForm:formBlock>
		<pimsForm:column1>
			<pimsForm:text name="name" alias="Name" value="${fn:escapeXml(param['name'])}" />
			<pimsForm:text name="proteinName" alias="Protein name" value="${fn:escapeXml(param['proteinName'])}" />
			<pimsForm:text name="alias" alias="Alias" value="${fn:escapeXml(param['alias'])}" />
		</pimsForm:column1>
		<pimsForm:column2>

			<pimsForm:select alias="Lab Notebook" name="${prop_projects}" >
		    	<option value="" >[any]</option>
	    		<c:forEach items="${projects}" var="prj">
		    	<c:choose><c:when test="${access==prj.hook}">
		    		<option value="${prj.hook}" selected="selected"><c:out value="${prj.name}"  /></option>
		    	</c:when><c:otherwise>
		    		<option value="${prj.hook}"><c:out value="${prj.name}" /></option>
		    	</c:otherwise></c:choose>
	    		</c:forEach>
			</pimsForm:select>
			
			<pimsForm:select alias="Target Group" name="${prop_targetgroups}" >
		    	<option value="" >[any]</option>
	    		<c:forEach items="${targetGroups}" var="group">
		    	<c:choose><c:when test="${targetGroup==group.hook}">
		    		<option value="${group.hook}" selected="selected"><c:out value="${group.name}"  /></option>
		    	</c:when><c:otherwise>
		    		<option value="${group.hook}"><c:out value="${group.name}" /></option>
		    	</c:otherwise></c:choose>
	    		</c:forEach>
			</pimsForm:select>
			
	
			<pimsForm:select alias="Target Status" name="${prop_milestones}" >
	    		<option value="" >[any]</option>
	    		<c:forEach items="${targetStatus}" var="tstatus">
		    	<c:choose><c:when test="${status==tstatus.hook}">
		    		<option value="${tstatus.hook}" selected="selected"><c:out value="${tstatus.name}"  /></option>
		    	</c:when><c:otherwise>
		    		<option value="${tstatus.hook}"><c:out value="${tstatus.name}" /></option>
		    	</c:otherwise></c:choose>
	    		</c:forEach>
			</pimsForm:select>
		
			<input  value="Search" type="submit" onClick="dontWarn()" />
		
		</pimsForm:column2>
		</pimsForm:formBlock>
		
	</pimsForm:form>
</pimsWidget:box>

<!--update the select after search-->
</div>

<strong>${totalRecords}</strong> targets recorded in the database<br/>
<strong>${resultSize}</strong> match search criteria

<hr />

<form id="selectedTargets" action="${pageContext.request.contextPath}/report/TargetsReportAction" method="get"  style="background-color: transparent; width: auto;">

	<h2>${resultSize} Targets</h2>

	
	<c:choose><c:when test="${empty results}" >
    	<h2>No Targets found</h2>
	</c:when><c:otherwise>
    <script type="text/javascript">	 focusElement =null </script>
    <%
        request.setAttribute("listMetaClass", targetMetaClass);
        // was request.setAttribute("results", targets);
        request.setAttribute("chooseExp", true);
    %>
    
    <%-- TODO get this list out of the session now the list is paged --%>
<pimsWidget:box title="Results" initialState="open">
  <display:table class="list" id="mytable" name="${results}"
  							  pagesize="${pagesize}"
  							 sort="list" defaultsort="8"
  							 partialList="true" size="${resultSize}"
  							 decorator="org.pimslims.servlet.target.TargetDecorator">

     <display:column escapeXml="false"   headerClass="sortable" title="<input name='checkAll' id='checkAll' onclick='toggleAllCheckboxes(this)' type='checkbox'/> " media="html">
               <input name="${mytable.hook}" class="behaviour_selectAll"  type="checkbox" />
    </display:column>
  	<display:column escapeXml="false"  style="padding:2px 0 0 3px;width:20px;" media="html" sortable="true" title="Name" >
		<pimsWidget:link bean="${mytable}"/>
	 </display:column>
     <display:column escapeXml="true"  property="proteinName" sortable="true" headerClass="sortable" title="Protein name" />
     <display:column escapeXml="true"  property="aliases" title="Aliases"/>
     <%-- <display:column escapeXml="true"  property="functionDescription" maxLength="100" title="Function description"/> --%>
     <display:column escapeXml="true"  property="whyChosen" headerClass="details" maxLength="100" title="Comments" />
     <display:column escapeXml="true"  property="projectList" sortable="true" headerClass="sortable" maxWords="3" title="Lab Notebook" />
     <display:column escapeXml="true"  property="targetGroupList" sortable="true" headerClass="sortable" maxWords="3" title="Target group" />
     <display:column escapeXml="true"  property="status" sortable="true" headerClass="sortable"/>
    <display:setProperty name="paging.banner.group_size" value="10" />
	<display:setProperty name="export.decorated" value="true" />
  <display:setProperty name="paging.banner.item_name" value="Target" />
  <display:setProperty name="paging.banner.items_name" value="Targets" />
	<display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
	<display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
  </display:table>
</pimsWidget:box>

	</c:otherwise>
	</c:choose>

 Reports on selected targets:
    <select name="action">
    <option value="Compare Experiment Parameter" selected="selected"> Compare Experiment Parameter</option>
    <option value="Constructs Summary">Constructs Summary</option>
    </select>

<input  value="Next &gt;&gt;&gt;" type="submit" />
</form>  


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error/>
</c:if>


<jsp:include page="/JSP/core/Footer.jsp" />

