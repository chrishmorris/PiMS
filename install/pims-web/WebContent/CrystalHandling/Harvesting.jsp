<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Selecting a crystal from a well and mounting it on a pin,
with optional in-well cryoprotection.

Author: Ed Daniel
Date: April 2012
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>
<%-- 
<jsp:useBean id="results" scope="request" type="java.util.Collection" />
--%>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Harvesting" />
    <jsp:param name="extraStylesheets" value="" />
</jsp:include>

<c:set var="breadcrumbs"></c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Crystal harvesting: 9098:A01.1"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>


<pimsWidget:box title="Harvesting" initialState="fixed" extraClasses="noscroll">

    <pimsForm:form id="tabsForm" action="/#" mode="edit" method="post">
Formfields
    </pimsForm:form>


<div style="width:90%; position:relative;top:0; margin:10px auto;">
    <img src="drop.jpg" style="width:100%"/>
</div>
</pimsWidget:box>

<pimsWidget:box title="Implementation details (Ed)" initialState="fixed" extraClasses="noscroll">
<ul>
<li>Pin: standard "Edit role" interface</li>
<li>Need all children of original Trial, with their x and y positions, in order to show previously-chosen crystals</li>
<li>Action on form submit:
    <ul>
	<li>(Optional) Add Cryoprotection experiment</li>
	<li>(Mandatory) Add harvesting experiment (store x and y co-ordinates)</li>
	<li>(Mandatory) Put OutputSample into Container (pin)</li>
    </ul>
</li>
<li></li>
<li></li>
<li></li>
<li></li>
<li></li>
<li></li>
</ul>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>    

<jsp:include page="/JSP/core/Footer.jsp" />