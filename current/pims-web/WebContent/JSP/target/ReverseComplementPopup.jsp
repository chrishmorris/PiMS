<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: susy
Date: 15 Oct 2010
Servlets: ReverseComplementPopup

--%>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Reverse Complement</title>
</head>
<body style="font-family:'Courier New',monospace; font-size:80%; ">
    <%-- bean declarations  --%>
    <jsp:useBean id="rcChunks" scope="request" type="java.util.List<java.lang.String>" />
    <jsp:useBean id="targName" scope="request" type="java.lang.String" />


<c:catch var="error">

<%-- page body here --%>
<%--ReverseComplement.jsp
    To display the Reverse complement of the Target DNA sequence
 --%>
<h3 style="font-family:verdana,arial,helvetica,sans-serif; color:#000099;">Reverse Complement DNA sequence for Target: <c:out value="${targName}" /></h3>
    <c:forEach items="${rcChunks }" var="chunk" varStatus="status" >
       <c:out value="${fn:replace(chunk,' ','')}" /><br />       
    </c:forEach>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
</body>