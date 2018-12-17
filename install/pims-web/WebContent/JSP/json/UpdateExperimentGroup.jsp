<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: 19 Feb 2008
obsolete
--%>
// obsolete
<c:catch var="error">
<jsp:useBean id="experiments" scope="request" type="java.util.Collection<org.pimslims.presentation.ExperimentUpdateBean>" /> 

<c:set var="numExpts"  value="${fn:length(experiments)}" />

{ updated:{ experiments:[
<c:forEach var="ex" items="${experiments}" varStatus="exptCounter">
<c:set var="numInputs"  value="${fn:length(ex.updatedInputSamples)}" />
<c:set var="numOutputs" value="${fn:length(ex.updatedOutputSamples)}" />
<c:set var="numParams"  value="${fn:length(ex.updatedParameters)}" />
    { 
        name:"<c:out value='${ex.name}' />", 
        dbId:"<c:out value='${ex.dbId}' />",         
        <c:forEach var="entry" items="${ex.changed}">
            ${entry.key}: "<c:out value='${entry.value}' />",
        </c:forEach>

        params:{<c:forEach var="p" items="${ex.updatedParameters}" varStatus="paramCounter">
            pd<c:out value='${p.definitionDbId}' />: "<c:out value='${p.value}' />"<c:if test="${paramCounter.count < numParams}">,</c:if> 
        </c:forEach>},        
        
        inputs:{ <c:forEach var="is" items="${ex.updatedInputSamples}" varStatus="inputCounter">
            is${is.refInputSampleDbId}:{
                htmlid: "is<c:out value='${is.refInputSampleDbId}' />", 
                name:"<c:out value='${is.sampleName}' />", 
                hook:"<c:out value='${is.sampleHook}' />", 
                amount:"<c:out value='${is.amount}' />" 
            }<c:if test="${inputCounter.count < numInputs}">,</c:if> 
        </c:forEach>},
        
        outputs:{ <c:forEach var="os" items="${ex.updatedOutputSamples}" varStatus="outputCounter">
            os${ex.refOutputSampleDbId}:{ 
                htmlid: "os${ex.refOutputSampleDbId}",
                name:"<c:out value='${ex.outputSampleName}' />", 
                hook:"${ex.outputSampleHook}", 
                amount:"" 
            }<c:if test="${outputCounter.count < numOutputs}">,</c:if> 
        </c:forEach>}
        

    }<c:if test="${exptCounter.count < numExpts}">,</c:if>
</c:forEach>
]
}
}

</c:catch><c:if test="${error != null}">"/>
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    throw "<error/> <c:out value='${error}'/>"
</c:if>

