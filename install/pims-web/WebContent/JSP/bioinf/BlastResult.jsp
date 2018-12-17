<%--
---------------------------------------------------------------------------
BlastResult.jsp called by org.pimslims.servlet.Blast.PDBBlast.java
--------------------------------------------------------------------------- 
To display a table of Blast hits, output from WSBlast serivces at the EBI
Table has links to the record in external database and to alignments
NOTE: Cannot link directly to TargetDB records, use search page
To add additional database links you will need to edit /bioinf/BioinfUtility
to add the details to dbURLMap
Author: Susy Griffiths, YSBL-York
Date: June 2007
--%>

<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.bioinf.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:useBean id="target_protName" scope="request" class="java.lang.String" />
<jsp:useBean id="result" scope="request" class="java.lang.String" />
<jsp:useBean id="target_hook" scope="request" class="java.lang.String" />
<jsp:useBean id="bhb" scope="request" class="org.pimslims.bioinf.BlastHeaderBean" />
<jsp:useBean id="blastBeans" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="blastHit" scope="request" class="org.pimslims.bioinf.BlastHitBean" />
<jsp:useBean id="blastAli" scope="request" class="org.pimslims.bioinf.BlastAlignmentBean" />
<jsp:useBean id="alignmentRows" scope="request" class="java.util.ArrayList" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Blast Results' />
</jsp:include>    

<!-- OLD -->

<!--main page content goes in here-->
<c:set var="count" value="${fn:length(requestScope.blastBeans)}" />

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> : 
   Target <a href="${pageContext.request.contextPath}/View/<c:out value='${target_hook}'/>"><c:out value="${target_protName}" /></a></c:set>
