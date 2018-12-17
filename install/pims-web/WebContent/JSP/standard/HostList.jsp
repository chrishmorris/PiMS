<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: Susy Griffiths
Date: 26-Apr-2009
Servlets: 

-->
<%-- bean declarations --%>
<jsp:useBean id="hostBeans" scope="request" type="java.util.Collection<HostBean>" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Host List" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
	<table border="0" class="list" >
	 <tr><th colspan="3" id="ftable">Hosts</th></tr>
 	 <tr><th>&nbsp;</th><th>Name</th><th class="sortable">Organism</th></tr>
  	 <c:forEach var="hostBean" items="${hostBeans}" varStatus="status" >
   	   <tr>
     	<%--TODO HostBean needs to extend ModelObjectShortBean
     	    
    <td><pimsWidget:link bean="${hostBean}"/></td>
     	 --%>  	 
  	 	<td><a href="${pageContext.request.contextPath}/View/${hostBean.hostHook}"
		    title="view <c:out value='${hostBean.hostName}' />">
		    View
		  </a></td>
  	 	<td><c:out value="${hostBean.hostName}" /></td><td><c:out value="${hostBean.hostOrganism}" /></td></tr>
     </c:forEach>
	</table>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
