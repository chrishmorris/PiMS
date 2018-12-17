<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%-- 
Author: susy
Date: 30-Jan-2008
Servlets: processed by /servlet/dnatarget/DnaTarget.java extends /servlet/spot/SpotNewTarget

-->
<%-- bean declarations--%> 
<jsp:useBean id="people" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="accessObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="organisms" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="suggestedName" scope="request" type="java.lang.String" />
<jsp:useBean id="dnaTypes" scope="request" type="java.util.Map" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new DNA Target' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<!-- OLD -->

<%-- page body here --%>
<c:set var="projectNum" value="${fn:length(requestScope.accessObjects)}" />
<c:set var="orgNum" value="${fn:length(requestScope.organisms)}" />
<c:set var="peopleNum" value="${fn:length(requestScope.people)}" />

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="Record a new DNA target"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<c:if test="${projectNum==0}">
    <p class="error">You cannot record a target, because there are no lab note books recorded in PiMS.</p>
    <pimsWidget:createLink className="org.pimslims.model.access.LabNotebook"/><br/><br/>
    <c:set var="formStyle" value="display: none" />
</c:if>
<c:if test="${orgNum==0}">
    <p class="error">You cannot record a target, because there are no organisms recorded in PiMS.</p>
    <pimsWidget:createLink className="org.pimslims.model.reference.Organism"/><br/><br/>
    <c:set var="formStyle" value="display: none" />
</c:if>
<c:if test="${peopleNum==0}">
    <p class="error">You cannot yet record a target, because there are no people recorded in PiMS.</p>
    <pimsWidget:createLink className="org.pimslims.model.people.Person"/><br/><br/>
    <c:set var="formStyle" value="display: none" />
</c:if>

<%--The form --%>
<c:if test="${formStyle!='display: none'}" >

<pimsWidget:box title="New DNA target details" initialState="open">
    <pimsForm:form id="newDNATarget" action="/dnatarget/NewDnaTarget" method="post" mode="create">
        <pimsForm:formBlock>
            <pimsForm:column1>
                <pimsForm:text name="targetId" alias="Name" value="${suggestedName}" validation="required:true, unique:{obj:'org.pimslims.model.target.Target',prop:'name'}" helpText="Unique identifier for the Target"/>
                <pimsForm:text name="dnaName"  alias="Sequence name" validation="required:true, unique:{obj:'org.pimslims.model.molecule.Molecule',prop:'name'}" helpText="The name of the DNA element"/>
                <pimsForm:text name="giNumber" alias="GI number" validation="wholeNumber:true" helpText="NCBI GenBank identifier"/>
       <pimsForm:select name="organismId" alias="Organism" helpText="The source organism of the target">
		<option value="[none]"> </option>
         <c:forEach var="p" items="${organisms}">
           <option value="${p.hook}" ><c:out value="${p.name}" /></option>
         </c:forEach>
       </pimsForm:select>
                <pimsForm:select name="funcDesc" alias="Function description" helpText="Description of the DNA Target's function or role">
                    <c:forEach items="${dnaTypes}" var="dnaType" >
                        <option value="${dnaType.key}"><c:out value="${dnaType.value}"/></option>
                    </c:forEach>
                </pimsForm:select>
            </pimsForm:column1>
            <pimsForm:column2>
            	<pimsForm:labNotebookField name="accessId" helpText="Select a lab notebook for the Target from the list" objects="${accessObjects}" />
                <pimsForm:select name="personHook" alias="Scientist" helpText="Select a Person from the list">
                    <option value="${currentPerson.hook}" selected ><c:out value="${currentPerson.name}" /></option>
                    <c:forEach var="p" items="${people}">  <%-- TODO what about confidentiality? --%>
                        <c:if test="${p.hook != currentPerson.hook}">  
                            <option value="${p.hook}" ><c:out value="${p.name}" /></option>
                        </c:if>
                    </c:forEach>
                </pimsForm:select>
            	<pimsForm:textarea name="comments" alias="Comments" helpText="Comments e.g. Why Target was selected"></pimsForm:textarea>
            </pimsForm:column2>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:textarea name="dnaSeq" alias="DNA sequence" helpText="The DNA sequence of the Target" validation="dnaSequence:true" onchange="this.value=cleanSequence2(this.value);"></pimsForm:textarea>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:submitCreate />
        </pimsForm:formBlock>
    </pimsForm:form>
</pimsWidget:box>


</c:if>


</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