<c:set var="icon" value="" />        
<c:set var="title" value="Blast Results for Target: ${target_protName}"/>
<c:set var="actions">
    <pimsWidget:linkWithIcon 
                icon="misc/help.gif" 
                url="${pageContext.request.contextPath}/help/target/HelpBlastTarget.jsp" 
                text="Blast help"/>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:choose>
 <c:when test="${count == 1}">
  <h4>No Blast Hits for <c:out value="${target_protName}" /> in the <c:out value="${bhb.databaseSearched}" /></h4>
  <br />
  <INPUT TYPE="button" VALUE="Back" onClick="history.go(-1);return true;"/>        
 </c:when>
 
 <c:when test="${count == 0}">
   <h4>The Blast search has failed</h4>
   <h5>Possible reasons include:</h5>
   You may need to set an HTTP Proxy to run Blast searches in PiMS, please contact your PiMS Administrator.
   <br /><br />
   The EBI web services may not be available at present, please try again later.
   <br /><br />
  <INPUT TYPE="button" VALUE="Back" onClick="history.go(-1);return true;"/>        
 </c:when>
  
 <c:otherwise>
 <c:set var="alignments">
  <a href="#alignments">Alignments</a>
 </c:set>
 <c:set var="summary">
  <a href="#summary">Summary table</a>
 </c:set>

 <pimsWidget:box title="Summary table"  initialState="open" extraHeader="${alignments}">
 <div class="collapsibleBox_content"> 

 <table id="summary" border="0" class="list">
  <tr><th colspan="5">Database: <c:out value="${bhb.databaseSearched}" /></th>
  <th colspan="2">Date: <c:out value="${bhb.searchDate}" /></th></tr>
  <c:choose>
   <c:when test="${fn:startsWith(bhb.databaseSearched,'Target')}" >
      <tr><td colspan="8"><strong>NOTE: </strong>to view a TargetDB record please click the Search TargetDB link below.<br />
      Enter the Hit ID (e.g. APC81235) in the Project Target ID field in the search form.<br />
      Search <a href="http://targetdb.pdb.org/#search" target="_blank">TargetDB</a></td></tr>
   </c:when>
   <c:otherwise>
   </c:otherwise>
  </c:choose>
  <tr><td colspan="8"> </td></tr>
  <tr><th>No.</th><th>Hit ID</th><th>Description</th><th>Length</th><th>Score</th><th>Identity&#037;</th><th>Positives&#037;</th><th>Evalue</th>
  </tr>
   <c:forEach items="${blastBeans}" var ="blastHit" varStatus="status" begin="1">
       <tr>
     	<td><a href="#ali${blastHit.alignmentBeans[0].hitNum}"><c:out value="${status.count}" /></a></td>
     	<c:choose>
     	 <c:when test="${fn:startsWith(bhb.databaseSearched,'Target')}" >
     	  <td><c:out value="${bhb.databaseSearched}: ${blastHit.hitDbId}"/></td>
     	 </c:when>
     	 <c:otherwise>
     	  <td><c:out value="${bhb.databaseSearched}:"/><a href="<c:out value='${blastHit.dbBaseURL}${blastHit.hitURLid}'/>" target="_blank" >${blastHit.hitDbId}</a></td>
		 </c:otherwise>
		</c:choose> 	
		<td><c:out value="${blastHit.description}" /></td>
		<td><c:out value="${blastHit.hitLength}" /></td>
		<td><c:out value="${blastHit.alignmentBeans[0].score}"/></td>
		<c:set var="percentID" value="${blastHit.alignmentBeans[0].numIdentities /bhb.targetLen *100}"/>
		<c:choose>
		 <c:when test="${percentID>99 && fn:containsIgnoreCase(bhb.databaseSearched,'PDB')}">
			<td class="highSim"><fmt:formatNumber type="Number" maxFractionDigits="2" >
			<c:out value="${percentID}" />
       		</fmt:formatNumber></td>
       	 </c:when>
		 <c:when test="${percentID>50 && fn:containsIgnoreCase(bhb.databaseSearched,'Target')}">
			<td class="highSim"><fmt:formatNumber type="Number" maxFractionDigits="2" >
			<c:out value="${percentID}" />
       		</fmt:formatNumber></td>
       	 </c:when>
       	 <c:otherwise>
       	 	<td><fmt:formatNumber type="Number" maxFractionDigits="2" >
			<c:out value="${percentID}" />
       		</fmt:formatNumber></td>       	 	
       	 </c:otherwise>
		</c:choose>
		<td>
		<c:choose>
		<c:when test="${null!=blastHit.alignmentBeans[0].positives}">
			<c:out value="${blastHit.alignmentBeans[0].positives}"/>
		</c:when>
		<c:otherwise>
			<c:out value="n/a" />
		</c:otherwise>
		</c:choose>
		</td>
		<td><c:out value="${blastHit.alignmentBeans[0].expect}"/></td>
	  </tr>
   </c:forEach>
 </table>
  </div>
</pimsWidget:box>
 
