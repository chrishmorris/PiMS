<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: cm65
Date: 19 Feb 2008

--%>
// obsolete start PlateExperimentEditProtol.jsp
<c:catch var="error">
<jsp:useBean id="protocolBean" scope="request" type="org.pimslims.presentation.protocol.ProtocolBean" /> 
groupParameters:{
},
setupParameters:{
<c:forEach var="entry" items="${protocolBean.setupParameterDefinitions}" varStatus="status">
pd<c:out value='${entry.dbId}' />:
<c:choose><c:when test="${fn:startsWith(entry.name, '__')}" >
{	name: "<c:out value='${entry.label}' />",
</c:when><c:otherwise>
{	name: "<c:out value='${entry.name}' />",
</c:otherwise></c:choose>
    hook: "<c:out value='${entry.hook}' />",
    type: "<c:out value='${entry.paramType}' />",
    unit: "<c:out value='${entry.unit}' />"
}<c:if test="${!status.last}">,</c:if>
</c:forEach>},
resultParameters:{
<c:forEach var="entry" items="${protocolBean.resultParameterDefinitions}" varStatus="status">
pd<c:out value='${entry.dbId}' />:
<c:choose><c:when test="${fn:startsWith(entry.name, '__')}" >
{	name: "<c:out value='${entry.label}' />",
	id: "<c:out value='${entry.name}' />",
	possibleValues:  ${entry.possibleValuesAsJavascriptArray},
</c:when><c:otherwise>
{	name: "<c:out value='${entry.name}' />",
</c:otherwise></c:choose>
    hook: "<c:out value='${entry.hook}' />",
    type: "<c:out value='${entry.paramType}' />",
    unit: "<c:out value='${entry.unit}' />"
}<c:if test="${!status.last}">,</c:if>
</c:forEach>},
inputSamples:{
<c:forEach var="entry" items="${protocolBean.inputSamples}" varStatus="status">
is<c:out value='${entry.dbId}' />:
{   name: "<c:out value='${entry.name}' />",
    hook: "<c:out value='${entry.hook}' />",
    sampleCategoryName: "<c:out value='${entry.sampleCategory.name}' />",
    sampleCategoryHook: "<c:out value='${entry.sampleCategory.hook}' />",
    displayUnit: "<c:out value='${entry.displayUnit}' />"
}<c:if test="${!status.last}">,</c:if>
</c:forEach>},
outputSamples:{
<c:forEach var="entry" items="${protocolBean.outputSamples}" varStatus="status">
os<c:out value='${entry.dbId}' />:
{   name: "<c:out value='${entry.name}' />",
    hook: "<c:out value='${entry.hook}' />",
    sampleCategoryName: "<c:out value='${entry.sampleCategory.name}' />",
    sampleCategoryHook: "<c:out value='${entry.sampleCategory.hook}' />",
    displayUnit: "<c:out value='${entry.displayUnit}' />"
}<c:if test="${!status.last}">,</c:if>
</c:forEach>}
</c:catch><c:if test="${error != null}"> 
    <%-- The reason for the fake tag is help RandomGet recognise the error --%>
    }; /*<error/> */ throw " <c:out value='${error}'/>"; {
</c:if>

// end PlateExperimentEditProtol.jsp
