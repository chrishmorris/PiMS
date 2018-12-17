<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
---------------------------------------------------------------------------
WeeklyBlastReport.jsp called by org.pimslims.servlet.Blast.WeeklyBlast.java
--------------------------------------------------------------------------- 
To display a table of 'Top Hits' created from weekly Blast of all Targets against 
PDB and TargetDb
Table has links to the record in external database and to Target
NOTE: Cannot link directly to TargetDB records, use search page
Author: Susy Griffiths, YSBL-York
Date: October 2007
--%>

<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.bioinf.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<c:catch var="error">
<jsp:useBean id="numTargets" scope="request" type="java.lang.String" />
<jsp:useBean id="topHitBeans" scope="request" type="java.util.List<org.pimslims.bioinf.TopHitBean>" />
<jsp:useBean id="numPDBDbRefs" scope="request" type="java.lang.Integer" />
<jsp:useBean id="numTDBDbRefs" scope="request" type="java.lang.Integer" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Scheduled Blast Report' />
</jsp:include>    

<!-- OLD -->

<!--main page content goes in here-->
<c:set var="count" value="${fn:length(requestScope.topHitBeans)}" />

<h2><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a> &gt;&gt; 
<a href="${pageContext.request.contextPath}/spot/SpotReports">Reports</a> &gt;&gt; PDB and TargetDB top hits</h2>
<%--Just for testing
<c:out value="PDB hits ${numPDBDbRefs}" />
<c:out value="TDB hits ${numTDBDbRefs}" />
--%>

