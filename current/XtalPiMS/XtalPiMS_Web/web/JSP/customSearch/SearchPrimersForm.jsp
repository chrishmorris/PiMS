<%--
Author: Petr Troshin aka pvt43
Date: 8 May 2008
Servlets: This is to be included in the form part of CustomSearch.jsp

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>

<c:if test="${!empty param.includeStAttr}">
	<c:import url="StAttributes.jsp"/>
</c:if>

	<tr>
	   <td>Primer Name</td>
	   <td><input name="name" value="<c:out value="${criteria['name']}" />" /></td>
	</tr>
