<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
PIMS-3435 to replace DNAandProtSeqPopup.jsp and DNAandProtSeqView which fail in IE
if the DNA sequence is >1024 characters in length
Displays alternating rows of DNA and its translated protein sequence
generated from a DNA sequence.
 
Author: susy
Date: 2 Nov 2010
Servlets: DNAandProtSeqPopup.java

--%>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Target sequences popup</title>
    <link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/construct.css" type="text/css" />
</head>
<body style="font-family:'Courier New',monospace; font-size:80%; ">

<%-- bean declarations --%>
<jsp:useBean id="protId" scope="request" type="java.lang.String" />
<jsp:useBean id="dnaAndProtSeq2" scope="request" type="java.util.List<java.lang.String>" />
<jsp:useBean id="seqLen" scope="request" type="java.lang.Integer" />

<c:catch var="error">

<%-- page body here --%>
<%--DNAandProtSeqPopup2.jsp
    To display the TargetDNA sequence and translated protein sequence
 --%>

<%-- page body here --%>
<h3 style="font-family:verdana,arial,helvetica,sans-serif; color:#000099;">DNA and translated protein sequence for: ${protId}</h3>
<c:choose>
 <c:when test="${empty dnaAndProtSeq2}" >
    <c:out value="Target has no DNA sequence" />
 </c:when>
 <c:otherwise>
    <div class="seqPopup">
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

<%-- now show the tail end of the sequence
?Not needed? --%>
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
  </c:otherwise>
 </c:choose>
<!-- 
//end 
-->

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
</body>