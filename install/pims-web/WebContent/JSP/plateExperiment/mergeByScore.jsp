<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pimsWidget" tagdir="/WEB-INF/tags/pimsWidget" %>
<%@ taglib prefix="pimsForm" tagdir="/WEB-INF/tags/pimsForm" %>
<c:catch var="error">

<jsp:include page="/JSP/core/Header.jsp">
    <jsp:param name="HeaderName" value="Copy from plates" />
    <jsp:param name="mayUpdate" value='${mayUpdate}' />
    <jsp:param name="isInModalWindow" value='${true}' />
    <jsp:param name="extraStylesheets" value='plates/plates,plates/scores' />
</jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/pims/plates4.4.0.js"></script>
<script type="text/javascript">
var groupHook = "${groupBean._Hook}";

var inputExperiments={
<c:forEach var="inputGroupBean" items="${inputGroupBeans}">
	<c:set var="plateExperimentBeans" scope="request" value="${inputGroupBean.experimentBeans}" />
          <jsp:include page="/JSP/json/PlateExperimentEditExperiments.jsp" />,
</c:forEach>
dummy:{}
}
<c:set var="plateExperimentBeans" scope="request" value="${outputGroupBean.experimentBeans}" />
var outputExperiments={
          <jsp:include page="/JSP/json/PlateExperimentEditExperiments.jsp" />
}
//end of experiments

</script>


<div style="position:relative;top:0;">
	<h3 style="margin:0;width:49.5%;float:left;text-align:center">Copy from group: ${inputGroupName}</h3>
	<h3 style="margin:0;width:49.5%;float:right;text-align:center">to group: ${outputGroupName}</h3>
	<div style="font-size:0;clear:both;">&nbsp;</div>
</div>

<div style="position:relative;top:0;">

	<div style="position:absolute;top:1%;left:1%;width:49%;height:26em;border-right:1px solid #009">&nbsp;</div>
	<div class="platescontainer copyfromplate" id="mergeinputgroups" >
	
	<c:forEach var="inputBean" items="${inputGroupBeans}" varStatus="status">
	
		<c:set var="rows" scope="request" value="${inputBean.holderRows}" />
		<c:set var="cols" scope="request" value="${inputBean.holderColumns}" />
		
		<div id="mergeinputgroup"> 
    		<c:if test="${!empty inputBean.northWestPlate}">
    			<div id="mergeinputplate_nw" >
        		<c:set var="plateCollection" value="${inputBean.northWestPlateLayout}" scope="request" />
        		<jsp:include page="tab_platesView_plate.jsp">
            		<jsp:param name="platePosition" value="northwest" />
            		<jsp:param name="plateName" value="${inputBean.northWestPlate.name}" />
            		<jsp:param name="plateHook" value="${inputBean.northWestPlate.hook}" />
            		<jsp:param name="noeditplatename" value="true" />
        		</jsp:include>
        		</div>
    		</c:if>

    		<c:if test="${!empty inputBean.northEastPlate}">
    			<div id="mergeinputplate_ne">
       			<c:set var="plateCollection" value="${inputBean.northEastPlateLayout}" scope="request" />
        		<jsp:include page="tab_platesView_plate.jsp">
            		<jsp:param name="platePosition" value="northeast" />
            		<jsp:param name="plateName" value="${inputBean.northEastPlate.name}" />
            		<jsp:param name="plateHook" value="${inputBean.northEastPlate.hook}" />
            		<jsp:param name="noeditplatename" value="true" />
        		</jsp:include>
        		</div>
    		</c:if>
    		
    		<div style="clear:both; font-size:0em;">&nbsp;</div>

    		<c:if test="${!empty inputBean.southWestPlate}">
    			<div id="mergeinputplate_sw">
        		<c:set var="plateCollection" value="${inputBean.southWestPlateLayout}" scope="request" />
        		<jsp:include page="tab_platesView_plate.jsp">
            		<jsp:param name="platePosition" value="southwest" />
            		<jsp:param name="plateName" value="${inputBean.southWestPlate.name}" />
            		<jsp:param name="plateHook" value="${inputBean.southWestPlate.hook}" />
            		<jsp:param name="noeditplatename" value="true" />
        		</jsp:include>
        		</div>
    		</c:if>

    		<c:if test="${!empty inputBean.southEastPlate}">
    			<div id="mergeinputplate_se">
        		<c:set var="plateCollection" value="${inputBean.southEastPlateLayout}" scope="request" />
        		<jsp:include page="tab_platesView_plate.jsp">
            		<jsp:param name="platePosition" value="southeast" />
            		<jsp:param name="plateName" value="${inputBean.southEastPlate.name}" />
            		<jsp:param name="plateHook" value="${inputBean.southEastPlate.hook}" />
            		<jsp:param name="noeditplatename" value="true" />
        		</jsp:include>
        		</div>
    		</c:if>
    		
    	
    	
    		<div id="mergeinputplate_icons">
    			<img id="mergeinputgroup_icon_up"
    				src="${pageContext.request.contextPath}/skins/default/images/icons/actions/moveup.gif" 
    			 	alt="Happy face" 
    			 	onclick="inputgroupMoveUp(this)" />
    			<img id="mergeinputgroup_icon_down"
    				src="${pageContext.request.contextPath}/skins/default/images/icons/actions/movedown.gif" 
    			 	alt="Sad face" 
    			 	onclick="inputgroupMoveDown(this)"/>
    		</div>
    	
    		<div id="mergeinputplate_separator">&nbsp; </div>
    	
    	</div>
		
	</c:forEach>
	
	</div>
</div>

<c:set var="rows" scope="request" value="${outputGroupBean.holderRows}" />
<c:set var="cols" scope="request" value="${outputGroupBean.holderColumns}" />
<div class="platescontainer copyfromplate" id="outputplates" style="position:absolute;top:1%;right:1%;width:48%;height:26em;">
    <c:if test="${!empty outputGroupBean.northWestPlate}">
        <c:set var="plateCollection" value="${outputGroupBean.northWestPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northwest" />
            <jsp:param name="plateName" value="${outputGroupBean.northWestPlate.name}" />
            <jsp:param name="plateHook" value="${outputGroupBean.northWestPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty outputGroupBean.northEastPlate}">
        <c:set var="plateCollection" value="${outputGroupBean.northEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northeast" />
            <jsp:param name="plateName" value="${outputGroupBean.northEastPlate.name}" />
            <jsp:param name="plateHook" value="${outputGroupBean.northEastPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty outputGroupBean.southWestPlate}">
        <c:set var="plateCollection" value="${outputGroupBean.southWestPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southwest" />
            <jsp:param name="plateName" value="${outputGroupBean.southWestPlate.name}" />
            <jsp:param name="plateHook" value="${outputGroupBean.southWestPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty outputGroupBean.southEastPlate}">
        <c:set var="plateCollection" value="${outputGroupBean.southEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southeast" />
            <jsp:param name="plateName" value="${outputGroupBean.southEastPlate.name}" />
            <jsp:param name="plateHook" value="${outputGroupBean.southEastPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>
</div>
</div>
<div style="position:relative;top:0;margin:26.5em 10px 0 10px;">
    <form action="#" style="text-align:right;">
        <input type="button" value="Cancel" onclick="cancelCopyFromPlate()" style="float:left;" />
        <input type="button" value="Apply Scores" onclick="applyWellScores()" style="float:left;" />
        Amount to copy: <input type="text" name="amount" class="amount" value="${measurement.displayValue}" /><c:choose><c:when test="${'uL'==refInputSample.displayUnit}">&#181;L</c:when><c:otherwise>${refInputSample.displayUnit}</c:otherwise></c:choose>

        <input type="hidden" value="${refInputSample.displayUnit}" name="displayUnit" class="displayunit" />
        <input type="button" value="Save" onclick="doCopyFromPlate(this)" />
    </form>
</div>


<script type="text/javascript">
$("content").style.padding="0";
refInputSample={ 
		name:"<c:out value="${refInputSample.name}"/>", 
		hook:"${refInputSample._Hook}", 
        amount:"${measurement.displayValue}",
		dbId:"${refInputSample.dbId}" , 
		displayUnit:"${refInputSample.displayUnit}" 
};
setUpPlates($("mergeinputgroups"),  inputExperiments,function(){},selectWellToMerge,function(){},function(){}
    function(){},selectWellToMerge,function(){}
);
setUpPlates($("outputplates"),outputExperiments,function(){},function(){},function(){},function(){}
   function(){},function(){},function(){}
);
showPlateScores($("mergeinputgroups"));
resetGroupList($("mergeinputgroups"));
$("outputplates").changedWells=[];
window.parent.document.getElementById("modalWindow_window_head").innerHTML="Merge "+refInputSample.name+" from plates by score";
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- note no footer -->
<jsp:include page="/JSP/core/footerJavascript.jsp" />
</div></div></body></html>
