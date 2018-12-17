<%--
  Brick name: history
  Rows: 3
  Columns: 1
  
  For a logged-in user, display their most recently viewed records.

--%>
<%-- 
History is written in three places - if you're changing one, you probably want to
update the others too:

* JSP/core/MRUInfo.jsp - the full history page
* JSP/core/historyMenu.jsp - the pull-down menu in the menu bar
* JSP/homepage-bricks/history.jsp - the homepage brick
 
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>

	<h3>History</h3>
	<div class="brickcontent">

	<c:choose><c:when test="${empty username}">
	  <p> Please <a href="${pageContext.request.contextPath}/Login">log in</a> to see your history.</p>
	</c:when><c:otherwise>
		<p style="font-weight:bold; font-size:90%; padding-top: .4em; padding-left: .5em;">Recently viewed items</p>
		<%-- if logged on, MenuBar.java has put MRUs in request --%>
		<c:choose><c:when test="${empty requestScope.menuMRUs }">
			<p style="padding-top: .4em; padding-left: .5em;">As you view items in PiMS, they<br/>will be added to this list.</p>
			<p style="padding-top: .4em; padding-left: .5em;">Your list is empty at the moment.<br/></p>
		</c:when><c:otherwise>
			<span style="text-align:left;display:block;margin-top:0.5em; margin-bottom:0.5em; padding-left: 0.9em;"><b><a href="${pageContext.request.contextPath}/JSP/core/MRUInfo.jsp">All History</a></b></span>
			
			<table class="bricklist" style="">
			<c:forEach var="m" items="${ requestScope.menuMRUs }">
               <tr><td colspan="2"><pimsWidget:link bean="${m}" /></td></tr>                 
			</c:forEach>
			</table>
		</c:otherwise></c:choose>

    </c:otherwise></c:choose>

 	</div>
