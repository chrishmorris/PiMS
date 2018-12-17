<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: susy
Date: 13 Oct 2010
Servlets: ComapreTargetSequences.java

--%>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <link rel="stylesheet" href="${pageContext.request['contextPath']}/skins/default/css/construct.css" type="text/css" />
        
  <title>Target Sequence alignment</title>
</head>
<body>
<%-- bean declarations --%>
       <jsp:useBean id="seqAli" scope="request" type="java.lang.String[]" />
       <jsp:useBean id="lengthTransSeq" scope="request" type="java.lang.Integer" />
       <jsp:useBean id="protSeqLen" scope="request" type="java.lang.Integer" />

<c:catch var="error">

<%-- page body here --%>
<%--CompareTargetSequences.jsp
To display the alignment between the Target protein sequence and the Translated DNA sequence
 --%>

<h3 style="font-family:verdana,arial,helvetica,sans-serif; color:#000099;">Alignment details:</h3>
    <strong>Query: &nbsp;</strong>Translated DNA sequence length: <c:out value="${lengthTransSeq}" /> &nbsp;
    <strong>Target: </strong>Target Protein sequence length: <c:out value="${protSeqLen}"/>

    <c:forEach items="${seqAli}"  var="al" varStatus="status">
        <c:set var="align" value="${al}" scope="request"/>
        <c:set var="status" value="${status}" scope="request"/>
        <c:choose>
            <c:when test="${status.count%2==0}">
                <%-- This is to prevent browsers to squash empty div and broke even formatting --%>
                <c:if test="${empty fn:trim(al)}">
                    <c:set var="space" value="&nbsp;"/>
                </c:if>
                <pre class="blastPattern" ><c:out value="${al}" /></pre>
            </c:when>
            <c:otherwise>
                <div style="margin: 1em 0 1em 0;" class="blastSequence"><pre>${al}</pre></div>
            </c:otherwise>
        </c:choose>
        <c:remove var="space" />
    </c:forEach>
    <c:remove var="align" />
    <c:remove var="status" />
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
</body>