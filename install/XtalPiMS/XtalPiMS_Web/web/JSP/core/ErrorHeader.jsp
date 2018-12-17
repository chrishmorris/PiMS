<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Robust JSP, capable of displaying a message even after a severe error --%>
<%-- cannot use header <c:catch>
<jsp:include page="/header" />
</c:catch>ignore errors, we are already in error recovery --%>
<html><head>
<style>

/* Menu styles */

ul.menu   { list-style:none; margin:0;padding:0;  font-size:smaller; z-index:9; display:inline;  background-color:#003; }
ul.menu li a:link, ul.menu li a:hover, ul.menu li a:visited { color:#fff; 
    text-decoration:none; width:100%;  padding:2px 10px 2px 10px; width:auto; }
ul.menu li a:hover { background-color:#ccf; color:#006; }
ul.menu li { font-family:arial,helvetica,sans-serif; display:inline; position:relative;top:0; font-weight:bold; }

</style>
</head><body>
<ul class="menu">
  
  <!-- PiMS Home -->
  <li id="menu_0"> <a  href="${pageContext.request.contextPath}/">Home</a>
  </li>

  <!-- login/logout -->
  <li id="menu_1" >
    <c:choose>
      <c:when test="${empty pageContext.request.remoteUser}">
        <a  href="${pageContext.request.contextPath}/Login" id="logInOutLink">Log in</a>
      </c:when><c:otherwise>
        <a  href="${pageContext.request.contextPath}/public/Logout" id="logInOutLink">Log out ${pageContext.request.remoteUser}</a>
      </c:otherwise>
    </c:choose>
  </li>
</ul>
<h1><c:out value="${param['HeaderName']}" /></h1>
</body></html>
