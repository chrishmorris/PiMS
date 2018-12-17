<%@attribute name="bean" required="false" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<%@attribute name="classNameToCreate" required="false" %>
<%@attribute name="suppressContextMenu" required="false" %>

<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<pims:import className="org.pimslims.model.reference.Organism" />

<%-- TODO the logic for making the menus is complicated.
This should be moved into Java, into class ModelObjectShortBean --%>

<c:choose>
<c:when test="${empty bean && empty classNameToCreate}">
    <span style="font-weight:bold;color:red" onclick="alert('Both classname and bean attributes are empty. One must be supplied.')">Error in link.tag</span>
</c:when>
<c:otherwise>

<c:set var="title" value=""/>
<c:set var="contextMenu" value="" />
<c:set var="icon" value="blank.gif" />

<c:choose><c:when test="${null ne bean && null ne bean.metaClass}">
    <c:set var="className" value="${bean.className}" />
    <%-- Default context menu --%>
	<c:if test="${empty suppressContextMenu || false==suppressContextMenu}">
	    <c:set var="contextMenu">
	        ${bean.menu}
	    </c:set>
	</c:if>
</c:when><c:otherwise>
    <c:set var="className" value="${classNameToCreate}" />
</c:otherwise></c:choose>

<%-- TODO put all this in ModelObjectShortBean --%>
<c:choose>
    <c:when test="${'org.pimslims.model.target.ResearchObjective'==className}">
        <c:set var="icon" value="construct.gif" />
		<c:set var="title" value="Project"/>
    </c:when><c:when test="${'org.pimslims.model.experiment.Experiment'==className}">
        <c:set var="icon" value="experiment.gif" />
		<c:set var="title" value="Experiment"/>
    </c:when><c:when test="${Organism['class'].name eq className}">
        <%-- no icon --%>
        <c:set var="icon" value="organism.gif" />
		<c:set var="title" value="Organism"/>
    </c:when><c:when test="${'org.pimslims.model.holder.Holder'==className}">
        <c:choose><c:when test="${bean.isPlate}">
          <c:set var="icon" value="plate.gif" />
          <c:set var="title" value="Plate"/>
        </c:when><c:otherwise>
          <c:set var="icon" value="holder.gif" />
		  <c:set var="title" value="Holder"/>
		</c:otherwise></c:choose>
    </c:when><c:when test="${'org.pimslims.model.location.Location'==className}">
        <c:set var="icon" value="location.gif" />
		<c:set var="title" value="Location"/>
    </c:when><c:when test="${'org.pimslims.model.molecule.Molecule'==className}">
        <c:set var="icon" value="molecule.gif" />
		<c:set var="title" value="Molecule"/>
    </c:when><c:when test="${'org.pimslims.model.people.Person'==className}">
        <c:set var="icon" value="person.gif" />
        <c:set var="title" value="Person"/>
    </c:when><c:when test="${'org.pimslims.model.people.Group'==className}">
        <c:set var="icon" value="peoplegroup.gif" />
        <c:set var="title" value="Group"/>
    </c:when><c:when test="${'org.pimslims.model.people.Organisation'==className}">
        <c:set var="icon" value="organisation.gif" />
        <c:set var="title" value="Organisation"/>
    </c:when><c:when test="${'org.pimslims.model.accessControl.User'==className}">
        <c:set var="icon" value="user.gif" />
		<c:set var="title" value="User"/>
    </c:when><c:when test="${'org.pimslims.model.experiment.ExperimentGroup'==className}">
        <c:set var="icon" value="plate.gif" />
		<c:set var="title" value="Experiment group"/>
    </c:when><c:when test="${'org.pimslims.model.protocol.Protocol'==className}">
        <c:set var="icon" value="protocol.gif" />
		<c:set var="title" value="Protocol"/>
    </c:when><c:when test="${'org.pimslims.model.sample.Sample'==className}">
        <c:set var="icon" value="sample.gif" />
        <c:set var="title" value="Sample"/>
    </c:when><c:when test="${'org.pimslims.model.sample.RefSample'==className}">
        <c:set var="icon" value="recipe.gif" />
        <c:set var="title" value="Recipe"/>
    </c:when><c:when test="${'org.pimslims.model.target.Target'==className}">
        <c:set var="icon" value="target.gif" />
        <c:set var="title" value="Target"/>
    </c:when><c:when test="${'org.pimslims.model.target.Project'==className}">
        <c:set var="title" value="Project"/>
    </c:when><c:when test="${'org.pimslims.model.target.TargetGroup'==className}">
        <c:set var="icon" value="targetgroup.gif" />
		<c:set var="title" value="Target Group"/>
    </c:when>
    <c:when test="${'org.pimslims.model.molecule.Extension'==className}">
        <c:set var="icon" value="extension.gif" />
        <c:set var="title" value="Extension"/>
    </c:when>
</c:choose>

<c:choose><c:when test="${!empty bean}">
    <pimsWidget:linkWithIcon 
	   	name="${utils:escapeJS(bean.name)}"
		contextMenu="${contextMenu}"
		text="${fn:escapeXml(bean.name)}" icon="types/small/${icon}" title="${title}"
		url="${pageContext.request.contextPath}/View/${bean.hook}" />
</c:when><c:otherwise>
    <pimsWidget:linkWithIcon 
        text="New ${title}" icon="actions/create/${icon}" title="Create a new ${title}"
        url="${pageContext.request.contextPath}/Create/${classNameToCreate}" />
</c:otherwise></c:choose>

</c:otherwise>
</c:choose>
