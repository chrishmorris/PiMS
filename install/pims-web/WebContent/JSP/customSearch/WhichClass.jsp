<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%-- 
Author: cm65
Date: 1 Jun 2009

-->
<%-- bean declarations e.g.:
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />
<jsp:useBean id="constructBeans" scope="request"
type="java.util.Collection<ConstructBean>" />
--%>

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Search PiMS' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>
<pimsWidget:pageTitle breadcrumbs="<a href='${pageContext.request.contextPath}/'>Home</a>" title="Search PiMS"  />

<c:set var="options">
    <option value="">[Choose type:]</option>
    <option value="<%= org.pimslims.model.target.Target.class.getName() %>">Targets</option>
    <option value="<%= org.pimslims.model.target.ResearchObjective.class.getName() %>">Constructs</option>
    <option value="<%= org.pimslims.model.experiment.Experiment.class.getName() %>">Experiments</option>
    <option value="<%= org.pimslims.model.protocol.Protocol.class.getName() %>">Protocols</option>
    <option value="<%= org.pimslims.model.sample.Sample.class.getName() %>">Samples</option>
    <optgroup label="Advanced">
    <option value="<%= org.pimslims.model.reference.Organism.class.getName() %>">Organisms</option>
    <option value="<%= org.pimslims.model.reference.ExperimentType.class.getName() %>">Experiment Types</option>
    <option value="<%= org.pimslims.model.holder.Holder.class.getName() %>">Containers</option>
    <option value="<%= org.pimslims.model.reference.SampleCategory.class.getName() %>">Sample types</option>
    <option value="<%= org.pimslims.model.molecule.Molecule.class.getName() %>">Molecules</option>
    <option value="<%= org.pimslims.model.people.Organisation.class.getName() %>">Organisations</option>
    </optgroup>
</c:set>
<pimsWidget:quickSearch  value="${fn:escapeXml(param['search_all'])}" initialState="open" >
	<pimsForm:formBlock><pimsForm:column1>
	<pimsForm:select name="_metaClass" alias="Search for">
        ${options}
    </pimsForm:select>
    </pimsForm:column1></pimsForm:formBlock>
</pimsWidget:quickSearch>
<script type="text/javascript">   if (null==focusElement) focusElement = document.getElementsByName('search_all')[0] </script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
</template></templates>