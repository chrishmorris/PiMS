<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%-- Tells browsers to reset current session data --%>
<% response.setStatus(401); %>

<head>
<title>Log in to PiMS</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/skins/default/images/favicon.ico" type="image/x-icon" />

<link rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/headerfooter.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/widgets5.0.0.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/forms5.0.0.css" type="text/css" />
<link media="print" rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/print.css" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/lib/prototype-1.7.1.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/validation.js" ></script>
<script type="text/javascript"><!--//--><![CDATA[//><!--
	<jsp:include page="/javascript/pims/inlinejavascript.js" />
//--><!]]></script>

<%-- Tells IE to reset current session data --%>
<script type="text/javascript">document.execCommand('ClearAuthenticationCache', 'false');</script>

</head>
<body onload="document.getElementById('j_username').focus()">

<div id="header" class="noprint">
      <ul id="menu">
        <li class="level1"><a href="${pageContext.request.contextPath}/">Home</a></li>
      </ul>
</div>

<div id="contentWrapper">
<div id="content">
<!--main page content goes in here-->

<div style="margin:10em 33%">
	<pimsWidget:box initialState="fixed" title="Log in to PiMS" extraClasses="noscroll">
	    <div style=""><!--attach form background image to this div-->
	    	<div style="text-align:center">
	    		<img src="${pageContext.request.contextPath}/skins/academic/login_logo.png"  alt="" style="padding-top:.75em"/>
	    	</div>
		<pimsForm:form action="/j_security_check" method="post" mode="edit">
		<pimsForm:formBlock>
		   <pimsForm:text name="j_username" alias="Username" validation="required:true"/>
		   <pimsForm:password name="j_password" alias="Password" validation="required:true"/>
		   <pimsForm:nonFormFieldInfo>
		     <input style="float:right;" type="submit" value="Log in" id="submitbutton" />
		   </pimsForm:nonFormFieldInfo>
		</pimsForm:formBlock>
		</pimsForm:form>
		</div>
	</pimsWidget:box>
</div>

<script type="text/javascript">
$("j_password").onfocus=function(){ $("submitbutton").disabled=""; }
//dummy, prevents script error
function onEdit(){}
</script>

<!--end of main page content-->
</div><!--end "content"-->
</div><!--end "main"-->

</body>
</html>
