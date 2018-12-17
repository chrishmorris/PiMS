<%--

Author: Peter Troshin
Date: February 2008
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<pimsWidget:box title="Targets" initialState="open">
<table class="list">
<tr>
<th>Target</th>
<th>Milestone</th>
<th>Target Creator</th>
<th>Recent experiment</th>
<th>Experimenter</th>
<th>Days since last progress</th>
<th>Experiment Lab</th>
</tr>
<c:forEach items="${activeTargets}" var="atarget">
<tr>
<td><pimsWidget:link bean="${pims:asShortBean(atarget.target)}"/></td>
<td>${atarget.lastMilestoneName}</td>
<td>${atarget.creator}</td>
<td><pimsWidget:link bean="${pims:asShortBean(atarget.lastExperiment)}"/></td>
<td>${atarget.experimentator}</td>
<td>
<c:choose>
<c:when test="${atarget.daysSinceLastProgress < 0}">
	to be run in ${-atarget.daysSinceLastProgress} days
</c:when>
<c:otherwise>
	${atarget.daysSinceLastProgress}
</c:otherwise>
</c:choose>
</td>
<td>${atarget.experimentLab}</td>
</tr>
</c:forEach>


</table>
</pimsWidget:box>
