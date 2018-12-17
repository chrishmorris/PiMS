<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%--
The PiMS front page

Author: Chris Morris
Date: Jan 2006
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="PiMS ${PIMS_VERSION}" />
    <jsp:param name="bodyCSSClass" value="bricks" />
    <jsp:param name="extraStylesheets" value="custom/bricks5.0.0" />
    <jsp:param name="extraStylesheets_IE" value="custom/bricks-IE" />
</jsp:include>

<c:catch var="error">

<c:forEach items="${paramValues['_path']}" varStatus="status">
  <pimsWidget:brick row="${paramValues['_top'][status.index]}" column="${paramValues['_left'][status.index]}" 
   name="${paramValues['_path'][status.index]}" 
   height="${paramValues['_height'][status.index]}" width="${paramValues['_width'][status.index]}"  
  /> 
</c:forEach>

<%-- TODO make the page configurable by means of this form.
<form method="get" action="#">
  <c:forEach items="${paramValues['row']}" varStatus="status">
  <input name="_top" value="${paramValues['row'][status.index]}" />
   <input name="_left" value="${paramValues['col'][status.index]}"/>
   <input name="_path" value="${paramValues['name'][status.index]}"/>
   <input name="_height" value="${paramValues['height'][status.index]}"/> 
  /> </br>
</c:forEach>
<input type="submit" value="Reload" />
</form>
--%>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />