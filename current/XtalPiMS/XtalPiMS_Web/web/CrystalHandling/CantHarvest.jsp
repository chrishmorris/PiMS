<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Not allowed to view crystal mounting/treatment
Author: Ed Daniel
Date: April 2012
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="No permissions" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Can't view or modify crystal treatment"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<pimsWidget:box title="More information" initialState="fixed" extraClasses="noscroll" extraHeader="">
<h2>You don't have the right permissions</h2>
<p>You need to be logged in as a user with: 
<ul>
<li>"create" permissions in at least one PiMS lab notebook, and</li>
<li>"update" permissions on the lab notebook of the trials plate.</li>
</ul> 

View-only accounts are allowed to view crystal images but not crystal harvesting and treatment; if you are using a view-only account, you need 
your own account with the proper permissions.</p>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<jsp:include page="/JSP/core/Footer.jsp" />