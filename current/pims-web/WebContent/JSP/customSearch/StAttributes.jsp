
<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%--
Author: Petr Troshin pvt43
Date: 9 May 2008
Servlets: Generate the list of standard fields on the object
Used in conjunction with CustomSearch.jsp page by conditional include

--%>
<c:forEach items="${searchAttributes}" var="attribute">
	  <!-- no null attributes -->
	  <c:if test="${!empty searchMetaClass.attributes[attribute]}">
      	<c:if test="${!searchMetaClass.attributes[attribute].hidden}">
      	<tr>
        	<td>
        	</td>
        	<td>
         		<input name="${attribute}" value="<c:out value='${criteria[attribute]}' />" />
        	</td>
      	</tr>
      	</c:if>
      </c:if>
</c:forEach>

<!-- OLD -->
