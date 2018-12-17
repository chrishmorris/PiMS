<%--
Writes Javascript arrays containing the names of all input samples, output samples
and parameters for the protocol, and displays the names in a box for the user.

Author: Ed Daniel
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<script type="text/javascript">
var inputNames=new Array();
var paramNames=new Array();
var outputNames=new Array();
<c:forEach var="is" items="${protocol.inputSamples}">inputNames.push("<c:out value="${is.name}"/>"); </c:forEach> 
<c:forEach var="pd" items="${protocol.parameterDefinitions}">paramNames.push("<c:out value="${pd.name}"/>"); </c:forEach> 
<c:forEach var="os" items="${protocol.outputSamples}">outputNames.push("<c:out value="${os.name}"/>"); </c:forEach> 
</script>

<c:set var="inputs"  value="" />
<c:set var="outputs" value="" />
<c:set var="params"  value="" />
<c:forEach var="is" items="${protocol.inputSamples}">
    <c:set var="inputs" value="${inputs}, ${is.name}"/>
</c:forEach> 
<c:forEach var="os" items="${protocol.outputSamples}">
    <c:set var="outputs" value="${outputs}, ${os.name}"/>
</c:forEach> 
<c:forEach var="pd" items="${protocol.parameterDefinitions}">
    <c:set var="params" value="${params}, ${pd.name}"/>
</c:forEach> 
<c:set var="inputs"><c:out value="${fn:substringAfter(inputs, ', ')}" /></c:set>
<c:set var="outputs"><c:out value="${fn:substringAfter(outputs,', ')}" /></c:set>
<c:set var="params"><c:out value="${fn:substringAfter(params, ', ')}" /></c:set>

<c:set var="protocolName"><c:out value="${protocol.name}"/></c:set>

<hr/>
<h4>Parameter and sample names already used in this protocol</h4>
	<pimsForm:form action="#" method="get" mode="create">
		<pimsForm:formBlock>
		    <pimsForm:nonFormFieldInfo label="Input Samples">${inputs}</pimsForm:nonFormFieldInfo>
    		<pimsForm:nonFormFieldInfo label="Parameters">${params}</pimsForm:nonFormFieldInfo>
    		<pimsForm:nonFormFieldInfo label="Output Samples">${outputs}</pimsForm:nonFormFieldInfo>
		</pimsForm:formBlock>
	</pimsForm:form>

 
