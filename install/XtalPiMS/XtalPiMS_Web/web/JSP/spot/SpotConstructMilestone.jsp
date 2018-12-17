<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*"  %>
<%@ page import="org.pimslims.presentation.construct.*"  %>
<%@ page import="org.pimslims.util.*"  %>
<%@ page import="org.pimslims.model.experiment.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:useBean id="spotConstructMilestones" scope="request" type="java.util.Collection<ConstructResultBean>" />

<%--
//----------------------------------------------------------------------------------------------
//			SpotConstructMilestone.jsp									
//			Displays a list of Milestones achieved for a Construct
//		 	Created by Johan van Niekerk,SSPF-Dundee
//			Modified for re-design by Susy Griffiths November 2008
//----------------------------------------------------------------------------------------------
--%>

<c:catch  var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Experiments for Construct: ${constructBean.name}' />
</jsp:include>

<!--SpotConstructMilestone.jsp-->
<%--Susy 210110 Revised --%>
<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a>
:  <c:forEach var="target" items="${constructBean.targetBeans}" >
    <pimsWidget:link bean="${target}" />
  </c:forEach>  
 : <pimsWidget:link bean="${constructBean}" />

</c:set>
<c:set var="icon" value="experiment.png" />        
<c:set var="title" value="Experiments for ${constructBean.name}"/><c:set var="deleteQuery" value="" />
<c:set var="actions">
    <pimsWidget:diagramLink hook="${constructBean.hook}" />
    <pimsWidget:linkWithIcon 
                icon="actions/create/experiment.gif" 
                url="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${expBlueprintHook}" 
                text="New experiment"/>
</c:set>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:set var="newExperiment"><a href="${pageContext.request.contextPath}/Create/org.pimslims.model.experiment.Experiment?researchObjective=${expBlueprintHook}">New Experiment</a></c:set>
<pimsWidget:box id="experiments" title="Experiments" extraHeader="${newExperiment}" initialState="open" >
<c:choose>
        <c:when test="${empty spotConstructMilestones}">
           There are no experiments on this Target <br/>
        </c:when>
        <c:otherwise>
            <table class="list">
                <tr><th>Milestone</th><th>Person</th><th>Experiment Date</th><th>Experiment</th><th>Files</th></tr>                <c:forEach items="${spotConstructMilestones}" var="spcm">
                 <c:choose>
                  <c:when test="${spcm.lastResult==true}">
                   <tr class="latestMilestone">
                  </c:when>
                  <c:otherwise>
                   <tr>
                  </c:otherwise>
                 </c:choose>
                    <td><c:out value="${spcm.milestoneName}" /></td>
                    <td><c:out value="${spcm.person_login_name}" /></td>
                    <td><pimsWidget:dateLink date="${spcm.dateOfExperiment}"  /></td>
                    <td><pimsWidget:link bean="${spcm.expBean}" /></td>
                    <td>
                      <c:forEach items="${spcm.files}" var="file">
                       <c:out value="${file.name}"/><br />
                      </c:forEach></td>
                   </tr> 
                </c:forEach>
            </table>        
        </c:otherwise>
    </c:choose>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>
<jsp:include page="/JSP/core/Footer.jsp" />
