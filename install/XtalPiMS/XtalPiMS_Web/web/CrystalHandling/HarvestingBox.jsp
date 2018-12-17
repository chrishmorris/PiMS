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
<c:set var="extraHeader" value="<span class=\"cumulativeduration\" 
	style=\"color:#006;font-weight:bold;font-size:1.2em;float:right;margin-right:1em\">Elapsed time: 00:00:00</span>" />
<c:if test="${empty crystal}">
	<c:set var="initialState" value="fixed" />
	<c:set var="cursor" value="crosshair" />
	<c:set var="extraHeader" value="" />
</c:if>

<pimsWidget:box title="Crystal harvesting" initialState="${initialState}" extraClasses="noscroll" extraHeader="${extraHeader}">
	<c:set var="formMode" value="view" />
	<c:if test="${empty crystal}"><c:set var="formMode" value="create" /></c:if>
    <pimsForm:form id="tabsForm" action="/update/SelectCrystal?barcode=${barcode}&well=${well}" mode="${formMode}" method="post">
    <input type="hidden" name="barcode" value="${barcode}" />
    <input type="hidden" name="well" value="${well}" />
    <pimsForm:formBlock>
    <div style="float:left:width:700px;">
        <div style="position:relative;width:700px;top:0;margin:0 10px;float:left;" id="imagecontainer">
        <img src="${image.url}" style="width:700px; cursor:${cursor}" id="dropimage"/>
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
		var imageWidth=${image.width}; 
		var imageHeight=${image.height}; 
		var imageWidthPerPixel=${image.widthPerPixel};
		var imageHeightPerPixel=${image.heightPerPixel};
		
		var imgTopLeftCorner=$("dropimage").cumulativeOffset();
		var imgTop=imgTopLeftCorner.top;
		var imgLeft=imgTopLeftCorner.left;
		
		Event.observe(document, "mousemove", getCoords);
		Event.observe("dropimage","click",setCoords);
		setXYScales();
		</script>
    </div>
    <div style="margin-left:750px;min-width:480px">

		<c:if test="${not empty failedPinBarcode}">
			<p style="color:#600;border:2px solid #600;background-color:#fcc;text-align:center;font-weight:bold;">Pin ${failedPinBarcode} does not exist in PiMS</p>
		</c:if>

    	<c:choose><c:when test="${empty crystal}">
			<h2>Plate <a href="/xtalpims/ViewTrialDrops.jsp?barcode=${barcode}&well=${well}">${barcode}</a>, drop ${barcode}:${well}</h2>
		</c:when><c:otherwise>
			<h2>Plate <a href="/xtalpims/ViewTrialDrops.jsp?barcode=${barcode}&well=${well}">${barcode}</a>, drop <a href="${wellUrl}">${barcode}:${well}</a>, crystal ${crystal}</h2>
			<span class="noprint"><pimsWidget:linkWithIcon url="/pims/View/${selectionExperiment.hook}" icon="types/small/experiment.gif" text="View selection experiment in PiMS" /><br/><br/></span>
		</c:otherwise></c:choose>
		<c:choose><c:when test="${not empty project}">
			<strong>Project: </strong><pimsWidget:link bean="${project}" />
		</c:when><c:otherwise>
			<strong>Project: </strong> (None defined)
		</c:otherwise></c:choose>
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

		<c:choose><c:when test="${not empty crystal}">
			<c:set var="heading" value="Co-ordinates of this crystal:" />
			<c:set var="x" value="${currentx}" />
			<c:set var="y" value="${currenty}" />
			<c:set var="r" value="${currentr}" />
			<c:set var="selectingNew" value="false" />
			<c:set var="xtalNumber" value="${crystal}" />
			<c:set var="conditionsValue" value="${conditions}" />
			<c:set var="barcodeValue" value="${pinBarcode}" />
		</c:when><c:otherwise>
			<c:set var="heading" value="Click the image to select a new crystal:" />
			<c:set var="x" value="" />
			<c:set var="y" value="" />
			<c:set var="r" value="100" />
			<c:set var="selectingNew" value="true" />
			<c:set var="xtalNumber" value="0" />
			<c:set var="conditionsValue" value="${conditions}" />
			<c:set var="barcodeValue" value="${failedPinBarcode}" />
		</c:otherwise></c:choose>

        <p>${heading}</p>
        <pimsForm:text name="${selectionExperiment.hook}:pageNumber" alias="Page number" value="${selectionExperiment.values['pageNumber']}" helpText="Cross reference to paper lab note book" />
        <pimsForm:text name="x" alias="X co-ordinate" value="${x}" validation="required:true,wholeNumber:true,min:0" helpText="The X coordinate of the crystal (microns from left edge of image)"/>
        <pimsForm:text name="y" alias="Y co-ordinate" value="${y}" validation="required:true,wholeNumber:true,min:0" helpText="The Y coordinate of the crystal (microns from top edge of image)" />
        <pimsForm:text name="r" alias="Radius" value="${r}" validation="required:true,wholeNumber:true,min:0" helpText="The radius of a circle enclosing the crystal (microns)" />
		<script type="text/javascript">
			selectingNew=${selectingNew};
			xtalNumber=${xtalNumber};
			updateCoordsOnChange();
			Event.observe("x","keyup",updateCoordsOnChange);
			Event.observe("y","keyup",updateCoordsOnChange);
			Event.observe("r","keyup",updateCoordsOnChange);
		</script>
		<pimsForm:text name="temperature" alias="Temperature" value="${temperature}" helpText="Crystallization temperature" validation="numeric:true"/>
		<pimsForm:textarea name="conditions" alias="Conditions">${conditionsValue}</pimsForm:textarea>
		<c:if test="${showBuffer}">
			<pimsForm:textarea name="buffer" alias="Protein buffer" helpText="Protein buffer and concentration">${buffer}</pimsForm:textarea>
		</c:if>
		<pimsForm:textarea name="proteinsequence" alias="Protein sequence" validation="proteinSequence:true">XYZ${proteinSequence}</pimsForm:textarea>
		<pimsForm:text name="pinbarcode" alias="Pin barcode" value="${pinBarcode}" />

		<c:choose><c:when test="${not empty crystal}">
	        <c:if test="${pinReused}"> (Pin has been re-used since)</c:if>
	        <br/><br/>
	        <input type="hidden" name="crystalnumber" value="${xtalNumber}" />
	        <input type="hidden" name="inputSample" value="${trialDropSample._Hook}" />
	        <input type="hidden" name="selectionExperiment" value="${selectionExperiment.hook}" />
        	<pimsForm:date name="${selectionExperiment.hook}:startDate" alias="Treatment start date" value="${selectionExperiment.values['startDate']}" validation="required:true,date:true" />

			<c:if test="${selectionExperiment.mayUpdate}">
	 		<pimsForm:editSubmit />
	        </c:if>
	        
			<c:if test="${selectionExperiment.mayUpdate}">
		        <div class="noprint" style="border:1px solid #600;text-align:center;font-weight:bold;color:#600;padding:0.5em 0;cursor:pointer" onclick="goToNotes()">Click here to enter notes</div>
		        <script type="text/javascript">
		        var notesTextarea;
		        function goToNotes(){
		        	openCollapsibleBox("notes");
		        	var textareas=$("notes").select("textarea");
		        	notesTextarea=textareas.last();
		        	notesTextarea.focus();
		        	notesTextarea.scrollTo();
		        	notesTextarea.blu=255;
		        	window.setTimeout(highlightNotesTextarea,150);
		        }
		        function highlightNotesTextarea(){
		        	var blu=notesTextarea.blu;
		        	blu-=15;
		        	notesTextarea.blu=blu;
		        	notesTextarea.style.background="rgb(255,255,"+blu+")";
		        	if(blu>0){ 
		        		window.setTimeout(highlightNotesTextarea,10);
		        	}else{
		        		window.setTimeout(fadeNotesTextarea,100);
		        	}
		        }
		        function fadeNotesTextarea(){
		        	var blu=notesTextarea.blu;
		        	blu+=5;
		        	notesTextarea.blu=blu;
		        	notesTextarea.style.background="rgb(255,255,"+blu+")";
		        	if(blu<255){ window.setTimeout(fadeNotesTextarea,10); }
		        }
		        </script>
		    </c:if>
	        
	        <p>Other crystals from this drop:</p>
		</c:when><c:otherwise>
			<pimsForm:select name="_OWNER" alias="Lab Notebook">
			<c:forEach var ="ln" items="${labNotebooks}">
				<option value="${ln.hook}">${ln.name}</option>
			</c:forEach>
			</pimsForm:select>
	        <%-- <pimsForm:labNotebookField name="_OWNER" objects="${labNotebooks}"></pimsForm:labNotebookField> --%>
	        <input type="hidden" name="crystalnumber" value="${count}" />
	        <input type="hidden" name="inputSample" value="${trialDropSample._Hook}" />
			<!-- <input type="hidden" name="expBlueprintHook" value="" /> -->
		    <pimsForm:submitButton />
			<p>Previous crystals from this drop:</p>
		</c:otherwise></c:choose>
 		
	<c:set var="count" value="0" />
	<c:set var="color" value="blue" />
	<c:forEach var="prev" items="${coords}">
		<c:if test="${empty crystal || crystal ne prev.crystalNumber}">
			<a href="${wellUrl}&crystal=${prev.crystalNumber}">${barcode}:${well} Crystal ${prev.crystalNumber}</a><br/>
		</c:if>
		<c:set var="count" value="${count+1}" />
	</c:forEach>
	<c:if test="${(!empty crystal && count eq 1) or (empty crystal && count eq 0)}">(None)</c:if>

    </div>
    </pimsForm:formBlock>
    </pimsForm:form>

	
</pimsWidget:box>

<script type="text/javascript">
suppressSubmitOnEnter("pinbarcode");
</script>
