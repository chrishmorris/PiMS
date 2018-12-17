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
<%@taglib prefix="bricks" uri="http://www.pims-lims.org/bricks"%>
<%@taglib prefix="xtalpims" uri="http://www.pims-lims.org/xtalpims" %>
<head>
<style type="text/css">
body { font-family: arial,helvetica,sans-serif; color:#006; padding:0 20%; }
#loadingimg { display:none; }
label { display:block; clear:both; margin:0.5em 0; }
label input { float:right; }
span.required { color:#900; font-weight:bold; }
div.error {background-color:red; border-radius:0.5em; color:white; padding:0.25em 0.5em; }
div.message {background-color:#006; border-radius:0.5em; color:white; padding:0.25em 0.5em; }
</style>
</head>
<body>
<img src="${pageContext.request['contextPath']}/xtal/images/ajax-loader.gif" alt="Loading data..." title="Loading data..." id="loadingimg" /></body>
<form action="${pageContext.request['contextPath']}/NewDiamondProject/" method="post" id="projectform" onsubmit="return validateProjectForm()">
<c:choose><c:when test="${!empty error}">
	<div class="error">${error}</div>
</c:when><c:when test="${!empty message}">
	<div class="message">${message}</div>
</c:when></c:choose>
<label>Diamond proposal<span class="required">*</span><input type="text" name="proposal" id="proposal" value="${proposal}"/></label>
<label>Protein acronym<span class="required">*</span><input type="text" name="acronym" id="acronym" value="${acronym}"/></label>
<label><input type="submit" name="submit" value="Create project"/></label>
</form>
<br/><a target="_top" href="${pageContext.request['contextPath']}/CreateTrialPlate">Record a new plate</a>
<script type="text/javascript">
function validateProjectForm(){
	var proposalBox=document.getElementById("proposal");	
	var acronymBox=document.getElementById("acronym");	
	var proposal=proposalBox.value.trim();
	var acronym=acronymBox.value.trim();
	if(""==proposal||""==acronym){
		alert("Both proposal and acronym are required.");
		return false;
	}
	if(!proposal.match(/^[A-Za-z]{2}[0-9]+$/i)){
		alert("Proposal codes are two letters followed by numbers, e.g., 'mx4025'.");
		return false;
	}
	
	return true;
}
</script>
</html>