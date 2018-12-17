<%@attribute name="bean" required="true" type="org.pimslims.presentation.ModelObjectShortBean"  %>
<%@ taglib prefix="pimsWidget" 
  tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag body-content="tagdependent" %>

<c:set var="type" value="record" />
<c:choose>
	<c:when test="${'org.pimslims.model.experiment.Experiment'==bean.className}">
		<c:set var="type" value="experiment" />
	</c:when><c:when test="${'org.pimslims.model.protocol.Protocol'==bean.className}">
		<c:set var="type" value="protocol" />
	</c:when>
</c:choose>

<pimsWidget:linkWithIcon 
		text="Copy" title="Create a copy of this ${type}" icon="actions/copy.gif"
		url="#" 
		onclick="copy_object('${bean.hook}');return false" />