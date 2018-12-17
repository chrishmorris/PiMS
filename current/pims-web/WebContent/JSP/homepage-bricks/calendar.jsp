<%--
  Brick name: calendar
  Rows: 2
  Columns: 1
  
  Display a clickable calendar, letting you jump to a day view.

--%>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
  <h3>Calendar</h3>
  <div class="brickcontent">
  	<%
  		java.util.Date now=new java.util.Date();
  		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		request.setAttribute("today",sdf.format(now));
  	%>
    <%-- BEWARE When editing the message below, ensure that a month spanning six weeks still fits into the box! --%>
    <p style="text-align: left; padding-top: .25em; padding-left: .3em;">Click on a day to view activity on that day. Or view <a href="${pageContext.request.contextPath}/Day/${today}">today's activity</a>.</p>
    <pimsWidget:calendar />
  </div>
