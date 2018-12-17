<%--
Display a list of Database references

Author: Peter Troshin 
Date: December 2007
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%--Caller must provide this --%>
<jsp:useBean id="results" scope="request" type="java.util.Collection" />


<%--Display the content of a table --%>
<!-- ListDBRefs.jsp -->
	<table>
	<tr>
	<th>Database</th>
	<th>Accession</th>
	<th>Type</th>
	<th>Sequence release</th>
    <th>&nbsp;</th>
	</tr>
			<c:forEach items="${results}" var="mObj"	varStatus="status2">
				<tr class="ajax_deletable" id="${mobj.hook}">
				<td>${mObj.name}</td>	
				<td>
				 <c:choose>
					 <c:when test="${empty mObj.link}">
						<c:out value="${mObj.accession}" />
   				 </c:when>
				   <c:otherwise>
					   <a target="_blank" href="${fn:escapeXml(mObj.link)}"><c:out value="${mObj.accession}" /></a>
				   </c:otherwise>
				</c:choose>
				</td>
				<td><c:out value="${mObj.type}" /></td>
                <td><c:out value="${mObj.release}" /></td>
                <td><pimsWidget:deleteLink bean="${mObj}" /></td>
  			</tr>
			</c:forEach>
</table>

<!-- /ListDBRefs.jsp -->

<!-- OLD -->
