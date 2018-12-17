<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>&#13;
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>&#13;
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>&#13;

<%-- &#13;
Author: cm65&#13;
Date: 11 May 2009&#13;
Servlets: &#13;
&#13;
-->&#13;
<%-- bean declarations e.g.:&#13;
<jsp:useBean id="targetBean" scope="request" type="TargetBean" />&#13;
<jsp:useBean id="constructBeans" scope="request"&#13;
type="java.util.Collection<ConstructBean>" />
--%>
<jsp:useBean id="directions" scope="request" type="java.util.Map" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Record a new primer extension and tag' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
<br />
<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/ExtensionsList">Extensions</a>
</c:set>
<c:set var="title" value="Record a new 5'-Extension:"/>
<c:set var="actions">
                <pimsWidget:linkWithIcon 
                    icon="misc/help.gif" 
                    url="${pageContext.request.contextPath}/help/HelpExtensions.jsp#extnew"
                    text="Help"/>
</c:set>

<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}"  />

<%--Later set this based on type of Extension to create depending on where this is called from
and also use to set the term as in ExtensionView--%>
<c:set var="dir" value="forward" scope="page" />

<pimsWidget:box id="box1" title="Extension details" initialState="open" >
	<pimsForm:form method="post" action="/Create/org.pimslims.model.molecule.Extension" mode="create">
		<pimsForm:formBlock>
			<pimsForm:column1>
				<input type="hidden" name="METACLASSNAME" value="org.pimslims.model.molecule.Extension"/>		
				<pimsForm:text alias="Name" name="name" value=""  validation="required:true, unique:{obj:'org.pimslims.model.molecule.Substance',prop:'name'}" helpText="The name of the Extension" /><%--maxlength="80"--%>
			</pimsForm:column1>
			<pimsForm:column2>
				<pimsForm:select alias="Direction" name="direction" helpText="Primer direction: Forward or Reverse" >
			      <c:forEach var="direction" items="${directions}" >
			      	<c:choose>
			      	<c:when test="${direction.key == 'forward'}">
						<option value="${direction.key}" selected="selected"><c:out value="${direction.value}" /></option>
					</c:when>
					<c:otherwise>
						<option value="${direction.key}"><c:out value="${direction.value}"/></option>
					</c:otherwise>
					</c:choose>
				</c:forEach>
				</pimsForm:select>
			</pimsForm:column2>
		</pimsForm:formBlock>
		<pimsForm:formBlock>
				<pimsForm:text alias="DNA sequence" name="sequence" value=""  validation="required:true, dnaSequence:true" onchange="this.value=cleanSequence2(this.value);" helpText="The DNA sequence of the Extension 5'- to 3'-" />		
		</pimsForm:formBlock>
		<pimsForm:formBlock>
			<pimsForm:column1>
				<pimsForm:text alias="Related protein Tag" name="relatedProteinTagSeq" value=""  helpText="Fusion sequence e.g. His tag sequence" onchange="this.value=extTagCheck(this.value); this.value=this.value.toUpperCase()"/>				
			
				<pimsForm:select name="labNotebookId" alias="Lab Notebook" helpText="The Lab Notebook this extension belongs to">
	     			<c:forEach var="p" items="${accessObjects}">
           				<option value="${p.hook}" ><c:out value="${p.name}" /></option>
         			</c:forEach>
       			</pimsForm:select>
			
			</pimsForm:column1>
			<pimsForm:column2>
				<pimsForm:text alias="Restriction site" name="restrictionSite" value=""  helpText="Restriction enzyme site" />				
			</pimsForm:column2>
		</pimsForm:formBlock>
		<pimsForm:formBlock>
     		<pimsForm:submitCreate onclick="dontWarn()" />
   		</pimsForm:formBlock>		
	</pimsForm:form>	
</pimsWidget:box>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />