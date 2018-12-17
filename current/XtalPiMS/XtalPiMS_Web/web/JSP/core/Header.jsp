<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     <%-- TODO <!DOCTYPE html> for HTML5 --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%--
Standard header for PiMS pages. Include /footer to balance tags opened in this JSP.

Example of including this header:

<%@include file="./WEB-INF/jspf/header-min.jspf"%>
    <jsp:param name="HeaderName" value="PiMS" /> - specify the page title
    <jsp:param name="bodyCSSClass" value="bricks" /> - optionally add CSS class
    <jsp:param name="extraStylesheets" value="custom/bricks" /> - optionally include stylesheets (IE and non-IE)
    <jsp:param name="extraStylesheets_IE" value="custom/bricks_IE" /> - optionally include stylesheets for IE
    <jsp:param name="extraStylesheets_nonIE" value="custom/bricks_non_IE" /> - optionally include stylesheets for non-IE
</jsp:include>

extraStylesheets, extraStylesheets_IE, extraStylesheets_nonIE:

Each of these is a comma-separated list of paths, relative to /skins/default/css, WITHOUT the .css or .dcss extension (allows us to change 
our minds about this later, as the CSS Constants mechanism is not really being used).

Stylesheet order is the same regardless of the order in which the params are supplied: extraStylesheets, then extraStylesheets_IE, 
then extraStylesheets_nonIE. Within each of these, the stylesheets are inserted in the order supplied. Remember that order can affect 
precedence of CSS rules.
--%>

<% request.setAttribute("isAdmin", request.isUserInRole("pims-administrator")); %>

<c:set var="perspectivePath" value="" scope="request"/>
<c:if test="${!empty sessionScope.perspective.name && 'standard'!=sessionScope.perspective.name}">
    <c:set var="perspectivePath" value="/"  scope="request"/>
</c:if>

<%--
  This block is to set the CSS class on the body element.
--%>
<c:set var="bodyCSSClass" value=""/>
<c:if test="${!empty param['bodyCSSClass']}">
  <c:set var="bodyCSSClass" value="${param['bodyCSSClass']}"/>
</c:if>


<head>
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta http-equiv="X-frame-options" content="sameorigin" />
<!-- Makes developing HTML easier <base href="http://${header['host']}/${pageContext.request.contextPath}/" /> -->
  
<c:if test="${!empty param.isInPopup}">
    <base target="_blank" />
</c:if>


<script type="text/javascript">
function ignoreBeforeLoad(e) {
    if (document.loaded) {
    	return;
    }
    if (!e) var e = window.event
    //handle event
    e.cancelBubble = true;
    if (e.stopPropagation) e.stopPropagation();
    alert("Please try again when the page is loaded"); 
}
if (document.addEventListener) {document.addEventListener('click',ignoreBeforeLoad,true);};
<%-- could else if (document.attachEvent){ document.attachEvent('onclick', ignoreBeforeLoad);  --%>
if (document.captureEvents) {
	// Firefox or WebKit
	document.captureEvents(Event.CLICK);
}
window.onerror = function(message, url ,linenumber)  {
    if (!document.captureEvents 
            && 'complete'!=document.readyState
            && 'interactive'!=document.readyState
    		
    ) {
    	// IE
        alert("Please try again when the page is loaded: "+document.readyState); 
        return true;
    }
    alert("Javascript error: "+message+"\nAt line: "+linenumber+" in page: "+url);
    return false;
}  
</script>

<link rel="shortcut icon" href="${pageContext.request['contextPath']}/skins/${PIMS_DISTRIBUTION}/favicon.ico" type="image/x-icon" />


<%-- 
*** Core stylesheets 
--%>
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/headerfooter.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/forms5.0.0.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/widgets5.0.0.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/list.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request['contextPath']}/css/calendarSkins/aqua/theme.css" title="Aqua" type="text/css" media="all" />
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/time.js"></script>
<!--[if IE]>
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/forms_IE.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/widgets_IE.css" type="text/css" />
<![endif]-->

<link rel="stylesheet" media="screen and (-webkit-device-pixel-ratio:0.75)" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/core/android.css" />
<%-- 
**********************************************
*                                            *
* Start of stylesheet inclusions to refactor *
*                                            *
**********************************************
--%>

<link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/construct.css" type="text/css" />
    
    <%-- obsolete CSS --%>	
   <c:if test="${!empty param['usePrimerManagementCSS']}">
  	<link rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/css/primerform.css" type="text/css" />
		<![if !IE]>
 		 	<link rel="stylesheet" href="${pageContext.request.contextPath}${perspectivePath}/css/primerform_nonIE.css" type="text/css" />
		<![endif]>
   </c:if>

