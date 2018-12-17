<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="MAX" value="<%=new Integer(Integer.MAX_VALUE) %>" />


<%-- note may need ${requestScope['javax.servlet.forward.request_uri']} --%>
<pimsWidget:box title="Page-display controls"  initialState="closed">
   <form action="${pageContext.request.requestURI}"  class="grid "  >

		
		<jsp:doBody />
		
		<pimsForm:formBlock>
			<span style="font-weight:bold">Number of results to show on one page</span>
			<c:forTokens items="10 20 30 50 100 -1" delims=" " var="token" >
				<c:set var="checked" value=""/>
				<c:if test="${token == pagesize}">
					<c:set var="checked">checked="checked"</c:set>
				</c:if>
				<c:choose>
				<c:when test="${token == '-1'}">
					&nbsp;&nbsp;&nbsp;<input type="radio" ${checked} value="${MAX}" name="pagesize" />unlimited
				</c:when>
				<c:otherwise>
					&nbsp;&nbsp;&nbsp;<input type="radio" ${checked} value="${token}" name="pagesize" />${token}
				</c:otherwise>
				</c:choose>
			</c:forTokens>
			&nbsp;&nbsp;
			<input type="submit" name="Submit" value="Update view"  onclick="dontWarn()" />
		</pimsForm:formBlock>
	</form>
</pimsWidget:box>

