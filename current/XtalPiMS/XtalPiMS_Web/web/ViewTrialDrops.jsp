<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="/WEB-INF/utils.tld" prefix="utils" %>
<c:set var="HeaderName" scope="page" value="View Trial Drops" />
<head>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache,no-store,max-age=0" />
	<meta http-equiv="expires" content="0" />

    <title>[xtalPIMS] : View Trial Drops</title>
<link rel="shortcut icon" href="${pageContext.request['contextPath']}/xtal/images/faviconxp.png" />

    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/layout.css" type="text/css" />
    <link rel="stylesheet" href="${pageContext.request['contextPath']}/css/header.css" type="text/css" />
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request['contextPath']}/css/viewtrialdrops3.css"/>
    <link media="print" rel="stylesheet" type="text/css" href="${pageContext.request['contextPath']}/css/viewtrialdrops3print.css"/>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/menubar.js"></script>
    
    <%-- TODO 1.7.1  
  <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/lib/prototype-1.7.1.js" ></script> --%>
  <script type="text/javascript" src="${pageContext.request.contextPath}/xtal/javascript/prototype-1.6.0.2.js" ></script>
  
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/wz_jsgraphics-3.05.js" ></script>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DropView.js" ></script>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/TrialDropList.js" ></script>
    <script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/viewtrialdrops3.js" ></script>
<script type="text/javascript">
    var contextPath = "<%=request.getContextPath()%>";
</script>
</head>
<body onload="setUpMenus();initTrialDrops(${param['mayUpdate']})">
<%-- TODO use xtal/menubar.jsp --%>
<div id="menu" class="noprint" style="background-color: #000066; border: 2px solid; z-index: 10"><%@include file="/WEB-INF/jspf/menubar.jspf"%></div>
<script type="text/javascript">
	//variables for describing the current well
	var barcode = '${utils:escapeJS(param['barcode'])}';
	<% request.getSession().setAttribute("recentBarcode", request.getParameter("barcode")); %>
	var inspection = '<%=(null == request.getParameter("name")) ? "-" : request.getParameter("name")%>';
	var initialWell = '<%=(null == request.getParameter("well")) ? "" : request.getParameter("well")%>';
	var subposition = '<%=(null == request.getParameter("subPosition")) ? "-1" : request.getParameter("subPosition")%>';
		
	// Overkill?
	//var initialDate = parseInt('<%=request.getParameter("name")%>'); 
	var user = '<%=request.getRemoteUser()%>';
</script>

<% if (request.getParameter("barcode") == null) { %>
	<h3>Please navigate to this page through the menus above, you cannot access this page directly without specifying a correct barcode and inspection name.</h3>
<%} else {%>

<div style="position:relative; margin-top:10px">
<div class="box" id="dropview" style="min-width:400px">
	<h3 style="font-size:14px;margin:0;" id="dropview_header">
	<span class="noprint" style="float:right;">
	Zoom:<select id="zoomselect" onchange="doZoom(this)" style="font-size:80%"><option value="fit">Fit</option><option selected="selected" value="100">100%</option><option value="200">200%</option><option value="400">400%</option></select>&nbsp;&nbsp;&nbsp;
	Annotate:<select style="font-size:80%"><option>Annotate</option></select><input type="checkbox" checked="checked" />and move to next
	</span>
	Image
	</h3>
	<div class="boxcontent" id="mainimg">&nbsp;</div>
	<div style="font-size:0;clear:both">&nbsp;</div>
</div>

<div id="infobar">
	<form action="#">
	<div class="box" id="navigation">
		<h3 style="font-size:14px;margin:0;" id="navigation_head">Navigation</h3>
		<div class="boxcontent">
			<div id="controls" style="font-size:80%;">
<div id="player" style="text-align:right">
	<a class="playericon playericonfirst" id="playerfirst" href="#" onclick="return false">First</a>
	<a class="playericon playericonprev" id="playerprev" href="#" onclick="return false">Prev</a>
	<a class="playericon playericonplay" href="#" id="startmovie" onclick="return false">Play</a>
	<a class="playericon playericonnext" id="playernext" href="#" onclick="return false">Next</a>
	<a class="playericon playericonlast" id="playerlast" href="#" onclick="return false">Last</a>
	Delay: <input type="button" name="delaydown" id="delaydown" value="-"
	/><input readonly="readonly" type="text" name="delay" id="delay" 
	/><input type="button" name="delayup" id="delayup" value="+" />
</div>
			</div>
			<div id="wells">
				<p>Loading well data...</p>
			</div>
		</div>
	</div>
	</form>

	<div class="box" id="information">
		<h3 style="font-size:14px;margin:0;" id="information_header">Information</h3>		
		<div class="boxcontent" id="dropinfo">
		<img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/waiting.gif" alt="Loading..."/>
		</div>
	</div>
	
	<div style="display:none" id="imagecache">&nbsp;</div>
	<div class="box noprint" id="timecoursebox">
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
				<img src="${pageContext.request['contextPath']}/skins/default/images/icons/actions/waiting.gif" alt="Loading..."/>
			</div>
		</div>
	</div>
	<br/>
	
    <div class="box" >
      <h3 style="font-size:14px;margin:0;" >Mouse location</h3>
      <div class="boxcontent" >
        x:<input id="xElement" />microns<br />
        y:<input id="yElement" />microns
      </div>
    </div>
    <br />
	<c:if  test="${!empty subPositionLinks}">
     <div class="box" id="subPositionLinks">
     <h3 style="font-size:14px;margin:0;" >Inspection</h3>
        <div class="boxcontent" id="dropinfo">
		      Name: <c:out value="${inspection.name}" /> <br/>
		      Time: <c:out value="${inspection.completionTime.time}" /><br/>
		      Description: <c:out value="${inspection.details}" /><br/>
		   <h3 style="font-size:14px;margin:0;"> <a title="Edit this inspection"  class="noprint" href='${pageContext.request.contextPath}/Update/Inspection?name=<c:out value="${inspection.name}" />'>Plate View</a></h3>
		     </div> 
        <c:forEach var="entry" items="${subPositionLinks}">
           <h3 style="font-size:14px;margin:0;" class="noprint"> <a href='${pageContext.request.contextPath}/ViewTrialDrops.jsp?<c:out value="${entry.value} "/>'><c:out value="${entry.key}" /></a></h3>
        </c:forEach>
        </div>
    </c:if>
    </div>
</div>

<%}%>

</body>
</html>
