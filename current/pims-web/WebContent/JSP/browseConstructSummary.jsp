<%--
browseConstructSummary.JSP
@param _List_ - the list of the specific model objects provided by ReportExperimentParameters servlet 
@param includeHeader - set by ListEntryClones to false when the page firstly
displayed the header and footer come from a servlet. But then includeHeader
set to true at the end of this jsp, and since next request come to this jsp
and header and footer must be generated from here.

@author Bill Lin
@date September 2007
Re-design by Susy griffiths 07/2010
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

<!-- OLD -->
</c:if>

<%--Re-design, header and breadcrumb trail --%>
<c:set var="tgr" value="TargetGroupReport" />

<c:set var="breadcrumbs">
<a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> 
  <c:if test="${fn:contains(referrer,tgr)}">
    <jsp:useBean id="groupName" scope="request" type="java.lang.String" />
    <c:set var="tgrBreadcrumb" value="${fn:substringAfter(referrer, tgr)}"/>
    <c:set var="tgrHook" value="${fn:substringAfter(referrer, tgr)}"/>
    <c:set var="tgrName" value="${groupName}"/>
    
     : <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.TargetGroup">Target Groups</a> : 
      <a href="${pageContext.request.contextPath}/report/TargetGroupReport<c:out value="${tgrBreadcrumb}"/>"><c:out value="${groupName}"/></a>
  </c:if>
</c:set>
<c:set var="icon" value="blank.png" />        
<c:set var="title" value="Construct Summary for Targets"/>

<pimsWidget:pageTitle title="${title}" icon="${icon}" breadcrumbs="${breadcrumbs}"/>