<%-- 
********************************************
*                                          *
* End of stylesheet inclusions to refactor *
*                                          *
********************************************
--%>

<%-- *** Screen- and print-specific stuff in these two core stylesheets --%>
<link rel="stylesheet" media="screen" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/screen.css" type="text/css" />
<link rel="stylesheet" media="print" href="${pageContext.request.contextPath}${perspectivePath}/skins/default/css/core/print.css" type="text/css" />

<%-- *** Apply extra stylesheets, to supplement and/or override the core stylesheets. --%>
<c:if test="${!empty param['extraStylesheets']}">
    <c:forEach var="sheet" items="${fn:split(param['extraStylesheets'], ',')}">
        <c:set var ="sheet" value="${fn:trim(sheet)}" />
        <link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/<c:out value="${sheet}" />.css" type="text/css" />
    </c:forEach>    
</c:if>
<c:if test="${!empty param['extraStylesheets_IE']}">
<!--[if IE]>
    <c:forEach var="sheet" items="${fn:split(param['extraStylesheets_IE'], ',')}">
        <c:set var ="sheet" value="${fn:trim(sheet)}" />
        <link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/<c:out value="${sheet}" />.css" type="text/css" />
    </c:forEach>    
<![endif]-->
</c:if>
<c:if test="${!empty param['extraStylesheets_nonIE']}">
<![if !IE]>
    <c:forEach var="sheet" items="${fn:split(param['extraStylesheets_nonIE'], ',')}">
        <c:set var ="sheet" value="${fn:trim(sheet)}" />
        <link rel="stylesheet" href="${pageContext.request['contextPath']}${perspectivePath}/skins/default/css/<c:out value="${sheet}" />.css" type="text/css" />
    </c:forEach>    
<![endif]>
</c:if>





  <title><c:out value="${param['HeaderName']}" /></title>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/lib/prototype-1.7.1.js" ></script>

  <%-- Scripts for layout TODO can we move these to bottom of page? Moving calendar-setup.js gets and error  --%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/calendar.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/calendar-en.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/calendar-setup.js"></script>  
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/pims5.0.0.js" ></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/linewrap.js" ></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/widgets.js" ></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/formgrid.js" ></script>
  


</head>
<%-- may also need onpageshow="if (event.persisted) {onLoadPims();focusFirstElement();}" --%>
<body class="${bodyCSSClass}" onload="onLoadPims();focusFirstElement()">

<div id="contextmenu_real" onmouseout="hideContextMenu()" onmouseover="this.style.display='block'">Default</div>

<%--
* focusElement should be the first focusElement
--%>
<script type="text/javascript">
var contextPath="${pageContext.request.contextPath}";
var focusElement;
</script>


<c:choose><c:when test="${empty param.isInPopup && empty param.isInModalWindow}">

<script type="text/javascript">
// if !isInPopup and !isInModalWindow, we aren't meant to be in an iframe
// - so if we are, bust out of it (PIMS-2980)
if (top.location != self.location) {
    top.location = self.location.href;
}
</script>


	<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>
	<div class="printonly">
	      <c:choose>
	        <c:when test="${!empty username}">
	          <div style="margin:0 auto; text-align:center; width:auto; border:1px solid black;font-family:arial,helvetica,sans-serif;padding:0.2em;margin:0 0.5em 0.5em 0">
	            <img style="vertical-align:middle;" src="${pageContext.request.contextPath}/skins/${PIMS_DISTRIBUTION }/login_logo.png" height="43" alt="PIMS" /> Printed for <strong>${username}</strong>
	          </div>
	        </c:when><c:otherwise>
	                <img style="" src="${pageContext.request.contextPath}/skins/${PIMS_DISTRIBUTION }/login_logo.png" height="43" alt="PIMS" />
	        </c:otherwise>
	      </c:choose>
	</div>
	
	<div id="header" class="noprint">
	 <c:set var="visible" value="disabled='disabled'"/>
	   <c:if test="${pageContext.request['method']!='POST'}">
			  <c:set var="visible" value=""/>
	   </c:if>


	
	  	<form id="perspective"
	        	action="${pageContext.request.contextPath}/View/" method="post"
	        	class="perspective menubar_right">
	        	
	        	<%-- style does not work in  Android --%>
	   <c:if test="${!fn:contains(header['User-Agent'], 'Android')}" >
	      <img src="${pageContext.request['contextPath']}/skins/${PIMS_DISTRIBUTION }/header_logo.png" alt="">
          <a class="menubar_right" id="quicksearchlink" onclick="showQuickSearch();return false;" href="#">Search</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   </c:if> 	
	        	Perspective:&nbsp;
	      <select name="perspective" ${visible} onchange="document.forms['perspective'].submit()" >
	        <option value="${sessionScope.perspective.name}" selected="selected">&nbsp;${sessionScope.perspective.name}</option>
	<%--  Perspective list
	--%>
	         <c:forEach var="perspectiveName" items="${sessionScope['enabledPerspectiveNames']}">
		         <c:if test="${perspectiveName!=sessionScope.perspective.name}">
		          <option value="${perspectiveName}"  >${perspectiveName} </option>
		          </c:if>
	         </c:forEach>
	         <c:if test="${isAdmin}" >
	              <option value="admin"  >admin </option>
	         </c:if>
	      </select>
	    </form>

