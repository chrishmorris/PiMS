<%--
Display a list of files associated with a model object
This JSP is called by org.pimslims.servlet.ListFiles
and also from the experiment support.
The caller must supply the header and footer for the page.


Author: Petr Troshin
Date: Jan 2007
--%>
<!-- UploadFiles.jsp -->
<%@ taglib prefix="utils" uri="/WEB-INF/utils.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<jsp:useBean id="modelObject" scope="request" type="org.pimslims.presentation.ModelObjectBean" />
<jsp:useBean id="mayUpdate" scope="request" type="java.lang.Boolean" />

<c:if test="${empty fileFName}"> 
	<c:set var="fileFName" value="fileToUpload" scope="request" />
</c:if>


<form action="/update/ListFiles/${modelObject.hook}" method="post" enctype="multipart/form-data" >
    <%-- TODO CSRF token --%>
<c:choose>
<c:when test="${mayUpdate}">
			<div class="formrow"><label for="fileDescription">File description:</label>
			<input style="width: 32em;" ${readonly} type="text" name="fileDescription"  id="fileDescription" maxlength="253" value="${descrValue}" /> </div>
      <div class="formrow"><label for="file">File:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
          &nbsp;&nbsp;&nbsp;<input style="width: 32em; " type="file" name="${fileFName}" id="${fileFName}" /> 
      </div>
      <input type="submit" value="${buttonName eq null? 'Upload' : buttonName}"  name="userreq" 
      onClick="if (''==document.getElementById('${fileFName}').value) {alert('Please choose a file to upload'); return false;} return true" />    
</c:when>
<c:otherwise>
	<br />Note: You do not have access rights to upload the files for this ${metaClass.alias}
</c:otherwise>
</c:choose>
</form>

<!-- /UploadFiles.jsp -->

<!-- OLD -->
