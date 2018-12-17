<%@ page language="java" contentType="text/html; charset=utf-8"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>


<c:catch var="error">
	<c:set var="dayviewlink"><a href="${pageContext.request.contextPath}/Day/${day.linkFormattedDate}">Day view</a></c:set>

	<c:choose><c:when test="${empty day.targets && empty day.experiments && empty day.otherObjects}">
		<pimsWidget:box title="${day.formattedDate} (no activity)" initialState="closed" extraHeader="${dayviewlink}">
		<p>No activity on this day</p>
		</pimsWidget:box>
	</c:when><c:otherwise>
		<pimsWidget:box title="${day.formattedDate}" initialState="open" extraHeader="${dayviewlink}" extraClasses="padded">



<div class="threecolumnlist">
	<div class="column">
	<c:set var="currentColumn" value="1"/>
	<c:forEach var="bean" items="${beans}" varStatus="status">
		<pimsWidget:link bean="${bean}"/>
		<c:if test="${status.count ge fn:length(beans)/3 && '1' eq currentColumn}">
			</div>
			<div class="column">
			<c:set var="currentColumn" value="2"/>
		</c:if>
		<c:if test="${status.count ge (fn:length(beans)/3)*2 && '2' eq currentColumn}">
			</div>
			<div class="column">
			<c:set var="currentColumn" value="3"/>
		</c:if>
	</c:forEach>
	</div>

<div class="shim">&nbsp;</div>
</div>







			<%-- Target items may be different heights due to constructs underneath, and need custom logic to
			     iterate through them, so can't use three-column tag 
			 --%>
			<c:if test="${!empty day.targets}">
				<h4>Targets</h4>
				<div class="threecolumnlist">
					<c:forEach var="target" items="${day.targets}" varStatus="status">
						<c:if test="${status.count gt 3 && status.count %3 eq 1}">
							<br/><%-- Separate adjacent rows of targets, but no unneeded whitespace between targets and h4 above/below --%>
						</c:if>
						<div class="column overrideblocklinks">
							<pimsWidget:link bean="${target.bean}"/>
				  			<c:forEach var="subtarget" items="${target}">
				  				<br/>&nbsp;&nbsp;<pimsWidget:link bean="${subtarget.bean}"/>
							</c:forEach>
						</div>
						<c:if test="${status.count %3 eq 0}">
							<div class="shim">&nbsp;</div>
						</c:if>
					</c:forEach>
				</div>
				<div class="shim">&nbsp;</div>
			</c:if>
	
			<c:if test="${!empty day.experiments}">
				<h4>Experiments</h4>
				<pimsWidget:threeColumnList beans="${day.experiments}" />
			</c:if>
		
			<c:if test="${!empty day.otherObjects}">
				<h4>Other</h4>
				<pimsWidget:threeColumnList beans="${day.otherObjects}" />
			</c:if>
		
		</pimsWidget:box>
	</c:otherwise></c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>