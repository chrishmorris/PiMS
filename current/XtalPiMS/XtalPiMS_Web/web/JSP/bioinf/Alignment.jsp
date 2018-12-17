<%--

Author: Peter Troshin, STFC Daresbury Laboratory
Date: October 2007
--%>

<%@ page contentType="text/html; charset=utf-8" language="java" import="java.text.*,java.util.*,org.pimslims.bioinf.*, org.pimslims.bioinf.local.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="align" scope="request" type="org.pimslims.bioinf.local.PimsAlignment" />

<%-- ${align.alignment.summary} --%>

<a name="align${status.count}"/>
<table border="0" class="list">
	<tr>
		<th colspan="1">Hit no: ${status.count}</th>
		<td colspan="5">${align.type}: <a href="${pageContext.request.contextPath}/View/${align.hook}">${align.hitName}</a>
		Related ${align.compTypeDecorated}: <a href="${pageContext.request.contextPath}/View/${align.linkedHook}">${align.linkedName}</a></td>
	</tr>
	<tr>
		<th>Hit length</th>
		<th>Query seq. length</th>
		<th>Alignment length</th>
		<th>Score</th>
		<th>Identities</th>
		<th>Gaps</th>
	</tr>
	<tr>
		<td>${align.hitSeqLength}</td>
		<td>${align.querySeqLength}</td>
		<td>${align.templateLength}</td>
		<td>${align.score}</td>
		<td>${align.identity}</td>
		<td>${align.gaps}</td>
	</tr>
	<tr>
		<th colspan="2">Identity over hit</th>
		<th colspan="2">Identity over alignment</th>
		<th colspan="1">Identity over query</th>
		<th>Hit length as % of query</th>
	</tr>
	<tr>
		<td colspan="2">${align.identityOverHit}&#037; (${align.identity}/${align.hitSeqLength})</td>
		<td colspan="2">${align.identityOverAlignment}&#037; (${align.identity}/${align.templateLength})</td>
		<td colspan="1">${align.identityOverQuerySeq}&#037; (${align.identity}/${align.querySeqLength})</td>
		<td>${align.hitOverQuerySeq}&#037; (${align.hitSeqLength}/${align.querySeqLength})</td>
	</tr>
	<tr>
	<td colspan="6">
		<c:forEach items="${align.formatted}" var="line" varStatus="count">
		<c:choose>
			<c:when test="${count.count%2==0}">
				<%-- This is to prevent browsers to squash empty div and broke even formatting --%>
				<c:if test="${empty fn:trim(line)}">
					<c:set var="space" value="&nbsp;"/>
				</c:if>
				<div  style="margin: -0.5em 0 -0.5em 0;" class="blastPattern" ><pre><span style="font-size:120%;">${line}</span>${space}</pre></div>
			</c:when>
			<c:otherwise>
				<div style="margin: 1em 0 1em 0;" class="blastSequence" ><pre>${line}</pre></div>
			</c:otherwise>
		</c:choose>
		<c:remove var="space" />
 	</c:forEach>

	</td>
</tr>
</table>


<!-- OLD -->
