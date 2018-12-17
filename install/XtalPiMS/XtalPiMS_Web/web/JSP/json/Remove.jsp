<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: Sept 2009
Servlets: AjaxRemove
--%>
<c:catch var="error">
<jsp:useBean id="labBookPage" scope="request" type="org.pimslims.presentation.ModelObjectShortBean" /> 
<jsp:useBean id="role" scope="request" type="java.lang.String" /> <%-- The role in the lab book page of the removed objects --%>
<jsp:useBean id="removed" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" /> 
<jsp:useBean id="associated" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectShortBean>" /> 

<c:set var="numRemoved"  value="${fn:length(removed)}" />
<c:set var="numAssociated"  value="${fn:length(associated)}" />
{ removed:{
    from: ${labBookPage.hook},
    role: ${role},
    removed: {<c:forEach var="rem" items="${removed}" varStatus="counter" >
        ${rem.hook}<c:if test="${counter.count < numRemoved}">,</c:if> 
    </c:forEach>}    
    associated: {<c:forEach var="assoc" items="${associated}" varStatus="counter" >
        ${assoc.hook}<c:if test="${counter.count < numAssociated}">,</c:if> 
    </c:forEach>}
  } 
}
</c:catch><c:if test="${error != null}">"/>
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    throw "<error/> <c:out value='${error}'/>"
</c:if>
