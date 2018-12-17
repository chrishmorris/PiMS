<%--
Provide links to create subtypes,
after the user has tried to create an abstract type.
This JSP is called by org.pimslims.servlet.CustomCreate

Author: Chris Morris
Date: Jan 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="metaClass" scope="request" type="org.pimslims.metamodel.MetaClass" />
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Create a ${metaClass.alias}' />
</jsp:include>

<!-- OLD -->
<c:forEach items="${metaClass.subtypes}" var="subclass">
<a   
href="../Create/${subclass.metaClassName}?${pageContext.request.queryString}">
Create a new ${subclass.alias}</a><br />
</c:forEach>
<jsp:include page="/JSP/core/Footer.jsp" />
