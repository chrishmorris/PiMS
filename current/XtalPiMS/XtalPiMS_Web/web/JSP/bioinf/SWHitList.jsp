<%--
Author: Peter Troshin, STFC Daresbury Laboratory
Date: September 2007
--%>

<%@ page contentType="text/html; charset=utf-8" language="java" import="org.pimslims.bioinf.*, org.pimslims.bioinf.local.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:catch var="error">
<jsp:useBean id="alignments" scope="request" type="java.util.Collection<org.pimslims.bioinf.local.PimsAlignment>" />

       <tr>
    
   	<td><label title="Click to view full alignment"><a href="#align${status.count}">${status.count}</a></label></td>
		<td><label title="Click to view the molecule"><a href="${pageContext.request.contextPath}/View/${ali.hook}">${ali.hitName}</a></label></td>
		<td>
			<c:choose>
			<c:when test="${fn:startsWith(ali.linkedHook, 'org.pimslims.model.sample.Sample:')}"> 
				<a id="linkedHit${ali.hook}" href="${pageContext.request.contextPath}/ViewPrimer/${ali.linkedHook}">${ali.compTypeDecorated}</a>&nbsp;${ali.type}
			</c:when>
			<c:otherwise>
				<a id="linkedHit${ali.hook}" href="${pageContext.request.contextPath}/View/${ali.linkedHook}">${ali.compTypeDecorated}</a>&nbsp;${ali.type}
			</c:otherwise>
			</c:choose>
		</td>
		<td>${ali.templateLength}</td>
		<td>${ali.score}</td>
		<td><fmt:formatNumber type="Number" maxFractionDigits="2">
	    ${ali.percentIdentity}
	    </fmt:formatNumber></td>
		<td><fmt:formatNumber type="Number" maxFractionDigits="2">
	    ${ali.percentGaps}
	    </fmt:formatNumber></td>
	  </tr>
<%-- Cannot add tooltips here inside the table
	thus, collect them to add later --%>  
<c:set var="hitTabletooltipJS" scope="request"> 
${hitTabletooltipJS}
Element.observe(window,"load",function(){
  addTooltip("linkedHit${ali.hook}","Related ${ali.compTypeDecorated}","Click to view related ${ali.compTypeDecorated}&nbsp;${ali.linkedName}");
});
</c:set>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if> 

<!-- OLD -->
