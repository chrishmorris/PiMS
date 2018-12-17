<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!-- oppfPurificationParams.jsp -->
<jsp:useBean id="parameters" scope="request" type="java.util.List<org.pimslims.model.experiment.Parameter>" />

<c:set var="longtype" value="Long" scope="page" />
<c:set var="doubletype" value="Double" scope="page" />
<c:set var="floattype" value="Float" scope="page" />
<c:set var="stringtype" value="String" scope="page" />
<c:set var="booleantype" value="Boolean" scope="page" />
<c:set var="datetimetype" value="DateTime" scope="page" />
<c:set var="intervaltype" value="Interval" scope="page" />
<!-- oppf/oppfPurificationParams.jsp -->
<script type="text/javascript">

var volume=new Array(2);
var protein=new Array(2);
var yield=new Array(2);
var yieldName=new Array(2);

<c:forEach items="${parameters}" var="parameter" varStatus="prStatus">
<c:choose>
<c:when test="${fn:contains(parameter.name, 'Volume of Pool Pool 1')}">
			    volume[0]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, 'Volume of Pool Pool 2')}">
			    volume[1]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, 'Volume of Pool Pool 3')}">
			    volume[2]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, '[Protein of Pool] Pool 1')}">
			    protein[0]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, '[Protein of Pool] Pool 2')}">
			    protein[1]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, '[Protein of Pool] Pool 3')}">
			    protein[2]=<c:out value="${parameter.value}" />
</c:when>
<c:when test="${fn:contains(parameter.name, 'Yield Pool 1')}">
			    yieldName[0]='<c:out value="${parameter._Hook}:value" />'
</c:when>
<c:when test="${fn:contains(parameter.name, 'Yield Pool 2')}">
			    yieldName[1]='<c:out value="${parameter._Hook}:value" />'
</c:when>
<c:when test="${fn:contains(parameter.name, 'Yield Pool 3')}">
			    yieldName[2]='c:out value="${parameter._Hook}:value" />' 
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>
</c:forEach>

function updatePool(name, value) {
  
  if (name.match("Volume of Pool")) {
   	var pool = name.charAt(name.length-1);
   	//alert("Volume of Pool "+pool+" ["+value+"]");
   	volume[pool-1]=value;
  }
  
  if (name.match("Protein of Pool")) {
    var pool = name.charAt(name.length-1);
   	//alert("Protein of Pool "+pool+" ["+value+"]");
   	protein[pool-1]=value;
  }
  
  calculateYield();
}

function calculateYield() {
  var elements = document.getElementsByTagName('input');
    
  for(i=0;i<3;i++) {
    if (!isNaN(protein[i]*volume[i])) {
      for (var j = 0; j < elements.length; ++j) {
        var element = elements[j];
        if (element.name == yieldName[i]) {
         	element.value = (protein[i]*volume[i]).toFixed(2);
         	var yieldspan=document.getElementById(yieldName[i]);
         	yieldspan.innerHTML=(protein[i]*volume[i]).toFixed(2);
        }
      }
    }
  }
}

</script>


<h4 class="printonly">Conditions and Results </h4>
<c:choose><c:when test="${empty parameters}">
    No parameters have been defined
</c:when></c:choose>

<c:set var="halfway">0</c:set>
<c:forEach items="${parameters}" var="parameter">
	<c:set var="halfway" value="${1+halfway}" />
	<c:if test="${parameter.paramType==booleantype}">
			<c:set var="halfway" value="${1+halfway}" />
	</c:if>
</c:forEach>
<c:set var="halfway" value="${halfway/2}" />
<c:set var="blocksWritten">0</c:set>

<%--
	<c:if test="${!empty validation}"><script type="text/javascript">alert('${validation}')</script></c:if>
--%>

<c:set var="col1" value=""/>
<c:set var="col2" value=""/>

<%-- single column
<pimsForm:formBlock>
<c:forEach items="${parameters}" var="parameter">
<%@include file="/JSP/oppf/oppfpurification_parameter.jsp"%>
</c:forEach>
</pimsForm:formBlock>
--%>

<c:forEach items="${parameters}" var="parameter">

	<c:choose>
	<c:when test="${blocksWritten < halfway}">
		<c:set var="col1">${col1}<%@include file="/JSP/oppf/oppfpurification_parameter.jsp"%></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="col2">${col2}<%@include file="/JSP/oppf/oppfpurification_parameter.jsp"%></c:set>
	</c:otherwise>
	</c:choose>
</c:forEach>

<pimsForm:formBlock>
<pimsForm:column1>${col1}</pimsForm:column1>
<pimsForm:column2>${col2}</pimsForm:column2>
</pimsForm:formBlock>

<pimsForm:editSubmit />


<script type="text/javascript">
calculateYield();
</script>


<!-- /oppfPurificationParams.jsp -->
