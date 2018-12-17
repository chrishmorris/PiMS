<%@ page contentType="application/json; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: July 2012
Servlets: AjaxUpdate
--%>
<c:catch var="error">
<jsp:useBean id="changed" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectUpdateBean>" /> 

<c:set var="numChanged"  value="${fn:length(changed)}" />
{ 
<c:forEach var="bean" items="${changed}" varStatus="counter" >
    '${bean.hook}' : {
    <c:set var="numInBean"  value="${fn:length(bean.changed)}" />
    <c:forEach var="attr" items="${bean.changed}" varStatus="counter2" >
      '${attr.key}' : '${attr.value}' <c:if test="${counter2.count < numInBean}">,</c:if> 
    </c:forEach>
    } <c:if test="${counter.count < numChanged}">,</c:if> 
</c:forEach>
}

</c:catch><c:if test="${error != null}">"/>
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    throw "<error/> <c:out value='${error}'/>"
</c:if>
