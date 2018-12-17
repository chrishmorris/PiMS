<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@attribute name="beans" required="true" type="java.util.List" %>
<%--
	Takes a collection of beans and generates a three-column list of 
	<pimsWidget:link>s.
--%>
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
