<%-- 
    Document   : ViewMicroscopeImages.jsp
    Currently broken. Based on old ViewTimeCourse.
    Best fix is probably to make a new one based on current
    time course view.
    
    Old header.jspf copied here, because it is the only live page 
    where it was still used.
--%> 

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="HeaderName" scope="page" value="View Microscope Images" />

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> 

<%@page session = "true" %>
<%@page contentType  = "text/html"%>
<%@page pageEncoding = "UTF-8"%>
<%@page isThreadSafe="false" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache,no-store,max-age=0" />
    <meta http-equiv="expires" content="Sun, 15 Jan 1998 17:00:00 GMT" />
    
    <title>[xtalPIMS] : <c:out value="${pageScope['HeaderName']}"/></title>
<link rel="shortcut icon" href="${pageContext.request['contextPath']}/xtal/images/faviconxp.png" />
    
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/layout.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/header.css" type="text/css" />
    
  <c:if test="${!empty param['usePlateExperimentCSS']}">
      <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/plateexperiment.css" type="text/css" />
  </c:if>    
    <!-- This can be made unconditional once leeds construct pages are redesigned -->
<c:if test="${!param.disableFormCSS}">
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/skins/default/css/core/forms.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/skins/default/css/core/widgets.css" type="text/css" />
</c:if>
<!--[if IE]>
  <link rel="stylesheet" href="${pageContext.request['contextPath']}/skins/default/css/core/forms-IE.dcss" type="text/css" />
  <link rel="stylesheet" href="${pageContext.request['contextPath']}/skins/default/css/core/widgets-IE.dcss" type="text/css" />
<![endif]-->
    
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/list.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/pims.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/viewtrialdrops1.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/print.css" type="text/css" media="print" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/calendar/assets/skins/sam/calendar.css" />

    
    
    <!--CSS file (default YUI Sam Skin) -->
    <link type="text/css" rel="stylesheet" href="${pageContext.request['contextPath']}/css/datatable.css"/>
    <!--CSS files for YUI Menus Sam Skin -->
    <link type="text/css" rel="stylesheet" href="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/menu/assets/skins/sam/menu.css"/>     
    <link type="text/css" rel="stylesheet" href="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/autocomplete/assets/skins/sam/autocomplete.css"/>     
    <!--CSS file for the Bricks -->
    <link type="text/css" rel="stylesheet" href="${pageContext.request['contextPath']}/css/bricks.css"/>
    <!--xtalPIMS javascript core -->
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/xtalpims.js"></script>
    <!--Menu Javascript -->
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/menubar.js"></script>


    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/menubar.js"></script>

  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/lib/prototype/prototype-1.6.0.2.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/pims.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/widgets.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/validation.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/formgrid.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/experimentGroup.js" ></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/javascript/pims/modalWindow.js"></script>

<script type="text/javascript">
    <jsp:include page="/javascript/pims/inlinejavascript.js" />
</script>
    <!--Script to fix PNG problem in IE 6 and below!-->
<!--[if lt IE 7.]>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/pngfix.js" defer="defer"></script>
<![endif]-->
<%{ request.setAttribute("username", org.pimslims.servlet.PIMSServlet.getUsername(request)); }%>    
</head>

<body onload="setUpMenus()">


<div id="pageheader" class="noprint">    
    <div id="menubackground"></div>
    <div id="menuContainer" class="noprint">
        <div id="menu">
            <%@include file="./menubar.jspf"%>
            
        </div>
    </div>
</div>
<script type="text/javascript">
    var contextPath = "<%=request.getContextPath()%>";
</script>
<span id="contextPath" style="display:none"><%=request.getContextPath()%></span>

<div id="contentWrapper"><div id="content">



<%-- HTML STARTS HERE --%>

<div id="microimages" style="float:left; height:200px;">
	<div id="images" style="position:absolute; height:200px; top:78px; left:0px;overflow:auto"></div>
</div>

<div id="imageHolder" style="float:left;clear:both; padding:6px; vertical-align:top;">
	<div style="float:left">
		<img src="${pageContext.request['contextPath']}/images/ajax-loader.gif" alt="Loading..." id="imageView" style="height:570px" />
	</div>
	<div id="imageInfo" style="float:left"></div>
</div>

<script type="text/javascript">
	<!--
	var barcode = '<%=request.getParameter("barcode")%>';
	var well = '<%=request.getParameter("well")%>';
	var date = parseInt('<%=request.getParameter("date")%>');
	-->
</script>
    <!--YAHOO Javascript core -->
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/calendar/calendar-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/history/history-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/json/json-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datasource/datasource-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datatable/datatable-beta-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/xtalpims.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/viewmicroimages.js"></script>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>