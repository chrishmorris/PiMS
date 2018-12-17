<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 

<%@page session = "true" %>
<%@page contentType = "text/html"%>
<%@page pageEncoding = "UTF-8"%>
<%@page isThreadSafe="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="pims" uri="http://www.pims-lims.org" %>
<%@taglib prefix="xtalpims" uri="http://www.pims-lims.org/xtalpims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<c:set var="HeaderName" scope="page" value="Upload Inspection" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>
<%-- HTML STARTS HERE --%>


This does not currently work
<pimsWidget:box title="Upload Inspection" initialState="fixed">
<pimsForm:form method="post" enctype="multipart/form-data" action="/InspectionUpload" mode="edit" >
   <pimsForm:formBlock>
         <pimsForm:column1>
         Upload order XML file to create new screen or update existing screen which found by name.
         <a href='${pageContext.request.contextPath}/example/order.xsd'>This file</a> specifies the schema.<br/> <br/> 
        <pimsForm:formItemLabel name="orderUpload" alias="Choose Order File" 
        helpText="If you want to upload an xml order file, choose the file here"  />
            <div class="formfield" >
            <input type="file" name="orderUpload" />
            </div>
     <input type="submit" value="Upload file" onclick="dontWarn()" />
     </pimsForm:column1>
   
   </pimsForm:formBlock>
   
 </pimsForm:form>
</pimsWidget:box><%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>