<c:choose>
 <c:when test="${numPDBDbRefs==0 && numTDBDbRefs==0 }">
  <h3>No Top Hits recorded in PiMS<br /></h3>
      <a href="${pageContext.request['contextPath']}/help/target/HelpAutomatedBlast.jsp">Help</a> is available
  
 </c:when>

 <c:otherwise>
 <%--Add a key as a collapsible box --%>
 <div class="noprint">
 <pimsWidget:box title="Key"  initialState="closed" >
  <div class="collapsibleBox_content">
  <table id="key" border="0" class="list">
   <tr><th colspan="2" class="columnDivider">-mouse over the headings and cells for a description</th>
    <th colspan="3" class="columnDivider" title="PDB Top hits for your targets with the date of the latest Blast search" >
    PDB<br />Latest search:</th>
    <th colspan="4" title="TargetDB Top hits for your targets with the date of the latest Blast search">
    TargetDB<br />Latest search:</th></tr>
   <tr><td colspan="9" class="rowDivider"></td></tr>
   <tr><th title="Target name - organism - project"><br />Target</th>
    <th class="columnDivider" title="Length of the protein sequence"><br />Length</th>
    <th class="textCentre" title="PDB identifier for the Top Hit, link to MSD-PDB entry">Top<br />Hit</th>
    <th title="Expect value for hits below threshold of 1.0"><br />Evalue</th>
    <th class="columnDivider2" title="Number of identical residues as a percentage of the target length">&#037;<br />Identity</th>
    <th class="textCentre" title="TargetDB identifier for the Top Hit">Top<br />Hit</th><th title="Expect value for hits below threshold of 1.0"><br />Evalue</th>
    <th class="textCentre" title="Number of identical residues as a percentage of the target length">&#037;<br />Identity</th>
    <th class="textCentre" title="The TargetDB status of the Hit"><br />Status</th>
   </tr>
   <tr>
    <td class="solvedHere"title="View details for Target1: Green background -Target structure was solved locally"><a href="javascript:void(0)">Target1 - Homo sapiens - Human proteins</a></td>
    <td class="columnDivider3">350</td>
    <td title="TopHit since date shown"><a href="javascript:void(0)">1ABC</a><br /><small>04/11/2007</small></td>
	<td class="hitUnchanged" title="Orange background -Top hit is the same as in previous search, ">6.0E-87</td>
	<td class="hitUnchangedplus" title="Orange background -Top hit is the same as in previous search">41.0</td>
	<td title="TargetDB identifier for the Top Hit, since date shown">TDB123<br /><small>17/09/2007</small></td>
	<td class="hitUnchanged" title="Expect value for hit">0.31</td>
	<td class="highSim" title="Yellow background bold text -Target and Hit sequences share &gt;50&#037; identical residues over the length of the Target"><strong>64.0</strong></td>
	<td title="No background colour -The TargetDB status of the hit is earlier than &#39;Crystallized&#39;">Expressed</td>
   </tr>
   <tr>
    <td title="View details for Target2"><a href="javascript:void(0)">Target2 - Bacillus anthracis - Anthrax proteins</a></td>
    <td class="columnDivider3">1276</td>
    <td title="No background colour, Bold text no date -New top hit"><a href="javascript:void(0)"><strong>2DEF</strong></a></td>
	<td title="No background colour -New top hit, &lt;99&#037; identical">1.0E-21</td>
	<td class="columnDivider" title="No background colour, plain text -New top hit, &lt;99&#037; identical">25.53</td>
	<td title="TargetDB identifier for the Top Hit, since date shown">TDB456<br /><small>14/10/2007</small></td>
	<td class="hitUnchanged" title="Expect value for hit">1.0E-167</td>
	<td class="hitUnchanged" title="No background colour -Target and Hit sequences share &lt;50&#037; identical residues over the alignment">36.0</td>
	<td class="crystOrLater" title="Red background, Bold text -TargetDB status of the hit is &#39;Crystallized&#39; or later"><strong>In PDB</strong></td>
   </tr>
   <tr>
    <td title="View details for Target3"><a href="javascript:void(0)">Target3 - Staphylococcus aureus - S.aureus proteins</a></td>
    <td class="columnDivider3">306</td>
    <td class="solvedElsewhere" title="Red background -Hit shares &ge;99&#037; identity with Target, structure solved elsewhere"><a href="javascript:void(0)">3GHI</a><br /><small>17/09/2007</small></td>
	<td class="hitUnchanged" title="Orange background colour -Top hit is the same as in previous search">1.0E-155</td>
	<td class="highSimplus" title="Yellow background colour, Bold text -Target and Hit sequences share &gt;99&#037; identical residues over the length of the Target"><strong>99.2</strong></td>
	<td title="No background colour or value -No TargetDB Top hit for this Target"></td>
	<td title="No background colour or value -No TargetDB Top hit for this Target"></td>
	<td title="No background colour or value -No TargetDB Top hit for this Target"></td>
	<td title="No background colour or value -No TargetDB Top hit for this Target"></td>   
   </tr>
   <tr>
    <td title="View details for Target4"><a href="javascript:void(0)">Target4 - Escherichia coli - E.coli proteins</a></td>
    <td class="columnDivider3">96</td>
    <td title="No background colour or value -No PDB Top hit for this Target"></td>
	<td title="No background colour or value -No PDB Top hit for this Target"></td>
	<td class="columnDivider" title="No background colour or value -No PDB Top hit for this Target"></td>
	<td title="TargetDB identifier for the Top Hit, Bold text no date -New top hit"><strong>TDB789<br /></strong></td>
	<td title="Expect value for hit">1.0E-28</td>
	<td title="No background colour -Target and Hit sequences share &lt;50&#037; identical residues over the alignment">43.0</td>
	<td title="No background colour -The TargetDB status of the hit is earlier than &#39;Crystallized&#39;">Cloned</td>
   </tr>

  </table>
  </div>
</pimsWidget:box>
</div>
 <%-- End of key--%>
