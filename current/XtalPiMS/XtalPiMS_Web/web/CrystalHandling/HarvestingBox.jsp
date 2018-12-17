<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%--
Shows the provided xtal coordinates on the image. If readonly, shows coordinates as text, otherwise allows setting coords for new xtal.
Author: Ed Daniel
Date: April 2012
--%>
<%@ taglib uri="/WEB-INF/pims.tld" prefix="pims" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java"  %>

<c:set var="initialState" value="open" />
<c:set var="wellUrl" value="${pageContext.request.contextPath}/update/SelectCrystal?barcode=${barcode}&well=${well}" />
<c:set var="cursor" value="auto" />
<c:set var="extraHeader" value="" />
<c:choose><c:when test="${empty crystal}">
	<c:set var="initialState" value="fixed" />
	<c:set var="cursor" value="crosshair" />
	<c:set var="extraHeader" value="" />
</c:when><c:otherwise>
	<c:if test="${isAdmin}">
		<c:set var="extraHeader"><pimsWidget:linkWithIcon url="/pims/View/${selectionExperiment.hook}" icon="types/small/experiment.gif" text="View selection experiment in PiMS" /></c:set>
	</c:if>
</c:otherwise></c:choose>
	
<pimsWidget:box title="Crystal selection" initialState="${initialState}" extraClasses="noscroll" extraHeader="${extraHeader}">
	<c:set var="formMode" value="view" />
	<c:if test="${empty crystal}"><c:set var="formMode" value="create" /></c:if>
    <pimsForm:formBlock>
    
   <!-- BEGIN left-hand side - image with crosshairs -->
    <div style="float:left:width:700px;">
        <div style="position:relative;width:700px;top:0;margin:0 10px;float:left;" id="imagecontainer">
        <img src="${image.url}" style="width:700px; cursor:${cursor}" id="dropimage"/>
        <img src="${image.url}" style="position:absolute;left:-10000px;top:0;" id="dropimagefullsize"/>

		<c:if test="${!isBeamline}">
        <p>Crystals in this drop:
		<c:set var="count" value="0" />
		<c:set var="color" value="blue" />
		<c:forEach var="prev" items="${coords}">
			<c:choose><c:when test="${empty crystal || crystal ne prev.crystalNumber}">
				&nbsp;<a href="${wellUrl}&crystal=${prev.crystalNumber}">${prev.crystalNumber}</a>
			</c:when><c:otherwise>
				&nbsp;<strong>${prev.crystalNumber}</strong>
			</c:otherwise></c:choose>
			<c:set var="count" value="${count+1}" />
		</c:forEach>
		<c:if test="${(!empty crystal && count eq 1) or (empty crystal && count eq 0)}">&nbsp;None</c:if>
        </p>
		</c:if>
		
        </div>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/xpims/crystalHandling.js"></script>
		<script type="text/javascript">
		
	
		var wellUrl="${wellUrl}";
		var selectingNew=true;
		var xtalNumber=1;
		var xScale=-1;
		var yScale=-1;
		var xOffset=0; //correct for position of image within parent
		var yOffset=0;
		
		//used in setXYScales() - pixel dimensions, and microns/pixel
		<c:choose><c:when test="${null eq image.width}">
		while($("dropimagefullsize").getWidth()<10){
			//wait for image to load
		}
		var imageWidth=$("dropimagefullsize").getWidth(); 
		var imageHeight=$("dropimagefullsize").getHeight();
		</c:when><c:otherwise>
			var imageWidth=${image.width}; 
			var imageHeight=${image.height};
		</c:otherwise></c:choose>
		var imageWidthPerPixel=${image.widthPerPixel};
		var imageHeightPerPixel=${image.heightPerPixel};
		
		var imgTopLeftCorner=$("dropimage").cumulativeOffset();
		var imgTop=imgTopLeftCorner.top;
		var imgLeft=imgTopLeftCorner.left;
		
		Event.observe(document, "mousemove", getCoords);
		Event.observe("dropimage","click",setCoords);
		setXYScales();
		</script>

		<!--  draw crosshairs -->
 		<c:set var="count" value="1" />
		<c:set var="color" value="blue" />
		<c:forEach var="prev" items="${coords}">
			<c:choose><c:when test="${empty crystal || crystal ne prev.crystalNumber}">
				<c:set var="color" value="blue" />
				<c:set var="border" value="1" />
			</c:when><c:otherwise>
				<c:set var="color" value="red" />
				<c:set var="border" value="2" />
			</c:otherwise></c:choose>
			<script type="text/javascript">
			var x=${prev.x}; 
			var y=${prev.y}; 
			var r=${prev.r}; 
			var xtalnum=${prev.crystalNumber}; 
			drawCrosshairs(x,y,r,xtalnum,"${color}",${border});
			</script>
			<c:set var="x" value="${prev.x}" />
			<c:set var="y" value="${prev.y}" />
			<c:set var="r" value="${prev.r}" />
			<c:if test="${crystal eq prev.crystalNumber}">
				<c:set var="currentx" value="${prev.x}" />
				<c:set var="currenty" value="${prev.y}" />
				<c:set var="currentr" value="${prev.r}" />
			</c:if>
			<c:set var="count" value="${count+1}" />
		</c:forEach>
		<script type="text/javascript">xtalNumber=${count};</script>
    </div>
   <!-- END left-hand side - image with crosshairs -->

   <!-- BEGIN right-hand side - form -->
    <div style="margin-left:750px;min-width:480px;overflow:hidden;">

    <pimsForm:form id="harvestForm" action="/update/SelectCrystal?barcode=${barcode}&well=${well}" mode="${formMode}" method="post">
    <input type="hidden" name="barcode" value="${barcode}" />
    <input type="hidden" name="plateBarcode" value="${barcode}" />
    <input type="hidden" name="well" value="${well}" />
	<c:if test="${isBeamline}">
	<input type="hidden" name="pin" value="PLATE"/>
	</c:if>

	<div id="nav" style="float:right">Nav</div>

		<c:if test="${not empty failedPinBarcode}">
			<p style="color:#600;border:2px solid #600;background-color:#fcc;text-align:center;font-weight:bold;">Pin ${failedPinBarcode} does not exist in PiMS</p>
		</c:if>

    	<c:choose><c:when test="${empty crystal}">
			<h2>Plate <a href="/xtalpims/ViewTrialDrops.jsp?barcode=${barcode}&well=${well}">${barcode}</a>, drop ${barcode}:${well}: Select crystal</h2>
			<p>Click on the image to mark the crystal.</p></p>
		</c:when><c:otherwise>
			<h2>Plate <a href="/xtalpims/ViewTrialDrops.jsp?barcode=${barcode}&well=${well}">${barcode}</a>, drop <a href="${wellUrl}">${barcode}:${well}</a>, crystal ${crystal}</h2>
		</c:otherwise></c:choose>

		<c:choose><c:when test="${not empty acronym}">
			<pimsForm:nonFormFieldInfo label="Project"><c:out value="${acronym}"/></pimsForm:nonFormFieldInfo>
			<input type="hidden" name="proteinAcronym" value="<c:out value="${acronym}"/>" />
		</c:when><c:otherwise>
			<pimsForm:nonFormFieldInfo label="Project">(None defined)</pimsForm:nonFormFieldInfo>
		</c:otherwise></c:choose>


		<c:choose><c:when test="${not empty crystal}">
			<c:set var="x" value="${currentx}" />
			<c:set var="y" value="${currenty}" />
			<c:set var="r" value="${currentr}" />
			<c:set var="selectingNew" value="false" />
			<c:set var="xtalNumber" value="${crystal}" />
			<c:set var="conditionsValue" value="${conditions}" />
			<c:set var="barcodeValue" value="${pinBarcode}" />
		</c:when><c:otherwise>
			<c:set var="x" value="" />
			<c:set var="y" value="" />
			<c:set var="r" value="100" />
			<c:set var="selectingNew" value="true" />
			<c:set var="xtalNumber" value="0" />
			<c:set var="conditionsValue" value="${conditions}" />
			<c:set var="barcodeValue" value="${failedPinBarcode}" />
		</c:otherwise></c:choose>

		<!-- Coordinates fields -->
        <pimsForm:nonFormFieldInfo label="Co-ordinates">
        	<div style="height:4em;position:relative">
				<div class="viewonly" style="position:absolute;top:0;left:0">        
	        	<strong>X</strong>&nbsp;${x}&#181;m
	        	&nbsp;&nbsp;&nbsp;&nbsp;<strong>Y</strong>&nbsp;${y}&#181;m
	        	&nbsp;&nbsp;&nbsp;&nbsp;<strong>Radius</strong>&nbsp;${r}&#181;m
				</div>
				<div class="editonly" style="position:absolute;top:0;left:0">        
	        	<strong>X</strong>&nbsp;<input type="text" id="x" name="x" value="${x}" style="width:4em;" onchange="onEdit();"/>&#181;m
	        	&nbsp;&nbsp;<strong>Y</strong>&nbsp;<input type="text" id="y" name="y" value="${y}" style="width:4em;" onchange="onEdit();"/>&#181;m
	        	&nbsp;&nbsp;<strong>Radius</strong>&nbsp;<input type="text" id="r" name="r" value="${r}" style="width:4em;" onchange="onEdit();"/>&#181;m
	        	<br/><div style="font-size:80%;padding:0.5em 0 1em 0">Click the image to set co-ordinates</div>
				</div>
			</div>
        </pimsForm:nonFormFieldInfo>
		<script type="text/javascript">
			selectingNew=${selectingNew};
			xtalNumber=${xtalNumber};
			updateCoordsOnChange();
			$("x").validation={required:true,wholeNumber:true,min:0,alias:"X co-ordinate"};
			$("y").validation={required:true,wholeNumber:true,min:0,alias:"Y co-ordinate"};
			$("r").validation={required:true,wholeNumber:true,min:0,alias:"Radius"};
			Event.observe("x","keyup",updateCoordsOnChange);
			Event.observe("y","keyup",updateCoordsOnChange);
			Event.observe("r","keyup",updateCoordsOnChange);
			$("x").up(".formitem").down(".fieldname").insert('<span class="required">*</span>');
		</script>
		
		<!-- Temperature -->
		<c:choose><c:when test="${empty temperature || !empty crystal}">
			<pimsForm:text name="temperature" alias="Temperature" value="${temperature}" helpText="Crystallization temperature" validation="numeric:true"/>
		</c:when><c:otherwise>
			<pimsForm:nonFormFieldInfo label="Temperature">${temperature}</pimsForm:nonFormFieldInfo>
			<input type="hidden" name="temperature" value="${temperature}" />
		</c:otherwise></c:choose>
		
		<!-- Conditions -->
		<c:choose><c:when test="${empty conditionsValue || !empty crystal}">
			<pimsForm:textarea name="conditions" alias="Conditions">${conditionsValue}</pimsForm:textarea>
		</c:when><c:otherwise>
			<pimsForm:nonFormFieldInfo label="Conditions">${conditionsValue}</pimsForm:nonFormFieldInfo>
			<input type="hidden" name="conditions" value="${conditionsValue}" />
		</c:otherwise></c:choose>

		<!-- Buffer -->
		<c:if test="${showBuffer}">
		<c:choose><c:when test="${empty buffer || !empty crystal}">
			<pimsForm:textarea name="buffer" alias="Protein buffer" helpText="Protein buffer and concentration">${buffer}</pimsForm:textarea>
		</c:when><c:otherwise>
			<pimsForm:nonFormFieldInfo label="Buffer">${buffer}</pimsForm:nonFormFieldInfo>
			<input type="hidden" name="buffer" value="${buffer}" />
		</c:otherwise></c:choose>
		</c:if>
	
		<!--  Lab notebook field -->
		<c:if test="${empty crystal}">
			<c:choose><c:when test="${!empty diamondPlateOwner}">
					<input type="hidden" name="_OWNER"  value="${diamondPlateOwner}"/>
			</c:when><c:when test="${!empty plateLabNotebook}">
					<input type="hidden" name="_OWNER"  value="${plateLabNotebook}"/>
			</c:when><c:when test="${fn:length(labNotebooks) eq 1}">
				<c:forEach var ="ln" items="${labNotebooks}">
					<input type="hidden" name="_OWNER"  value="${ln.hook}"/>
				</c:forEach>
			</c:when><c:otherwise>
				<pimsForm:select name="_OWNER" alias="Lab Notebook">
				<c:forEach var ="ln" items="${labNotebooks}">
					<option value="${ln.hook}">${ln.name}</option>
				</c:forEach>
				</pimsForm:select>
			</c:otherwise></c:choose>
		</c:if>

		<input type="hidden" name="pinbarcode" value="<c:out value="${pinBarcode}"/>" />

		<c:choose>
		<c:when test="${empty crystal}">
			<input type="hidden" name="crystalnumber" value="${count}" />
	        <input type="hidden" name="inputSample" value="${trialDropSample._Hook}" />

	        <c:choose><c:when test="${isBeamline}">
			    <pimsForm:submitButton buttonText="Add crystal to plate task list"/>
			    <br/><br/><br/><br/>
			    <div class="formfield" style="text-align:right">
			    <input style="background:black;color:white;cursor:pointer;border:none;padding:3px 15px" type="button" value="Queue plate to beamline..." onclick="document.location.href=contextPath+'/AssembleShipment/?plateshipment=yes&plateBarcode=${barcode}'" />
			    </div>
			    
			</c:when><c:otherwise>
			    <pimsForm:submitButton buttonText="Select crystal"/>
			</c:otherwise></c:choose>
			 
		    <script type="text/javascript">
		    /* See viewtrialdrops3.js - this version doesn't care about success or failure and just submits the parent form */
			function annotateDropAsCrystal(scheme, frm) {
				var ao, annotation, _img, url;
				var plateBarcode="${barcode}";
				var well="${well}";
				annotation = "Crystals";
				if ("" !== annotation) {
					url = contextPath + "/ScoreImageServlet?barcode=" + plateBarcode + "&well=" + well +  "&scheme=" + scheme + "&annotation=" + annotation;
					new Ajax.Request(url, {
						method: "POST",
						onSuccess: function (transport) { frm.submit(); },
						onFailure: function (transport) { frm.submit(); }
					});
				}
			}
			$("harvestForm").onsubmit="annotateDropAsCrystal('default',this)";
			</script>
		</c:when>
		<c:otherwise>
	        <input type="hidden" name="crystalnumber" value="${xtalNumber}" />
	        <input type="hidden" name="inputSample" value="${trialDropSample._Hook}" />
	        <input type="hidden" name="selectionExperiment" value="${selectionExperiment.hook}" />
	        <div style="display:none">
        	<pimsForm:date name="${selectionExperiment.hook}:startDate" alias="Treatment start date" value="${selectionExperiment.values['startDate']}" validation="required:true,date:true" />
			</div>
			<c:if test="${selectionExperiment.mayUpdate}">
		 		<pimsForm:editSubmit />
			</c:if>
		</c:otherwise>
		</c:choose>

    </pimsForm:form>
			
	<c:if test="${!empty crystal}">
		<hr/>
		<c:choose><c:when test="${empty sequence or empty acronym}">
	
			<h4 class="error">You must provide sequence and acronym below before you can ship this crystal.</h4>

		</c:when><c:when test="${isForPlateShipment}">

	
		</c:when><c:when test="${not empty finalMountExperiment}">

			<c:choose><c:when test="${'PLATE'==pinBarcode}">
				<c:set var="undoShippingLabel">Remove crystal from plate task list</c:set>
				<h4>Crystal added to plate task list<c:if test="${crystalShipped}">, plate shipped</c:if></h4>
			</c:when><c:otherwise>
				<c:set var="undoShippingLabel">Undo 'Ready for shipping'</c:set>
				<h4>Crystal mounted<c:if test="${crystalShipped}"> and shipped</c:if></h4>
				<pimsForm:form action="#" method="post" mode="view">
				<c:if test="${!fn:startsWith(pinBarcode, 'pin_')}">
					<pimsForm:nonFormFieldInfo label="Pin">
						<strong><c:out value="${pinBarcode}"/></strong><c:if test="${pinReused}"> (pin has been re-used since)</c:if>
					</pimsForm:nonFormFieldInfo>
				</c:if>
				<c:if test="${!empty finalmount_puckBarcode && !empty finalmount_positionInPuck}">
					<pimsForm:nonFormFieldInfo label="Puck">
						<strong><c:out value="${finalmount_puckBarcode}"/></strong>, position <strong><c:out value="${finalmount_positionInPuck}"/></strong>
					</pimsForm:nonFormFieldInfo>
				</c:if>
				</pimsForm:form>
			</c:otherwise></c:choose>
			<c:if test="${finalMountExperiment.mayDelete && !crystalShipped}">
				<form method="post" style="text-align:center;margin-bottom:1em" id="mountform"
					action="${pageContext.request.contextPath}/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}">
					<input type="hidden" name="hook" value="${finalMountExperiment.hook}" />
					<input type="hidden" name="hook" value="${finalMountSample.hook}" />
					<c:if test="${!empty preShipExperiment}">
						<input type="hidden" name="hook" value="${preShipExperiment.hook}" />
						<input type="hidden" name="hook" value="${preShipSample.hook}" />
					</c:if>
					<div style="text-align:right">
					<input type="submit" name="delete" value="${undoShippingLabel}" onclick="dontWarn()" />
					</div>
				</form>
			</c:if>
		
		</c:when><c:otherwise>
			<h4>In-situ diffraction</h4>
				<pimsForm:form mode="edit" method="post" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}" id="plateMountForm">
				<input type="hidden" name="inputSample" value="Crystal:${lastOutputSample}" />
				<input type="hidden" name="plateBarcode" value="${barcode}" />
				<input type="hidden" name="pin" value="PLATE" />
				<input type="hidden" name="puck" value="${barcode}" />
				<input type="hidden" name="well" value="${well}" />
				<input type="hidden" name="crystalNumber" value="${crystal}" />
				<input type="hidden" name="treatmentURL" value="" />
				<input type="hidden" name="ship" value="ship" />
				<div style="text-align:right">
				<input type="submit" class="shipbutton" name="shipbutton" value="Add to plate task list" onclick="return Treatment.submitMountForm(this)" />
				</div>
				</pimsForm:form>
			<hr/>

			<c:if test="${'false'==isBeamline}">

				<h4>Mount crystal on pin</h4>
				<c:set var="mountFormId" value="harvestmountform" />
				<pimsForm:form mode="edit" method="post" action="/update/CrystalTreatment/?barcode=${barcode}&well=${well}&crystal=${crystal}" id="${mountFormId}">
					<pimsForm:nonFormFieldInfo label="Pin barcode">
						<input type="text" value="" id="pin" name="pin" style="width:60%"/>
						<input type="button" value="Pin has no barcode" id="createdummypin" name="createdummypin" style="width:30%"/>
					</pimsForm:nonFormFieldInfo>
					<pimsForm:text name="puck" alias="Puck" value="${puckBarcode}" validation="" />
					<pimsForm:nonFormFieldInfo label="Position in puck"><div class="puckcontents">Scan a puck first<input type="hidden" id="puckPosition" name="puckPosition" value="" /></div></pimsForm:nonFormFieldInfo>
					<input type="hidden" name="inputSample" value="Crystal:${lastOutputSample}" />
					<input type="hidden" name="plateBarcode" value="${barcode}" />
					<input type="hidden" name="well" value="${well}" />
					<input type="hidden" name="crystalNumber" value="${crystal}" />
					<input type="hidden" name="treatmentURL" value="" />
					<input type="hidden" name="ship" value="ship" />
					<div style="text-align:right">
					<input type="submit" class="shipbutton" name="shipbutton" value="Ready to ship" onclick="return Treatment.submitMountForm(this)" />
					</div>
				</pimsForm:form>
				<hr/>
			</c:if>
	
		</c:otherwise></c:choose>
	</c:if>

	<!-- "Add notes" box -->
	<c:if test="${selectionExperiment.mayUpdate}">
        <div class="noprint" style="border:1px solid #600;margin-bottom:0.5em;text-align:center;font-weight:bold;color:#600;padding:0.5em 0;cursor:pointer" onclick="Treatment.goToNotes()">Click here to enter notes</div>
    </c:if>

   <!-- END right-hand side - form -->
    </div>
    </pimsForm:formBlock>

	<div style="clear:both;margin-top:10px;"></div>
	
</pimsWidget:box>