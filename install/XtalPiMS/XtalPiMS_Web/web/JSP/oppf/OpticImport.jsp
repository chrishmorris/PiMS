<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<%@ page contentType="text/html; charset=utf-8" language="java" 
import="java.util.*,org.pimslims.presentation.*,org.pimslims.lab.*"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>&#13;
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>&#13;
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>&#13;
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>&#13;

<c:catch var="error">
<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Import Targets from Optic' />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
</jsp:include>

<%-- page body here --%>
<br />
<%-- 
<c:set var="breadcrumbs">
    <a href="${pageContext.request.contextPath}/Search/org.pimslims.model.location.Location">Locations</a>
</c:set>
--%>
<c:set var="title" value="Import Targets from Optic"/>
<%-- 
<c:set var="actions">
 <a href="${pageContext.request.contextPath}/help/HelpExtensions.jsp">Help</a>
</c:set>
--%>

<pimsWidget:pageTitle breadcrumbs="${breadcrumbs}" title="${title}" actions="${actions}" icon="${icon}"  />

<%--Later set this based on type of Extension to create depending on where this is called from
and also use to set the term as in ExtensionView--%>
<c:set var="dir" value="forward" scope="page" />

<pimsWidget:box id="box1" title="Details" initialState="open" >
    <pimsForm:form method="post" action="/OpticImporter" mode="create" enctype="multipart/form-data">

        <pimsForm:formBlock>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;Optic targets can be uploaded into PiMS in one of two ways:<br />
            1. Enter a list of comma separated Optic target numbers"  </p>
        </pimsForm:formBlock>
        
        <pimsForm:formBlock>
            <pimsForm:formItem name="$opticElement" alias="Enter a list of comma separated Optic target numbers" >
                <pimsForm:formItemLabel name="opticElement" alias="Optic target numbers" helpText="Enter a list of comma separated Optic target numbers" validation="${validation}" datatype="" />
                <div class="formfield" >
                    <input ${disabled} type="text" name="opticElement" id="opticElement" />
                </div>
            </pimsForm:formItem>
        </pimsForm:formBlock>

        <pimsForm:formBlock>
            <p>&nbsp;&nbsp;&nbsp;&nbsp;or<br />
            2. If your targets are in a spreadsheet, choose the file here. Targets will be loaded from the column headed 'OPTIC id'.</p>
        </pimsForm:formBlock>
        <pimsForm:formBlock>
            <pimsForm:formItemLabel name="spreadsheet" alias="spreadsheet"  />
            <div class="formfield" >
            <input type="file" name="spreadsheet" id="spreadsheet" />
            </div>
        </pimsForm:formBlock>
        
        <pimsForm:formBlock>
            <p>Choose the laboratory notebook the targets will be assigned to:</p>
        </pimsForm:formBlock>
        
        <pimsForm:formBlock>
        <pimsForm:column1>  
            <pimsForm:labNotebookField name="labNotebookId" helpText="The lab notebook these targets will join" objects="${accessObjects}" />
        </pimsForm:column1>
        </pimsForm:formBlock>       
        
        <pimsForm:formBlock>    
            <pimsForm:submitButton buttonText="Upload"  />
        </pimsForm:formBlock>
        
    </pimsForm:form>    
</pimsWidget:box>
</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}<error /></p>
</c:if>    
<jsp:include page="/JSP/core/Footer.jsp" />