<pimsWidget:box title="Alignments" initialState="open" id="alignments" extraHeader="${summary}">
 <div class="collapsibleBox_content"> 
  <c:forEach items="${blastBeans}" var ="hitDetails" varStatus="aliStatus" begin="1">
  <c:set var="numberAlis" value="${fn:length(hitDetails.alignmentBeans)}" />
   <table  id="ali${hitDetails.hitNum}" border="0" class="list">
	<tr class="blastali"><th colspan="1">Hit no: <c:out value="${hitDetails.hitNum}"/></th>
	
	 <c:choose>
      <c:when test="${fn:startsWith(bhb.databaseSearched,'Target')}" >
     <!-- <td colspan="7" >ID: <c:out value="${bhb.databaseSearched}: ${hitDetails.hitDbId}"/> -->
     	        <td colspan="7" >ID: <a href="<c:out value='${hitDetails.dbBaseURL}'/>" target="_blank" >${bhb.databaseSearched}:</a> ${hitDetails.hitDbId}
        &nbsp;&nbsp;&nbsp;
     	<c:out value="${hitDetails.description}" /></td>
      </c:when>
      <c:otherwise>
        <td colspan="7" >ID: <c:out value="${bhb.databaseSearched}:"/><a href="<c:out value='${hitDetails.dbBaseURL}${hitDetails.hitURLid}'/>" target="_blank" >${hitDetails.hitDbId}</a>
        &nbsp;&nbsp;&nbsp;
        <c:out value="${hitDetails.description}" /></td>
	  </c:otherwise>
	 </c:choose> 		
 	</tr>
	<tr><th>Hit length</th><th>Target length</th><th>Alignment length</th><th>Score (bits)</th><th>E value</th><th>Identities</th><th>Positives</th><th>Gaps</th>
	</tr>
	 <c:set var="alignments" value="${hitDetails.alignmentBeans}" />
	 <c:forEach items="${alignments}" var="alignment" varStatus="aStatus" >
	  <c:choose>
	  <c:when test="${aStatus.count==1}" >
	  <tr>
	   <td><c:out value="${hitDetails.hitLength}" /></td>
 	    <td><c:out value="${bhb.targetLen}" /></td>
 	    <td><c:out value="${alignment.alignmentLen}" /></td>
		<td><c:out value="${alignment.score}" />&nbsp;&nbsp;<c:out value="(${alignment.bits})"/></td>
		<td><c:out value="${alignment.expect}"/></td>
	    <td><c:out value="${alignment.numIdentities}"/></td>
	    <td>
	     <c:choose>
		  <c:when test="${0!=alignment.numPositives}">
			<c:out value="${alignment.numPositives}"/>
		 </c:when>
		 <c:otherwise>
			<c:out value="n/a" />
		 </c:otherwise>
		</c:choose>
	    </td>
	    <td><c:out value="${alignment.gaps}"/></td>    
	   </tr>
	   <tr><th colspan="2">&#037; identity over hit</th><th colspan="2">&#037; identity over alignment</th><th colspan="2">&#037; identity over Target</th><th colspan="2">Hit length as &#037; of Target</th></tr>
       <tr>
	    <td colspan="2"><fmt:formatNumber type="Number" maxFractionDigits="2" >
	    <c:out value="${alignment.numIdentities /hitDetails.hitLength * 100}" />
	    </fmt:formatNumber>&nbsp;&nbsp; 
      	(<c:out value="${alignment.numIdentities}" />/<c:out value="${hitDetails.hitLength}" />)</td>
      	<td colspan="2"><fmt:formatNumber type="Number" maxFractionDigits="2" >
       	<c:out value="${alignment.numIdentities /alignment.alignmentLen *100}" />
       	</fmt:formatNumber>&nbsp;&nbsp; 
       	(<c:out value="${alignment.numIdentities}" />/<c:out value="${alignment.alignmentLen}" />)</td>
      	<td colspan="2"><fmt:formatNumber type="Number" maxFractionDigits="2" >
       	<c:out value="${alignment.numIdentities /bhb.targetLen *100}" />
       	</fmt:formatNumber>&nbsp;&nbsp; 
       	(<c:out value="${alignment.numIdentities}" />/<c:out value="${bhb.targetLen}" />)</td>
      	<td colspan="2"><fmt:formatNumber type="Number" maxFractionDigits="2" >
       	<c:out value="${hitDetails.hitLength /bhb.targetLen *100}" />
       	</fmt:formatNumber>&nbsp;&nbsp; 
       	(<c:out value="${hitDetails.hitLength}" />/<c:out value="${bhb.targetLen}" />)</td>       
       </tr> 
       <tr><th colspan="8">Map of alignment: Top row = target sequence from 
	   <c:out value="${alignment.querySeqStart}" /> to 
  	   <c:out value="${alignment.querySeqEnd}" />, Bottom row = hit from 
  	   <c:out value="${alignment.matchSeqStart}" /> to 
       <c:out value="${alignment.matchSeqEnd}" />
         <c:if test="${null!=alignment.strand }">&nbsp;<em> <c:out value="${alignment.strand}" /> strand</em>
         </c:if>
       </th>
	   </tr>
	   <tr>
	    <td colspan = "8">
    	<div class="blastSequence">
        <br />
        <!-- If pattern contains errors due to errors in xml PiMS-1159 -->
        <c:if test="${fn:length(alignment.alignmentRows) == 0}">
         <pre class="blastPattern" ><c:out value="Sorry, PiMS is unable to display this alignment" /></pre>
        </c:if> 
		<c:forEach items="${alignment.alignmentRows}" var="row" varStatus="rowStatus" >
		 <c:choose>
		 <c:when test="${rowStatus.count % 3 == 2}">
		  <pre class="blastPattern" ><c:out value="${row}" /></pre>
		 </c:when>
		 <c:when test="${rowStatus.count % 3 == 0 }">
		  <pre><c:out value="${row}" /></pre><br />
		 </c:when>
		 <c:otherwise>
		  <pre><c:out value="${row}" /></pre>
		 </c:otherwise>
		 </c:choose>
        </c:forEach >
        <br />
        </div>
       </td>
	  </tr>  
     </c:when>
     <c:otherwise>
      <tr>
	   <td colspan = "8">
    	<div class="blastSequence">
    	<strong>Sub-alignment</strong>: Top row = target sequence from 
	   <c:out value="${alignment.querySeqStart}" /> to 
  	   <c:out value="${alignment.querySeqEnd}" />, Bottom row = hit from 
  	   <c:out value="${alignment.matchSeqStart}" /> to 
       <c:out value="${alignment.matchSeqEnd}" />
         <c:if test="${null!=alignment.strand }">&nbsp;<em> <c:out value="${alignment.strand}" /> strand</em>
         </c:if>
       <br /> 
    	Score (bits): <c:out value="${alignment.score}" /> <c:out value="(${alignment.bits})"/>,
    	E value: <c:out value="${alignment.expect}"/>, 
    	Identities: <fmt:formatNumber type="Number" maxFractionDigits="2" >
       <c:out value="${alignment.numIdentities /alignment.alignmentLen *100}" />
       </fmt:formatNumber>&#037; 
       (<c:out value="${alignment.numIdentities}" />/<c:out value="${alignment.alignmentLen}" />), 
       Positives: 
       <c:choose>
       	<c:when test="${0!=alignment.numPositives}" >
       	<fmt:formatNumber type="Number" maxFractionDigits="2" >
       		<c:out value="${alignment.numPositives /alignment.alignmentLen *100}" />
       	</fmt:formatNumber>&#037; 
       	(<c:out value="${alignment.numPositives}" />/<c:out value="${alignment.alignmentLen}" />)
       	</c:when>
       	<c:otherwise>
			<c:out value="n/a" />
        </c:otherwise>
       </c:choose>
        <br />
        <c:if test="${fn:length(alignment.alignmentRows) == 0}">
         <pre class="blastPattern" ><c:out value="Sorry, PiMS is unable to display this alignment" /></pre>
        </c:if> 
		<c:forEach items="${alignment.alignmentRows}" var="row" varStatus="rowStatus" >
		 <c:choose>
		 <c:when test="${rowStatus.count % 3 == 2}">
		  <pre class="blastPattern" ><c:out value="${row}" /></pre>
		 </c:when>
		 <c:when test="${rowStatus.count % 3 == 0 }">
		  <pre><c:out value="${row}" /></pre><br />
		 </c:when>
		 <c:otherwise>
		  <pre><c:out value="${row}" /></pre>
		 </c:otherwise>
		 </c:choose>
        </c:forEach >
        <br />
        </div>
       </td>
	  </tr>        
     </c:otherwise>
     </c:choose>
    </c:forEach>
  </table>
 </c:forEach>
 </div>
</pimsWidget:box>
 </c:otherwise>
 </c:choose>
<%-- This is for debugging, prints out xml
   <c:out value="${result}" />
 --%>

<!--end of main page content-->
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
