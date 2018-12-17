<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.presentation.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<%
//----------------------------------------------------------------------------------------------
//			new_target.jsp
//			Servlets: SpotNewTarget
//
//		 	Created by Johan van Niekerk,SSPF-Dundee				10 October 2005
//			Modified by														Date
//----------------------------------------------------------------------------------------------
%>
<c:catch var="error">
<jsp:useBean id="people" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="accessObjects" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="organisms" scope="request" type="java.util.Collection<org.pimslims.presentation.ModelObjectBean>" />
<jsp:useBean id="suggestedName" scope="request" type="java.lang.String" />

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new Target' />
</jsp:include>

<c:set var="breadcrumbs"><a href="${pageContext.request.contextPath}/Search/org.pimslims.model.target.Target">Targets</a></c:set>
<c:set var="icon" value="target.png" />        
<c:set var="title" value="Record a new target"/>
<c:set var="actions"></c:set>
<pimsWidget:pageTitle pageType="create" title="${title}" icon="${icon}" actions="${actions}" breadcrumbs="${breadcrumbs}"/>

<% if (0==accessObjects.size()) {/* give link to create */ %>
    <p class="error">You cannot yet record a target, because there are no lab Notebooks recorded.</p>
    <c:set var="formStyle" value="display: none" />
<% } %>
<% if (0==organisms.size()) {/* give link to create */ %>
    <p class="error">You cannot yet record a target, because there are no organisms recorded.</p>
    <pimsWidget:createLink className="org.pimslims.model.reference.Organism"/><br/><br/>
    <c:set var="formStyle" value="display: none" />
<% } %>
<% if (0==people.size()) {/* give link to create */ %>
    <p class="error">You cannot yet record a target, because there are no people recorded.</p>
    <pimsWidget:createLink className="org.pimslims.model.people.Person"/><br/><br/>
    <c:set var="error" value="true" />
    <c:set var="formStyle" value="display: none" />
<% } %>

<c:if test="${formStyle!='display: none'}" >

<pimsWidget:box title="New target details" initialState="fixed">
 <pimsForm:form method="post" action="/spot/SpotNewTarget" mode="create" id="new_target">
   <pimsForm:formBlock>
     <pimsForm:column1>
       <pimsForm:text name="targetId" alias="Gene Name" helpText="The gene of interest" value="${suggestedName}" validation="required:true,unique:{ obj:'org.pimslims.model.target.Target',prop:'name'}" />
       <pimsForm:text name="proteinName" alias="Protein name" value="${suggestedName}" helpText="The gene product"  validation="required:true, unique:{obj:'org.pimslims.model.molecule.Molecule',prop:'name'}" />
       <%-- <pimsForm:text name="giNumber" alias="GI Number" helpText="NCBI GenBank identifier" validation="wholeNumber:true" /> --%>
       <pimsForm:text name="funcDesc" alias="Function Description"  helpText="The function description of this target"/>
       <pimsForm:select name="organismId" alias="Organism" helpText="The source organism of the target">
		<option value="[none]"> </option>
         <c:forEach var="p" items="${organisms}">
           <option value="${p.hook}" ><c:out value="${p.name}" /></option>
         </c:forEach>
       </pimsForm:select>
     </pimsForm:column1>
     <pimsForm:column2>

	   <pimsForm:labNotebookField name="accessId" helpText="The lab notebook this target belongs to" objects="${accessObjects}" />
      
      <%-- TODO remove --%>
       <pimsForm:select name="personHook"  alias="Scientist" helpText="The scientist responsible for the Target"  >
        <option value="${currentPerson.hook}" selected ><c:out value="${currentPerson.name}" /></option>
	      <c:forEach var="p" items="${people}">
	        <c:if test="${p.hook != currentPerson.hook}">
              <option value="${p.hook}" ><c:out value="${p.name}" /></option>
            </c:if>
          </c:forEach>
       </pimsForm:select>

      <pimsForm:textarea name="comments" alias="Comments" helpText="Comments e.g. Why Target was selected" ></pimsForm:textarea>

     </pimsForm:column2>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
      <pimsForm:textarea name="dnaSeq" alias="DNA sequence" helpText="The DNA sequence of the Target" validation="dnaSequence:true, custom:function(val,alias){return checkTargetDNASeq(val,alias)}" onchange="this.value=checkATG(this.value);"/>
      <pimsForm:textarea name="protSeq" alias="Protein sequence" helpText="The sequence of the Target protein" validation="proteinSequence:true" onchange="this.value=cleanSequence(this.value);"/>
   </pimsForm:formBlock>
   <pimsForm:formBlock>
     <pimsForm:submitCreate onclick="dontWarn()" />
   </pimsForm:formBlock>
 </pimsForm:form>
</pimsWidget:box>


</c:if>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>

<jsp:include page="/JSP/core/Footer.jsp" />