<%--
--%>
 <pimsWidget:box title="Results"  initialState="open" >
  <div class="collapsibleBox_content"> 
 <table id="summary" border="0" class="list">
  <tr><th colspan="2" class="columnDivider">
  <a href="${pageContext.request.contextPath}/help/HelpAutomatedBlast.jsp">Help</a></th>
  <c:choose>
  <c:when test="${numPDBDbRefs==0}" >
   <jsp:useBean id="dateTDBSearch" scope="request" type="java.lang.String" />
    <th colspan="3" class="columnDivider">PDB<br />Latest search:</th>
    <th colspan="4">TargetDB<br />Latest search: <c:out value="${dateTDBSearch}"/></th></tr>   
  </c:when>
  <c:when test="${numTDBDbRefs==0}" >
   <jsp:useBean id="datePDBSearch" scope="request" type="java.lang.String" />
  
    <th colspan="3" class="columnDivider">PDB<br />Latest search: <c:out value="${datePDBSearch}" /></th>
    <th colspan="4">TargetDB<br />Latest search: </th></tr>   
  </c:when>
  <c:otherwise>
  
   <th colspan="3" class="columnDivider">PDB<br />Latest search: <c:out value="${datePDBSearch}"/></th>
   <th colspan="4">TargetDB<br />Latest search: <c:out value="${dateTDBSearch}"/></th></tr>
  </c:otherwise>
  </c:choose>
  <tr><td colspan="9" class="rowDivider"></td></tr>
  <tr><th title="Target name - organism - project"><br />Target</th>
  <th class="columnDivider" title="Length of the protein sequence"><br />Length</th>
  <th class="textCentre" title="PDB identifier for the Top Hit, link to MSD-PDB entry">Top<br />Hit</th>
  <th title="Expect value for hits below threshold of 1.0"><br />Evalue</th>
  <th class="columnDivider2" title="Number of identical residues as a percentage of the target length">&#037; Identity</th>
  <th class="textCentre" title="TargetDB identifier for the Top Hit">Top<br />Hit</th>
  <th title="Expect value for hits below threshold of 1.0"><br />Evalue</th>
  <th class="textCentre" title="Number of identical residues as a percentage of the target length">&#037; Identity</th>
  <th class="textCentre" title="The TargetDB status of the  Hit"><br />Status</th>
  </tr>
   <c:forEach items="${topHitBeans}" var ="topHitBean" varStatus="status" begin="0">
       <tr>

<c:choose>
	<c:when test="${topHitBean.isSolvedLocally=='true'}">
      <td class="solvedHere">
    </c:when>
    <c:otherwise>
      <td>
    </c:otherwise>
</c:choose>
    <a href="${pageContext.request.contextPath}/View/<c:out value='${topHitBean.targetId}'/>"><c:out value="${topHitBean.details}" /></a></td>
	  <td class="columnDivider3"><c:out value="${topHitBean.targetSeqLen }"/></td>
<c:choose>
 <c:when test="${empty topHitBean.hitIDPDB }">
 	<td></td><td></td><td class="columnDivider3"></td>
 </c:when>
<%--PDB ID column --%>
 <c:otherwise>
	  <c:choose>
	  	<c:when test="${topHitBean.isSolvedLocally=='false'&&topHitBean.simPDB>=99&&topHitBean.isSameAsPrevious=='true'}">
			<td class="solvedElsewhere">
			<a href="http://www.ebi.ac.uk/pdbe-srv/view/entry/<c:out value='${fn:substring(topHitBean.hitIDPDB,0,4)}'/>" target="_blank" >${topHitBean.hitIDPDB}</a>
			 <c:if test="${topHitBean.pdbHitSince!=null }"><br />
			 <small><c:out value="${topHitBean.pdbHitSince }"/></small></c:if></td>
		</c:when>
		<c:when test="${topHitBean.isSameAsPrevious=='true'}">
			<td><a href="http://www.ebi.ac.uk/pdbe-srv/view/entry/<c:out value='${fn:substring(topHitBean.hitIDPDB,0,4)}'/>" target="_blank" >${topHitBean.hitIDPDB}</a>

			 <c:if test="${topHitBean.pdbHitSince!=null }"><br />
			 <small><c:out value="${topHitBean.pdbHitSince }"/></small></c:if></td>
 		</c:when>
		<c:when test="${topHitBean.isSolvedLocally=='false'&&topHitBean.simPDB>=99&&topHitBean.isSameAsPrevious=='false'}">
			<td class="solvedElsewhere"><strong>
			<a href="http://www.ebi.ac.uk/pdbe-srv/view/entry/=<c:out value='${fn:substring(topHitBean.hitIDPDB,0,4)}'/>" target="_blank" >${topHitBean.hitIDPDB}</a>
			</strong>
		</c:when>
		<c:when test="${topHitBean.isSameAsPrevious=='false'}">
			<td><strong>
			<a href="http://www.ebi.ac.uk/pdbe-srv/view/entry/<c:out value='${fn:substring(topHitBean.hitIDPDB,0,4)}'/>" target="_blank" >${topHitBean.hitIDPDB}</a>
			</strong></td>			
		</c:when>
	  
		<c:otherwise>
			<td><c:out value="${topHitBean.hitIDPDB }"/></td>
		</c:otherwise>
	 </c:choose>

