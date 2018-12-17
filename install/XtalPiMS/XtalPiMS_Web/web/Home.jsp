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

<c:set var="HeaderName" scope="page" value="Home" />
<%@include file="./WEB-INF/jspf/header-min.jspf"%>



<%-- HTML STARTS HERE --%>
<!-- Dependencies -->

<iframe id="yui-history-iframe" class="yui-history-iframe" src="blank.html"></iframe>
<form action=""><input id="yui-history-field" type="hidden" /></form>

<bricks:brickGrid columns="2" width="100%">
	<bricks:brickRow>
		<bricks:brick title="Recent Plate Inspections" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<div id="inspectionstable" class="yui-skin-sam"><img src="xtal/images/ajax-loader.gif" alt="Loading data..." title="Loading data..." /></div>
		</bricks:brick>
		<bricks:brick title="Recent Annotations" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<div id="annotationstable" class="yui-skin-sam"><img src="xtal/images/ajax-loader.gif" alt="Loading data..." title="Loading data..." /></div>
		</bricks:brick>
		
	</bricks:brickRow>
	<bricks:brickRow>
		<bricks:brick title="Recent Microscope Images" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<div id="microscope_images"><img src="xtal/images/ajax-loader.gif" alt="Loading data..." title="Loading data..." /></div>
		</bricks:brick>
		<bricks:brick title="My Groups" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<div id="groupstable" class="yui-skin-sam"><img src="xtal/images/ajax-loader.gif" alt="Loading data..." title="Loading data..." /></div>
		</bricks:brick>
	</bricks:brickRow>
	<%--
	<bricks:brickRow>
		<bricks:brick title="Welcome" width="2" height="1" bodyClass="bodyClass"
			headerClass="headerClass">
			<p style="background-color:yellow;font-size:120%;padding:10px;border:1px solid #000066">xtalPiMS has been updated
			with a new version of the ViewTrialDrops page! You can find some help on how
			to use the page from the Help menu on that page, or <a href="ViewTrialDrops3Help.jsp">here</a>.
			If you would prefer to continue to use the original page, go <a href="Preferences.jsp">here</a>
			and select "Version 1" from the drop drop-down list. This setting will last until you log out.</p>
			<p>Welcome to your <i>xtal</i>PIMS Dashboard. From here you will
			be able to monitor your crystallization trials, both within your lab
			and when you send it to other places, such as the synchrotron at the
			ESRF.</p>
			<p><i>xtal</i>PIMS is part of the Protein Information Management
			System (PIMS) which covers crystallization, i.e. creation of
			crystallization trials, monitoring of experiments through imaging
			systems, harvesting of crystals and managing data collection
			information (using developments from the e-HTPX Project).</p>
			<p>Please use the menu bar at the top of the page to navigate
			around the site</p>
			<p>If you have any feedback or comments you would like to make
			about <i>xtal</i>PIMS, please send us an email:</p>
			<center><a href="mailto:jon@strubi.ox.ac.uk"><img
				src="xtal/images/128x128/mail.png" style="border: 0;" alt="Mail Us" /></a></center>
		</bricks:brick>
		<bricks:brick title="Requested &amp; Planned Features" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<ul>
				<li>Crystallization Screen Management</li>
				<li>Screen conditions</li>
				<li>Comparing screen condition results</li>
				<li>Crystallogenesis</li>
				<li>Admin features</li>
				<li>e-HTPX / Trip Management features (transferring crystallization data to a synchrotron)</li>
			</ul>
		</bricks:brick>
		<bricks:brick title="Notes for Beta Testers" width="1" height="1" bodyClass="bodyClass" headerClass="headerClass">
			<p>
				Firstly thank you for agreeing to help test and develop the system.  Until we start 
				to develop this front page (which will provide a summary of your activities and highlight 
				new developments such as the latest imagings of your plates, etc., this page will provide 
				a reference into the development of the system
			</p>
			<p>
				If you have any suggestions, comments or anything, please send me an <a href="mailto:ian@strubi.ox.ac.uk">email</a>.  I would particularly welcome suggestions about what you would like to see on this page as obviously all the comments, etc. will disappear once the software is released and in general use.
			</p>
		</bricks:brick>
	</bricks:brickRow>
	--%> 
</bricks:brickGrid>

<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/utilities/utilities.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/container/container_core-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/history/history-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/json/json-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/lib/yui-2.7.0b/build/datatable/datatable-min.js"></script>
<script type="text/javascript" src="${pageContext.request['contextPath']}/js/xpims/DataFilter.js.ycomp.js"></script>

<script type="text/javascript">
PIMS.xtal.username = '<%=request.getRemoteUser()%>';
PIMS.xtal.sessionBookmark = '<%=(null != request.getSession().getAttribute("platesDataTable")) ? request.getSession().getAttribute("homeDataTable") : "" %>';
PIMS.xtal.recentBarcode = '<%=(null != request.getSession().getAttribute("recentBarcode")) ? request.getSession().getAttribute("recentBarcode") : "" %>';
PIMS.xtal.onDOMReady(PIMS.xtal.initHome);
</script>


<%-- HTML ENDS HERE --%>
<%@include file="./WEB-INF/jspf/footer.jspf"%>