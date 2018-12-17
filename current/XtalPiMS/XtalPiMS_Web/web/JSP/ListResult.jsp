<%--
Display all the protocols.
This JSP is called by org.pimslims.servlet.search.ListProtocol

Author: Chris Morris
Date: Jan 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page language="java" import="java.util.*" %>
<%@ page import="org.pimslims.metamodel.*" %>
<%@ page import="org.pimslims.servlet.*" %>
<%@ page import="org.pimslims.presentation.servlet.utils.*" %>
<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Search by Name" />
</jsp:include>


<TABLE cellpadding="0" width="100%" summary=" with their attributes">
    <tr class="rowHeader">
    <th>Results</th>
	</tr>

<%--Display the content of a table --%>
	<% String style = "listodd"; %>
    <c:forEach items="${results}" var="result">
        <tr class="<%=style%>">
					<td><pimsWidget:link bean="${result}" />
	    </tr>
        <% style = style.equals("listodd") ? "listeven" : "listodd";  %>
    </c:forEach>
</table>

<%-- LATER makeBreadCrumb(displayName+"s") --%>
<jsp:include page="/JSP/core/Footer.jsp" />

<!-- OLD -->
