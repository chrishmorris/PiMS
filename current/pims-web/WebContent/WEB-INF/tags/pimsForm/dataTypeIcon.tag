<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="datatype" required="false" %>

<c:set var="longtype" value="Long" scope="page" />
<c:set var="doubletype" value="Double" scope="page" />
<c:set var="floattype" value="Float" scope="page" />
<c:set var="stringtype" value="String" scope="page" />
<c:set var="booleantype" value="Boolean" scope="page" />
<c:set var="datetimetype" value="DateTime" scope="page" />
<c:set var="intervaltype" value="Interval" scope="page" />

<c:if test="${!empty datatype}">
<c:choose>
	<c:when test="${datatype=='Int' || datatype=='Integer' || type==longtype }">
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/numbericon.gif" />     
	</c:when>
	<c:when test="${datatype==floattype || datatype==doubletype}">
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/numbericon.gif" /> 
	</c:when>	
	<c:when test="${datatype==stringtype}">
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/stringicon.gif" /> 
	</c:when>	 
	<c:when test="${datatype==booleantype}">
	    <img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/booleanicon.gif" /> 
	</c:when>
	<c:otherwise>
		<img src="${pageContext.request.contextPath}/skins/default/images/icons/plateexperiment/blankicon.gif" /> 
	</c:otherwise>
</c:choose>
</c:if>