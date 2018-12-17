<%--
The purpose of this page is to display one message for the user.
Use it if something goes wrong, to pass the message for the user 

Author: Petr Troshin
Date: May 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="message" scope="request" type="java.lang.String" />
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='A problem...' />
</jsp:include>


<div style="border:1px dotted red; padding:15px; background-color:white; font-family: sans-serif; ">
<p>Sorry, PIMS cannot perform the action you have requested.
Please find the reason for this below.
if you think this is an error please pass this message to the PIMS developers.</p>

<p style="font-family: sans-serif; font-weight: bold;">
${message}
</p>
</div>
<jsp:include page="/JSP/core/Footer.jsp" />
