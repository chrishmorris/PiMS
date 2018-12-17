<%--

Author: Peter Troshin
Date: February 2008
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@page import="org.pimslims.presentation.target.TargetExperimentBean"%>

<%@page import="java.util.List"%>
<jsp:useBean id="activeTargets" type="List<TargetExperimentBean>" scope="request" />

<ul>
<c:forEach items="${activeTargets}" var="atarget" varStatus="status" end="13">
	<%-- overkill but sort of limit anyway--%>
	<li><pimsWidget:link bean="${pims:asShortBean(atarget.target)}"/></li>
</c:forEach>
</ul>

<!-- OLD -->
