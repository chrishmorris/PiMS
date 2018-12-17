<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: cm65
Date: 7 Jul 2009
Servlets: 

-->
<%-- bean declarations e.g.:
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />
<jsp:useBean id="constructBeans" scope="request"
type="java.util.Collection<ConstructBean>" />
--%>

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Vectors' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<pimsWidget:box title="Vectors"  initialState="open" >
    <jsp:include page="/read/VectorList" />
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
</template></templates>