<pimsWidget:box title="Construct summary" initialState="open">

 <c:choose>
  <c:when test="empty ${requestScope._List_}">
		Empty List
  </c:when>
  <c:otherwise>

  <display:table class="list" id="row" name="${requestScope._List_}" 
                              defaultsort="1" pagesize="${param.resultSize}"
                             sort="list" decorator="org.pimslims.servlet.target.TargetDecorator">
  							   
     <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="Target" >
       <pimsWidget:linkWithIcon text="${row.targetName}" title="View Target" icon="types/small/target.gif"
        url="${pageContext.request.contextPath}/View/${row.targetHook}" /> 
     </display:column> 
     <display:column escapeXml="true"  property="targetName" title="Target" media="xml excel csv"/>

     <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="Construct" >
       <pimsWidget:linkWithIcon text="${row.name}" title="View Construct" icon="types/small/construct.gif"
        url="${pageContext.request.contextPath}/View/${row.hook}" /> 
     </display:column>
     <display:column escapeXml="true" property="name" title="Construct" media="xml excel csv"/>

   	 <display:column style="font-family: monospace" title="Forward primer" media="xml excel csv" >
   		 	  <pims:sequence sequence="${row.fwdPrimer}" format='DEFAULT' escapeStyle="TEXT" />		
	 </display:column>
     <display:column style="font-family: monospace" title="Forward primer" media="html" >
            <a href="javascript:widePopUp('${pageContext.request.contextPath}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${row.name}%20Forward%20primer" />&amp;seq=<c:out value="${row.fwdPrimer}" />')"><c:out value="${row.fwdPrimer}" /></a>
     </display:column>  

     <display:column style="font-family: monospace" title="Reverse primer" media="xml excel csv" >
              <pims:sequence sequence="${row.revPrimer}" format='DEFAULT' escapeStyle="TEXT" />     
     </display:column>
     <display:column style="font-family: monospace" title="Reverse primer" media="html">
            <a href="javascript:widePopUp('${pageContext.request.contextPath}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${row.name}%20Reverse%20primer" />&amp;seq=<c:out value="${row.revPrimer}" />')"><c:out value="${row.revPrimer}" /></a>
     </display:column>  

    <%--IS ORGANISM NEEDED?
 	 <display:column escapeXml="true"  property="organismName" title="Organism" />
    --%>
     <%--Construct DNA sequence --%>
     <c:set var="truncDNA" value="${fn:substring(row.dnaSeq,-1,20)}" />
 	 <display:column style="font-family: monospace" title="Construct DNA sequence" media="pdf" >
		 	  <pims:sequence sequence="${truncDNA}" format='FASTA' escapeStyle="TEXT" />...
	 </display:column>    
     <display:column style="font-family: monospace" title="Construct DNA sequence" media="xml excel csv" >
              <pims:sequence sequence="${row.dnaSeq}" format='FASTA' escapeStyle="TEXT" />
     </display:column>
     <display:column style="font-family: monospace" title="Construct DNA sequence" media="html">
         <c:if test="${null != row.dnaSeq && row.dnaSeq != ''}" >
            <a href="javascript:widePopUp('${pageContext.request.contextPath}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${row.name}%20DNA%20sequence" />&amp;seq=<c:out value="${row.dnaSeq}" />')"><c:out value="${fn:substring(row.dnaSeq,-1,20)}" />...</a>
         </c:if>
     </display:column>  

    <%--DNA sequence parameters --%>
     <display:column style="text-align:right;" escapeXml="true"  property="dnaLen" title="DNA length" />
     <display:column style="text-align:right;" title="GC content">
              <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${row.gcContent}" /></fmt:formatNumber>
     </display:column>
    <%--IS THIS NEEDED?
     <display:column escapeXml="true"  property="dnaLenBy3" title="DNA length /3" />
      --%>

     <%--Final Protein sequence --%>
     <c:set var="truncProt" value="${fn:substring(row.finalProt,-1,20)}" />
     <display:column style="font-family: monospace" title="Final Protein sequence" media="pdf" >
              <pims:sequence sequence="${truncProt}" format='FASTA' escapeStyle="TEXT" />...
     </display:column>    
     <display:column style="font-family: monospace" title="Final Protein sequence" media="xml excel csv" >
              <pims:sequence sequence="${row.finalProt}" format='FASTA' escapeStyle="TEXT" />
     </display:column>
     <display:column style="font-family: monospace" title="Final Protein sequence" media="html">
         <c:if test="${null != row.finalProt && row.finalProt != ''}" >
            <a href="javascript:widePopUp('${pageContext.request.contextPath}/JSP/spot/SpotPopupFastaSeq.jsp?header=<c:out value="${row.name}%20Final%20Protein%20sequence" />&amp;seq=<c:out value="${row.finalProt}" />')"><c:out value="${fn:substring(row.finalProt,-1,20)}" />...</a>
         </c:if>
     </display:column>  

    <%--PROTEIN SEQUENCE PARAMETERS --%>
 	 <display:column escapeXml="true" property="finalProteinLength" title="Protein length" />
     <display:column style="text-align:right;" title="Mass (Da)">
              <fmt:formatNumber type="Number" maxFractionDigits="1" ><c:out value="${row.weight}" /></fmt:formatNumber>
     </display:column>
     <display:column style="text-align:right;" title="Extinction cm-1 M-1">
              <fmt:formatNumber type="Number" ><c:out value="${row.extinction}" /></fmt:formatNumber>
     </display:column>
     <display:column style="text-align:right;" title="pI">
              <fmt:formatNumber type="Number" maxFractionDigits="2" ><c:out value="${row.protPi}" /></fmt:formatNumber>
     </display:column>
 	  	
<%--TODO isDoMainConstruct and isStopCodon not being set correctly
 	 <display:column escapeXml="true"  property="isDoMainConstruct" title="Target Domain?" />
 	 <display:column escapeXml="true"  property="isStopCodon" title="Standard Stop codon?" />
 --%> 	

 	
     <display:column style="padding:2px 0 0 3px;width:20px;" media="html" title="Construct" >
       <pimsWidget:linkWithIcon text="${row.name}" title="View Construct" icon="types/small/construct.gif"
        url="${pageContext.request.contextPath}/View/${row.hook}" /> 
     </display:column>
     <display:column escapeXml="true" property="name" title="Construct" media="xml excel csv"/>
 			
	<display:setProperty name="paging.banner.group_size" value="15" />
	<display:setProperty name="export.decorated" value="false" />
	
	<display:setProperty name="export.excel.include_header" value="true" />
	<display:setProperty name="export.csv.include_header" value="true" />
	<display:setProperty name="sort.amount" value="list" /> <!-- set page if want to sort only first page before viewing -->
	<display:setProperty name="export.amount" value="list" /> <!-- set list if want to export all -->
	<display:setProperty name="paging.banner.item_name" value="Record" />
  <display:setProperty name="paging.banner.items_name" value="Records" />
  </display:table>
  
  </c:otherwise>
 </c:choose>
 </pimsWidget:box>
  
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p><error>
</c:if>

<c:if test="${includeHeader}">
	<jsp:include page="/JSP/core/Footer.jsp" />
</c:if>


<!-- browseConstructSummary.jsp -->
