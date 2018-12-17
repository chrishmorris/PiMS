<%--
browseExperimentAndParameters.JSP
@param _List_ - the list of the specific model objects provided by ReportExperimentParameters servlet 
@param includeHeader - set by ListEntryClones to false when the page firstly
displayed the header and footer come from a servlet. But then includeHeader
set to true at the end of this jsp, and since next request come to this jsp
and header and footer must be generated from here.

@author Bill Lin
@date August 2007
Re-design by Susy Griffiths 07/2010
--%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %> 
<fmt:setLocale value='en_UK' />
<%@ page buffer = "64kb" %>
 
<c:catch var="error">

<c:if test="${includeHeader}">
 <jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value='List ${headerName}' />
 </jsp:include>

</c:if>
<jsp:useBean id="tgrHook" scope="request" type="java.lang.String" />
<jsp:useBean id="tgrName" scope="request" type="java.lang.String" />


<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> :
    <c:if test="${tgrHook != '' && tgrName != ''}" >
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a> : 
    <a href="${pageContext.request.contextPath}/report/TargetGroupReport<c:out value="${tgrHook}"/>"><c:out value="${tgrName}"/></a> :  
    </c:if>
    <c:if test="${not empty referrer}">
      <a href="${referrer}">Select parameters</a>     
    </c:if>
</c:set>
<c:set var="icon" value="blank.png" />        
<c:set var="title" value="Experiment Parameter report"/>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}"/>

 <pimsWidget:box title="Report" initialState="open">
  <display:table class="list" id="row" name="${requestScope._List_}" 
                              defaultsort="1" pagesize="${param.resultSize}"
                             sort="list" decorator="org.pimslims.servlet.target.TargetDecorator">
                               
     <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="Target" >
       <pimsWidget:linkWithIcon text="${row.targetName}" title="View Target" icon="types/small/target.gif"
        url="${pageContext.request.contextPath}/View/${row.targetHook}" /> 
     </display:column>
     <display:column escapeXml="true"  property="targetName" title="Target" media="xml excel csv"/>

    <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="Construct" >
       <pimsWidget:linkWithIcon text="${row.constructName}" title="View Construct" icon="types/small/construct.gif"
        url="${pageContext.request.contextPath}/View/${row.constructHook}" /> 
    </display:column>
    <display:column escapeXml="true"  property="constructName" title="Construct" media="xml excel csv"/>
    
    <c:forEach var="entry" items="${row.parameters}" >
        <display:column title="${entry.key}" >
            <c:forEach var="pValue" items="${entry.value}" >
                <c:out value="${pValue} "/>
            </c:forEach>
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

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>

<c:if test="${includeHeader}">
    <jsp:include page="/JSP/core/Footer.jsp" />
</c:if>


<!-- BrowseExperimentAndParameters.jsp -->
<%--
<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.text.*,java.util.*,org.pimslims.servlet.target.*"  %>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %> 

<fmt:setLocale value='en_UK' />
<%@ page buffer = "64kb" %>
<jsp:useBean id="conParams" scope="request" type="java.util.Collection<ConstructParameterBean>" />
<jsp:useBean id="pdHooks" scope="request" type="java.util.List<String>" />
<jsp:useBean id="targetHooks" scope="request" type="java.util.List<String>" />
<%--
browseParametersOfTarget.jsp
@param _List_ - the list of the specific model objects provided by ReportTargetParameters servlet 
@param includeHeader - set by ListEntryClones to false when the page firstly
displayed the header and footer come from a servlet. But then includeHeader
set to true at the end of this jsp, and since next request come to this jsp
and header and footer must be generated from here.

@author Bill Lin  modified by Susy Griffiths July 2010
@date August 2007
--%>
<%--
<c:catch var="error">

 <jsp:include page="/JSP/core/Header.jsp">
     <jsp:param name="HeaderName" value='List Parameters of Targets' />
     <jsp:param name="mayUpdate" value='${mayUpdate}' />     
 </jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> 
</c:set>
<c:set var="icon" value="blank.png" />        
<c:set var="title" value="Experiment Parameter report:"/>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}"/>
<c:out value="Page size = ${pagesize}" /><br />
<c:out value="Result size = ${resultSize}" /><br />
<c:out value="Number of beans is ${fn:length(conParams)}" /><br />
<c:out value="Number of parameters is ${fn:length(pdHooks)}" /><br />
<c:out value="Number of Targets is ${fn:length(targetHooks)}" />
<%--
  <c:choose>
   <c:when test="${empty pdHooks}">
     <h4><c:out value="No Parameters selected. Please select some Parameters to compare" /></h4>
   </c:when>
   <c:otherwise>
--%>
<%--
    <pimsWidget:pageControls>
    </pimsWidget:pageControls>

 <pimsWidget:box title="Report2" initialState="open">
  <display:table class="list" id="mytable" name="${conParams}" 
                               pagesize="${pagesize}"
                              sort="list" defaultsort="1"
                              partialList="true" size="${resultSize}"
                              decorator="org.pimslims.servlet.target.TargetDecorator">
 
     <display:column style="padding:2px 0 0 3px;width:20px;" title="Target" sortable="true" headerClass="sortable" >
       <pimsWidget:linkWithIcon text="${mytable.targetName}" title="View Target" icon="types/small/target.gif"
        url="${pageContext.request.contextPath}/View/${mytable.targetHook}" /> 
     </display:column>

     <display:column style="padding:2px 0 0 3px;width:20px;" title="Target" sortable="true" headerClass="sortable" >
       <pimsWidget:linkWithIcon text="${mytable.constructName}" title="View Construct" icon="types/small/construct.gif"
        url="${pageContext.request.contextPath}/View/${mytable.constructHook}" /> 
     </display:column>

    <c:forEach var="entry" items="${mytable.parameters}" >
        <display:column title="${entry.key}" sortable="true" headerClass="sortable" >
            <c:forEach var="pValue" items="${entry.value}" >
                <c:out value="${pValue} "/>
            </c:forEach>
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

    <c:forEach var="targetHook" items="${targetHooks}" >
     <input name="${targetHook}"  class="behaviour_selectAll"
                            type="hidden" value="on"> 
    </c:forEach>    
    <c:forEach var="pdHook" items="${pdHooks}" >
     <input name="${pdHook}"  class="behaviour_selectAll"
                            type="hidden" value="on"> 
    </c:forEach>    

<%--
  </c:otherwise>
 </c:choose>
--%>
<%--
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />

<!-- BrowseExperimentAndParameters.jsp -->
--%>