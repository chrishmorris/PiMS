<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.text.*,java.util.*,org.pimslims.bioinf.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%--

Author: Peter Troshin, STFC Daresbury Laboratory
Date: September 2007
--%>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Local Similarity Search Results' />
</jsp:include>

<%-- page body here --%>
<c:set var="title">Similarity Search Results</c:set>
<c:set var="breadcrumbs">
</c:set>

<c:set var="actions">
    <pimsWidget:linkWithIcon 
                icon="misc/help.gif" 
                url="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp#summary" 
                text="Similarity Search Help"/>
    Database: Local PIMS Database

</c:set>

<pimsWidget:pageTitle icon="blank.png" title="${title}" breadcrumbs="${breadcrumbs}" actions="${actions}"/>

 <c:set var="alis">
  <a href="#alis">View Alignments</a>
 </c:set>
 <c:set var="summary">
  <a href="#summary">View Summary table</a>
 </c:set>

 <span id="summary" />
 <pimsWidget:box title="Summary table"  initialState="open" extraHeader="${alis}">
 <div class="collapsibleBox_content"> 

 <table border="0" class="list">
    <tr>
        <th id="No">No.</th><th>Hit Id</th><th>Hit Type</th><th id="alignLength">Length</th><th id=score>Score</th><th>Identity &#037;</th><th>Gaps &#037;</th>
    </tr>
       <c:forEach items="${alignments}" var="ali" varStatus="status" >
        <c:set var="ali" value="${ali}" scope="request"/>
        <c:set var="status" value="${status}" scope="request"/>
        <c:import url="SWHitList.jsp"></c:import>
     </c:forEach>
    <c:remove var="ali" />
    <c:remove var="status" />    
 </table>
 </div>
 </pimsWidget:box>
 
 <span id="alis" />
 <pimsWidget:box title="Alignments"  initialState="open" extraHeader="${summary}">
 <div class="collapsibleBox_content"> 

<c:forEach items="${alignments}"  var="al" varStatus="status">
    <c:set var="align" value="${al}" scope="request"/>
    <c:set var="status" value="${status}" scope="request"/>
  <c:import url="Alignment.jsp"></c:import>
<hr />
</c:forEach>
<c:remove var="align" />
<c:remove var="status" />
 </div>
 </pimsWidget:box>

<%-- OLD

	<table id="summary" border="0" class="list">
  <tr><th colspan="2">Similarity Search Summary <a href="${pageContext.request.contextPath}/help/HelpLocalSimilaritySearch.jsp#summary">help</a></th>
  <th colspan="4">Database: Local PIMS Database<br /></th>
  <th colspan="2">View <a class="blast" href="#alignments">Alignments</a></th></tr>

  <tr><td colspan="8"> </td></tr>
  <tr><th id="No">No.</th><th>Hit Id</th><th>Hit Type</th><th id="alignLength">Length</th><th id=score>Score</th><th>Identity &#037;</th><th>Gaps &#037;</th>
  </tr>

   <c:forEach items="${alignments}" var="ali" varStatus="status" >
		<c:set var="ali" value="${ali}" scope="request"/>
		<c:set var="status" value="${status}" scope="request"/>
		<c:import url="SWHitList.jsp"></c:import>
	 </c:forEach>
	<c:remove var="ali" />
	<c:remove var="status" />
 </table>

<a name="alignments"></a>
<h2>Alignments details</h2>
<c:forEach items="${alignments}"  var="al" varStatus="status">
	<c:set var="align" value="${al}" scope="request"/>
	<c:set var="status" value="${status}" scope="request"/>
  <c:import url="Alignment.jsp"></c:import>
</c:forEach>
<c:remove var="align" />
<c:remove var="status" />

--%>
<script type="text/javascript">
Element.observe(window,"load",function(){
  addTooltip("No","Alignment number","Click to view individual alignment");
  addTooltip("alignLength","Alignment length","The common sequence length");
  addTooltip("score","Smith-Waterman score","Measure the quality of the alignment");
  addTooltip("blastSequence","Alignment between the Query the Hit.","There RC Query is a reverse complement of the Query");
});
${hitTabletooltipJS}
</script>

</c:catch><c:if test="${error != null}">"/>
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    <error/><p class="error"> <c:out value='${error}'/></p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
