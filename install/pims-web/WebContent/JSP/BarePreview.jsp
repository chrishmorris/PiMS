<%--

Author: Petr Troshin
Date: December 2007
Preview biological entry as clear text
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="View the record from ${database} with accession ${dbid}" />
</jsp:include>

<!-- OLD -->

<div  style="width: 50em; text-align: left; background-color:white; ">
<pre>
<c:out value="${record}"/>   
</pre>

</div> 

<jsp:include page="/JSP/core/Footer.jsp" />
