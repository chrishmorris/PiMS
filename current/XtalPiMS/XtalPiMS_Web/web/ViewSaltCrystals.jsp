<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="HeaderName" scope="page" value="View Trial Drops" />
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache,no-store,max-age=0" />
	<meta http-equiv="expires" content="0" />

	<title>[xtalPIMS] : View Salt Crystals</title>
<link rel="shortcut icon" href="${pageContext.request['contextPath']}/xtal/images/faviconxp.png" />


	<link rel="stylesheet" href="${pageContext.request['contextPath']}/css/layout.css" type="text/css" />
	<link rel="stylesheet" href="${pageContext.request['contextPath']}/css/header.css" type="text/css" />
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request['contextPath']}/css/viewtrialdrops3.css"/>
	<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/menubar.js"></script>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/prototype-1.7.1.js" ></script>
	<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/wz_jsgraphics-3.05.js" ></script>
	<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DropView.js" ></script>
	<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/TrialDropList.js" ></script>
	<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/viewsaltcrystals.js" ></script>
<script type="text/javascript">
	var contextPath = "<%=request.getContextPath()%>";
</script>
</head>
<body onload="setUpMenus();initTrialDrops(${param['mayUpdate']})">
<div id="menu" class="noprint" style="background-color: #000066; border: 2px solid; z-index: 10"><%@include file="/WEB-INF/jspf/menubar.jspf"%></div>
<script type="text/javascript">
	var localName = '<%=request.getParameter("localName")%>';
	var well = '<%=request.getParameter("well")%>';
</script>

<% if ((request.getParameter("localName") == null) || (request.getParameter("well") == null)){ %>
	<h3>Please navigate to this page through the menus above, you cannot access this page directly without specifying a correct screen name and well.</h3>
<%} else {%>
<div style="position:relative; margin-top:10px">
<div class="box" id="drops" style="width:100%">
	<h3 style="font-size:14px;margin:0;" id="drops_header">
	Example Salt Crystals
	</h3>
	<div class="boxcontent" id="drops_images" style="overflow-y:auto"><img src="${pageContext.request['contextPath']}/images/ajax-loader.gif" alt="Loading..." id="imageView" style="width:140px" /></div>
	<div style="font-size:0;clear:both">&nbsp;</div>
</div>
<div id="tcbox">
<div class="box" id="dropview" style="min-width:400px">
	<h3 style="font-size:14px;margin:0;" id="dropview_header">
	<span style="float:right;">
	Zoom:<select id="zoomselect" onchange="doZoom(this)" style="font-size:80%"><option value="fit">Fit</option><option selected="selected" value="100">100%</option><option value="200">200%</option><option value="400">400%</option></select>&nbsp;&nbsp;&nbsp;
	Annotate:<select style="font-size:80%"><option>Annotate</option></select><input type="checkbox" checked="checked" />and move to next
	</span>
	Image
	</h3>
	<div class="boxcontent" id="mainimg">&nbsp;</div>
	<div style="font-size:0;clear:both">&nbsp;</div>
</div>

<div id="infobar">
	<div class="box" id="timecoursebox">
		<h3 style="font-size:14px;margin:0;" id="timecourse_header"><span style="float:right;cursor:pointer" onclick="closeFullTimeCourse()">[x]</span>Timecourse</h3>
		<div class="boxcontent">
			<div id="tccontrols" style="font-size:80%;">
<div id="tcplayer" style="text-align:right">
	<a class="playericon playericonfirst" id="tcplayerfirst" href="#" onclick="return false">First</a>
	<a class="playericon playericonprev" id="tcplayerprev" href="#" onclick="return false">Prev</a>
	<a class="playericon playericonplay" href="#" id="tcstartmovie" onclick="return false">Play</a>
	<a class="playericon playericonnext" id="tcplayernext" href="#" onclick="return false">Next</a>
	<a class="playericon playericonlast" id="tcplayerlast" href="#" onclick="return false">Last</a>
	Delay: <input type="button" name="tcdelaydown" id="tcdelaydown" value="-"
	/><input readonly="readonly" type="text" name="tcdelay" id="tcdelay" 
	/><input type="button" name="tcdelayup" id="tcdelayup" value="+" />
</div>
			</div>
			<div id="fulltimecourse">
				<img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/waiting.gif" />
			</div>
		</div>
	</div>
</div>
</div>
</div>

<%-- TODO Remove below --%>
<div style="position:relative; margin-top:10px;display:none">
</div>

<%}%>

<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>
