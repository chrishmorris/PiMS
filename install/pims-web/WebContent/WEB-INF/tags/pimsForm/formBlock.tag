<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@attribute name="id" required="false"  %>
<%@attribute name="extraClasses" required="false"  %>

<c:set var="idInsert" value="" />
<c:if test="${!empty id}"><c:set var="idInsert" value="id=\"${id}\"" /></c:if>

<c:set var="element" value="div" />
<c:if test="${_MHTML }"><c:set var="element" value="table" /></c:if>
<${element } class="formblock ${extraClasses}" ${idInsert}>
<jsp:doBody/>
<div class="shim">&nbsp;</div>
</${element }>