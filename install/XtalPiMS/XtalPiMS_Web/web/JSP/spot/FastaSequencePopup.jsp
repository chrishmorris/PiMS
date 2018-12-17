<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Continuation of PIMS-3435 to replace SpotPopupFastaSeq.jsp and which fails in IE
if the sequence is >1024 characters in length
Displays the sequence in Fasta format.
Author: susy
Date: 5 Nov 2010
Servlets: org.pimslims.servlet.spot.FastaSequencePopup

--%>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>FASTA sequence popup</title>
    <link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/construct.css" type="text/css" />
</head>
<body style="font-family:'Courier New',monospace; font-size:80%; ">

<%-- bean declarations --%>
<jsp:useBean id="sequence" scope="request" type="java.lang.String" />
<jsp:useBean id="fastaHeader" scope="request" type="java.lang.String" />
<jsp:useBean id="fastaSequence" scope="request" type="java.util.List" />
<jsp:useBean id="seqLen" scope="request" type="java.lang.Integer" />

<c:catch var="error">

<%-- page body here --%>
<c:choose>
    <c:when test="${empty fastaSequence}">
        <h3 style="font-family:verdana,arial,helvetica,sans-serif; color:#000099;">No sequence to display</h3>
    </c:when>
    <c:otherwise>
        <div class="seqPopup"> 
            &gt;<c:out value="${fastaHeader}"  /><br />    
            <c:forEach items="${fastaSequence}" var="seq" varStatus="status" >
                <c:out value="${seq}"/><br />
            </c:forEach> 
        </div>
    </c:otherwise>
</c:choose>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
</body>