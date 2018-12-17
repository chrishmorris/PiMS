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
var groupHook = "${group._Hook}";

<c:set var="plateExperimentBeans" scope="request" value="${inputGroupBean.experimentBeans}" />
var inputExperiments={
          <jsp:include page="/JSP/json/PlateExperimentEditExperiments.jsp" />
}
<c:set var="plateExperimentBeans" scope="request" value="${outputGroupBean.experimentBeans}" />
var outputExperiments={
          <jsp:include page="/JSP/json/PlateExperimentEditExperiments.jsp" />
}
</script>


<div style="position:relative;top:0;">
	<h3 style="margin:0;width:49.5%;float:left;text-align:center">Copy from group: ${inputGroupBean.name}</h3>
	<h3 style="margin:0;width:49.5%;float:right;text-align:center">to group: ${outputGroupBean.name}</h3>
	<div style="font-size:0;clear:both;">&nbsp;</div>
</div>

<div style="position:relative;top:0;">

<c:set var="rows" scope="request" value="${inputGroupBean.holderRows}" />
<c:set var="cols" scope="request" value="${inputGroupBean.holderColumns}" />
<div style="position:absolute;top:1%;left:1%;width:49%;height:26em;border-right:1px solid #009">&nbsp;</div>
<div class="platescontainer copyfromplate" id="inputplates" style="position:absolute;top:1%;left:1%;width:48%;height:26em;">
    <c:if test="${!empty inputGroupBean.northWestPlate}">
        <c:set var="plateCollection" value="${inputGroupBean.northWestPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northwest" />
            <jsp:param name="plateName" value="${inputGroupBean.northWestPlate.name}" />
            <jsp:param name="plateHook" value="${inputGroupBean.northWestPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty inputGroupBean.northEastPlate}">
        <c:set var="plateCollection" value="${inputGroupBean.northEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="northeast" />
            <jsp:param name="plateName" value="${inputGroupBean.northEastPlate.name}" />
            <jsp:param name="plateHook" value="${inputGroupBean.northEastPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty inputGroupBean.southWestPlate}">
        <c:set var="plateCollection" value="${inputGroupBean.southWestPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southwest" />
            <jsp:param name="plateName" value="${inputGroupBean.southWestPlate.name}" />
            <jsp:param name="plateHook" value="${inputGroupBean.southWestPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>

    <c:if test="${!empty inputGroupBean.southEastPlate}">
        <c:set var="plateCollection" value="${inputGroupBean.southEastPlateLayout}" scope="request" />
        <jsp:include page="tab_platesView_plate.jsp">
            <jsp:param name="platePosition" value="southeast" />
            <jsp:param name="plateName" value="${inputGroupBean.southEastPlate.name}" />
            <jsp:param name="plateHook" value="${inputGroupBean.southEastPlate.hook}" />
            <jsp:param name="noeditplatename" value="true" />
        </jsp:include>
    </c:if>
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
setUpPlates($("inputplates"),  inputExperiments,
		   startWellSelect,endWellSelect,updateWellSelect,hideWellContents
		   );
setUpPlates($("outputplates"),outputExperiments,
		destinationPlatesOnmousedown,destinationPlatesOnmouseup,destinationPlatesOnmouseover,destinationPlatesOnmouseout
		);
selectAllWells($("inputplates"));
$("outputplates").changedWells=[];
showStatusesInInputPlates();
showRefInputSamplesInOutputPlates();
copyFromPlateAddWellTitles();
window.parent.document.getElementById("modalWindow_window_head").innerHTML="Copy "+refInputSample.name+" from plates";
</script>

</c:catch><c:if test="${error != null}">"/>
    <p class="error">error ${error}</p>
</c:if>

<!-- note no footer -->
<jsp:include page="/JSP/core/footerJavascript.jsp" />
</div></div></body></html>
