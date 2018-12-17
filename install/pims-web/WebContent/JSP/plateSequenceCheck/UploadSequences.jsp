<%--
A page for upload sequences for further comparison with certain types of plate experiments  
Author: Peter Troshin
Date: November 2007
--%>

<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<jsp:include page="/JSP/core/Header.jsp">
	<jsp:param name="HeaderName" value="Load sequences archive" />
</jsp:include>

<!-- OLD -->


<form action="${pageContext.request.contextPath}/update/SequenceUploader" 
			enctype="multipart/form-data"	
			method="post"
			style="background-color:#ccf; padding:0.25em;margin-top:0.5em"
			>

<br />						      
<span align="left" style="font-weight: bold;"> 
Sequence files can contain either clear sequence or sequence in FASTA format. Sequence files must have .seq extension. They may contain information about a well they are related to. 
In this case well information should immediately prefix the extension e.g. A01.seq. Position information must be 3 letters long, thus position like A1 should be prefixed with 0. 
<p>You are free to add any other texturial information in the name of sequencing files before the position.</p>  
<p>Alternatively you can upload a single text file which contain a list of sequences in FASTA. </p>
<p>Please navigate to the ZIP file which contains your sequences.</p>

Then click 'Upload file' button.</span>
<br />
<input style="float:right; width:auto;"	type="submit" 
			 name="submit" value="Upload file" 
			 onClick="if (''==document.getElementById('sequences').value) {alert('Please choose a file to upload'); return false;} return true"/> 
	Choose a file
	<input type="file" name="sequences" id="sequences"/> 
</form>



<br />


<jsp:include page="/JSP/core/Footer.jsp" />
