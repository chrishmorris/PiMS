<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: Marc Savitsky
Date: 11 March 2008
Servlets: 
--%>

<jsp:useBean id="imageModel" scope="request" type="org.pimslims.presentation.barcodeGraph.BarcodeGraphNode" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='${headerTitle}' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>


<br />

<table cellspacing="4" cellpadding="4" border="4" id="layout">  
<tr>      
<td style="text-align:center;">
<a href="${pageContext.request.contextPath}/View/${imageModel._Hook}" style="text-decoration:none;">
<img src="${pageContext.request.contextPath}/read/BarcodeServlet?data=<c:out value="${imageModel.label}" />&type=Code128B&height=40" alt="<c:out value="${imageModel.label}"/>" style="border:none;" />
<br /><c:out value="${imageModel.label}" />
</a>
</td>
</tr>

<c:if test="${not empty imageModel.children}">
       
	<c:forEach items="${imageModel.children}" var="childNode">
	<tr>
	<td> </td>
	</tr>	
	<tr>      
	<td style="text-align:center;">
	<a href="${pageContext.request.contextPath}/View/${childNode._Hook}" style="text-decoration:none;">
	<img src="${pageContext.request.contextPath}/read/BarcodeServlet?data=<c:out value="${childNode.label}"/>&type=Code128B&height=40" alt="<c:out value="${childNode.label}"/>" style="border:none;" />
	<br /><c:out value="${childNode.label}"/>
	</a>
	</td>
	</tr>

    <c:if test="${not empty childNode.children}">
    
        <tr>
	    <td> </td>
		</tr>
	
    	<tr>
		<c:forEach items="${childNode.children}" var="grandChildNode">	  
		<td style="text-align:center;">
		<a href="${pageContext.request.contextPath}/View/${grandChildNode._Hook}" style="text-decoration:none;">
		<img src="${pageContext.request.contextPath}/read/BarcodeServlet?data=<c:out value="${grandChildNode.label}"/>&type=Code128B&height=40" alt="<c:out value="${grandChildNode.label}"/>" style="border:none;" />
		<br /><c:out value="${grandChildNode.label}"/>
		</a>
		<c:if test="${not empty grandChildNode.children}">
    
			<c:forEach items="${grandChildNode.children}" var="greatgrandChildNode">
			<br />	
			<a href="${pageContext.request.contextPath}/View/${greatgrandChildNode._Hook}" style="text-decoration:none;">  
			<img src="${pageContext.request.contextPath}/read/BarcodeServlet?data=<c:out value="${greatgrandChildNode.label}"/>&type=Code128B&height=40" alt="<c:out value="${greatgrandChildNode.label}"/>" style="border:none;" />
			<br /><c:out value="${greatgrandChildNode.label}"/>
			</a>
			</c:forEach>
 
    	</c:if>
		
		</td>
		</c:forEach>
		</tr>    
    
    </c:if>
	
	</c:forEach> 
</c:if>

</table>
</div>
</div>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
</body>
</html>

