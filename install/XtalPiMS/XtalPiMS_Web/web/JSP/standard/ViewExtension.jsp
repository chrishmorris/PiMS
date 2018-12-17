<%@ page contentType="text/html; charset=utf-8" language="java"
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<pims:import className="org.pimslims.model.molecule.Extension" />
<%-- 
Author: Susy Griffiths YSBL
Date: 22-Apr-2009
Servlets: standard/ViewExtension.java

-->
<%-- bean declarations --%>
<jsp:useBean id="extension" scope="request" type="org.pimslims.presentation.construct.ExtensionBean" />

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Extension: ${extension.exName}" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- ViewExtension.jsp --%>
<c:if test="${extension.direction == 'forward'}">
    <c:set var="dir" value="Forward" />
    <c:set var="term" value="N-term" />
</c:if>
<c:if test="${extension.direction == 'reverse'}">
    <c:set var="dir" value="Reverse" />
    <c:set var="term" value="C-term" />
</c:if>

<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/ExtensionsList">Extensions</a>
</c:set>
<c:set var="title" value="${dir} Extension: ${extension.exName}"/>
<c:set var="actions">
 <%--TODO for copy need a copy method to be called from org.pimslims.srvlet.Copy.java --%>
 <pimsWidget:copyLink bean="${extension}" />
 <!--
 <a href="${pageContext.request.contextPath}/Create/org.pimslims.model.molecule.Extension">New Extension</a> 
  -->
 <pimsWidget:createLink className="org.pimslims.model.molecule.Extension" />
 <pimsWidget:deleteLink bean="${extension}" />
 <pimsWidget:linkWithIcon icon="misc/help.gif" 
    url="${pageContext.request.contextPath}/help/HelpExtensions.jsp#extdetails"
    text="Help"/>
 
</c:set>

<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}" />

<pimsWidget:box id="details" title="Details" initialState="open" >
	<pimsForm:form action="/Update" id="ID" method="post" mode="view" >
		<pimsForm:formBlock id="blk1">
			<pimsForm:text name="${extension.extensionHook}:${Extension['PROP_NAME']}" value="${extension.exName}" alias="Name" helpText="The name of the Extension" validation="required:true" />			
			<pimsForm:text name="${extension.extensionHook}:${Extension['PROP_SEQSTRING']}" value="${extension.exSeq}" alias="DNA sequence" helpText="The DNA sequence of the Extension 5'- to 3'-" validation="dnaSequence:true, required:true" onchange="this.value=this.value.toUpperCase()" />			
            <pimsForm:text name="${extension.extensionHook}:${Extension['PROP_RELATEDPROTEINTAGSEQ']}" value="${extension.encodedTag}" alias="Related ${term} tag" helpText="${term} fusion sequence" onchange="this.value=extTagCheck(this.value); this.value=this.value.toUpperCase()" />         
            <pimsForm:text name="${extension.extensionHook}:${Extension['PROP_RESTRICTIONENZYME']}" value="${extension.restrictionSite}" alias="Restriction site" helpText="Restriction enzyme site" />
		
			<pimsForm:column1>			    
                <pimsForm:nonFormFieldInfo label="Lab Notebook"><c:out value="${extension.access.name}" /></pimsForm:nonFormFieldInfo>
       		</pimsForm:column1>
		</pimsForm:formBlock>
		
			<pimsForm:column2>
				<pimsForm:editSubmit />
			</pimsForm:column2>
	</pimsForm:form>
</pimsWidget:box>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />
