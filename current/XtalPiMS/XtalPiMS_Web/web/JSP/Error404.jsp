<%-- 
Custom 404 page

This is configured as the Tomcat error page for error 404.
Callers can also dispatch to it. They should provide
the original URI of the request, e.g. by:
    request.getRequestDispatcher("/JSP/Error404.jsp"+request.getRequestURI()).forward(request, response);
See PIMSServlet.getRequiredObject()

Note that ${pageContext.request.requestURI} is JSP/Error4o4.jsp
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Page not found: ' />
</jsp:include>      


<!--friendly bit-->
<div style="border:1px solid blue;padding:15px;background-color:#ccf">
<p>PIMS is unable to show you the requested page <em>${requestScope["javax.servlet.forward.request_uri"]}</em>.
This could be because it doesn't exist, or because you don't have permission to see it.</p>
<p>Alternatively, it could be an error in PIMS. 
If so, it would be most helpful if you could paste the developer information below into an email 
and send it to pims-defects@dl.ac.uk 
- and, if possible, tell us what you were doing just before this happened. 
This will help us determine what went wrong, so that we can fix it.</p>
</div><br/>

<!--Dev info-->        
<span style="color:blue; font-size:large; font-weight:bold; font-family:arial,helvetica,sans-serif">Developer information</span>
<table style="border:1px solid blue;font-size:smaller;padding:5px">
  <tr><td>Request URI</td><td>${requestScope["javax.servlet.forward.request_uri"]}</td></tr>
  <tr><td>Included</td><td>${requestScope["javax.servlet.include.request_uri"]}</td></tr>
  <tr><td>Previous page</td><td>${header['Referer']}</td></tr>
  <tr><td>Hook</td><td>${hook}</td></tr>
  <tr><td>User</td><td>${pageContext.request['remoteUser']}</td></tr>
  <tr><td>Method</td><td>${pageContext.request['method']}</td></tr>
  <tr><td>Server name</td><td>${pageContext.request['serverName']}</td></tr>
  <tr><td>Path Info</td><td>${pageContext.request['pathInfo']}</td></tr>
  <tr><td>Query string</td><td>${pageContext.request['queryString']}</td></tr>
  <c:forEach var="headerName" items="${pageContext.request['headerNames']}">
    <tr><td>${headerName}</td><td>${header[headerName]}</td></tr>
  </c:forEach>
</table>
<jsp:include page="/JSP/core/Footer.jsp" />
