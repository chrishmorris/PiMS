<%--
Display all the MRUs of Current user.
Author: Bill Lin
Date: March 2006
--%>
<%-- 
History is written in three places - if you're changing one, you probably want to
update the others too:

* JSP/core/MRUInfo.jsp - the full history page
* JSP/core/historyMenu.jsp - the pull-down menu in the menu bar
* JSP/homepage-bricks/history.jsp - the homepage brick
 
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%@page import="org.pimslims.presentation.mru.MRUController"%>
<%@page import="org.pimslims.presentation.mru.MRU"%>
<%@page import="org.pimslims.servlet.PIMSServlet"%>
<%@page import="java.util.*"%>
<c:catch var="error">

<%--Current user name and MRU --%>
<%
	String UserName=PIMSServlet.getUsername(request);
	int maxMRUNumber=MRUController.getMaxSize();
	int maxMRUNumberPerClass=MRUController.getMaxSizeOfEachClass();
	ArrayList<MRU> MRUs =new ArrayList<MRU>(MRUController.getMRUs(UserName));
    request.setAttribute("MRUs", MRUs);
    request.setAttribute("targetMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.target.Target")));
    request.setAttribute("constructMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.target.ResearchObjective")));
    request.setAttribute("sampleMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.sample.Sample")));
    request.setAttribute("protocolMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.protocol.protocol")));
    request.setAttribute("experimentMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.experiment.Experiment")));
    request.setAttribute("experimentGroupMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.experiment.ExperimentGroup")));
    request.setAttribute("personMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.people.Person")));
    request.setAttribute("userMRUs", new ArrayList<MRU>(MRUController.getMRUs(UserName, "org.pimslims.model.accessControl.User")));
	request.setAttribute("UserName", UserName);
%>

<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="History: Most Recently Used Items" />
</jsp:include>

<c:set var="actions">
        <pimsWidget:helpLink url="${pageContext.request.contextPath}/help/HelpMRU.jsp" />
</c:set>
<pimsWidget:pageTitle title="History" actions="${actions}"/>

<%--Display the MRU table for current user --%>
<c:choose><c:when test="${empty UserName}" >
    <h3>No History Available</h3>
</c:when><c:otherwise>

	<pimsWidget:box extraClasses="padded" title="Targets and Constructs" initialState="open">
	<ul>
	<li>
		<h4>Targets</h4>
		<pimsWidget:threeColumnList beans="${requestScope.targetMRUs}"/>
		<h4>Constructs</h4>
		<pimsWidget:threeColumnList beans="${requestScope.constructMRUs}"/>
		</li>
		</ul>
	</pimsWidget:box>
	
	<pimsWidget:box extraClasses="padded" title="Samples" initialState="open">
	<ul>
    <li>
	    <pimsWidget:threeColumnList beans="${requestScope.sampleMRUs}"/>
	    </li>
        </ul>
	</pimsWidget:box>
	
	<pimsWidget:box extraClasses="padded" title="Protocols" initialState="open">
	    <pimsWidget:threeColumnList beans="${requestScope.protocolMRUs}"/>
	    </li>
        </ul>
	</pimsWidget:box>
	
	<pimsWidget:box extraClasses="padded" title="Experiments" initialState="open">
	<ul>
    <li>
	    <h4>Single experiments</h3>
	    <pimsWidget:threeColumnList beans="${requestScope.experimentMRUs}"/>
	    <h4>Plates and experiment groups</h3>
	    <pimsWidget:threeColumnList beans="${requestScope.experimentGroupMRUs}"/>
	    </li>
        </ul>
	</pimsWidget:box>
	
	<pimsWidget:box extraClasses="padded" title="People" initialState="closed">
	<ul>
    <li>
	    <h4>Users</h3>
	    <pimsWidget:threeColumnList beans="${requestScope.userMRUs}"/>
	    </li>
        </ul>
	</pimsWidget:box>

</c:otherwise>
</c:choose>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<%-- LATER makeBreadCrumb(displayName+"s") --%>
<jsp:include page="/JSP/core/Footer.jsp" />