<%--PDB Expect and Similarity columns --%>
	 <c:choose>
	  <c:when test="${topHitBean.isSameAsPrevious=='true'&&topHitBean.simPDB>=99 }">
			<td class="hitUnchanged"><c:out value="${topHitBean.expectPDB }"/></td> 
	    	<td class="highSimplus"><strong><fmt:formatNumber type="Number" maxFractionDigits="2" >
	    	<c:out value="${topHitBean.simPDB }"/></fmt:formatNumber></strong></td>
	  </c:when>
	  <c:when test="${topHitBean.isSameAsPrevious=='false'&&topHitBean.simPDB>=99 }">
			<td><c:out value="${topHitBean.expectPDB }"/></td> 
	    	<td class="highSimplus"><strong><fmt:formatNumber type="Number" maxFractionDigits="2" >
	    	<c:out value="${topHitBean.simPDB }"/></fmt:formatNumber></strong></td>
	  </c:when>
	  <c:when test="${topHitBean.isSameAsPrevious=='true'&&topHitBean.simPDB<99 }">
			<td class="hitUnchanged"><c:out value="${topHitBean.expectPDB }"/></td> 
	    	<td class="hitUnchangedplus"><fmt:formatNumber type="Number" maxFractionDigits="2" >
	    	<c:out value="${topHitBean.simPDB }"/></fmt:formatNumber></td>
	  </c:when>	  
	  <c:otherwise>
	  	  <td><c:out value="${topHitBean.expectPDB }"/></td> 
	  	  <td class="columnDivider"><fmt:formatNumber type="Number" maxFractionDigits="2" >
	  	  <c:out value="${topHitBean.simPDB }"/></fmt:formatNumber></td>	  
	  </c:otherwise>	  
	 </c:choose>
	</c:otherwise>
   </c:choose>	
<%--TargetDB ID and Expect columns --%>	  
   <c:choose>
    <c:when test="${empty topHitBean.hitIDTDB }">
     <td></td><td></td><td></td><td></td>
    </c:when>
    <c:otherwise>	
	  <c:choose>
	   	<c:when test="${topHitBean.isSameAsPreviousTDB=='true'}">
	  		<td><c:out value="${topHitBean.hitIDTDB }"/>
	  			<c:if test="${topHitBean.tdbHitSince !=null}"><br />
	  			<small><c:out value="${topHitBean.tdbHitSince}"/></small></c:if></td>
	  		<td class="hitUnchanged"><c:out value="${topHitBean.expectTDB }"/></td>
	  	 </c:when>
	  	 <c:otherwise>
	  		<td><strong><c:out value="${topHitBean.hitIDTDB }"/></strong></td>
	  		<td><c:out value="${topHitBean.expectTDB }"/></td>
	  	</c:otherwise>	  	
	  </c:choose>
<%--TargetDb similarity column --%>
	  <c:choose>
	  	<c:when test="${topHitBean.isGt50=='true'}" >
	  	  	  <td class="highSim"><strong><fmt:formatNumber type="Number" maxFractionDigits="2" >
	  	  	  <c:out value="${topHitBean.simTDB }"/></fmt:formatNumber></strong></td>	  	  
	  	</c:when>
	  	<c:when test="${topHitBean.isGt50=='false'&& topHitBean.isSameAsPreviousTDB=='true'}" >
	  	  	  <td class="hitUnchanged"><fmt:formatNumber type="Number" maxFractionDigits="2" >
	  	  	  <c:out value="${topHitBean.simTDB }"/></fmt:formatNumber></td>	  	  
	  	</c:when> 
	  	<c:otherwise>
	  	  	  <td><fmt:formatNumber type="Number" maxFractionDigits="2" >
	  	  	  <c:out value="${topHitBean.simTDB }"/></fmt:formatNumber></td>
	  	</c:otherwise>
	  </c:choose>
<%--TargetDb Status column --%>
	  <c:choose>
	    <c:when test="${topHitBean.isCrystallizedOrLater=='true'}">
	    	  <td class="crystOrLater"><strong><c:out value="${topHitBean.targetDBStatus }"/></strong></td>
	    </c:when>
	    <c:otherwise>
	          <td><c:out value="${topHitBean.targetDBStatus }"/></td>
	    </c:otherwise>
	  </c:choose>
	  
	</c:otherwise>
   </c:choose>            
	  </tr>
   </c:forEach>
 </table>
 </div>
</pimsWidget:box>
 
</c:otherwise>
</c:choose>

<!--end of main page content-->
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
