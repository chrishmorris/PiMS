<%--
Custom view of modelobjects changed on a date
Servlet: org.pimslims.servlet.DayServlet
Author: Marc Savitsky
Date: September 2008
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ page import="org.pimslims.model.sample.*" %>

<c:catch var="error">
<%--  
<jsp:useBean id="targets" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBeanTree>" />
<jsp:useBean id="experiments" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="others" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" /> 
--%>
<pims:import className="<%= Sample.class.getName() %>" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Day View: ${displaydate}" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>
<!-- DayView.jsp -->

<script type="text/javascript">

var thisDate = new Date();
thisDate.setTime(${datetime}); 

function validateForm(theForm){
  var reason = "";
  for (var i = 0; i < theForm.elements.length; ++i) {
      var element = theForm.elements[i];
      if (element.name.match("name"))
      	reason += validateName(element.value);
  }
  
  if (reason != "") {
    alert("Some fields need correction:\n" + reason);
    return false;
  }
  return true;
}

function validateName(value){
  if (value=="") 
  	return "A Name is required\n";
  return "";
}

</script>


<%-- TODO When we have a week/month view, breadcrumbs should link to these --%>
<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/">Home</a></c:set>
<c:set var="icon" value="date.png" />        
<c:set var="title" value="${displaydate}"/>
<c:set var="actions">
<a href="${pageContext.request.contextPath}/Day/${prevday}" title="Previous Day">Previous day</a>
<a href="${pageContext.request.contextPath}/Day/${nextday}" title="Next Day">Next day</a>
<a href="${pageContext.request.contextPath}/Week/${today}" title="Week view">Week view</a>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>






<pimsWidget:box title="Instruments" initialState="closed">

<c:set var="UNBOOKED"><td style="color: gray">Click to reserve</td></c:set>


<table>
  <tr><th><script>document.write('<span title="'+getClockChange()+'">'+getOffsetString(new Date())+'</span>'); </script></th>
    <c:forEach var="instrument" items="${instruments}">
      <th><pimsWidget:link bean="${instrument}" /></th>
    </c:forEach>
  </tr>
<c:forEach var="slot" items="${timeslots}">
  <tr><th>${slot.header}</th> 
    <c:forEach var="instrument" items="${instruments}">
      <c:choose><c:when test="${!slot.instruments[instrument.name].booked}">
        <td style="color: gray">
          <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.schedule.ScheduledTask?scheduledTime=${slot.start}&completionTime=${slot.end}">
            Click to reserve
            </a>
        </td>
      </c:when><c:when test="${0<slot.instruments[instrument.name].rows}">
        <td style="border: 1px solid black;" rowspan="${slot.rows}"><c:out value="${slot.instruments[instrument.name].name}" /></td>
      </c:when><c:otherwise>
        <!-- rowspan -->
      </c:otherwise></c:choose>
    </c:forEach>
  </tr>
</c:forEach>
</table>

</pimsWidget:box>


<c:if test="${not empty targets}">
	
	<pimsWidget:box title="Targets and constructs" initialState="closed">
	<ul style="list-style-type:none; width:50em;">
		<c:forEach var="target" items="${targets}" varStatus="status">
		
			<li style="padding-bottom:1em; float:left; width:25em;"><pimsWidget:link bean="${target.bean}"/>
  				<ul style="list-style-type:none;">
					<c:forEach var="subtarget" items="${target}">
  						<li><pimsWidget:link bean="${subtarget.bean}"/>
  						</li>
					</c:forEach>
				</ul>
  			</li>
		
		</c:forEach>
	</ul>
	<br style="clear:both;"/>
	</pimsWidget:box>
	
</c:if>
	

<!-- Used in experiments... -->
<c:if test="${not empty experiments}">
	<pimsWidget:box title="Experiments" initialState="closed">
	<ul style="list-style-type: none;">
	<c:forEach var="experiment" items="${experiments}" >
  		<li><pimsWidget:link bean="${experiment}"/></li>
	</c:forEach>
	</ul>
	</pimsWidget:box>
</c:if>   
 
<c:if test="${not empty others}">
	<pimsWidget:box title="Other Pages" initialState="closed">
	<ul style="list-style-type: none;">
	<c:forEach var="other" items="${others}" >
  		<li><pimsWidget:link bean="${other}"/></li>
	</c:forEach>
	</ul>
	</pimsWidget:box>
</c:if>

<c:if test="${empty targets && empty experiments && empty others}">
	<pimsWidget:box title="Information" initialState="fixed">
	<p style="color:#600;text-align:center;font-weight:bold;">No activity today</p>
	</pimsWidget:box>
</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
