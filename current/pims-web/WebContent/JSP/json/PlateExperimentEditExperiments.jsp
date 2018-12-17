<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: 19 Feb 2008
Servlets: UpdateExperimentGroup
--%>

// start of PlateExperimentEditExperiments.jsp
<c:catch var="error">
<jsp:useBean id="plateExperimentBeans" scope="request" type="java.util.Collection<org.pimslims.presentation.plateExperiment.PlateExperimentBean>" /> 

<c:forEach var="ex" items="${plateExperimentBeans}" varStatus="status" >
ex<c:out value='${ex.dbId}' />:
{   name:"<c:out value='${ex.name}' />"
    , hook:"<c:out value='${ex.hook}' />"
    , dbId:"<c:out value='${ex.dbId}' />"
    , status:"<c:out value='${ex.status}' />"
    , blueprintName:"<c:out value='${ex.bluePrintName}' />"
    , blueprintHook:"<c:out value='${ex.bluePrintHook}' />"
    , pcrProductSize:"<c:out value='${ex.pcrProductSize}' />"
    , proteinMW:"<c:out value='${ex.proteinMW}' />"
    , milestoneAchieved:"<c:out value='${ex.hasMilestone}' />"
    <c:forEach var="is" items="${ex.inputSampleBeans}">
    , is<c:out value='${is.refInputSampleDbId}' />:
    {	name: "<c:out value='${is.sampleName}' />",
        hook: "<c:out value='${is.sampleHook}' />",
    	amount: "<c:out value='${is.amount}' />"
    }</c:forEach>
    <c:forEach var="os" items="${ex.outputSampleBeans}">
    , os<c:out value='${os.refOutputSampleDbId}' />:
    {	name: "<c:out value='${os.sampleName}' />",
       	hook: "<c:out value='${os.sampleHook}' />"
    }</c:forEach>      
    <c:forEach var="pd" items="${ex.parameterBeans}" varStatus="xstatus">
    , pd<c:out value='${pd.definitionDbId}' />: "<c:out value='${pd.value}' />"<c:if test="${!xstatus.last}"></c:if>
    <c:if test="${pd.isScoreParameter}">
    , score: "<c:out value='${pd.intValue}' />"</c:if>
    </c:forEach>     
}<c:if test="${!status.last}">,</c:if>
</c:forEach>
</c:catch><c:if test="${error != null}">
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    }; /* <error/>*/throw " <c:out value='${error}'/>"; {
</c:if>
// end of PlateExperimentEditExperiments.jsp
