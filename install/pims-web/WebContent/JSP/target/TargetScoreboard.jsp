<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Display the target scoreboard
--%> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- TargetScoreboard.jsp  -->
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Target Scoreboard" />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a></c:set>
<pimsWidget:pageTitle 
	title="Target scoreboard"
	icon="target.png" 
	breadcrumbs="${breadcrumbs}" />


<div style="width:30em;margin:0 auto">
<pimsWidget:box title="Scoreboard" initialState="fixed">
${scoreboardTable}
</pimsWidget:box>
</div>

</c:catch>

<jsp:include page="/JSP/core/Footer.jsp" />
