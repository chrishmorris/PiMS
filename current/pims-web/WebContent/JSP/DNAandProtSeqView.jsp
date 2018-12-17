<%--
DNAandProtseqView.JSP
Used in DNAandProtSeqPopup.jsp
Author: Susy Griffiths, YSBL-York
Date: June 2006
--%>
<%@page import="java.util.ArrayList" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="org.pimslims.lab.sequence.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/skins/default/css/construct.css" type="text/css" />


<%--Caller must provide dnaSeq --%>
<% String dnaSeq=request.getParameter("dnaSeq");%>
<%ArrayList dnaAndProtSeq2 = ThreeLetterProteinSeq.dnaAndProtArray(dnaSeq);
pageContext.setAttribute("dnaAndProtSeq2", dnaAndProtSeq2);%>

<div class="seqPopup">
<c:set var="seqLen" value="${fn:length(param['dnaSeq'])/3}" />
<c:choose>
 <c:when test="${seqLen<26}">
  <c:forEach items="${dnaAndProtSeq2}" var="seq" varStatus="status" >
  <c:choose>
   <c:when test="${status.count==1}">
   <c:out value="${seq}" />&nbsp;&nbsp;<fmt:formatNumber type="Number" maxFractionDigits="0" ><c:out value="${seqLen*3}" /></fmt:formatNumber><br />
   </c:when>
   <c:when test="${status.count==2}">
	<c:set var="seq" value="${fn:replace(seq, 'Met','MET')}" />
	<c:set var="seq" value="${fn:replace(seq, 'Ter', 'TER')}" />
   <c:out value="${seq}" />&nbsp;&nbsp;<strong><fmt:formatNumber type="Number" maxFractionDigits="0" ><c:out value="${seqLen}" /></fmt:formatNumber></strong><br /><br />
   </c:when>
  </c:choose>
 </c:forEach>
 </c:when>
 <c:otherwise>
<%-- the number of complete rows to show --%>
<c:set var="cpteRows" value="${(seqLen - (seqLen%25) ) /25 * 2 }" />
<c:set var="resPerRow" scope="page" value="25" />
<c:forEach items="${dnaAndProtSeq2}" var="seq" varStatus="status" begin="0" end ="${cpteRows-1}" >
<c:choose>
<c:when test="${status.count%2==1}">
    <c:out value="${seq}" />&nbsp;&nbsp;<fmt:formatNumber type="Number" maxFractionDigits="0" >
        <c:out value="${resPerRow*(status.count+1)/2*3}" />
    </fmt:formatNumber><br />
</c:when>
<c:when test="${status.count%2==0}">
	<c:set var="seq" value="${fn:replace(seq, 'Met','MET')}" />
	<c:set var="seq" value="${fn:replace(seq, 'Ter', 'TER')}" />
    <c:out value="${seq}" />&nbsp;&nbsp;<strong><fmt:formatNumber type="Number" maxFractionDigits="0" >
        <c:out value="${resPerRow*(status.count)/2}" />
    </fmt:formatNumber></strong>
<br /><br />
</c:when>
</c:choose>
</c:forEach>

<%-- now show the tail end of the sequence --%>
<c:if test="${''!=dnaAndProtSeq2[cpteRows]}">
   <c:out value="${dnaAndProtSeq2[cpteRows]}" />&nbsp;&nbsp;
   <fmt:formatNumber type="Number" maxFractionDigits="0" >
       <c:out value="${seqLen*3}" />
   </fmt:formatNumber><br />
   	<c:set var="endSeq" value="${fn:replace(dnaAndProtSeq2[cpteRows+1], 'Met','MET')}" />
	<c:set var="endSeq" value="${fn:replace(endSeq, 'Ter', 'TER')}" />

   <c:out value="${endSeq}" />&nbsp;&nbsp;<strong>
   
<%--   <c:out value="${dnaAndProtSeq2[cpteRows+1]}" />&nbsp;&nbsp;<strong>--%>
   <fmt:formatNumber type="Number" maxFractionDigits="0" >
       <c:out value="${seqLen}" />
   </fmt:formatNumber></strong><br /><br />
</c:if>
 </c:otherwise>
</c:choose>  
</div>
<!-- 
//end 
-->

