<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%-- 
Author: Marc Savitsky
Date: 30-Apr-2008
Servlets: processed by /servlet/dnatarget/DnaTarget.java extends /servlet/spot/SpotNewTarget

-->
<%-- bean declarations--%> 
<jsp:useBean id="people" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="accessObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="organisms" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="suggestedName" scope="request" type="java.lang.String" /> 

<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new Natural Source Target' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
<c:set var="projectNum" value="${fn:length(requestScope.accessObjects)}" />
<c:set var="orgNum" value="${fn:length(requestScope.organisms)}" />
<c:set var="peopleNum" value="${fn:length(requestScope.people)}" />

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="New Natural Source Target"/>
<c:set var="pageType" value="create"/>
<pimsWidget:pageTitle title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}" pageType="${pageType}" />

<%-- hide the form if the user cannot use it --%>
<c:set var="showForm" value="true" />
<c:if test="${projectNum==0}">
    <p class="error">You cannot record a target, because there are no  recorded in PiMS.</p>
    <c:set var="showForm" value="false" />
</c:if>
<c:if test="${orgNum==0}">
    <p class="error">You cannot record a target, because there are no organisms recorded in PiMS.</p>
    <pimsWidget:createLink className="org.pimslims.model.reference.Organism"/><br/><br/>
    <c:set var="showForm" value="false" />
</c:if>
<c:if test="${peopleNum==0}">
    <p class="error">You cannot yet record a target, because there are no people recorded in PiMS.</p>
    <pimsWidget:createLink className="org.pimslims.model.people.Person"/><br/><br/>
    <c:set var="showForm" value="false" />
</c:if>

<%-- The form --%>
<c:if test="${showForm}">
    <pimsWidget:box id="details" title="Target Details" initialState="fixed" >
        <pimsForm:form action="/naturalsourcetarget/NewNaturalSourceTarget" id="ID" method="post" mode="create" >
            <pimsForm:formBlock>
                <pimsForm:column1>
                    <pimsForm:text value="${suggestedName}"  name="targetId" alias="Name" helpText="Unique identifier for the Target" validation="required:true,unique:{ obj:'org.pimslims.model.target.Target',prop:'name'}" />
                    <pimsForm:text name="sourceName" alias="Source name" helpText="The name of the Natural Source" validation="required:true, unique:{obj:'org.pimslims.model.molecule.Molecule',prop:'name'}"/>
                    <pimsForm:text name="funcDesc" alias="Function Description" helpText="The function of the Target" />
                </pimsForm:column1>
                <pimsForm:column2>
                	<pimsForm:labNotebookField name="accessId" helpText="Select a lab notebook for the Target from the list" objects="${accessObjects}" />
                    <pimsForm:select name="organismId" alias="Organism" helpText="The source organism of the target">
                        <option value="[none]"> </option>
                        <c:forEach var="p" items="${organisms}">
                            <option value="${p.hook}" ><c:out value="${p.name}" /></option>
                        </c:forEach>
                    </pimsForm:select>
                    <pimsForm:select name="personHook"  alias="Scientist" helpText="The scientist responsible for the Target"  >
                        <option value="${currentPerson.hook}" selected ><c:out value="${currentPerson.name}" /></option>
                         <c:forEach var="p" items="${people}"> <%-- TODO what about confidentiality? --%>
                            <c:if test="${p.hook != currentPerson.hook}">
                              <option value="${p.hook}" ><c:out value="${p.name}" /></option>
                            </c:if>
                        </c:forEach>
                    </pimsForm:select>
                </pimsForm:column2>
            </pimsForm:formBlock>
            <pimsForm:formBlock>
                <pimsForm:textarea name="comments" alias="Comments" helpText="Comments e.g. Why Target was selected" />
                <pimsForm:submitCreate />
            </pimsForm:formBlock>
        </pimsForm:form>
    </pimsWidget:box>
</c:if>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