<ul id="menu"> 	
  <!-- PiMS Home -->
  <li ><a onclick="return warnChange()" href="${pageContext.request.contextPath}/">Home</a>
    <!-- home submenu would be here -->
  </li>
  <!-- login/logout -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()">
    <c:choose>
      <c:when test="${empty username}">
        <a onclick="return warnChange()" href="${pageContext.request.contextPath}/Login" id="logInOutLink">Log in</a>
      </c:when><c:otherwise>
        <a onclick="return warnChange()" href="${pageContext.request.contextPath}/public/Logout" id="logInOutLink">Log out ${username}</a>
        <ul style="" class="submenu">
          <li><a onclick="return warnChange()" href="${pageContext.request.contextPath}/update/PasswordChange" >New Password</a></li>
        </ul>
      </c:otherwise>
    </c:choose>
  </li>

	<c:choose><c:when test="${empty sessionScope.perspective.name}">
      <script type="text/javascript" src="${pageContext.request.contextPath}/menu/standard.js"></script>
    </c:when><c:otherwise>
      <script type="text/javascript" src="${pageContext.request.contextPath}/menu/${sessionScope.perspective.name}.js"></script>
    </c:otherwise></c:choose>

    <script type="text/javascript">
      if (!submenus) {var submenus ={};}
      initMainMenu(submenus, '${pageContext.request.contextPath}');
    </script> 
   
  <!-- History menu -->
  <li onmouseover="showMenu(this)" onmouseout="hideMenus()"><a onclick="return warnChange()" href="${pageContext.request.contextPath}/JSP/core/MRUInfo.jsp">History</a>
    <ul style="width:auto" class="submenu">
      <%-- MRUs --%>
      <c:set var="menuMRUs" scope="request" value="<%= org.pimslims.presentation.mru.MRUController.getDisplayableMRUs(request.getRemoteUser()) %>" />
      <jsp:include page="/JSP/core/historyMenu.jsp" />
    </ul>
  </li>
</ul>	
	</div>

</c:when>
<c:otherwise>
  <%--nothing--%>
</c:otherwise></c:choose>


<%-- TODO it probably isn't necessary to inline this, browsers block for javascript load --%>
<script type="text/javascript">/* <![CDATA[ */
	<jsp:include page="/javascript/pims/inlinejavascript.js" />
/* ]]> */</script>


<!--[if lt IE 9]>
<div style="border:1px solid red; color:red; background-color:#fcc; font-weight:bold; text-align:center">
<p>It appears that you are using an old version of Internet Explorer. PiMS only supports IE9, 
Firefox 3 or greater, Chrome, and Safari 4 or above. It is very likely that some parts of PiMS will not work as expected.</p>
</div>
<![endif]-->
<![if !IE]>
<script type="text/javascript">
//<![CDATA[
if(navigator.userAgent.indexOf("Firefox") == -1 
		&& navigator.userAgent.indexOf("WebKit") == -1 
		&& (!navigator.vendor || -1==navigator.vendor.indexOf("Apple"))){
document.write('<div style="clear:both; border:1px solid red; color:red; background-color:#fcc; font-weight:bold; text-align:center">'+
'<span>It appears that you are using as browser: '+navigator.userAgent +' supplied by: '+navigator.vendor
+'. PiMS only supports  '+
'Firefox 3 or above, Internet Explorer 9 or above, Chrome, and Safari 4 or above.<br/>It is very likely that some parts of PiMS will not work as expected.<\/span>'+
'<\/div>');
}
//]]>
</script>
<![endif]>

<!--
 * This div should not appear on the page unless attribute selectors fail.
 * Such a failure is caused in IE7 when, e.g., content precedes the doctype.
-->
<div id="doctypesniffer">Attribute selector fail - check for content before doctype</div>

<div id="contentWrapper"><div id="content">
<!-- end of header -->
