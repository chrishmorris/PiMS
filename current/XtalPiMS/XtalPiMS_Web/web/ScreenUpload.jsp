<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*"  %>
<%@ page import="org.pimslims.model.people.Person"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value='Upload screen' />
</jsp:include>

<a href="http://pisampler.mrc-lmb.cam.ac.uk/" >Design a screen with PiSampler</a> <br /> <br />  
<pimsWidget:box title="Upload Screen" initialState="fixed">
<pimsForm:form method="post" enctype="multipart/form-data" action="/update/ScreenUpload" mode="edit" >
   <pimsForm:formBlock>
         Upload a file to create a new screen, or update an existing screen.
         <br/> <br/> 
        <pimsForm:formItemLabel name="screenUpload" alias="Upload Screen File" helpText="If you want to upload an xml screen file, choose the file here"  />
            <div class="formfield" >
            <input type="file" name="screenUpload" /> 
            <input type="submit" value="Upload file" onclick="dontWarn()" />
            </div>   
   </pimsForm:formBlock>
   Several file formats are accepted. Here is documentation or examples:
   <ul>
     <li><a target="example" href='${pageContext.request.contextPath}/example/xtalPiMS_York_SilverBullet.xml'>PiMS XML</a></li>
     <!-- <li><a target="example"  href='${pageContext.request.contextPath}/example/****'>PiMS CSV</a></li> -->
     <li><a  target="example" href='http://pisampler.mrc-lmb.cam.ac.uk/help.html'>PiSampler CSV</a></li>
     <li><a  target="example" href='${pageContext.request.contextPath}/example/HKI Random II.csv'>Rhombix CSV</a></li>
   </ul>
 </pimsForm:form>
</pimsWidget:box>

<jsp:include page="/JSP/core/Footer.jsp" />
